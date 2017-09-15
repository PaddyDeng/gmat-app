package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;

import java.util.List;

public class SimulationListAdapter extends BaseRecyclerViewAdapter<SimulationData> {

    private int simulationType;

    public SimulationListAdapter(Context context, List<SimulationData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    public void setType(int type) {
        this.simulationType = type;
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.simulation_list_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, SimulationData itemData) {
        int averscore = itemData.getAverscore();
        if (simulationType == C.ALL) {
            int i = averscore % 10;
            if (i < 4) {
                i = 0;
            } else {
                i = 10;
            }
            int num = averscore / 10;
            averscore = num * 10 + i;
        }
//        holder.getTextView(R.id.simulation_item_content_tv).setText(HtmlUtil
//                .fromHtml(mContext.getString(R.string.str_simulation_item_content, calc(itemData.getQuestionids()), averscore)));

        holder.getTextView(R.id.simulation_item_title_tv).setText(itemData.getName());

        holder.getTextView(R.id.simulation_item_content_tv).setText(HtmlUtil
                .fromHtml(mContext.getString(R.string.str_simu_item_des,
                        itemData.getUserlowertk() + "/" + calc(itemData.getQuestionids()), averscore)));

        TextView statusTv = holder.getTextView(R.id.simulation_item_start_btn);
        statusTv.setBackgroundResource(R.drawable.simulation_shape_bg);
        if (!TextUtils.isEmpty(itemData.getMarkquestion()) && TextUtils.equals(itemData.getMarkquestion(), C.MARK_QUESTION)) {
            statusTv.setSelected(true);
            statusTv.setText(mContext.getString(R.string.str_simulation_item_see_result_btn));
        } else {
            if (TextUtils.equals(itemData.getMarkquestion(), C.GOON_MARK_QUESTION)) {
                statusTv.setBackgroundResource(R.drawable.sim_record_shpae_bg);
                statusTv.setSelected(true);
                statusTv.setText(mContext.getString(R.string.str_simulation_item_go_on_btn));
            } else {
                statusTv.setSelected(false);
                statusTv.setText(mContext.getString(R.string.str_simulation_item_start_btn));
            }
        }
    }

    private int calc(String questionIds) {
        String[] split = questionIds.split(",");
        return split != null ? split.length : 78;
    }
}
