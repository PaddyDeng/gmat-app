package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CollectionActivity extends BaseActivity {


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
    private List<TextView> txtList;
    private int[] selectIds;
    private int selectionId = C.SC;//默认选择sc
    private int currentId;


    private final static int CONTAINER = R.id.collection_fl;

    private SparseArray<Fragment> naviMapFragment = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        scTxt.setSelected(true);

        txtList = new ArrayList<>();
        txtList.add(scTxt);
        txtList.add(crTxt);
        txtList.add(rcTxt);
        txtList.add(pcTxt);
        txtList.add(dsTxt);
        selectIds = new int[]{C.SC, C.CR, C.RC, C.PS, C.DS};


    }

    @Override
    protected void initData() {
        super.initData();

        addFragment(new CollectionOGFragment(), R.id.collection_og);
        addFragment(new CollectionPrepFragment(), R.id.collection_prep);
        addFragment(new CollectionLectureFragment(), R.id.collection_lecture);
        addFragment(new CollectionGwdFragment(), R.id.collection_gwd);
        addFragment(new CollectionManhattanFragment(), R.id.collection_mhd);

        currentId = R.id.collection_og;
        replaceFragment(getSupportFragmentManager(), R.id.collection_og);
    }


    @OnClick({R.id.single_contact_sc, R.id.single_contact_cr, R.id.single_contact_rc, R.id.single_contact_pc, R.id.single_contact_ds,
            R.id.collection_og, R.id.collection_prep, R.id.collection_lecture, R.id.collection_gwd,R.id.collection_mhd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_contact_sc:
            case R.id.single_contact_cr:
            case R.id.single_contact_rc:
            case R.id.single_contact_pc:
            case R.id.single_contact_ds:
                click(v);
                break;
            case R.id.collection_og:
            case R.id.collection_prep:
            case R.id.collection_lecture:
            case R.id.collection_gwd:
            case R.id.collection_mhd:
                currentId = v.getId();
                switchFragment(v);
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
        int indexOf = txtList.indexOf(v);
        selectionId = selectIds[indexOf];
        ((BaseCollectionFragment) naviMapFragment.get(currentId)).asyncRequest();
    }


    private void switchFragment(View view) {
        int viewId = view.getId();
        if (naviMapFragment.indexOfKey(viewId) < 0) return;
        if (!view.isSelected()) {
            replaceFragment(getSupportFragmentManager(), viewId);
        }
    }

    private void replaceFragment(FragmentManager fm, int viewId) {
        String tag = String.valueOf(viewId);
        FragmentTransaction transaction = fm.beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            transaction.replace(CONTAINER, naviMapFragment.get(viewId), tag);
            transaction.addToBackStack(tag);
        } else {
            transaction.replace(CONTAINER, getSupportFragmentManager().findFragmentByTag(tag), tag);
        }
        transaction.commitAllowingStateLoss();
        //
        for (int i = 0, size = naviMapFragment.size(); i < size; i++) {
            int curId = naviMapFragment.keyAt(i);
            if (curId == viewId) {
                findViewById(viewId).setSelected(true);
            } else {
                findViewById(curId).setSelected(false);
            }
        }
    }

    private void addFragment(Fragment fragment, int id) {
        View view = findViewById(id);
        view.setSelected(false);
        naviMapFragment.put(id, fragment);
    }

    public int getSelectionId() {
        return selectionId;
    }

}
