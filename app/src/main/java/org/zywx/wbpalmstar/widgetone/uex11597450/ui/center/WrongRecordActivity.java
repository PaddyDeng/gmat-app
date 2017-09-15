package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.CRFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.DSFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.PSFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.RCFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.SCFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class WrongRecordActivity extends BaseActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worng_record);
    }

    @Override
    protected void initView() {
        viewPager.setAdapter(getPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果
    }

    private PagerAdapter getPagerAdapter() {
        //添加fragment
        final ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(SCFragment.getInstance(C.ERROR_TOPIC));
        fragList.add(CRFragment.getInstance(C.ERROR_TOPIC));
        fragList.add(RCFragment.getInstance(C.ERROR_TOPIC));
        fragList.add(PSFragment.getInstance(C.ERROR_TOPIC));
        fragList.add(DSFragment.getInstance(C.ERROR_TOPIC));
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.title_type)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
