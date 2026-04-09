package com.pedro.app;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private PrefsManager pm;
    private FrameLayout contentFrame;
    private TextView navHome, navSettings;
    private boolean isShowingSettings = false;

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(this, R.string.active, Toast.LENGTH_SHORT).show();
                    if (isShowingSettings) refreshSettings();
                }
            }
    );

    @Override
    protected void attachBaseContext(Context newBase) {
        PrefsManager tempPm = new PrefsManager(newBase);
        super.attachBaseContext(LocaleHelper.updateResources(newBase, tempPm.getLanguage()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isShowingSettings", isShowingSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState != null) {
            isShowingSettings = savedInstanceState.getBoolean("isShowingSettings", false);
        }

        pm = new PrefsManager(this);
        contentFrame = findViewById(R.id.content_frame);
        navHome = findViewById(R.id.nav_home);
        navSettings = findViewById(R.id.nav_settings);

        navHome.setOnClickListener(v -> showHome());
        navSettings.setOnClickListener(v -> showSettings());

        if (pm.isFirstLaunch()) {
            new AlertDialog.Builder(this)
                .setTitle(R.string.onboarding_title)
                .setMessage(R.string.onboarding_desc)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_ok, (d, w) -> {
                    pm.setFirstLaunchDone();
                    showHome();
                    checkPermissions();
                })
                .show();
        } else {
            if (isShowingSettings) showSettings();
            else showHome();
            checkPermissions();
        }
    }

    private void checkPermissions() {
        if (!hasUsageStatsPermission()) {
            new AlertDialog.Builder(this)
                .setTitle(R.string.usage_stats_title)
                .setMessage(R.string.usage_stats_desc)
                .setCancelable(false)
                .setPositiveButton(R.string.grant_access, (d, w) -> {
                    try {
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                    } catch (Exception e) {
                        Toast.makeText(this, "Fallo manualmente.", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
        }
        
        if (!isAccessibilityEnabled()) {
            new AlertDialog.Builder(this)
                .setTitle(R.string.perm_acc_title)
                .setMessage(R.string.perm_acc_desc)
                .setPositiveButton(R.string.btn_settings, (d, w) -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)))
                .show();
        }
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShowingSettings) {
            refreshSettings();
        } else {
            View hv = contentFrame.getChildAt(0);
            if (hv != null) refreshHome(hv);
        }
    }

    private void showHome() {
        isShowingSettings = false;
        updateNavUI();
        contentFrame.removeAllViews();
        View homeView = LayoutInflater.from(this).inflate(R.layout.fragment_home, contentFrame, false);
        contentFrame.addView(homeView);
        refreshHome(homeView);
    }

    private void showSettings() {
        isShowingSettings = true;
        updateNavUI();
        contentFrame.removeAllViews();
        View settingsView = LayoutInflater.from(this).inflate(R.layout.fragment_settings, contentFrame, false);
        contentFrame.addView(settingsView);
        refreshSettings(settingsView);
    }

    private void updateNavUI() {
        if (isShowingSettings) {
            navSettings.setTextColor(getColor(R.color.accent_cyan));
            navHome.setTextColor(getColor(R.color.nav_inactive));
        } else {
            navHome.setTextColor(getColor(R.color.accent_cyan));
            navSettings.setTextColor(getColor(R.color.nav_inactive));
        }
        navHome.setText(R.string.nav_home);
        navSettings.setText(R.string.nav_settings);
    }

    private void refreshHome(View v) {
        TextView kpiUnlocks = v.findViewById(R.id.kpi_unlocks);
        TextView kpiResisted = v.findViewById(R.id.kpi_resisted);
        TextView kpiUsage = v.findViewById(R.id.kpi_usage);
        TextView kpiProj = v.findViewById(R.id.kpi_projection);

        if (kpiUnlocks != null) {
            kpiUnlocks.setText(String.valueOf(pm.getUnlocksToday()));
            ((View)kpiUnlocks.getParent()).setOnClickListener(view -> 
                showInfoDialog(getString(R.string.unlocks), getString(R.string.desc_unlocks)));
        }
        if (kpiResisted != null) {
            kpiResisted.setText(String.valueOf(pm.getResistedToday()));
            ((View)kpiResisted.getParent()).setOnClickListener(view -> 
                showInfoDialog(getString(R.string.resisted), getString(R.string.desc_resisted)));
        }
        
        long todayMs = getSocialTimeSum(0);
        if (kpiUsage != null) {
            kpiUsage.setText(formatMillis(todayMs));
            ((View)kpiUsage.getParent()).setOnClickListener(view -> 
                showInfoDialog(getString(R.string.social_day), getString(R.string.desc_usage)));
        }
        
        if (kpiProj != null) {
            // User requested a real sum instead of a projection
            long annualMs = getSocialTimeSum(365);
            
            if (annualMs > 0) {
                long daysLost = annualMs / (1000L * 60 * 60 * 24);
                if (daysLost > 0) {
                    kpiProj.setText(daysLost + " " + getString(R.string.days_suffix));
                } else {
                    long hoursLost = annualMs / (1000L * 60 * 60);
                    kpiProj.setText(hoursLost + "h");
                }
            } else {
                kpiProj.setText("0 " + getString(R.string.days_suffix));
            }
            ((View)kpiProj.getParent()).setOnClickListener(view -> 
                showInfoDialog(getString(R.string.lost_year), getString(R.string.desc_usage_annual)));
        }

        LinearLayout appList = v.findViewById(R.id.app_list_home);
        if (appList == null) return;
        appList.removeAllViews();
        Set<String> blockedIds = pm.getBlockedIds();

        for (String[] app : QuizData.APPS) {
            if (!blockedIds.contains(app[0])) continue;
            
            LinearLayout row = createRow();
            TextView tv = new TextView(this);
            tv.setText(app[1]);
            tv.setTextColor(getColor(R.color.text_primary));
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            row.addView(tv);
            
            final String pkg = app[3];
            row.setOnClickListener(view -> {
                if (!pm.isUnlocked(pkg)) {
                    Intent intent = new Intent(this, QuizActivity.class);
                    intent.putExtra("INTERCEPTED_PACKAGE", pkg);
                    startActivity(intent);
                }
            });
            appList.addView(row);
        }
    }

    private void showInfoDialog(String title, String message) {
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.btn_ok, null)
            .show();
    }

    private void refreshSettings() {
        if (contentFrame.getChildCount() > 0) refreshSettings(contentFrame.getChildAt(0));
    }

    private void refreshSettings(View v) {
        TextView brutalMsg = v.findViewById(R.id.brutal_message);
        if (brutalMsg != null) {
            String[] messages = getResources().getStringArray(R.array.poe_messages);
            if (messages.length > 0) {
                brutalMsg.setText(messages[new Random().nextInt(messages.length)]);
            }
        }

        TextView valLang = v.findViewById(R.id.val_language);
        if (valLang != null) {
            String lang = pm.getLanguage();
            if (lang.equals("it")) valLang.setText("ITALIANO");
            else if (lang.equals("en")) valLang.setText("ENGLISH");
            else if (lang.equals("es")) valLang.setText("ESPAÑOL");
            else if (lang.equals("de")) valLang.setText("DEUTSCH");
        }

        View btnLang = v.findViewById(R.id.btn_language);
        if (btnLang != null) {
            btnLang.setOnClickListener(view -> {
                String l = pm.getLanguage();
                String next = l.equals("it") ? "en" : l.equals("en") ? "es" : l.equals("es") ? "de" : "it";
                pm.setLanguage(next);
                recreate();
            });
        }

        View btnManageApps = v.findViewById(R.id.btn_manage_apps);
        if (btnManageApps != null) {
            btnManageApps.setOnClickListener(view -> {
                String[] items = new String[QuizData.APPS.length];
                boolean[] checkedItems = new boolean[QuizData.APPS.length];
                Set<String> blockedIds = pm.getBlockedIds();
                for (int i = 0; i < QuizData.APPS.length; i++) {
                    items[i] = QuizData.APPS[i][1];
                    checkedItems[i] = blockedIds.contains(QuizData.APPS[i][0]);
                }
                new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.choose_apps_title))
                    .setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                        checkedItems[which] = isChecked;
                    })
                    .setPositiveButton("SALVA", (dialog, which) -> {
                        Set<String> newBlocked = new java.util.HashSet<>();
                        for (int i = 0; i < QuizData.APPS.length; i++) {
                            if (checkedItems[i]) {
                                newBlocked.add(QuizData.APPS[i][0]);
                            }
                        }
                        pm.setBlockedIds(newBlocked);
                        refreshSettings(v);
                    })
                    .show();
            });
        }

        LinearLayout appList = v.findViewById(R.id.app_list_settings);
        if (appList != null) {
            appList.removeAllViews();
            Set<String> blockedIds = pm.getBlockedIds();
            for (String[] app : QuizData.APPS) {
                String id = app[0];
                if (!blockedIds.contains(id)) continue;
                
                LinearLayout row = createRow();
                TextView tv = new TextView(this);
                tv.setText(app[1]);
                tv.setTextColor(getColor(R.color.text_primary));
                row.addView(tv, new LinearLayout.LayoutParams(0, -2, 1));
                
                View cb = new View(this);
                cb.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(20), dpToPx(20)));
                cb.setBackgroundColor(getColor(R.color.accent_cyan));
                row.addView(cb);
                
                row.setOnClickListener(view -> {
                    Intent intent = new Intent(this, QuizActivity.class);
                    intent.putExtra("INTERCEPTED_PACKAGE", id);
                    intent.putExtra("SETTINGS_UNLOCK", true);
                    resultLauncher.launch(intent);
                });
                appList.addView(row);
            }
        }

        TextView valTopic = v.findViewById(R.id.val_topic);
        TextView valDiff = v.findViewById(R.id.val_diff);
        TextView valDur = v.findViewById(R.id.val_duration);
        if (valTopic != null) valTopic.setText(pm.getTopic().toUpperCase());
        if (valDiff != null) valDiff.setText(pm.getDifficulty().toUpperCase());
        if (valDur != null) valDur.setText(pm.getDurationSeconds()/60 + " " + getString(R.string.minutes_suffix));

        View btnTopic = v.findViewById(R.id.btn_topic);
        if (btnTopic != null) {
            btnTopic.setOnClickListener(view -> {
                String[] t = QuizData.TOPICS; int i=0; for(;i<t.length;i++) if(t[i].equals(pm.getTopic())) break;
                pm.setTopic(t[(i+1)%t.length]); refreshSettings(v);
            });
        }
        View btnDiff = v.findViewById(R.id.btn_diff);
        if (btnDiff != null) {
            btnDiff.setOnClickListener(view -> {
                String[] d = QuizData.DIFFS; int i=0; for(;i<d.length;i++) if(d[i].equals(pm.getDifficulty())) break;
                pm.setDifficulty(d[(i+1)%d.length]); refreshSettings(v);
            });
        }
        View btnDur = v.findViewById(R.id.btn_duration);
        if (btnDur != null) {
            btnDur.setOnClickListener(view -> {
                int d = pm.getDurationSeconds();
                pm.setDurationSeconds(d >= 1200 ? 600 : d + 300);
                refreshSettings(v);
            });
        }

        v.findViewById(R.id.btn_donate).setOnClickListener(view -> 
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=ferralessandro11@gmail.com&currency_code=EUR&item_name=Donazione%20Pedro")))
        );
        View btnInstagram = v.findViewById(R.id.btn_instagram);
        if (btnInstagram != null) {
            btnInstagram.setOnClickListener(view -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/alessandroferrari"));
                    intent.setPackage("com.instagram.android");
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/alessandroferrari")));
                }
            });
        }
        v.findViewById(R.id.btn_accessibility).setOnClickListener(view -> 
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        );
        refreshAccessibilityUI(v);
    }

    private void refreshAccessibilityUI(View v) {
        TextView tvStatus = v.findViewById(R.id.val_accessibility_status);
        if (tvStatus == null) return;
        boolean enabled = isAccessibilityEnabled();
        tvStatus.setText(enabled ? R.string.connected : R.string.disconnected);
        tvStatus.setTextColor(enabled ? getColor(R.color.accent_green) : getColor(R.color.accent_red));
    }

    private long getSocialTimeSum(int days) {
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        if (usm == null) return 0;

        long end = System.currentTimeMillis();
        long installTime = 0;
        try {
            installTime = getPackageManager().getPackageInfo(getPackageName(), 0).firstInstallTime;
        } catch (Exception ignored) {}

        Calendar calendar = Calendar.getInstance();
        long start;
        
        if (days == 0) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            start = Math.max(calendar.getTimeInMillis(), installTime);
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, -days);
            start = Math.max(calendar.getTimeInMillis(), installTime);
        }
        
        long total = 0;
        
        // Per calcolare il tempo speso in modo infallibile ed accertarci di star analizzando ESCLUSIVAMENTE 
        // le app contrassegnate come in "stato di blocco", analizziamo tutti i counter scartando a priori ogni
        // applicazione che non figura nel set di isPackageBlocked.
        Map<String, UsageStats> aggregated = usm.queryAndAggregateUsageStats(start, end);
        if (aggregated != null) {
            for (Map.Entry<String, UsageStats> entry : aggregated.entrySet()) {
                String pkg = entry.getKey().toLowerCase().trim();
                // Verifichiamo strettamente che l'app sia segnata come attualmente bloccata
                if (pm.isPackageBlocked(pkg)) {
                    total += entry.getValue().getTotalTimeInForeground();
                }
            }
        }
        
        return total;
    }

    private String formatMillis(long ms) {
        if (ms <= 0) return "0m";
        long seconds = (ms / 1000) % 60;
        long minutes = (ms / (1000 * 60)) % 60;
        long hours = (ms / (1000 * 60 * 60));
        
        if (hours > 0) return hours + "h " + minutes + "m";
        return minutes + "m " + seconds + "s";
    }

    private boolean isAccessibilityEnabled() {
        try {
            int e = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            if (e == 1) {
                String ss = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                String t = getPackageName() + "/" + AppBlockService.class.getCanonicalName();
                if (ss != null) {
                    android.text.TextUtils.SimpleStringSplitter sp = new android.text.TextUtils.SimpleStringSplitter(':');
                    sp.setString(ss); while (sp.hasNext()) if (sp.next().equalsIgnoreCase(t)) return true;
                }
            }
        } catch (Exception ignored) { }
        return false;
    }

    private LinearLayout createRow() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setBackgroundResource(R.drawable.bg_glass_card);
        int p = dpToPx(16);
        row.setPadding(p, p, p, p);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.bottomMargin = dpToPx(12);
        row.setLayoutParams(lp);
        return row;
    }

    private int dpToPx(int dp) { return (int) (dp * getResources().getDisplayMetrics().density); }
}
