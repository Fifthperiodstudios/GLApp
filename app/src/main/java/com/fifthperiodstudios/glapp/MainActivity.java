package com.fifthperiodstudios.glapp;

import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.support.design.widget.TabLayout;

public class MainActivity extends AppCompatActivity{

    SharedPreferences prefs;
    private LoginViewAdapter loginViewAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    public class LoginViewAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        public LoginViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void setup (){
            StudentLoginFragment f1 = new StudentLoginFragment ();
            mFragmentTitleList.add("Schüler");
            LehrerLoginFragment f2 = new LehrerLoginFragment ();
            mFragmentTitleList.add("Lehrer");
            mFragmentList.add(f1);
            mFragmentList.add(f2);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        toolbar = (Toolbar) findViewById(R.id.student_teacher_toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.student_teacher_pager);

        prefs = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE);

//        createBackgroundService();
        //Ein Kommentar hahahahaha
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
        if (prefs.getString("mobilkey", "DEF").equals("DEF")) {
            setupLoginFragments();
        }else{
            Intent intent = new Intent(this, GLAPPActivity.class);
            intent.putExtra("mobilkey", prefs.getString("mobilkey", "DEF"));
            startActivity(intent);
            finish();
        }
    }

    public void setupLoginFragments(){
        loginViewAdapter = new LoginViewAdapter(getSupportFragmentManager());
        //die neuen Fragmente für die einzelnen Tage werden erstellt
        loginViewAdapter.setup();
        //dem ViewPager wird der Adapter übergeben
        mViewPager.setAdapter(loginViewAdapter);
        //die tabs werden eingerichtet
        tabLayout = (TabLayout) findViewById(R.id.student_teacher_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
