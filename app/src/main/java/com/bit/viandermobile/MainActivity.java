package com.bit.viandermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.bit.viandermobile.entities.Session;
import com.bit.viandermobile.factories.SessionFactory;
import com.bit.viandermobile.models.SessionViewModel;

import static  com.bit.viandermobile.constants.Constants.*;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000;

    private SessionViewModel sessionViewModel;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sessionViewModel = new ViewModelProvider(this, new SessionFactory(getApplication())).get(SessionViewModel.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sessionViewModel.getSession().observe(MainActivity.this, new Observer<Session>() {
                    @Override
                    public void onChanged(Session session) {
                        Intent intent = null;
                        if(session == null){
                            Log.i("Launch", "Login");
                            intent = new Intent(MainActivity.this, LoginActivity.class);
                        }else{
                            Log.i("Launch", "Home");
                            intent = new Intent(MainActivity.this, HomeActivity.class);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(USERNAME_KEY, session.getUsername());
                            editor.putString(TOKEN_KEY, session.getToken());
                            editor.apply();
                        }
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }, SPLASH_TIME_OUT);

    }

}