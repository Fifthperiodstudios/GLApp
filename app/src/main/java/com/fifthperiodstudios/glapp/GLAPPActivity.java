package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class GLAPPActivity extends AppCompatActivity implements GLAPPActivityView, UpdateDataSignal {

    private ViewPager mViewPager;
    private StundenViewAdapter svAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private GLAPPActivityPresenter presenter;
    private StundenplanRepo repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        Intent intent = getIntent();

        repository = new StundenplanRepo(getApplicationContext());

        presenter = new GLAPPActivityPresenter(this, repository);

        repository.setPresenter(presenter);

        presenter.setMobilKey((String) intent.getExtras().get("mobilKey"));

        presenter.loadStundenplan(isOnline());

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateData(WochentagFragment f) {
        logOut();
    }

    private void logOut() {
        SharedPreferences prefs = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE);
        prefs.edit().putString("mobilKey", "DEF").commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void displayFreshStundenplan(StundenplanParser.Stundenplan stundenplan) {
        displayStundenplan(stundenplan);
    }

    public void displayStundenplan(StundenplanParser.Stundenplan stundenplan) {
        svAdapter = new StundenViewAdapter(getSupportFragmentManager(), stundenplan);
        //die neuen Fragmente für die einzelnen Tage werden erstellt
        svAdapter.setup();
        //dem ViewPager wird der Adapter übergeben
        mViewPager.setAdapter(svAdapter);
        //die tabs werden eingerichtet
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void displayOldStundenplan(StundenplanParser.Stundenplan stundenplan) {
        displayStundenplan(stundenplan);
        Toast.makeText(getApplicationContext(), "Kein Internet, gespeicherter Stundenplan könnte veraltet sein", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayKeinenStundenplan(){
        Toast.makeText(getApplicationContext(), "Etwas ist schief gelaufen", Toast.LENGTH_SHORT).show();
    }
}
