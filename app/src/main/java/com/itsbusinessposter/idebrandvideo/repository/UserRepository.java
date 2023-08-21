package com.itsbusinessposter.idebrandvideo.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
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
import com.itsbusinessposter.idebrandvideo.api.ApiService;
import com.itsbusinessposter.idebrandvideo.api.ApiStatus;
import com.itsbusinessposter.idebrandvideo.api.common.NetworkBoundResource;
import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.database.AppDatabase;
import com.itsbusinessposter.idebrandvideo.database.BusinessDao;
import com.itsbusinessposter.idebrandvideo.database.UserDao;
import com.itsbusinessposter.idebrandvideo.database.UserLoginDao;
import com.itsbusinessposter.idebrandvideo.items.AppInfo;
import com.itsbusinessposter.idebrandvideo.items.BusinessItem;
import com.itsbusinessposter.idebrandvideo.items.SubjectItem;
import com.itsbusinessposter.idebrandvideo.items.UserFrame;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.items.UserLogin;
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
import okhttp3.ResponseBody;
import retrofit2.Response;

public class UserRepository {

    public Application application;
    public AppDatabase db;
    public UserDao userDao;
    public UserLoginDao userLoginDao;
    public BusinessDao businessDao;

    public MediatorLiveData<Resource<List<SubjectItem>>> subjectResult = new MediatorLiveData<>();

    public UserRepository(Application application) {
        this.application = application;
        db = AppDatabase.getInstance(application);
        userDao = db.getUserDao();
        userLoginDao = db.getUserLoginDao();
        businessDao = db.getBusinessDao();
    }

    public LiveData<UserLogin> getLoginUserData(String userId) {
        return userLoginDao.getUserLoginData(userId);
    }

    public LiveData<Resource<List<SubjectItem>>> getSubjects(String apiKey) {
      return new NetworkBoundResource<List<SubjectItem>, List<SubjectItem>>() {
          @Override
          protected void saveCallResult(@NonNull List<SubjectItem> item) {
              try{
                  db.runInTransaction(() -> {
                      userDao.deleteSubject();
                      userDao.insert(item);
                  });
              }catch (Exception e){

              }
          }

          @Override
          protected boolean shouldFetch(@Nullable List<SubjectItem> data) {
              return Config.IS_CONNECTED;
          }

          @NonNull
          @Override
          protected LiveData<List<SubjectItem>> loadFromDb() {
              return userDao.getSubjectItems();
          }

          @NonNull
          @Override
          protected LiveData<ApiResponse<List<SubjectItem>>> createCall() {
              return ApiClient.getApiService().getSubjectItems(apiKey);
          }
      }.asLiveData();
    }

