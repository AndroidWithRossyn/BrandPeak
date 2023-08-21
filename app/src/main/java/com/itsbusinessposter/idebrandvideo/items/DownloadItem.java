package com.itsbusinessposter.idebrandvideo.items;

import android.net.Uri;

public class DownloadItem {

    public Uri uri;

    public boolean isVideo;

    public DownloadItem(Uri uri, boolean isVideo) {
        this.uri = uri;
        this.isVideo = isVideo;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
