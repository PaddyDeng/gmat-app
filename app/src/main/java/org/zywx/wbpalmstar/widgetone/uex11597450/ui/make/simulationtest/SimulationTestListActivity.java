package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseTabActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SimulationTestListActivity extends BaseTabActivity {

    private int type = 1; //默认语文;  语文, 数学, 全套 分别 对应 type = 1 | 2 | 3

    public static void startSimulationActivity(Context c, int type) {
        Intent intent = new Intent(c, SimulationTestListActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, type);
        c.startActivity(intent);
    }

    @BindView(R.id.simulation_title_tv)
    TextView simuTitleTv;

    public int getType() {
        return type;
    }

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        type = intent.getIntExtra(Intent.EXTRA_INDEX, C.LANGUAGE);//默认语文
        String title = getString(R.string.str_sim_language_title);
        if (type == C.MATH) {
            title = getString(R.string.str_sim_math_title);
        } else if (type == C.ALL) {
            title = getString(R.string.str_sim_all_title);
        }
        simuTitleTv.setText(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_test_list);
    }

    @Override
    protected void initView() {
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(new PrepFragment());
        fragList.add(new GwdFragment());
        fragList.add(new FeaturedQueFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.simluation_tab_arr)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
