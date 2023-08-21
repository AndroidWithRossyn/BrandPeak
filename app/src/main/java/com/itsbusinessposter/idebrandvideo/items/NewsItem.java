package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "news", primaryKeys = "id")
public class NewsItem implements Serializable {

    @NonNull
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("image")
    public String imageUrl;
    @SerializedName("date")
    public String date;
    @SerializedName("description")
    public String description;

    public NewsItem(String id, String title, String imageUrl, String date, String description) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.date = date;
        this.description = description;
    }
}
