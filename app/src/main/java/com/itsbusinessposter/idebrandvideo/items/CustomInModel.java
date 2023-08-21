package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "custom_in", primaryKeys = "id")
public class CustomInModel {

    @NonNull
    @SerializedName("customCategoryId")
    public String id;

    @SerializedName("customCategoryName")
    public String title;

    @SerializedName("customCategoryIcon")
    public String image;

    @TypeConverters
    @SerializedName("posts")
    public List<PostItem> postItemList;

    public CustomInModel(@NonNull String id, String title, String image, List<PostItem> postItemList) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.postItemList = postItemList;
    }
}
