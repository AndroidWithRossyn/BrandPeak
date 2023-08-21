package com.itsbusinessposter.idebrandvideo;

import com.itsbusinessposter.idebrandvideo.items.OfferItem;

public class Config {


    /**
     * AppVersion
     * Current App Version
     */
    public static String APP_VERSION = "1.4";

    /**
     * API Related
     * Change your API URL Here
     * Your API KEY no need to add because we pass api key through firebase
     */
    public static final String APP_API_URL = "https://brandpeak.in/";
    public static String API_KEY = "";

    public static final boolean IS_DEVELOPING = true;
    public static boolean isFromNotifications = false;
    public static final int IMAGE_CACHE_LIMIT = 100;
    public static int BUSINESS_SIZE;
    public static boolean IS_CONNECTED = false;
    public static final boolean PRE_LOAD_IMAGE = false;

    public static final String EMPTY_STRING = "";
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";

    public static final int HOME_CATEGORY_SHOW = 8;
    public static final int HOME_BUSINESS_CATEGORY_SHOW = 8;

    public static OfferItem offerItem = new OfferItem(0, "", "", "", "", "");

    /**
     * Sticker List
     */
    public static int[] stickers = new int[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
    };


    /**
     * Error Codes
     */
    public static int ERROR_CODE_10001 = 10001; // Totally No Record
    public static int ERROR_CODE_10002 = 10002; // No More Record at pagination

}
