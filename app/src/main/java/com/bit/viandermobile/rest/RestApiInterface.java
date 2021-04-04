package com.bit.viandermobile.rest;

import com.bit.viandermobile.domain.LoginDto;
import com.bit.viandermobile.domain.LoginRequestDto;
import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.domain.ProfileDto;
import com.bit.viandermobile.domain.UserDto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiInterface {

    @POST("/auth/login/")
    Call<LoginDto> login(@Body LoginRequestDto loginRequestDto);

    @GET("/api/posts/{id}")
    Call<PostDto> getPosts(@Header("Authorization") String token,
                           @Path("id") int id);

    @GET("/api/profiles/{id}")
    Call<ProfileDto> getProfiles(@Header("Authorization") String token,
                                 @Path("id") int id);

    @PUT("/api/profiles/{id}")
    Call<ProfileDto> updateProfile(@Header("Authorization") String token,
                                   @Path("id") int id,
                                   @Body ProfileDto profileDto);

    @GET("/api/users/{id}")
    Call<UserDto> getUser(@Header("Authorization") String token,
                          @Path("id") int id);

    @GET("/api/users/")
    Call<List<UserDto>> getUserByUsername(@Header("Authorization") String token,
                                          @Query("username") String username);

}