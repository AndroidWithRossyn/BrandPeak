package com.itsbusinessposter.idebrandvideo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {

    public static Bitmap createFitBitmap(String str, int i) {
        int i2;
        if (str == null || str.isEmpty()) {
            return null;
        }
        Bitmap decodeFile = BitmapFactory.decodeFile(new File(str).getAbsolutePath());
        if (decodeFile.getWidth() > decodeFile.getHeight()) {
            i2 = (decodeFile.getHeight() * i) / decodeFile.getWidth();
        } else if (decodeFile.getWidth() < decodeFile.getHeight()) {
            i2 = i;
            i = (decodeFile.getWidth() * i) / decodeFile.getHeight();
        } else if (decodeFile.getWidth() == decodeFile.getHeight()) {
            i2 = i;
        } else {
            i = 0;
            i2 = 0;
        }
        return Bitmap.createScaledBitmap(decodeFile, i, i2, false);
    }


    public static Bitmap adjustRotatedBitmap(Bitmap bitmap, String photoPath) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            return rotatedBitmap;
        } catch (IOException ex) {
            ex.printStackTrace();
            return bitmap;
        }
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        //bm.recycle();
        return resizedBitmap;
    }

    private interface InSampleSize {
        int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight);
    }

    private static class InSampleSizePower2 implements InSampleSize {
        @Override
        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }

    private static class InSampleSizeViewSize implements InSampleSize {
        @Override
        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = Math.max(height / reqHeight, width / reqWidth);

            return inSampleSize;
        }
    }

    public static InSampleSize getInSampleSizePower2() {
        return new InSampleSizePower2();
    }

    public static InSampleSize getInSampleSizeViewSize() {
        return new InSampleSizeViewSize();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int calculateInSampleSize1(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = Math.max(height / reqHeight, width / reqWidth);

        return inSampleSize;
    }

    public static Bitmap decodeBitmapFromResource(Resources res, int resId,
                                                  int reqWidth, int reqHeight, InSampleSize inSampleSize) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = inSampleSize.calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeBitmapFromUri(Context context, Uri uri,
                                             int reqWidth, int reqHeight, InSampleSize inSampleSize) {

        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

            // Calculate inSampleSize
            options.inSampleSize = inSampleSize.calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inMutable = true;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeBitmapFromFile(Context context, File file,
                                              int reqWidth, int reqHeight, InSampleSize inSampleSize) {

        return decodeBitmapFromUri(context, Uri.fromFile(file), reqWidth, reqHeight, inSampleSize);
    }

    public static Bitmap decodeBitmapFromPath(Context context, String path,
                                              int reqWidth, int reqHeight, InSampleSize inSampleSize) {

        return decodeBitmapFromFile(context, new File(path), reqWidth, reqHeight, inSampleSize);
    }

    public static Bitmap decodeBitmapFromAsset(Context context, String path,
                                               int reqWidth, int reqHeight, InSampleSize inSampleSize) {

        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getAssets().open(path), null, options);

            // Calculate inSampleSize
            options.inSampleSize = inSampleSize.calculateInSampleSize(options, reqWidth, reqHeight);


            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inMutable = true;
            return BitmapFactory.decodeStream(context.getAssets().open(path), null, options);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromByteArray(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static Bitmap getBitmapForFilePath(String path) {
        File image = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);

        return bitmap;
    }

    public static Pair<Integer, Integer> originalSizeOfImage(Resources res, int resId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        return new Pair<>(options.outWidth, options.outHeight);
    }

    public static Pair<Integer, Integer> getImageViewSizeBasedOnBitmapSize(Pair<Integer, Integer> imageviewSize, Pair<Integer, Integer> bitmapSize) {

        int tempHeight = imageviewSize.second;
        for(; tempHeight != 0; tempHeight--) {
            int tempWidth = (bitmapSize.first * tempHeight) / bitmapSize.second;

            if(tempWidth <= imageviewSize.first && tempHeight <= imageviewSize.second) {
                return new Pair<>(tempWidth, tempHeight);
            }
        }

        return imageviewSize;
    }


    public static Bitmap sampledBitmapFromStream(InputStream inputstream, InputStream inputstream1, int i, int j)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputstream, null, options);
        options.inSampleSize = calculateInSampleSize(options, i, j);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(inputstream1, null, options);
    }

    public static float exifOrientationToDegrees(int i)
    {
        if (i == 6)
        {
            return 90F;
        }
        if (i == 3)
        {
            return 180F;
        }
        return i != 8 ? 0.0F : 270F;
    }


}
