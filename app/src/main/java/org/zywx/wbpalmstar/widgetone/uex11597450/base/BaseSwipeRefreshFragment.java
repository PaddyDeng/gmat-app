package org.zywx.wbpalmstar.widgetone.uex11597450.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnLoadMoreListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SwipeInit;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseSwipeRefreshFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    //    @BindView(R.id.swipe_refresh_layout)
//    protected PullRefreshLayout mPullRefreshLayout;
    protected BaseRecyclerViewAdapter<T> adapter;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_swipe_refresh_layout, container, false);
        ButterKnife.bind(this, view);
//        mPullRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        SwipeInit.setRefreshColor(mSwipeRefreshLayout);

        if (adapter == null) {
            initNewsList();
        }
        adapter.onShowInitView(true);
        asyncRequest();
//        mSwipeRefreshLayout.setRefreshing(true);
        return view;
    }


    public void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type) {
        updateRecycleView(list, msg, type, false);
    }

    public void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustShowNull) {
        mSwipeRefreshLayout.setRefreshing(false);
//        mPullRefreshLayout.finishRefresh();
        adapter.onShowInitView(false);
        adapter.onShowEmptyView(false, msg);
        switch (type) {
            case InitDataType.TYPE_REFRESH_SUCCESS:
                if (mustShowNull && (list == null || list.isEmpty())) {
                    adapter.updateNull();
                    adapter.onShowEmptyView(true, msg);
                } else {
                    adapter.update(list);
                }
                break;
            case InitDataType.TYPE_LOAD_MORE_SUCCESS:
//                    adapter.setShowFooter(false);
                adapter.onLoadMoreSuccess();
                adapter.addTail(list);
                break;
            case InitDataType.TYPE_REFRESH_FAIL:
//                if (adapter.getItemCount() == 0) {
//                    //显示空布局
//                    adapter.onShowEmptyView(true, msg);
//                }
                if (adapter.getAdapterData() == null || adapter.getAdapterData().isEmpty()) {
                    adapter.onShowEmptyView(true, msg);
                    adapter.notifyItemChanged(0);
                }
                break;
            case InitDataType.TYPE_LOAD_MORE_FAIL:
                adapter.onLoadMoreFail(msg);
                break;
        }
    }


    private void initNewsList() {
        adapter = getAdapter();

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) mRecyclerView.getAdapter();
                List data = adapter.getAdapterData();
                if (data != null && !data.isEmpty()) {
                    setListener(data, position);
                }
            }
        });

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //加载更多
                asyncLoadMore();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(getLayoutManager());
        initRecyclerViewItemDecoration(mRecyclerView);
//        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(adapter);

    }

    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {

    }

    /**
     * item listener
     */
    protected void setListener(List<T> data, int position) {
    }

    protected abstract void asyncLoadMore();

    protected abstract BaseRecyclerViewAdapter<T> getAdapter();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    /**
     * 初始化数据
     */
    protected abstract void asyncRequest();

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        asyncRequest();
    }

}