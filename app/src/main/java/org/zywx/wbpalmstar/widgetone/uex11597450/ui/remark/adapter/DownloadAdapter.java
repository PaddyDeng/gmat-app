package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.download.DownloadData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;

import java.util.List;

public class DownloadAdapter extends BaseRecyclerViewAdapter<DownloadData> {

    public DownloadAdapter(Context context, List<DownloadData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.down_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, DownloadData itemData) {
        holder.getTextView(R.id.download_item_title_tv).setText(itemData.getTitle());
        String name = itemData.getUsername();
        if (!TextUtils.isEmpty(itemData.getNickname())) {
            name = itemData.getNickname();
        }
        holder.getTextView(R.id.post_up_person_tv).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_post_person, name)));
        holder.getTextView(R.id.see_person_num_tv).setText(itemData.getViewCount());
        holder.getTextView(R.id.up_time_tv).setText(itemData.getDateTime());
        holder.getTextView(R.id.down_item_des).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_item_download_person_num, itemData.getViewCount())));
    }
}
