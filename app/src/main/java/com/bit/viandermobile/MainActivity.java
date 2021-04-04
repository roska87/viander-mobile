package com.bit.viandermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.bit.viandermobile.domain.UserDto;
import com.bit.viandermobile.models.VianderViewModel;

public class MainActivity extends AppCompatActivity {

    private VianderViewModel vianderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vianderViewModel = new ViewModelProvider(this, new VianderFactory(getApplication())).get(VianderViewModel.class);
        vianderViewModel.login("roska87", "roska123");
        vianderViewModel.getLoggedUser().observeForever(userDto -> {
            Log.i("LoggedUser", userDto.getUsername());
        });

    }
}