package com.pedro.app;

import android.accessibilityservice.AccessibilityService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;

/**
 * Servizio di accessibilità che monitora l'apertura delle app.
 * Se un'app è nella lista bloccata e NON è temporaneamente sbloccata,
 * lancia QuizActivity per forzare il quiz.
 */
public class AppBlockService extends AccessibilityService {

    private static final String TAG = "QuizLock";
    private String lastPackage = "";
    private long lastCheckTime = 0;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null) return;

        String pkg = event.getPackageName() != null ? event.getPackageName().toString() : null;
        if (pkg == null || pkg.isEmpty()) return;

        int eventType = event.getEventType();
        
        // Accetta SOLO eventi di cambio finestra (quando un'app viene messa in primo piano)
        // Ignora qualsiasi evento in background come notifiche, toast, ecc.
        if (eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            return;
        }

        // Throttling per evitare loop infiniti
        long now = System.currentTimeMillis();
        if (pkg.equals(lastPackage) && (now - lastCheckTime < 500)) {
            return;
        }
        
        lastPackage = pkg;
        lastCheckTime = now;

        // Non bloccare la nostra app o il sistema
        if (pkg.equals(getPackageName()) || isSystemPackage(pkg)) return;

        PrefsManager pm = new PrefsManager(getApplicationContext());

        // Se l'app non è bloccata, ignora
        if (!pm.isPackageBlocked(pkg)) return;

        // Troviamo il pacchetto "radice" reale per gestire sblocchi coerenti
        String basePkg = pkg;
        String mappedId = QuizData.packageToId(pkg);
        if (mappedId != null) {
            String rootPkg = QuizData.idToPackage(mappedId);
            if (rootPkg != null) {
                basePkg = rootPkg;
            }
        }

        // Controlla Focus Mode (priorità MASSIMA — blocco assoluto)
        if (pm.isFocusModeActive()) {
            Log.i(TAG, "Focus Mode attivo — blocco: " + basePkg);
            Toast.makeText(getApplicationContext(), R.string.focus_mode_active_toast, Toast.LENGTH_SHORT).show();
            performGlobalAction(GLOBAL_ACTION_HOME);
            return;
        }

        // Controlla blocco programmato (ha priorità su tutto, anche gli sblocchi)
        if (pm.isScheduledBlockEnabled() && isInScheduledBlock(pm)) {
            Log.i(TAG, "Blocco programmato attivo per: " + basePkg);
            Toast.makeText(getApplicationContext(), R.string.scheduled_block_toast, Toast.LENGTH_SHORT).show();
            performGlobalAction(GLOBAL_ACTION_HOME);
            return;
        }

        // Se l'app è sbloccata temporaneamente, ignora (controllo il rootPkg)
        if (pm.isUnlocked(basePkg)) return;

        // Controlla limite giornaliero globale (blocca senza quiz se superato)
        if (isDailyLimitExceeded(pm)) {
            Log.i(TAG, "Limite giornaliero superato per: " + basePkg);
            Toast.makeText(getApplicationContext(), R.string.daily_limit_exceeded, Toast.LENGTH_SHORT).show();
            performGlobalAction(GLOBAL_ACTION_HOME);
            return;
        }

        // Controlla limite per-app (blocca senza quiz se superato)
        if (mappedId != null && isAppLimitExceeded(pm, mappedId, basePkg)) {
            Log.i(TAG, "Limite per-app superato per: " + basePkg);
            Toast.makeText(getApplicationContext(), R.string.app_limit_exceeded_toast, Toast.LENGTH_SHORT).show();
            performGlobalAction(GLOBAL_ACTION_HOME);
            return;
        }

        // Blocca l'app lanciando il Quiz
        Log.i(TAG, "Intercettato avvio app bloccata: " + basePkg);
        
        Intent intent = new Intent(this, QuizActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | 
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("INTERCEPTED_PACKAGE", basePkg); // Passo sempre il package radice
        startActivity(intent);
    }

    private boolean isInScheduledBlock(PrefsManager pm) {
        int nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int start = pm.getBlockStartHour();
        int end = pm.getBlockEndHour();
        if (start == end) return false;
        if (start < end) return nowHour >= start && nowHour < end;
        return nowHour >= start || nowHour < end; // Overnight (es. 22-07)
    }

    private boolean isDailyLimitExceeded(PrefsManager pm) {
        int limitMin = pm.getDailyLimitMinutes();
        if (limitMin <= 0) return false;

        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        if (usm == null) return false;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        long end = System.currentTimeMillis();

        long totalMs = 0;
        Map<String, UsageStats> aggregated = usm.queryAndAggregateUsageStats(start, end);
        if (aggregated != null) {
            for (Map.Entry<String, UsageStats> entry : aggregated.entrySet()) {
                String pkg = entry.getKey().toLowerCase().trim();
                if (pm.isPackageBlocked(pkg)) {
                    totalMs += entry.getValue().getTotalTimeInForeground();
                }
            }
        }
        return totalMs >= (limitMin * 60L * 1000L);
    }

    private boolean isAppLimitExceeded(PrefsManager pm, String appId, String pkg) {
        int limitMin = pm.getAppLimitMinutes(appId);
        if (limitMin <= 0) return false;

        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        if (usm == null) return false;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Map<String, UsageStats> aggregated = usm.queryAndAggregateUsageStats(
            cal.getTimeInMillis(), System.currentTimeMillis());
        if (aggregated == null) return false;

        long appMs = 0;
        for (Map.Entry<String, UsageStats> entry : aggregated.entrySet()) {
            String entryPkg = entry.getKey().toLowerCase().trim();
            if (entryPkg.equals(pkg) || entryPkg.startsWith(pkg + ".")) {
                appMs += entry.getValue().getTotalTimeInForeground();
            }
        }
        return appMs >= (limitMin * 60L * 1000L);
    }

    private boolean isSystemPackage(String pkg) {
        return pkg.equals("android") ||
               pkg.startsWith("com.android.") || 
               pkg.startsWith("com.sec.") ||
               pkg.startsWith("com.miui.") || 
               pkg.startsWith("com.huawei.") ||
               pkg.startsWith("com.samsung.") || 
               pkg.startsWith("com.google.android.inputmethod") ||
               pkg.equals("com.google.android.apps.nexuslauncher") ||
               pkg.equals("com.google.android.gms") ||
               pkg.contains("launcher") || 
               pkg.contains("trebuchet");
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "Service Interrupted");
    }
}
