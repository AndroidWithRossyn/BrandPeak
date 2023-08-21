package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.itsbusinessposter.idebrandvideo.items.SubjectItem;
import com.itsbusinessposter.idebrandvideo.items.UserFrame;
import com.itsbusinessposter.idebrandvideo.items.UserItem;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserItem user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(UserItem user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserItem> userList);

    @Query("SELECT * FROM user")
    LiveData<List<UserItem>> getAll();

    @Query("SELECT * FROM user WHERE userId = :user_id")
    LiveData<UserItem> getUserData(String user_id);

    @Query("SELECT * FROM user WHERE userId = :user_id ")
    UserItem getUserRawData(String user_id);

    @Query("SELECT businessLimit FROM user WHERE userId = :user_id")
    int getLimitByUserId(String user_id);

    @Query("SELECT * FROM user WHERE userId = :user_id")
    LiveData<UserItem> getPointByUserIdLiveData(String user_id);

    @Query("UPDATE user SET businessLimit = :businessLimit WHERE userId = :user_id ")
    void updatePointByUserId(String businessLimit, String user_id);

    @Query("DELETE FROM user WHERE userId = :user_id")
    void deleteById(String user_id);

    @Query("DELETE FROM User")
    void deleteTable();

    //Subject Region
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<SubjectItem> subjectItemList);

    @Query("SELECT * FROM subject ")
    LiveData<List<SubjectItem>> getSubjectItems();

    @Query("DELETE FROM subject")
    void deleteSubject();


    ///User Frame Region
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFrame(List<UserFrame> userFrameList);

    @Query("SELECT * FROM userFrame")
    LiveData<List<UserFrame>> getFrames();

    @Query("DELETE FROM userFrame")
    void deleteFrames();
}
