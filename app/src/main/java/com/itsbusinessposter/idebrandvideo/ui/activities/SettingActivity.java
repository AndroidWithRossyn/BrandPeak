package com.itsbusinessposter.idebrandvideo.ui.activities;

import static com.itsbusinessposter.idebrandvideo.utils.Constant.CHECKED_ITEM;
import static com.itsbusinessposter.idebrandvideo.utils.Constant.NOTIFICATION_ENABLED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.itsbusinessposter.idebrandvideo.Ads.BannerAdManager;
import com.itsbusinessposter.idebrandvideo.BuildConfig;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.databinding.ActivitySettingBinding;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;

import java.io.File;
import java.text.DecimalFormat;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    int checkedItem;
    String selected;
    PrefManager prefManager;
    String[] themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefManager = new PrefManager(this);

        BannerAdManager.showBannerAds(this, binding.llAdview);
        setUpUi();
    }

    private void setUpUi() {
        themes = getResources().getStringArray(R.array.theme_array);
        binding.toolbar.toolName.setText(getResources().getString(R.string.menu_setting));
        binding.toolbar.toolbarIvMenu.setBackground(getResources().getDrawable(R.drawable.ic_back));
        binding.toolbar.toolbarIvMenu.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.txtVersion.setText(getString(R.string.version) + ": " + BuildConfig.VERSION_NAME);
        binding.txtTheme.setText(getString(R.string.theme) + ": " + (prefManager.getString(CHECKED_ITEM).equals("yes") ? themes[1] : themes[2]));
        if(prefManager.getString(CHECKED_ITEM).equals("yes")){
            binding.swTheme.setChecked(true);
        }else if (prefManager.getString(CHECKED_ITEM).equals("no")){
            binding.swTheme.setChecked(false);
        }
//        binding.swTheme.setChecked(prefManager.getBoolean(CHECKED_ITEM));
        binding.swTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.swTheme.setChecked(isChecked);
                binding.txtTheme.setText(getString(R.string.theme) + ": " + (isChecked ? themes[1] : themes[2]));
                AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

                if (isChecked) {
                    prefManager.setString(CHECKED_ITEM, "yes");
                }else{
                    prefManager.setString(CHECKED_ITEM, "no");
                }
            }
        });

        binding.swNotification.setChecked(prefManager.getBoolean(NOTIFICATION_ENABLED));
        binding.swNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.swNotification.setChecked(isChecked);
                prefManager.setBoolean(NOTIFICATION_ENABLED, isChecked);
            }
        });

        initializeCache();
        binding.cvClearCache.setOnClickListener(v->{
            deleteCache(this);
            initializeCache();
        });

        binding.cvPrivacyPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.PRIVACY_POLICY);
            startActivity(intent);
        });

        binding.cvTermService.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.TERM_CONDITION);
            startActivity(intent);
        });

        binding.cvRefund.setOnClickListener(v -> {
            Intent intent = new Intent(SettingActivity.this, PrivacyActivity.class);
            intent.putExtra("type", Constant.REFUND_POLICY);
            startActivity(intent);
        });
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private void initializeCache() {
        long size = 0;
        try {
            size += getDirSize(this.getCacheDir());
            size += getDirSize(this.getExternalCacheDir());
        } catch (Exception e) {

        }

        binding.txtCache.setText(getString(R.string.clear_cache) + ": " + readableFileSize(size));

    }

    public long getDirSize(File dir) {
        long size = 0;

        for (File file : dir.listFiles()) {
            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }
        return size;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0 Bytes";
        final String[] units = new String[]{"Bytes", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}