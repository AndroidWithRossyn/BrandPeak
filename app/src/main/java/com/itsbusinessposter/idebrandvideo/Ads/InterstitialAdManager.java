package com.itsbusinessposter.idebrandvideo.Ads;

import static com.itsbusinessposter.idebrandvideo.MyApplication.prefManager;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.ump.ConsentInformation;
import com.itsbusinessposter.idebrandvideo.utils.Constant;

public class InterstitialAdManager {
    private static InterstitialAd mInterstitialAd;
    private static Activity mcontext;
    private static Listener listener;

    public static void Interstitial(Activity context, Listener mlistener) {
        mcontext = context;
        listener = mlistener;
        LoadAds();
    }

    public static void LoadAds() {
        if (prefManager().getBoolean(Constant.INTERSTITIAL_AD_ENABLE) && prefManager().getBoolean(Constant.ADS_ENABLE) && !Constant.IS_SUBSCRIBED) {
            switch (prefManager().getString(Constant.AD_NETWORK)) {
                case Constant.ADMOB:
                    AdRequest.Builder builder = new AdRequest.Builder();
                    int request = GDPRChecker.getStatus();
                    if (request == ConsentInformation.ConsentStatus.NOT_REQUIRED) {
                        // load non Personalized ads
                        Bundle extras = new Bundle();
                        extras.putString("npa", "1");
                        builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
                    } // else do nothing , it will load PERSONALIZED ads
                    InterstitialAd.load(mcontext, prefManager().getString(Constant.INTERSTITIAL_AD_ID), builder.build(),
                            new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                    // The mInterstitialAd reference will be null until
                                    // an ad is loaded.
                                    mInterstitialAd = interstitialAd;
                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                    // Handle the error
                                    mInterstitialAd = null;
                                    listener.onAdFailedToLoad();
                                }
                            });
                    break;
                case Constant.FACEBOOK:

                    break;
                case Constant.UNITY:

                    break;
            }
        } else {
            mInterstitialAd = null;
        }
    }

    public static boolean isLoaded() {
        return mInterstitialAd == null ? false : true;
    }

    public static void showAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    mInterstitialAd = null;
                    listener.onAdDismissed();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    mInterstitialAd = null;
                    listener.onAdFailedToLoad();
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                }
            });
            if (mInterstitialAd != null) {
                mInterstitialAd.show(mcontext);
            }
        }
    }

    public interface Listener {

        void onAdFailedToLoad();

        void onAdDismissed();

    }
}
