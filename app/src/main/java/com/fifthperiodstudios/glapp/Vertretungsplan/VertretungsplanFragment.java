package com.fifthperiodstudios.glapp.Vertretungsplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Downloader.DownloadVertretungsplanStatusListener;
import com.fifthperiodstudios.glapp.Downloader.VertretungsplanDownloader;
import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.OnUpdateListener;
import com.fifthperiodstudios.glapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class VertretungsplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadVertretungsplanStatusListener, OnUpdateListener {

    private Vertretungsplan vertretungsplan;
    private Farben farben;

    private VertretungsplanDownloader vertretungsplanDownloader;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView keineVertretung;

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerManager;

    public VertretungsplanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.vertretungsplan_fragment, container, false);
        Bundle args = getArguments();

        farben = (Farben) args.getSerializable("farben");

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        keineVertretung = rootView.findViewById(R.id.keine_vertretung_text);
        recyclerView = rootView.findViewById(R.id.recyc);

        vertretungsplanDownloader = new VertretungsplanDownloader(getActivity(), args.getString("mobilKey"), this);
        vertretungsplanDownloader.downloadVertretungsplan();

        return rootView;
    }

    @Override
    public void onRefresh() {
        vertretungsplanDownloader.downloadVertretungsplan();
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung() {
        keineVertretung.setVisibility(View.VISIBLE);
        keineVertretung.setText("Keine Internetverbindung \n(๑◕︵◕๑)");
    }

    @Override
    public void fertigHeruntergeladen(Vertretungsplan vertretungsplan) {
        if(vertretungsplan.getStunden().size() == 0){
            recyclerView.setVisibility(View.GONE);
            keineVertretung.setVisibility(View.VISIBLE);
            keineVertretung.setText("Es fällt bei dir leider nichts aus \n(๑◕︵◕๑)");
        }else {
            this.vertretungsplan = vertretungsplan;
            keineVertretung.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerAdapter = new VertretungsViewAdapter(vertretungsplan, farben);
            recyclerManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(recyclerManager);
            recyclerView.setAdapter(recyclerAdapter);
        }

    }

    @Override
    public void andererFehler() {

    }

    @Override
    public void updateData(Farben farben) {
        this.farben = farben;
        if(vertretungsplan != null && vertretungsplan.getStunden().size() != 0) {
            ((VertretungsViewAdapter)recyclerAdapter).setFarben(farben);
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}
