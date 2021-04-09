package com.bit.viandermobile.factories;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.bit.viandermobile.models.SessionViewModel;

public class SessionFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Application application;

    public SessionFactory(@NonNull Application application){
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == SessionViewModel.class){
            return (T) new SessionViewModel(application);
        }
        return null;
    }

}