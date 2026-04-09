package com.pedro.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class PoeActivity extends AppCompatActivity {

    private TextView poeMessage;
    private TextView poeTimer;
    private PrefsManager pm;

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        PrefsManager tempPm = new PrefsManager(newBase);
        super.attachBaseContext(LocaleHelper.updateResources(newBase, tempPm.getLanguage()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        pm = new PrefsManager(this);
        
        // Verifica se l'onboarding è già stato mostrato oggi
        if (pm.hasShownPoeToday()) {
            startMainActivity();
            return;
        }

        setContentView(R.layout.activity_poe);

        poeMessage = findViewById(R.id.poe_message);
        poeTimer = findViewById(R.id.poe_timer);

        // Seleziona un messaggio casuale
        String[] messages = getResources().getStringArray(R.array.poe_messages);
        if (messages != null && messages.length > 0) {
            poeMessage.setText(messages[new Random().nextInt(messages.length)]);
        }

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
                int seconds = (int) (millisUntilFinished / 1000);
                if (seconds == 0) seconds = 1; // UI polish
                poeTimer.setText(getString(R.string.access_in, seconds));
            }

            public void onFinish() {
                pm.markPoeAsShownToday();
                startMainActivity();
            }
        }.start();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
