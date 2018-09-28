package com.fifthperiodstudios.glapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import java.util.ArrayList;

public class StundenViewAdapter extends FragmentPagerAdapter {


    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
    private StundenplanParser.Stundenplan stundenplan;
    String[] k = {"Mo", "Di", "Mi", "Do", "Fr"};

    public StundenViewAdapter(FragmentManager fm, StundenplanParser.Stundenplan stundenplan) {
        super(fm);
        this.stundenplan = stundenplan;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void setup (){
        for (int i = 0; i < 5; i++) {
            WochentagFragment w = new WochentagFragment();
            Bundle args = new Bundle();
            args.putSerializable("data", stundenplan.getWochentage().get(i));
            w.setArguments(args);
            mFragmentList.add(w);
        }
    }

    /*@Override
    public Fragment getItem(int position) {
        return WochentagFragment.newInstance(position, k[position], stundenplan.getWochentage().get(position));
    }*/

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return k[position];
    }

    public void updateFragments(){
        for(Fragment f : mFragmentList){
            if(f != null){
                ((WochentagFragment) f).updateFragment();
            }

        }
    }

}
