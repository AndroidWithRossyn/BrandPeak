package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "subject", primaryKeys = "id")
public class SubjectItem {

    @NonNull
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;

    public SubjectItem(@NonNull String id, String title) {
        this.id = id;
        this.title = title;
    }
}
