package com.fifthperiodstudios.glapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

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
import java.util.List;

public class GLAPPActivity extends AppCompatActivity implements UpdateDataSignal {

    private ViewPager mViewPager;
    private StundenViewAdapter svAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private String mobilKey;
    private StundenplanParser.Stundenplan stundenplan;
    private static final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mobilKey = (String) intent.getExtras().get("mobilkey");
        new DownloadXmlTask().execute(URL + mobilKey);
    }

    //Fragmente einstellen
    public void setupFragments(StundenplanParser.Stundenplan result) {
        //guckt dass die geparseten Wochentage nicht leer sind
        if (!result.getWochentage().isEmpty()) {
            //stellt die farben ein:
            setUpColors(result);
            //Stellt den FragmentPagerAdapter ein
            svAdapter = new StundenViewAdapter(getSupportFragmentManager(), result);
            //die neuen Fragmente für die einzelnen Tage werden erstellt
            svAdapter.setup();
            //dem ViewPager wird der Adapter übergeben
            mViewPager.setAdapter(svAdapter);
            //die tabs werden eingerichtet
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);
        } else {
            Toast.makeText(GLAPPActivity.this, "Etwas ist schief gelaufen :/", Toast.LENGTH_SHORT).show();
        }
    }

    public void setUpColors(StundenplanParser.Stundenplan stundenplan) {
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
        colors.add("#c8d6e5");
        colors.add("#c8d6e5");
        colors.add("#c8d6e5");
        for (Fach f : stundenplan.getFächer()) {
            int i = (int) (Math.random() * colors.size());
            f.setColor(colors.get(i));
            colors.remove(i);
        }
    }


    @Override
    public void updateData(WochentagFragment f) {
        //setUpColors(stundenplan);
        logOut();
        //svAdapter.updateFragments();
    }

    private void logOut(){
        SharedPreferences prefs = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE);
        prefs.edit().putString("mobilkey", "DEF");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private class DownloadXmlTask extends AsyncTask<String, Void, StundenplanParser.Stundenplan> {

        protected StundenplanParser.Stundenplan doInBackground(String... urls) {
            try {
                return loadXmlFromNetwork(urls[0]);
            } catch (IOException e) {
                return new StundenplanParser.Stundenplan();
            } catch (XmlPullParserException e) {
                return new StundenplanParser.Stundenplan();
            }

        }


        protected void onPostExecute(StundenplanParser.Stundenplan result) {
            super.onPostExecute(result);

            setupFragments(result);
        }

        private StundenplanParser.Stundenplan loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
            InputStream stream = null;
            // Instantiate the parser
            StundenplanParser stundenplanParser = new StundenplanParser();

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
                    stundenplan = (StundenplanParser.Stundenplan) stundenplanParser.parseStundenplan(new FileInputStream(file));
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
