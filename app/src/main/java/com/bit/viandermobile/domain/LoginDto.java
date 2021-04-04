package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;

public class LoginDto {

    @SerializedName("key")
    private String key;

    public LoginDto() {
    }

    public LoginDto(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}