package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.community;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.DownloadAdapter;

/**
 * 机经下载
 */
public class MachineDownloadFragment extends BaseMaterialFragment {
    private int page;

    @Override
    protected void asyncFail() {
        page--;
        page = page < 1 ? 1 : page;
    }

    @Override
    public String getSelectId() {
        return "8";
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
