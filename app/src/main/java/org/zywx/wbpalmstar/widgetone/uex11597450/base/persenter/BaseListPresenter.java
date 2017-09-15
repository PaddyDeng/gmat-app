package org.zywx.wbpalmstar.widgetone.uex11597450.base.persenter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.pullrefresh.PullRefreshLayout;

import java.util.List;

/**
 * Created by fire on 2017/8/21  10:27.
 */

public interface BaseListPresenter<T> {

    void view(PullRefreshLayout mPullRefreshLayout, RecyclerView mRecyclerView);

    void view(SwipeRefreshLayout mSwipeRefreshLayout, RecyclerView mRecyclerView);

    void initView();

    void initSwipeView();

    void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type);

    void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustShowNull);

    boolean autoAsyncData();
}
