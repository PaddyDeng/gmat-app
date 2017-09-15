package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.Correct;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.Releattr;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.SimuRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;

import java.util.List;

public class SimuRecordAdapter extends BaseRecyclerViewAdapter<SimuRecordData> {

    public SimuRecordAdapter(Context context, List<SimuRecordData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.simulation_record_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, SimuRecordData itemData) {
        Context context = holder.itemView.getContext();
        Releattr releattr = itemData.getReleattr();
        if (releattr == null) releattr = new Releattr();
        String totletime = releattr.getTotletime();
        if (TextUtils.isEmpty(totletime)) {
            totletime = "0s";
        }
        Correct correct = releattr.getCorrect();
        if (correct == null) {
            correct = new Correct();
            correct.setNumAll("0");
        }

        holder.getTextView(R.id.simulation_record_item_title_tv).setText(context.getString(R.string.str_simu_record_title_des, itemData.getName(), itemData.getNameid()));
        holder.getTextView(R.id.simulation_record_item_content_tv)
                .setText(HtmlUtil.fromHtml(context.getString(R.string.str_simu_record_make_des, correct.getNumAll() + "/" + itemData.getNum(),
                        ((int) correct.getCorrect()) + "%",totletime)));
        if (!TextUtils.isEmpty(itemData.getMarkquestion()) && TextUtils.equals(itemData.getMarkquestion(), C.MARK_QUESTION)) {
            holder.getTextView(R.id.simulation_record_item_start_btn).setSelected(false);
            holder.getTextView(R.id.simulation_record_item_start_btn).setText(mContext.getString(R.string.str_simulation_item_see_result_btn));
        } else {
            holder.getTextView(R.id.simulation_record_item_start_btn).setSelected(true);
            holder.getTextView(R.id.simulation_record_item_start_btn).setText(mContext.getString(R.string.str_simulation_item_go_on_btn));
        }
    }
}
