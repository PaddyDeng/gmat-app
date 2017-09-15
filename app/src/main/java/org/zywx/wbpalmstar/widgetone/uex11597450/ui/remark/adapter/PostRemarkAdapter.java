package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.caimuhao.rxpicker.bean.ImageItem;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.io.File;
import java.util.List;

public class PostRemarkAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<ImageItem> mDatas;
    private OnItemClickListener choosePicListener;
    private OnItemClickListener itemClickListener;

    public PostRemarkAdapter(List<ImageItem> mDatas) {
        this.mDatas = mDatas;
    }

    public void setDatas(List<ImageItem> lists) {
        if (lists == null || lists.isEmpty()) return;
        mDatas = lists;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.post_remark_img_item_layout, parent, false));
    }

    public void setChoosePicListener(OnItemClickListener choosePicListener) {
        this.choosePicListener = choosePicListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        final ImageItem item = mDatas.get(position);
        if (!TextUtils.isEmpty(item.getPath())) {
            File file = new File(item.getPath());
            Glide.with(holder.itemView.getContext()).load(file).asBitmap().into(holder.getImageView(R.id.post_remark_iv));
            holder.getImageView(R.id.post_remark_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(v, position);
                    }
                }
            });
        } else {
            holder.getImageView(R.id.post_remark_iv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.logh("==", "click");
                    if (choosePicListener != null && position == (mDatas.size() - 1)) {
                        choosePicListener.onClick(v, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
