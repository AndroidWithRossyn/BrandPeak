package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "stickers", primaryKeys = "stickerId")
public class StickerItem {

    @NonNull
    @SerializedName("stickerId")
    public String stickerId;

    @SerializedName("stickerImage")
    public String stickerImage;

    public StickerItem(String stickerId, String stickerImage) {
        this.stickerId = stickerId;
        this.stickerImage = stickerImage;
    }
}
