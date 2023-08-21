package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "userFrame", primaryKeys = "id")
public class UserFrame {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("frameImage")
    public String imageUrl;

    public UserFrame(@NonNull String id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }
}
