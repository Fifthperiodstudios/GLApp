package com.fifthperiodstudios.glapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fifthperiodstudios.glapp.Klausurplan.KlausurplanFragment;
import com.fifthperiodstudios.glapp.Stundenplan.Stundenplan;
import com.fifthperiodstudios.glapp.Stundenplan.StundenplanFragment;
import com.fifthperiodstudios.glapp.Vertretungsplan.VertretungsplanFragment;

import java.util.ArrayList;

public class GLAPPViewAdapter extends FragmentPagerAdapter {


    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();
    private Farben farben;

    public GLAPPViewAdapter(FragmentManager fm, String mobilKey, Farben farben) {
        super(fm);
        this.farben = farben;

        Bundle args = new Bundle();
        args.putString ("mobilKey", mobilKey);
        args.putSerializable("farben", farben);
        StundenplanFragment stundenplanFragment = new StundenplanFragment();
        stundenplanFragment.setArguments(args);
        VertretungsplanFragment vertretungsplanFragment = new VertretungsplanFragment();
        vertretungsplanFragment.setArguments(args);
        KlausurplanFragment klausurplanFragment = new KlausurplanFragment();
        klausurplanFragment.setArguments(args);

        mFragmentList.add(stundenplanFragment);
        mFragmentTitleList.add("Stundenplan");

        mFragmentList.add(vertretungsplanFragment);
        mFragmentTitleList.add("Vertretungsplan");

        mFragmentList.add(klausurplanFragment);
        mFragmentTitleList.add("Klausurplan");
    }

    public ArrayList<String> getmFragmentTitleList() {
        return mFragmentTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public void setup (String mobilKey){

    }

    /*@Override
    public Fragment getItem(int position) {
        return WochentagFragment.newInstance(position, k[position], stundenplan.getWochentage().get(position));
    }*/

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
