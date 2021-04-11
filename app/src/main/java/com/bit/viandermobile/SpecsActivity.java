package com.bit.viandermobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;


public class SpecsActivity extends AppCompatActivity {

    Timer timer;
    CheckBox ch1, ch2, ch3, ch4, ch5, ch6, ch7, ch8, ch9, ch10, ch11;
    Button btnReset, btnConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specs);
        ch1 = findViewById(R.id.checkbox1);
        ch2 = findViewById(R.id.checkbox2);
        ch3 = findViewById(R.id.checkbox3);
        ch4 = findViewById(R.id.checkbox4);
        ch5 = findViewById(R.id.checkbox5);
        ch6 = findViewById(R.id.checkbox6);
        ch7 = findViewById(R.id.checkbox7);
        ch8 = findViewById(R.id.checkbox8);
        ch9 = findViewById(R.id.checkbox9);
        ch10 = findViewById(R.id.checkbox10);
        ch11 = findViewById(R.id.checkbox11);

        btnReset = findViewById(R.id.btnReset);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuffer result = new StringBuffer();
                result.append("Confirmado");
                Messages.message(  SpecsActivity.this, result.toString());

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Intent a = new Intent(SpecsActivity.this, HomeActivity.class);
                        startActivity(a);
                    }
                }, 500);
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
                Messages.message(  SpecsActivity.this, result2.toString());
            }

        });
    {

        }
    }
}