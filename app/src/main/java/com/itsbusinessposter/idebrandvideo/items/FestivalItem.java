package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "festival", primaryKeys = "id")
public class FestivalItem {

    @NonNull
    @SerializedName("festivalId")
    public String id;

    @SerializedName("festivalTitle")
    public String name;

    @SerializedName("festivalImage")
    public String image;

    @SerializedName("festivalDate")
    public String date;

    @SerializedName("isActive")
    public boolean isActive;

    @SerializedName("video")
    public boolean video;

    public FestivalItem(@NonNull String id, String name, String image, String date, boolean isActive, boolean video) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.isActive = isActive;
        this.video = video;
    }

    @Override
    public String toString() {
        return "FestivalItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", isActive=" + isActive +
                ", video=" + video +
                '}';
    }
}

