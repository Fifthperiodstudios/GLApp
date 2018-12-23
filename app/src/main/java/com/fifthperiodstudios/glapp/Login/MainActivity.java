package com.fifthperiodstudios.glapp.Login;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;

import android.support.design.widget.TabLayout;

import com.fifthperiodstudios.glapp.GLAPPActivity;
import com.fifthperiodstudios.glapp.Notification.BackgroundService;
import com.fifthperiodstudios.glapp.R;

public class MainActivity extends AppCompatActivity{

    SharedPreferences prefs;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE);
    }

    private void createBackgroundService() {
        final long MINUTEN = 180;//in welchem Abstand soll der Hintergrund dienst arbeiten
        AlarmManager alarmManager = (AlarmManager)MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);

        Intent startBackgroundserviceIntent = new Intent(MainActivity.this,BackgroundService.class);
        PendingIntent startBackgroundservicePendingIntent = PendingIntent.getService(MainActivity.this,0,startBackgroundserviceIntent,0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 1000*60*MINUTEN);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*MINUTEN,startBackgroundservicePendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!prefs.getString("mobilKey", "DEF").equals("DEF")) {
            Intent intent = new Intent(this, GLAPPActivity.class);
            intent.putExtra("mobilKey", prefs.getString("mobilKey", "DEF"));
            startActivity(intent);
            finish();
        }
    }

}
