package org.zywx.wbpalmstar.widgetone.uex11597450.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.persenter.BaseListPresenter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.persenter.BaseListPresenterImpl;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.view.BaseListView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.pullrefresh.PullRefreshLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by fire on 2017/8/21  09:28.
 */

public abstract class BaseListPullActivity<T> extends BaseActivity implements BaseListView<T> {

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    protected PullRefreshLayout mPullRefreshLayout;
    @BindView(R.id.base_refresh_title_container)
    protected RelativeLayout titleContainer;
    @BindView(R.id.title_centertxt)
    protected TextView titleTxt;
    @BindView(R.id.discussion_container)
    protected RelativeLayout mContainer;
    @BindView(R.id.editTextBodyLl)
    protected LinearLayout etContainer;
    @BindView(R.id.circleEt)
    protected EditText remarkEt;
    @BindView(R.id.sendIv)
    protected ImageView sendPost;

    protected BaseListPresenter<T> mListPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list_pull);
    }

    @Override
    protected void initData() {
        super.initData();
        mListPresenter = new BaseListPresenterImpl(this);
        mListPresenter.view(mPullRefreshLayout, mRecyclerView);
    }

    @Override
    protected void initView() {
        if (mListPresenter != null)
            mListPresenter.initView();
    }

    protected boolean isAuto() {
        return true;
    }

    @Override
    public boolean autoAsyncData() {
        return isAuto();
    }

    @Override
    public boolean canAutoLoadMore() {
        return true;
    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {

    }

    @Override
    public void setListener(List<T> data, int position) {

    }

    @Override
    public void asyncRequestBefore() {

    }

    public void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type) {
        if (mListPresenter != null)
            mListPresenter.updateRecycleView(list, msg, type);
    }

    public void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustShowNull) {
        if (mListPresenter != null)
            mListPresenter.updateRecycleView(list, msg, type, mustShowNull);
    }

    protected void refreshUi(List<T> data, boolean refresh) {
        if (data == null || data.isEmpty()) {
            if (refresh) {
                updateRecycleView(null, getString(R.string.str_no_data_tip), InitDataType.TYPE_REFRESH_FAIL);
            } else {
                updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
            }
        } else {
            if (refresh) {
                updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
            } else {
                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
            }
        }
    }

    protected void refreshFail(Throwable throwable, boolean isRefresh) {
        refreshShowFail(throwable, isRefresh, false);
    }

    protected void refreshShowFail(Throwable throwable, boolean isRefresh, boolean showFailLastRow) {
        if (isRefresh) {
            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, throwMsg(throwable), showFailLastRow ? InitDataType.TYPE_LOAD_MORE_FAIL : InitDataType.TYPE_LOAD_MORE_SUCCESS);
        }
    }
}
