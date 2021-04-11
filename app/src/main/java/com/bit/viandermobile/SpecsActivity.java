package com.bit.viandermobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Timer;
import java.util.TimerTask;


import androidx.appcompat.app.AppCompatActivity;

import static com.bit.viandermobile.constants.Constants.CHECKBOX_KEY;
import static com.bit.viandermobile.constants.Constants.SHARED_PREFS;
import static com.bit.viandermobile.constants.Constants.TOKEN_KEY;
import static com.bit.viandermobile.constants.Constants.USERNAME_KEY;


public class SpecsActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;


    Timer timer;
    private CheckBox ch1, ch2, ch3, ch4, ch5, ch6, ch7, ch8, ch9, ch10, ch11;
    private Button btnReset, btnConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specs);

        mPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
        ch1 = (CheckBox) findViewById(R.id.checkbox1);
        ch2 = (CheckBox) findViewById(R.id.checkbox2);
        ch3 = (CheckBox) findViewById(R.id.checkbox3);
        ch4 = (CheckBox) findViewById(R.id.checkbox4);
        ch5 = (CheckBox) findViewById(R.id.checkbox5);
        ch6 = (CheckBox) findViewById(R.id.checkbox6);
        ch7 = (CheckBox) findViewById(R.id.checkbox7);
        ch8 = (CheckBox) findViewById(R.id.checkbox8);
        ch9 = (CheckBox) findViewById(R.id.checkbox9);
        ch10 = (CheckBox) findViewById(R.id.checkbox10);
        ch11 = (CheckBox) findViewById(R.id.checkbox11);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnReset = (Button) findViewById(R.id.btnReset);

        checkSharedPreferences();


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {

                StringBuffer result = new StringBuffer();
                result.append("Confirmado");
                Messages.message(SpecsActivity.this, result.toString());

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent a = new Intent(SpecsActivity.this, HomeActivity.class);
                        startActivity(a);
                    }
                }, 500);

                if (ch1.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox), "False");
                    mEditor.commit();
                }

                if (ch2.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox2), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox2), "False");
                    mEditor.commit();
                }

                if (ch3.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox3), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox3), "False");
                    mEditor.commit();
                }

                if (ch4.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox4), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox4), "False");
                    mEditor.commit();
                }

                if (ch5.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox5), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox5), "False");
                    mEditor.commit();
                }

                if (ch6.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox6), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox6), "False");
                    mEditor.commit();
                }

                if (ch7.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox7), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox7), "False");
                    mEditor.commit();
                }

                if (ch8.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox8), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox8), "False");
                    mEditor.commit();
                }

                if (ch9.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox9), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox9), "False");
                    mEditor.commit();
                }

                if (ch10.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox10), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox10), "False");
                    mEditor.commit();
                }

                if (ch11.isChecked()) {
                    mEditor.putString(getString(R.string.checkbox11), "True");
                    mEditor.commit();
                }
                else {
                    mEditor.putString(getString(R.string.checkbox11), "False");
                    mEditor.commit();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ch1.isChecked())
                    ch1.setChecked(false);
                if (ch2.isChecked())
                    ch2.setChecked(false);
                if (ch3.isChecked())
                    ch3.setChecked(false);
                if (ch4.isChecked())
                    ch4.setChecked(false);
                if (ch5.isChecked())
                    ch5.setChecked(false);
                if (ch6.isChecked())
                    ch6.setChecked(false);
                if (ch7.isChecked())
                    ch7.setChecked(false);
                if (ch8.isChecked())
                    ch8.setChecked(false);
                if (ch9.isChecked())
                    ch9.setChecked(false);
                if (ch10.isChecked())
                    ch10.setChecked(false);
                if (ch11.isChecked())
                    ch11.setChecked(false);

                StringBuffer result2 = new StringBuffer();
                result2.append("Selección eliminada");
                Messages.message(SpecsActivity.this, result2.toString());

            }

        });
    }

    private void checkSharedPreferences(){
        String checkbox = mPreferences.getString(getString(R.string.checkbox), "False");
        String checkbox2 = mPreferences.getString(getString(R.string.checkbox2), "False");
        String checkbox3 = mPreferences.getString(getString(R.string.checkbox3), "False");
        String checkbox4 = mPreferences.getString(getString(R.string.checkbox4), "False");
        String checkbox5 = mPreferences.getString(getString(R.string.checkbox5), "False");
        String checkbox6 = mPreferences.getString(getString(R.string.checkbox6), "False");
        String checkbox7 = mPreferences.getString(getString(R.string.checkbox7), "False");
        String checkbox8 = mPreferences.getString(getString(R.string.checkbox8), "False");
        String checkbox9 = mPreferences.getString(getString(R.string.checkbox9), "False");
        String checkbox10 = mPreferences.getString(getString(R.string.checkbox10), "False");
        String checkbox11 = mPreferences.getString(getString(R.string.checkbox11), "False");


        if (checkbox.equals("True")){
            ch1.setChecked(true);
        }
        else {
            ch1.setChecked(false);
        }
        if (checkbox2.equals("True")){
            ch2.setChecked(true);
        }
        else {
            ch2.setChecked(false);
        }
        if (checkbox3.equals("True")){
            ch3.setChecked(true);
        }
        else {
            ch3.setChecked(false);
        }
        if (checkbox4.equals("True")){
            ch4.setChecked(true);
        }
        else {
            ch4.setChecked(false);
        }
        if (checkbox5.equals("True")){
            ch5.setChecked(true);
        }
        else {
            ch5.setChecked(false);
        }
        if (checkbox6.equals("True")){
            ch6.setChecked(true);
        }
        else {
            ch6.setChecked(false);
        }
        if (checkbox7.equals("True")){
            ch7.setChecked(true);
        }
        else {
            ch7.setChecked(false);
        }
        if (checkbox8.equals("True")){
            ch8.setChecked(true);
        }
        else {
            ch8.setChecked(false);
        }
        if (checkbox9.equals("True")){
            ch9.setChecked(true);
        }
        else {
            ch9.setChecked(false);
        }
        if (checkbox10.equals("True")){
            ch10.setChecked(true);
        }
        else {
            ch10.setChecked(false);
        }
        if (checkbox11.equals("True")){
            ch11.setChecked(true);
        }
        else {
            ch11.setChecked(false);
        }
    }




}