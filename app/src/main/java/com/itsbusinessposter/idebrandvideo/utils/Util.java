package com.itsbusinessposter.idebrandvideo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.R;

import org.json.JSONException;

public class Util {

    private static Typeface fromAsset;
    private static Fonts currentTypeface;

    public static void showLog(String message) {

        if (Config.IS_DEVELOPING) {
            Log.d("Team_iQueen", message);
        }
    }

    public static void showErrorLog(String s, JSONException e) {
        if (Config.IS_DEVELOPING) {
            try {
                StackTraceElement l = e.getStackTrace()[0];
                Log.d("Team_iQueen", s);
                Log.d("Team_iQueen", "Line : " + l.getLineNumber());
                Log.d("Team_iQueen", "Method : " + l.getMethodName());
                Log.d("Team_iQueen", "Class : " + l.getClassName());
            } catch (Exception ee) {
                Log.d("Team_iQueen", "Error in psErrorLogE");
            }
        }
    }

    public static void showErrorLog(String log, Object obj) {
        if (Config.IS_DEVELOPING) {
            try {
                Log.d("Team_iQueen", log);
                Log.d("Team_iQueen", "Line : " + getLineNumber());
                Log.d("Team_iQueen", "Class : " + getClassName(obj));
            } catch (Exception ee) {
                Log.d("Team_iQueen", "Error in psErrorLog");
            }
        }
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[4].getLineNumber();
    }

    public static String getClassName(Object obj) {
        return "" + ((Object) obj).getClass();
    }


    public static void StatusBarColor(Window window, int color) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    public static void fadeIn(View view, Context context) {
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
    }

    public static Typeface getTypeFace(Context context, Fonts fonts) {

        if (currentTypeface == fonts) {
            if (fromAsset == null) {
                if (fonts == Fonts.NOTO_SANS) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/NotoSans-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
                } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
                } else if (fonts == Fonts.ROBOTO_LIGHT) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
                } else if (fonts == Fonts.ROBOTO_BOLD) {
                    fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
                }
            }
        } else {
            if (fonts == Fonts.NOTO_SANS) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/NotoSans-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
            } else if (fonts == Fonts.ROBOTO_MEDIUM) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Medium.ttf");
            } else if (fonts == Fonts.ROBOTO_LIGHT) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Light.ttf");
            } else if (fonts == Fonts.ROBOTO_BOLD) {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
            } else {
                fromAsset = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");
            }

            //fromAsset = Typeface.createFromAsset(activity.getAssets(), "font/Roboto-Italic.ttf");
            currentTypeface = fonts;
        }
        return fromAsset;
    }

    public enum Fonts {
        ROBOTO,
        NOTO_SANS,
        ROBOTO_LIGHT,
        ROBOTO_MEDIUM,
        ROBOTO_BOLD
    }

    public static int dpToPx(Context context, int i) {
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    public static void loadFirebase(Context context) {
        PrefManager prefManager = new PrefManager(context);
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
                        prefManager.setString(Constant.api_key, mFirebaseRemoteConfig.getString("apiKey"));

                        Config.API_KEY = prefManager.getString(Constant.api_key);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                Util.showErrorLog("Firebase", e);
                    }
                });
    }

    public static void showToast(Context context, String message) {
        try {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showErrorLog(e.getMessage(), e);
        }
    }

}
