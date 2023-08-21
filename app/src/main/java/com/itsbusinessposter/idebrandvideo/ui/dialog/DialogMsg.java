package com.itsbusinessposter.idebrandvideo.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.itsbusinessposter.idebrandvideo.Ads.NativeAdManager;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.items.SubsPlanItem;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class DialogMsg {

    public TextView msgTextView, titleTextView, descriptionTextView;
    public AppCompatButton oKButton, canceLButton, halfButton;

    private ImageView imageView;
    private Dialog dialog;
    private View view;
    private boolean cancelable;
    public RatingBar ratingBar;
    public FrameLayout flNative;
    public float newRating;
    private boolean attached = false;
    public Activity activity;

    public LottieAnimationView successAnim, errorAnim, warningAnim, confirmAnim;
    public Button okBtn, cancelBtn;


    public CardView cbRazorPay;
    public ProgressBar pbPayment;
    public ImageView ivPayCancel;


    public DialogMsg(Activity activity, Boolean cancelable) {
        this.activity = activity;
        this.dialog = new Dialog(activity);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.cancelable = cancelable;
    }

    private WindowManager.LayoutParams getLayoutParams(@NonNull Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
        }
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        return layoutParams;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    public EditText etCode;
    public TextView tv_plan_selected, tv_plan_name, tv_have_p, tv_error, tv_applied_code, tv_code, tv_code_dec, tv_total_payable,
            tv_price, tv_currency, tv_currency1, tv_original_price;
    public Button btn_apply;
    public RelativeLayout rlOpen;
    public ConstraintLayout csApplied;
    public int FINAL_PRICE;

    public void showPaymentDialog(SubsPlanItem item) {
        this.dialog.setContentView(R.layout.dialog_payment);
        cbRazorPay = dialog.findViewById(R.id.cv_pay_razorpay);
        pbPayment = dialog.findViewById(R.id.pb_payment);
        ivPayCancel = dialog.findViewById(R.id.iv_cancel);
        TextView title = (TextView) dialog.findViewById(R.id.tv1);
        TextView card = (TextView) dialog.findViewById(R.id.txt_card);
        tv_plan_selected = (TextView) dialog.findViewById(R.id.tv_plan_selected);
        tv_plan_name = (TextView) dialog.findViewById(R.id.tv_plan_name);
        tv_have_p = (TextView) dialog.findViewById(R.id.tv_have_p);
        tv_error = (TextView) dialog.findViewById(R.id.tv_error);
        tv_applied_code = (TextView) dialog.findViewById(R.id.tv_applied_code);
        tv_code = (TextView) dialog.findViewById(R.id.tv_code);
        tv_code_dec = (TextView) dialog.findViewById(R.id.tv_code_dec);
        tv_total_payable = (TextView) dialog.findViewById(R.id.tv_total_payable);
        tv_price = (TextView) dialog.findViewById(R.id.tv_price);
        tv_currency = (TextView) dialog.findViewById(R.id.tv_currency);
        tv_currency1 = (TextView) dialog.findViewById(R.id.tv_currency_1);
        tv_original_price = (TextView) dialog.findViewById(R.id.tv_ori_price);
        etCode = (EditText) dialog.findViewById(R.id.et_promo);
        btn_apply = (Button) dialog.findViewById(R.id.btn_apply);

        rlOpen = (RelativeLayout) dialog.findViewById(R.id.rl_open);
        csApplied = (ConstraintLayout) dialog.findViewById(R.id.cs_applied);

        GlideBinding.setTextSize(etCode, "edit_text");
        GlideBinding.setTextSize(btn_apply, "button_text_12");

        GlideBinding.setTextSize(tv_plan_selected, "font_body_size");
        GlideBinding.setTextSize(tv_plan_name, "font_title_size");
        GlideBinding.setTextSize(tv_have_p, "font_body_size");
        GlideBinding.setTextSize(tv_error, "font_body_s_size");
        GlideBinding.setTextSize(tv_applied_code, "font_body_size");
        GlideBinding.setTextSize(tv_code, "font_body_size");
        GlideBinding.setTextSize(tv_code_dec, "font_body_size");
        GlideBinding.setTextSize(tv_total_payable, "font_h7_size");
        GlideBinding.setTextSize(tv_price, "font_h6_size");
        GlideBinding.setTextSize(tv_original_price, "font_h7_size");
        GlideBinding.setTextSize(tv_currency, "font_h6_size");
        GlideBinding.setTextSize(tv_currency1, "font_title_size");
        GlideBinding.setTextSize(card, "font_body_size");
        GlideBinding.setTextSize(title, "font_title_size");

        GlideBinding.setFont(etCode, "bold");
        GlideBinding.setFont(btn_apply, "medium");

        GlideBinding.setFont(tv_plan_selected, "normal");
        GlideBinding.setFont(tv_plan_name, "extra_bold");
        GlideBinding.setFont(tv_have_p, "bold");
        GlideBinding.setFont(tv_error, "normal");
        GlideBinding.setFont(tv_applied_code, "normal");
        GlideBinding.setFont(tv_code, "extra_bold");
        GlideBinding.setFont(tv_code_dec, "normal");
        GlideBinding.setFont(tv_total_payable, "extra_bold");
        GlideBinding.setFont(tv_price, "normal");
        GlideBinding.setFont(tv_original_price, "normal");
        GlideBinding.setFont(tv_currency, "normal");
        GlideBinding.setFont(tv_currency1, "normal");
        GlideBinding.setFont(card, "extra_bold");
        GlideBinding.setFont(title, "extra_bold");

        tv_plan_name.setText(item.planName);
        tv_original_price.setText(item.planPrice);
        tv_price.setText(item.planPrice);

        double price = Double.parseDouble(item.planPrice);

        FINAL_PRICE = (int) price;

        rlOpen.setVisibility(View.VISIBLE);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
        }
    }

    public void showAppInfoDialog(String updateText, String cancelText, String title, String description) {
        this.dialog.setContentView(R.layout.dialog_app_info);
        descriptionTextView = dialog.findViewById(R.id.descriptionTextView);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);

        descriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        msgTextView = dialog.findViewById(R.id.titleTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

        msgTextView.setText(title);
        descriptionTextView.setText(description);
        okBtn.setText(updateText);
        cancelBtn.setText(cancelText);

        GlideBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideBinding.setTextSize(descriptionTextView, "font_body_xs_size");
        GlideBinding.setTextSize(cancelBtn, "button_text_12");
        GlideBinding.setTextSize(okBtn, "button_text_12");

        GlideBinding.setFont(descriptionTextView, "bold");
        GlideBinding.setFont(msgTextView, "extra_bold");
        GlideBinding.setFont(cancelBtn, "medium");
        GlideBinding.setFont(okBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));
            dialog.setCancelable(cancelable);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public void showSuccessDialog(String message, String okTitle) {
        this.dialog.setContentView(R.layout.dialog_message);
        successAnim = dialog.findViewById(R.id.success_animation);
        successAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);

        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

        okBtn.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.green_A700));

        titleTextView.setText(dialog.getContext().getString(R.string.success));
        msgTextView.setText(message);
        okBtn.setText(okTitle);

        GlideBinding.setTextSize(titleTextView, "font_body_size");
        GlideBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideBinding.setTextSize(okBtn, "button_text_12");

        GlideBinding.setFont(titleTextView, "extra_bold");
        GlideBinding.setFont(msgTextView, "bold");
        GlideBinding.setFont(okBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public void showErrorDialog(String message, String okTitle) {
        this.dialog.setContentView(R.layout.dialog_message);
        errorAnim = dialog.findViewById(R.id.error_animation);
        errorAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);

        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

        okBtn.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.red_A700));

        titleTextView.setText(dialog.getContext().getString(R.string.error));
        msgTextView.setText(message);
        okBtn.setText(okTitle);

        GlideBinding.setTextSize(titleTextView, "font_body_size");
        GlideBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideBinding.setTextSize(okBtn, "button_text_12");

        GlideBinding.setFont(titleTextView, "extra_bold");
        GlideBinding.setFont(msgTextView, "bold");
        GlideBinding.setFont(okBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            this.dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            this.dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public void showWarningDialog(String title, String message, String okTitle, boolean cancelable) {
        this.dialog.setContentView(R.layout.dialog_message);
        warningAnim = dialog.findViewById(R.id.warn_animation);
        warningAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);

        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);

        flNative = dialog.findViewById(R.id.rl_native_ad);
        NativeAdManager.showAds(dialog.getContext(), flNative);

        if (cancelable) {
            cancelBtn.setVisibility(View.VISIBLE);
        }

        okBtn.setBackgroundColor(dialog.getContext().getResources().getColor(R.color.amber_800));

        titleTextView.setText(title);
        msgTextView.setText(message);
        okBtn.setText(okTitle);

        GlideBinding.setTextSize(titleTextView, "font_body_size");
        GlideBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideBinding.setTextSize(okBtn, "button_text_12");
        GlideBinding.setTextSize(cancelBtn, "button_text_12");

        GlideBinding.setFont(titleTextView, "extra_bold");
        GlideBinding.setFont(msgTextView, "bold");
        GlideBinding.setFont(okBtn, "medium");
        GlideBinding.setFont(cancelBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
            cancelBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void showConfirmDialog(String title, String message, String okTitle, String cancelTitle) {
        this.dialog.setContentView(R.layout.dialog_message);
        confirmAnim = dialog.findViewById(R.id.confirm_animation);
        confirmAnim.setVisibility(View.VISIBLE);
        titleTextView = dialog.findViewById(R.id.dialogTitleTextView);
        cancelBtn = dialog.findViewById(R.id.dialogCancelButton);
        cancelBtn.setVisibility(View.VISIBLE);
        msgTextView = dialog.findViewById(R.id.dialogMessageTextView);
        okBtn = dialog.findViewById(R.id.dialogOkButton);

        titleTextView.setText(title);
        titleTextView.setAllCaps(true);
        msgTextView.setText(message);
        okBtn.setText(okTitle);
        cancelBtn.setText(cancelTitle);

        GlideBinding.setTextSize(titleTextView, "font_body_size");
        GlideBinding.setTextSize(msgTextView, "font_body_s_size");
        GlideBinding.setTextSize(okBtn, "button_text_12");
        GlideBinding.setTextSize(cancelBtn, "button_text_12");

        GlideBinding.setFont(titleTextView, "extra_bold");
        GlideBinding.setFont(msgTextView, "bold");
        GlideBinding.setFont(okBtn, "medium");
        GlideBinding.setFont(cancelBtn, "medium");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
            okBtn.setOnClickListener(view -> DialogMsg.this.cancel());
            cancelBtn.setOnClickListener(view -> DialogMsg.this.cancel());
        }
    }


    public EditText etPhone;
    public CountryCodePicker countryCodePicker;
    public RelativeLayout confirm_phone_number;

    public void showEnterNumberDialog() {
        this.dialog.setContentView(R.layout.login_enter_number);
        etPhone = dialog.findViewById(R.id.edit_text_phone_number_login_acitivty);
        countryCodePicker = dialog.findViewById(R.id.CountryCodePicker);
        confirm_phone_number = dialog.findViewById(R.id.relative_layout_confirm_phone_number);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(true);
        }
    }

    public ImageView ivReal, ivRemove;
    public Button btnReal, btnRemove, btnNo, btnManual;
    public LinearLayout lvRemove;
    public LottieAnimationView scanAnimation;

    public void showRemoveBGDialog() {
        this.dialog.setContentView(R.layout.dialog_remove_bg);

        ivReal = dialog.findViewById(R.id.iv_real);
        ivRemove = dialog.findViewById(R.id.iv_remove);
        btnReal = dialog.findViewById(R.id.btn_real);
        btnRemove = dialog.findViewById(R.id.btn_remove);
        btnNo = dialog.findViewById(R.id.btn_bo);
        btnManual = dialog.findViewById(R.id.btn_manual);
        lvRemove = dialog.findViewById(R.id.lv_remove);
        scanAnimation = dialog.findViewById(R.id.animation_view);

        btnReal.setText("Remove BG");

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
        }
    }

    public ImageView ivCancel;
    public RoundedImageView ivOffer;
    public CardView cvOffer;

    public void showOfferDialog(String imageUrl) {
        this.dialog.setContentView(R.layout.offer_dialog);

        ivOffer = dialog.findViewById(R.id.iv_offer);
        ivCancel = dialog.findViewById(R.id.iv_cancel);
        cvOffer = dialog.findViewById(R.id.cardView7);

        GlideBinding.bindImage(ivOffer, imageUrl);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setAttributes(getLayoutParams(dialog));

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCancelable(cancelable);
        }
    }

}
