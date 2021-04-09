package com.bit.viandermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.bit.viandermobile.entities.Session;
import com.bit.viandermobile.factories.SessionFactory;
import com.bit.viandermobile.models.SessionViewModel;

import static  com.bit.viandermobile.constants.Constants.*;

public class MainActivity extends AppCompatActivity {

    private SessionViewModel sessionViewModel;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        sessionViewModel = new ViewModelProvider(this, new SessionFactory(getApplication())).get(SessionViewModel.class);

        startHeavyProcessing();

        /*
        sessionViewModel.getSession().observe(this, new Observer<Session>() {
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
         */
    }

    private void startHeavyProcessing(){
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //some heavy processing resulting in a Data String
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            return "Login!";
        }

        @Override
        protected void onPostExecute(String result) {
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

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


}