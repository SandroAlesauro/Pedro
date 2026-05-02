package com.pedro.app;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Map;

/**
 * BroadcastReceiver che invia una notifica riassuntiva serale alle 21:00.
 * Mostra il tempo speso sui social, gli sblocchi e le rinunce del giorno.
 */
public class DailySummaryReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "pedro_daily_summary";
    private static final int NOTIFICATION_ID = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
        PrefsManager pm = new PrefsManager(context);
        if (!pm.isNotificationEnabled()) return;

        Context localizedCtx = LocaleHelper.updateResources(context, pm.getLanguage());

        long todayMs = getSocialTimeToday(context, pm);
        int unlocks = pm.getUnlocksToday();
        int resisted = pm.getResistedToday();

        createNotificationChannel(localizedCtx);

        String title = localizedCtx.getString(R.string.notif_title);
        String usageText = formatMillis(todayMs);
        String body = localizedCtx.getString(R.string.notif_body, usageText, unlocks, resisted);

        // Intent per aprire l'app al tap sulla notifica
        Intent tapIntent = new Intent(context, PoeActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(context, 0, tapIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pi)
                .setAutoCancel(true);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(NOTIFICATION_ID, builder.build());
        }

        // Riprogramma per domani
        scheduleDaily(context);
    }

    /**
     * Calcola il tempo totale speso oggi sulle app bloccate.
     */
    private long getSocialTimeToday(Context context, PrefsManager pm) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usm == null) return 0;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long start = cal.getTimeInMillis();
        long end = System.currentTimeMillis();

        long total = 0;
        Map<String, UsageStats> aggregated = usm.queryAndAggregateUsageStats(start, end);
        if (aggregated != null) {
            for (Map.Entry<String, UsageStats> entry : aggregated.entrySet()) {
                String pkg = entry.getKey().toLowerCase().trim();
                if (pm.isPackageBlocked(pkg)) {
                    total += entry.getValue().getTotalTimeInForeground();
                }
            }
        }
        return total;
    }

    private String formatMillis(long ms) {
        if (ms <= 0) return "0m";
        long minutes = (ms / (1000 * 60)) % 60;
        long hours = ms / (1000 * 60 * 60);
        if (hours > 0) return hours + "h " + minutes + "m";
        return minutes + "m";
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.notif_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.notif_channel_desc));
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }

    /**
     * Programma la notifica giornaliera alle 21:00.
     * Se le 21:00 sono già passate oggi, programma per domani.
     */
    public static void scheduleDaily(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent intent = new Intent(context, DailySummaryReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 21);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }

    /**
     * Cancella la notifica programmata.
     */
    public static void cancelDaily(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;

        Intent intent = new Intent(context, DailySummaryReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        am.cancel(pi);
    }
}
