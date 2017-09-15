package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyData;

import java.util.List;

public class CommDetailAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<ReplyData> mReplyDataList;

    public CommDetailAdapter(List<ReplyData> replyDataList) {
        mReplyDataList = replyDataList;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.comm_detail_re_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        ReplyData data = mReplyDataList.get(position);
        if (!TextUtils.isEmpty(data.getImage())) {
            GlideUtil.loadDefault(data.getImage(), holder.getCircleImageView(R.id.re_item_head_img), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        }
        String name = data.getUsername();
        if (!TextUtils.isEmpty(data.getNickname())) {
            name = data.getNickname();
        }
        holder.getTextView(R.id.remark_new_detail_item_user_name).setText(name);
        holder.getTextView(R.id.remark_new_detail_item_time).setText(TimeUtils.longToString(Long.parseLong(data.getCreateTime()) * 1000, "yyyy-MM-dd"));
        holder.getTextView(R.id.remark_new_detail_re_item_content).setText(data.getContent());

    }

    @Override
    public int getItemCount() {
        return mReplyDataList == null ? 0 : mReplyDataList.size();
    }
}
