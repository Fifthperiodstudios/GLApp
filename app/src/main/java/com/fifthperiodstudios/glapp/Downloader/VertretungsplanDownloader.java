package com.fifthperiodstudios.glapp.Downloader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.fifthperiodstudios.glapp.Vertretungsplan.Vertretungsplan;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class VertretungsplanDownloader {
    private DownloadVertretungsplanStatusListener downloadVertretungsplanStatusListener;
    private Activity activity;
    private Vertretungsplan vertretungsplan;
    private String mobilKey;
    private final String URL = "https://mobil.gymnasium-lohmar.org/XML/vplan.php?mobilKey=";

    public VertretungsplanDownloader(Activity activity, String mobilKey, DownloadVertretungsplanStatusListener downloadVertretungsplanStatusListener) {
        this.activity = activity;
        this.mobilKey = mobilKey;
        this.downloadVertretungsplanStatusListener = downloadVertretungsplanStatusListener;
    }

    public void downloadVertretungsplan() {
        if (isOnline()) {
            new DownloadVertretungsplanXML().execute(URL + mobilKey);
        } else {
            downloadVertretungsplanStatusListener.keineInternetverbindung();
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

    private class DownloadVertretungsplanXML extends AsyncTask<String, Void, Vertretungsplan> {

        protected Vertretungsplan doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return null;
            } catch (XmlPullParserException e) {
                return null;
            }

        }

        protected void onPostExecute(Vertretungsplan result) {
            super.onPostExecute(result);
            if (result == null) {
                downloadVertretungsplanStatusListener.andererFehler();
            } else {
                downloadVertretungsplanStatusListener.fertigHeruntergeladen(result);
            }
        }

        private Vertretungsplan loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            VertretungsplanParser vertretungsplanParser = new VertretungsplanParser();
            try {
                stream = downloadUrl(urlString);

                try {
                    vertretungsplan = (Vertretungsplan) vertretungsplanParser.parseVertretungsplan(stream);
                    //FileOutputStream fos = activity.getApplicationContext().openFileOutput("Vertretungsplan", Context.MODE_PRIVATE);
                    //ObjectOutputStream os = new ObjectOutputStream(fos);
                    //os.writeObject(vertretungsplan);
                    //os.close();
                    //fos.close();
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

            return vertretungsplan;
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