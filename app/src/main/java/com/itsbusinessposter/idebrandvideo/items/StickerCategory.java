package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "stc_category", primaryKeys = "id")
public class StickerCategory {

    @NonNull
    @SerializedName("stickerCategoryId")
    public String id;

    @SerializedName("stickerCategoryName")
    public String name;

    public StickerCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
