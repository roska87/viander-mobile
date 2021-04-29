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
import android.widget.TextView;
import com.bit.viandermobile.entities.Session;
import com.bit.viandermobile.factories.SessionFactory;
import com.bit.viandermobile.factories.VianderFactory;
import com.bit.viandermobile.models.SessionViewModel;
import com.bit.viandermobile.models.VianderViewModel;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import static  com.bit.viandermobile.constants.Constants.*;

public class LoginActivity extends AppCompatActivity {

    public static final int LOGIN_REQUEST_CODE = 1;
    public static final String MSG_TOKEN = "com.bit.vianderapp.MSG_TOKEN";

    private VianderViewModel vianderViewModel;
    private SessionViewModel sessionViewModel;

    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        setGooglePlusButtonText(signInButton, getString(R.string.google_login));

        // Initializing EditTexts and our Button
        EditText emailEdt = ((TextInputLayout) findViewById(R.id.idEdtEmail)).getEditText();
        EditText passwordEdt = ((TextInputLayout) findViewById(R.id.idEdtPassword)).getEditText();
        Button loginBtn = findViewById(R.id.idBtnLogin);

        // getting the data which is stored in shared preferences.
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        vianderViewModel = new ViewModelProvider(this, new VianderFactory(getApplication())).get(VianderViewModel.class);
        sessionViewModel = new ViewModelProvider(this, new SessionFactory(getApplication())).get(SessionViewModel.class);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.loginLayout), R.string.coming_soon, Snackbar.LENGTH_LONG).show();
            }
        });

        // calling on click listener for login button.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to check if the user fields are empty or not.
                if (TextUtils.isEmpty(emailEdt.getText().toString()) || TextUtils.isEmpty(passwordEdt.getText().toString())) {
                    // this method will call when email and password fields are empty.
                    Snackbar.make(findViewById(R.id.loginLayout), R.string.enter_fields_login, Snackbar.LENGTH_LONG).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String email = emailEdt.getText().toString();
                    String password = passwordEdt.getText().toString();

                    // below two lines will put values for
                    // email and password in shared preferences.
                    editor.putString(EMAIL_KEY, emailEdt.getText().toString());
                    editor.putString(PASSWORD_KEY, passwordEdt.getText().toString());

                    // to save our data with key and value.
                    editor.apply();

                    vianderViewModel.login(email, password);
                    vianderViewModel.getLoggedUser().observe(LoginActivity.this, userDto -> {
                        editor.putString(TOKEN_KEY, vianderViewModel.getToken().getValue());
                        editor.putString(USERNAME_KEY, userDto.getUsername());
                        editor.apply();

                        sessionViewModel.save(new Session(vianderViewModel.getToken().getValue(), userDto.getUsername()));

                        //Log.i("LoggedUser", userDto.getUsername());
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
    protected void onSaveInstanceState(Bundle outState) {
        EditText emailEdt = ((TextInputLayout) findViewById(R.id.idEdtEmail)).getEditText();
        EditText passwordEdt = ((TextInputLayout) findViewById(R.id.idEdtPassword)).getEditText();
        outState.putString(USERNAME_KEY, emailEdt.getText().toString());
        outState.putString(PASSWORD_KEY, passwordEdt.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        EditText emailEdt = ((TextInputLayout) findViewById(R.id.idEdtEmail)).getEditText();
        EditText passwordEdt = ((TextInputLayout) findViewById(R.id.idEdtPassword)).getEditText();
        emailEdt.setText(savedInstanceState.getString(USERNAME_KEY));
        passwordEdt.setText(savedInstanceState.getString(PASSWORD_KEY));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

}