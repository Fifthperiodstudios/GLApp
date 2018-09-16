package com.fifthperiodstudios.glapp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.TabLayout;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;
    private static final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=GyJOmW6lnPK7M&schuljahr=5";

    private StundenViewAdapter svAdapter;
    private ViewPager mViewPager;

    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);


        prefs = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean(("firstrun"), true)) {
            //Download Stundenplan;
            loadPage();
            prefs.edit().putBoolean("firstrun", false).commit();
        } else {
            File directory = getApplicationContext().getFilesDir();
            File file = new File(directory, "Stundenplan.xml");
            StundenplanParser stundenplanParser = new StundenplanParser();
            try {
                setupFragments(stundenplanParser.parse(new FileInputStream(file)));
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if (wifiInfo.getNetworkId() == -1) {
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        } else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    // Uses AsyncTask to download the XML feed from stackoverflow.com.
    public void loadPage() {

        if (checkWifiOnAndConnected()) {
            new DownloadXmlTask().execute(URL);
        }
    }

    public void setupFragments (List<StundenplanParser.Wochentag> result){
        if (!result.isEmpty()) {
            svAdapter =
                    new StundenViewAdapter(getSupportFragmentManager(), (ArrayList<StundenplanParser.Wochentag>) result);
            String[] k = {"Mo", "Di", "Mi", "Do", "Fr"};
            for (int i = 0; i < 5; i++) {
                svAdapter.addFragment(i, new WochentagFragment(), k[i]);
            }
            mViewPager.setAdapter(svAdapter);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        } else {
            Toast.makeText(MainActivity.this, "Sorry", Toast.LENGTH_SHORT).show();
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

           setupFragments(result);
        }

        private List<StundenplanParser.Wochentag> loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            StundenplanParser stundenplanParser = new StundenplanParser();
            List<StundenplanParser.Wochentag> wochenTage = null;

            try {
                stream = downloadUrl(urlString);

                FileOutputStream outputStream = new FileOutputStream(new File(getApplicationContext().getFilesDir(), "Stundenplan.xml"));
                int bytesRead = -1;
                byte[] buffer = new byte[4096];
                while ((bytesRead = stream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();

                File directory = getApplicationContext().getFilesDir();
            File file = new File(directory, "Stundenplan.xml");
            try {
                wochenTage = (ArrayList<StundenplanParser.Wochentag>) stundenplanParser.parse(new FileInputStream(file));
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
