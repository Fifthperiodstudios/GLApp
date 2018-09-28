package com.fifthperiodstudios.glapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WochentagFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UpdateDataSignal updateDataSignal;

    public void updateFragment (){
        if(mRecyclerView != null) {
            mRecyclerView.swapAdapter(mAdapter,false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter.notifyDataSetChanged();
        }

    }

    public WochentagFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.stundenplanliste, container, false);
        Bundle args = getArguments();
        mRecyclerView = rootView.findViewById(R.id.recyc);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = new StundenListAdapter((StundenplanParser.Wochentag)args.getSerializable("data"));
        mRecyclerView.setAdapter(mAdapter);

        try {
            updateDataSignal = (UpdateDataSignal) this.getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(this.getActivity().toString()
                    + " must implement UpdateDataSignal");
        }


        return rootView;
    }

    @Override
    public void onRefresh() {
        updateDataSignal.updateData(this);
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    // onswipe event
}
