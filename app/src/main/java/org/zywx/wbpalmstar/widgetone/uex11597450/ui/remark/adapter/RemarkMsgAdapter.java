package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import java.util.List;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils.split;

public class RemarkMsgAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<ReplyData> datas;

    private OnItemClickListener mListener;

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public RemarkMsgAdapter(List<ReplyData> datas) {
        this.datas = datas;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.remark_msg_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        ReplyData data = datas.get(position);
        String headUrl = RetrofitProvider.BASEURL + split(data.getUserImage());
        GlideUtil.loadDefault(headUrl, holder.getCircleImageView(R.id.reply_header), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        holder.getTextView(R.id.remark_new_msg_user_name).setText(data.getUserName());
        holder.getTextView(R.id.remark_new_msg_content).setText(data.getContent());
        holder.getTextView(R.id.remark_reply_com_tv).setText(data.getGossipContent());
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
        return datas.size();
    }
}
