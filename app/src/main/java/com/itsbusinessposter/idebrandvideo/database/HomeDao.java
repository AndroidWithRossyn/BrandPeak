package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsbusinessposter.idebrandvideo.items.HomeItem;

@Dao
public interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(HomeItem homeItemList);

    @Query("SELECT * FROM home")
    LiveData<HomeItem> getHomeItem();

    @Query("DELETE FROM home")
    void deleteTable();
}
