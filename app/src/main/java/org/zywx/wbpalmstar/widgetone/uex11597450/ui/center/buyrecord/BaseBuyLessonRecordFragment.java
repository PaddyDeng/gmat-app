package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.buyrecord;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter.BuyCourseAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


public abstract class BaseBuyLessonRecordFragment extends BaseRefreshFragment<Object> {

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(getActivity());
    private int pagerNumber = 1;

    @Override
    public void asyncLoadMore() {
        asyncData(++pagerNumber, false);
    }

    @Override
    public BaseRecyclerViewAdapter<Object> getAdapter() {
        return new BuyCourseAdapter(getActivity(), null, mManager);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    public void asyncRequest() {
        pagerNumber = 1;
        asyncData(pagerNumber, true);
    }

    private void asyncData(int pagerNumber, boolean refresh) {
        addToCompositeDis(HttpUtil.getBuyCourse(getCatid(), String.valueOf(pagerNumber))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
//                        updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_LOAD_MORE_FAIL);
                        updateRecycleView(null, getString(R.string.str_buy_tip), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRecycleView(null, getString(R.string.str_buy_tip), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
    }

    protected abstract String getCatid();
}
