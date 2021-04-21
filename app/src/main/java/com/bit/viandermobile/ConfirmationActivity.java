package com.bit.viandermobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bit.viandermobile.entities.Session;

import static com.bit.viandermobile.constants.Constants.*;
import org.apache.commons.lang3.StringUtils;

public class ConfirmationActivity extends AppCompatActivity {

    private static final int RECEIVED_NOTIFICATION_ID = 1;
    private static final int TOTAL_AMOUNT_DEFAULT = 0;
    private static final int SPLASH_TIME_OUT = 5000;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        TextView totalAmountTextView = findViewById(R.id.totalAmountTextView);

        Intent intent = getIntent();
        int totalAmount = intent.getIntExtra(TOTAL_AMOUNT, TOTAL_AMOUNT_DEFAULT);
        totalAmountTextView.setText(StringUtils.join(getString(R.string.confirmation_total_amount_message), totalAmount));

        Button confirmationBtn = findViewById(R.id.backHomeButton);
        confirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConfirmationActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent1 = new Intent(ConfirmationActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(ConfirmationActivity.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ConfirmationActivity.this, getString(R.string.channel_id))
                        .setSmallIcon(R.drawable.ic_baseline_fastfood_24)
                        .setContentTitle(getText(R.string.request_state))
                        .setContentText(getText(R.string.request_confirm))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(getText(R.string.request_confirm)))
                        .setPriority(NotificationCompat.FLAG_BUBBLE)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ConfirmationActivity.this);
                notificationManager.notify(RECEIVED_NOTIFICATION_ID, builder.build());
            }
        }, SPLASH_TIME_OUT);


    }

}