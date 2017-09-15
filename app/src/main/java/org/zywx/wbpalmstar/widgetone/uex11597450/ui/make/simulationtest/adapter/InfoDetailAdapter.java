package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class InfoDetailAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public InfoDetailAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    public List<Fragment> getData() {
        return list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }
}
