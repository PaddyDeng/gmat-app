package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestType;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.SimulationResultActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.List;
import java.util.Random;

public class KnowListAdapter extends BaseRecyclerViewAdapter<QuestionBankData> {

    private Random mRandom;

    public KnowListAdapter(Context context, List<QuestionBankData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        mRandom = new Random();
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.point_list_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(final BaseRecyclerViewHolder holder, int position, final QuestionBankData itemData) {

        holder.getTextView(R.id.know_point_avg_tv).setText(Utils.getRandomNum(mRandom) + "%");
        holder.getTextView(R.id.know_point_avg_use_time).setText(25 + mRandom.nextInt(15) + "m");
        holder.getTextView(R.id.point_list_item_number).setText(String.valueOf(itemData.getKnowPointMakeNum()));
        holder.getTextView(R.id.point_list_item_tv).setText(itemData.getStname());
        holder.getTextView(R.id.point_list_item_des).setText(mContext.getString(R.string.str_item_des,itemData.getMakeSize() + "/" + itemData.getQuestionList().size()));

        final Context context = holder.itemView.getContext();
        TextView paintImg = holder.getTextView(R.id.know_point_iv);//笔
        TextView topSearchResultTv = holder.getTextView(R.id.know_point_search_result);//上面的查看结果
        TextView statusTv = holder.getTextView(R.id.point_item_start_btn);//包含三种状态的下面的tv

        paintImg.setSelected(false);

        Utils.setGone(topSearchResultTv);
        Utils.setVisible(paintImg);
        statusTv.setTextColor(context.getResources().getColor(R.color.color_blue));
        statusTv.setBackgroundColor(context.getResources().getColor(R.color.color_white));

        if (itemData.getHasMakeTest() == C.START) {
            statusTv.setText(mContext.getString(R.string.str_simulation_item_see_result_btn));//查看结果
            Utils.setVisible(topSearchResultTv);
            Utils.setGone(paintImg);
            statusTv.setTextColor(context.getResources().getColor(R.color.color_white));
            statusTv.setBackgroundResource(R.drawable.search_result_bg);
        } else if (itemData.getHasMakeTest() == C.END) {
            Utils.setVisible(paintImg);
            Utils.setGone(topSearchResultTv);
            statusTv.setTextColor(context.getResources().getColor(R.color.color_green));
            paintImg.setSelected(true);
            statusTv.setText(context.getString(R.string.str_simulation_item_see_result_btn));
        } else if (itemData.getHasMakeTest() == C.NO_START) {
            statusTv.setText(mContext.getString(R.string.str_single_item_btn));
        }
        statusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "onClick: " + itemData.getHasMakeTest());
                if (itemData.getHasMakeTest() == C.NO_START) {
                    //去做题
                    TestActivity.startTestActivity(context, itemData, TestType.KNOWS_TEST);
                } else {
                    //查看结果
                    SimulationResultActivity.startSearchResult(context, itemData, TestType.KNOWS_TEST);
                }
            }
        });

        topSearchResultTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去做题
                TestActivity.startTestActivity(context, itemData, TestType.KNOWS_TEST);
            }
        });
    }
}
