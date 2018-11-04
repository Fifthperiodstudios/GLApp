package com.fifthperiodstudios.glapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    private Toolbar toolbar;

    public static final String SHAREDPREFERENCES_NAME = "com.fifthperiodstudios.glapp";
    public static final String SHAREDPREFERENCES_BENACHRICHTIGUNGEN = "benachrichtigungen";
    public static final String SHAREDPREFERENCES_AKTUALISIEREN = "autoAktualiseren";

    boolean benachrichtigungen;
    boolean autoAktualisieren;
    int farbeNummer;
    String farbeName;

    Switch swtBenachrichtigungen;
    Switch swtAutoAktualisieren;

    TextView tVbenachrichtigungen;
    TextView tVanzeigen;

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tVbenachrichtigungen = findViewById(R.id.textView);
        tVanzeigen = findViewById(R.id.textView2);

        sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_NAME,0);
        sharedPreferencesEditor = sharedPreferences.edit();

        benachrichtigungen = sharedPreferences.getBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN,true);
        autoAktualisieren = sharedPreferences.getBoolean(SHAREDPREFERENCES_AKTUALISIEREN,true);
        farbeNummer = sharedPreferences.getInt("farbeNummer",0);
        farbeName = sharedPreferences.getString("farbeName","#54de54");

        swtBenachrichtigungen = findViewById(R.id.switch1);
        swtAutoAktualisieren = findViewById(R.id.switch2);


        recyclerManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerViewAdapter();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(recyclerManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        save();
        return true;
    }

    private void save() {
        sharedPreferencesEditor.putBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN,swtBenachrichtigungen.isChecked());
        sharedPreferencesEditor.putBoolean(SHAREDPREFERENCES_AKTUALISIEREN,swtAutoAktualisieren.isChecked());
        sharedPreferencesEditor.commit();
    }
}
