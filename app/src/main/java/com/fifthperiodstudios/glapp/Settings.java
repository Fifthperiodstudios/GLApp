package com.fifthperiodstudios.glapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

public class Settings extends AppCompatActivity {
    private Toolbar toolbar;

    private String SHAREDPREFERENCES_NAME = "com.fifthperiodstudios.glapp";
    private String SHAREDPREFERENCES_BENACHRICHTIGUNGEN = "benachrichtigungen";
    private String SHAREDPREFERENCES_INTERVALL = "intervallbenachrichtigung";

    private boolean benachrichtigungen;

    private Switch swtBenachrichtigungen;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    //    SharedPreferences helpPreferences;
    private Farben farben;
    private Stundenplan stundenplan;
    private Spinner spinner;

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

        sharedPreferences = getSharedPreferences(SHAREDPREFERENCES_NAME, 0);
        sharedPreferencesEditor = sharedPreferences.edit();

        benachrichtigungen = sharedPreferences.getBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN, true);

        spinner = (Spinner) findViewById(R.id.intervall_spinner);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.intervall, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(getSelectedFromIntervall(sharedPreferences.getInt(SHAREDPREFERENCES_INTERVALL, 15)));
        swtBenachrichtigungen = findViewById(R.id.switch1);

        swtBenachrichtigungen.setChecked(benachrichtigungen);

        recyclerManager = new LinearLayoutManager(this);
        recyclerAdapter = new RecyclerViewAdapter(stundenplan, farben, getSupportFragmentManager());
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(recyclerManager);
        recyclerView.setAdapter(recyclerAdapter);

        Button randomBtn = findViewById(R.id.random_btn);
        randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                farben.getInts().clear();
                farben.getFarbenFaecher().clear();
                for (int i = 0; i < stundenplan.getWochentage().size(); i++) {
                    for (int j = 0; j < stundenplan.getWochentage().get(i).getStunden().size(); j++) {
                        farben.getFarbeFach(stundenplan.getWochentage().get(i).getStunden().get(j).getFach());
                    }
                }
                recyclerAdapter.notifyDataSetChanged();
                ((RecyclerViewAdapter) recyclerAdapter).setHaveColorsChanged(true);
            }
        });
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
        int intervall = getIntervallFromSelected(spinner.getSelectedItemPosition());
        sharedPreferencesEditor.putInt(SHAREDPREFERENCES_INTERVALL, intervall);
        sharedPreferencesEditor.putBoolean(SHAREDPREFERENCES_BENACHRICHTIGUNGEN, swtBenachrichtigungen.isChecked());
        sharedPreferencesEditor.commit();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("Farben", farben);
        if (((RecyclerViewAdapter) recyclerAdapter).haveColorsChanged()) {
            setResult(RESULT_OK, returnIntent);
        } else {
            setResult(0, returnIntent);
        }
    }

    public int getSelectedFromIntervall(int intervall) {
        int pos = 0;
        switch (intervall) {
            case 15:
                pos = 0;
                break;
            case 30:
                pos = 1;
                break;
            case 45:
                pos = 2;
                break;
            case 60:
                pos = 3;
                break;
            case 120:
                pos = 4;
                break;
        }
        return pos;
    }

    public int getIntervallFromSelected(int selected) {
        int intervall = 15;
        switch (selected) {
            case 0:
                intervall = 15;
                break;
            case 1:
                intervall = 30;
                break;
            case 2:
                intervall = 45;
                break;
            case 3:
                intervall = 60;
                break;
            case 4:
                intervall = 120;
                break;
        }
        return intervall;
    }
}
