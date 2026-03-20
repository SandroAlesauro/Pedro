package com.pedro.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PoeActivity extends AppCompatActivity {

    private TextView poeMessage;
    private TextView poeTimer;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        prefs = getSharedPreferences("com.quizlock.app.prefs", MODE_PRIVATE);
        
        // Verifica se l'onboarding è già stato mostrato oggi
        if (hasShownToday()) {
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_poe);

        poeMessage = findViewById(R.id.poe_message);
        poeTimer = findViewById(R.id.poe_timer);

        // Seleziona un messaggio casuale
        String[] messages = getResources().getStringArray(R.array.poe_messages);
        poeMessage.setText(messages[new Random().nextInt(messages.length)]);

        // Disabilita back button
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Fai niente - attrito forzato
            }
        });

        // Avvia il countdown di 5 secondi
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                poeTimer.setText("ACCESSO IN: " + (millisUntilFinished / 1000));
            }

            public void onFinish() {
                markAsShownToday();
                startMainActivity();
            }
        }.start();
    }

    private boolean hasShownToday() {
        String lastShownDate = prefs.getString("last_poe_date", "");
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return currentDate.equals(lastShownDate);
    }

    private void markAsShownToday() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        prefs.edit().putString("last_poe_date", currentDate).apply();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
