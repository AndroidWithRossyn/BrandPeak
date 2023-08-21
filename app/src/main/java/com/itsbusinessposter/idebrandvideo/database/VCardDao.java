package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsbusinessposter.idebrandvideo.items.ItemVcard;

import java.util.List;

@Dao
public interface VCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insetAll(List<ItemVcard> vCards);

    @Query("SELECT * FROM vCard")
    LiveData<List<ItemVcard>> getVCards();

    @Query("DELETE FROM vCard")
    void deleteTable();

}
