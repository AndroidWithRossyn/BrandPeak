package com.itsbusinessposter.idebrandvideo.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class AddTextItem implements Serializable {

    public View viewLayout;
    public TextView tv_text;
    public ImageView iv_close;
    public ImageView iv_edit;

    public AddTextItem(View viewLayout, TextView tv_text, ImageView iv_close, ImageView iv_edit) {
        this.viewLayout = viewLayout;
        this.tv_text = tv_text;
        this.iv_close = iv_close;
        this.iv_edit = iv_edit;
    }
}
