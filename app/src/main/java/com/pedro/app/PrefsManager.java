package com.pedro.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Gestore centralizzato delle SharedPreferences.
 * Usato sia da MainActivity/QuizActivity che da AppBlockService.
 */
public class PrefsManager {

    private static final String PREFS = "QuizLockData";

    private final SharedPreferences prefs;

    public PrefsManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // ---- BLOCKED PACKAGES & IDS ----

    public Set<String> getBlockedIds() {
        return new HashSet<>(prefs.getStringSet("blocked_ids", new HashSet<>()));
    }

    public void setBlockedIds(Set<String> ids) {
        prefs.edit().putStringSet("blocked_ids", ids).apply();
    }

    public void addBlockedId(String id) {
        Set<String> ids = getBlockedIds();
        ids.add(id);
        setBlockedIds(ids);
    }

    public void removeBlockedId(String id) {
        Set<String> ids = getBlockedIds();
        ids.remove(id);
        setBlockedIds(ids);
    }

    public boolean isPackageBlocked(String pkg) {
        if (pkg == null) return false;
        
        // Prima controlliamo se il package è mappato a un ID conosciuto
        String id = QuizData.packageToId(pkg);
        if (id != null) {
            return getBlockedIds().contains(id);
        }
        
        // Fallback: controllo se il package è presente in un set di package personalizzati
        Set<String> customBlocked = prefs.getStringSet("blocked_packages", new HashSet<>());
        return customBlocked.contains(pkg);
    }

    public void addBlockedPackage(String pkg) {
        Set<String> packages = new HashSet<>(prefs.getStringSet("blocked_packages", new HashSet<>()));
        packages.add(pkg);
        prefs.edit().putStringSet("blocked_packages", packages).apply();
    }

    public void removeBlockedPackage(String pkg) {
        Set<String> packages = new HashSet<>(prefs.getStringSet("blocked_packages", new HashSet<>()));
        packages.remove(pkg);
        prefs.edit().putStringSet("blocked_packages", packages).apply();
    }

    // ---- TRACKED APPS (per menu selezione) ----
    
    public Set<String> getTrackedIds() {
        Set<String> tracked = prefs.getStringSet("tracked_ids", null);
        if (tracked == null) {
            // Default di partenza: le più comuni
            tracked = new HashSet<>();
            tracked.add("ig");
            tracked.add("tt");
            tracked.add("yt");
            tracked.add("x");
        }
        return new HashSet<>(tracked);
    }
    
    public void setTrackedIds(Set<String> ids) {
        prefs.edit().putStringSet("tracked_ids", ids).apply();
    }

    // ---- UNLOCK TIMERS ----

    public void setUnlockExpiry(String pkg, long expiryTimestamp) {
        prefs.edit().putLong("unlock_" + pkg, expiryTimestamp).apply();
    }

    public boolean isUnlocked(String pkg) {
        if (pkg == null) return false;
        long expiry = prefs.getLong("unlock_" + pkg, 0);
        return System.currentTimeMillis() < expiry;
    }

