package com.example.stardaapp;

import com.google.gson.annotations.SerializedName;

public class ResponseHapus {
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;
    String getMessage() {
        return message;
    }
    boolean getSuccess() {
        return success;
    }
}
