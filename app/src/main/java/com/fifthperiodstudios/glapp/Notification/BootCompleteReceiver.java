package com.fifthperiodstudios.glapp.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fifthperiodstudios.glapp.Notification.BackgroundService;

import java.util.Calendar;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            final long MINUTEN = 180;//in welchem Abstand soll der Hintergrund dienst arbeiten

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            Intent startBackgroundserviceIntent = new Intent(context, BackgroundService.class);
            PendingIntent startBackgroundservicePendingIntent = PendingIntent.getService(context, 0, startBackgroundserviceIntent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * MINUTEN);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * MINUTEN, startBackgroundservicePendingIntent);

        }
    }
}