package com.fifthperiodstudios.glapp.Vertretungsplan;


import android.content.SharedPreferences;
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

import com.fifthperiodstudios.glapp.GLAPPPresenter;
import com.fifthperiodstudios.glapp.GLAPPViews;
import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.OnUpdateListener;
import com.fifthperiodstudios.glapp.R;

import java.util.ArrayList;


public class VertretungsplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GLAPPViews, GLAPPViews.VertretungsplanView {

    private Vertretungsplan vertretungsplan;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView keineVertretung;

    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerAdapter;
    RecyclerView.LayoutManager recyclerManager;
    private GLAPPPresenter glappPresenter;
    private SharedPreferences sharedPreferences;
    private TextView informationen;

    public VertretungsplanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.vertretungsplan_fragment, container, false);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        keineVertretung = rootView.findViewById(R.id.keine_vertretung_text);
        recyclerView = rootView.findViewById(R.id.recyc);
        informationen = rootView.findViewById(R.id.informationen);
        sharedPreferences = getActivity().getSharedPreferences("com.fifthperiodstudios.glapp", getActivity().MODE_PRIVATE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        glappPresenter.downloadVertretungsplan();
    }

    @Override
    public void onRefresh() {
        glappPresenter.downloadVertretungsplan();
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung() {
        recyclerView.setVisibility(View.GONE);
        keineVertretung.setVisibility(View.VISIBLE);
        keineVertretung.setText(R.string.connection_vplan_error);
    }

    @Override
    public void fertigHeruntergeladen(Vertretungsplan vertretungsplan, Farben farben) {
        this.vertretungsplan = vertretungsplan;
        sharedPreferences.edit().putString("Vertretungsplandatum", vertretungsplan.getDatum()).commit();
        setupVertretunsplanFragment(vertretungsplan, farben);
    }

    public void setupVertretunsplanFragment(Vertretungsplan vertretungsplan, Farben farben){
        if(vertretungsplan.getStunden().size() == 0){
            recyclerView.setVisibility(View.GONE);
            keineVertretung.setVisibility(View.VISIBLE);
            keineVertretung.setText(R.string.keine_vertretung_text);
        }else {
            this.vertretungsplan = vertretungsplan;
            keineVertretung.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerAdapter = new VertretungsViewAdapter(vertretungsplan, farben);
            recyclerManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(recyclerManager);
            recyclerView.setAdapter(recyclerAdapter);
        }
        if(vertretungsplan.getInformationen().size() != 0) {
            informationen.setVisibility(View.VISIBLE);
            String s = "Informationen: ";
            for (String k: vertretungsplan.getInformationen()) {
                s =  s + "\n" + k;
            }
            informationen.setText(s);
        }else {
            informationen.setVisibility(View.GONE);
        }
    }
    @Override
    public void andererFehler() {
        Toast.makeText(getContext(), R.string.some_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(GLAPPPresenter presenter) {
        this.glappPresenter = presenter;
    }

    @Override
    public void updateFarben(Farben farben) {
        if(vertretungsplan != null && vertretungsplan.getStunden().size() != 0) {
            ((VertretungsViewAdapter)recyclerAdapter).setFarben(farben);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

}
