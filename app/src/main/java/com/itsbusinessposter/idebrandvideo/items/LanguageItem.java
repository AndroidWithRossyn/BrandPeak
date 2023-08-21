package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "language", primaryKeys = "id")
public class LanguageItem {

    @NonNull
    @SerializedName("id")
    public String id;
    public String image;
    @SerializedName("title")
    public String title;
    public boolean isChecked;

    public LanguageItem(@NonNull String id, String image, String title, boolean isChecked) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "LanguageItem{" +
                "id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
