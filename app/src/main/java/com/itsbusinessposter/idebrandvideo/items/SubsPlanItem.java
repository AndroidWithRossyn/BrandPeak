package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "subs_plan", primaryKeys = "id")
public class SubsPlanItem {

    @NonNull
    @SerializedName("id")
    public String id;
    @SerializedName("planName")
    public String planName;
    public String planImage;
    @SerializedName("planPrice")
    public String planPrice;
    @SerializedName("discountPrice")
    public String planDiscount;
    @SerializedName("duration")
    public String planDuration;

    @TypeConverters
    @SerializedName("planDetail")
    public List<String> pointItemList;

    public SubsPlanItem(@NonNull String id, String planName, String planImage, String planPrice, String planDiscount, String planDuration, List<String> pointItemList) {
        this.id = id;
        this.planName = planName;
        this.planImage = planImage;
        this.planPrice = planPrice;
        this.planDiscount = planDiscount;
        this.planDuration = planDuration;
        this.pointItemList = pointItemList;
    }
}
