package com.itsbusinessposter.idebrandvideo.items;

import com.google.gson.annotations.SerializedName;

public class PaymentKey {

    @SerializedName("razorpayKeyId")
    public String razorpayKeyId;

    public PaymentKey(String razorpayKeyId) {
        this.razorpayKeyId = razorpayKeyId;
    }
}
