package com.fifthperiodstudios.glapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fifthperiodstudios.glapp.Klausurplan.KlausurplanFragment;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanFragment;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanFragment;

import java.util.ArrayList;

public class GLAPPViewAdapter extends FragmentPagerAdapter {


    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

    public GLAPPViewAdapter(FragmentManager fm, StundenplanFragment stundenplanFragment, VertretungsplanFragment vertretungsplanFragment, KlausurplanFragment klausurplanFragment) {
        super(fm);

        mFragmentList.add(stundenplanFragment);
        mFragmentTitleList.add("Stundenplan");

        mFragmentList.add(vertretungsplanFragment);
        mFragmentTitleList.add("Vertretungsplan");

        mFragmentList.add(klausurplanFragment);
        mFragmentTitleList.add("Klausurplan");
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
