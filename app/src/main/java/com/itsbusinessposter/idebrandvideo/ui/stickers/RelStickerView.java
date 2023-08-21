package com.itsbusinessposter.idebrandvideo.ui.stickers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.itsbusinessposter.idebrandvideo.R;
import com.itsbusinessposter.idebrandvideo.utils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RelStickerView extends RelativeLayout
        implements MultiTouchListener.TouchCallbackListener {
    public static final String TAG = "ResizableStickerView";
    double angle = 0.0d;
    int baseh;
    int basew;
    int basex;
    int basey;
    private ImageView border_iv;
    private Bitmap btmp = null;

    /* renamed from: cX */
    float f165cX = 0.0f;

    /* renamed from: cY */
    float f166cY = 0.0f;
    private String colorType = "colored";
    private Context context;
    double dAngle = 0.0d;
    private ImageView delete_iv;
    private String drawableId;
    /* access modifiers changed from: private */
    public int f26s;
    private String field_four = "";
    private int field_one = 0;
    private String field_three = "";
    /* access modifiers changed from: private */
    public String field_two = "0,0";
    private ImageView flip_iv;
    /* access modifiers changed from: private */

    /* renamed from: he */
    public int f167he;
    float heightMain = 0.0f;
    private int hueProg = 1;
    private int imgAlpha = 255;
    private int imgColor = 0;
    private boolean isBorderVisible = false;
    private boolean isColorFilterEnable = false;
    public boolean isMultiTouchEnabled = true;
    /* access modifiers changed from: private */
    public int leftMargin = 0;
    /* access modifiers changed from: private */
    public TouchEventListener listener = null;
    private OnTouchListener mTouchListener1 = new TouchListerner();
    public ImageView main_iv;
    int margl;
    int margt;
    private OnTouchListener rTouchListener = new TouchListner1();
    private Uri resUri = null;
    private ImageView rotate_iv;
    private float rotation;
    Animation scale;
    private int scaleRotateProg = 0;
    private ImageView scale_iv;
    int screenHeight = 300;
    int screenWidth = 300;
    /* access modifiers changed from: private */
    public String stkr_path = "";
    double tAngle = 0.0d;
    /* access modifiers changed from: private */
    public int topMargin = 0;
    double vAngle = 0.0d;
    /* access modifiers changed from: private */

    /* renamed from: wi */
    public int f168wi;
    float widthMain = 0.0f;
    private int xRotateProg = 0;
    private int yRotateProg = 0;
    private float yRotation;
    private int zRotateProg = 0;
    Animation zoomInScale;
    Animation zoomOutScale;
    private boolean isImage = false;

    public interface TouchEventListener {
        void onDelete();

        void onEdit(View view, Uri uri);

        void onRotateDown(View view);

        void onRotateMove(View view);

        void onRotateUp(View view);

        void onScaleDown(View view);

        void onScaleMove(View view);

        void onScaleUp(View view);

        void onTouchDown(View view);

        void onTouchMove(View view);

        void onTouchUp(View view);

        void onMainClick(View view);
    }

    public RelStickerView(Context context2, boolean isImage) {
        super(context2);
        this.isImage = isImage;
        init(context2);
    }

    public RelStickerView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        init(context2);
    }

    public RelStickerView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        init(context2);
    }

    public RelStickerView setOnTouchCallbackListener(TouchEventListener touchEventListener) {
        this.listener = touchEventListener;
        return this;
    }

    public void init(Context context2) {
        this.context = context2;
        this.main_iv = new ImageView(this.context);
        this.scale_iv = new ImageView(this.context);
        this.border_iv = new ImageView(this.context);
        this.flip_iv = new ImageView(this.context);
        this.rotate_iv = new ImageView(this.context);
        this.delete_iv = new ImageView(this.context);
        this.f26s = dpToPx(this.context, 25);
        this.f168wi = dpToPx(this.context, 200);
        this.f167he = dpToPx(this.context, 200);
        this.scale_iv.setImageResource(R.drawable.sticker_scale);
        this.border_iv.setImageResource(R.drawable.sticker_border_gray);
        if (isImage) {
            this.flip_iv.setImageResource(R.drawable.sticker_done);
        } else {
            this.flip_iv.setImageResource(R.drawable.sticker_flip);
        }
        this.rotate_iv.setImageResource(R.drawable.sticker_rotate);
        this.delete_iv.setImageResource(R.drawable.sticker_delete1);
        LayoutParams layoutParams = new LayoutParams(this.f168wi, this.f167he);
        LayoutParams layoutParams2 = new LayoutParams(-1, -1);
        layoutParams2.setMargins(5, 5, 5, 5);
        if (Build.VERSION.SDK_INT >= 17) {
            layoutParams2.addRule(17);
        } else {
            layoutParams2.addRule(1);
        }
        LayoutParams layoutParams3 = new LayoutParams(this.f26s, this.f26s);
        layoutParams3.addRule(12);
        layoutParams3.addRule(11);
        layoutParams3.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams4 = new LayoutParams(this.f26s, this.f26s);
        layoutParams4.addRule(10);
        layoutParams4.addRule(11);
        layoutParams4.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams5 = new LayoutParams(this.f26s, this.f26s);
        layoutParams5.addRule(12);
        layoutParams5.addRule(9);
        layoutParams5.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams6 = new LayoutParams(this.f26s, this.f26s);
        layoutParams6.addRule(10);
        layoutParams6.addRule(9);
        layoutParams6.setMargins(5, 5, 5, 5);
        LayoutParams layoutParams7 = new LayoutParams(-1, -1);
        setLayoutParams(layoutParams);
        setBackgroundResource(R.drawable.sticker_border_gray);
        addView(this.border_iv);
        this.border_iv.setLayoutParams(layoutParams7);
        this.border_iv.setScaleType(ImageView.ScaleType.FIT_XY);
        this.border_iv.setTag("border_iv");
        addView(this.main_iv);
        this.main_iv.setLayoutParams(layoutParams2);
        addView(this.flip_iv);
        this.flip_iv.setLayoutParams(layoutParams4);
        this.flip_iv.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (isImage) {
                    RelStickerView.this.listener.onMainClick(view);
                } else {
                    ImageView imageView = RelStickerView.this.main_iv;
                    float f = -180.0f;
                    if (RelStickerView.this.main_iv.getRotationY() == -180.0f) {
                        f = 0.0f;
                    }
                    imageView.setRotationY(f);
                    RelStickerView.this.main_iv.invalidate();
                    RelStickerView.this.requestLayout();
                }
            }
        });
        addView(this.rotate_iv);
        this.rotate_iv.setLayoutParams(layoutParams5);
        this.rotate_iv.setOnTouchListener(this.rTouchListener);
        addView(this.delete_iv);
        this.delete_iv.setLayoutParams(layoutParams6);
        this.delete_iv.setOnClickListener(new DeleteClick());
        addView(this.scale_iv);
        this.scale_iv.setLayoutParams(layoutParams3);
        this.scale_iv.setOnTouchListener(this.mTouchListener1);
        this.scale_iv.setTag("scale_iv");
        this.rotation = getRotation();
        this.scale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_anim_view);
        this.zoomOutScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_out_view);
        this.zoomInScale = AnimationUtils.loadAnimation(getContext(), R.anim.sticker_scale_zoom_in_view);
        this.isMultiTouchEnabled = setDefaultTouchListener(true);
    }

    public boolean setDefaultTouchListener(boolean z) {
        if (z) {
            setOnTouchListener(new MultiTouchListener().enableRotation(true).setOnTouchCallbackListener(this));
            return true;
        }
        setOnTouchListener((OnTouchListener) null);
        return false;
    }

    public void setBorderVisibility(boolean z) {
        this.isBorderVisible = z;
        if (!z) {
            this.border_iv.setVisibility(GONE);
            this.scale_iv.setVisibility(GONE);
            this.flip_iv.setVisibility(GONE);
            this.rotate_iv.setVisibility(GONE);
            this.delete_iv.setVisibility(GONE);
            setBackgroundResource(VISIBLE);
            if (this.isColorFilterEnable) {
                this.main_iv.setColorFilter(Color.parseColor("#303828"));
            }
        } else if (this.border_iv.getVisibility() != VISIBLE) {
            this.border_iv.setVisibility(VISIBLE);
            this.scale_iv.setVisibility(VISIBLE);
            this.flip_iv.setVisibility(VISIBLE);
            this.rotate_iv.setVisibility(VISIBLE);
            this.delete_iv.setVisibility(VISIBLE);
            setBackgroundResource(R.drawable.sticker_border_gray);
            this.main_iv.startAnimation(this.scale);
        }
    }

    public boolean getBorderVisbilty() {
        return this.isBorderVisible;
    }

    public boolean getIsImage() {
        return this.isImage;
    }

    public void opecitySticker(int i) {
        try {
            this.main_iv.setAlpha(i);
            this.imgAlpha = i;
        } catch (Exception unused) {
        }
    }

    public int getHueProg() {
        return this.hueProg;
    }

    public void setHueProg(int i) {
        this.hueProg = i;
        if (this.hueProg == 0) {
            this.main_iv.setColorFilter(-1);
        } else if (this.hueProg == 100) {
            this.main_iv.setColorFilter(ViewCompat.MEASURED_STATE_MASK);
        } else {
//            this.main_iv.setColorFilter(ColorFilterGenerator.adjustHue((float) i));
        }
    }

    public String getColorType() {
        return this.colorType;
    }

    public int getAlphaProg() {
        return this.imgAlpha;
    }

    public void setAlphaProg(int i) {
        opecitySticker(i);
    }

    public int getColor() {
        return this.imgColor;
    }

    public void setColor(int i) {
        try {
            this.main_iv.setColorFilter(i);
            this.imgColor = i;
        } catch (Exception unused) {
        }
    }

    public void setBgDrawable(String str) {
        Glide.with(this.context).load(Integer.valueOf(getResources().getIdentifier(str,
                "drawable", this.context.getPackageName())))
                .apply(new RequestOptions().dontAnimate().placeholder((int)
                        R.drawable.no_image).error((int)
                        R.drawable.no_image)).into(this.main_iv);
        this.drawableId = str;
        this.main_iv.startAnimation(this.zoomOutScale);
    }

    public void setStrPath(String str) {
        try {
            this.btmp = ImageUtils.getResampleImageBitmap(Uri.parse(str), this.context, this.screenWidth > this.screenHeight ? this.screenWidth : this.screenHeight);
            this.main_iv.setImageBitmap(this.btmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.stkr_path = str;
        this.main_iv.startAnimation(this.zoomOutScale);
    }

    public Uri getMainImageUri() {
        return this.resUri;
    }

    public void setMainImageUri(Uri uri) {
        this.resUri = uri;
        this.main_iv.setImageURI(this.resUri);
    }

    public Bitmap getMainImageBitmap() {
        return this.btmp;
    }

    public void setMainImageBitmap(Bitmap bitmap) {
        this.main_iv.setImageBitmap(bitmap);
    }

    public void optimize(float f, float f2) {
        setX(getX() * f);
        setY(getY() * f2);
        getLayoutParams().width = (int) (((float) this.f168wi) * f);
        getLayoutParams().height = (int) (((float) this.f167he) * f2);
    }

    public void optimizeScreen(float f, float f2) {
        this.screenHeight = (int) f2;
        this.screenWidth = (int) f;
    }

    public void setMainLayoutWH(float f, float f2) {
        this.widthMain = f;
        this.heightMain = f2;
    }

    public float getMainWidth() {
        return this.widthMain;
    }

    public float getMainHeight() {
        return this.heightMain;
    }

    public void incrX() {
        setX(getX() + 2.0f);
    }

    public void decX() {
        setX(getX() - 2.0f);
    }

    public void incrY() {
        setY(getY() + 2.0f);
    }

    public void decY() {
        setY(getY() - 2.0f);
    }

    public ElementInfo getComponentInfo() {
        if (this.btmp != null) {
            this.stkr_path = saveBitmapObject1(this.btmp);
        }
        ElementInfo elementInfo = new ElementInfo();
        elementInfo.setPOS_X(getX());
        elementInfo.setPOS_Y(getY());
        elementInfo.setWIDTH(this.f168wi);
        elementInfo.setHEIGHT(this.f167he);
        elementInfo.setRES_ID(this.drawableId);
        elementInfo.setSTC_COLOR(this.imgColor);
        elementInfo.setRES_URI(this.resUri);
        elementInfo.setSTC_OPACITY(this.imgAlpha);
        elementInfo.setCOLORTYPE(this.colorType);
        elementInfo.setBITMAP(this.btmp);
        elementInfo.setROTATION(getRotation());
        elementInfo.setY_ROTATION(this.main_iv.getRotationY());
        elementInfo.setXRotateProg(this.xRotateProg);
        elementInfo.setYRotateProg(this.yRotateProg);
        elementInfo.setZRotateProg(this.zRotateProg);
        elementInfo.setScaleProg(this.scaleRotateProg);
        elementInfo.setSTKR_PATH(this.stkr_path);
        elementInfo.setSTC_HUE(this.hueProg);
        elementInfo.setFIELD_ONE(this.field_one);
        elementInfo.setFIELD_TWO(this.field_two);
        elementInfo.setFIELD_THREE(this.field_three);
        elementInfo.setFIELD_FOUR(this.field_four);
        return elementInfo;
    }

    public void setComponentInfo(ElementInfo elementInfo) {
        this.f168wi = elementInfo.getWIDTH();
        this.f167he = elementInfo.getHEIGHT();
        this.drawableId = elementInfo.getRES_ID();
        this.resUri = elementInfo.getRES_URI();
        this.btmp = elementInfo.getBITMAP();
        this.rotation = elementInfo.getROTATION();
        this.imgColor = elementInfo.getSTC_COLOR();
        this.yRotation = elementInfo.getY_ROTATION();
        this.imgAlpha = elementInfo.getSTC_OPACITY();
        this.stkr_path = elementInfo.getSTKR_PATH();
        this.colorType = elementInfo.getCOLORTYPE();
        this.hueProg = elementInfo.getSTC_HUE();
        this.field_two = elementInfo.getFIELD_TWO();
        if (!this.stkr_path.equals("")) {
            setStrPath(this.stkr_path);
        } else if (this.drawableId.equals("")) {
            this.main_iv.setImageBitmap(this.btmp);
        } else {
            setBgDrawable(this.drawableId);
        }
        if (this.colorType.equals("white")) {
            setColor(this.imgColor);
        } else {
            setHueProg(this.hueProg);
        }
        setRotation(this.rotation);
        opecitySticker(this.imgAlpha);
        if (this.field_two.equals("")) {
            getLayoutParams().width = this.f168wi;
            getLayoutParams().height = this.f167he;
            setX(elementInfo.getPOS_X());
            setY(elementInfo.getPOS_Y());
        } else {
            String[] split = this.field_two.split(",");
            int parseInt = Integer.parseInt(split[0]);
            int parseInt2 = Integer.parseInt(split[1]);
            ((LayoutParams) getLayoutParams()).leftMargin = parseInt;
            ((LayoutParams) getLayoutParams()).topMargin = parseInt2;
            getLayoutParams().width = this.f168wi;
            getLayoutParams().height = this.f167he;
            setX(elementInfo.getPOS_X() + ((float) (parseInt * -1)));
            setY(elementInfo.getPOS_Y() + ((float) (parseInt2 * -1)));
        }
        if (elementInfo.getTYPE() == "SHAPE") {
            this.flip_iv.setVisibility(GONE);
        }
        if (elementInfo.getTYPE() == "STICKER") {
//            this.flip_iv.setVisibility(GONE);
        }
        this.main_iv.setRotationY(this.yRotation);
    }

    private void saveStkrBitmap(final Bitmap bitmap) {
        final ProgressDialog show = ProgressDialog.show(this.context, "", "", true);
        show.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    String unused = RelStickerView.this.stkr_path = RelStickerView.this.saveBitmapObject1(bitmap);
                } catch (Exception e) {
                    Log.i("testing", "Exception " + e.getMessage());
                    e.printStackTrace();
                } catch (Throwable unused2) {
                }
                show.dismiss();
            }
        }).start();
        show.setOnDismissListener(new RingProgressClick());
    }

    /* access modifiers changed from: private */
    public String saveBitmapObject1(Bitmap bitmap) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), ".Poster Maker Stickers/category1");
        file.mkdirs();
        File file2 = new File(file, "raw1-" + System.currentTimeMillis() + ".png");
        String absolutePath = file2.getAbsolutePath();
        if (file2.exists()) {
            file2.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
            return absolutePath;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("testing", "Exception" + e.getMessage());
            return "";
        }
    }

    public int dpToPx(Context context2, int i) {
        context2.getResources();
        return (int) (Resources.getSystem().getDisplayMetrics().density * ((float) i));
    }

    private double getLength(double d, double d2, double d3, double d4) {
        return Math.sqrt(Math.pow(d4 - d2, 2.0d) + Math.pow(d3 - d, 2.0d));
    }

    public void enableColorFilter(boolean z) {
        this.isColorFilterEnable = z;
    }

    public void onTouchCallback(View view) {
        if (this.listener != null) {
            this.listener.onTouchDown(view);
        }
    }

    public void onTouchUpCallback(View view) {
        if (this.listener != null) {
            this.listener.onTouchUp(view);
        }
    }

    public void onTouchMoveCallback(View view) {
        if (this.listener != null) {
            this.listener.onTouchMove(view);
        }
    }

    class TouchListerner implements OnTouchListener {
        TouchListerner() {
        }

        @SuppressLint({"NewApi"})
        public boolean onTouch(View view, MotionEvent motionEvent) {
            RelStickerView relStickerView = (RelStickerView) view.getParent();
            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) RelStickerView.this.getLayoutParams();
            switch (motionEvent.getAction()) {
                case 0:
                    if (relStickerView != null) {
                        relStickerView.requestDisallowInterceptTouchEvent(true);
                    }
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onScaleDown(RelStickerView.this);
                    }
                    RelStickerView.this.invalidate();
                    RelStickerView.this.basex = rawX;
                    RelStickerView.this.basey = rawY;
                    RelStickerView.this.basew = RelStickerView.this.getWidth();
                    RelStickerView.this.baseh = RelStickerView.this.getHeight();
                    RelStickerView.this.getLocationOnScreen(new int[2]);
                    RelStickerView.this.margl = layoutParams.leftMargin;
                    RelStickerView.this.margt = layoutParams.topMargin;
                    break;
                case 1:
                    int unused = RelStickerView.this.f168wi = RelStickerView.this.getLayoutParams().width;
                    int unused2 = RelStickerView.this.f167he = RelStickerView.this.getLayoutParams().height;
                    int unused3 = RelStickerView.this.leftMargin = ((FrameLayout.LayoutParams) RelStickerView.this.getLayoutParams()).leftMargin;
                    int unused4 = RelStickerView.this.topMargin = ((FrameLayout.LayoutParams) RelStickerView.this.getLayoutParams()).topMargin;
                    RelStickerView relStickerView2 = RelStickerView.this;
                    String unused5 = relStickerView2.field_two = String.valueOf(RelStickerView.this.leftMargin) + "," + String.valueOf(RelStickerView.this.topMargin);
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onScaleUp(RelStickerView.this);
                        break;
                    }
                    break;
                case 2:
                    if (relStickerView != null) {
                        relStickerView.requestDisallowInterceptTouchEvent(true);
                    }
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onScaleMove(RelStickerView.this);
                    }
                    float degrees = (float) Math.toDegrees(Math.atan2((double) (rawY - RelStickerView.this.basey), (double) (rawX - RelStickerView.this.basex)));
                    if (degrees < 0.0f) {
                        degrees += 360.0f;
                    }
                    int i = rawX - RelStickerView.this.basex;
                    int i2 = rawY - RelStickerView.this.basey;
                    int i3 = i2 * i2;
                    int sqrt = (int) (Math.sqrt((double) ((i * i) + i3)) * Math.cos(Math.toRadians((double) (degrees - RelStickerView.this.getRotation()))));
                    int sqrt2 = (int) (Math.sqrt((double) ((sqrt * sqrt) + i3)) * Math.sin(Math.toRadians((double) (degrees - RelStickerView.this.getRotation()))));
                    int i4 = (sqrt * 2) + RelStickerView.this.basew;
                    int i5 = (sqrt2 * 2) + RelStickerView.this.baseh;
                    if (i4 > RelStickerView.this.f26s) {
                        layoutParams.width = i4;
                        layoutParams.leftMargin = RelStickerView.this.margl - sqrt;
                    }
                    if (i5 > RelStickerView.this.f26s) {
                        layoutParams.height = i5;
                        layoutParams.topMargin = RelStickerView.this.margt - sqrt2;
                    }
                    RelStickerView.this.setLayoutParams(layoutParams);
                    RelStickerView.this.performLongClick();
                    break;
            }
            return true;
        }
    }

    class TouchListner1 implements OnTouchListener {
        TouchListner1() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            RelStickerView relStickerView = (RelStickerView) view.getParent();
            switch (motionEvent.getAction()) {
                case 0:
                    if (relStickerView != null) {
                        relStickerView.requestDisallowInterceptTouchEvent(true);
                    }
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onRotateDown(RelStickerView.this);
                    }
                    Rect rect = new Rect();
                    ((View) view.getParent()).getGlobalVisibleRect(rect);
                    RelStickerView.this.f165cX = rect.exactCenterX();
                    RelStickerView.this.f166cY = rect.exactCenterY();
                    RelStickerView.this.vAngle = (double) ((View) view.getParent()).getRotation();
                    RelStickerView.this.tAngle = (Math.atan2((double) (RelStickerView.this.f166cY - motionEvent.getRawY()), (double) (RelStickerView.this.f165cX - motionEvent.getRawX())) * 180.0d) / 3.141592653589793d;
                    RelStickerView.this.dAngle = RelStickerView.this.vAngle - RelStickerView.this.tAngle;
                    break;
                case 1:
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onRotateUp(RelStickerView.this);
                        break;
                    }
                    break;
                case 2:
                    if (relStickerView != null) {
                        relStickerView.requestDisallowInterceptTouchEvent(true);
                    }
                    if (RelStickerView.this.listener != null) {
                        RelStickerView.this.listener.onRotateMove(RelStickerView.this);
                    }
                    RelStickerView.this.angle = (Math.atan2((double) (RelStickerView.this.f166cY - motionEvent.getRawY()), (double) (RelStickerView.this.f165cX - motionEvent.getRawX())) * 180.0d) / 3.141592653589793d;
                    ((View) view.getParent()).setRotation((float) (RelStickerView.this.angle + RelStickerView.this.dAngle));
                    ((View) view.getParent()).invalidate();
                    ((View) view.getParent()).requestLayout();
                    break;
            }
            return true;
        }
    }

    class DeleteClick implements OnClickListener {
        DeleteClick() {
        }

        public void onClick(View view) {
            final ViewGroup viewGroup = (ViewGroup) RelStickerView.this.getParent();
            RelStickerView.this.zoomInScale.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    viewGroup.removeView(RelStickerView.this);
                }
            });
            RelStickerView.this.main_iv.startAnimation(RelStickerView.this.zoomInScale);
            RelStickerView.this.setBorderVisibility(false);
            if (RelStickerView.this.listener != null) {
                RelStickerView.this.listener.onDelete();
            }
        }
    }

    class RingProgressClick implements DialogInterface.OnDismissListener {
        public void onDismiss(DialogInterface dialogInterface) {
        }

        RingProgressClick() {
        }
    }
}
