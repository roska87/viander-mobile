package com.bit.viandermobile.models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bit.viandermobile.entities.Session;
import com.bit.viandermobile.repositories.LocalRepository;

public class SessionViewModel extends AndroidViewModel {

    private LocalRepository localRepository;
    private final LiveData<Session> session;

    public SessionViewModel(@NonNull Application application) {
        super(application);
        localRepository = new LocalRepository(application);
        session = localRepository.getSession();
    }

    public LiveData<Session> getSession(){
        return session;
    }

    public void save(Session session){
        localRepository.insert(session);
    }

    public void delete(){
        localRepository.delete();
    }

}