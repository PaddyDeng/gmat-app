package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.difficultymake;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseTabActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiffDetailActivity extends BaseTabActivity {

    private int levelId;
    private String title;

    @BindView(R.id.diff_detail_title_tv)
    TextView titleTxt;

    public static void startDiffDetail(Context c, String title, int levelId) {
        Intent intent = new Intent(c, DiffDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, title);
        intent.putExtra(Intent.EXTRA_INDEX, levelId);
        c.startActivity(intent);
    }

    public int getLevelId() {
        return levelId;
    }

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent != null) {
            levelId = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
            title = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_detail);
    }

    @Override
    protected void initView() {
        titleTxt.setText(title);
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        final List<Fragment> fragList = new ArrayList<>();
        fragList.add(new SCDiffFragment());
        fragList.add(new CRDiffFragment());
        fragList.add(new RCDiffFragment());
        fragList.add(new PSDiffFragment());
        fragList.add(new DSDiffFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.title_type)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
