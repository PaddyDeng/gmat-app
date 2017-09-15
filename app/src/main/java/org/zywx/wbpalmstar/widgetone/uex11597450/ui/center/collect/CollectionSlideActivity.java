package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.collect;

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

public class CollectionSlideActivity extends BaseTabActivity {

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
    @BindView(R.id.contact_or_collection_title_tv)
    TextView titleTv;
    private List<Fragment> fragList;
    private List<TextView> txtList;
    private int[] selectIds;
    private int selectionId = C.SC;//默认选择sc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        titleTv.setText(getString(R.string.str_main_center_my_keep));
        scTxt.setSelected(true);
        txtList = new ArrayList<>();
        txtList.add(scTxt);
        txtList.add(crTxt);
        txtList.add(rcTxt);
        txtList.add(pcTxt);
        txtList.add(dsTxt);
        selectIds = new int[]{C.SC, C.CR, C.RC, C.PS, C.DS};
    }

    @OnClick({R.id.single_contact_sc, R.id.single_contact_cr, R.id.single_contact_rc, R.id.single_contact_pc, R.id.single_contact_ds,})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_contact_sc:
            case R.id.single_contact_cr:
            case R.id.single_contact_rc:
            case R.id.single_contact_pc:
            case R.id.single_contact_ds:
                click(v);
                break;
            default:
                break;
        }
    }

    private void click(View v) {
//        if (v == null || fragList == null || fragList.isEmpty()) {
//            return;
//        }
        for (TextView tv : txtList) {
            if (tv == v) {
                tv.setSelected(true);
            } else {
                tv.setSelected(false);
            }
        }
        selectionId = selectIds[txtList.indexOf(v)];
        ((BaseCollectionFragment) fragList.get(currentPage)).asyncRequest();
        if (currentPage > 0) {
            ((BaseCollectionFragment) fragList.get(currentPage - 1)).asyncRequest();
        }
        if (currentPage < fragList.size() - 1) {
            ((BaseCollectionFragment) fragList.get(currentPage + 1)).asyncRequest();
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
        fragList.add(new CollectionOGFragment());
        fragList.add(new CollectionPrepFragment());
        fragList.add(new CollectionLectureFragment());
        fragList.add(new CollectionGwdFragment());
        return new TabPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.single_contact_arr)) {
            @Override
            public Fragment getItem(int position) {
                return fragList.get(position);
            }
        };
    }
}
