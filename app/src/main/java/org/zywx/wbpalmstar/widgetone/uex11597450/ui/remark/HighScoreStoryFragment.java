package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.support.annotation.StringDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.RecycleViewLinearDivider;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreItemData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/28  09:26.
 * 高分故事
 */
public class HighScoreStoryFragment extends BaseRefreshFragment<HighScoreItemData> {

    private int page;
    private boolean needHeaderData = true;
    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(getActivity());

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        super.initRecyclerViewItemDecoration(mRecyclerView);
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_height_divider));
    }

    @Override
    public BaseRecyclerViewAdapter<HighScoreItemData> getAdapter() {
        return new HighScoreAdapter(getActivity(), null, mManager);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    public void asyncLoadMore() {
        needHeaderData = false;
        asyncData(++page, false);
    }

    @Override
    public void asyncRequest() {
        needHeaderData = true;
        asyncData(page = 1, true);
    }

    private void asyncData(final int requestPage, final boolean isRefresh) {
        addToCompositeDis(HttpUtil
                .highScoreList(requestPage)
                .subscribe(new Consumer<HighScoreData>() {
                    @Override
                    public void accept(@NonNull HighScoreData data) throws Exception {
                        if (getHttpResSuc(data.getCode())) {
                            List<HighScoreItemData> lidata = data.getLidata();
                            if (lidata != null && !lidata.isEmpty() && needHeaderData) {
                                lidata.add(0, new HighScoreItemData());
                            }
                            if (isRefresh) {
                                updateRecycleView(lidata, data.getMessage(), InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(lidata, data.getMessage(), InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            refreshFail(data.getMessage(), isRefresh);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        refreshFail(errorTip(throwable), isRefresh);
                        if (page != 1) {
                            page--;
                        }
                    }
                }));
    }

    @Override
    public void setListener(List<HighScoreItemData> data, int position) {
        super.setListener(data, position);
        if (position == 0) return;
        HighScoreDetailActivity.startHighScoreDetailAct(getActivity(), data.get(position).getContentid());
    }

    private void refreshFail(String msg, boolean isRefresh) {
        if (isRefresh) {
            updateRecycleView(null, msg, InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, msg, InitDataType.TYPE_LOAD_MORE_FAIL);
        }
    }

}
