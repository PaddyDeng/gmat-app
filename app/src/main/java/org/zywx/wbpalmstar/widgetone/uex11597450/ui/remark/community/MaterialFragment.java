package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.community;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.CommunityData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.DownloadAdapter;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 材料下载
 */
public class MaterialFragment extends BaseMaterialFragment {

    private int page;

    @Override
    protected void asyncFail() {
        page--;
        page = page < 1 ? 1 : page;
    }

    @Override
    public String getSelectId() {
        return "3";
    }

    @Override
    public void asyncLoadMore() {
        ++page;
        asyncData(false, page);
    }

    @Override
    public void asyncRequest() {
        page = 1;
        asyncData(true, page);
    }
}
