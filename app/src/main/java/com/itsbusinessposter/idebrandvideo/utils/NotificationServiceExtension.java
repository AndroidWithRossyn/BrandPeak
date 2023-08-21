package com.itsbusinessposter.idebrandvideo.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.ui.activities.SplashyActivity;
import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings("unused")
public class NotificationServiceExtension implements OneSignal.OSRemoteNotificationReceivedHandler {

    public String id = "";
    public String type = "";
    public String name = "";
    public String link = "";
    public boolean video = false;
    public Context context;
    public static final String CHANNEL_ID = "iqueen_channel";
    public static final int NOTIFICATION_ID = 1;
    PrefManager prefManager;


    @Override
    public void remoteNotificationReceived(Context context, OSNotificationReceivedEvent osNotificationReceivedEvent) {
        this.context = context;
        prefManager = new PrefManager(context);

        OSNotification notification = osNotificationReceivedEvent.getNotification();
        Util.showLog("NOTIFICATION: " + notification.toString());

        OSMutableNotification mutableNotification = notification.mutableCopy();
        mutableNotification.setExtender(builder -> {
            // Sets the accent color to Green on Android 5+ devices.
            // Accent color controls icon and action buttons on Android 5+. Accent color does not change app title on Android 10+
            builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            // Sets the notification Title to Red
            Spannable spannableTitle = new SpannableString(notification.getTitle());
            spannableTitle.setSpan(new ForegroundColorSpan(Color.RED), 0, notification.getTitle().length(), 0);
            builder.setContentTitle(spannableTitle);
            // Sets the notification Body to Blue
            Spannable spannableBody = new SpannableString(notification.getBody());
            spannableBody.setSpan(new ForegroundColorSpan(Color.BLUE), 0, notification.getBody().length(), 0);
            builder.setContentText(spannableBody);
            //Force remove push from Notification Center after 30 seconds
            builder.setTimeoutAfter(30000);
            return builder;
        });
        // If complete isn't call within a time period of 25 seconds, OneSignal internal logic will show the original notification
        // To omit displaying a notification, pass `null` to complete()
        JSONObject data = notification.getAdditionalData();

        Util.showLog(data.toString());
        try {
            type = data.getString("type");
            video = data.getBoolean("video");
            if (type.equals(Constant.CATEGORY)) {

                id = data.getString("id");
                name = data.getString("name");

            } else if (type.equals(Constant.FESTIVAL)) {

                id = data.getString("id");
                name = data.getString("festival");

            } else if (type.equals(Constant.SUBS_PLAN)) {

                id = data.getString("id");
                name = data.getString("subscriptionPlan");

            } else if (type.equals(Constant.CUSTOM)) {

                id = data.getString("id");
                name = data.getString("custom");

            } else if (type.equals(Constant.EXTERNAL)) {
                link = data.getString("externalLink");
                // intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getString("externalLink")));
            }

        } catch (JSONException e) {
            Util.showErrorLog(e.getMessage(), e);
        }
        osNotificationReceivedEvent.complete(null);
        if(prefManager.getBoolean(Constant.NOTIFICATION_ENABLED)) {
            sendNotification(notification);
        }
    }

    private void sendNotification(OSNotification notification) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;

        prefManager.setBoolean(Constant.IS_NOT, true);
        prefManager.setString(Constant.PRF_ID, id);
        prefManager.setString(Constant.PRF_NAME, name);
        prefManager.setString(Constant.PRF_TYPE, type);
        prefManager.setString(Constant.PRF_LINK, link);
        prefManager.setBoolean(Constant.INTENT_VIDEO, video);

        intent = new Intent(context, SplashyActivity.class);
        intent.putExtra(Constant.INTENT_TYPE, type);
        intent.putExtra(Constant.INTENT_IS_FROM_NOTIFICATION, true);
        intent.putExtra(Constant.INTENT_FEST_ID, id);
        intent.putExtra(Constant.INTENT_FEST_NAME, name);
        intent.putExtra(Constant.INTENT_POST_IMAGE, "");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Config.isFromNotifications = true;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "QuotesPush";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setAutoCancel(true)
                .setSound(uri)
                .setAutoCancel(true)
                .setLights(Color.RED, 800, 800)
                .setContentText(notification.getBody())
                .setChannelId(CHANNEL_ID);

        mBuilder.setSmallIcon(getNotificationIcon(mBuilder));
        try {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "errror large- " + e.getMessage(), Toast.LENGTH_LONG).show();
            prefManager.setBoolean(Constant.IS_NOT, false);
        }

        if (notification.getTitle().trim().isEmpty()) {
            mBuilder.setContentTitle(context.getString(R.string.app_name));
            mBuilder.setTicker(context.getString(R.string.app_name));
        } else {
            mBuilder.setContentTitle(notification.getTitle());
            mBuilder.setTicker(notification.getTitle());
        }

        if (notification.getBigPicture() != null) {
            mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(notification.getBigPicture())));
        }

        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getColour());
            return R.mipmap.ic_launcher;
        } else {
            return R.mipmap.ic_launcher;
        }
    }

    private int getColour() {
        return 0x8b5630;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            InputStream input;
            if (src.contains("https://")) {
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            } else {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                input = connection.getInputStream();
            }
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }
}
