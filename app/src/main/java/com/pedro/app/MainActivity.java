package com.pedro.app;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private final Handler handler = new Handler(Looper.getMainLooper());

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

        // Grafico settimanale
        WeeklyChartView chartView = v.findViewById(R.id.weekly_chart);
        if (chartView != null) {
            chartView.setData(getWeeklyUsageData(), getWeekDayLabels());
        }

        // Indicatore limite giornaliero
        LinearLayout limitContainer = v.findViewById(R.id.daily_limit_container);
        if (limitContainer != null) {
            int limitMin = pm.getDailyLimitMinutes();
            if (limitMin > 0) {
                limitContainer.setVisibility(View.VISIBLE);
                long usedMin = todayMs / (1000 * 60);

                TextView limitText = v.findViewById(R.id.daily_limit_text);
                if (limitText != null) {
                    limitText.setText(usedMin + "m / " + limitMin + "m");
                }

                View limitBar = v.findViewById(R.id.daily_limit_bar);
                if (limitBar != null) {
                    float ratio = Math.min((float) usedMin / limitMin, 1.0f);
                    limitBar.post(() -> {
                        ViewGroup parent = (ViewGroup) limitBar.getParent();
                        if (parent != null) {
                            ViewGroup.LayoutParams lp = limitBar.getLayoutParams();
                            lp.width = (int) (parent.getWidth() * ratio);
                            limitBar.setLayoutParams(lp);
                        }
                    });
                }
            } else {
                limitContainer.setVisibility(View.GONE);
            }
        }

        // Focus Mode
        TextView focusTimerText = v.findViewById(R.id.focus_timer_text);
        View btnFocusStart = v.findViewById(R.id.btn_focus_start);
        View btnFocusStop = v.findViewById(R.id.btn_focus_stop);
        if (btnFocusStart != null && btnFocusStop != null && focusTimerText != null) {
            if (pm.isFocusModeActive()) {
                btnFocusStart.setVisibility(View.GONE);
                btnFocusStop.setVisibility(View.VISIBLE);
                focusTimerText.setVisibility(View.VISIBLE);
                updateFocusTimer(v);
            } else {
                int dur = pm.getFocusDurationMinutes();
                ((TextView) btnFocusStart).setText(String.format(getString(R.string.focus_start), dur));
                btnFocusStart.setVisibility(View.VISIBLE);
                btnFocusStop.setVisibility(View.GONE);
                focusTimerText.setVisibility(View.GONE);
            }
            btnFocusStart.setOnClickListener(view -> {
                int dur = pm.getFocusDurationMinutes();
                pm.setFocusModeEnd(System.currentTimeMillis() + (dur * 60L * 1000L));
                refreshHome(v);
            });
            btnFocusStop.setOnClickListener(view -> {
                pm.setFocusModeEnd(0);
                Toast.makeText(this, R.string.focus_ended, Toast.LENGTH_SHORT).show();
                refreshHome(v);
            });
        }

        // Weekly Insight
        TextView insightView = v.findViewById(R.id.weekly_insight);
        if (insightView != null) {
            long[] weekData = getWeeklyUsageData();
            long thisWeek = 0, lastWeek = 0;
            for (int i = 4; i < 7; i++) thisWeek += weekData[i];
            for (int i = 0; i < 4; i++) lastWeek += weekData[i];
            // Normalize: compare 3 days vs 4 days
            if (lastWeek > 0 && thisWeek > 0) {
                float thisAvg = thisWeek / 3f;
                float lastAvg = lastWeek / 4f;
                int pctChange = (int) (((thisAvg - lastAvg) / lastAvg) * 100);
                if (pctChange < -5) {
                    insightView.setText(String.format(getString(R.string.insight_less), Math.abs(pctChange)));
                } else if (pctChange > 5) {
                    insightView.setText(String.format(getString(R.string.insight_more), pctChange));
                } else {
                    insightView.setText(R.string.insight_same);
                }
                insightView.setVisibility(View.VISIBLE);
            } else {
                insightView.setText(R.string.insight_no_data);
                insightView.setVisibility(View.VISIBLE);
            }
        }

        // Share Stats
        View btnShare = v.findViewById(R.id.btn_share_stats);
        if (btnShare != null) {
            final long usageMs = todayMs;
            btnShare.setOnClickListener(view -> {
                String text = String.format(getString(R.string.share_text),
                    formatMillis(usageMs), pm.getUnlocksToday(), pm.getResistedToday());
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(shareIntent, "Pedro"));
            });
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

        // Limite giornaliero
        TextView valLimit = v.findViewById(R.id.val_daily_limit);
        if (valLimit != null) {
            int limitMin = pm.getDailyLimitMinutes();
            valLimit.setText(limitMin > 0 ? limitMin + " " + getString(R.string.minutes_suffix) : getString(R.string.off));
        }
        View btnLimit = v.findViewById(R.id.btn_daily_limit);
        if (btnLimit != null) {
            btnLimit.setOnClickListener(view -> {
                int[] options = {0, 15, 30, 45, 60, 90, 120};
                int current = pm.getDailyLimitMinutes();
                int idx = 0;
                for (int i = 0; i < options.length; i++) {
                    if (options[i] == current) { idx = i; break; }
                }
                pm.setDailyLimitMinutes(options[(idx + 1) % options.length]);
                refreshSettings(v);
            });
        }

        // Focus Duration
        TextView valFocusDur = v.findViewById(R.id.val_focus_duration);
        if (valFocusDur != null) {
            valFocusDur.setText(pm.getFocusDurationMinutes() + " MIN");
        }
        View btnFocusDur = v.findViewById(R.id.btn_focus_duration);
        if (btnFocusDur != null) {
            btnFocusDur.setOnClickListener(view -> {
                int[] opts = {15, 25, 30, 45, 60, 90, 120};
                int current = pm.getFocusDurationMinutes();
                int idx2 = 0;
                for (int i = 0; i < opts.length; i++) {
                    if (opts[i] == current) { idx2 = i; break; }
                }
                pm.setFocusDurationMinutes(opts[(idx2 + 1) % opts.length]);
                refreshSettings(v);
            });
        }

        // Per-App Limits
        LinearLayout perAppContainer = v.findViewById(R.id.per_app_limits_container);
        if (perAppContainer != null) {
            perAppContainer.removeAllViews();
            Set<String> blocked = pm.getBlockedIds();
            boolean first = true;
            for (String[] app : QuizData.APPS) {
                if (!blocked.contains(app[0])) continue;
                if (!first) {
                    View div = new View(this);
                    div.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    div.setBackgroundColor(0x15FFFFFF);
                    perAppContainer.addView(div);
                }
                first = false;
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setPadding(dp(16), dp(14), dp(16), dp(14));

                TextView name = new TextView(this);
                name.setText(app[1]);
                name.setTextColor(getColor(R.color.text_primary));
                name.setTextSize(12);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                name.setLayoutParams(lp);
                row.addView(name);

                TextView val = new TextView(this);
                int limitMin = pm.getAppLimitMinutes(app[0]);
                val.setText(limitMin > 0 ? limitMin + " MIN" : getString(R.string.off));
                val.setTextColor(getColor(R.color.accent_cyan));
                val.setTextSize(11);
                row.addView(val);

                final String appId = app[0];
                row.setOnClickListener(view -> {
                    int[] limits = {0, 15, 30, 45, 60, 90, 120};
                    int cur = pm.getAppLimitMinutes(appId);
                    int ci = 0;
                    for (int i = 0; i < limits.length; i++) {
                        if (limits[i] == cur) { ci = i; break; }
                    }
                    pm.setAppLimitMinutes(appId, limits[(ci + 1) % limits.length]);
                    refreshSettings(v);
                });
                perAppContainer.addView(row);
            }
        }

        // Blocco programmato
        TextView valToggle = v.findViewById(R.id.val_schedule_toggle);
        TextView valStartH = v.findViewById(R.id.val_schedule_start);
        TextView valEndH = v.findViewById(R.id.val_schedule_end);
        if (valToggle != null) valToggle.setText(pm.isScheduledBlockEnabled() ? getString(R.string.on) : getString(R.string.off));
        if (valStartH != null) valStartH.setText(String.format("%02d:00", pm.getBlockStartHour()));
        if (valEndH != null) valEndH.setText(String.format("%02d:00", pm.getBlockEndHour()));

        View btnToggleSched = v.findViewById(R.id.btn_schedule_toggle);
        if (btnToggleSched != null) {
            btnToggleSched.setOnClickListener(view -> {
                pm.setScheduledBlockEnabled(!pm.isScheduledBlockEnabled());
                refreshSettings(v);
            });
        }
        View btnSchedStart = v.findViewById(R.id.btn_schedule_start);
        if (btnSchedStart != null) {
            btnSchedStart.setOnClickListener(view -> showHourPicker(pm.getBlockStartHour(), hour -> {
                pm.setBlockStartHour(hour);
                refreshSettings(v);
            }));
        }
        View btnSchedEnd = v.findViewById(R.id.btn_schedule_end);
        if (btnSchedEnd != null) {
            btnSchedEnd.setOnClickListener(view -> showHourPicker(pm.getBlockEndHour(), hour -> {
                pm.setBlockEndHour(hour);
                refreshSettings(v);
            }));
        }

        // Notifica serale
        TextView valNotif = v.findViewById(R.id.val_notification);
        if (valNotif != null) {
            valNotif.setText(pm.isNotificationEnabled() ? getString(R.string.on) : getString(R.string.off));
        }
        View btnNotif = v.findViewById(R.id.btn_notification_toggle);
        if (btnNotif != null) {
            btnNotif.setOnClickListener(view -> {
                boolean newState = !pm.isNotificationEnabled();
                if (newState && Build.VERSION.SDK_INT >= 33) {
                    if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
                        return;
                    }
                }
                pm.setNotificationEnabled(newState);
                if (newState) DailySummaryReceiver.scheduleDaily(this);
                else DailySummaryReceiver.cancelDaily(this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pm.setNotificationEnabled(true);
            DailySummaryReceiver.scheduleDaily(this);
            if (isShowingSettings) refreshSettings();
        }
    }

    private long[] getWeeklyUsageData() {
        long[] result = new long[7];
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        if (usm == null) return result;

        for (int i = 0; i < 7; i++) {
            Calendar dayStart = Calendar.getInstance();
            dayStart.add(Calendar.DAY_OF_YEAR, -(6 - i));
            dayStart.set(Calendar.HOUR_OF_DAY, 0);
            dayStart.set(Calendar.MINUTE, 0);
            dayStart.set(Calendar.SECOND, 0);
            dayStart.set(Calendar.MILLISECOND, 0);

            long endMs = (i == 6) ? System.currentTimeMillis() :
                dayStart.getTimeInMillis() + 86400000L;

            Map<String, UsageStats> aggregated = usm.queryAndAggregateUsageStats(
                dayStart.getTimeInMillis(), endMs);
            if (aggregated != null) {
                for (Map.Entry<String, UsageStats> entry : aggregated.entrySet()) {
                    String pkg = entry.getKey().toLowerCase().trim();
                    if (pm.isPackageBlocked(pkg)) {
                        result[i] += entry.getValue().getTotalTimeInForeground();
                    }
                }
            }
        }
        return result;
    }

    private String[] getWeekDayLabels() {
        String[] labels = new String[7];
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE", java.util.Locale.getDefault());
        for (int i = 0; i < 7; i++) {
            Calendar day = Calendar.getInstance();
            day.add(Calendar.DAY_OF_YEAR, -(6 - i));
            String label = sdf.format(day.getTime()).toUpperCase();
            labels[i] = label.length() >= 2 ? label.substring(0, 2) : label;
        }
        return labels;
    }

    private interface HourCallback {
        void onHourSelected(int hour);
    }

    private void showHourPicker(int currentHour, HourCallback callback) {
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d:00", i);
        }
        new AlertDialog.Builder(this)
            .setTitle(R.string.schedule_select_hour)
            .setItems(hours, (d, which) -> callback.onHourSelected(which))
            .show();
    }

    private void updateFocusTimer(View homeView) {
        handler.removeCallbacksAndMessages(null);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!pm.isFocusModeActive()) {
                    Toast.makeText(MainActivity.this, R.string.focus_ended, Toast.LENGTH_SHORT).show();
                    if (!isShowingSettings) refreshHome(homeView);
                    return;
                }
                long remainMs = pm.getFocusModeEnd() - System.currentTimeMillis();
                int min = (int) (remainMs / 60000);
                int sec = (int) ((remainMs % 60000) / 1000);
                TextView timer = homeView.findViewById(R.id.focus_timer_text);
                if (timer != null) timer.setText(String.format("%02d:%02d", min, sec));
                handler.postDelayed(this, 1000);
            }
        });
    }

    private int dp(int val) {
        return (int) (val * getResources().getDisplayMetrics().density);
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
