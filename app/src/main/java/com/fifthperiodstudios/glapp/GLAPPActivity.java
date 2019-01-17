package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fifthperiodstudios.glapp.Klausurplan.KlausurplanFragment;
import com.fifthperiodstudios.glapp.Login.MainActivity;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanFragment;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GLAPPActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private GLAPPViewAdapter glappViewAdapter;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Farben farben;
    public final String SHAREDPREFERENCES_NAME = "com.fifthperiodstudios.glapp";
    public final String SHAREDPREFERENCES_MOBILKEY = "mobilKey";

    private KlausurplanFragment klausurplanFragment;
    private StundenplanFragment stundenplanFragment;
    private VertretungsplanFragment vertretungsplanFragment;

    private GLAPPPresenterImpl glappPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportFragmentManager().getFragments().isEmpty()) {
            klausurplanFragment = new KlausurplanFragment();
            stundenplanFragment = new StundenplanFragment();
            vertretungsplanFragment = new VertretungsplanFragment();
        }else {
            stundenplanFragment = (StundenplanFragment) getSupportFragmentManager().getFragments().get(0);
            if(stundenplanFragment == null){
                stundenplanFragment = new StundenplanFragment();
            }
            vertretungsplanFragment = (VertretungsplanFragment) getSupportFragmentManager().getFragments().get(1);
            if(vertretungsplanFragment == null){
                vertretungsplanFragment = new VertretungsplanFragment();
            }
            klausurplanFragment = (KlausurplanFragment) getSupportFragmentManager().getFragments().get(2);
            if(klausurplanFragment == null){
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

        File stundenplanFile = new File(getApplicationContext().getExternalFilesDir(null), "Stundenplan");
        File klausurplanFile = new File(getApplicationContext().getExternalFilesDir(null), "Klausurplan");
        File farbenFile = new File(getApplicationContext().getExternalFilesDir(null), "Farben");
        String mobilKey = getIntent().getStringExtra("mobilKey");
        String vertretungsplanDatum =  getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE).getString("Vertretungsplandatum", "DEF");
        String stundenplanDatum = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE).getString("Stundenplandatum", "DEF");
        String klausurplanDatum = getSharedPreferences("com.fifthperiodstudios.glapp", MODE_PRIVATE).getString("Klausurplandatum", "DEF");

        GLAPPRepositoryImpl glappRepository = new GLAPPRepositoryImpl(mobilKey, stundenplanFile, klausurplanFile, farbenFile, vertretungsplanDatum, stundenplanDatum, klausurplanDatum);
        glappPresenter = new GLAPPPresenterImpl(glappRepository,
                                                stundenplanFragment,
                                                vertretungsplanFragment,
                                                klausurplanFragment);
        glappPresenter.loadFarben();
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

        switch(id){
            case R.id.action_settings:
                Intent intent = new Intent (this, Settings.class);
                intent.putExtra("Farben", glappPresenter.getFarben());
                intent.putExtra("Stundenplan", glappPresenter.getStundenplan());
                this.startActivityForResult(intent, 1);
                break;
            case R.id.action_logout:
                logout ();
                break;
            case R.id.action_about:
                Intent intent1 = new Intent(this, AboutInformationActivity.class);
                this.startActivity(intent1);
        }
        return true;
    }

    private void logout(){
        SharedPreferences prefs = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        this.getSharedPreferences(SHAREDPREFERENCES_NAME,MODE_PRIVATE).edit().clear().commit();
        prefs.edit().putString(SHAREDPREFERENCES_MOBILKEY, "DEF").commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                farben = (Farben) data.getSerializableExtra("Farben");
                glappPresenter.updateFarben(farben);
            }else if(resultCode == 0){

            }
        }
    }


}
