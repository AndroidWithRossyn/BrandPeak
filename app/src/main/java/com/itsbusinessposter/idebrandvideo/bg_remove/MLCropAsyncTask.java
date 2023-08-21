package com.itsbusinessposter.idebrandvideo.bg_remove;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class MLCropAsyncTask extends AsyncTask<Void, Void, Void> {
    Activity activity;
    Bitmap cropped;
    int left;
    Bitmap mask;
    MLOnCropTaskCompleted onTaskCompleted;
    int top;

    private void show(final boolean z) {
        if (this.activity != null) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (z) {
                    } else {
                    }
                }
            });
        }
    }

    public MLCropAsyncTask(MLOnCropTaskCompleted mLOnCropTaskCompleted, Activity activity2) {
        this.onTaskCompleted = mLOnCropTaskCompleted;
        this.activity = activity2;
    }


    public void onPreExecute() {
        super.onPreExecute();
        show(true);
    }


    public Void doInBackground(Void... voidArr) {
        this.cropped = BGConfig.currentBit;
        this.left = 0;
        this.top = 0;
        this.mask = null;

        DeeplabMobile deeplabMobile = new DeeplabMobile();
        deeplabMobile.initialize(this.activity.getApplicationContext());
        this.cropped = loadInBackground(BGConfig.currentBit, deeplabMobile);
        BGConfig.currentBit  = cropped;
        Log.e("SSS", "DO IN BACK");
        return null;
    }


    public void onPostExecute(Void voidR) {
        show(false);
        Log.e("SSS", "EXECUTE");
        this.onTaskCompleted.onTaskCompleted(this.cropped, this.mask, this.left, this.top);
    }

    public Bitmap loadInBackground(Bitmap bitmap, DeeplabMobile deeplabMobile) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float max = 513.0f / ((float) Math.max(bitmap.getWidth(), bitmap.getHeight()));
        int round = Math.round(((float) width) * max);
        int round2 = Math.round(((float) height) * max);
        this.mask = deeplabMobile.segment(SupportedClass.tfResizeBilinear(bitmap, round, round2));
        if (this.mask == null) {
            return null;
        }
        this.mask = BGBitmapUtils.createClippedBitmap(this.mask, (this.mask.getWidth() - round) / 2, (this.mask.getHeight() - round2) / 2, round, round2);
        this.mask = BGBitmapUtils.scaleBitmap(this.mask, width, height);
        this.left = (this.mask.getWidth() - width) / 2;
        this.top = (this.mask.getHeight() - height) / 2;
        int i = this.top;
        this.top = i;
//        StoreManager.setCurrentCroppedMaskBitmap(this.activity, this.mask);
        return SupportedClass.cropBitmapWithMask(bitmap, this.mask, 0, 0);
    }
}
