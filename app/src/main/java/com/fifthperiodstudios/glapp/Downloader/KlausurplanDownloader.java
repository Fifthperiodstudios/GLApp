package com.fifthperiodstudios.glapp.Downloader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Klausurplan.KlausurenplanParser;

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

public class KlausurplanDownloader {
    private DownloadKlausurplanStatusListener downloadKlausurplanStatusListener;
    private Activity activity;
    private Klausurplan klausurplan;
    private String mobilKey;
    private final String URL = "https://mobil.gymnasium-lohmar.org/XML/klausur.php?mobilKey=";

    public KlausurplanDownloader (Activity activity, String mobilKey, DownloadKlausurplanStatusListener downloadKlausurplanStatusListener) {
        this.activity = activity;
        this.mobilKey = mobilKey;
        this.downloadKlausurplanStatusListener = downloadKlausurplanStatusListener;
    }

    public void downloadKlausurplan () {
        if(isOnline()) {
            new DownloadKlausurplanXML().execute(URL + mobilKey);
        }else {
            try {
                FileInputStream fis = activity.getApplicationContext().openFileInput("Klausurplan");
                ObjectInputStream is = new ObjectInputStream(fis);
                Klausurplan klausurplan = (Klausurplan) is.readObject();
                is.close();
                fis.close();
                downloadKlausurplanStatusListener.keineInternetverbindung(klausurplan);
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

    private class DownloadKlausurplanXML extends AsyncTask<String, Void, Klausurplan> {

        protected Klausurplan doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return null;
            } catch (XmlPullParserException e) {
                return null;
            }

        }

        protected void onPostExecute(Klausurplan result) {
            super.onPostExecute(result);
            if(result == null){
                downloadKlausurplanStatusListener.andererFehler();
            }else {
                downloadKlausurplanStatusListener.fertigHeruntergeladen(result);
            }
        }

        private Klausurplan loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            KlausurenplanParser klausurplanParser = new KlausurenplanParser();
            try {
                stream = downloadUrl(urlString);
                try {
                    klausurplan = (Klausurplan) klausurplanParser.parseKlausurplan(stream);
                    FileOutputStream fos = activity.getApplicationContext().openFileOutput("Klausurplan", Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(klausurplan);
                    os.close();
                    fos.close();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return klausurplan;
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