package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.buyrecord;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseTabActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class BuyRecordActivity extends BaseTabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_record);
    }

    @Override
    protected void initView() {
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(new PublicLessonFragment());
        fragList.add(new VideoLessonFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.buy_lesson_arr)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
