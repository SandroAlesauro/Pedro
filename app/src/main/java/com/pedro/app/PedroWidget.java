package com.pedro.app;

import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Map;

public class PedroWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PrefsManager pm = new PrefsManager(context);
        for (int widgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_pedro);

            // Calcola tempo social di oggi
            long todayMs = getTodaySocialTime(context, pm);
            views.setTextViewText(R.id.widget_usage, formatMillis(todayMs));

            // Focus mode
            if (pm.isFocusModeActive()) {
                long remainMs = pm.getFocusModeEnd() - System.currentTimeMillis();
                int remainMin = (int) (remainMs / 60000);
                views.setTextViewText(R.id.widget_focus, "🔒 FOCUS " + remainMin + "m");
                views.setViewVisibility(R.id.widget_focus, View.VISIBLE);
            } else {
                views.setViewVisibility(R.id.widget_focus, View.GONE);
            }

            // Click apre l'app
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.widget_usage, pendingIntent);

            appWidgetManager.updateAppWidget(widgetId, views);
        }
    }

    private long getTodaySocialTime(Context context, PrefsManager pm) {
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usm == null) return 0;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long totalMs = 0;
        Map<String, UsageStats> aggregated = usm.queryAndAggregateUsageStats(
            cal.getTimeInMillis(), System.currentTimeMillis());
        if (aggregated != null) {
            for (Map.Entry<String, UsageStats> entry : aggregated.entrySet()) {
                String pkg = entry.getKey().toLowerCase().trim();
                if (pm.isPackageBlocked(pkg)) {
                    totalMs += entry.getValue().getTotalTimeInForeground();
                }
            }
        }
        return totalMs;
    }

    private String formatMillis(long ms) {
        long totalMin = ms / 60000;
        if (totalMin < 60) return totalMin + "m";
        long hours = totalMin / 60;
        long mins = totalMin % 60;
        return hours + "h" + (mins > 0 ? mins + "m" : "");
    }
}
