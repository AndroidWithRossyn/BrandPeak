package com.itsbusinessposter.idebrandvideo.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;

import com.itsbusinessposter.idebrandvideo.Ads.BannerAdManager;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityPrivacyBinding;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;

public class PrivacyActivity extends AppCompatActivity {

    ActivityPrivacyBinding binding;
    String type, privacy;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);

        BannerAdManager.showBannerAds(this, binding.llAdview);
        if (getIntent().getExtras() != null) {

            type = getIntent().getExtras().getString("type");

            if (type.equals(Constant.PRIVACY_POLICY)) {
                privacy = prefManager.getString(Constant.PRIVACY_POLICY);
                binding.toolbar.toolName.setText(getResources().getString(R.string.menu_privacy_policy));
            } else if (type.equals(Constant.TERM_CONDITION)) {
                privacy = prefManager.getString(Constant.TERM_CONDITION);
                binding.toolbar.toolName.setText(getResources().getString(R.string.terms_and_service));
            } else {
                privacy = prefManager.getString(Constant.REFUND_POLICY);
                binding.toolbar.toolName.setText(getResources().getString(R.string.refund_policy));
            }

            setData();
        }
    }

    private void setData() {
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.wvPrivacy.getSettings().setJavaScriptEnabled(true);
        String encodedHtml = Base64.encodeToString(privacy.getBytes(),
                Base64.NO_PADDING);
        if (type.equals(Constant.PRIVACY_POLICY)) {
            binding.wvPrivacy.loadUrl(privacy);
        } else {
            binding.wvPrivacy.loadData(encodedHtml, "text/html", "base64");
        }
    }
}