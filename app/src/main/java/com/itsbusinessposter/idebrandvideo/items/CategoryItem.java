package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "category", primaryKeys = "id")
public class CategoryItem {

    @NonNull
    @SerializedName("categoryId")
    public String id;

    @SerializedName("categoryName")
    public String name;

    @SerializedName("categoryIcon")
    public String image;

    @SerializedName("video")
    public boolean video;

    public CategoryItem(@NonNull String id, String name, String image, boolean video) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.video = video;
    }

    @Override
    public String toString() {
        return "CategoryItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", video=" + video +
                '}';
    }
}

