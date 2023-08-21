package com.itsbusinessposter.idebrandvideo.ui.activities;

import static com.itsbusinessposter.idebrandvideo.utils.Constant.ADMOB;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.ADS_ENABLE;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.AD_NETWORK;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.BANNER_AD_ENABLE;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.BANNER_AD_ID;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.CHECKED_ITEM;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.FACEBOOK;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.INTERSTITIAL_AD_CLICK;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.INTERSTITIAL_AD_ENABLE;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.INTERSTITIAL_AD_ID;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.NATIVE_AD_ENABLE;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.NATIVE_AD_ID;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.OPEN_AD_ENABLE;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.OPEN_AD_ID;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.PRIVACY_POLICY;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.PRIVACY_POLICY_LINK;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.PUBLISHER_ID;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.UNITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.items.AppVersion;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;

public class SplashyActivity extends AppCompatActivity {

    PrefManager prefManager;
    UserViewModel userViewModel;
    DialogMsg dialogMsg;

    String id = "";
    String type = "";
    String imgUrl = "";
    String name = "";
    boolean video = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashy);

        prefManager = new PrefManager(this);
        dialogMsg = new DialogMsg(this, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        initData();

        if (prefManager.getString(CHECKED_ITEM).equals("yes")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        try {

            if (Config.isFromNotifications) {
                id = prefManager.getString(Constant.PRF_ID);
                name = prefManager.getString(Constant.PRF_NAME);
                imgUrl = "";
                type = prefManager.getString(Constant.PRF_TYPE);
                video = prefManager.getBoolean(Constant.INTENT_VIDEO);

                Util.showLog("NOT: " + id + ", " + name + ", " + imgUrl + ", " + type + ", " + video);

            }
        } catch (Exception e) {
            Util.showErrorLog(e.getMessage(), e);
        }

        getData();
    }

    public void getData() {
        if (Config.IS_CONNECTED) {
            Util.showLog("Internet connected");

            FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(60)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                boolean updated = task.getResult();
                                Util.showLog("Config params updated: " + updated);

                            } else {

                            }
                            Util.showLog("API_KEY: " + mFirebaseRemoteConfig.getString("apiKey"));
                            prefManager.setString(Constant.api_key, mFirebaseRemoteConfig.getString("apiKey"));

                            Config.API_KEY = prefManager.getString(Constant.api_key);
                            prefManager.setString("FIRST", "TRUE");
                            userViewModel.setAppInfo("new");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Util.showErrorLog("Firebase", e);
                            gotoMainActivity();
                        }
                    });

        } else {
            Util.showLog("Internet is not connected");
            gotoMainActivity();
        }
    }

    private void initData() {

        userViewModel.getAppInfo().observe(this, listResource -> {

            if (listResource != null) {

                Util.showLog("Got Data: " + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {
                                prefManager.setString(PRIVACY_POLICY, listResource.data.privacyPolicy);
                                prefManager.setString(Constant.TERM_CONDITION, listResource.data.termsCondition);
                                prefManager.setString(Constant.REFUND_POLICY, listResource.data.refundPolicy);

                                prefManager.setString(Constant.RAZORPAY_KEY_ID, listResource.data.razorpayKeyId);

                                prefManager.setString(PRIVACY_POLICY_LINK, listResource.data.privacyPolicy);


                                prefManager.setBoolean(ADS_ENABLE, listResource.data.adsEnabled.equals(Config.ONE) ? true : false);

                                prefManager.setString(AD_NETWORK, listResource.data.ad_network);

                                prefManager.setString(PUBLISHER_ID, listResource.data.publisher_id);

                                prefManager.setString(BANNER_AD_ID, listResource.data.banner_ad_id);
                                prefManager.setBoolean(BANNER_AD_ENABLE, listResource.data.banner_ad.equals(Config.ONE) ? true : false);

                                prefManager.setString(INTERSTITIAL_AD_ID, listResource.data.interstitial_ad_id);
                                prefManager.setBoolean(INTERSTITIAL_AD_ENABLE, listResource.data.interstitial_ad.equals(Config.ONE) ? true : false);
                                prefManager.setInt(INTERSTITIAL_AD_CLICK, Integer.parseInt(listResource.data.interstitial_ad_click));

                                prefManager.setString(NATIVE_AD_ID, listResource.data.native_ad_id);
                                prefManager.setBoolean(NATIVE_AD_ENABLE, listResource.data.native_ad.equals(Config.ONE) ? true : false);

                                prefManager.setString(OPEN_AD_ID, listResource.data.open_ad_id);
                                prefManager.setBoolean(OPEN_AD_ENABLE, listResource.data.open_ad.equals(Config.ONE) ? true : false);

                                if (prefManager.getBoolean(ADS_ENABLE)) {
                                    initializeAds();
                                }

                                checkVersionNo(listResource.data.appVersion);

                                Config.offerItem = listResource.data.offerItem;

                            } catch (NullPointerException ne) {
                                Util.showErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Util.showErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.setLoadingState(false);

                        }

                        break;
                    case ERROR:
                        // Error State
                        prefManager.setBoolean(Constant.IS_LOGIN, false);
                        dialogMsg.showErrorDialog(getString(R.string.click_try_again), getString(R.string.try_again));
                        dialogMsg.show();

                        dialogMsg.okBtn.setOnClickListener(v -> {
                            dialogMsg.cancel();
                            getData();
                        });

                        userViewModel.setLoadingState(false);

                        break;
                    default:
                        // Default

                        userViewModel.setLoadingState(false);

                        break;
                }

            } else {

                // Init Object or Empty Data
                Util.showLog("Empty Data");
                prefManager.setBoolean(Constant.IS_LOGIN, false);

            }

        });

    }

    private void initializeAds() {
        switch (prefManager.getString(AD_NETWORK)) {
            case ADMOB:
                break;
            case UNITY:
                break;
            case FACEBOOK:
                break;
        }
    }

    private void checkVersionNo(AppVersion appVersion) {

        if (appVersion.updatePopupShow.equals(Config.ONE) && !appVersion.newAppVersionCode.equals(Config.APP_VERSION)) {

            dialogMsg.showAppInfoDialog(getString(R.string.force_update__button_update), getString(R.string.app__cancel),
                    getString(R.string.force_update_true), appVersion.versionMessage);
            dialogMsg.show();

            if (appVersion.cancelOption.equals(Config.ZERO)) {
                dialogMsg.cancelBtn.setVisibility(View.GONE);
            }
            dialogMsg.cancelBtn.setOnClickListener(v -> {
                dialogMsg.cancel();
                gotoMainActivity();
            });

            dialogMsg.okBtn.setOnClickListener(v -> {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appVersion.appLink)));
            });

        } else {
            gotoMainActivity();
        }

    }

    private void gotoMainActivity() {
        Intent intent;
        Util.showLog("NOTI_SS: " + Config.isFromNotifications);
        if (Config.isFromNotifications) {
            if (type.equals(Constant.FESTIVAL) || type.equals(Constant.CATEGORY) || type.equals(Constant.CUSTOM)) {
                intent = new Intent(SplashyActivity.this, DetailActivity.class);
                intent.putExtra(Constant.INTENT_TYPE, type);
                intent.putExtra(Constant.INTENT_FEST_ID, id);
                intent.putExtra(Constant.INTENT_FEST_NAME, name);
                intent.putExtra(Constant.INTENT_POST_IMAGE, "");
                intent.putExtra(Constant.INTENT_VIDEO, video);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else if (type.equals(Constant.EXTERNAL)) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(prefManager.getString(Constant.PRF_LINK)));
            } else {
                intent = new Intent(SplashyActivity.this, SubsPlanActivity.class);
            }
            Config.isFromNotifications = false;
        } else {
            if (!prefManager.getBoolean(Constant.IS_FIRST_TIME_LAUNCH)) {
                intent = new Intent(SplashyActivity.this, IntroActivity.class);
            } else {
                intent = new Intent(SplashyActivity.this, MainActivity.class);
            }
        }
        startActivity(intent);
        finish();
    }
}