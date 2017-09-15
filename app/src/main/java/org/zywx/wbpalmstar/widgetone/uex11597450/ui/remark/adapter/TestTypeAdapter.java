package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.OnAdapterListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.SimpleAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TestTypeData;

import java.util.List;

public class TestTypeAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<TestTypeData> mDatas;
    private OnAdapterListener<TestTypeData> mOnItemClickListener;

    public TestTypeAdapter(List<TestTypeData> datas) {
        mDatas = datas;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_type_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        Context context = holder.itemView.getContext();
        TextView desTv = holder.getTextView(R.id.test_type_item_tv);
        final TestTypeData data = mDatas.get(position);
        desTv.setText(data.getDes());
        int drawable1 = context.getResources().getIdentifier(data.getDrawableName(), "drawable", context.getPackageName());
        Drawable drawable = ContextCompat.getDrawable(context, drawable1);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        desTv.setCompoundDrawables(null, drawable, null, null);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(v, position, data);
                }
            }
        });
    }

    public void setOnItemClickListener(SimpleAdapter<TestTypeData> itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
