package com.fifthperiodstudios.glapp.Stundenplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.R;

public class WochentagActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wochentag);
        Stundenplan.Wochentag wochentag = (Stundenplan.Wochentag) getIntent().getSerializableExtra("Wochentag");
        Farben farben = (Farben) getIntent().getSerializableExtra("farben");
        Log.d("TAG", wochentag.getStunden().get(0).getFach().getFach());
        Log.d("TAG", farben.getFarbeFach(wochentag.getStunden().get(0).getFach()));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mRecyclerView = findViewById(R.id.recyc);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new StundenListAdapter(wochentag, farben);
        mRecyclerView.setAdapter(mAdapter);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

}
