package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.MsgData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import java.util.List;

public class MsgAdapter extends BaseRecyclerViewAdapter<MsgData> {

    public MsgAdapter(Context context, List<MsgData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.msg_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, MsgData itemData) {
        long aLong = Long.parseLong(itemData.getTime());
        String time = TimeUtils.longToString(aLong * 1000, "MM月dd日 HH:mm:ss");
        String[] timeList = time.split(" ");
        holder.getTextView(R.id.msg_date_tv).setText(timeList[0]);
        holder.getTextView(R.id.msg_time_tv).setText(timeList[1]);
        if (TextUtils.isEmpty(itemData.getTitle())) {
            holder.getTextView(R.id.msg_content_tv).setText(itemData.getContent());
        } else {
            holder.getTextView(R.id.msg_content_tv).setText(mContext.getString(R.string.str_msg_des, itemData.getTitle(), itemData.getContent()));
        }
    }

}
