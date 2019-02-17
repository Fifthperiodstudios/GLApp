package com.fifthperiodstudios.glapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.fifthperiodstudios.glapp.Login.MainActivity;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanParser;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsstunde;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static android.content.Context.NOTIFICATION_SERVICE;

public class CheckVertretungsplanWorker extends Worker {
    public CheckVertretungsplanWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.fifthperiodstudios.glapp", Context.MODE_PRIVATE);
            String datum = sharedPreferences.getString("Vertretungsplandatum", "DEF");
            String mobilKey = sharedPreferences.getString("mobilKey", "DEF");
            //"https://mobil.gymnasium-lohmar.org/XML/vplan.php?mobilKey=" + mobilKey + "&timestamp=" + datum
            //"https://fifthperiodstudios.github.io/GLApp/"
            String vplan = loadVertretungsplanAsStringFromNetwork("https://mobil.gymnasium-lohmar.org/XML/vplan.php?mobilKey=" + mobilKey + "&timestamp=" + datum);
            if (vplan != null && !vplan.equals("0")) {
                InputStream newstream = null;
                try {
                    newstream = new ByteArrayInputStream(vplan.getBytes("UTF-8"));
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                    Vertretungsplan vertretungsplan = new VertretungsplanParser().parseVertretungsplan(newstream);
                    sharedPreferences.edit().putString("Vertretungsplandatum", vertretungsplan.getDatum()).commit();
                    for (int i = 0; i<vertretungsplan.getStunden().size(); i++) {
                        Vertretungsstunde vertretungsstunde = vertretungsplan.getStunden().get(i);

                        Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);//hier kann man auch extras anhägen!
                        PendingIntent notPend = PendingIntent.getActivity(getApplicationContext(), 0, activityIntent, 0);

                        String textKurz = "";
                        if (vertretungsstunde.getVLehrer().isEmpty()){
                            textKurz += "Bei " + vertretungsstunde.getFLehrer() + " ";
                        }else if(!vertretungsstunde.getVLehrer().equals(vertretungsstunde.getFLehrer())){
                            textKurz += "Vertretung bei " + vertretungsstunde.getVLehrer() + " ";
                        }
                        if (vertretungsstunde.getRaumNeu().isEmpty()){
                            textKurz += "in Raum " + vertretungsstunde.getRaum();
                        }else{
                            textKurz += "Raumwechsel: " + vertretungsstunde.getRaumNeu();
                        }

                        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_VERTRETUNG");
                        notificationBuilder
                                .setContentTitle(vertretungsstunde.getDatumAlsText() + " " + vertretungsstunde.getStunde() + ". " + vertretungsstunde.getFach().getFach() + " " + vertretungsstunde.getBemerkung())
                                .setContentText(textKurz)
                                .setSmallIcon(R.drawable.ic_event_busy_black_24dp)
                                .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                                .setContentIntent(notPend)
                                .setAutoCancel(true)//beim starten/druecken der Benachrichtigung verschwindet die Benachrichtigug
                                .setPriority(NotificationCompat.PRIORITY_HIGH);
                        notificationManager.notify(i, notificationBuilder.build());
                    }
                } catch (final UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return Result.success();
    }


    private String loadVertretungsplanAsStringFromNetwork(String urlString) throws IOException, XmlPullParserException {
        InputStream stream = null;
        // Instantiate the parser
        String stundenplan;

        try {
            stream = downloadUrl(urlString);
            stundenplan = convertStreamToString(stream);
            //stundenplan = parser.parseStundenplan(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return stundenplan;
    }


    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        InputStream in = urlConnection.getInputStream();
        return in;
    }
    private String convertStreamToString(InputStream in) {
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
