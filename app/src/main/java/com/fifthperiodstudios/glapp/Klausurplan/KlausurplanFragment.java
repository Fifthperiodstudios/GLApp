package com.fifthperiodstudios.glapp.Klausurplan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fifthperiodstudios.glapp.Downloader.DownloadKlausurplanStatusListener;
import com.fifthperiodstudios.glapp.Downloader.KlausurplanDownloader;
import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.OnUpdateListener;
import com.fifthperiodstudios.glapp.R;

public class KlausurplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadKlausurplanStatusListener, OnUpdateListener {

    private Klausurplan klausurplan;
    private Farben farben;
    private KlausurplanDownloader klausurplanDownloader;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView keineKlausuren;

    public KlausurplanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.klausurplan_fragment, container, false);
        Bundle args = getArguments();

        farben = (Farben) args.getSerializable("farben");

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = rootView.findViewById(R.id.recyc);
        keineKlausuren = rootView.findViewById(R.id.keine_vertretung_text);
        
        klausurplanDownloader = new KlausurplanDownloader(getActivity(), args.getString("mobilKey"), this);
        klausurplanDownloader.downloadKlausurplan();

        return rootView;
    }

    @Override
    public void onRefresh() {
        klausurplanDownloader.downloadKlausurplan();
        Toast.makeText(getContext(), "Aktualisiert", Toast.LENGTH_SHORT).show();
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung(Klausurplan klausurplan) {
        setupKlausurplanFragment(klausurplan);
        Toast.makeText(getContext(), "Keine Internetverbindung", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fertigHeruntergeladen(Klausurplan klausurplan) {
       setupKlausurplanFragment(klausurplan);
    }
    
    
    public void setupKlausurplanFragment(Klausurplan klausurplan){
        if(klausurplan != null && klausurplan.getKlausuren().size() != 0){
            this.klausurplan = klausurplan;
            keineKlausuren.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerAdapter = new KlausurplanViewAdapter(klausurplan, farben);
            recyclerManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(recyclerManager);
            recyclerView.setAdapter(recyclerAdapter);
        }else {
            recyclerView.setVisibility(View.GONE);
            keineKlausuren.setVisibility(View.VISIBLE);
            keineKlausuren.setText("Es stehen momentan keine Klausuren mehr an :)");
        }
    }
    @Override
    public void andererFehler() {
        Toast.makeText(getContext(), "Es ist etwas schiefgelaufen", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateData(Farben farben) {
        this.farben = farben;
        if(klausurplan != null && klausurplan.getKlausuren().size() != 0){
            ((KlausurplanViewAdapter)recyclerAdapter).setFarben(farben);
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}
