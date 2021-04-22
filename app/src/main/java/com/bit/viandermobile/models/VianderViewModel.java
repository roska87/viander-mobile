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
import com.google.android.gms.common.util.CollectionUtils;

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
    private LiveData<List<PostDto>> viandCounts;
    private LiveData<List<PostDto>> allViands;

    public VianderViewModel(@NonNull Application application){
        super(application);
        vianderRepository = new VianderRepository(application);
        loggedUser = vianderRepository.getLoggedUser();
        token = vianderRepository.getToken();
        viandsMenu = vianderRepository.getViands();
        menu = vianderRepository.getRandomPosts();
        viandCounts = vianderRepository.getViandCounts();
        allViands = vianderRepository.getAllViands();
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

    public LiveData<List<PostDto>> getViandCounts() {
        return viandCounts;
    }

    public LiveData<List<PostDto>> getAllViands() {
        return allViands;
    }

    public void getViandCounts(String token){
        vianderRepository.getViandCounts(token);
    }

    public void getMenu(String token, String username){
        vianderRepository.getRandomPosts(token, username);
    }

    public void changeViand(String token, String username, List<Integer> viandPositions){
        vianderRepository.changePost(token, username, viandPositions);
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

    public void getAllViands(String token, String username){
        vianderRepository.getAvailablePosts(token, username);
    }

    private String formatFilters(List<Pair<Boolean, String>> filterList){
        StringBuilder filters = new StringBuilder();
        for(Pair<Boolean, String> pair : filterList){
            filters.append(pair.first ? "" : "!").append(pair.second).append(",");
        }
        return filters.toString();
    }

    public void updateViandCount(String token, List<Integer> viandIds){
        if(!CollectionUtils.isEmpty(viandIds)){
            vianderRepository.updateViandCounts(token, viandIds);
        }
    }

    public void updateViand(int dayNumber, PostDto postDto){
        vianderRepository.updateViand(dayNumber, postDto);
    }

}