package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestType;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.SimulationResultActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.List;
import java.util.Random;

public class DiffDetailAdapter extends BaseRecyclerViewAdapter<QuestionBankData> {

    Random mRandom;

    public DiffDetailAdapter(Context context, List<QuestionBankData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        mRandom = new Random();
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.single_contact_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(final BaseRecyclerViewHolder holder, int position, final QuestionBankData itemData) {
        Utils.setVisible(holder.getTextView(R.id.single_contact_btn));
        Utils.setGone(holder.getTextView(R.id.single_result_btn));
        holder.getTextView(R.id.single_contact_title_tv).setText(itemData.getStname());
        List<Integer> questionList = itemData.getQuestionList();
        int avg = Utils.getRandomNum(mRandom);

        holder.getTextView(R.id.single_contact_content).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_single_item_des,
                itemData.getMakeSize() + "/" + questionList.size(), avg + "%")));
//        holder.getTextView(R.id.single_contact_content).setText(HtmlUtil.fromHtml(mContext.getString(R.string.str_single_item_content,
////                questionList.size(), avg + "%")));
        holder.getTextView(R.id.single_contact_btn).setBackgroundResource(R.drawable.single_contact_btn_selector);
        if (itemData.getHasMakeTest() == C.START) {
            holder.getTextView(R.id.single_contact_btn).setSelected(true);
            holder.getTextView(R.id.single_contact_btn).setText(mContext.getString(R.string.str_go_on_make));
            Utils.setVisible(holder.getTextView(R.id.single_result_btn));
        } else if (itemData.getHasMakeTest() == C.END) {
//            holder.getTextView(R.id.single_contact_btn).setBackgroundResource(R.drawable.make_test_again_start_bg);
//            holder.getTextView(R.id.single_contact_btn).setSelected(true);
//            holder.getTextView(R.id.single_contact_btn).setText(mContext.getString(R.string.str_again_make));
            Utils.setVisible(holder.getTextView(R.id.single_result_btn));
            Utils.setGone(holder.getTextView(R.id.single_contact_btn));
        } else if (itemData.getHasMakeTest() == C.NO_START) {
            holder.getTextView(R.id.single_contact_btn).setSelected(false);
            holder.getTextView(R.id.single_contact_btn).setText(mContext.getString(R.string.str_single_item_btn));
        }
        holder.getTextView(R.id.single_contact_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemData.getHasMakeTest() != C.END) {
                    //去做题
                    TestActivity.startTestActivity(holder.itemView.getContext(), itemData, TestType.DIFF_TEST);
                }
            }
        });

        holder.getTextView(R.id.single_result_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看结果
                SimulationResultActivity.startSearchResult(holder.itemView.getContext(), itemData, TestType.DIFF_TEST);
            }
        });
    }
}
