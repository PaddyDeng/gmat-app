package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.community;

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
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.DownloadData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.DownloadAdapter;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public abstract class BaseMaterialFragment extends BaseRefreshFragment<DownloadData> {

    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());

    protected void asyncData(final boolean refresh, int page) {
        addToCompositeDis(HttpUtil.getPostTestList(getSelectId(), String.valueOf(page))
                .subscribe(new Consumer<ResultBean<List<DownloadData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<DownloadData>> bean) throws Exception {
                        List<DownloadData> data = bean.getData();
                        if (data != null && !data.isEmpty()) {
                            if (refresh) {
                                updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            asyncFail(refresh, bean.getMessage(), true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        asyncFail(refresh, errorTip(throwable), false);
                    }
                }));
    }

    private void asyncFail(boolean refresh, String msg, boolean loadBoo) {
        if (refresh) {
            updateRecycleView(null, msg, loadBoo ? InitDataType.TYPE_REFRESH_SUCCESS : InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, msg, loadBoo ? InitDataType.TYPE_LOAD_MORE_SUCCESS : InitDataType.TYPE_LOAD_MORE_FAIL);
        }
        if (!loadBoo) {
            asyncFail();
        }
    }

    protected abstract void asyncFail();

    @Override
    public BaseRecyclerViewAdapter<DownloadData> getAdapter() {
        return new DownloadAdapter(getActivity(), null, mManager);
    }

    @Override
    public void setListener(List<DownloadData> data, int position) {
        super.setListener(data, position);
        DownloadData downloadData = data.get(position);
        if (downloadData != null) {
            DownloadRichTextActivity.startCommunity(getActivity(), downloadData.getId());
        }
    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_height_divider));
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    public abstract String getSelectId();
}
