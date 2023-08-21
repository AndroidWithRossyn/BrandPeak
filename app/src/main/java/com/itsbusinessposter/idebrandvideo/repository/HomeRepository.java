package com.itsbusinessposter.idebrandvideo.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.api.ApiClient;
import com.itsbusinessposter.idebrandvideo.api.ApiResponse;
import com.itsbusinessposter.idebrandvideo.api.common.NetworkBoundResource;
import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.database.AppDatabase;
import com.itsbusinessposter.idebrandvideo.database.HomeDao;
import com.itsbusinessposter.idebrandvideo.items.HomeItem;
import com.itsbusinessposter.idebrandvideo.utils.Util;

public class HomeRepository {

    AppDatabase db;
    HomeDao homeDao;

    public HomeRepository(Application application) {
        db = AppDatabase.getInstance(application);
        homeDao = db.getHomeDao();
    }


    public LiveData<Resource<HomeItem>> getHomeData(String apiKey) {
        return new NetworkBoundResource<HomeItem, HomeItem>() {
            @Override
            protected void saveCallResult(@NonNull HomeItem item) {

                try {
                    db.runInTransaction(() -> {
                        homeDao.deleteTable();
                        homeDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable HomeItem data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<HomeItem> loadFromDb() {
                return homeDao.getHomeItem();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<HomeItem>> createCall() {
                return ApiClient.getApiService().getHomeData(apiKey);
            }
        }.asLiveData();
    }
}
