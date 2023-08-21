package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsbusinessposter.idebrandvideo.items.NewsItem;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NewsItem> news);

    @Query("SELECT * FROM news")
    LiveData<List<NewsItem>> getNews();

    @Query("DELETE FROM news")
    void deleteNews();

}
