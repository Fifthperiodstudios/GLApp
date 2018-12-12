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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            new DownloadStundenplanXML().execute(URL + mobilKey);
        }else {
            try {
                FileInputStream fis = activity.getApplicationContext().openFileInput("Stundenplan");
                ObjectInputStream is = new ObjectInputStream(fis);
                Stundenplan stundenplan = (Stundenplan) is.readObject();
                is.close();
                fis.close();
                downloadStundenplanStatusListener.keineInternetverbindung(stundenplan);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
                downloadStundenplanStatusListener.fertigHeruntergeladen(result);
            }
        }

        private Stundenplan loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            StundenplanParser stundenplanParser = new StundenplanParser();
            try {
                stream = downloadUrl(urlString);

                try {
                    stundenplan = (Stundenplan) stundenplanParser.parseStundenplan(stream);
                    FileOutputStream fos = activity.getApplicationContext().openFileOutput("Stundenplan", Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(stundenplan);
                    os.close();
                    fos.close();
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

