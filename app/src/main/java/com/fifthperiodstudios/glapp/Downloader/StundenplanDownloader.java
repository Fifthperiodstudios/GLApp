package com.fifthperiodstudios.glapp.Downloader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.fifthperiodstudios.glapp.Stundenplan.Fach;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class StundenplanDownloader {
    private DownloadStundenplanStatusListener downloadStundenplanStatusListener;
    private Activity activity;
    private Stundenplan stundenplan;
    private String mobilKey;
    private final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=";

    public StundenplanDownloader (Activity activity, String mobilKey, DownloadStundenplanStatusListener downloadStundenplanStatusListener) {
        this.activity = activity;
        this.mobilKey = mobilKey;
        this.downloadStundenplanStatusListener = downloadStundenplanStatusListener;
    }

    public void downloadStundenplan () {
        if(isOnline()) {
            Log.d("LOG", "aslödk");
            new DownloadStundenplanXML().execute(URL + mobilKey);
        }else {
            try {
                File directory = activity.getApplicationContext().getFilesDir();
                File file = new File(directory, "Stundenplan.xml");
                StundenplanParser stundenplanParser = new StundenplanParser();
                stundenplan = (Stundenplan) stundenplanParser.parseStundenplan(new FileInputStream(file));
                setUpColors(stundenplan);
                downloadStundenplanStatusListener.keineInternetverbindung(stundenplan);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUpColors(Stundenplan stundenplan) {
        ArrayList<String> colors = new ArrayList<String>();
        colors.add("#1abc9c");
        colors.add("#3498db");
        colors.add("#2ecc71");
        colors.add("#9b59b6");
        colors.add("#34495e");
        colors.add("#16a085");
        colors.add("#f1c40f");
        colors.add("#e74c3c");
        colors.add("#95a5a6");
        colors.add("#B33771");
        colors.add("#A64B48");
        colors.add("#A0A68B");//s
        colors.add("#454442");//s
        for (Fach f : stundenplan.getFächer()) {
            int i = (int) (Math.random() * colors.size());
            f.setColor(colors.get(i));
            colors.remove(i);
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

    private class DownloadStundenplanXML extends AsyncTask<String, Void, Stundenplan> {

        protected Stundenplan doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return null;
            } catch (XmlPullParserException e) {
                return null;
            }

        }


        protected void onPostExecute(Stundenplan result) {
            super.onPostExecute(result);
            if(result == null){
                downloadStundenplanStatusListener.andererFehler();
            }else {
                setUpColors(stundenplan);
                downloadStundenplanStatusListener.fertigHeruntergeladen(result);
            }
        }

        private Stundenplan loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            StundenplanParser StundenplanParser = new StundenplanParser();
            try {
                stream = downloadUrl(urlString);

                FileOutputStream outputStream = new FileOutputStream(new File(activity.getApplicationContext().getFilesDir(), "Stundenplan.xml"));
                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = stream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();

                File directory = activity.getApplicationContext().getFilesDir();
                File file = new File(directory, "Stundenplan.xml");
                try {
                    stundenplan = (Stundenplan) StundenplanParser.parseStundenplan(new FileInputStream(file));
                } catch (XmlPullParserException e) {
                    downloadStundenplanStatusListener.andererFehler();
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    downloadStundenplanStatusListener.andererFehler();
                    e.printStackTrace();
                } catch (IOException e) {
                    downloadStundenplanStatusListener.andererFehler();
                    e.printStackTrace();
                }

            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return stundenplan;
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

