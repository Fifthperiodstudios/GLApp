package com.fifthperiodstudios.glapp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class BackgroundService extends Service {
//    @android.annotation.Nullable
    public static final String SHAREDPREFERENCES_NAME = "com.fifthperiodstudios.glapp";
    public static final String SHAREDPREFERENCES_BENACHRICHTIGUNGEN = "benachrichtigungen";
    public static final String SHAREDPREFERENCES_AKTUALISIEREN = "autoAktualiseren";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_NAME,0);

        if (sharedPreferences.getBoolean(SHAREDPREFERENCES_AKTUALISIEREN,true)) {
            /*Hier den WiFi Manager benutzen um titel vertretung Fach und co zu erhalten
             * */
            boolean esGibtNeueVertretung = false;//Nur provisorisch, dieser boolean muss durch den Wifi manager gesetzt werden
//          ^wichtig^   ^wichtig^   ^wichtig^   ^wichtig^

            String title = "Titel";
            String textKurz = "Diese Nachricht wird vom Background service geschreiben";
            String textLang = null;

            if (sharedPreferences.getBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN,true)&&esGibtNeueVertretung) {
                startService(new Intent(this, NotificationService.class)
                        .putExtra("title", title)
                        .putExtra("textKurz", textKurz)
                        .putExtra("textLang", textLang)
                        .putExtra("vertretung",esGibtNeueVertretung));
            }
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }
}