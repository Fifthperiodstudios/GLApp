package com.fifthperiodstudios.glapp.Klausurplan;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fifthperiodstudios.glapp.Downloader.DownloadKlausurplanStatusListener;
import com.fifthperiodstudios.glapp.Downloader.KlausurplanDownloader;
import com.fifthperiodstudios.glapp.Farben;
import com.fifthperiodstudios.glapp.OnUpdateListener;
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
import java.util.Date;

public class KlausurplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DownloadKlausurplanStatusListener, OnUpdateListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Klausurplan klausurplan;
    private Farben farben;
    private KlausurplanDownloader klausurplanDownloader;

    public KlausurplanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.klausurplan_fragment, container, false);
        Bundle args = getArguments();

        farben = (Farben) args.getSerializable("farben");

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        klausurplanDownloader = new KlausurplanDownloader(getActivity(), args.getString("mobilKey"), this);
        klausurplanDownloader.downloadKlausurplan();

        return rootView;
    }

    @Override
    public void onRefresh() {
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void keineInternetverbindung(Klausurplan klausurplan) {

    }

    @Override
    public void fertigHeruntergeladen(Klausurplan klausurplan) {

    }

    @Override
    public void andererFehler() {

    }

    @Override
    public void updateData(Farben farben) {

    }
}