    public LiveData<Resource<UserLogin>> doLogin(String apiKey, String email, String password) {
        return new NetworkBoundResource<UserLogin, UserItem>() {
            String user_id = "";

            @Override
            protected void saveCallResult(@NonNull UserItem item) {
                Util.showLog("SaveCallResult of doLogin. " + item.email + " " + item.userName);
                try {
                    db.runInTransaction(() -> {
                        // set User id
                        user_id = item.userId;

                        // clear user login data
                        userLoginDao.deleteUserLogin();

                        // insert user data
                        userDao.insertUser(item);

                        // insert user login
                        UserLogin userLogin = new UserLogin(user_id, true, item);
                        userLoginDao.insertUserLogin(userLogin);

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable UserLogin data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<UserLogin> loadFromDb() {
                Util.showLog("Load User Login data from database.");
                if (user_id == null || user_id.equals("")) {
                    return AbsentLiveData.create();
                }

                return userLoginDao.getUserLoginData(user_id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserItem>> createCall() {
                Util.showLog("DATA: " + apiKey + " " + email + " " + password);
                ApiService apiService = ApiClient.getApiService();
                return apiService.postUserLogin(apiKey, email, password);
            }
        }.asLiveData();
    }

    public LiveData<Resource<UserItem>> doRegister(String apiKey, String email, String userName, String password, String phone) {

        Util.showLog(email + " " + userName + " " + password + " " + phone);

        final MutableLiveData<Resource<UserItem>> statusLiveData =
                new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {

                // Call the API Service
                Response<UserItem> response = ApiClient.getApiService().postUser(apiKey,
                        userName,
                        email,
                        password,
                        phone).execute();


                // Wrap with APIResponse Class
                ApiResponse<UserItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.runInTransaction(() -> {
                            if (apiResponse.body != null) {
                                // set User id
                                String user_id = apiResponse.body.userId;

                                // clear user login data
                                userLoginDao.deleteUserLogin();

                                // insert user data
                                userDao.insertUser(apiResponse.body);

                                statusLiveData.postValue(Resource.success(response.body()));
                            }
                        });
                    } catch (Exception ex) {
                        Util.showErrorLog("Error at ", ex);
                    }

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

    public LiveData<Resource<UserItem>> getUserDataById(String apiKey, String user_id) {
        return new NetworkBoundResource<UserItem, UserItem>() {

            String userId = "";

            @Override
            protected void saveCallResult(@NonNull UserItem item) {
                Util.showLog("SaveCallResult of doLogin. " + item.email + " " + item.userName);
                try {
                    db.runInTransaction(() -> {

                        userId = item.userId;

                        // clear user login data
                        userLoginDao.deleteUserLogin();

                        // insert user data
                        userDao.insertUser(item);

                        // insert user login
                        UserLogin userLogin = new UserLogin(userId, true, item);
                        userLoginDao.insertUserLogin(userLogin);

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable UserItem data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<UserItem> loadFromDb() {
                return userDao.getUserData(user_id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<UserItem>> createCall() {
                return ApiClient.getApiService().getUserById(apiKey, user_id);
            }
        }.asLiveData();
    }

    public LiveData<UserLogin>getDbUserData(String user_id){
        return userLoginDao.getUserLoginData(user_id);
    }

    public LiveData<Resource<Boolean>> deleteUser(UserItem user) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            //Background work here
            try {
                db.runInTransaction(() -> {

                    userLoginDao.deleteUserLogin();
                    businessDao.deleteBusiness();
                    Util.showLog("User is deleted.");
                    statusLiveData.postValue(Resource.success(true));

                });
            } catch (Exception ex) {
                Util.showErrorLog("Error at ", ex);
            }
            handler.post(() -> {
                //UI Thread work here

            });
        });

        return statusLiveData;
    }

    public LiveData<Resource<UserLogin>> registerGoogleUser(String apiKey, String name, String email, String imageUrl) {
        final MutableLiveData<Resource<UserLogin>> statusLiveData = new MutableLiveData<>(); // To update the status to the listener

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            try {

                // Call the API Service
                Response<UserItem> response = ApiClient.getApiService().postGoogleUser(apiKey, name, email, imageUrl).execute();

                // Wrap with APIResponse Class
                ApiResponse<UserItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.runInTransaction(() -> {
                            if (apiResponse.body != null) {
                                // set User id
                                String userId = apiResponse.body.userId;

                                // clear user login data
                                userLoginDao.deleteUserLogin();

                                // insert user data
                                userDao.insertUser(apiResponse.body);

                                // insert user login
                                UserLogin userLogin = new UserLogin(userId, true, apiResponse.body);
                                userLoginDao.insertUserLogin(userLogin);

                                statusLiveData.postValue(Resource.success(userLogin));
                            }
                        });
                    } catch (Exception ex) {
                        Util.showErrorLog("Error at ", ex);
                    }

                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), null));
            }

        });
        return statusLiveData;
    }

    public LiveData<Resource<UserLogin>> registerMobileUser(String apiKey, String name, String email, String mobile) {
        final MutableLiveData<Resource<UserLogin>> statusLiveData = new MutableLiveData<>(); // To update the status to the listener

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            try {

                // Call the API Service
                Response<UserItem> response = ApiClient.getApiService().postMobileUser(apiKey, name, email, mobile).execute();

                // Wrap with APIResponse Class
                ApiResponse<UserItem> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    try {
                        db.runInTransaction(() -> {
                            if (apiResponse.body != null) {
                                // set User id
                                String userId = apiResponse.body.userId;

                                // clear user login data
                                userLoginDao.deleteUserLogin();

                                // insert user data
                                userDao.insertUser(apiResponse.body);

                                // insert user login
                                UserLogin userLogin = new UserLogin(userId, true, apiResponse.body);
                                userLoginDao.insertUserLogin(userLogin);

                                statusLiveData.postValue(Resource.success(userLogin));
                            }
                        });
                    } catch (Exception ex) {
                        Util.showErrorLog("Error at ", ex);
                    }

                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), null));
            }

        });
        return statusLiveData;
    }

    public LiveData<Resource<UserItem>> uploadImage(String apiKey, Context context, String filePath, Uri uri, String userId, String userName, String email, String phone, ContentResolver contentResolver) {

        MultipartBody.Part body = null;
        RequestBody fullName = null;
        if (filePath!=null && !filePath.equals("")) {
            File file = new File(filePath);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file news_title
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            fullName =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), file.getName());
        }
        RequestBody useIdRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), userId);

        RequestBody useNameRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), userName);

        RequestBody useEmailRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), email);

        RequestBody usePhoneRB =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), phone);

        MultipartBody.Part finalBody = body;
        RequestBody finalFullName = fullName;

        return new NetworkBoundResource<UserItem, UserItem>() {

            private UserItem resultsDb;
            String user_id = "";

            @Override
            protected void saveCallResult(@NonNull UserItem item) {
                Util.showLog("SaveCallResult");

                try {
                    db.runInTransaction(() -> {

                        // set User id
                        user_id = item.userId;

                        // update user data
                        userDao.update(item);

                        // update user login
                        UserLogin userLogin = new UserLogin(user_id, true, item);
                        userLoginDao.update(userLogin);

                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }

                resultsDb = item;
            }

            @Override
            protected boolean shouldFetch(@Nullable UserItem data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<UserItem> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                } else {
                    return new LiveData<UserItem>() {
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
            protected LiveData<ApiResponse<UserItem>> createCall() {
                return ApiClient.getApiService().doUploadImage(apiKey, useIdRB, finalFullName, finalBody, useNameRB, useEmailRB, usePhoneRB);
            }
        }.asLiveData();

    }

    public LiveData<Resource<Boolean>> resentCodeForUser(String apiKey, String email) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().resentCodeAgain(apiKey, email).execute();


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

    public LiveData<Resource<ApiStatus>> verificationCodeForUser(String apiKey, String userId, String code) {

        return new NetworkBoundResource<ApiStatus, ApiStatus>() {
            private ApiStatus resultsDb;

            @Override
            protected void saveCallResult(@NonNull ApiStatus item) {
                resultsDb = item;
            }

            @Override
            protected boolean shouldFetch(@Nullable ApiStatus data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<ApiStatus> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                }

                return new LiveData<ApiStatus>() {
                    @Override
                    protected void onActive() {
                        super.onActive();
                        setValue(resultsDb);
                    }
                };
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ApiStatus>> createCall() {
                return ApiClient.getApiService().verifyEmail(apiKey, userId, code);
            }
        }.asLiveData();
    }

    public LiveData<Resource<ApiStatus>> passwordUpdate(String apiKey, String loginUserId, String password) {

        return new NetworkBoundResource<ApiStatus, ApiStatus>() {

            private ApiStatus resultsDb;

            @Override
            protected void saveCallResult(@NonNull ApiStatus item) {

                Util.showLog("SaveCallResult of passwordUpdate");
                resultsDb = item;

            }

            @Override
            protected boolean shouldFetch(@Nullable ApiStatus data) {
                // for passwordUpdate, always should fetch
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<ApiStatus> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                }

                return new LiveData<ApiStatus>() {
                    @Override
                    protected void onActive() {
                        super.onActive();
                        setValue(resultsDb);
                    }
                };
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ApiStatus>> createCall() {
                Util.showLog("Call API Service to update password.");
                return ApiClient.getApiService().postPasswordUpdate(apiKey, loginUserId, password);
            }

            @Override
            protected void onFetchFailed(int code, String message) {
                Util.showLog("Fetch Failed of password update.");
            }
        }.asLiveData();
    }

    public LiveData<Resource<ApiStatus>> forgotPassword(String apiKey, String email) {

        return new NetworkBoundResource<ApiStatus, ApiStatus>() {

            private ApiStatus resultsDb;

            @Override
            protected void saveCallResult(@NonNull ApiStatus item) {

                Util.showLog("SaveCallResult of forgotPassword");

                resultsDb = item;

            }

            @Override
            protected boolean shouldFetch(@Nullable ApiStatus data) {
                // for forgot password, always should fetch
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<ApiStatus> loadFromDb() {
                if (resultsDb == null) {
                    return AbsentLiveData.create();
                }

                return new LiveData<ApiStatus>() {
                    @Override
                    protected void onActive() {
                        super.onActive();
                        setValue(resultsDb);
                    }
                };
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ApiStatus>> createCall() {
                Util.showLog("Call API Service to Request Forgot Password.");
                return ApiClient.getApiService().postForgotPassword(apiKey, email);
            }

            @Override
            protected void onFetchFailed(int code, String message) {
                Util.showLog("Fetch Failed of forgot Password.");
            }
        }.asLiveData();
    }

    public LiveData<BusinessItem> getDefaultBusiness() {
        return businessDao.getDefaultBusiness(true);
    }

    public LiveData<Resource<Boolean>> sendContact(String apiKey, String name, String email, String number, String massage, String subjectId) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ApiStatus> response;

            try {
                response = ApiClient.getApiService().sendContact(apiKey, name, email, number, massage, subjectId).execute();


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

    public LiveData<Resource<AppInfo>> getAppInfo(String apiKey) {
        return new NetworkBoundResource<AppInfo, AppInfo>() {
            @Override
            protected void saveCallResult(@NonNull AppInfo item) {

                try {
                    db.runInTransaction(() -> {
                        userLoginDao.deleteAppInfo();
                        userLoginDao.insertAppInfo(item);
                    });
                }catch (Exception e){

                }

            }

            @Override
            protected boolean shouldFetch(@Nullable AppInfo data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<AppInfo> loadFromDb() {
                return userLoginDao.getAppInfo();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<AppInfo>> createCall() {
                return ApiClient.getApiService().getAppInfo(apiKey);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<UserFrame>>> getUserFrames(String apiKey, String userId) {
        return new NetworkBoundResource<List<UserFrame>, List<UserFrame>>() {
            @Override
            protected void saveCallResult(@NonNull List<UserFrame> item) {

                try {
                    db.runInTransaction(() -> {
                        userDao.deleteFrames();
                        userDao.insertFrame(item);
                    });
                }catch (Exception e){

                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<UserFrame> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<UserFrame>> loadFromDb() {
                return userDao.getFrames();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<UserFrame>>> createCall() {
                return ApiClient.getApiService().getUserFrame(apiKey, userId);
            }
        }.asLiveData();
    }

    public LiveData<AppInfo> getDBAppInfo() {
        return userLoginDao.getAppInfo();
    }

    /** TODO: PDF
     */
    public LiveData<Resource<ResponseBody>> getPDFData(String apiKey) {
        final MutableLiveData<Resource<ResponseBody>> statusLiveData = new MutableLiveData<>();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            Response<ResponseBody> response;

            try {
                response = ApiClient.getApiService().getPDFData(apiKey).execute();

                if (response.isSuccessful()) {
                    statusLiveData.postValue(Resource.success(response.body()));
                } else {
                    statusLiveData.postValue(Resource.error("error", response.errorBody()));
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
}
