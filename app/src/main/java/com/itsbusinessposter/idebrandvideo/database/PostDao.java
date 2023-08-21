package com.itsbusinessposter.idebrandvideo.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.itsbusinessposter.idebrandvideo.items.MainStrModel;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.items.StickerItem;

import java.util.List;

@Dao
public interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PostItem postItem);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PostItem> postItems);

    @Query("SELECT * FROM posts WHERE fest_id = :fest_id AND type = :type AND is_video = :isVideo")
    LiveData<List<PostItem>> getByFestId(String fest_id, String type, boolean isVideo);

    @Query("SELECT * FROM posts WHERE fest_id = :fest_id AND type = :type AND language = :language AND is_video = :isVideo")
    LiveData<List<PostItem>> getByLanguage(String fest_id, String type, String language, boolean isVideo);

    @Query("SELECT*FROM posts WHERE is_trending = :is_trending")
    LiveData<List<PostItem>> getTrending(boolean is_trending);

    @Query("SELECT * FROM posts WHERE is_trending =:is_trending AND language =:language")
    LiveData<List<PostItem>>getTrendingByLang(boolean is_trending, String language);

    @Query("DELETE FROM posts WHERE fest_id = :fest_id AND type = :type AND language = :language AND is_video = :isVideo")
    void deleteByFestId(String fest_id, String type, String language, boolean isVideo);

    @Query("DELETE FROM posts WHERE fest_id = :fest_id AND type = :type AND is_video = :isVideo")
    void deleteByFestId(String fest_id, String type, boolean isVideo);

    @Query("DELETE FROM posts WHERE is_trending = :is_trending")
    void deleteTrending(boolean is_trending);

    @Query("DELETE FROM posts")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSticker(MainStrModel mainStrModel);

    @Query("SELECT * FROM main_str")
    LiveData<MainStrModel> getStickers();

    @Query("DELETE FROM main_str")
    void deleteStickers();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSticker(List<StickerItem> stickerItem);

    @Query("SELECT * FROM stickers")
    LiveData<List<StickerItem>> getStickersByKeyword();

    @Query("DELETE FROM stickers")
    void deleteAllStickers();
}
