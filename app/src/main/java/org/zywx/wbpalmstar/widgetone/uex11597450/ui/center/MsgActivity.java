package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseListActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter.MsgAdapter;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MsgActivity extends BaseListActivity {


    private LinearLayoutManager manager = new LinearLayoutManager(mContext);
    private int pagerNum = 1;

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        return new MsgAdapter(mContext, null, manager);
    }

    @Override
    protected void initView() {
        super.initView();
        titleTxt.setText(getString(R.string.str_main_center_msg));
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return manager;
    }


    private void asyncRequest(String pn, final boolean isRefresh) {
        HttpUtil.getMsg(pn)
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            List data = bean.getMessageslist();
                            if (isRefresh) {
                                if (data != null && !data.isEmpty()) {
                                    updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                                } else {
                                    updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_REFRESH_FAIL);
                                }
                            } else {
                                if (data != null && !data.isEmpty()) {
                                    updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                                } else {
                                    updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_LOAD_MORE_FAIL);
                                }
                            }
                        } else {
                            if (isRefresh) {
                                updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_REFRESH_FAIL);
                            } else {
                                updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    @Override
    protected void asyncUiInfo() {
        pagerNum = 1;
        asyncRequest(String.valueOf(pagerNum), true);
    }

    @Override
    public void asyncRequest() {
        pagerNum = 1;
        asyncRequest(String.valueOf(pagerNum), true);
    }

    @Override
    public void asyncLoadMore() {
        asyncRequest(String.valueOf(++pagerNum), false);
    }

}
