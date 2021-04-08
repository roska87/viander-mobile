package com.bit.viandermobile.repositories;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.bit.viandermobile.domain.LoginDto;
import com.bit.viandermobile.domain.LoginRequestDto;
import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.domain.ProfileDto;
import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.rest.RestApiClient;
import com.bit.viandermobile.rest.RestApiInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VianderRepository {

    private RestApiInterface apiService = RestApiClient.getClient().create(RestApiInterface.class);
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<UserDto> loggedUser = new MutableLiveData<>();
    private MutableLiveData<String> token = new MutableLiveData<>();
    private MutableLiveData<List<PostDto>> viandsMenu = new MutableLiveData<>();
    private Application application;

    public VianderRepository(Application application){
        this.application = application;
    }

    public boolean isLogged(){
        return username.getValue() != null;
    }

    public MutableLiveData<UserDto> getLoggedUser(){
        return loggedUser;
    }

    public MutableLiveData<String> getToken(){
        return token;
    }

    public MutableLiveData<List<PostDto>> getViands(){
        return viandsMenu;
    }

    public void login(String username2, String password){
        Log.i("username", username2);
        Log.i("password", password);
        LoginRequestDto login = new LoginRequestDto(username2, password);
        apiService.login(login).enqueue(new Callback<LoginDto>() {
            @Override
            public void onResponse(Call<LoginDto> call, Response<LoginDto> response) {
                if(response.body() != null && response.body().getKey() != null){
                    token.setValue(formatLoginKey(response.body().getKey()));
                    Log.i("login", token.getValue());
                    if(token != null){
                        username.setValue(username2);
                        getUser();
                    }
                }
                if(response.body() == null){
                    Toast.makeText(application.getApplicationContext(), "body is null", Toast.LENGTH_LONG).show();
                }
                if(response.body().getKey() == null){ //TODO chequear response.body() == null
                    Toast.makeText(application.getApplicationContext(), "key is null", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginDto> call, Throwable t) {
                Log.d("", "", t);
                Toast.makeText(application.getApplicationContext(), "Error en login: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout(){
        apiService.logout(token.getValue()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loggedUser = new MutableLiveData<>();
                username = new MutableLiveData<>();
                viandsMenu = new MutableLiveData<>();
                token = new MutableLiveData<>();
                Log.i("logout", response.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "Error en logout: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUser(){
        apiService.getUserByUsername(token.getValue(), username.getValue()).enqueue(new Callback<List<UserDto>>() {
            @Override
            public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                loggedUser.setValue(response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<UserDto>> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "Error al obtener usuario: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateProfile(ProfileDto profileDto){
        apiService.updateProfile(token.getValue(), profileDto.getId(), profileDto).enqueue(new Callback<ProfileDto>() {
            @Override
            public void onResponse(Call<ProfileDto> call, Response<ProfileDto> response) {
                getUser();
            }

            @Override
            public void onFailure(Call<ProfileDto> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "Error al actualizar perfil: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getPost(String token, int id){
        apiService.getPosts(token, id).enqueue(new Callback<PostDto>() {
            @Override
            public void onResponse(Call<PostDto> call, Response<PostDto> response) {
                Log.i("GET POST", response.body().toString());
                List<PostDto> postLst = new ArrayList<>();
                postLst.add(response.body());
                viandsMenu.setValue(postLst);
            }

            @Override
            public void onFailure(Call<PostDto> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "Error al obtener post: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String formatLoginKey(String loginKey){
        if(TextUtils.isEmpty(loginKey)){
            return null;
        }
        return "Token " + loginKey;
    }



}
