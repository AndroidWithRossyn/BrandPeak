package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsbusinessposter.idebrandvideo.items.FestivalItem;

import java.util.List;

@Dao
public interface FestivalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FestivalItem> festivalItems);

    @Query("SELECT *FROM festival")
    LiveData<List<FestivalItem>> getFestivals();

    @Query("DELETE FROM festival WHERE name = :name")
    void DeleteByName(String name);

//    @Query("DELETE FROM festival WHERE festivalId NOT IN (SELECT MIN(festivalId) FROM festival GROUP BY image)")
//    void deleteDuplicates();

    @Query("DELETE FROM festival")
    void deleteTable();
}
