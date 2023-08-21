package com.itsbusinessposter.idebrandvideo.repository;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.MyApplication;
import com.itsbusinessposter.idebrandvideo.api.ApiClient;
import com.itsbusinessposter.idebrandvideo.api.ApiResponse;
import com.itsbusinessposter.idebrandvideo.api.common.NetworkBoundResource;
import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.database.AppDatabase;
import com.itsbusinessposter.idebrandvideo.database.VCardDao;
import com.itsbusinessposter.idebrandvideo.items.ItemVcard;
import com.itsbusinessposter.idebrandvideo.items.UploadItem;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class VCardRepository {

    public Application application;
    public VCardDao vCardDao;
    public AppDatabase db;

    public VCardRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        vCardDao = db.getVCardDao();
    }

    public LiveData<Resource<List<ItemVcard>>> getVCards(String apiKey){

        return new NetworkBoundResource<List<ItemVcard>, List<ItemVcard>>() {
            @Override
            protected void saveCallResult(@NonNull List<ItemVcard> item) {
                try {
                    db.runInTransaction(() -> {
                        vCardDao.deleteTable();
                        vCardDao.insetAll(item);

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ItemVcard> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<ItemVcard>> loadFromDb() {
                return vCardDao.getVCards();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemVcard>>> createCall() {
                    return ApiClient.getApiService().getVCards(MyApplication.prefManager().getString(Constant.api_key));
                }
        }.asLiveData();
    }

    public LiveData<Resource<UploadItem>> uploadImage(String apiKey, String filePath, Uri imageUri) {
        MultipartBody.Part body = null;
        RequestBody fullName = null;
        Util.showLog("File: " + filePath);
        if (!filePath.equals("")) {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file news_title
            body = MultipartBody.Part.createFormData("profile_image", file.getName(), requestFile);

            fullName =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), file.getName());
        }

        RequestBody finalFullName = fullName;
        MultipartBody.Part finalBody = body;

        final MutableLiveData<Resource<UploadItem>> statusLiveData =
                new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<UploadItem> response = ApiClient.getApiService().upLoadImage(apiKey, finalFullName, finalBody).execute();


                // Wrap with APIResponse Class
                ApiResponse<UploadItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    statusLiveData.postValue(Resource.success(response.body()));

                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), null));
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;

    }

    public LiveData<Resource<UploadItem>> createVcard(String apiKey, String businessName, String yourName, String designation,
                                                      String mobile, String whatsapp, String email, String website, String location,
                                                      String facebook, String insta, String youtube, String twitter, String linkedin,
                                                      String about, String imageUrl, String tempID) {

        final MutableLiveData<Resource<UploadItem>> statusLiveData =
                new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<UploadItem> response = ApiClient.getApiService().createVcard(apiKey, businessName, yourName,
                        designation, mobile, whatsapp, email, website, location, facebook, insta, youtube, twitter, linkedin,
                        about, imageUrl, tempID).execute();


                // Wrap with APIResponse Class
                ApiResponse<UploadItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    statusLiveData.postValue(Resource.success(response.body()));

                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), null));
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;
    }
}
