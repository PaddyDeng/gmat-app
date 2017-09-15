package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.TeacherData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.PreProGramLessonActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;

import java.util.List;


public class TeacherAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    private List<TeacherData> data;


    private OnItemClickListener mListener;

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TeacherAdapter(List<TeacherData> data) {
        this.data = data;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_item_layout, parent, false);
        mCardAdapterHelper.onTeacherCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        final Context context = holder.itemView.getContext();
        final TeacherData teacherData = data.get(position);
        GlideUtil.loadDefault(RetrofitProvider.BASEURL + teacherData.getTeacherIamge(), holder.getCircleImageView(R.id.teacher_item_img), false,
                DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        holder.getTextView(R.id.teacher_title_tv).setText(teacherData.getTeacherName());
        holder.getTextView(R.id.teacher_item_number).setText(context.getString(R.string.str_teacher_lesson_number, teacherData.getRandomNumber()));
        holder.getTextView(R.id.teacher_des_tv).setText(HtmlUtil.fromHtml(HtmlUtil.replaceSpace(HtmlUtil.fromHtml(teacherData.getIntroduce()).toString())));
        holder.getTextView(R.id.teacher_item_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teacherData != null && !TextUtils.isEmpty(teacherData.getTeacherId())) {
                    PreProGramLessonActivity.startPre(context, teacherData.getTeacherId(), teacherData.getTitle());
                }
            }
        });
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
        return data.size();
    }
}
