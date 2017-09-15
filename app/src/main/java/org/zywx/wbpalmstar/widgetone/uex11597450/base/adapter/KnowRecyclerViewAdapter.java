package org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.ControlTextView;

import java.util.List;

import static org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowData.CategoryTypeBean.CategoryContentBean;

public class KnowRecyclerViewAdapter extends RecyclerView.Adapter<KnowHolder> {

    private List<CategoryContentBean> mContentBeens;
    private Context mContext;
    private int fontSize;

    public KnowRecyclerViewAdapter(List<CategoryContentBean> contentBeens, Context context) {
        mContentBeens = contentBeens;
        mContext = context;
        fontSize = SharedPref.getFontSize(context);
    }

    @Override
    public KnowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        KnowHolder holder = new KnowHolder(LayoutInflater.from(mContext).inflate(R.layout.act_know_list_layout, parent, false));
        holder.setClick(mRecyClick);
        return holder;
    }

    private RecyClick mRecyClick;

    public void setonClickListener(RecyClick mClick) {
        mRecyClick = mClick;
    }

    @Override
    public void onBindViewHolder(KnowHolder holder, int position) {
        ControlTextView tv = holder.findViewById(R.id.know_item_content_tv);
        tv.setText(mContentBeens.get(position).getContenttitle());
        tv.setFontSize(fontSize);
    }

    @Override
    public int getItemCount() {
        return mContentBeens.size();
    }

    public interface RecyClick {
        void OnClick(View view, int position);
    }
}
