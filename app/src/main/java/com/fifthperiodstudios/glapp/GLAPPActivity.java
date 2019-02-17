package com.fifthperiodstudios.glapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fifthperiodstudios.glapp.Klausurplan.KlausurplanFragment;
import com.fifthperiodstudios.glapp.Login.MainActivity;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanFragment;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanFragment;

import java.io.File;
import java.util.concurrent.TimeUnit;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class GLAPPActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private GLAPPViewAdapter glappViewAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Farben farben;
    private final String SHAREDPREFERENCES_NAME = "com.fifthperiodstudios.glapp";
    private final String SHAREDPREFERENCES_MOBILKEY = "mobilKey";
    private String SHAREDPREFERENCES_BENACHRICHTIGUNGEN = "benachrichtigungen";
    private String SHAREDPREFERENCES_INTERVALL = "intervallbenachrichtigung";

    private SharedPreferences sharedPreferences;

    private KlausurplanFragment klausurplanFragment;
    private StundenplanFragment stundenplanFragment;
    private VertretungsplanFragment vertretungsplanFragment;

    private GLAPPPresenterImpl glappPresenter;
    private File stundenplanFile;
    private File klausurplanFile;
    private File farbenFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);

        if (getSupportFragmentManager().getFragments().isEmpty()) {
            klausurplanFragment = new KlausurplanFragment();
            stundenplanFragment = new StundenplanFragment();
            vertretungsplanFragment = new VertretungsplanFragment();
        } else {
            stundenplanFragment = (StundenplanFragment) getSupportFragmentManager().getFragments().get(0);
            if (stundenplanFragment == null) {
                stundenplanFragment = new StundenplanFragment();
            }
            vertretungsplanFragment = (VertretungsplanFragment) getSupportFragmentManager().getFragments().get(1);
            if (vertretungsplanFragment == null) {
                vertretungsplanFragment = new VertretungsplanFragment();
            }
            klausurplanFragment = (KlausurplanFragment) getSupportFragmentManager().getFragments().get(2);
            if (klausurplanFragment == null) {
                klausurplanFragment = new KlausurplanFragment();
            }
        }


        glappViewAdapter = new GLAPPViewAdapter(getSupportFragmentManager(), stundenplanFragment, vertretungsplanFragment, klausurplanFragment);

        //die tabs werden eingerichtet
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(glappViewAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage(R.string.berechtigung_erklaerung)
                        .setTitle("Berechtigung");
                builder.setPositiveButton("VERSTANDEN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(GLAPPActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1255);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1255);
            }
        }

        stundenplanFile = new File(getApplicationContext().getExternalFilesDir(null), "Stundenplan");
        klausurplanFile = new File(getApplicationContext().getExternalFilesDir(null), "Klausurplan");
        farbenFile = new File(getApplicationContext().getExternalFilesDir(null), "Farben");
        String mobilKey = getIntent().getStringExtra("mobilKey");
        String vertretungsplanDatum = sharedPreferences.getString("Vertretungsplandatum", "DEF");
        String stundenplanDatum = sharedPreferences.getString("Stundenplandatum", "DEF");
        String klausurplanDatum = sharedPreferences.getString("Klausurplandatum", "DEF");

        GLAPPRepositoryImpl glappRepository = new GLAPPRepositoryImpl(mobilKey, stundenplanFile, klausurplanFile, farbenFile, vertretungsplanDatum, stundenplanDatum, klausurplanDatum);
        glappPresenter = new GLAPPPresenterImpl(glappRepository,
                stundenplanFragment,
                vertretungsplanFragment,
                klausurplanFragment);
        glappPresenter.loadFarben();


        boolean firststart = sharedPreferences.getBoolean("firstStart", true);
        if (firststart) {
            createNotificationChannels();
            sharedPreferences.edit().putBoolean("firstStart", false).commit();
        }

        settings();
    }

    public void settings() {
        boolean notification = sharedPreferences.getBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN, true);
        if (notification) {
            PeriodicWorkRequest.Builder checkVertretungsplanBuilder = new PeriodicWorkRequest.Builder(CheckVertretungsplanWorker.class, 1, TimeUnit.MINUTES);
            PeriodicWorkRequest checkVertretungsplanWork = checkVertretungsplanBuilder.build();
            WorkManager.getInstance().enqueueUniquePeriodicWork("fifthperiodstudios", ExistingPeriodicWorkPolicy.KEEP, checkVertretungsplanWork);
        }
    }

    public void updateNotifications() {
        boolean notification = sharedPreferences.getBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN, true);
        int intervall = sharedPreferences.getInt(SHAREDPREFERENCES_INTERVALL, 15);

        if (!notification) {
            WorkManager.getInstance().cancelUniqueWork("fifthperiodstudios");
        } else {
            WorkManager.getInstance().cancelUniqueWork("fifthperiodstudios");
            PeriodicWorkRequest.Builder checkVertretungsplanBuilder = new PeriodicWorkRequest.Builder(CheckVertretungsplanWorker.class, intervall, TimeUnit.MINUTES);
            PeriodicWorkRequest checkVertretungsplanWork = checkVertretungsplanBuilder.build();
            WorkManager.getInstance().enqueueUniquePeriodicWork("fifthperiodstudios", ExistingPeriodicWorkPolicy.KEEP, checkVertretungsplanWork);
        }
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

        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                intent.putExtra("Farben", glappPresenter.getFarben());
                intent.putExtra("Stundenplan", glappPresenter.getStundenplan());
                this.startActivityForResult(intent, 1);
                break;
            case R.id.action_logout:
                logout();
                break;
            case R.id.action_about:
                Intent intent1 = new Intent(this, AboutInformationActivity.class);
                this.startActivity(intent1);
        }
        return true;
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE).edit().clear().commit();
        prefs.edit().putString(SHAREDPREFERENCES_MOBILKEY, "DEF").commit();
        if(stundenplanFile.exists()) {
            stundenplanFile.delete();
        }
        if(klausurplanFile.exists()) {
            klausurplanFile.delete();
        }
        if(farbenFile.exists()) {
            farbenFile.delete();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                farben = (Farben) data.getSerializableExtra("Farben");
                glappPresenter.updateFarben(farben);
            } else if (resultCode == 0) {

            }
            updateNotifications();
        }
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "CHANNEL_VERTRETUNG",
                    "Vertretungs_Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Dieser Channel ist für die Vertretung");//hier müsste die Beschreibung des Channels rein

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
}
