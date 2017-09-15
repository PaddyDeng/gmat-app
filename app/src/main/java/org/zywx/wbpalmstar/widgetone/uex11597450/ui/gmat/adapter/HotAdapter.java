package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.NewHotData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.NewResultHot;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;

import java.util.List;

public class HotAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<NewHotData> lists;
    private OnItemClickListener mListener;
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    public HotAdapter(List<NewHotData> lists) {
        this.lists = lists;
    }

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_item_layout, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Context context = holder.itemView.getContext();
        NewHotData data = lists.get(position);
        NewResultHot result = data.getResult();
        holder.getTextView(R.id.hot_item_des).setText(result.getContenttitle());
        GlideUtil.loadDefault(RetrofitProvider.BASEURL + result.getAppImage(), holder.getImageView(R.id.hot_item_bg), false
                , DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
//        holder.getTextView(R.id.hot_item_number).setText(HtmlUtil.fromHtml(context.getString(R.string.str_hot_times, Integer.parseInt(result.getViews()))));
        holder.getTextView(R.id.hot_item_number).setText(context.getString(R.string.str_stydy_times, Integer.parseInt(result.getViews())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }
}
