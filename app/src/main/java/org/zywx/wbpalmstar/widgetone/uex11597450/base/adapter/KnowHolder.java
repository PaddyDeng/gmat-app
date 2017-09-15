package org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class KnowHolder extends RecyclerView.ViewHolder {

    protected SparseArray<View> mViews;
    private KnowRecyclerViewAdapter.RecyClick mRecyClick;
    private View itemView;

    public void setClick(final KnowRecyclerViewAdapter.RecyClick mClick) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClick != null) {
                    mClick.OnClick(v, getAdapterPosition());
                }
            }
        });
    }

    public KnowHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        mViews = new SparseArray<>();
    }

    public <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
}
