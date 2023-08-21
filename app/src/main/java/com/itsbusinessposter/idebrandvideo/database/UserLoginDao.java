package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.itsbusinessposter.idebrandvideo.items.AppInfo;
import com.itsbusinessposter.idebrandvideo.items.UserLogin;

import java.util.List;

@Dao
public interface UserLoginDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUserLogin(UserLogin userLogin);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(UserLogin userLogin);

    @Query("SELECT * FROM user_login WHERE user_id = :user_id")
    LiveData<UserLogin> getUserLoginData(String user_id);

    @Query("SELECT * FROM user_login")
    LiveData<List<UserLogin>> getUserLoginData();

    @Query("DELETE FROM user_login")
    void deleteUserLogin();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppInfo(AppInfo appInfo);

    @Query("SELECT * FROM app_info")
    LiveData<AppInfo> getAppInfo();

    @Query("DELETE FROM app_info")
    void deleteAppInfo();

}
