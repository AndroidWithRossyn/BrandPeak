package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "feature", primaryKeys = "id")
public class FeatureItem {

    @NonNull
    @SerializedName("featureId")
    public String id;

    @SerializedName("id")
    public String festId;

    @SerializedName("title")
    public String title;

    @SerializedName("image")
    public String image;

    @SerializedName("type")
    public String type;

    @SerializedName("video")
    public boolean video;

    @TypeConverters
    @SerializedName("post")
    public List<PostItem> postItemList;

    public FeatureItem(@NonNull String id, String festId, String title, String image, String type, boolean video, List<PostItem> postItemList) {
        this.id = id;
        this.festId = festId;
        this.title = title;
        this.image = image;
        this.type = type;
        this.video = video;
        this.postItemList = postItemList;
    }
}
