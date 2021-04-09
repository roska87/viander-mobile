package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;

public class PostRandomRequestDto {

    @SerializedName("contains")
    private String contains;
    @SerializedName("notContains")
    private String notContains;

    public PostRandomRequestDto() {
    }

    public PostRandomRequestDto(String contains, String notContains) {
        this.contains = contains;
        this.notContains = notContains;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public String getNotContains() {
        return notContains;
    }

    public void setNotContains(String notContains) {
        this.notContains = notContains;
    }

}