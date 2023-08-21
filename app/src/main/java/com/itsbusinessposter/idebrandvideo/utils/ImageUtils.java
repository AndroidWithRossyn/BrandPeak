package com.itsbusinessposter.idebrandvideo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

public class ImageUtils {
    public static Bitmap getResampleImageBitmap(Uri uri, Context context) throws IOException {
        String realPathFromURI = getRealPathFromURI(uri, context);
        try {
            return resampleImage(realPathFromURI, 800);
        } catch (Exception e) {
            e.printStackTrace();
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(realPathFromURI));
        }
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context, int i) throws IOException {
        try {
            return resampleImage(getRealPathFromURI(uri, context), i);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint({"UseValueOf"})
    public static Bitmap resampleImage(String str, int i) throws Exception {
        int exifRotation;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = getClosestResampleSize(options.outWidth, options.outHeight, i);
            Bitmap decodeFile = BitmapFactory.decodeFile(str, options2);
            if (decodeFile == null) {
                return null;
            }
            Matrix matrix = new Matrix();
            if (decodeFile.getWidth() > i || decodeFile.getHeight() > i) {
                BitmapFactory.Options resampling = getResampling(decodeFile.getWidth(), decodeFile.getHeight(), i);
                matrix.postScale(((float) resampling.outWidth) / ((float) decodeFile.getWidth()), ((float) resampling.outHeight) / ((float) decodeFile.getHeight()));
            }
            if (new Integer(Build.VERSION.SDK).intValue() > 4 && (exifRotation = ExifUtils.getExifRotation(str)) != 0) {
                matrix.postRotate((float) exifRotation);
            }
            return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BitmapFactory.Options getResampling(int i, int i2, int i3) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        float f = i > i2 ? ((float) i3) / ((float) i) : i2 > i ? ((float) i3) / ((float) i2) : ((float) i3) / ((float) i);
        options.outWidth = (int) ((((float) i) * f) + 0.5f);
        options.outHeight = (int) ((((float) i2) * f) + 0.5f);
        return options;
    }

    public static int getClosestResampleSize(int i, int i2, int i3) {
        int max = Math.max(i, i2);
        int i4 = 1;
        while (true) {
            if (i4 >= Integer.MAX_VALUE) {
                break;
            } else if (i4 * i3 > max) {
                i4--;
                break;
            } else {
                i4++;
            }
        }
        if (i4 > 0) {
            return i4;
        }
        return 1;
    }

    public static BitmapFactory.Options getBitmapDims(Uri uri, Context context) {
        String realPathFromURI = getRealPathFromURI(uri, context);
        Log.i("texting", "Path " + realPathFromURI);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(realPathFromURI, options);
        return options;
    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        try {
            Cursor query = context.getContentResolver().query(uri, (String[]) null,
                    (String) null, (String[]) null, (String) null);
            if (query == null) {
                return uri.getPath();
            }
            query.moveToFirst();
            @SuppressLint("Range") String string = query.getString(query.getColumnIndex("_data"));
            query.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return uri.toString();
        }
    }

    public static int[] getResizeDim(float f, float f2, int i, int i2) {
        float f3;
        float f4 = 0;
        float f5 = (float) i;
        float f6 = (float) i2;
        float f7 = f / f2;
        float f8 = f2 / f;
        if (f > f5) {
            f3 = f5 * f8;
            if (f3 > f6) {
                f5 = f6 * f7;
                return new int[]{(int) f5, (int) f6};
            }
        } else {
            if (f2 > f6) {
                f4 = f6 * f7;
                if (f4 > f5) {
                    f6 = f5 * f8;
                    return new int[]{(int) f5, (int) f6};
                }
            } else if (f7 > 0.75f) {
                f3 = f5 * f8;
                if (f3 > f6) {
                    f5 = f6 * f7;
                    return new int[]{(int) f5, (int) f6};
                }
            } else {
                if (f8 > 1.5f) {
                    f4 = f6 * f7;
                    if (f4 > f5) {
                        f6 = f5 * f8;
                    }
                } else {
                    f3 = f5 * f8;
                    if (f3 > f6) {
                        f5 = f6 * f7;
                    }
                }
                return new int[]{(int) f5, (int) f6};
            }
            f5 = f4;
            return new int[]{(int) f5, (int) f6};
        }
        f6 = f3;
        return new int[]{(int) f5, (int) f6};
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {

        float f;
        float f2 = 0;
        if (bitmap == null) {
            return null;
        }
        float f3 = (float) i;
        float f4 = (float) i2;
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();

        Log.e("SSSS", "sh " + f3 + f4 + "hjfja" + width + height);
        float f5 = width / height;
        float f6 = height / width;
        if (width > f3) {
            f = f3 * f6;
            if (f > f4) {
                f3 = f4 * f5;
                return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
            }
        } else {
            if (height > f4) {
                f2 = f4 * f5;
                if (f2 > f3) {
                    f4 = f3 * f6;
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
            } else if (f5 > 0.75f) {
                f = f3 * f6;
                if (f > f4) {
                    f3 = f4 * f5;
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
            } else {
                if (f6 > 1.5f) {
                    f2 = f4 * f5;
                    if (f2 > f3) {
                        f4 = f3 * f6;
                    }
                } else {
                    f = f3 * f6;
                    if (f > f4) {
                        f3 = f4 * f5;
                    }
                }
                return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
            }
            f3 = f2;
            return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
        }
        f4 = f;
        return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
    }

    public static Bitmap resizeBitmap2(Bitmap bit, int width, int height) {


        if (bit == null) {
            return null;
        }
        float wr = (float) width;
        float hr = (float) height;
        float wd = (float) bit.getWidth();
        float he = (float) bit.getHeight();
        float rat1 = wd / he;
        float rat2 = he / wd;

        Log.e("SIZE: ", "Sw: " + wr + "Sh: " + hr + "Bw: " + wd + "Bh: " + he + "R1:"
                + rat1 + "R2: " + rat2);

        if (wd > wr) {
            if (wr * rat2 > hr) {
                he = hr;
                wd = he * rat1;
                Log.e("Bw > Sw & Sw * R2: ", "Sw: " + wr + "Sh: " + hr + "Bw: " + wd + "Bh: " + he + "R1:"
                        + rat1 + "R2: " + rat2 + "Sw * R2: " + wr * rat2);
            } else {
                wd = wr;
                he = wd * rat2;
                Log.e("Bw > Sw", "Sw: " + wr + "Sh: " + hr + "Bw: " + wd + "Bh: " + he + "R1:"
                        + rat1 + "R2: " + rat2);
            }
        }
        else if (he > hr) {
            he = hr;
            wd = he * rat1;
            if (wd > wr) {
                wd = wr;
                he = wd * rat2;
                Log.e("Bh > Sh & Bw * R1: ", "Sw: " + wr + "Sh: " + hr + "Bw: " + wd + "Bh: " + he + "R1:"
                        + rat1 + "R2: " + rat2 + "Sw * R2: " + he * rat1);
            }
            Log.e("Bh > Sh", "Sw: " + wr + "Sh: " + hr + "Bw: " + wd + "Bh: " + he + "R1:"
                    + rat1 + "R2: " + rat2);
        }
        else if (rat1 > 0.75f) {
            if (wr * rat2 > hr) {
                he = hr;
                wd = he * rat1;
            } else {
                wd = wr;
                he = wd * rat2;
            }
            Log.e("R1 > 0.75f & Sw * R2: ", "Sw: " + wr + "Sh: " + hr + "Bw: " + wd + "Bh: " + he + "R1:"
                    + rat1 + "R2: " + rat2);
        }
        else if (rat2 > 1.5f) {
            he = hr;
            wd = he * rat1;
            if (wd > wr) {
                wd = wr;
                he = wd * rat2;
            }
            Log.e("R2 > 1.5f & Sw * R2: ", "Sw: " + wr + "Sh: " + hr + "Bw: " + wd + "Bh: " + he + "R1:"
                    + rat1 + "R2: " + rat2);
        }
        else if (wr * rat2 > hr) {
            he = hr;
            wd = he * rat1;
        } else {
            wd = wr;
            he = wd * rat2;
        }

        return Bitmap.createScaledBitmap(bit, (int) wd, (int) he, false);
    }


    public static int dpToPx(Context context, int i) {
        context.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    public static Bitmap mergelogo(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        float width2 = (float) bitmap2.getWidth();
        float height2 = (float) bitmap2.getHeight();
        float f = width2 / height2;
        float f2 = height2 / width2;
        if (width2 > width) {
            bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int) width, (int) (width * f2), false);
        } else if (height2 > height) {
            bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int) (f * height), (int) height, false);
        }
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, (float) (bitmap.getWidth() - bitmap2.getWidth()), (float) (bitmap.getHeight() - bitmap2.getHeight()), (Paint) null);
        return createBitmap;
    }

    public static Bitmap mergelogo(Bitmap bitmap, Bitmap bitmap2, float f) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap2, bitmap.getWidth(), bitmap.getHeight(), true), 0.0f, 0.0f, (Paint) null);
        return createBitmap;
    }

    public static Bitmap CropBitmapTransparency(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int i = -1;
        int height = bitmap.getHeight();
        int i2 = -1;
        int i3 = width;
        int i4 = 0;
        while (i4 < bitmap.getHeight()) {
            int i5 = i2;
            int i6 = i;
            int i7 = i3;
            for (int i8 = 0; i8 < bitmap.getWidth(); i8++) {
                if (((bitmap.getPixel(i8, i4) >> 24) & 255) > 0) {
                    if (i8 < i7) {
                        i7 = i8;
                    }
                    if (i8 > i6) {
                        i6 = i8;
                    }
                    if (i4 < height) {
                        height = i4;
                    }
                    if (i4 > i5) {
                        i5 = i4;
                    }
                }
            }
            i4++;
            i3 = i7;
            i = i6;
            i2 = i5;
        }
        if (i < i3 || i2 < height) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, i3, height, (i - i3) + 1, (i2 - height) + 1);
    }

    public static Bitmap bitmapmasking(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode((Xfermode) null);
        return createBitmap;
    }

    public static Bitmap getTiledBitmap(Context context, int i, int i2, int i3) {
        Rect rect = new Rect(0, 0, i2, i3);
        Paint paint = new Paint();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(context.getResources(), i, options), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawRect(rect, paint);
        return createBitmap;
    }

    public static Bitmap getColoredBitmap(int i, int i2, int i3) {
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawColor(i);
        return createBitmap;
    }

    public static Bitmap getThumbnail(Bitmap bitmap, int i, int i2) {
        Bitmap bitmap2;
        Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
        int width = copy.getWidth();
        int height = copy.getHeight();
        if (height > width) {
            bitmap2 = cropCenterBitmap(copy, width, width);
        } else {
            bitmap2 = cropCenterBitmap(copy, height, height);
        }
        return Bitmap.createScaledBitmap(bitmap2, i, i2, true);
    }

    public static Bitmap cropCenterBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width < i && height < i2) {
            return bitmap;
        }
        int i3 = 0;
        int i4 = width > i ? (width - i) / 2 : 0;
        if (height > i2) {
            i3 = (height - i2) / 2;
        }
        if (i > width) {
            i = width;
        }
        if (i2 > height) {
            i2 = height;
        }
        return Bitmap.createBitmap(bitmap, i4, i3, i, i2);
    }

    public static Bitmap scaleCenterCrop(Bitmap bitmap, int i, int i2) {
        float f = (float) i2;
        float width = (float) bitmap.getWidth();
        float f2 = (float) i;
        float height = (float) bitmap.getHeight();
        float max = Math.max(f / width, f2 / height);
        float f3 = width * max;
        float f4 = max * height;
        float f5 = (f - f3) / 2.0f;
        float f6 = (f2 - f4) / 2.0f;
        RectF rectF = new RectF(f5, f6, f3 + f5, f4 + f6);
        Bitmap createBitmap = Bitmap.createBitmap(i2, i, bitmap.getConfig());
        new Canvas(createBitmap).drawBitmap(bitmap, (Rect) null, rectF, (Paint) null);
        return createBitmap;
    }

    private void setTextSizeForWidth(Paint paint, float f, String str) {
        paint.setTextSize(48.0f);
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        paint.setTextSize((f * 48.0f) / ((float) rect.width()));
    }
}
