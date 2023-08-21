package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user", primaryKeys = "userId")
public class UserItem {

    @NonNull
    @SerializedName("userId")
    public String userId;

    @SerializedName("userName")
    public String userName;

    @SerializedName("profileImage")
    public String userImage;

    @SerializedName("emailId")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("phoneNumber")
    public String phone;

    @SerializedName("userType")
    public String loginType;

    @SerializedName("is_email_verify")
    public boolean is_email_verify;

    @SerializedName("planName")
    public String planName;

    @SerializedName("planDuration")
    public String planDuration;

    @SerializedName("planStartDate")
    public String planStartDate;

    @SerializedName("planEndDate")
    public String planEndDate;

    @SerializedName("isSubscribe")
    public boolean isSubscribed;

    @SerializedName("businessLimit")
    public int businessLimit;

    public UserItem(@NonNull String userId, String userName, String userImage, String email, String password, String phone, String loginType, boolean is_email_verify, String planName, String planDuration, String planStartDate, String planEndDate, boolean isSubscribed, int businessLimit) {
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.loginType = loginType;
        this.is_email_verify = is_email_verify;
        this.planName = planName;
        this.planDuration = planDuration;
        this.planStartDate = planStartDate;
        this.planEndDate = planEndDate;
        this.isSubscribed = isSubscribed;
        this.businessLimit = businessLimit;
    }
}
