package com.fifthperiodstudios.glapp.Login;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.Calendar;

import com.fifthperiodstudios.glapp.GLAPPActivity;
import com.fifthperiodstudios.glapp.Notification.BackgroundService;
import com.fifthperiodstudios.glapp.R;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanFragment;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanFragment;

public class MainActivity extends AppCompatActivity{

    private SharedPreferences prefs;
    private Toolbar toolbar;
    private LoginPresenter loginPresenter;
    private SchuelerLoginFragment schuelerLoginFragment;
    private ViewPager mViewPager;
    private LehrerLoginFragment lehrerLoginFragment;
    private GLAPPLoginViewAdapter glappLoginViewAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportFragmentManager().getFragments().isEmpty()) {
            schuelerLoginFragment = new SchuelerLoginFragment();
            lehrerLoginFragment = new LehrerLoginFragment();
        }else {
            schuelerLoginFragment = (SchuelerLoginFragment) getSupportFragmentManager().getFragments().get(0);
            if(schuelerLoginFragment == null){
                schuelerLoginFragment = new SchuelerLoginFragment();
            }

            lehrerLoginFragment = (LehrerLoginFragment) getSupportFragmentManager().getFragments().get(1);
            if(lehrerLoginFragment == null){
                lehrerLoginFragment = new LehrerLoginFragment();
            }
        }


        glappLoginViewAdapter = new GLAPPLoginViewAdapter(getSupportFragmentManager(), schuelerLoginFragment, lehrerLoginFragment);

        //die tabs werden eingerichtet
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(glappLoginViewAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
//        createBackgroundService();

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
        }else{
            loginPresenter = new LoginPresenterImpl(new LoginRepositoryImpl(), schuelerLoginFragment, lehrerLoginFragment);
            schuelerLoginFragment.setPresenter(loginPresenter);
            lehrerLoginFragment.setPresenter(loginPresenter);
        }
    }
}
