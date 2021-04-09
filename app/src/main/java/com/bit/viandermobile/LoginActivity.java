package com.bit.viandermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bit.viandermobile.entities.Session;
import com.bit.viandermobile.factories.SessionFactory;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.SessionViewModel;
import com.bit.viandermobile.models.VianderViewModel;

import static  com.bit.viandermobile.constants.Constants.*;

public class LoginActivity extends AppCompatActivity {

    public static final int LOGIN_REQUEST_CODE = 1;
    public static final String MSG_TOKEN = "com.bit.vianderapp.MSG_TOKEN";

    private VianderViewModel vianderViewModel;
    private SessionViewModel sessionViewModel;

    SharedPreferences sharedpreferences;
    String email, password, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initializing EditTexts and our Button
        EditText emailEdt = findViewById(R.id.idEdtEmail);
        EditText passwordEdt = findViewById(R.id.idEdtPassword);
        Button loginBtn = findViewById(R.id.idBtnLogin);

        // getting the data which is stored in shared preferences.
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        //email = sharedpreferences.getString(EMAIL_KEY, null);
        //password = sharedpreferences.getString(PASSWORD_KEY, null);

        vianderViewModel = new ViewModelProvider(this, new VianderFactory(getApplication())).get(VianderViewModel.class);
        sessionViewModel = new ViewModelProvider(this, new SessionFactory(getApplication())).get(SessionViewModel.class);

        // calling on click listener for login button.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to check if the user fields are empty or not.
                if (TextUtils.isEmpty(emailEdt.getText().toString()) && TextUtils.isEmpty(passwordEdt.getText().toString())) {
                    // this method will call when email and password fields are empty.
                    Toast.makeText(LoginActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    String email = emailEdt.getText().toString();
                    String password = passwordEdt.getText().toString();

                    // below two lines will put values for
                    // email and password in shared preferences.
                    editor.putString(EMAIL_KEY, emailEdt.getText().toString());
                    editor.putString(PASSWORD_KEY, passwordEdt.getText().toString());

                    // to save our data with key and value.
                    editor.apply();

                    vianderViewModel.login(email, password);
                    vianderViewModel.getLoggedUser().observeForever(userDto -> {
                        editor.putString(TOKEN_KEY, vianderViewModel.getToken().getValue());
                        editor.putString(USERNAME_KEY, userDto.getUsername());
                        editor.apply();

                        sessionViewModel.save(new Session(vianderViewModel.getToken().getValue(), userDto.getUsername()));

                        Log.i("LoggedUser", userDto.getUsername());
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    });

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (token != null) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}