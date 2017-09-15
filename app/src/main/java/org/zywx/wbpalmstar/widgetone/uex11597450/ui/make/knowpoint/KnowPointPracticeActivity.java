package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.knowpoint;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseTabActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class KnowPointPracticeActivity extends BaseTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_point_practice);
    }

    @Override
    protected void initView() {
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(new SCKnowPrFragment());
        fragList.add(new CRKnowPrFragment());
        fragList.add(new RCKnowPrFragment());
        fragList.add(new PSKnowPrFragment());
        fragList.add(new DSKnowPrFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.title_type)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
