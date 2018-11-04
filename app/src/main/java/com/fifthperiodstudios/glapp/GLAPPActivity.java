package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Downloader.DownloadStundenplanStatusListener;
import com.fifthperiodstudios.glapp.Downloader.StundenplanDownloader;
import com.fifthperiodstudios.glapp.Login.MainActivity;
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

public class GLAPPActivity extends AppCompatActivity implements DownloadStundenplanStatusListener {

    private ViewPager mViewPager;
    private GLAPPViewAdapter glappViewAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private String mobilKey;
    private Stundenplan stundenplan;
    StundenplanDownloader stundenplanDownloader;
    private static final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        Intent intent = getIntent();
        mobilKey = (String) intent.getExtras().get("mobilKey");
        stundenplanDownloader = new StundenplanDownloader(this, mobilKey, this);
        stundenplanDownloader.downloadStundenplan();
    }


    //Fragmente einstellen
    public void setupFragments(Stundenplan result) {
        //Stellt den FragmentPagerAdapter ein
        glappViewAdapter = new GLAPPViewAdapter(getSupportFragmentManager(), result);
        //die neuen Fragmente für die einzelnen Tage werden erstellt
        glappViewAdapter.setup(mobilKey);
        //dem ViewPager wird der Adapter übergeben
        mViewPager.setAdapter(glappViewAdapter);
        //die tabs werden eingerichtet
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void keineInternetverbindung(Stundenplan stundenplan) {
        Toast.makeText(getApplicationContext(), "Keine Internetverbindung, alter Stundenplan", Toast.LENGTH_SHORT);
        setupFragments(stundenplan);
    }

    @Override
    public void fertigHeruntergeladen(Stundenplan stundenplan) {
        setupFragments(stundenplan);
    }

    @Override
    public void andererFehler() {
        Toast.makeText(getApplicationContext(), "Etwas ist schiefgelaufen :/", Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.action_settings:
                Intent intent = new Intent (this, Settings.class);
                this.startActivity(intent);
                break;
            case R.id.action_logout:
                logout ();
                break;
        }
        return true;
    }

    private void logout(){
        SharedPreferences prefs = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE);
        prefs.edit().putString("mobilKey", "DEF").commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
