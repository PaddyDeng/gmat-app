package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import java.util.List;

public class SearchQuestionAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<QuestionsData> mQuestionsData;

    private OnItemClickListener mListener;

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public SearchQuestionAdapter(List<QuestionsData> questionsData) {
        mQuestionsData = questionsData;
    }

    public void updateData() {
        notifyDataSetChanged();
    }

    public void setData(List<QuestionsData> mData) {
        mQuestionsData = mData;
        notifyDataSetChanged();
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), View.inflate(parent.getContext(), R.layout.search_question_item_layout, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        QuestionsData data = mQuestionsData.get(position);
        holder.getTextView(R.id.search_link_content).setText(data.getQuestiontitle());
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
        return mQuestionsData == null ? 0 : mQuestionsData.size();
    }
}
