package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;

import java.util.List;
import java.util.Random;

public class RecordAdapter extends BaseRecyclerViewAdapter<PracticeRecordData> {

    private Random mRandom;

    public RecordAdapter(Context context, List<PracticeRecordData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        mRandom = new Random();
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.record_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, PracticeRecordData itemData) {
        Context context = holder.itemView.getContext();
        holder.getTextView(R.id.record_topic_type).setText(context.getString(R.string.str_record_title, itemData.getSection(), itemData.getQuestionid()));
        holder.getTextView(R.id.record_topic_you_answer).setText(context.getString(R.string.str_record_your_answer, itemData.getYouanswer()));
        holder.getTextView(R.id.record_topic_correct_answer).setText(context.getString(R.string.str_record_correct_answer, itemData.getQuestionanswer()));
        holder.getTextView(R.id.record_title).setText(itemData.getQuestiontitle());

        String date = TimeUtils.longToString(itemData.getStartmake(), "yyyy.MM.dd HH:mm:ss");
        int useTime = (int) (itemData.getUsetime() / 1000.0f);
        StringBuffer useStr = new StringBuffer();
        if (useTime < 60) {
            useStr.append(useTime + "s");
        } else if (useTime < 1 * 60 * 60) {
            int min = useTime / 60;
            int sec = useTime % 60;
            useStr.append(min + "m" + sec + "s");
        }
        holder.getTextView(R.id.record_times).setText(context.getString(R.string.str_record_use_time, date, useStr.toString(), 800 + mRandom.nextInt(80)));
    }
}
