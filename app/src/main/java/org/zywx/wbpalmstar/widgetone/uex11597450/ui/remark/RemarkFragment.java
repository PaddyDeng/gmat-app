package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnLoadMoreListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.RemarkRefreshReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.RemarkAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SwipeInit;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.pullrefresh.PullRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 初次进入界面不刷新时，设置adapter的数据源为null，之后在刷新数据。
 */
public class RemarkFragment extends BaseRefreshFragment<RemarkData> {

    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    protected int page = 1;
    private boolean viewCreate;
    private boolean pushComeOn;//推送来了。或者发表了八卦。或者在评论详情页评论。,需要刷新
    private boolean downRefresh;

    @Override
    public void asyncLoadMore() {
        page++;
        netRequest(page, false);
    }


    /**
     * @param page      页码
     * @param isRefresh true 是下拉刷新
     */
    public void netRequest(int page, final boolean isRefresh) {
        addToCompositeDis(HttpUtil.getRemarkList(String.valueOf(page)).subscribe(new Consumer<ResultBean<RemarkBean>>() {
            @Override
            public void accept(@NonNull ResultBean<RemarkBean> bean) throws Exception {
                List<RemarkData> data = bean.getData().getData();
                if (bean.getData() != null && !data.isEmpty()) {
                    if (isRefresh && !TextUtils.isEmpty(bean.getNum()) && !TextUtils.equals(bean.getNum(), "0")) {
                        RemarkData remarkData = new RemarkData();
                        remarkData.setRemarkNum(bean.getNum());
                        data.add(0, remarkData);
                    }
                    if (isRefresh) {
                        updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                    } else {
                        updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                    }
                } else {
                    refreshRecycleFail(isRefresh, bean.getMessage());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                refreshRecycleFail(isRefresh, errorTip(throwable));
            }
        }));
    }

    private void refreshRecycleFail(boolean isRefresh, String msg) {
        if (isRefresh) {
            updateRecycleView(null, msg, InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, msg, InitDataType.TYPE_LOAD_MORE_FAIL);
        }
    }


    @Override
    public BaseRecyclerViewAdapter<RemarkData> getAdapter() {
        return new RemarkAdapter(getActivity(), null, manager);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return manager;
    }

    @Override
    public void asyncRequest() {
        if (pushComeOn || downRefresh || getAdapter() == null || getAdapter().getAdapterData().isEmpty()) {
            pushComeOn = false;
            downRefresh = false;
            page = 1;
            netRequest(page, true);
        }
    }

    @Override
    public void setListener(List<RemarkData> data, int position) {
        super.setListener(data, position);
        RemarkData bean = data.get(position);
        if (TextUtils.isEmpty(bean.getRemarkNum())) {//不是新消息
            RemarkDetailActivity.startRemarkDetail(getActivity(), bean.getId(), C.COM_REQUEST_CODE);
        }
//        startActivity(new Intent(getActivity(), RemarkNewMsgActivity.class));
//        通过fragment的startActivityForResult调用
    }

    private RemarkRefreshReceiver mReceiver = new RemarkRefreshReceiver() {
        @Override
        protected void performAction(Intent intent) {
            if (viewCreate) {
                pushComeOn = true;
                //刷新列表
                asyncRequest();
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mReceiver.getIntentFilter());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }


    //    @Override
//    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.frag_remark_layout, container, false);
//        ButterKnife.bind(this, view);
//        mPullRefreshLayout.setOnRefreshListener(this);
////        mSwipeRefreshLayout.setOnRefreshListener(this);
////        SwipeInit.setRefreshColor(mSwipeRefreshLayout);
////        mSwipeRefreshLayout.setRefreshing(true);
//        return view;
//    }
//
    public LinearLayoutManager getManager() {
        return manager;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewCreate = true;
//        asyncRequest();
    }


    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity.getEtContainerStatus()) {
                    mainActivity.showOrHideEt(View.GONE, null, 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void asyncRequestBefore() {
        downRefresh = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreate = false;
    }

}
