package com.itsbusinessposter.idebrandvideo.items;

import android.view.View;

public class FrameItem {

    public boolean is_from_url;
    public String imageUrl;
    public View layout;
    public int previewImage;
    public boolean is_premium;

    public FrameItem(boolean is_from_url, String imageUrl, View layout, int previewImage, boolean is_premium) {
        this.is_from_url = is_from_url;
        this.imageUrl = imageUrl;
        this.layout = layout;
        this.previewImage = previewImage;
        this.is_premium = is_premium;
    }
}
