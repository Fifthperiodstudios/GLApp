package com.fifthperiodstudios.glapp.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.fifthperiodstudios.glapp.Login.MainActivity;
import com.fifthperiodstudios.glapp.R;

public class NotificationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String title = intent.getExtras().getString("title","Fehler beim Auslesen des Titels");
        String textKurz = intent.getExtras().getString("textKurz","Fehler beim Auslesen des kurzen Textes kk");
        String textLang = intent.getExtras().getString("textLang",null);
        boolean vertretung = intent.getExtras().getBoolean("vertretung",false);
        int notificationId;

        Intent notInt = new Intent(this,MainActivity.class);//hier kann man auch extras anhägen!
        PendingIntent notPend = PendingIntent.getActivity(NotificationService.this,0,notInt,0);

        NotificationCompat.Builder notificationBuilder;

        if (vertretung){
            notificationBuilder = new NotificationCompat.Builder(this,CreateNotificationChannels.CHANNEL_ID1);
            notificationId = 1;
        }else{
            notificationBuilder = new NotificationCompat.Builder(this, CreateNotificationChannels.CHANNEL_ID2);
            notificationId = 2;
        }
        notificationBuilder
                .setContentTitle(title)
                .setContentText(textKurz)
                .setSmallIcon(R.drawable.schullogo_18dp)
//                .setColor(getResources().getColor(R.color.colorPrimary))
//                .setLargeIcon(R.drawable.schullogo_homepage_small_icon)
//                .setVibrate(new long[]{0,1000})//aus,an,aus,an,...
//                .setSound()
//                .setLights(Color.WHITE,1000,1000)//farbe,an,aus
//                .setWhen(System.currentTimeMillis())//hier könnte man eine Zeit mitgeben, wann die vertretung erschienen ist.
                .setContentIntent(notPend)
                .setAutoCancel(true)//beim starten/druecken der Benachrichtigung verschwindet die Benachrichtigug
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        if(textLang!=null){
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(textKurz+textLang));//langer & kurzer text gleich?
        }
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId,notificationBuilder.build());

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
