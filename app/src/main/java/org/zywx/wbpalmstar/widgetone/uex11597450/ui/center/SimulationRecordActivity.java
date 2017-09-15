package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseTabActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;

import java.util.ArrayList;

public class SimulationRecordActivity extends BaseTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_record);
    }

    @Override
    protected void initView() {
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(new LangRecordFragment());
        fragList.add(new MathRecordFragment());
        fragList.add(new AllRecordFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.simulation_tab_arr)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
