package com.bit.viandermobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bit.viandermobile.constants.Constants;
import com.bit.viandermobile.domain.PostDto;
import com.bit.viandermobile.factories.SessionFactory;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.SessionViewModel;
import com.bit.viandermobile.models.VianderViewModel;

import org.apache.commons.lang3.StringUtils;

import static  com.bit.viandermobile.constants.Constants.*;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private VianderViewModel vianderViewModel;
    private SessionViewModel sessionViewModel;

    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email, username, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        vianderViewModel = new ViewModelProvider(this, new VianderFactory(getApplication())).get(VianderViewModel.class);
        sessionViewModel = new ViewModelProvider(this, new SessionFactory(getApplication())).get(SessionViewModel.class);

        // initializing our shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // getting data from shared prefs and
        // storing it in our string variable.
        email = sharedpreferences.getString(EMAIL_KEY, null);
        username = sharedpreferences.getString(USERNAME_KEY, null);
        token = sharedpreferences.getString(TOKEN_KEY, null);
        if(token != null){
            Log.i("Token -> ", token);
        }

        vianderViewModel.getPost(token, 59);

        vianderViewModel.getViandsMenu().observe(this, new Observer<List<PostDto>>() {
            @Override
            public void onChanged(List<PostDto> postDtos) {
                if(postDtos != null && !postDtos.isEmpty()){
                    TextView postView = findViewById(R.id.idPost);
                    postView.setText(postDtos.get(0).getTitle());
                }
            }
        });


        Button confirmationBtn = findViewById(R.id.idBtnConfirmation);
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ConfirmationActivity.class);
                i.putExtra(TOTAL_AMOUNT, 123);
                startActivity(i);
                finish();
            }
        });


        // initializing our textview and button.
        TextView welcomeTV = findViewById(R.id.idTVWelcome);
        welcomeTV.setText(StringUtils.join(getString(R.string.welcome), " ", email));
        Button logoutBtn = findViewById(R.id.idBtnLogout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionViewModel.delete();

                // calling method to edit values in shared prefs.
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(TOKEN_KEY, null);
                editor.remove(EMAIL_KEY);
                editor.remove(PASSWORD_KEY);

                // below line will clear
                // the data in shared prefs.
                editor.clear();

                // below line will apply empty
                // data to shared prefs.
                editor.apply();

                vianderViewModel.logout();

                // starting mainactivity after
                // clearing values in shared preferences.
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}