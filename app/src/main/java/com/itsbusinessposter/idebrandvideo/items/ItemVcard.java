package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "vCard", primaryKeys = "id")
public class ItemVcard {

    @NonNull
    @SerializedName("cardId")
    public String id;

    @SerializedName("cardName")
    public String cardName;

    @SerializedName("cardImage")
    public String link;

    public ItemVcard(@NonNull String id, String link) {
        this.id = id;
        this.link = link;
    }

    @Override
    public String toString() {
        return "ItemVcard{" +
                "id='" + id + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
