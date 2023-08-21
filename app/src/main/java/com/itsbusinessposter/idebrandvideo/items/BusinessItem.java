package com.itsbusinessposter.idebrandvideo.items;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "business", primaryKeys = "id")
public class BusinessItem implements Serializable {

    @NonNull
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("logo")
    public String logo;
    @SerializedName("email")
    public String email;
    @SerializedName("mobileNo")
    public String phone;
    @SerializedName("website")
    public String website;
    @SerializedName("address")
    public String address;

    public boolean isDefault;

    public BusinessItem(@NonNull String id, String name, String logo, String email, String phone, String website, String address, boolean isDefault) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.email = email;
        this.phone = phone;
        this.website = website;
        this.address = address;
        this.isDefault = isDefault;
    }
}
