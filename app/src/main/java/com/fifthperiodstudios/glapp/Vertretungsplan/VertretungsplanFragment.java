package com.fifthperiodstudios.glapp.Vertretungsplan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fifthperiodstudios.glapp.Downloader.DownloadVertretungsplanStatusListener;
import com.fifthperiodstudios.glapp.Downloader.VertretungsplanDownloader;
import com.fifthperiodstudios.glapp.R;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class VertretungsplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadVertretungsplanStatusListener {

    private Vertretungsplan vertretungsplan;
    private Stundenplan stundenplan;

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
        stundenplan = (Stundenplan) args.getSerializable("stundenplan");

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        vertretungsplanDownloader = new VertretungsplanDownloader(getActivity(), stundenplan, args.getString("mobilKey"), this);
        vertretungsplanDownloader.downloadVertretungsplan();

        recyclerManager = new LinearLayoutManager(container.getContext());
        //recyclerAdapter = new VertretungsViewAdapter();

        recyclerView = rootView.findViewById(R.id.recyc);
        recyclerView.setLayoutManager(recyclerManager);
        //recyclerView.setAdapter(recyclerAdapter);

        return rootView;
    }

    @Override
    public void onRefresh() {

        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung() {

    }

    @Override
    public void fertigHeruntergeladen(Vertretungsplan vertretungsplan) {

    }

    @Override
    public void andererFehler() {

    }
}
