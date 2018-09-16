package com.fifthperiodstudios.glapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class StundenViewAdapter extends FragmentPagerAdapter{

    private ArrayList<StundenplanParser.Wochentag> wochentage;
    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

    public StundenViewAdapter(FragmentManager fm, ArrayList<StundenplanParser.Wochentag> wochentage) {
        super(fm);
        this.wochentage = wochentage;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(int position, WochentagFragment fragment, String title) {
        Bundle args = new Bundle();
        args.putSerializable("data", wochentage.get(position));
        fragment.setArguments(args);

        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
