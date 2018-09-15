package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=GyJOmW6lnPK7M&schuljahr=5";
    private ArrayList<StundenplanParser.Wochentag> wochentagList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadPage();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean(("firstrun"), true)) {
            //Download Stundenplan;

            prefs.edit().putBoolean("firstrun", false);
        }
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }
    // Uses AsyncTask to download the XML feed from stackoverflow.com.
    public void loadPage() {

        if (checkWifiOnAndConnected()) {
            new DownloadXmlTask().execute(URL);
        }
    }

    private class DownloadXmlTask extends AsyncTask<String, Void, List<StundenplanParser.Wochentag>> {

        protected List<StundenplanParser.Wochentag> doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return new ArrayList<StundenplanParser.Wochentag>();
            } catch (XmlPullParserException e) {
                return new ArrayList<StundenplanParser.Wochentag>();
            }

        }


        protected void onPostExecute(List<StundenplanParser.Wochentag> result) {
            super.onPostExecute(result);

            if(!result.isEmpty()) {
                mAdapter = new MyAdapter(result.get(0));// use a linear layout manager
                mRecyclerView.setAdapter(mAdapter);
            }else {
                Toast.makeText(MainActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
            }
        }

        private List<StundenplanParser.Wochentag> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            StundenplanParser stundenplanParser = new StundenplanParser();
            List<StundenplanParser.Wochentag> wochenTage = null;

            try {
                stream = downloadUrl(urlString);
                wochenTage = stundenplanParser.parse(stream);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            return wochenTage;
        }

        // Given a string representation of a URL, sets up a connection and gets
// an input stream.
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
    }
}
