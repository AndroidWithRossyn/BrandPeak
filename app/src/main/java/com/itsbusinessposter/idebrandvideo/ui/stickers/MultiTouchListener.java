package com.itsbusinessposter.idebrandvideo.ui.stickers;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;


public class MultiTouchListener implements View.OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    Bitmap bitmap;

    /* renamed from: bt */
    boolean f163bt = false;

    /* renamed from: gd */
    GestureDetector f164gd = null;
    public boolean isRotateEnabled = true;
    public boolean isRotationEnabled = false;
    public boolean isTranslateEnabled = true;
    private TouchCallbackListener listener = null;
    private int mActivePointerId = -1;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;

    public interface TouchCallbackListener {
        void onTouchCallback(View view);

        void onTouchMoveCallback(View view);

        void onTouchUpCallback(View view);
    }

    private static float adjustAngle(float f) {
        return f > 180.0f ? f - 360.0f : f < -180.0f ? f + 360.0f : f;
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D();
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector scaleGestureDetector) {
            this.mPivotX = scaleGestureDetector.getFocusX();
            this.mPivotY = scaleGestureDetector.getFocusY();
            this.mPrevSpanVector.set(scaleGestureDetector.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, ScaleGestureDetector scaleGestureDetector) {
            TransformInfo transformInfo = new TransformInfo();
            float f = 0.0f;
            transformInfo.deltaAngle = MultiTouchListener.this.isRotateEnabled ? Vector2D.getAngle(this.mPrevSpanVector, scaleGestureDetector.getCurrentSpanVector()) : 0.0f;
            transformInfo.deltaX = MultiTouchListener.this.isTranslateEnabled ? scaleGestureDetector.getFocusX() - this.mPivotX : 0.0f;
            if (MultiTouchListener.this.isTranslateEnabled) {
                f = scaleGestureDetector.getFocusY() - this.mPivotY;
            }
            transformInfo.deltaY = f;
            transformInfo.pivotX = this.mPivotX;
            transformInfo.pivotY = this.mPivotY;
            transformInfo.minimumScale = MultiTouchListener.this.minimumScale;
            transformInfo.maximumScale = MultiTouchListener.this.maximumScale;
            MultiTouchListener.this.move(view, transformInfo);
            return false;
        }
    }

    private static void adjustTranslation(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }

    private static void computeRenderOffset(View view, float f, float f2) {
        if (view.getPivotX() != f || view.getPivotY() != f2) {
            float[] fArr = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr);
            view.setPivotX(f);
            view.setPivotY(f2);
            float[] fArr2 = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr2);
            float f3 = fArr2[1] - fArr[1];
            view.setTranslationX(view.getTranslationX() - (fArr2[0] - fArr[0]));
            view.setTranslationY(view.getTranslationY() - f3);
        }
    }

    public MultiTouchListener setGestureListener(GestureDetector gestureDetector) {
        this.f164gd = gestureDetector;
        return this;
    }

    public MultiTouchListener setOnTouchCallbackListener(TouchCallbackListener touchCallbackListener) {
        this.listener = touchCallbackListener;
        return this;
    }

    public MultiTouchListener enableRotation(boolean z) {
        this.isRotationEnabled = z;
        return this;
    }

    public MultiTouchListener setMinScale(float f) {
        this.minimumScale = f;
        return this;
    }

    /* access modifiers changed from: private */
    public void move(View view, TransformInfo transformInfo) {
        if (this.isRotationEnabled) {
            view.setRotation(adjustAngle(view.getRotation() + transformInfo.deltaAngle));
        }
    }

    public boolean handleTransparency(View view, MotionEvent motionEvent) {
        try {
            boolean z = true;
            if (motionEvent.getAction() == 2 && this.f163bt) {
                return true;
            }
            if (motionEvent.getAction() != 1 || !this.f163bt) {
                int[] iArr = new int[2];
                view.getLocationOnScreen(iArr);
                int rawY = (int) (motionEvent.getRawY() - ((float) iArr[1]));
                float rotation = view.getRotation();
                Matrix matrix = new Matrix();
                matrix.postRotate(-rotation);
                float[] fArr = {(float) ((int) (motionEvent.getRawX() - ((float) iArr[0]))), (float) rawY};
                matrix.mapPoints(fArr);
                int i = (int) fArr[0];
                int i2 = (int) fArr[1];
                if (motionEvent.getAction() == 0) {
                    this.f163bt = false;
                    view.setDrawingCacheEnabled(true);
                    this.bitmap = Bitmap.createBitmap(view.getDrawingCache());
                    i = (int) (((float) i) * (((float) this.bitmap.getWidth()) / (((float) this.bitmap.getWidth()) * view.getScaleX())));
                    i2 = (int) (((float) i2) * (((float) this.bitmap.getWidth()) / (((float) this.bitmap.getHeight()) * view.getScaleX())));
                    view.setDrawingCacheEnabled(false);
                }
                if (i >= 0 && i2 >= 0 && i <= this.bitmap.getWidth()) {
                    if (i2 <= this.bitmap.getHeight()) {
                        if (this.bitmap.getPixel(i, i2) != 0) {
                            z = false;
                        }
                        if (motionEvent.getAction() != 0) {
                            return z;
                        }
                        this.f163bt = z;
                        return z;
                    }
                }
                return false;
            }
            this.f163bt = false;
            if (this.bitmap == null) {
                return true;
            }
            this.bitmap.recycle();
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.mScaleGestureDetector.onTouchEvent(view, motionEvent);
        FrameLayout relativeLayout = (FrameLayout) view.getParent();
        if (this.f164gd != null) {
            this.f164gd.onTouchEvent(motionEvent);
        }
        if (this.isTranslateEnabled) {
            int action = motionEvent.getAction();
            int actionMasked = motionEvent.getActionMasked() & action;
            int i = 0;
            if (actionMasked != 6) {
                switch (actionMasked) {
                    case 0:
                        if (relativeLayout != null) {
                            relativeLayout.requestDisallowInterceptTouchEvent(true);
                        }
                        if (this.listener != null) {
                            this.listener.onTouchCallback(view);
                        }
                        view.bringToFront();
                        this.mPrevX = motionEvent.getX();
                        this.mPrevY = motionEvent.getY();
                        this.mActivePointerId = motionEvent.getPointerId(0);
                        break;
                    case 1:
                        this.mActivePointerId = -1;
                        if (this.listener != null) {
                            this.listener.onTouchUpCallback(view);
                        }
                        float rotation = view.getRotation();
                        if (Math.abs(90.0f - Math.abs(rotation)) <= 5.0f) {
                            rotation = rotation > 0.0f ? 90.0f : -90.0f;
                        }
                        if (Math.abs(0.0f - Math.abs(rotation)) <= 5.0f) {
                            rotation = rotation > 0.0f ? 0.0f : -0.0f;
                        }
                        if (Math.abs(180.0f - Math.abs(rotation)) <= 5.0f) {
                            rotation = rotation > 0.0f ? 180.0f : -180.0f;
                        }
                        view.setRotation(rotation);
                        Log.i("testing", "Final Rotation : " + rotation);
                        break;
                    case 2:
                        if (relativeLayout != null) {
                            relativeLayout.requestDisallowInterceptTouchEvent(true);
                        }
                        if (this.listener != null) {
                            this.listener.onTouchMoveCallback(view);
                        }
                        int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex != -1) {
                            float x = motionEvent.getX(findPointerIndex);
                            float y = motionEvent.getY(findPointerIndex);
                            if (!this.mScaleGestureDetector.isInProgress()) {
                                adjustTranslation(view, x - this.mPrevX, y - this.mPrevY);
                                break;
                            }
                        }
                        break;
                    case 3:
                        this.mActivePointerId = -1;
                        break;
                }
            } else {
                int i2 = (65280 & action) >> 8;
                if (motionEvent.getPointerId(i2) == this.mActivePointerId) {
                    if (i2 == 0) {
                        i = 1;
                    }
                    this.mPrevX = motionEvent.getX(i);
                    this.mPrevY = motionEvent.getY(i);
                    this.mActivePointerId = motionEvent.getPointerId(i);
                }
            }
        }
        return true;
    }
}
