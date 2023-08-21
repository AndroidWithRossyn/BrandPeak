package com.itsbusinessposter.idebrandvideo.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "main_str")
public class MainStrModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters
    @SerializedName("StickerCategory")
    public List<StickerCategory> strCategories;

    @TypeConverters
    @SerializedName("data")
    public List<StickerModel> strModelList;

    public MainStrModel(int id, List<StickerCategory> strCategories, List<StickerModel> strModelList) {
        this.id = id;
        this.strCategories = strCategories;
        this.strModelList = strModelList;
    }
}
