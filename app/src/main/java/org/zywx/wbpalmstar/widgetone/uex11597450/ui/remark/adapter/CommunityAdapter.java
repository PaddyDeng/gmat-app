package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.CommunityData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TestInfomationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;

import java.util.List;

public class CommunityAdapter extends BaseRecyclerViewAdapter<TestInfomationData> {

    public CommunityAdapter(Context context, List<TestInfomationData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
//        if (viewType == 100) {
//            return R.layout.community_one_item_layout;
//        }
        return R.layout.commuity_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
//        if (position == 0) {
//            return 100;
//        }
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, TestInfomationData itemData) {
        Context context = holder.itemView.getContext();
//        int type = getEveryItemViewType(position);

//        if (type == 100) {
//            //第一条
//            if (!TextUtils.isEmpty(itemData.getImage())) {
//                GlideUtil.loadDefault(itemData.getImage(), holder.getCircleImageView(R.id.community_head_iv), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
//            }
//            String name = itemData.getUsername();
//            if (!TextUtils.isEmpty(itemData.getNickname())) {
//                name = itemData.getNickname();
//            }
//            holder.getTextView(R.id.community_name_tv).setText(name);
//            holder.getTextView(R.id.community_one_title_tv).setText(itemData.getTitle());
//            holder.getTextView(R.id.community_one_post_time).setText(context.getString(R.string.str_community_post, itemData.getDateTime()));
//            holder.getTextView(R.id.community_one_right_tv).setText(itemData.getViewCount());
//        } else {
//            if (!TextUtils.isEmpty(itemData.getImage())) {
//                GlideUtil.loadDefault(itemData.getImage(), holder.getCircleImageView(R.id.community_item_head), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
//            }
//            String name = itemData.getUsername();
//            if (!TextUtils.isEmpty(itemData.getNickname())) {
//                name = itemData.getNickname();
//            }
        holder.getTextView(R.id.community_item_name).setText(itemData.getSource());
        holder.getTextView(R.id.community_item_title).setText(itemData.getContenttitle());

        holder.getTextView(R.id.community_item_post_time).setText(context.getString(R.string.str_community_post,
                TimeUtils.longToString(Long.parseLong(itemData.getContentinputtime()) * 1000, "yyyy-MM-dd")));
        holder.getTextView(R.id.community_item_right_view).setText(itemData.getViews());
//        }
    }
}
