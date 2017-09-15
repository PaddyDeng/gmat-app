package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.intelligent;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseTabActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 顺序练习 2017.8.1 14:17
 */
public class OrderPracticeActivity extends BaseTabActivity {

    @BindView(R.id.intelligent_contact_sc)
    TextView scTxt;
    @BindView(R.id.intelligent_contact_rc)
    TextView rcTxt;
    @BindView(R.id.intelligent_contact_cr)
    TextView crTxt;
    private List<Fragment> fragList;
    private List<TextView> txtList;
    private int[] selectIds;
    private int selectionId = C.SC;//默认选择sc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intelligent_practice);
        scTxt.setSelected(true);
        txtList = new ArrayList<>();
        txtList.add(scTxt);
        txtList.add(crTxt);
        txtList.add(rcTxt);
        selectIds = new int[]{C.SC, C.CR, C.RC};
    }


    @OnClick({R.id.intelligent_contact_sc, R.id.intelligent_contact_cr, R.id.intelligent_contact_rc})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.intelligent_contact_sc:
            case R.id.intelligent_contact_cr:
            case R.id.intelligent_contact_rc:
                click(v);
                break;
            default:
                break;
        }
    }

    private void click(View v) {
        for (TextView tv : txtList) {
            if (tv == v) {
                tv.setSelected(true);
            } else {
                tv.setSelected(false);
            }
        }
        selectionId = selectIds[txtList.indexOf(v)];

        ((BaseOrderPracticeFragment) fragList.get(currentPage)).asyncRequest();
        if (currentPage > 0) {
            ((BaseOrderPracticeFragment) fragList.get(currentPage - 1)).asyncRequest();
        }
        if (currentPage < fragList.size() - 1) {
            ((BaseOrderPracticeFragment) fragList.get(currentPage + 1)).asyncRequest();
        }
    }

    public int getSelectionId() {
        return selectionId;
    }

    @Override
    protected void initView() {
        super.initView();
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        fragList = new ArrayList<>();
        fragList.add(new OgOrderFragment());
        fragList.add(new PerpOrderFragment());
        fragList.add(new OgNewOrderFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.intelligent_arr)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
