package com.itsbusinessposter.idebrandvideo.viewmodel;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.itsbusinessposter.idebrandvideo.api.ApiStatus;
import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.items.AppInfo;
import com.itsbusinessposter.idebrandvideo.items.BusinessItem;
import com.itsbusinessposter.idebrandvideo.items.SubjectItem;
import com.itsbusinessposter.idebrandvideo.items.UserFrame;
import com.itsbusinessposter.idebrandvideo.items.UserItem;
import com.itsbusinessposter.idebrandvideo.items.UserLogin;
import com.itsbusinessposter.idebrandvideo.repository.UserRepository;
import com.itsbusinessposter.idebrandvideo.utils.AbsentLiveData;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;

import java.util.List;

import okhttp3.ResponseBody;

public class UserViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public boolean isLoading = false;
    UserRepository userRepository;

    private MutableLiveData<String> subjectObj = new MutableLiveData<>();

    // for Login
    private LiveData<Resource<UserLogin>> doUserLoginData;
    private MutableLiveData<UserItem> doUserLoginObj = new MutableLiveData<>();

    // for register

    private LiveData<Resource<UserItem>> registerUserData;
    private MutableLiveData<UserItem> registerUserObj = new MutableLiveData<>();

    // for user data

    private LiveData<Resource<UserItem>> userData;
    private MutableLiveData<String> userDataObj = new MutableLiveData<>();

    // for register Google
    private LiveData<Resource<UserLogin>> registerGoogleUserData;
    private MutableLiveData<TmpDataHolder> registerGoogleUserObj = new MutableLiveData<>();

    // for register Google
    private LiveData<Resource<UserLogin>> registerPhoneUserData;
    private MutableLiveData<TmpDataHolder> registerPhoneUserObj = new MutableLiveData<>();

    // for image upload
    private MutableLiveData<String> imgObj = new MutableLiveData<>();

    //for resent verification code
    private LiveData<Resource<Boolean>> resentVerifyCodeData;
    private MutableLiveData<String> resentVerifyCodeObj = new MutableLiveData<>();

    //for verification code
    private LiveData<Resource<ApiStatus>> verificationEmailData;
    private MutableLiveData<TmpDataHolder> verificationEmailObj = new MutableLiveData<>();

    // for password update
    private LiveData<Resource<ApiStatus>> passwordUpdateData;
    private MutableLiveData<TmpDataHolder> passwordUpdateObj = new MutableLiveData<>();

    // for forgot password
    private LiveData<Resource<ApiStatus>> forgotpasswordData;
    private MutableLiveData<String> forgotPasswordObj = new MutableLiveData<>();

    // for send Contact
    private MutableLiveData<String> contactObj = new MutableLiveData<>();

    // for App info
    private LiveData<Resource<AppInfo>> appInfoData;
    private MutableLiveData<String> appInfoObj = new MutableLiveData<>();

    //For User Frame
    private MutableLiveData<String> frameObj = new MutableLiveData<>();

    PrefManager prefManager;

    public UserViewModel(@NonNull Application application) {
        super(application);
        prefManager = new PrefManager(application);
        userRepository = new UserRepository(application);

        // for  login
        doUserLoginData = Transformations.switchMap(doUserLoginObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.doLogin(prefManager.getString(Constant.api_key), obj.email, obj.password);
        });

        // for register
        registerUserData = Transformations.switchMap(registerUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.doRegister(prefManager.getString(Constant.api_key), obj.email,
                    obj.userName,
                    obj.password,
                    obj.phone);
        });

        // for user data
        userData = Transformations.switchMap(userDataObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.getUserDataById(prefManager.getString(Constant.api_key), obj);
        });

        // for Google Register
        registerGoogleUserData = Transformations.switchMap(registerGoogleUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.registerGoogleUser(prefManager.getString(Constant.api_key), obj.name, obj.email, obj.imageUrl);
        });

        // for Phone Register
        registerPhoneUserData = Transformations.switchMap(registerPhoneUserObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.registerMobileUser(prefManager.getString(Constant.api_key), obj.name, obj.email, obj.phone);
        });

        // for Recent Code
        resentVerifyCodeData = Transformations.switchMap(resentVerifyCodeObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            return userRepository.resentCodeForUser(prefManager.getString(Constant.api_key), obj);

        });

        // for Verify Email
        verificationEmailData = Transformations.switchMap(verificationEmailObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return userRepository.verificationCodeForUser(prefManager.getString(Constant.api_key), obj.loginUserId, obj.code);
        });

        // Password Update
        passwordUpdateData = Transformations.switchMap(passwordUpdateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.passwordUpdate(prefManager.getString(Constant.api_key), obj.loginUserId, obj.password);
        });

        // Forgot Password
        forgotpasswordData = Transformations.switchMap(forgotPasswordObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.forgotPassword(prefManager.getString(Constant.api_key), obj);
        });

        appInfoData = Transformations.switchMap(appInfoObj, obj -> {
            if (obj == null) {
                return  AbsentLiveData.create();
            }
            return userRepository.getAppInfo(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<Resource<List<SubjectItem>>> getSubjects() {
        subjectObj.setValue("PS");
        return Transformations.switchMap(subjectObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.getSubjects(prefManager.getString(Constant.api_key));
        });
    }


    public void setLoadingState(Boolean state) {
        isLoading = state;
        loadingState.setValue(state);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    // User Login

    public void setUserLogin(UserItem obj) {
        setLoadingState(true);
        this.doUserLoginObj.setValue(obj);
    }

    public LiveData<UserLogin> getLoginUser(String userId) {
        return userRepository.getLoginUserData(userId);
    }

    // User register

    public LiveData<Resource<UserItem>> getRegisterUser() {
        return registerUserData;
    }

    public void setRegisterUserData(UserItem user) {
        setLoadingState(true);
        registerUserObj.setValue(user);
    }

    public LiveData<Resource<UserLogin>> getUserLoginStatus() {
        return doUserLoginData;
    }

    // get User Data
    public void setUserById(String string) {
        userDataObj.setValue(string);
    }

    public LiveData<Resource<UserItem>> getUserDataById() {
        return userData;
    }

    // User Logout
    public LiveData<Resource<Boolean>> deleteUserLogin(UserItem user) {

        if (user == null) {
            return AbsentLiveData.create();
        }

        return userRepository.deleteUser(user);
    }

    // Google Register
    public void setRegisterGoogleUser(String displayName, String email, String photoUrl) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.name = displayName;
        tmpDataHolder.email = email;
        tmpDataHolder.imageUrl = photoUrl;

        registerGoogleUserObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<UserLogin>> getGoogleLoginData() {
        return registerGoogleUserData;
    }

    // Phone Register
    public void setRegisterPhoneUser(String displayName, String email, String mobile) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.name = displayName;
        tmpDataHolder.email = email;
        tmpDataHolder.phone = mobile;

        registerPhoneUserObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<UserLogin>> getPhoneLoginData() {
        return registerPhoneUserData;
    }

    // Upload Image
    public LiveData<Resource<UserItem>> uploadImage(Context context, String filePath, Uri uri, String userId,
                                                    String userName, String email,
                                                    String phone,
                                                    ContentResolver contentResolver) {

        imgObj.setValue("PS");

        return Transformations.switchMap(imgObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.uploadImage(prefManager.getString(Constant.api_key), context, filePath, uri, userId, userName, email, phone, contentResolver);
        });

    }

    // Resent Code
    public void setResentVerifyCodeObj(String email) {
        resentVerifyCodeObj.setValue(email);
    }

    public LiveData<Resource<Boolean>> getResentVerifyCodeData() {
        return resentVerifyCodeData;
    }

    // Verify Email
    public void setEmailVerificationUser(String loginUserId, String code) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder();
        tmpDataHolder.loginUserId = loginUserId;
        tmpDataHolder.code = code;
        verificationEmailObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<ApiStatus>> getEmailVerificationUser() {
        return verificationEmailData;
    }

    // Change password
    public LiveData<Resource<ApiStatus>> passwordUpdate(String loginUserId, String password) {

        TmpDataHolder holder = new TmpDataHolder();
        holder.loginUserId = loginUserId;
        holder.password = password;

        passwordUpdateObj.setValue(holder);
        return passwordUpdateData;
    }

    // Forgot password
    public LiveData<Resource<ApiStatus>> forgotPassword(String email) {
        forgotPasswordObj.setValue(email);
        return forgotpasswordData;
    }

    // Local User
    public LiveData<UserLogin>getDbUserData(String userId){
        return userRepository.getDbUserData(userId);
    }

    public LiveData<BusinessItem> getDefaultBusiness() {
        return userRepository.getDefaultBusiness();
    }

    public LiveData<Resource<Boolean>> sendContact(String name, String email, String number, String massage, String subjectId) {
        contactObj.setValue(massage);
        return Transformations.switchMap(contactObj, obj->{
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.sendContact(prefManager.getString(Constant.api_key), name, email, number, massage, subjectId);
        });
    }

    // for App info

    public void setAppInfo(String obj){
        appInfoObj.setValue(obj);
    }

    public LiveData<Resource<AppInfo>> getAppInfo() {
        return appInfoData;
    }
    public LiveData<AppInfo> getDBAppInfo() {
        return userRepository.getDBAppInfo();
    }

    public LiveData<Resource<List<UserFrame>>> getFrameData(String userId) {
        frameObj.setValue("PS");
        return Transformations.switchMap(frameObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.getUserFrames(prefManager.getString(Constant.api_key), userId);
        });

    }


    /** TODO: PDF
     */
    private MutableLiveData<String> pdfOBJ = new MutableLiveData<>();
    public LiveData<Resource<ResponseBody>> getPDFData() {
        pdfOBJ.setValue("PD");
        return Transformations.switchMap(pdfOBJ, obj->{
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return userRepository.getPDFData(prefManager.getString(Constant.api_key));
        });
    }
    // TMP data holder
    class TmpDataHolder {

        public String name = "";
        public String email = "";
        public String imageUrl = "";
        public String loginUserId = "";
        public String password = "";
        public String phone = "";
        public String code = "";
    }
}
