package com.quizlock.app;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(android.content.Context newBase) {
        PrefsManager tempPm = new PrefsManager(newBase);
        super.attachBaseContext(LocaleHelper.updateResources(newBase, tempPm.getLanguage()));
    }


    private PrefsManager pm;
    private String interceptedPackage;
    private String appId;
    private boolean isSettingsUnlock = false;

    private int currentStep = 0;
    private int totalGoals;
    private List<QuizData.Question> questions = new ArrayList<>();
    private boolean answered = false;

    private TextView txtAppName, txtProgress, txtQuestion, txtError;
    private TextView[] optViews;
    private View interstitialCard, quizCard;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        pm = new PrefsManager(this);

        txtAppName = findViewById(R.id.quiz_app_name);
        txtProgress = findViewById(R.id.quiz_progress);
        txtQuestion = findViewById(R.id.quiz_question);
        txtError = findViewById(R.id.quiz_error_msg);
        interstitialCard = findViewById(R.id.interstitial_card);
        quizCard = findViewById(R.id.quiz_card);
        
        optViews = new TextView[]{
                findViewById(R.id.quiz_opt_0),
                findViewById(R.id.quiz_opt_1),
                findViewById(R.id.quiz_opt_2),
                findViewById(R.id.quiz_opt_3)
        };
        
        findViewById(R.id.quiz_cancel).setOnClickListener(v -> goHome());

        findViewById(R.id.btn_interstitial_home).setOnClickListener(v -> {
            pm.incrementResistedToday();
            goHome();
        });
        
        findViewById(R.id.btn_interstitial_quiz).setOnClickListener(v -> {
            interstitialCard.setVisibility(View.GONE);
            quizCard.setVisibility(View.VISIBLE);
            showQuestion(0);
        });

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            optViews[i].setOnClickListener(v -> onAnswer(idx));
        }

        handleIntent();
    }

    private void goHome() {
        if (isSettingsUnlock) finish();
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void handleIntent() {
        isSettingsUnlock = getIntent().getBooleanExtra("SETTINGS_UNLOCK", false);
        interceptedPackage = getIntent().getStringExtra("INTERCEPTED_PACKAGE");

        if (interceptedPackage == null) { finish(); return; }

        String lang = pm.getLanguage();

        if (isSettingsUnlock) {
            appId = interceptedPackage;
            txtAppName.setText(getString(R.string.quiz_removing) + appId.toUpperCase());
            totalGoals = 10;
            questions = QuizData.getUnlockQuiz(lang);
        } else {
            if (pm.isUnlocked(interceptedPackage)) { finish(); return; }
            if (pm.isInCooldown()) { showCooldownAndClose(); return; }
            appId = QuizData.packageToId(interceptedPackage);
            txtAppName.setText(appId != null ? appId.toUpperCase() : "QUIZ");
            totalGoals = 5;
            questions = QuizData.getQuestions(lang, pm.getTopic(), pm.getDifficulty());
        }

        if (questions == null || questions.isEmpty()) {
            finish();
            return;
        }

        // Shuffle e selezione per garantire varietà se il set è grande
        Collections.shuffle(questions);
        
        // FONDAMENTALE: Se ci sono meno domande del target, abbassa il target al numero di domande disponibili
        totalGoals = Math.min(totalGoals, questions.size());
        
        if (questions.size() > totalGoals) {
            questions = questions.subList(0, totalGoals);
        }

        // Se è uno sblocco per le impostazioni saltiamo l'attesa di riflessione
        if (isSettingsUnlock) {
            interstitialCard.setVisibility(View.GONE);
            quizCard.setVisibility(View.VISIBLE);
            showQuestion(0);
        } else {
            interstitialCard.setVisibility(View.VISIBLE);
            quizCard.setVisibility(View.GONE);
        }
    }

    private void showQuestion(int step) {
        if (step >= questions.size()) { finish(); return; }
        currentStep = step;
        answered = false;
        txtError.setVisibility(View.GONE);
        txtProgress.setText((step + 1) + "/" + totalGoals);
        
        QuizData.Question q = questions.get(step);
        txtQuestion.setText(q.q);

        for (int i = 0; i < 4; i++) {
            if (i < q.opts.length) {
                optViews[i].setVisibility(View.VISIBLE);
                optViews[i].setText(q.opts[i]);
                optViews[i].setBackgroundResource(R.drawable.bg_option);
                optViews[i].setBackgroundTintList(null);
                optViews[i].setTextColor(Color.WHITE);
                optViews[i].setEnabled(true);
            } else {
                optViews[i].setVisibility(View.GONE);
            }
        }
    }

    private void onAnswer(int selected) {
        if (answered) return;
        answered = true;
        QuizData.Question q = questions.get(currentStep);
        boolean correct = (selected == q.answer);

        if (correct) {
            optViews[selected].setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            optViews[selected].setTextColor(Color.BLACK);
            handler.postDelayed(() -> {
                if (currentStep + 1 >= totalGoals) {
                    if (isSettingsUnlock) {
                        pm.removeBlockedId(appId);
                        setResult(RESULT_OK);
                    } else {
                        pm.setUnlockExpiry(interceptedPackage, System.currentTimeMillis() + (pm.getDurationSeconds() * 1000L));
                        pm.incrementUnlocksToday();
                    }
                    finish();
                } else {
                    showQuestion(currentStep + 1);
                }
            }, 600);
        } else {
            optViews[selected].setTextColor(Color.GRAY);
            if (!isSettingsUnlock) pm.setCooldownEnd(System.currentTimeMillis() + 120000);
            handler.postDelayed(this::showCooldownAndClose, 800);
        }
        for (TextView v : optViews) v.setEnabled(false);
    }

    private void showCooldownAndClose() {
        txtQuestion.setVisibility(View.GONE);
        for (TextView v : optViews) v.setVisibility(View.GONE);
        txtProgress.setText(R.string.quiz_error);
        txtError.setText(isSettingsUnlock ? getString(R.string.quiz_block_kept) : getString(R.string.quiz_cooldown));
        txtError.setVisibility(View.VISIBLE);
        handler.postDelayed(this::goHome, 1500);
    }
}
