package com.fifthperiodstudios.glapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class CreateNotificationChannels extends Application {

    public static final String CHANNEL_ID1 = "channel1";
    public static final String CHANNEL_ID2 = "channel2";

    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_ID1,
                    "Vertretungs_Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Dieser Channel ist für die Vertretung");//hier müsste die Beschreibung des Channels rein

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_ID2,
                    "News_Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("Dieser Channel ist für die neusten Meldungen");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }

}
