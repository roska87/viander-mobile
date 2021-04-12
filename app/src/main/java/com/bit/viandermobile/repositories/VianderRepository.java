package com.bit.viandermobile.repositories;

import android.app.Application;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.bit.viandermobile.R;
import com.bit.viandermobile.domain.LoginDto;
import com.bit.viandermobile.domain.LoginRequestDto;
import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.domain.PostRandomDto;
import com.bit.viandermobile.domain.PostRandomRequestDto;
import com.bit.viandermobile.domain.ProfileDto;
import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.rest.RestApiClient;
import com.bit.viandermobile.rest.RestApiInterface;
import com.bit.viandermobile.utils.TokenUtil;
import com.google.android.gms.common.util.CollectionUtils;
import static org.apache.commons.lang3.StringUtils.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VianderRepository {

    private RestApiInterface apiService = RestApiClient.getClient().create(RestApiInterface.class);
    private MutableLiveData<UserDto> loggedUser = new MutableLiveData<>();
    private MutableLiveData<String> token = new MutableLiveData<>();
    private MutableLiveData<List<PostDto>> viandsMenu = new MutableLiveData<>();
    private MutableLiveData<Map<Integer, PostDto>> randomPosts = new MutableLiveData<>();
    private Application application;

    public VianderRepository(Application application){
        this.application = application;
    }

    public LiveData<UserDto> getLoggedUser(){
        return loggedUser;
    }

    public LiveData<String> getToken(){
        return token;
    }

    public LiveData<List<PostDto>> getViands(){
        return viandsMenu;
    }

    public LiveData<Map<Integer, PostDto>> getRandomPosts() {
        return randomPosts;
    }

    public void login(String username, String password){
        Log.i("username", username);
        Log.i("password", password);
        LoginRequestDto login = new LoginRequestDto(username, password);
        apiService.login(login).enqueue(new Callback<LoginDto>() {
            @Override
            public void onResponse(Call<LoginDto> call, Response<LoginDto> response) {
                if(response.body() != null && response.body().getKey() != null){
                    String tokenKey = response.body().getKey();
                    Log.i("login", tokenKey);
                    if(tokenKey != null){
                        String tokenStr = TokenUtil.formatTokenKey(tokenKey);
                        token.setValue(tokenStr);
                        getUser(tokenStr, username);
                    }
                }
                if(response.code() == 400){
                    Toast.makeText(application.getApplicationContext(), R.string.invalid_credentials, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginDto> call, Throwable t) {
                Log.d("", "", t);
                String message = join(application.getApplicationContext().getString(R.string.invalid_credentials), " ", t.getMessage());
                Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logout(){
        apiService.logout(token.getValue()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loggedUser = new MutableLiveData<>();
                viandsMenu = new MutableLiveData<>();
                token = new MutableLiveData<>();
                Log.i("logout", response.body().toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String message = join(application.getApplicationContext().getString(R.string.error_logout), " ", t.getMessage());
                Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUser(String token, String username){
        apiService.getUserByUsername(token, username).enqueue(new Callback<List<UserDto>>() {
            @Override
            public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                if(CollectionUtils.isEmpty(response.body())){
                    String message = join(application.getApplicationContext().getString(R.string.error_user_not_found));
                    Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    return;
                }
                loggedUser.setValue(response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<UserDto>> call, Throwable t) {
                String message = join(application.getApplicationContext().getString(R.string.error_get_user), " ", t.getMessage());
                Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateProfile(String token, String username, ProfileDto profileDto){
        apiService.getUserByUsername(token, username).enqueue(new Callback<List<UserDto>>() {
            @Override
            public void onResponse(Call<List<UserDto>> call, Response<List<UserDto>> response) {
                UserDto user = response.body().get(0);
                ProfileDto profile = user.getProfile();
                profile.setFilters(profileDto.getFilters());
                profile.setWeekDays(profileDto.getWeekDays());
                apiService.updateProfile(token, profile).enqueue(new Callback<ProfileDto>() {
                    @Override
                    public void onResponse(Call<ProfileDto> call, Response<ProfileDto> response) {
                        getUser(token, username);
                        Log.i("Perfil", "actualizado");
                    }

                    @Override
                    public void onFailure(Call<ProfileDto> call, Throwable t) {
                        Log.e("Perfil error: ", t.getMessage());
                        String message = join(application.getApplicationContext().getString(R.string.error_profile_update), " ", t.getMessage());
                        Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserDto>> call, Throwable t) {
                String message = join(application.getApplicationContext().getString(R.string.error_get_profile), " ", t.getMessage());
                Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                String message = join(application.getApplicationContext().getString(R.string.error_get_post), " ", t.getMessage());
                Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getRandomPosts(String token, int limit){
        PostRandomRequestDto postRandomRequestDto = getFilters();
        apiService.getPostRandom(token, limit, postRandomRequestDto).enqueue(new Callback<PostRandomDto>() {
            @Override
            public void onResponse(Call<PostRandomDto> call, Response<PostRandomDto> response) {
                PostRandomDto randomDto = response.body();
                Map<Integer, PostDto> map = new HashMap<>();
                if(randomDto.getResults() != null && randomDto.getResults().size() > 0){
                    for(int i = 0; i < randomDto.getResults().size(); i++){
                        map.put(i+1, randomDto.getResults().get(i));
                    }
                }
                randomPosts.setValue(map);
            }

            @Override
            public void onFailure(Call<PostRandomDto> call, Throwable t) {
                String message = join(application.getApplicationContext().getString(R.string.error_get_random_post), " ", t.getMessage());
                Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void changePost(String token, List<Integer> positions){
        PostRandomRequestDto postRandomRequestDto = getFilters();
        apiService.getPostRandom(token, positions.size(), postRandomRequestDto).enqueue(new Callback<PostRandomDto>() {
            @Override
            public void onResponse(Call<PostRandomDto> call, Response<PostRandomDto> response) {
                PostRandomDto randomDto = response.body();
                Map<Integer, PostDto> map = randomPosts.getValue();
                for(int i = 0; i < positions.size(); i++){
                    map.put(positions.get(i), randomDto.getResults().get(i));
                }
                randomPosts.setValue(map);
            }

            @Override
            public void onFailure(Call<PostRandomDto> call, Throwable t) {
                String message = join(application.getApplicationContext().getString(R.string.error_change_random_post), " ", t.getMessage());
                Toast.makeText(application.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private PostRandomRequestDto getFilters(){
        List<String> contains = new ArrayList<>();
        List<String> notContains = new ArrayList<>();
        String filters = loggedUser.getValue().getProfile().getFilters();
        String[] filtersArray = filters.split(",");
        for(String filter : filtersArray){
            if(filter.startsWith("!")){
                notContains.add(filter.replace("!", ""));
            }else{
                contains.add(filter);
            }
        }
        String containsStr = join(contains, ",");
        String notContainsStr = join(notContains, ",");
        return new PostRandomRequestDto(containsStr, notContainsStr);
    }

}