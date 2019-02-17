package com.fifthperiodstudios.glapp.Klausurplan;

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

import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.GLAPPPresenter;
import com.fifthperiodstudios.glapp.GLAPPViews;
import com.fifthperiodstudios.glapp.R;


public class KlausurplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GLAPPViews.KlausurplanView {

    private Klausurplan klausurplan;
    private GLAPPPresenter glappPresenter;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView keineKlausuren;
    private SharedPreferences sharedPreferences;

    public KlausurplanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.klausurplan_fragment, container, false);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = rootView.findViewById(R.id.recyc);
        keineKlausuren = rootView.findViewById(R.id.keine_klausuren_text);

        sharedPreferences = getActivity().getSharedPreferences("com.fifthperiodstudios.glapp", getActivity().MODE_PRIVATE);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        glappPresenter.downloadKlausurplan();
    }

    @Override
    public void onRefresh() {
        glappPresenter.downloadKlausurplan();
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung(Klausurplan klausurplan, Farben farben) {
        setupKlausurplanFragment(klausurplan, farben);
        Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fertigHeruntergeladen(Klausurplan klausurplan, Farben farben) {
        //sharedPreferences.edit().putString("Klausurplandatum", klausurplan.getDatum()).commit();
        this.klausurplan = klausurplan;
        setupKlausurplanFragment(klausurplan, farben);
    }
    
    
    public void setupKlausurplanFragment(Klausurplan klausurplan, Farben farben){
        if(klausurplan != null && klausurplan.getKlausuren().size() != 0){
            keineKlausuren.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerAdapter = new KlausurplanViewAdapter(klausurplan, farben);
            recyclerManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(recyclerManager);
            recyclerView.setAdapter(recyclerAdapter);
        }else {
            recyclerView.setVisibility(View.GONE);
            keineKlausuren.setVisibility(View.VISIBLE);
            keineKlausuren.setText(R.string.keine_klausuren_text);
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
        if(klausurplan != null && klausurplan.getKlausuren().size() != 0){
            ((KlausurplanViewAdapter)recyclerAdapter).setFarben(farben);
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}
