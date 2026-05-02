package com.pedro.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Riceve l'evento BOOT_COMPLETED per riprogrammare la notifica serale
 * dopo un riavvio del dispositivo.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            PrefsManager pm = new PrefsManager(context);
            if (pm.isNotificationEnabled()) {
                DailySummaryReceiver.scheduleDaily(context);
            }
        }
    }
}
