package com.bit.viandermobile.rest;

import com.bit.viandermobile.domain.LoginDto;
import com.bit.viandermobile.domain.LoginRequestDto;
import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.domain.PostRandomDto;
import com.bit.viandermobile.domain.PostRandomRequestDto;
import com.bit.viandermobile.domain.PostViandCountDto;
import com.bit.viandermobile.domain.ProfileDto;
import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.domain.ViandCountDto;
import com.bit.viandermobile.domain.ViandCountResultDto;

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

    @POST("/auth/logout/")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @GET("/api/posts/{id}/")
    Call<PostDto> getPosts(@Header("Authorization") String token,
                           @Path("id") int id);

    @GET("/api/posts/")
    Call<List<PostDto>> getPosts(@Header("Authorization") String token);

    @POST("/api/random/")
    Call<PostRandomDto> getPostRandom(@Header("Authorization") String token,
                                      @Query("limit") int limit,
                                      @Body PostRandomRequestDto postRandomRequestDto);

    @GET("/api/profiles/{id}/")
    Call<ProfileDto> getProfiles(@Header("Authorization") String token,
                                 @Path("id") int id);

    @PUT("/api/profiles/")
    Call<ProfileDto> updateProfile(@Header("Authorization") String token,
                                   @Body ProfileDto profileDto);

    @GET("/api/users/{id}/")
    Call<UserDto> getUser(@Header("Authorization") String token,
                          @Path("id") int id);

    @GET("/api/users/")
    Call<List<UserDto>> getUserByUsername(@Header("Authorization") String token,
                                          @Query("username") String username);


    @POST("/api/viand_count/")
    Call<Object> postViandCount(@Header("Authorization") String token,
                                @Body PostViandCountDto postViandCountDto);

    @POST("/api/viand_count_result/")
    Call<ViandCountResultDto> getViandCount(@Header("Authorization") String token);

}