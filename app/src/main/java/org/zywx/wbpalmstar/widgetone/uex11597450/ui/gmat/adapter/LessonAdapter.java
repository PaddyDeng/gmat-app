package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.HotResultData;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<HotResultData> mList;
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private OnItemClickListener mListener;

    public LessonAdapter(List<HotResultData> list) {
        mList = list;
    }

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public void updataList(List<HotResultData> list) {
        if (list == null || list.isEmpty()) return;
//        mList.clear();
//        mList.addAll(list);
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamt_lesson_item_layout, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        Context context = holder.itemView.getContext();
        HotResultData data = mList.get(position);
        GlideUtil.loadDefault(RetrofitProvider.BASEURL + data.getAppImage(), holder.getImageView(R.id.lesson_item_img), false
                , DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        holder.getTextView(R.id.lesson_item_title).setText(data.getContenttitle());
        if (TextUtils.isEmpty(data.getTime())) {
            holder.getTextView(R.id.lesson_item_start_time).setText(context.getString(R.string.str_lesson_every_open));
        } else {
            holder.getTextView(R.id.lesson_item_start_time).setText(context.getString(R.string.str_lesson_open_time, data.getTime()));
        }
        if (!TextUtils.isEmpty(data.getHour())) {//直播课与视频课区分
            holder.getTextView(R.id.lesson_item_lesson_time).setText(context.getString(R.string.str_lesson_course_time, data.getHour()));//直播课
        } else {
            holder.getTextView(R.id.lesson_item_lesson_time).setText(context.getString(R.string.str_lesson_time, data.getTimes()));
        }
        holder.getTextView(R.id.lesson_item_price).setText(context.getString(R.string.str_lesson_price, data.getPrice()));
        holder.getTextView(R.id.lesson_item_search_num).setText(context.getString(R.string.str_lesson_search_num, data.getViews()));
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
        return mList == null ? 0 : mList.size();
    }
}