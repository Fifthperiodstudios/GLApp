package com.fifthperiodstudios.glapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

public class Settings extends AppCompatActivity {
    private Toolbar toolbar;

    public static final String SHAREDPREFERENCES_NAME = "com.fifthperiodstudios.glapp";
    public static final String SHAREDPREFERENCES_BENACHRICHTIGUNGEN = "benachrichtigungen";
    public static final String SHAREDPREFERENCES_AKTUALISIEREN = "autoAktualiseren";

    boolean benachrichtigungen;
    boolean autoAktualisieren;
    int farbeNummer;
    private String farbeName;

    private Switch swtBenachrichtigungen;
    private Switch swtAutoAktualisieren;

    private TextView tVbenachrichtigungen;
    private TextView tVanzeigen;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    //    SharedPreferences helpPreferences;
    private Farben farben;
    private Stundenplan stundenplan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        farben = (Farben) getIntent().getSerializableExtra("Farben");
        stundenplan = (Stundenplan) getIntent().getSerializableExtra("Stundenplan");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tVbenachrichtigungen = findViewById(R.id.textView);
        tVanzeigen = findViewById(R.id.textView2);

        sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_NAME, 0);
        sharedPreferencesEditor = sharedPreferences.edit();

        benachrichtigungen = sharedPreferences.getBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN, true);
        autoAktualisieren = sharedPreferences.getBoolean(SHAREDPREFERENCES_AKTUALISIEREN, true);

        swtBenachrichtigungen = findViewById(R.id.switch1);
        swtAutoAktualisieren = findViewById(R.id.switch2);

        swtBenachrichtigungen.setChecked(benachrichtigungen);
        swtAutoAktualisieren.setChecked(autoAktualisieren);

        recyclerManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerViewAdapter(stundenplan, farben, getSupportFragmentManager());
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(recyclerManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        save();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        save();
        finish();
    }

    private void save() {
        sharedPreferencesEditor.putBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN, swtBenachrichtigungen.isChecked());
        sharedPreferencesEditor.putBoolean(SHAREDPREFERENCES_AKTUALISIEREN, swtAutoAktualisieren.isChecked());
        sharedPreferencesEditor.commit();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Farben", farben);
        if (((RecyclerViewAdapter) recyclerAdapter).haveColorsChanged()) {
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(0, returnIntent);
        }
    }
}
