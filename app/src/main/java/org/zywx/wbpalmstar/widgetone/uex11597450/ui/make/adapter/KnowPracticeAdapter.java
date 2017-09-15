package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.KnowsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import java.util.List;

public class KnowPracticeAdapter extends BaseRecyclerViewAdapter<KnowsData> {

    public KnowPracticeAdapter(Context context, List<KnowsData> data) {
        super(context, data);
    }

    public KnowPracticeAdapter(Context context, List<KnowsData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.know_practice_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, KnowsData itemData) {
        holder.getTextView(R.id.know_practice_title_tv).setText(itemData.getKnows());
        holder.getTextView(R.id.know_practice_content)
                .setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_know_item_content,
                        itemData.getQuestionSize(), itemData.getMakeQueSize())));
    }
}
