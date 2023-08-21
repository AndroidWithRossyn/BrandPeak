package com.itsbusinessposter.idebrandvideo.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.api.ApiClient;
import com.itsbusinessposter.idebrandvideo.api.ApiResponse;
import com.itsbusinessposter.idebrandvideo.api.common.NetworkBoundResource;
import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.database.AppDatabase;
import com.itsbusinessposter.idebrandvideo.database.PostDao;
import com.itsbusinessposter.idebrandvideo.items.MainStrModel;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.items.StickerItem;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class PostsRepository {

    public AppDatabase db;
    public PostDao postDao;

    public MediatorLiveData<Resource<List<PostItem>>> result = new MediatorLiveData<>();
    public MediatorLiveData<Resource<List<PostItem>>> trending_result = new MediatorLiveData<>();
    public MediatorLiveData<Resource<MainStrModel>> stickerResult = new MediatorLiveData<>();

    public PostsRepository(Application application) {
        db = AppDatabase.getInstance(application);
        postDao = db.getPostDao();
    }

    public LiveData<Resource<List<PostItem>>> getById(String apiKey, String festId, String type, String language, boolean isVideo) {
        return new NetworkBoundResource<List<PostItem>, List<PostItem>>() {
                @Override
                protected void saveCallResult(@NonNull List<PostItem> item) {
                    try {
                        db.runInTransaction(() -> {
                            if (language.equals("")) {
                                postDao.deleteByFestId(festId, type, isVideo);
                            } else {
                                postDao.deleteByFestId(festId, type, language, isVideo);
                            }
                            postDao.insertAll(item);

                        });
                    } catch (Exception ex) {
                        Util.showErrorLog("Error at ", ex);
                    }
                }

                @Override
                protected boolean shouldFetch(@Nullable List<PostItem> data) {
                    return Config.IS_CONNECTED;
                }

                @NonNull
                @Override
                protected LiveData<List<PostItem>> loadFromDb() {
                    if (language.equals("")) {
                        return postDao.getByFestId(festId, type, isVideo);
                    }
                    return postDao.getByLanguage(festId, type, language, isVideo);
                }

                @NonNull
                @Override
                protected LiveData<ApiResponse<List<PostItem>>> createCall() {
                    if (isVideo) {
                        return ApiClient.getApiService().getVideosById(apiKey, festId, type);
                    } else {
                        if (type.equals(Constant.CUSTOM)) {
                            return ApiClient.getApiService().getCustomPost(apiKey, festId);
                        }
                        if (type.equals(Constant.BUSINESS)) {
                            return ApiClient.getApiService().getBusinessPost(apiKey, festId);
                        }
                        return ApiClient.getApiService().getPost(apiKey, type, festId);
                    }
                }
            }.asLiveData();
    }

    public LiveData<Resource<List<PostItem>>> getTrendingPost(String language) {
        List<PostItem> postItemList = new ArrayList<>();

        try {
            db.runInTransaction(() -> {
//                postDao.deleteTrending(true);
                for (int i = 0; i < postItemList.size(); i++) {
                    postDao.insert(postItemList.get(i));
                }
            });
        } catch (Exception ex) {
            Util.showLog("Error at " + ex);
        }

        if (language.equals("")) {
            trending_result.addSource(postDao.getTrending(true), data -> {
                trending_result.setValue(Resource.success(data));
            });
        } else {
            trending_result.addSource(postDao.getTrendingByLang(true, language), data -> {
                trending_result.setValue(Resource.success(data));
            });
        }
        return trending_result;
    }

    public LiveData<Resource<MainStrModel>> getStickers(String apiKey) {
        return new NetworkBoundResource<MainStrModel, MainStrModel>() {
            @Override
            protected void saveCallResult(@NonNull MainStrModel item) {
                try {
                    db.runInTransaction(() -> {
                        postDao.deleteStickers();
                        postDao.insertSticker(item);

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable MainStrModel data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<MainStrModel> loadFromDb() {
                return postDao.getStickers();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MainStrModel>> createCall() {
                return ApiClient.getApiService().getStickers(apiKey);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<StickerItem>>> getStickersByKeyword(String key, String keyword) {
        return new NetworkBoundResource<List<StickerItem>, List<StickerItem>>(){

            @Override
            protected void saveCallResult(@NonNull List<StickerItem> item) {
                try {
                    db.runInTransaction(() -> {
                        postDao.deleteAllStickers();
                        postDao.insertAllSticker(item);

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<StickerItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<StickerItem>> loadFromDb() {
                return postDao.getStickersByKeyword();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<StickerItem>>> createCall() {
                return ApiClient.getApiService().getStickersByKeyword(key, keyword);
            }
        }.asLiveData();
    }
}
