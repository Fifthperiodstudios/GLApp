package com.fifthperiodstudios.glapp;

import android.app.FragmentTransaction;
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
import java.util.List;

import android.support.design.widget.TabLayout;

public class MainActivity extends AppCompatActivity{

    SharedPreferences prefs;
    private static final String URL = null;
    public static final String GLAPPDATEN = "com.example.myfirstapp.GLAPPDATEN";
    private StundenplanParser.Stundenplan stundenplan;
    private static String mobilKey = "";
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
        tabLayout = (TabLayout) findViewById(R.id.student_teacher_tabs);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.student_teacher_pager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        break;
                    case 1:
                        toolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
            //Override default methods and change toolbar color inside of here
        });

        prefs = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getString("mobilKey", "DEF").equals("DEF")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            setupLoginFragments();
        }else{
            Intent intent = new Intent(this, GLAPPActivity.class);
            intent.putExtra("mobilKey", prefs.getString("mobilKey", "DEF"));
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
