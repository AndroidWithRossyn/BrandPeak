package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "custom_category", primaryKeys = "id")
public class CustomCategory {

    @NonNull
    @SerializedName("customCategoryId")
    public String id;

    @SerializedName("customCategoryName")
    public String title;

    @SerializedName("customCategoryIcon")
    public String image;

    public CustomCategory(@NonNull String id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    @Override
    public String toString() {
        return "CustomCategory{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
