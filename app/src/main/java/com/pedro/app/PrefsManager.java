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

    // ---- BLOCKED PACKAGES ----

    public Set<String> getBlockedPackages() {
        return new HashSet<>(prefs.getStringSet("blocked_packages", new HashSet<>()));
    }

    public void setBlockedPackages(Set<String> packages) {
        prefs.edit().putStringSet("blocked_packages", packages).apply();
    }

    public boolean isPackageBlocked(String pkg) {
        if (pkg == null) return false;
        Set<String> blocked = getBlockedPackages();
        // Controllo esatto e parziale per sicurezza
        if (blocked.contains(pkg)) return true;
        for (String b : blocked) {
            if (pkg.contains(b) || b.contains(pkg)) return true;
        }
        return false;
    }

    public void addBlockedPackage(String pkg) {
        Set<String> set = getBlockedPackages();
        set.add(pkg);
        setBlockedPackages(set);
    }

    public void removeBlockedPackage(String pkg) {
        Set<String> set = getBlockedPackages();
        set.remove(pkg);
        setBlockedPackages(set);
    }

    // ---- BLOCKED APP IDS (per la UI) ----

    public Set<String> getBlockedIds() {
        return new HashSet<>(prefs.getStringSet("blocked_ids", new HashSet<>()));
    }

    public void setBlockedIds(Set<String> ids) {
        prefs.edit().putStringSet("blocked_ids", ids).apply();
        // Sincronizza anche i package name
        Set<String> packages = new HashSet<>();
        for (String id : ids) {
            String pkg = QuizData.idToPackage(id);
            if (pkg != null) packages.add(pkg);
        }
        setBlockedPackages(packages);
    }

    public boolean isIdBlocked(String id) {
        return getBlockedIds().contains(id);
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
    
    // ---- TRACKED APPS (per menu selezione) ----
    
    public Set<String> getTrackedIds() {
        Set<String> tracked = prefs.getStringSet("tracked_ids", null);
        if (tracked == null) {
            // Default di partenza: solo le più letali.
            tracked = new HashSet<>();
            tracked.add("ig");
            tracked.add("tt");
            tracked.add("yt");
        }
        return new HashSet<>(tracked);
    }
    
    public void setTrackedIds(Set<String> ids) {
        prefs.edit().putStringSet("tracked_ids", ids).apply();
    }

    // ---- UNLOCK TIMERS ----

    public long getUnlockExpiry(String pkg) {
        return prefs.getLong("unlock_" + pkg, 0);
    }

    public void setUnlockExpiry(String pkg, long expiryTimestamp) {
        prefs.edit().putLong("unlock_" + pkg, expiryTimestamp).apply();
    }

    public boolean isUnlocked(String pkg) {
        if (pkg == null) return false;
        long expiry = getUnlockExpiry(pkg);
        boolean unlocked = System.currentTimeMillis() < expiry;
        return unlocked;
    }

    public void clearAllUnlocks() {
        SharedPreferences.Editor editor = prefs.edit();
        for (String key : prefs.getAll().keySet()) {
            if (key.startsWith("unlock_")) {
                editor.remove(key);
            }
        }
        editor.commit();
    }

    public long getRemainingSeconds(String pkg) {
        long expiry = getUnlockExpiry(pkg);
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

    public int getGoals() {
        return prefs.getInt("quiz_goals", 3);
    }

    public void setGoals(int goals) {
        prefs.edit().putInt("quiz_goals", goals).apply();
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

    public long getCooldownEnd() {
        return prefs.getLong("cooldown_end", 0);
    }

    public void setCooldownEnd(long timestamp) {
        prefs.edit().putLong("cooldown_end", timestamp).apply();
    }

    public boolean isInCooldown() {
        return System.currentTimeMillis() < getCooldownEnd();
    }

    public long getCooldownRemainingSeconds() {
        long diff = getCooldownEnd() - System.currentTimeMillis();
        return diff > 0 ? diff / 1000 : 0;
    }

    // ---- STATS DASHBOARD ----
    
    public int getUnlocksToday() {
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());
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

    // ---- FIRST LAUNCH & RESISTED ----
    
    public boolean isFirstLaunch() {
        return prefs.getBoolean("is_first_launch", true);
    }
    
    public void setFirstLaunchDone() {
        prefs.edit().putBoolean("is_first_launch", false).apply();
    }
    
    public int getResistedToday() {
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());
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
}
