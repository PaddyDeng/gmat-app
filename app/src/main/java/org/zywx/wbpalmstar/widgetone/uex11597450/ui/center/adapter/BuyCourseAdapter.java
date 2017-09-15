package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;

import java.util.List;

public class BuyCourseAdapter extends BaseRecyclerViewAdapter<Object> {

    public BuyCourseAdapter(Context context, List<Object> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return 0;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, Object itemData) {

    }
}
