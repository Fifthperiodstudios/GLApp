package com.fifthperiodstudios.glapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fifthperiodstudios.glapp.Login.MainActivity;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanFragment;

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
    private String mobilKey;
    private Farben farben;
    private static final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?mobilKey=";
    public static final String SHAREDPREFERENCES_NAME = "com.fifthperiodstudios.glapp";
    public static final String SHAREDPREFERENCES_MOBILEKEY = "mobilKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        Intent intent = getIntent();
        mobilKey = (String) intent.getExtras().get(SHAREDPREFERENCES_MOBILEKEY);
        setupFragments();
    }


    //Fragmente einstellen
    public void setupFragments() {
        Farben farben;
        FileInputStream fis = null;
        try {
            fis = getApplicationContext().openFileInput("farben");
            ObjectInputStream is = new ObjectInputStream(fis);
            farben = (Farben) is.readObject();
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
            farben = new Farben ();
        } catch (IOException e) {
            farben = new Farben ();
        } catch (ClassNotFoundException e) {
            farben = new Farben();
        }

        //Stellt den FragmentPagerAdapter ein
        glappViewAdapter = new GLAPPViewAdapter(getSupportFragmentManager(), mobilKey, farben);
        //dem ViewPager wird der Adapter übergeben
        mViewPager.setAdapter(glappViewAdapter);
        //die tabs werden eingerichtet
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
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
                Stundenplan stundenplan = ((StundenplanFragment)glappViewAdapter.getItem(0)).getStundenplan();
                Farben farben = ((StundenplanFragment)glappViewAdapter.getItem(0)).getFarben();
                intent.putExtra("Stundenplan", stundenplan);
                intent.putExtra("farben", farben);
                this.startActivityForResult(intent, 1);
                break;
            case R.id.action_logout:
                logout ();
                break;
        }
        return true;
    }

    private void logout(){
        SharedPreferences prefs = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
        this.getSharedPreferences(SHAREDPREFERENCES_NAME,MODE_PRIVATE).edit().clear().commit();
        prefs.edit().putString(SHAREDPREFERENCES_MOBILEKEY, "DEF").commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                farben = (Farben) data.getSerializableExtra("farben");
                for(int i = 0; i<glappViewAdapter.getCount(); i++){
                    ((OnUpdateListener)glappViewAdapter.getItem(i)).updateData(farben);
                }
                FileOutputStream fos = null;
                try {
                    fos = getApplicationContext().openFileOutput("farben", Context.MODE_PRIVATE);
                    ObjectOutputStream os = new ObjectOutputStream(fos);
                    os.writeObject(farben);
                    os.close();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(resultCode == RESULT_CANCELED){

            }
        }
    }
}
