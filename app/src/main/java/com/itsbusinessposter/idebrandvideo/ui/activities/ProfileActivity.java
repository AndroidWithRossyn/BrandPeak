package com.itsbusinessposter.idebrandvideo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itsbusinessposter.idebrandvideo.Ads.BannerAdManager;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityProfileBinding;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.items.UserLogin;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.utils.Connectivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    UserViewModel userViewModel;
    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mm:ss aa", Locale.getDefault());
    SimpleDateFormat df2 = new SimpleDateFormat("dd MMM, yy", Locale.ENGLISH);
    SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    UserItem userItem;
    PrefManager prefManager;
    Connectivity connectivity;
    ProgressDialog prgDialog;
    DialogMsg dialogMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        connectivity = new Connectivity(this);
        dialogMsg = new DialogMsg(this, false);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getString(R.string.login_loading));
        prgDialog.setCancelable(false);

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
        setUpViewModel();

    }

    private void setUpViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUserDataById().observe(this, listResource -> {
            if (listResource != null) {
                Util.showLog("Got Data "
                        + listResource.message +
                        listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {
                            userItem = listResource.data;
                            setData(new UserLogin(listResource.data.userId, true, listResource.data));
                        }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            userItem = listResource.data;
                            setData(new UserLogin(listResource.data.userId, true, listResource.data));
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

    private void setData(UserLogin data) {
        binding.setUserData(data.user);
        if (data.user.isSubscribed) {
            try {
                Date startDate = df3.parse(data.user.planStartDate);
                Date endDate = df3.parse(data.user.planEndDate);
                binding.progressBar.setProgress(printDifference(startDate, endDate));
            } catch (ParseException e) {
                e.printStackTrace();
                Util.showErrorLog(e.getMessage(), e);
            }
        }
    }

    private void setUpUi() {
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_profile));

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.ivEdit.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileEditActivity.class));
        });
        binding.rlAccountDetail.setOnClickListener(v -> {
            if (userItem != null) {
                showAccountDialog();
            }
        });

        binding.rlChangePassword.setOnClickListener(v -> {
            showChangePasswordDialog();
        });

        binding.rlPrivacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.PRIVACY_POLICY);
            startActivity(intent);
        });

        binding.rlTerm.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.TERM_CONDITION);
            startActivity(intent);
        });

        binding.rlRefundPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.REFUND_POLICY);
            startActivity(intent);
        });

        binding.btnSubsUpgrade.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, SubsPlanActivity.class));
        });

        binding.btnSubsPurchase.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, SubsPlanActivity.class));
        });
    }

    private void showChangePasswordDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_change_password);
        dialog.setCancelable(false);

        TextView tv_title = (TextView) dialog.findViewById(R.id.save_title);
        TextView tv_title_new = (TextView) dialog.findViewById(R.id.tv_title_new);
        TextView tv_title_confirm = (TextView) dialog.findViewById(R.id.tv_title_confirm);

        EditText etNew = (EditText) dialog.findViewById(R.id.etNew);
        EditText etConfirm = (EditText) dialog.findViewById(R.id.etConfirm);

        Button btn_change = (Button) dialog.findViewById(R.id.btn_change);

        GlideBinding.setTextSize(tv_title_new, "font_body_s_size");
        GlideBinding.setTextSize(tv_title_confirm, "font_body_s_size");
        GlideBinding.setTextSize(etNew, "font_body_s_size");
        GlideBinding.setTextSize(etConfirm, "font_body_s_size");
        GlideBinding.setTextSize(tv_title, "font_body_size");

        GlideBinding.setFont(tv_title, "extra_bold");
        GlideBinding.setFont(tv_title_new, "bold");
        GlideBinding.setFont(tv_title_confirm, "bold");
        GlideBinding.setFont(etNew, "normal");
        GlideBinding.setFont(etConfirm, "normal");

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_change.setOnClickListener(v -> {
            if (!connectivity.isConnected()) {
                Util.showToast(this, getString(R.string.error_message__no_internet));
                return;
            }
            String password = etNew.getText().toString().trim();
            String confirmPassword = etConfirm.getText().toString().trim();
            if (password.equals("") || confirmPassword.equals("")) {
                Util.showToast(this, getString(R.string.hint_password));
                return;
            }

            if (!(password.equals(confirmPassword))) {
                Util.showToast(this, getString(R.string.pass_not_match));
                return;
            }

            userViewModel.passwordUpdate(prefManager.getString(Constant.USER_ID), password).observe(this,
                    listResource -> {

                        if (listResource != null) {

                            Util.showLog("Got Data" + listResource.message + listResource.toString());

                            switch (listResource.status) {
                                case LOADING:
                                    // Loading State
                                    // Data are from Local DB

                                    prgDialog.show();
//                                    updateForgotBtnStatus();

                                    break;
                                case SUCCESS:
                                    // Success State
                                    // Data are from Server

                                    if (listResource.data != null) {

                                        prefManager.setString(Constant.USER_PASSWORD, etNew.getText().toString());
                                        prgDialog.cancel();
                                        dialogMsg.showSuccessDialog(listResource.data.message, getString(R.string.ok));
                                        dialogMsg.show();
                                        dialog.dismiss();
//                                        updateForgotBtnStatus();
                                    }

                                    break;
                                case ERROR:
                                    // Error State

                                    dialogMsg.showErrorDialog(listResource.message, getString(R.string.ok));
                                    dialogMsg.show();
                                    userViewModel.isLoading = false;
                                    dialog.dismiss();
                                    prgDialog.cancel();

                                    break;
                                default:

                                    break;
                            }

                        } else {

                            // Init Object or Empty Data
                            Util.showLog("Empty Data");

                        }
                    });

        });

        dialog.show();
    }

    private void showAccountDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_account_details);
        dialog.setCancelable(false);

        TextView tv_title = (TextView) dialog.findViewById(R.id.save_title);

        TextView tv_title_name = (TextView) dialog.findViewById(R.id.textView8);
        TextView tv_title_email = (TextView) dialog.findViewById(R.id.textView9);
        TextView tv_title_phone = (TextView) dialog.findViewById(R.id.textView10);

        TextView tv_name = (TextView) dialog.findViewById(R.id.text_name);
        TextView tv_email = (TextView) dialog.findViewById(R.id.text_email);
        TextView tv_phone = (TextView) dialog.findViewById(R.id.text_phone);

        GlideBinding.setTextSize(tv_title_name, "font_body_size");
        GlideBinding.setTextSize(tv_title_email, "font_body_size");
        GlideBinding.setTextSize(tv_title_phone, "font_body_size");
        GlideBinding.setTextSize(tv_title, "font_body_size");

        GlideBinding.setFont(tv_title_name, "extra_bold");
        GlideBinding.setFont(tv_title_email, "extra_bold");
        GlideBinding.setFont(tv_title_phone, "extra_bold");
        GlideBinding.setFont(tv_title, "extra_bold");

        GlideBinding.setTextSize(tv_name, "font_body_s_size");
        GlideBinding.setTextSize(tv_email, "font_body_s_size");
        GlideBinding.setTextSize(tv_phone, "font_body_s_size");

        GlideBinding.setFont(tv_name, "bold");
        GlideBinding.setFont(tv_email, "bold");
        GlideBinding.setFont(tv_phone, "bold");

        tv_name.setText(userItem.userName);
        tv_phone.setText(userItem.phone);
        tv_email.setText(userItem.email);

        dialog.findViewById(R.id.iv_close).setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.findViewById(R.id.btn_sign_out).setOnClickListener(v -> {
            dialog.dismiss();
            DialogMsg dialogMsg = new DialogMsg(this, false);
            dialogMsg.showConfirmDialog(getString(R.string.menu_logout), getString(R.string.message__want_to_logout),
                    getString(R.string.message__logout),
                    getString(R.string.message__cancel_close));
            dialogMsg.show();

            dialogMsg.okBtn.setOnClickListener(view -> {

                dialogMsg.cancel();

                if (userItem != null) {
                    userViewModel.deleteUserLogin(userItem).observe(this, status -> {
                        if (status != null) {

                            Util.showLog("User is Status : " + status);

                            prefManager.setBoolean(Constant.IS_LOGIN, false);
                            prefManager.remove(Constant.USER_ID);
                            prefManager.remove(Constant.USER_EMAIL);
                            prefManager.remove(Constant.USER_PASSWORD);

                            userItem = null;

                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .build();
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                            googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                        }
                    });

                    Util.showLog("nav_logout_login");
                }
            });

            dialogMsg.cancelBtn.setOnClickListener(view -> dialogMsg.cancel());
        });

        dialog.show();
    }

    public int printDifference(Date startDate, Date endDate) {
        //milliseconds
        long totalDuration = endDate.getTime() - startDate.getTime();
        Date currentTime = new Date();
        long currentDuration = currentTime.getTime() - startDate.getTime();

        Util.showLog("startDate : " + startDate);
        Util.showLog("endDate : " + endDate);
        Util.showLog("different : " + totalDuration);
        Util.showLog("Current different : " + currentDuration);

        final double percentage = currentDuration / ((double) totalDuration);
        int finalPr = (int) (percentage * 100);

        Util.showLog("Final Pr : " + finalPr);
        return finalPr;
    }
}