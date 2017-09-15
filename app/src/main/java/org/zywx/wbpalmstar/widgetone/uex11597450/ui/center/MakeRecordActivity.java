package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseTabActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.CRFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.DSFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.PSFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.RCFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo.SCFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import java.util.ArrayList;

public class MakeRecordActivity extends BaseTabActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_record);
    }

    @Override
    protected void initView() {
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        //添加fragment
        final ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(SCFragment.getInstance(C.ALL_TOPIC));
        fragList.add(CRFragment.getInstance(C.ALL_TOPIC));
        fragList.add(RCFragment.getInstance(C.ALL_TOPIC));
        fragList.add(PSFragment.getInstance(C.ALL_TOPIC));
        fragList.add(DSFragment.getInstance(C.ALL_TOPIC));
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.title_type)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
