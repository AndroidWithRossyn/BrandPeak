package com.itsbusinessposter.idebrandvideo.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "custom_model")
public class CustomModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters
    @SerializedName("category")
    public List<CustomCategory> categories;

    @TypeConverters
    @SerializedName("data")
    public List<CustomInModel> customInModelList;


    public CustomModel(List<CustomCategory> categories, List<CustomInModel> customInModelList) {
        this.categories = categories;
        this.customInModelList = customInModelList;
    }
}
