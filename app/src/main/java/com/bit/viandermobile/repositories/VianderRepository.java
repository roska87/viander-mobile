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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VianderRepository {

    private RestApiInterface apiService = RestApiClient.getClient().create(RestApiInterface.class);
    private String token = null;
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<UserDto> loggedUser = new MutableLiveData<>();
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

    public MutableLiveData<List<PostDto>> getViands(){
        return viandsMenu;
    }

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    /*
    public boolean login(String username2, String password){
        LoginRequestDto loginRequestDto = new LoginRequestDto(username2, password);
        try {
            Response<LoginDto> loginDto = apiService.login(loginRequestDto).execute();
            token = this.formatLoginKey(loginDto.body().getKey());
            if(token != null){
                username.setValue(username2);
                getUser();
                return true;
            }
        } catch (IOException e) {
            Toast.makeText(application.getApplicationContext(), "Error en login: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

     */


    public void login(String username2, String password){
        LoginRequestDto login = new LoginRequestDto(username2, password);
        apiService.login(login).enqueue(new Callback<LoginDto>() {
            @Override
            public void onResponse(Call<LoginDto> call, Response<LoginDto> response) {
                token = formatLoginKey(response.body().getKey());
                Log.i("login", token);
                if(token != null){
                    username.setValue(username2);
                    getUser();
                }
            }

            @Override
            public void onFailure(Call<LoginDto> call, Throwable t) {
                Log.d("", "", t);
                Toast.makeText(application.getApplicationContext(), "Error en login: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUser(){
        apiService.getUserByUsername(token, username.getValue()).enqueue(new Callback<List<UserDto>>() {
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
        apiService.updateProfile(token, profileDto.getId(), profileDto).enqueue(new Callback<ProfileDto>() {
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

    private String formatLoginKey(String loginKey){
        if(TextUtils.isEmpty(loginKey)){
            return null;
        }
        return "Token " + loginKey;
    }



}
