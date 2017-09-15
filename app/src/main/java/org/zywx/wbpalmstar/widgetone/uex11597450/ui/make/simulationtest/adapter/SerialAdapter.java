package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.OptionInfoData;

import java.util.List;


public class SerialAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<OptionInfoData> lists;
    private Context mContext;

    public SerialAdapter(Context c, List<OptionInfoData> lists) {
        this.lists = lists;
        mContext = c;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext()).inflate(R.layout.serial_item_layout, parent, false));
    }

    private OnItemClickListener onItemClickListener;

    public void setListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        OptionInfoData data = lists.get(position);
        TextView serialTv = holder.getTextView(R.id.serial_num_tv);
        serialTv.setText(String.valueOf(data.getTopicNum()));
        if (data.isCorrect()) {
            serialTv.setTextColor(mContext.getResources().getColor(R.color.color_green));
        } else {
            serialTv.setTextColor(mContext.getResources().getColor(R.color.color_red));
        }
        if (data.isSelected()) {
            serialTv.setTextColor(mContext.getResources().getColor(R.color.color_white));
            if (data.isCorrect()) {
                serialTv.setBackgroundResource(R.drawable.circle_bg_selector);
            } else {
                serialTv.setBackgroundResource(R.drawable.circle_error_bg_shape);
            }
        } else {
            serialTv.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }
        serialTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }
}
