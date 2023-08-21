package com.itsbusinessposter.idebrandvideo;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.itsbusinessposter.idebrandvideo.Ads.AppOpenManager;
import com.itsbusinessposter.idebrandvideo.utils.Connectivity;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;
import com.itsbusinessposter.idebrandvideo.utils.Util;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    Connectivity connectivity;

    PrefManager prefManager;
    public static Context context;

    private static final String ONESIGNAL_APP_ID = "83ea55d2-4e6d-42ef-8647-e1cb32107ece";

    public static AppOpenManager appOpenAdManager;
    public static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;

        connectivity = new Connectivity(this);
        prefManager = new PrefManager(this);

        context = this;
        if (connectivity.isConnected()) {
            Config.IS_CONNECTED = true;
        } else {
            Config.IS_CONNECTED = false;
        }

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        OneSignal.promptForPushNotifications();

        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {}
                });
    }

    public static int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        point.y = display.getHeight();

        return point.y;
    }

    public static int getColumnWidth(int column, float grid_padding) {
        Resources r = context.getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, grid_padding, r.getDisplayMetrics());
        return (int) ((getScreenWidth() - ((column + 1) * padding)) / column);
    }

    public static void ShowOpenAds() {
        try {
            if(prefManager().getBoolean(Constant.OPEN_AD_ENABLE) && prefManager().getBoolean(Constant.ADS_ENABLE) && !Constant.IS_SUBSCRIBED) {
                appOpenAdManager = new AppOpenManager(myApplication);
            }
        }catch (Exception e) {
            Util.showErrorLog(e.getMessage(), e);
        }
    }

    public static PrefManager prefManager(){
        return new PrefManager(myApplication);
    }
}
