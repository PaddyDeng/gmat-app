package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.ItemSlideHelper;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import java.util.List;

public class CollectionAdapter extends BaseRecyclerViewAdapter<PracticeRecordData> implements ItemSlideHelper.Callback {

    private RecyclerView mRecyclerView;

    public CollectionAdapter(Context context, List<PracticeRecordData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.collection_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, final PracticeRecordData itemData) {
        final Context context = holder.itemView.getContext();
        holder.getTextView(R.id.collection_topic_type).setText(context.getString(R.string.str_record_title, itemData.getSection(), itemData.getQuestionid()));
        holder.getTextView(R.id.collection_question_title).setText(itemData.getQuestiontitle());
        holder.getTextView(R.id.collection_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PracticeManager.getInstance().dropCollectionData(itemData.getQuestionid());
                mData.remove(itemData);
                notifyDataSetChanged();
                if(mData.isEmpty()){
                    onShowEmptyView(true,context.getString(R.string.str_no_data_tip));
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnItemTouchListener(new ItemSlideHelper(mRecyclerView.getContext(), this));
    }

    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            if (viewGroup.getChildCount() == 2) {
                return viewGroup.getChildAt(1).getLayoutParams().width;
            }
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return mRecyclerView.getChildViewHolder(childView);
    }

    @Override
    public View findTargetView(float x, float y) {
        return mRecyclerView.findChildViewUnder(x, y);
    }
}
