package com.quizlock.app;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

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

        // Throttling per evitare loop infiniti o rallentamenti
        long now = System.currentTimeMillis();
        if (pkg.equals(lastPackage) && 
            eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            (now - lastCheckTime < 500)) {
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

        // Se l'app è sbloccata temporaneamente, ignora (controllo il rootPkg)
        if (pm.isUnlocked(basePkg)) return;

        // Blocca l'app lanciando il Quiz
        Log.i(TAG, "Intercettato avvio app bloccata: " + basePkg);
        
        Intent intent = new Intent(this, QuizActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
                        Intent.FLAG_ACTIVITY_CLEAR_TOP | 
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("INTERCEPTED_PACKAGE", basePkg); // Passo sempre il package radice
        startActivity(intent);
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
