package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsbusinessposter.idebrandvideo.items.LanguageItem;

import java.util.List;

@Dao
public interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insetAll(List<LanguageItem> languages);

    @Query("SELECT * FROM language")
    LiveData<List<LanguageItem>> getLanguages();

    @Query("DELETE FROM language")
    void deleteTable();

    @Query("UPDATE language SET isChecked = :isChecked WHERE id =:id")
    void UpdateLanguage(boolean isChecked, String id);

}
