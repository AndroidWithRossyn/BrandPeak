package com.itsbusinessposter.idebrandvideo.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "str_model")
public class StickerModel {

    @PrimaryKey (autoGenerate = true)
    public int id;

    @SerializedName("stickerCategoryId")
    public String stickerCategoryId;

    @SerializedName("stickerCategoryName")
    public String stickerCategoryName;

    @TypeConverters
    @SerializedName("sticker")
    public List<StickerItem> stickers;

    public StickerModel(int id, String stickerCategoryId, String stickerCategoryName, List<StickerItem> stickers) {
        this.id = id;
        this.stickerCategoryId = stickerCategoryId;
        this.stickerCategoryName = stickerCategoryName;
        this.stickers = stickers;
    }
}
