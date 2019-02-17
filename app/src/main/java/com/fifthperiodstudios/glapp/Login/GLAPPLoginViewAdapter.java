package com.fifthperiodstudios.glapp.Login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class GLAPPLoginViewAdapter extends FragmentPagerAdapter {


    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

    public GLAPPLoginViewAdapter(FragmentManager fm, SchuelerLoginFragment schuelerLoginFragment, LehrerLoginFragment lehrerLoginFragment) {
        super(fm);

        mFragmentList.add(schuelerLoginFragment);
        mFragmentTitleList.add("Schüler");

        mFragmentList.add(lehrerLoginFragment);
        mFragmentTitleList.add("Lehrer");

    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
