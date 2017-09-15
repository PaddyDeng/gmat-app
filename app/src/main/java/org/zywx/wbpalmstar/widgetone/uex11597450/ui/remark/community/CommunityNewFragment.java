package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.community;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.SimpleAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TestTypeData;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.CommunityDetailActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.TestTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CommunityNewFragment extends BaseFragment {

    @BindView(R.id.fixed_community_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.fixed_community_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.test_type_recycler)
    RecyclerView mRecyclerView;
    private List<Fragment> list;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_new_community, container, false);
    }


    @Override
    protected void initWhenRootViewNull(Bundle savedInstanceState) {
        initTabAdapter(getPagerAdapter(), mViewPager, mTabLayout);
        TabLayout.Tab one = mTabLayout.getTabAt(0);
        one.setCustomView(R.layout.comm_one_tab_layout);
        TabLayout.Tab two = mTabLayout.getTabAt(1);
        two.setCustomView(R.layout.comm_two_tab_layout);
        TabLayout.Tab thr = mTabLayout.getTabAt(2);
        thr.setCustomView(R.layout.comm_thr_tab_layout);

        initRecycle();
    }

    private void initRecycle() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 20);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position < 5) {
                    return 4;
                }
                return 5;
            }
        });
        manager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        String[] array = getResources().getStringArray(R.array.test_type_drawable_name);
        String[] arrays = getResources().getStringArray(R.array.test_type_arr);
        TestTypeAdapter adapter = new TestTypeAdapter(initTestType(array, arrays));
        adapter.setOnItemClickListener(new SimpleAdapter<TestTypeData>() {
            @Override
            public void onClick(View view, int position, TestTypeData data) {
//                点击事件
//                click(data);
                CommunityDetailActivity.startCommunity(getActivity(), data.getId());
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

//    private void click(TestTypeData data) {
//        switch (data.getFlag()) {
//            case TestTypeData.TIME://考试时间
//                break;
//            case TestTypeData.ADDRESS://考试地址
//                break;
//            case TestTypeData.CONTENT://考试内容
//                break;
//            case TestTypeData.PROCESS://报名流程
//                break;
//            case TestTypeData.PRICE://考试费用
//                break;
//            case TestTypeData.REVIEW://成绩复议
//                break;
//            case TestTypeData.CARD://考试证件
//                break;
//            case TestTypeData.TAKE://转考退考
//                break;
//            case TestTypeData.WAY://送分方式
//                break;
//            default:
//                break;
//        }
//    }

    private List<TestTypeData> initTestType(String[] names, String[] arrays) {
        List<TestTypeData> mDatas = new ArrayList<>();
        for (int i = 0, size = names.length; i < size; i++) {
            TestTypeData data = new TestTypeData();
            data.setDrawableName(names[i]);
            String des = arrays[i];
            data.setDes(des);
            if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_time))) {
//                data.setFlag(TestTypeData.TIME);
                data.setId("32");
            } else if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_address))) {
//                data.setFlag(TestTypeData.ADDRESS);
                data.setId("41");
            } else if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_content))) {
//                data.setFlag(TestTypeData.CONTENT);
                data.setId("42");
            } else if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_registration_process))) {
//                data.setFlag(TestTypeData.PROCESS);
                data.setId("43");
            } else if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_price))) {
//                data.setFlag(TestTypeData.PRICE);
                data.setId("44");
            } else if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_review))) {
//                data.setFlag(TestTypeData.REVIEW);
                data.setId("45");
            } else if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_card))) {
//                data.setFlag(TestTypeData.CARD);
                data.setId("46");
            } else if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_take))) {
//                data.setFlag(TestTypeData.TAKE);
                data.setId("47");
            } else if (TextUtils.equals(des, getString(R.string.str_test_infomation_test_send_point_way))) {
//                data.setFlag(TestTypeData.WAY);
                data.setId("48");
            }
            mDatas.add(data);
        }
        return mDatas;
    }


    public PagerAdapter getPagerAdapter() {
        list = new ArrayList<>();
        list.add( new TestInformationFragment());
        list.add(new MachineDownloadFragment());
        list.add(new MaterialFragment());
        return new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
    }


}
