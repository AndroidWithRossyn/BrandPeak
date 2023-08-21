package com.itsbusinessposter.idebrandvideo.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.api.ApiClient;
import com.itsbusinessposter.idebrandvideo.api.ApiResponse;
import com.itsbusinessposter.idebrandvideo.api.ApiStatus;
import com.itsbusinessposter.idebrandvideo.api.common.NetworkBoundResource;
import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.database.AppDatabase;
import com.itsbusinessposter.idebrandvideo.database.BusinessDao;
import com.itsbusinessposter.idebrandvideo.items.BusinessItem;
import com.itsbusinessposter.idebrandvideo.utils.AbsentLiveData;
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

public class BusinessRepository {

    public Application application;
    public AppDatabase db;
    public BusinessDao businessDao;

    private MediatorLiveData<Resource<List<BusinessItem>>> result = new MediatorLiveData<>();
    private MediatorLiveData<Resource<BusinessItem>> resultBusiness = new MediatorLiveData<>();

    public BusinessRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        businessDao = db.getBusinessDao();
    }

    public LiveData<Resource<List<BusinessItem>>> getBusiness(String apiKey, String userId) {
        return new NetworkBoundResource<List<BusinessItem>, List<BusinessItem>>() {
            @Override
            protected void saveCallResult(@NonNull List<BusinessItem> item) {
                try {
                    db.runInTransaction(() -> {
                        businessDao.deleteBusiness();
                        businessDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<BusinessItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<BusinessItem>> loadFromDb() {
                return businessDao.getAll();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<BusinessItem>>> createCall() {
                Util.showLog("BUSINESS LOAD");
                return ApiClient.getApiService().getBusiness(apiKey, userId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<BusinessItem>> getBusinessData(String businessId) {
        resultBusiness.addSource(businessDao.getBusinessData(businessId), data -> resultBusiness.setValue(Resource.success(data)));
        return resultBusiness;
    }

    public LiveData<Resource<Boolean>> deleteBusiness(String apiKey, String businessId) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().deleteBusiness(apiKey, businessId).execute();

                if (response.isSuccessful()) {
                    businessDao.deleteById(businessId);
                    statusLiveData.postValue(Resource.success(true));
                } else {
                    statusLiveData.postValue(Resource.error("error", false));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;
    }

    public BusinessItem getDefaultBusiness() {
        return businessDao.getDBBusiness(true);
    }

    public LiveData<Resource<List<BusinessItem>>> addBusiness(String apiKey, String filePath, Uri imageUri, String userId,
                                                    String name, String email, String phone, String website, String address,
                                                    boolean b, ContentResolver contentResolver) {
        MultipartBody.Part body = null;
        RequestBody fullName = null;
        Util.showLog("File: " + filePath);
        if (!filePath.equals("")) {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file news_title
            body = MultipartBody.Part.createFormData("bussinessImage", file.getName(), requestFile);

            fullName =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), file.getName());
        }
        RequestBody useIdRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), userId);

        RequestBody nameRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), name);

        RequestBody emailRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), email);

        RequestBody phoneRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), phone);

        RequestBody websiteRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), website);

        RequestBody addressRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), address);

        MultipartBody.Part finalBody = body;
        RequestBody finalFullName = fullName;
        return new NetworkBoundResource<List<BusinessItem>, List<BusinessItem>>() {

            List<BusinessItem> resultsDb;

            @Override
            protected void saveCallResult(@NonNull List<BusinessItem> item) {
                try {
                    db.runInTransaction(() -> {
                        businessDao.deleteBusiness();
                        businessDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<BusinessItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<BusinessItem>> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<List<BusinessItem>>() {
                        @Override
                        protected void onActive() {
                            super.onActive();
                            setValue(resultsDb);
                        }
                    };
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<BusinessItem>>> createCall() {
                return ApiClient.getApiService().addBusiness(apiKey, useIdRB, finalFullName, finalBody,
                        nameRB, emailRB, phoneRB, websiteRB, addressRB);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<BusinessItem>>> updateBusiness(String apiKey, String filePath, Uri imageUri,
                                                                 String bussinessId, String name,
                                                                 String email, String phone,
                                                                 String website, String address, boolean b, ContentResolver contentResolver) {
        MultipartBody.Part body = null;
        RequestBody fullName = null;
        Util.showLog("File: " + filePath);
        if (filePath!=null && !filePath.equals("")) {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file news_title
            body = MultipartBody.Part.createFormData("bussinessImage", file.getName(), requestFile);

            fullName =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), file.getName());
        }
        RequestBody businessId =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), bussinessId);

        RequestBody nameRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), name);

        RequestBody emailRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), email);

        RequestBody phoneRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), phone);

        RequestBody websiteRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), website);

        RequestBody addressRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), address);

        MultipartBody.Part finalBody = body;
        RequestBody finalFullName = fullName;
        return new NetworkBoundResource<List<BusinessItem>, List<BusinessItem>>() {

            List<BusinessItem> resultsDb;

            @Override
            protected void saveCallResult(@NonNull List<BusinessItem> item) {
                try {
                    db.runInTransaction(() -> {
                        businessDao.deleteById(bussinessId);
                        businessDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<BusinessItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<BusinessItem>> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<List<BusinessItem>>() {
                        @Override
                        protected void onActive() {
                            super.onActive();
                            setValue(resultsDb);
                        }
                    };
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<BusinessItem>>> createCall() {
                return ApiClient.getApiService().updateBusiness(apiKey, businessId, finalFullName, finalBody,
                        nameRB, emailRB, phoneRB, websiteRB, addressRB);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> setDefault(String apiKey, String userId, String businessId) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().setDefault(apiKey, userId, businessId).execute();

                if (response.isSuccessful()) {
                    statusLiveData.postValue(Resource.success(true));
                } else {
                    statusLiveData.postValue(Resource.error("error", false));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;
    }

    public int getBusinessCount() {
        return businessDao.getCount();
    }
}
