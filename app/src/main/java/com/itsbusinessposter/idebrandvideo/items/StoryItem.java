package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "story_items", primaryKeys = "storyId")
public class StoryItem {

    @NonNull
    @SerializedName("storyId")
    public String storyId;

    @SerializedName("name")
    public String title;

    @SerializedName("image")
    public String imageUrl;

    @SerializedName("id")
    public String festivalId;

    @SerializedName("storyType")
    public String type;

    @SerializedName("externalLink")
    public String externalLink;

    @SerializedName("video")
    public boolean video;

    public StoryItem(@NonNull String storyId, String title, String imageUrl, String festivalId, String type, String externalLink, boolean video) {
        this.storyId = storyId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.festivalId = festivalId;
        this.type = type;
        this.externalLink = externalLink;
        this.video = video;
    }

    @Override
    public String toString() {
        return "StoryItem{" +
                "storyId='" + storyId + '\'' +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", festivalId='" + festivalId + '\'' +
                ", type='" + type + '\'' +
                ", externalLink='" + externalLink + '\'' +
                ", video=" + video +
                '}';
    }
}
