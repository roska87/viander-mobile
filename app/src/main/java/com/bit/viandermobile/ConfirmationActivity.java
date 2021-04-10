package com.bit.viandermobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static com.bit.viandermobile.constants.Constants.*;
import org.apache.commons.lang3.StringUtils;

public class ConfirmationActivity extends AppCompatActivity {

    private static final int TOTAL_AMOUNT_DEFAULT = 0;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);

        Intent intent = getIntent();
        int totalAmount = intent.getIntExtra(TOTAL_AMOUNT, TOTAL_AMOUNT_DEFAULT);
        totalAmountTextView.setText(StringUtils.join(getString(R.string.confirmation_total_amount_message), " ", totalAmount));

        Button confirmationBtn = findViewById(R.id.backHomeButton);
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfirmationActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}