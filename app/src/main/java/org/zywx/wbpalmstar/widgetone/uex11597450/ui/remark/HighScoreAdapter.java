package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreItemData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;

import java.util.List;

/**
 * Created by fire on 2017/8/28  09:53.
 */

public class HighScoreAdapter extends BaseRecyclerViewAdapter<HighScoreItemData> {

    public HighScoreAdapter(Context context, List<HighScoreItemData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        if (viewType == 111) {
            return R.layout.one_img_layout;
        }
        return R.layout.high_score_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        if (position == 0) {
            return 111;
        }
        return 100;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, HighScoreItemData itemData) {
        if (position == 0) {
        } else {
            Object image1 = itemData.getImage1();
            if (image1 instanceof String) {
                GlideUtil.load(RetrofitProvider.BASEURL + image1, holder.getImageView(R.id.high_score_item_iv));
            }
            holder.getTextView(R.id.high_score_item_title).setText(itemData.getContenttitle());
            holder.getTextView(R.id.high_score_user_name).setText(mContext.getString(R.string.str_high_score_user_name, itemData.getFullName()));
            holder.getTextView(R.id.high_score_item_out_time).setText(mContext.getString(R.string.str_high_score_out_time, itemData.getOutTime()));
        }
    }

}