    public void clearAllUnlocks() {
        SharedPreferences.Editor editor = prefs.edit();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith("unlock_")) {
                editor.remove(key);
            }
        }
        editor.apply();
    }

    public long getRemainingSeconds(String pkg) {
        long expiry = prefs.getLong("unlock_" + pkg, 0);
        long diff = expiry - System.currentTimeMillis();
        return diff > 0 ? diff / 1000 : 0;
    }

    // ---- QUIZ CONFIG ----

    public String getTopic() {
        return prefs.getString("quiz_topic", "logic");
    }

    public void setTopic(String topic) {
        prefs.edit().putString("quiz_topic", topic).apply();
    }

    public String getDifficulty() {
        return prefs.getString("quiz_diff", "medio");
    }

    public void setDifficulty(String diff) {
        prefs.edit().putString("quiz_diff", diff).apply();
    }

    public int getDurationSeconds() {
        return prefs.getInt("quiz_duration", 600);
    }

    public void setDurationSeconds(int seconds) {
        prefs.edit().putInt("quiz_duration", seconds).apply();
    }

    // ---- LANGUAGE SETTINGS ----

    public String getLanguage() {
        return prefs.getString("app_language", "it");
    }

    public void setLanguage(String lang) {
        prefs.edit().putString("app_language", lang).apply();
    }

    // ---- COOLDOWN ----

    public void setCooldownEnd(long timestamp) {
        prefs.edit().putLong("cooldown_end", timestamp).apply();
    }

    public boolean isInCooldown() {
        return System.currentTimeMillis() < prefs.getLong("cooldown_end", 0);
    }

    // ---- STATS DASHBOARD ----
    
    public int getUnlocksToday() {
        String today = getTodayString();
        String savedDate = prefs.getString("unlocks_date", "");
        if (!today.equals(savedDate)) {
             prefs.edit().putInt("unlocks_count", 0).putString("unlocks_date", today).apply();
             return 0;
        }
        return prefs.getInt("unlocks_count", 0);
    }
    
    public void incrementUnlocksToday() {
        int current = getUnlocksToday();
        prefs.edit().putInt("unlocks_count", current + 1).apply();
    }

    public int getResistedToday() {
        String today = getTodayString();
        String savedDate = prefs.getString("resisted_date", "");
        if (!today.equals(savedDate)) {
             prefs.edit().putInt("resisted_count", 0).putString("resisted_date", today).apply();
             return 0;
        }
        return prefs.getInt("resisted_count", 0);
    }
    
    public void incrementResistedToday() {
        int current = getResistedToday();
        prefs.edit().putInt("resisted_count", current + 1).apply();
    }

    // ---- POE TRACKING (MESSAGGIO GIORNALIERO) ----
    
    public boolean hasShownPoeToday() {
        String today = getTodayString();
        return today.equals(prefs.getString("last_poe_date", ""));
    }

    public void markPoeAsShownToday() {
        String today = getTodayString();
        prefs.edit().putString("last_poe_date", today).apply();
    }

    // ---- FIRST LAUNCH ----
    
    public boolean isFirstLaunch() {
        return prefs.getBoolean("is_first_launch", true);
    }
    
    public void setFirstLaunchDone() {
        prefs.edit().putBoolean("is_first_launch", false).apply();
    }

    // ---- DAILY LIMIT ----

    public int getDailyLimitMinutes() {
        return prefs.getInt("daily_limit_minutes", 0);
    }

    public void setDailyLimitMinutes(int minutes) {
        prefs.edit().putInt("daily_limit_minutes", minutes).apply();
    }

    // ---- SCHEDULED BLOCK ----

    public boolean isScheduledBlockEnabled() {
        return prefs.getBoolean("scheduled_block_enabled", false);
    }

    public void setScheduledBlockEnabled(boolean enabled) {
        prefs.edit().putBoolean("scheduled_block_enabled", enabled).apply();
    }

    public int getBlockStartHour() {
        return prefs.getInt("block_start_hour", 0);
    }

    public void setBlockStartHour(int hour) {
        prefs.edit().putInt("block_start_hour", hour).apply();
    }

    public int getBlockEndHour() {
        return prefs.getInt("block_end_hour", 7);
    }

    public void setBlockEndHour(int hour) {
        prefs.edit().putInt("block_end_hour", hour).apply();
    }

    // ---- NOTIFICATIONS ----

    public boolean isNotificationEnabled() {
        return prefs.getBoolean("notification_enabled", false);
    }

    public void setNotificationEnabled(boolean enabled) {
        prefs.edit().putBoolean("notification_enabled", enabled).apply();
    }

    private String getTodayString() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());
    }

    // ---- FOCUS MODE (POMODORO) ----

    public long getFocusModeEnd() {
        return prefs.getLong("focus_mode_end", 0);
    }

    public void setFocusModeEnd(long timestamp) {
        prefs.edit().putLong("focus_mode_end", timestamp).apply();
    }

    public boolean isFocusModeActive() {
        return System.currentTimeMillis() < getFocusModeEnd();
    }

    public int getFocusDurationMinutes() {
        return prefs.getInt("focus_duration", 25);
    }

    public void setFocusDurationMinutes(int minutes) {
        prefs.edit().putInt("focus_duration", minutes).apply();
    }

    // ---- PER-APP LIMITS ----

    public int getAppLimitMinutes(String appId) {
        return prefs.getInt("app_limit_" + appId, 0);
    }

    public void setAppLimitMinutes(String appId, int minutes) {
        prefs.edit().putInt("app_limit_" + appId, minutes).apply();
    }

    // ---- PROGRESSIVE DIFFICULTY ----

    public int getQuizCorrectCount() {
        return prefs.getInt("quiz_correct_count", 0);
    }

    public int getQuizTotalCount() {
        return prefs.getInt("quiz_total_count", 0);
    }

    public void recordQuizResult(boolean correct) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("quiz_total_count", getQuizTotalCount() + 1);
        if (correct) editor.putInt("quiz_correct_count", getQuizCorrectCount() + 1);
        editor.apply();
        autoAdjustDifficulty();
    }

    private void autoAdjustDifficulty() {
        int total = getQuizTotalCount();
        if (total < 10) return; // Attendi almeno 10 quiz per regolare
        int correct = getQuizCorrectCount();
        float rate = (float) correct / total;
        if (rate >= 0.85f && getDifficulty().equals("medio")) {
            setDifficulty("difficile");
            // Reset contatori per nuovo ciclo
            prefs.edit().putInt("quiz_correct_count", 0).putInt("quiz_total_count", 0).apply();
        } else if (rate < 0.40f && getDifficulty().equals("difficile")) {
            setDifficulty("medio");
            prefs.edit().putInt("quiz_correct_count", 0).putInt("quiz_total_count", 0).apply();
        }
    }
}
