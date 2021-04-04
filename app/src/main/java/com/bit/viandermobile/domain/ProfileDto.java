package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;

public class ProfileDto {

    @SerializedName("id")
    private int id;
    @SerializedName("image")
    private String image;
    @SerializedName("filters")
    private String filters;

    public ProfileDto() {
    }

    public ProfileDto(int id, String image, String filters) {
        this.id = id;
        this.image = image;
        this.filters = filters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }
}
