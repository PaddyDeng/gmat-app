package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseSwipeRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.RecycleViewLinearDivider;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.CommunityData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TestInfomationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.CommunityReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.CommunityDetailActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.CommunityAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class TestInformationFragment extends BaseRefreshFragment<TestInfomationData> {

    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
    private int page;
//    private boolean viewCreate;
    private Observable<Boolean> refreshObs;

//    private CommunityReceiver mReceiver = new CommunityReceiver() {
//        @Override
//        protected void performAction(Intent intent) {
//            if (viewCreate) {
//                asyncRequest();
//            }
//        }
//    };

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mReceiver.getIntentFilter());
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        viewCreate = true;
        refreshObs = RxBus.get().register(C.POST_COMMUNITY_REMARK, Boolean.class);
        refreshObs.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                asyncRequest();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        viewCreate = false;
        if (refreshObs != null)
            RxBus.get().unregister(C.POST_COMMUNITY_REMARK, refreshObs);
    }

    @Override
    public void asyncLoadMore() {
        ++page;
        asyncData(false);
    }

    @Override
    public BaseRecyclerViewAdapter<TestInfomationData> getAdapter() {
        return new CommunityAdapter(getActivity(), null, mManager);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    public void asyncRequest() {
        page = 1;
        asyncData(true);
    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
    }

    private void asyncData(final boolean refresh) {
        addToCompositeDis(HttpUtil.getTestInfo(String.valueOf(page))
                .subscribe(new Consumer<List<TestInfomationData>>() {
                    @Override
                    public void accept(@NonNull List<TestInfomationData> datas) throws Exception {
                        if (datas != null && !datas.isEmpty()) {
                            if (refresh) {
                                updateRecycleView(datas, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(datas, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            asyncFail(refresh, "");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        asyncFail(refresh, errorTip(throwable));
                    }
                }));
    }

    @Override
    public void setListener(List<TestInfomationData> data, int position) {
        super.setListener(data, position);
        TestInfomationData data1 = data.get(position);
        CommunityDetailActivity.startCommunity(getActivity(), data1.getContentid());
    }

    private void asyncFail(boolean refresh, String msg) {
        if (refresh) {
            updateRecycleView(null, msg, InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, msg, InitDataType.TYPE_LOAD_MORE_FAIL);
        }
    }
}
