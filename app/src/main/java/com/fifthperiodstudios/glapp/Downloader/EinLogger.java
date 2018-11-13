package com.fifthperiodstudios.glapp.Downloader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.GLAPPActivity;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class EinLogger {
    private EinLoggerListener einLoggerListener;
    private Activity activity;
    private String mobilKey;

    public EinLogger(Activity activity, EinLoggerListener einLoggerListener) {
        this.activity = activity;
        this.einLoggerListener = einLoggerListener;
    }

    public void logIn (String benutzername, String password, String URL) {
        if(isOnline()) {
            if(checkFields(benutzername, password)) {
                einLoggerListener.unvollstaendig();
            }else {
                new DownloadMobilKeyTask().execute(URL);
            }
        }else {
            einLoggerListener.keineInternetverbindung();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkFields (String u, String p){
        if(u.isEmpty() || p.isEmpty()) {
            return true;
        }
        return false;
    }

    private class DownloadMobilKeyTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return "";
            } catch (XmlPullParserException e) {
                return "";
            }

        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result == null) {
                einLoggerListener.andererFehler();
            }else if (result.isEmpty() || result.equals("0")) {
                einLoggerListener.falscheDaten();
            }else {
                einLoggerListener.eingeloggt(result);
            }
        }

        private String convertStreamToString(InputStream in) {
            java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }

        private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser

            try {
                stream = downloadUrl(urlString);
                mobilKey = convertStreamToString(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return mobilKey;
        }

        // Given a string representation of a URL, sets up a connection and gets
// an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            InputStream in = urlConnection.getInputStream();


            return in;
        }
    }
}
