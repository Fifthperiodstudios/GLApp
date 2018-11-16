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
            SchuelerLoginFragment f1 = new SchuelerLoginFragment();
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
//        createBackgroundService();
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
        if (prefs.getString("mobilKey", "DEF").equals("DEF")) {
            setupLoginFragments();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
