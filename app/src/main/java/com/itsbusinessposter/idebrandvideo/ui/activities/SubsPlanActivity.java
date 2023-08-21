package com.itsbusinessposter.idebrandvideo.ui.activities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.itsbusinessposter.idebrandvideo.Ads.BannerAdManager;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.adapters.SubsPlanAdapter;
import com.itsbusinessposter.idebrandvideo.api.ApiClient;
import com.itsbusinessposter.idebrandvideo.api.ApiResponse;
import com.itsbusinessposter.idebrandvideo.databinding.ActivitySubsPlanBinding;
import com.itsbusinessposter.idebrandvideo.items.CouponItem;
import com.itsbusinessposter.idebrandvideo.items.SubsPlanItem;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.utils.Connectivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.SubsPlanViewModel;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class SubsPlanActivity extends AppCompatActivity implements PaymentResultListener {

    ActivitySubsPlanBinding binding;
    SubsPlanViewModel subPlanViewModel;
    SubsPlanAdapter subsPlanAdapter;
    DialogMsg dialogMsg;
    Connectivity connectivity;
    UserViewModel userViewModel;
    PrefManager prefManager;
    UserItem userItem;
    String planId = "";
    String planPrice = "";
    String couponCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubsPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogMsg = new DialogMsg(this, false);
        connectivity = new Connectivity(this);
        prefManager = new PrefManager(this);

        Checkout.preload(getApplicationContext());

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        setUpViewModel();
    }

    private void setUpViewModel() {
        subPlanViewModel = new ViewModelProvider(this).get(SubsPlanViewModel.class);
        subPlanViewModel.getSubsPlanItems().observe(this, listResource -> {
            if (listResource != null) {

                Util.showLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {
                            setData(listResource.data);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            setData(listResource.data);
//                                        updateForgotBtnStatus();
                        }

                        break;
                    case ERROR:
                        // Error State

                        dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                        dialogMsg.show();

                        break;
                    default:

                        break;
                }

            } else {

                // Init Object or Empty Data
                Util.showLog("Empty Data");

            }
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getDbUserData(prefManager.getString(Constant.USER_ID)).observe(this, item -> {
            if (item != null) {
                userItem = item.user;
            }
        });
    }

    private void setData(List<SubsPlanItem> data) {
        subsPlanAdapter.subsPlanItemList(data);
        binding.shimmerViewContainer.stopShimmer();
        binding.shimmerViewContainer.setVisibility(GONE);
        binding.rvSubsplan.setVisibility(View.VISIBLE);
    }

    private void setUpUi() {
        binding.toolbar.toolName.setText(getResources().getString(R.string.subscribe));
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });
        subsPlanAdapter = new SubsPlanAdapter(this, item -> {

            planId = item.id;
            planPrice = item.planPrice;
            if (!connectivity.isConnected()) {
                Util.showToast(SubsPlanActivity.this, getResources().getString(R.string.error_message__no_internet));
                return;
            }

            if (!prefManager.getBoolean(Constant.IS_LOGIN)) {
                dialogMsg.showWarningDialog(getString(R.string.login_login), getString(R.string.login_first_login), getString(R.string.login_login), false);
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(v -> {
                    startActivity(new Intent(SubsPlanActivity.this, LoginActivity.class));
                });
                return;
            }

            dialogMsg.showPaymentDialog(item);
            dialogMsg.show();

            dialogMsg.cbRazorPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogMsg.cbRazorPay.setVisibility(GONE);
                    dialogMsg.pbPayment.setVisibility(View.VISIBLE);
                    startPayment(dialogMsg.FINAL_PRICE, prefManager.getString(Constant.RAZORPAY_KEY_ID));
                }
            });

            dialogMsg.ivPayCancel.setOnClickListener(v -> {
                dialogMsg.cancel();
            });

            dialogMsg.btn_apply.setOnClickListener(v -> {

                if (dialogMsg.etCode.getText().toString().equals("")) {
                    Util.showToast(SubsPlanActivity.this, "Enter Code");
                    return;
                }

                dialogMsg.btn_apply.setEnabled(false);
                dialogMsg.btn_apply.setText("Checking...");
                dialogMsg.cbRazorPay.setEnabled(false);

                checkCoupon(userItem.userId, dialogMsg.etCode.getText().toString());
            });

        });
        binding.rvSubsplan.setAdapter(subsPlanAdapter);
    }

    private void checkCoupon(String userId, String couponCode) {

                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());

                executor.execute(() -> {
                    //Background work here
                    try {

                        // Call the API Service
                        Response<CouponItem> response = ApiClient.getApiService().checkCoupon(prefManager.getString(Constant.api_key),
                                userId,
                                couponCode).execute();


                        // Wrap with APIResponse Class
                        ApiResponse<CouponItem> apiResponse = new ApiResponse<>(response);

                        // If response is successful
                        if (apiResponse.isSuccessful()) {

                            Util.showLog("" + apiResponse.body);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    double discountPrice = Double.parseDouble(apiResponse.body.discount) * dialogMsg.FINAL_PRICE / 100;

                                    double price = dialogMsg.FINAL_PRICE - discountPrice;

                                    dialogMsg.tv_price.setText("" + price);

                                    dialogMsg.FINAL_PRICE = (int) price;

                                    planPrice = String.valueOf(dialogMsg.FINAL_PRICE);

                                    dialogMsg.rlOpen.setVisibility(GONE);
                                    dialogMsg.csApplied.setVisibility(View.VISIBLE);

                                    dialogMsg.tv_code.setText(dialogMsg.etCode.getText().toString());
                                    dialogMsg.tv_code_dec.setText(apiResponse.body.discount + "% " + getString(R.string.discount_on)
                                            + " " + dialogMsg.tv_plan_name.getText());
                                    dialogMsg.btn_apply.setEnabled(true);
                                    dialogMsg.cbRazorPay.setEnabled(true);
                                }});

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Util.showLog("EEE: " + apiResponse.errorMessage);
                                    dialogMsg.tv_error.setText(apiResponse.errorMessage);
                                    dialogMsg.tv_error.setVisibility(View.VISIBLE);

                                    dialogMsg.btn_apply.setEnabled(true);
                                    dialogMsg.cbRazorPay.setEnabled(true);
                                    dialogMsg.btn_apply.setText(getString(R.string.apply));
                                }});
                        }

                    } catch (IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Util.showLog("EEE: " + "Coupon Code Not Valid");
                                dialogMsg.tv_error.setText("Coupon Code Not Valid");
                                dialogMsg.tv_error.setVisibility(View.VISIBLE);

                                dialogMsg.btn_apply.setEnabled(true);
                                dialogMsg.cbRazorPay.setEnabled(true);
                                dialogMsg.btn_apply.setText(getString(R.string.apply));
                            }});
                    }
                    handler.post(() -> {
                        //UI Thread work here

                    });
                });

    }

    private void startPayment(int planPrice, String key) {
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID(key);

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.login_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", userItem.userName);
            options.put("description", "Charge Of Plan");
            options.put("theme.color", "#f59614");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            options.put("currency", "INR");
            options.put("amount", (float) planPrice * 100);//pass amount in currency subunits
            options.put("prefill.email", userItem.email);
            if (userItem.phone != null && !userItem.phone.equals("")) {
                options.put("prefill.contact", userItem.phone);
            }
            checkout.open(activity, options);

        } catch (Exception e) {
            Util.showErrorLog("Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        subPlanViewModel.loadPayment(prefManager.getString(Constant.USER_ID), planId, paymentId, String.valueOf(dialogMsg.FINAL_PRICE), dialogMsg.tv_code.getText().toString()).observe(this,
                result -> {
                    if (result != null) {
                        switch (result.status) {
                            case SUCCESS:
                                dialogMsg.pbPayment.setVisibility(GONE);
                                dialogMsg.cancel();

                                dialogMsg.showSuccessDialog(result.data.message, getString(R.string.ok));
                                dialogMsg.show();
                                dialogMsg.okBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogMsg.cancel();
                                        userViewModel.getUserDataById().observe(SubsPlanActivity.this, listResource -> {
                                            if (listResource != null) {
                                                Util.showLog("Got Data "
                                                        + listResource.message +
                                                        listResource.toString());

                                                switch (listResource.status) {
                                                    case LOADING:
                                                        // Loading State
                                                        // Data are from Local DB

                                                        break;
                                                    case SUCCESS:
                                                        // Success State
                                                        // Data are from Server

                                                        if (listResource.data != null) {
                                                            userItem = listResource.data;
                                                            Constant.IS_SUBSCRIBED = listResource.data.isSubscribed;
                                                            onBackPressed();
                                                        }

                                                        break;
                                                    case ERROR:
                                                        // Error State

                                                        Util.showLog("Error: " + listResource.message);

                                                        break;
                                                    default:
                                                        // Default
                                                        break;
                                                }

                                            } else {

                                                // Init Object or Empty Data
                                                Util.showLog("Empty Data");

                                            }
                                        });
                                        userViewModel.setUserById(prefManager.getString(Constant.USER_ID));
                                    }
                                });
                                break;

                            case ERROR:
                                dialogMsg.pbPayment.setVisibility(GONE);
                                dialogMsg.cancel();

                                dialogMsg.showErrorDialog(result.message, getString(R.string.ok));
                                dialogMsg.show();
                                break;
                        }
                    }

                });
    }

    @Override
    public void onPaymentError(int i, String s) {
        dialogMsg.cancel();

        dialogMsg.showErrorDialog(s, getString(R.string.ok));
        dialogMsg.show();

    }
}