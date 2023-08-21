package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "offer", primaryKeys = "id")
public class OfferItem {

    @NonNull
    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("image")
    public String image;

    @SerializedName("banner")
    public String banner;

    @SerializedName("subscriptionId")
    public String subscriptionId;

    @SerializedName("subscriptionPlanName")
    public String subscriptionPlanName;

    public OfferItem(int id, String name, String image, String banner, String subscriptionId, String subscriptionPlanName) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.banner = banner;
        this.subscriptionId = subscriptionId;
        this.subscriptionPlanName = subscriptionPlanName;
    }
}
