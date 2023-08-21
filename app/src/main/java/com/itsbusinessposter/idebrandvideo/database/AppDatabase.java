package com.itsbusinessposter.idebrandvideo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.itsbusinessposter.idebrandvideo.items.AppInfo;
import com.itsbusinessposter.idebrandvideo.items.AppVersion;
import com.itsbusinessposter.idebrandvideo.items.BusinessCategoryItem;
import com.itsbusinessposter.idebrandvideo.items.BusinessItem;
import com.itsbusinessposter.idebrandvideo.items.CategoryItem;
import com.itsbusinessposter.idebrandvideo.items.CustomCategory;
import com.itsbusinessposter.idebrandvideo.items.CustomModel;
import com.itsbusinessposter.idebrandvideo.items.FestivalItem;
import com.itsbusinessposter.idebrandvideo.items.HomeItem;
import com.itsbusinessposter.idebrandvideo.items.ItemVcard;
import com.itsbusinessposter.idebrandvideo.items.LanguageItem;
import com.itsbusinessposter.idebrandvideo.items.MainStrModel;
import com.itsbusinessposter.idebrandvideo.items.NewsItem;
import com.itsbusinessposter.idebrandvideo.items.OfferItem;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.items.StickerCategory;
import com.itsbusinessposter.idebrandvideo.items.StickerItem;
import com.itsbusinessposter.idebrandvideo.items.StickerModel;
import com.itsbusinessposter.idebrandvideo.items.StoryItem;
import com.itsbusinessposter.idebrandvideo.items.SubjectItem;
import com.itsbusinessposter.idebrandvideo.items.SubsPlanItem;
import com.itsbusinessposter.idebrandvideo.items.UserFrame;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.items.UserLogin;

@Database(entities = {StoryItem.class, FestivalItem.class, CategoryItem.class, PostItem.class,
        LanguageItem.class, UserItem.class,
        UserLogin.class, BusinessItem.class, SubsPlanItem.class,
        SubjectItem.class, NewsItem.class, AppVersion.class, AppInfo.class, CustomCategory.class, HomeItem.class,
        BusinessCategoryItem.class, CustomModel.class, UserFrame.class, ItemVcard.class,
        StickerItem.class, StickerCategory.class, StickerModel.class, MainStrModel.class, OfferItem.class}, version = 19, exportSchema = false)
@TypeConverters({DataConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "festival_database";

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

    public abstract StoryDao getStoryDao();

    public abstract FestivalDao getFestivalDao();

    public abstract CategoryDao getCategoryDao();

    public abstract PostDao getPostDao();

    public abstract LanguageDao getLanguageDao();

    public abstract UserDao getUserDao();

    public abstract BusinessDao getBusinessDao();

    public abstract SubsPlanDao getSubsPlanDao();

    public abstract NewsDao getNewsDao();

    public abstract UserLoginDao getUserLoginDao();

    public abstract CustomCategoryDao getCustomCategoryDao();

    public abstract HomeDao getHomeDao();

    public abstract VCardDao getVCardDao();
}

