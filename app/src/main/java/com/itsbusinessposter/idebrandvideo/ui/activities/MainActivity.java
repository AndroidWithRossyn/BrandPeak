package com.itsbusinessposter.idebrandvideo.ui.activities;

import static com.itsbusinessposter.idebrandvideo.MyApplication.ShowOpenAds;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.ADMOB;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.ADS_ENABLE;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.AD_NETWORK;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.BANNER_AD_ENABLE;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.BANNER_AD_ID;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.itsbusinessposter.idebrandvideo.Ads.BannerAdManager;
import com.itsbusinessposter.idebrandvideo.Ads.GDPRChecker;
import com.itsbusinessposter.idebrandvideo.BuildConfig;
import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.MyApplication;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.binding.GlideBinding;
import com.itsbusinessposter.idebrandvideo.databinding.ActivityMainBinding;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.ui.dialog.DialogMsg;
import com.itsbusinessposter.idebrandvideo.ui.dialog.LanguageDialog;
import com.itsbusinessposter.idebrandvideo.ui.fragments.BusinessFragment;
import com.itsbusinessposter.idebrandvideo.ui.fragments.CustomFragment;
import com.itsbusinessposter.idebrandvideo.ui.fragments.NewsFragment;
import com.itsbusinessposter.idebrandvideo.ui.fragments.DownloadFragment;
import com.itsbusinessposter.idebrandvideo.ui.fragments.HomeFragment;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.itsbusinessposter.idebrandvideo.viewmodel.UserViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    PrefManager prefManager;
    private String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    UserViewModel userViewModel;
    UserItem userItem = null;
    DialogMsg dialogMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);
        dialogMsg = new DialogMsg(this, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setupFragment(new HomeFragment());
        binding.bottomNavigationView.setSelectedItemId(R.id.home_menu);

        if (!prefManager.getBoolean(Constant.NOTIFICATION_FIRST)) {
            prefManager.setBoolean(Constant.NOTIFICATION_ENABLED, true);
            prefManager.setBoolean(Constant.NOTIFICATION_FIRST, true);
        }
//        setupFragment(new HomeFragment());
//        binding.bottomNavigationView.setSelectedItemId(R.id.home_menu);
        setUi();
        initData();
        if (prefManager.getString(Constant.api_key).equals(Constant.api_key)) {
            Util.loadFirebase(this);
        }
        if (prefManager.getBoolean(Constant.IS_LOGIN)) {
            changeData();
        } else {
            setUserData(null, false);
        }
        showOfferDialog();
    }

    private void showOfferDialog() {
        if (Config.offerItem!=null && !Config.offerItem.image.equals("")) {
            dialogMsg.showOfferDialog(Config.offerItem.image);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) dialogMsg.cvOffer.getLayoutParams();

            int width = MyApplication.getColumnWidth(1, getResources().getDimension(com.intuit.ssp.R.dimen._15ssp));

            params.width = width;
            params.height = (int) (width * 1.3);

            dialogMsg.cvOffer.setLayoutParams(params);

            dialogMsg.show();
            dialogMsg.ivOffer.setOnClickListener(v -> {
                dialogMsg.cancel();
                startActivity(new Intent(MainActivity.this, SubsPlanActivity.class));
            });
            dialogMsg.ivCancel.setOnClickListener(v -> {
                dialogMsg.cancel();
            });
        }
    }

    private void changeData() {
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
                            setUserData(listResource.data, true);
                        }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            userItem = listResource.data;
                            setUserData(listResource.data, true);
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

    private void setUserData(UserItem data, boolean bool) {
        if (bool) {
            Constant.IS_SUBSCRIBED = data.isSubscribed;
            GlideBinding.bindImage(binding.header.cvProfileImage, data.userImage);
            binding.header.txtHeaderName.setText(data.userName);
            binding.header.txtHeaderEmail.setText(data.email);
            binding.header.liHeader.setOnClickListener(v -> {
                closeDrawer();
                startActivity(new Intent(this, ProfileActivity.class));
            });
            binding.navLogin.setVisibility(View.GONE);
            binding.navLogout.setVisibility(View.VISIBLE);
        } else {
            binding.navLogout.setVisibility(View.GONE);
            binding.navLogin.setVisibility(View.VISIBLE);
            binding.navProfile.setVisibility(View.GONE);
            binding.header.txtHeaderName.setText(getString(R.string.click_here));
            binding.header.txtHeaderEmail.setText(getString(R.string.login_first_login));
            binding.header.liHeader.setOnClickListener(v -> {
                closeDrawer();
                startActivity(new Intent(this, LoginActivity.class));
            });
        }
    }

    private void initData() {
        userViewModel.getAppInfo().observe(this, listResource -> {
            if (listResource != null) {

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

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Util.showLog("Empty Data");
                prefManager.setBoolean(Constant.IS_LOGIN, false);

            }

        });
        userViewModel.setAppInfo("good");
    }

    private void initializeAds() {
        switch (prefManager.getString(AD_NETWORK)) {
            case ADMOB:
                new GDPRChecker()
                        .withContext(MainActivity.this)
                        .withPrivacyUrl(prefManager.getString(PRIVACY_POLICY_LINK))
                        .withPublisherIds(prefManager.getString(PUBLISHER_ID))
                        .check();
                ShowOpenAds();
                BannerAdManager.showBannerAds(this, binding.llAdview);
                break;
            case UNITY:
                break;
            case FACEBOOK:
                break;
        }
    }

    private void setUi() {
        checkPer();
        binding.drawerLayout.setScrimColor(Color.TRANSPARENT);
        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                                                   @Override
                                                   public void onDrawerSlide(View drawer, float slideOffset) {

                                                       binding.contentView.setX(binding.navigationView.getWidth() * slideOffset);
                                                       DrawerLayout.LayoutParams lp =
                                                               (DrawerLayout.LayoutParams) binding.contentView.getLayoutParams();
                                                       lp.height = drawer.getHeight() -
                                                               (int) (drawer.getHeight() * slideOffset * 0.3f);
                                                       lp.topMargin = (drawer.getHeight() - lp.height) / 2;
                                                       binding.contentView.setLayoutParams(lp);
                                                   }

                                                   @Override
                                                   public void onDrawerOpened(View drawerView) {
                                                       Util.StatusBarColor(getWindow(), getResources().getColor(R.color.white));
                                                       binding.toolbar.toolbarIvMenu.setBackground(getDrawable(R.drawable.ic_back));
                                                   }

                                                   @Override
                                                   public void onDrawerClosed(View drawerView) {
                                                       Util.StatusBarColor(getWindow(), getResources().getColor(R.color.primary_color));
                                                       binding.toolbar.toolbarIvMenu.setBackground(getDrawable(R.drawable.ic_menu_icon));
                                                   }
                                               }
        );

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        binding.toolbar.toolName.setText(getResources().getString(R.string.app_name));
                        setupFragment(new HomeFragment());
                        return true;
                    case R.id.news_menu:
                        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_news));
                        setupFragment(new NewsFragment());
                        return true;
                    case R.id.custom_menu:
                        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_custom));
                        setupFragment(new CustomFragment());
                        return true;
                    case R.id.business_menu:
                        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_business));
                        setupFragment(new BusinessFragment());
                        return true;
                    case R.id.download_menu:
                        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_download));
                        setupFragment(new DownloadFragment());
                        return true;
                    default:
                        return false;
                }
            }
        });

        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            if (binding.drawerLayout.isOpen()) {
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.navLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        binding.navLanguage.setOnClickListener(v -> {
            closeDrawer();
            LanguageDialog dialog = new LanguageDialog(this, languages -> {
                prefManager.setString(Constant.USER_LANGUAGE, languages);
            });
            dialog.showDialog();
        });
        binding.navCategory.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, CategoryActivity.class));
        });

        binding.navBusinessCard.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, VCardActivity.class));
        });

        binding.navHome.setOnClickListener(v -> {
            closeDrawer();
            binding.bottomNavigationView.setSelectedItemId(R.id.home_menu);
        });

