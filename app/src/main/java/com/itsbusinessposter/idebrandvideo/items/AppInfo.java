package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "app_info", primaryKeys = "appTitle")
public class AppInfo {

    @NonNull
    @SerializedName("appTitle")
    public String appTitle;

    @SerializedName("author")
    public String author;

    @SerializedName("description")
    public String description;

    @SerializedName("contact")
    public String contact;

    @SerializedName("email")
    public String email;

    @SerializedName("website")
    public String website;

    @SerializedName("developedBy")
    public String developedBy;

    @SerializedName("appLogo")
    public String appLogo;

    @SerializedName("razorpayKeyId")
    public String razorpayKeyId;

    @SerializedName("privacyPolicy")
    public String privacyPolicy;

    @SerializedName("refundPolicy")
    public String refundPolicy;

    @SerializedName("termsCondition")
    public String termsCondition;

    @Embedded(prefix = "version_")
    @SerializedName("appUpdate")
    public AppVersion appVersion;

    public String privacyPolicy_link;

    @SerializedName("adsEnable")
    public String adsEnabled ;

    @SerializedName("adsNetwork")
    public String ad_network;

    @SerializedName("publisherId")
    public String publisher_id;

    @SerializedName("bannerAdsId")
    public String banner_ad_id;

    @SerializedName("appOpenAdsId")
    public String open_ad_id;

    @SerializedName("interstitialAdsId")
    public String interstitial_ad_id;

    @SerializedName("interstitialAdsClick")
    public String interstitial_ad_click;

    @SerializedName("nativeAdsId")
    public String native_ad_id;

    @SerializedName("bannerAdsEnable")
    public String banner_ad;

    @SerializedName("interstitialAdsEnable")
    public String interstitial_ad;

    @SerializedName("nativeAdsEnable")
    public String native_ad ;

    @SerializedName("appOpensAdsEnable")
    public String open_ad;

    @Embedded(prefix = "offer_")
    @SerializedName("offer")
    public OfferItem offerItem;

    public AppInfo(@NonNull String appTitle, String author, String description, String contact, String email, String website, String developedBy, String appLogo, String razorpayKeyId, String privacyPolicy, String refundPolicy, String termsCondition, AppVersion appVersion, String privacyPolicy_link, String adsEnabled, String ad_network, String publisher_id, String banner_ad_id, String open_ad_id, String interstitial_ad_id, String interstitial_ad_click, String native_ad_id, String banner_ad, String interstitial_ad, String native_ad, String open_ad, OfferItem offerItem) {
        this.appTitle = appTitle;
        this.author = author;
        this.description = description;
        this.contact = contact;
        this.email = email;
        this.website = website;
        this.developedBy = developedBy;
        this.appLogo = appLogo;
        this.razorpayKeyId = razorpayKeyId;
        this.privacyPolicy = privacyPolicy;
        this.refundPolicy = refundPolicy;
        this.termsCondition = termsCondition;
        this.appVersion = appVersion;
        this.privacyPolicy_link = privacyPolicy_link;
        this.adsEnabled = adsEnabled;
        this.ad_network = ad_network;
        this.publisher_id = publisher_id;
        this.banner_ad_id = banner_ad_id;
        this.open_ad_id = open_ad_id;
        this.interstitial_ad_id = interstitial_ad_id;
        this.interstitial_ad_click = interstitial_ad_click;
        this.native_ad_id = native_ad_id;
        this.banner_ad = banner_ad;
        this.interstitial_ad = interstitial_ad;
        this.native_ad = native_ad;
        this.open_ad = open_ad;
        this.offerItem = offerItem;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appTitle='" + appTitle + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", developedBy='" + developedBy + '\'' +
                ", appLogo='" + appLogo + '\'' +
                ", razorpayKeyId='" + razorpayKeyId + '\'' +
                ", privacyPolicy='" + privacyPolicy + '\'' +
                ", refundPolicy='" + refundPolicy + '\'' +
                ", termsCondition='" + termsCondition + '\'' +
                ", appVersion=" + appVersion +
                ", privacyPolicy_link='" + privacyPolicy_link + '\'' +
                ", adsEnabled='" + adsEnabled + '\'' +
                ", ad_network='" + ad_network + '\'' +
                ", publisher_id='" + publisher_id + '\'' +
                ", banner_ad_id='" + banner_ad_id + '\'' +
                ", open_ad_id='" + open_ad_id + '\'' +
                ", interstitial_ad_id='" + interstitial_ad_id + '\'' +
                ", interstitial_ad_click='" + interstitial_ad_click + '\'' +
                ", native_ad_id='" + native_ad_id + '\'' +
                ", banner_ad='" + banner_ad + '\'' +
                ", interstitial_ad='" + interstitial_ad + '\'' +
                ", native_ad='" + native_ad + '\'' +
                ", open_ad='" + open_ad + '\'' +
                ", offerItem=" + offerItem +
                '}';
    }
}
