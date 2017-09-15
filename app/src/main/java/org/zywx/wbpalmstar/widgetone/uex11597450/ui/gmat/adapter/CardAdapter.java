package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.PublicLessonData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.PreProGramLessonActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<PublicLessonData> mList;
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private OnItemClickListener mListener;

    public CardAdapter(List<PublicLessonData> mList) {
        this.mList = mList;
    }

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        ImageView view = holder.getImageView(R.id.gmat_public_lesson_img);
        final PublicLessonData data = mList.get(position);
        String url = RetrofitProvider.VIPLGW + data.getImage();
        GlideUtil.loadDefault(url, view, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        if (!data.isInit()) {
            String headUrl = RetrofitProvider.VIPLGW + data.getArticle();
            GlideUtil.loadDefault(headUrl, (ImageView) holder.getView(R.id.public_lesson_head), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
            holder.getTextView(R.id.public_lesson_title).setText(data.getName());
            holder.getTextView(R.id.public_lesson_name).setText(data.getListeningFile());
            holder.getTextView(R.id.public_lesson_num).setText(holder.itemView.getContext().getResources().getString(R.string.str_pub_lesson_num, data.getRandomNumber()));
        } else {
            Utils.setGone(holder.getTextView(R.id.public_lesson_title), holder.getTextView(R.id.public_lesson_name),
                    holder.getTextView(R.id.public_lesson_num), holder.getView(R.id.public_lesson_head));
        }

        holder.getTextView(R.id.immediately_sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null && !TextUtils.isEmpty(data.getId())) {
                    PreProGramLessonActivity.startPre(holder.itemView.getContext(), data.getId(), data.getName());
                }
            }
        });

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
        return mList.size();
    }


}
