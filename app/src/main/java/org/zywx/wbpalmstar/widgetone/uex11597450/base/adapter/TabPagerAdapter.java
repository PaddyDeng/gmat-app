package org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public abstract class TabPagerAdapter extends FragmentPagerAdapter {
    private String [] titles;
    protected ArrayList<Fragment> fragments;

    public TabPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles =titles;
    }
    public TabPagerAdapter(FragmentManager fm, String[] titles, ArrayList<Fragment> fragments) {
        super(fm);
        this.titles =titles;
        this.fragments=fragments;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
