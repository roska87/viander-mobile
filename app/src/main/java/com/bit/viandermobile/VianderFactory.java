package com.bit.viandermobile;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.bit.viandermobile.models.VianderViewModel;

public class VianderFactory extends ViewModelProvider.NewInstanceFactory {

    @NonNull
    private final Application application;

    public VianderFactory(@NonNull Application application){
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass == VianderViewModel.class){
            return (T) new VianderViewModel(application);
        }
        return null;
    }

}