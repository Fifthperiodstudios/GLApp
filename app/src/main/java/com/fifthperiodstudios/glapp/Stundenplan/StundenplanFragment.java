package com.fifthperiodstudios.glapp.Stundenplan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fifthperiodstudios.glapp.R;

public class StundenplanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private final String URL = "https://mobil.gymnasium-lohmar.org/XML/stupla.php?";
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Stundenplan stundenplan;

    public StundenplanFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.stundenplan_fragment, container, false);
        Bundle args = getArguments();
        stundenplan = (Stundenplan) args.getSerializable("stundenplan");
        StundenplanView stundenplanView = rootView.findViewById(R.id.stundenplanview);
        stundenplanView.setStundenplan (stundenplan);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        setupStundenplanFragment(stundenplan);

        return rootView;
    }

    public void setupStundenplanFragment (Stundenplan stundenplan) {

    }

    @Override
    public void onRefresh() {
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    // onswipe event
}
