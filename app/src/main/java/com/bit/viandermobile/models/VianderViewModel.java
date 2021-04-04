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

import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.domain.ProfileDto;
import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.repositories.VianderRepository;

import java.util.List;
import java.util.stream.Collectors;

public class VianderViewModel extends AndroidViewModel {

    private VianderRepository vianderRepository;
    private MutableLiveData<UserDto> loggedUser;
    private MutableLiveData<List<PostDto>> viandsMenu;

    public VianderViewModel(@NonNull Application application){
        super(application);
        vianderRepository = new VianderRepository(application);
        loggedUser = vianderRepository.getLoggedUser();
        viandsMenu = vianderRepository.getViands();
    }

    public LiveData<UserDto> getLoggedUser(){
        return loggedUser;
    }

    public LiveData<List<PostDto>> getViandsMenu(){
        return viandsMenu;
    }

    public void login(String username, String password){
        vianderRepository.login(username, password);
    }

    public void updateProfile(List<Pair<Boolean, String>> filterList){
        if(filterList == null || filterList.isEmpty()){
            return;
        }
        String filters = formatFilters(filterList);
        ProfileDto profileDto = loggedUser.getValue().getProfile();
        profileDto.setFilters(filters);
        vianderRepository.updateProfile(profileDto);
    }

    public void changeViand(int viandId){
        //TODO
    }

    private String formatFilters(List<Pair<Boolean, String>> filterList){
        StringBuilder filters = new StringBuilder();
        for(Pair<Boolean, String> pair : filterList){
            filters.append(pair.first ? "" : "!").append(pair.second).append(",");
        }
        return filters.toString();
    }

}