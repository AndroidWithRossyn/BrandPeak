package com.itsbusinessposter.idebrandvideo.utils;

import android.view.MotionEvent;
import android.view.View;

public class OnDragTouchListener implements View.OnTouchListener {


    public interface OnDragActionListener {

        void onDragStart(View view);


        void onDragEnd(View view, Boolean delete);
    }

    private View mView;
    private View mParent;
    private boolean isDragging;
    private boolean isInitialized = false;

    private int width;
    private float xWhenAttached;
    private float maxLeft;
    private float maxRight;
    private float dX;

    private int height;
    private float yWhenAttached;
    private float maxTop;
    private float maxBottom;
    private float dY;
    private final float SCROLL_THRESHOLD = 10;
    private float mDownX;
    private float mDownY;
    private boolean isOnClick = false;
    private Boolean isCancelInflated = false, isCancelShrinked = true, delete = false;
    private int bottomMargin;

    private OnDragActionListener mOnDragActionListener;

    public OnDragTouchListener(View view, int bottomMargin) {
        this(view, (View) view.getParent(), bottomMargin, null);
    }

    public OnDragTouchListener(View view, View parent, int bottomMargin) {
        this(view, parent, bottomMargin, null);
    }

    public OnDragTouchListener(View view,  int bottomMargin, OnDragActionListener onDragActionListener) {
        this(view, (View) view.getParent(), bottomMargin, onDragActionListener);
    }

    public OnDragTouchListener(View view, View parent,int bottomMargin, OnDragActionListener onDragActionListener) {
        initListener(view, parent, bottomMargin);
        setOnDragActionListener(onDragActionListener);
    }

    public void setOnDragActionListener(OnDragActionListener onDragActionListener) {
        mOnDragActionListener = onDragActionListener;
    }

    public void initListener(View view, View parent, int bottomMar) {
        mView = view;
        mParent = parent;
        isDragging = false;
        isInitialized = false;
        bottomMargin = bottomMar;
    }

    public void updateBounds() {
        updateViewBounds();
        updateParentBounds();
        isInitialized = true;
    }

    public void updateViewBounds() {
        width = mView.getWidth();
        xWhenAttached = mView.getX();
        dX = 0;

        height = mView.getHeight();
        yWhenAttached = mView.getY();
        dY = 0;
    }

    public void updateParentBounds() {
        maxLeft = 0;
        maxRight = maxLeft + mParent.getWidth();

        maxTop = 0;
        maxBottom = maxTop + mParent.getHeight();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDragging) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    onDragFinish(delete);
                    break;
                case MotionEvent.ACTION_MOVE:

                    float[] bounds = new float[4];
                    // LEFT
                    bounds[0] = event.getRawX() + dX;
                    if (bounds[0] < maxLeft) {
                        bounds[0] = maxLeft;
                    }
                    // RIGHT
                    bounds[2] = bounds[0] + width;
                    if (bounds[2] > maxRight) {
                        bounds[2] = maxRight;
                        bounds[0] = bounds[2] - width;
                    }
                    // TOP
                    bounds[1] = event.getRawY() + dY;
                    if (bounds[1] < maxTop) {
                        bounds[1] = maxTop;
                    }
                    // BOTTOM
                    bounds[3] = bounds[1] + height;
                    if (bounds[3] > maxBottom) {
                        bounds[3] = maxBottom;
                        bounds[1] = bounds[3] - height;
                    }
                    mView.animate().x(bounds[0]).y(bounds[1]).setDuration(0).start();

                    break;
            }
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    mDownX = event.getX();
                    mDownY = event.getY();
                    isOnClick = true;

                    return true;
                case MotionEvent.ACTION_MOVE:
                    if (isOnClick && (Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)) {
                        isOnClick = false;
                        isDragging = true;

                        if (!isInitialized) {
                            updateBounds();
                        }
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        if (mOnDragActionListener != null) {
                            mOnDragActionListener.onDragStart(mView);
                        }
                    }
                    return true;

                case MotionEvent.ACTION_UP:
                    if (isOnClick) {
                        v.performClick();
                        isOnClick = false;
                    }
                    return true;
            }
        }
        return false;
    }

    private void onDragFinish(Boolean delete) {
        if (mOnDragActionListener != null) {
            mOnDragActionListener.onDragEnd(mView, delete);
        }

        dX = 0;
        dY = 0;
        isDragging = false;
    }
}