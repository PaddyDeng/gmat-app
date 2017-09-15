package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.CameraData;

import java.util.List;

public class CameraQuestionAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<CameraData> mResultData;

    private OnItemClickListener mListener;

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CameraQuestionAdapter(List<CameraData> questionsData) {
        mResultData = questionsData;
    }

    public void setData(List<CameraData> mData) {
        mResultData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 100;
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 100) {
            return new BaseRecyclerViewHolder(parent.getContext(), View.inflate(parent.getContext(), R.layout.search_question_item_layout, null));
        }
        return new BaseRecyclerViewHolder(parent.getContext(), View.inflate(parent.getContext(), R.layout.camera_result_item_layout, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        Context context = holder.itemView.getContext();
        if (getItemViewType(position) == 100) {
            holder.getTextView(R.id.search_link_content).setText(context.getString(R.string.str_recognize_result));
        } else {
            CameraData data = mResultData.get(position - 1);
            holder.getTextView(R.id.search_question_content).setText(data.getQuestion());
//            ((CheckBox) holder.getView(R.id.search_question_checkbox)).setChecked(data.isChecked());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(v, position - 1);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mResultData == null ? 0 : (mResultData.size() > 5 ? 6 : mResultData.size() + 1);
    }
}
