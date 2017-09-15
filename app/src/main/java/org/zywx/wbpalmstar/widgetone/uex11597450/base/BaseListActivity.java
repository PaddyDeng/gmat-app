package org.zywx.wbpalmstar.widgetone.uex11597450.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.persenter.BaseListPresenter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.persenter.BaseListPresenterImpl;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.view.BaseListView;

import java.util.List;

import butterknife.BindView;

public abstract class BaseListActivity<T> extends BaseActivity implements BaseListView<T> {

    @BindView(R.id.base_list_content_title)
    protected TextView titleTxt;
    @BindView(R.id.base_list_swipe)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.base_list_recycler)
    RecyclerView mRecyclerView;
    protected BaseListPresenter<T> mListPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list_layout);
    }

    @Override
    protected void initData() {
        super.initData();
        mListPresenter = new BaseListPresenterImpl(this);
        mListPresenter.view(mSwipeRefreshLayout, mRecyclerView);
    }

    @Override
    protected void asyncUiInfo() {
        if (mListPresenter != null)
            mListPresenter.initSwipeView();
    }

    @Override
    public boolean autoAsyncData() {
        return true;
    }

    @Override
    public boolean canAutoLoadMore() {
        return true;
    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {

    }

    protected void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type) {
        if (mListPresenter != null)
            mListPresenter.updateRecycleView(list, msg, type);
    }

    protected void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustShowNull) {
        if (mListPresenter != null)
            mListPresenter.updateRecycleView(list, msg, type, mustShowNull);
    }

    @Override
    public void setListener(List<T> data, int position) {

    }

    @Override
    public void asyncRequestBefore() {

    }
}