//        String secureId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        Util.showLog("AndroidId: " + secureId);
        binding.navSubscribe.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, SubsPlanActivity.class));
        });
        binding.navSetting.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, SettingActivity.class));
        });

        binding.navContact.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, ContactUsActivity.class));
        });
        binding.navProfile.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, ProfileActivity.class));
        });
        binding.navAboutUs.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(this, AboutUsActivity.class));
        });
        binding.navRate.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(intent);
        });

        binding.navLogout.setOnClickListener(v -> {
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

                            setUserData(null, false);

                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .build();
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                            googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                            Util.showToast(MainActivity.this, getString(R.string.success_logout));
                        }
                    });

                    Util.showLog("nav_logout_login");
                }
            });

            dialogMsg.cancelBtn.setOnClickListener(view -> dialogMsg.cancel());
            closeDrawer();
        });

        binding.navPrivacyPolicy.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.PRIVACY_POLICY);
            startActivity(intent);
        });

        binding.navShare.setOnClickListener(v->{
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                String shareMessage= getString(R.string.share_msg);
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        });

    }

    private void checkPer() {
        Dexter.withContext(this)
                .withPermissions(
                        PERMISSIONS
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
    }

    private void closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setupFragment(Fragment fragment) {
        try {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_main, fragment)
                    .commitAllowingStateLoss();
        } catch (Exception e) {
            Util.showLog("Error! Can't replace fragment.");
        }
    }

    private void launchIntro() {
        startActivity(new Intent(this, IntroActivity.class));
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_main);

        if (fragment != null) {
            if (fragment instanceof HomeFragment) {

                DialogMsg dialogMsg = new DialogMsg(this, false);
                dialogMsg.showConfirmDialog(getString(R.string.menu_exit), getString(R.string.do_you_want_to_exit), getString(R.string.yes), getString(R.string.no));
                dialogMsg.show();
                dialogMsg.okBtn.setOnClickListener(v -> {
                    dialogMsg.cancel();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    super.onBackPressed();
                    finish();
                    System.exit(0);
                });
            } else {
                binding.toolbar.toolName.setText(getResources().getString(R.string.app_name));
                setupFragment(new HomeFragment());
                binding.bottomNavigationView.setSelectedItemId(R.id.home_menu);
            }
        }

    }
}