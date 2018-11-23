package com.fifthperiodstudios.glapp.Stundenplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fifthperiodstudios.glapp.R;

public class WochentagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wochentag);
        Stundenplan.Wochentag wochentag = (Stundenplan.Wochentag) getIntent().getSerializableExtra("Wochentag");
        mRecyclerView = findViewById(R.id.recyc);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new StundenListAdapter(wochentag);
        mRecyclerView.setAdapter(mAdapter);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

}
