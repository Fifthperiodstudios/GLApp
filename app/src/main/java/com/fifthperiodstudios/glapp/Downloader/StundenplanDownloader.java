package com.fifthperiodstudios.glapp.Downloader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;


import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class StundenplanDownloader {
    private DownloadStundenplanStatusListener downloadStundenplanStatusListener;
    private Activity activity;
    private Stundenplan stundenplan;
    private String mobilKey;
    private final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=";

    public StundenplanDownloader(Activity activity, String mobilKey, DownloadStundenplanStatusListener downloadStundenplanStatusListener) {
        this.activity = activity;
        this.mobilKey = mobilKey;
        this.downloadStundenplanStatusListener = downloadStundenplanStatusListener;
    }

    public void downloadStundenplan() {
        if (isOnline()) {
            new DownloadStundenplanXML().execute(URL + mobilKey);
        } else {
            try {
                this.stundenplan = loadStundenplanfromDevice();
                downloadStundenplanStatusListener.keineInternetverbindung(stundenplan);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Stundenplan loadStundenplanfromDevice() throws IOException, ClassNotFoundException {
        FileInputStream fis = activity.getApplicationContext().openFileInput("Stundenplan");
        ObjectInputStream is = new ObjectInputStream(fis);
        Stundenplan stundenplan = (Stundenplan) is.readObject();
        is.close();
        fis.close();
        return stundenplan;
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
            return loadXmlFromNetwork(urls[0]);
        }

        protected void onPostExecute(Stundenplan result) {
            super.onPostExecute(result);
            if (result == null) {
                downloadStundenplanStatusListener.andererFehler();
            } else {
                downloadStundenplanStatusListener.fertigHeruntergeladen(result);
            }
        }

        private String convertStreamToString(InputStream in) {
            java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }


        public Stundenplan downloadLatestStundenplan(String url, Stundenplan stundenplan) {
            InputStream stream = null;
            StundenplanParser stundenplanparser = new StundenplanParser();
            try {
                stream = downloadUrl(url + "&timestamp=" + String.valueOf((stundenplan.getDatum().getTime()/1000)+1));

                String neuodernicht = convertStreamToString(stream);

                if (!neuodernicht.equals("0")) {
                    InputStream newstream = new ByteArrayInputStream(neuodernicht.getBytes("UTF-8"));
                    stundenplan = stundenplanparser.parseStundenplan(newstream);
                }else{
                    Log.d("TAGDD", "downloadLatestStundenplan: " + neuodernicht);
                }
            } catch (IOException e) {
                stundenplan = loadFromInternet(url);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return stundenplan;
        }

        public Stundenplan loadStundenplan(String url) {
            Stundenplan stundenplan = null;
            try {
                stundenplan = loadStundenplanfromDevice();
                stundenplan = downloadLatestStundenplan(url, stundenplan);
            } catch (IOException e) {
                stundenplan = loadFromInternet(url);
            } catch (ClassNotFoundException e) {
                stundenplan = loadFromInternet(url);
            }
            return stundenplan;
        }

        public Stundenplan loadFromInternet(String url) {
            InputStream stream = null;
            Stundenplan stundenplan = null;
            try {
                stream = downloadUrl(url);
                StundenplanParser stundenplanParser = new StundenplanParser();
                stundenplan = (Stundenplan) stundenplanParser.parseStundenplan(stream);
                FileOutputStream fos = activity.getApplicationContext().openFileOutput("Stundenplan", Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(stundenplan);
                os.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return stundenplan;
        }

        private Stundenplan loadXmlFromNetwork(String urlString) {
            StundenplanDownloader.this.stundenplan = loadStundenplan(urlString);
            return StundenplanDownloader.this.stundenplan;
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



