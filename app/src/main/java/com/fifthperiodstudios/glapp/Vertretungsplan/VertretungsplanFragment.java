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
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Downloader.DownloadVertretungsplanStatusListener;
import com.fifthperiodstudios.glapp.Downloader.VertretungsplanDownloader;
import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.OnUpdateListener;
import com.fifthperiodstudios.glapp.R;

import java.util.ArrayList;


public class VertretungsplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadVertretungsplanStatusListener, OnUpdateListener {

    private Vertretungsplan vertretungsplan;
    private Farben farben;

    private VertretungsplanDownloader vertretungsplanDownloader;

    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        vertretungsplanDownloader = new VertretungsplanDownloader(getActivity(), args.getString("mobilKey"), this);
        vertretungsplanDownloader.downloadVertretungsplan();

        recyclerView = rootView.findViewById(R.id.recyc);

        return rootView;
    }

    @Override
    public void onRefresh() {
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung() {
        Toast.makeText(getContext(), "Vertretungplan kann nicht aktualisiert werden, bitte prüfe deine Internetverbindung", Toast.LENGTH_LONG).show();
    }

    @Override
    public void fertigHeruntergeladen(Vertretungsplan vertretungsplan) {
        if(vertretungsplan.getVertretungstage().size()==0){
            VertretungsplanStunde stunde = new VertretungsplanStunde();
            stunde.setRaum("Für heute und morgen liegt leider kein Vertretungsplan vor. :/");
            vertretungsplan.getStunden().add(stunde);
        }else {
            for (int i = 0; i < vertretungsplan.getVertretungstage().size(); i++) {
                VertretungsplanStunde stunde = new VertretungsplanStunde();
                if (i == 0) {
                    stunde.setRaum("Vertretung für heute:");
                } else {
                    stunde.setRaum("Vertretung für morgen:");
                }
                vertretungsplan.getStunden().add(stunde);
                VertretungsplanStunde stunde1 = new VertretungsplanStunde();
                if (vertretungsplan.getVertretungstage().get(i).getStunden().size() == 0) {
                    stunde1.setRaum("Für diesen Tag liegt leider keine Vertretung vor. :/");
                } else {
                    for (int j = 0; j < vertretungsplan.getVertretungstage().get(i).getStunden().size(); j++) {
                        vertretungsplan.getStunden().add(vertretungsplan.getVertretungstage().get(i).getStunden().get(j));
                    }
                }
            }
        }
        recyclerAdapter = new VertretungsViewAdapter(vertretungsplan, farben);
        recyclerManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void andererFehler() {

    }

    @Override
    public void updateData(Farben farben) {
        this.farben = farben;
        recyclerAdapter.notifyDataSetChanged();
    }
}
