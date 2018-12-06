package com.fifthperiodstudios.glapp.Downloader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.fifthperiodstudios.glapp.Klausurplan.Klausurplan;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class KlausurplanDownloader {
    private DownloadKlausurplanStatusListener downloadKlausurplanStatusListener;
    private Activity activity;
    private String mobilKey;
    private final String URL = "";
    private Klausurplan klausurplan;

    public KlausurplanDownloader (Activity activity, String mobilKey, DownloadKlausurplanStatusListener downloadKlausurplanStatusListener) {
        this.activity = activity;
        this.mobilKey = mobilKey;
        this.downloadKlausurplanStatusListener = downloadKlausurplanStatusListener;
    }

    public void downloadKlausurplan () {
        if(isOnline()) {
            new DownloadKlausurplanXML().execute(URL + mobilKey);
        }else {
            /*try {
                File directory = activity.getApplicationContext().getFilesDir();
                File file = new File(directory, "Klausurplan.xml");
                KlausurplanParser klausurplanParser = new KlausurplanParser();
                klausurplan = (Klausurplan) klausurplanParser.parseKlausurplan(new FileInputStream(file));
                downloadKlausurplanStatusListener.keineInternetverbindung(klausurplan);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
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
            //KlausurplanParser klausurplanParser = new KlausurplanParser(stundenplan);
            try {
                stream = downloadUrl(urlString);

                FileOutputStream outputStream = new FileOutputStream(new File(activity.getApplicationContext().getFilesDir(), "Klausurplan.xml"));
                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = stream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();

                File directory = activity.getApplicationContext().getFilesDir();
                File file = new File(directory, "Stundenplan.xml");
                /*try {
                    klausurplan = (Klausurplan) klausurplanParser.parseKlausurplan(new FileInputStream(file));
                } catch (XmlPullParserException e) {
                    downloadKlausurplanStatusListener.andererFehler();
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    downloadKlausurplanStatusListener.andererFehler();
                    e.printStackTrace();
                } catch (IOException e) {
                    downloadKlausurplanStatusListener.andererFehler();
                    e.printStackTrace();
                }*/

            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return new Klausurplan();
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
