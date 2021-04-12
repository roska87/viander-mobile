package com.bit.viandermobile.models;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.domain.ProfileDto;
import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.repositories.VianderRepository;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VianderViewModel extends AndroidViewModel {

    private VianderRepository vianderRepository;
    private LiveData<UserDto> loggedUser;
    private LiveData<String> token;
    private LiveData<List<PostDto>> viandsMenu;
    private LiveData<Map<Integer, PostDto>> menu;

    public VianderViewModel(@NonNull Application application){
        super(application);
        vianderRepository = new VianderRepository(application);
        loggedUser = vianderRepository.getLoggedUser();
        token = vianderRepository.getToken();
        viandsMenu = vianderRepository.getViands();
        menu = vianderRepository.getRandomPosts();
    }

    public LiveData<UserDto> getLoggedUser(){
        return loggedUser;
    }

    public void refreshLoggedUser(String token, String username){
        vianderRepository.getUser(token, username);
    }

    public LiveData<String> getToken(){
        return token;
    }

    public LiveData<List<PostDto>> getViandsMenu(){
        return viandsMenu;
    }

    public LiveData<Map<Integer, PostDto>> getMenu(){
        return menu;
    }

    public void changeViand(String token, List<Integer> viandPositions){
        vianderRepository.changePost(token, viandPositions);
    }

    public void login(String username, String password){
        vianderRepository.login(username, password);
    }

    public void logout(){
        vianderRepository.logout();
    }

    public void updateProfile(String token, String username, List<Pair<Boolean, String>> filterList, List<Integer> weekDays){
        String filters = null;
        if(filterList != null && !filterList.isEmpty()){
            filters = formatFilters(filterList);
        }
        ProfileDto profileDto = new ProfileDto();
        profileDto.setFilters(filters);
        profileDto.setWeekDays(StringUtils.join(weekDays, ","));
        vianderRepository.updateProfile(token, username, profileDto);
    }

    public void getPost(String token, int id){
        vianderRepository.getPost(token, id);
    }

    private String formatFilters(List<Pair<Boolean, String>> filterList){
        StringBuilder filters = new StringBuilder();
        for(Pair<Boolean, String> pair : filterList){
            filters.append(pair.first ? "" : "!").append(pair.second).append(",");
        }
        return filters.toString();
    }

}