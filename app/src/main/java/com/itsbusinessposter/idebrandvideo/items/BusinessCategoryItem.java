package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "business_category", primaryKeys = "businessCategoryId")
public class BusinessCategoryItem {

    @NonNull
    @SerializedName("businessCategoryId")
    public String businessCategoryId;

    @SerializedName("businessCategoryName")
    public String businessCategoryName;

    @SerializedName("businessCategoryIcon")
    public String businessCategoryIcon;

    @SerializedName("video")
    public boolean video;

    public BusinessCategoryItem(@NonNull String businessCategoryId, String businessCategoryName, String businessCategoryIcon, boolean video) {
        this.businessCategoryId = businessCategoryId;
        this.businessCategoryName = businessCategoryName;
        this.businessCategoryIcon = businessCategoryIcon;
        this.video = video;
    }
}
