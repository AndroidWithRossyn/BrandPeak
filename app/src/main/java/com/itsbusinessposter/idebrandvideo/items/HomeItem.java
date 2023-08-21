package com.itsbusinessposter.idebrandvideo.items;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "home")
public class HomeItem {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @TypeConverters
    @SerializedName("Story")
    public List<StoryItem> storyItemList;

    @TypeConverters
    @SerializedName("Festival")
    public List<FestivalItem> festivalItemList;

    @TypeConverters
    @SerializedName("Feature")
    public List<FeatureItem> featureItemList;

    @TypeConverters
    @SerializedName("BusinessCategory")
    public List<BusinessCategoryItem> businessCategoryList;

    @TypeConverters
    @SerializedName("Category")
    public List<CategoryItem> categoryList;

    public HomeItem(int id, List<StoryItem> storyItemList, List<FestivalItem> festivalItemList, List<FeatureItem> featureItemList, List<BusinessCategoryItem> businessCategoryList, List<CategoryItem> categoryList) {
        this.id = id;
        this.storyItemList = storyItemList;
        this.festivalItemList = festivalItemList;
        this.featureItemList = featureItemList;
        this.businessCategoryList = businessCategoryList;
        this.categoryList = categoryList;
    }
}
