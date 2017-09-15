package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.singlepractice;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseTabActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SinglePracticeActivity extends BaseTabActivity {

    @BindView(R.id.single_contact_sc)
    TextView scTxt;
    @BindView(R.id.single_contact_cr)
    TextView crTxt;
    @BindView(R.id.single_contact_rc)
    TextView rcTxt;
    @BindView(R.id.single_contact_pc)
    TextView pcTxt;
    @BindView(R.id.single_contact_ds)
    TextView dsTxt;
    @BindView(R.id.version_switch)
    TextView versionHint;
    private List<Fragment> fragList;
    private List<TextView> txtList;
    private int[] selectIds;
    private int selectionId = C.SC;//默认选择sc
    private int versionCond = 1;//默认新版
    private View recordView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        scTxt.setSelected(true);
        txtList = new ArrayList<>();
        txtList.add(scTxt);
        txtList.add(crTxt);
        txtList.add(rcTxt);
        txtList.add(pcTxt);
        txtList.add(dsTxt);
        selectIds = new int[]{C.SC, C.CR, C.RC, C.PS, C.DS};
        recordView = scTxt;
    }

    @OnClick({R.id.single_contact_sc, R.id.single_contact_cr,
            R.id.single_contact_rc, R.id.single_contact_pc, R.id.single_contact_ds, R.id.version_switch})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_contact_sc:
            case R.id.single_contact_cr:
            case R.id.single_contact_rc:
            case R.id.single_contact_pc:
            case R.id.single_contact_ds:
                recordView = v;
                click(v);
                break;
            case R.id.version_switch:
                if (versionCond == 1) {//回到旧版
                    versionHint.setText(getString(R.string.str_back_to_new_version));
                    versionCond = 0;
                } else {//回到新版
                    versionHint.setText(getString(R.string.str_back_to_old_version));
                    versionCond = 1;
                }
                switchQuestionBank();
                break;
            default:
                break;
        }
    }

    public int getVersionCond() {
        return versionCond;
    }

    private void switchQuestionBank() {
        click(recordView);
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
        ((BaseSinglePracticeFragment) fragList.get(currentPage)).asyncRequest();
        if (currentPage > 0) {
            ((BaseSinglePracticeFragment) fragList.get(currentPage - 1)).asyncRequest();
        }
        if (currentPage < fragList.size() - 1) {
            ((BaseSinglePracticeFragment) fragList.get(currentPage + 1)).asyncRequest();
        }
    }

    public int getSelectionId() {
        return selectionId;
    }

    @Override
    protected void setTabModeGravity(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
    }

    @Override
    protected void initView() {
        super.initView();
        initLayoutView(R.id.tabs, R.id.viewpager);
    }

    @Override
    protected PagerAdapter getPagerAdapter() {
        fragList = new ArrayList<>();
        fragList.add(new OGFragment());
        fragList.add(new PREPFragment());
        fragList.add(new LectureFragment());
        fragList.add(new GWDFragment());
        fragList.add(new Manhattan());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.single_contact_arr)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
