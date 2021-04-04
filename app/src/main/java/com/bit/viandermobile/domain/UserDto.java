package com.bit.viandermobile.domain;

import com.google.gson.annotations.SerializedName;

public class UserDto {

    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("profile")
    private ProfileDto profile;

    public UserDto() {
    }

    public UserDto(int id, String username, String email, ProfileDto profile) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.profile = profile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProfileDto getProfile() {
        return profile;
    }

    public void setProfile(ProfileDto profile) {
        this.profile = profile;
    }
}
