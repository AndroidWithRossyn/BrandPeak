package com.itsbusinessposter.idebrandvideo.items;

import com.google.gson.annotations.SerializedName;

public class UploadItem {

    @SerializedName("url")
    public String link;

    public UploadItem(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "UploadItem{" +
                "link='" + link + '\'' +
                '}';
    }
}
