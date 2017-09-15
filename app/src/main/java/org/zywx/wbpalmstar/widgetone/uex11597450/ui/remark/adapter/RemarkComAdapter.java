package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.UrlUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.span.SpannableClickable;

import java.util.List;

public class RemarkComAdapter extends BaseRecyclerViewAdapter<ReplyBean> {

    public RemarkComAdapter(Context context, List<ReplyBean> data) {
        super(context, data);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.remark_detail_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, ReplyBean itemData) {
        String icon = Utils.split(itemData.getUserImage());
        String headUrl = RetrofitProvider.BASEURL + icon;
        GlideUtil.loadDefault(headUrl, holder.getImageView(R.id.remark_detail_item_head_img), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        holder.getTextView(R.id.remark_detail_item_time).setText(TimeUtils.longToString(Long.parseLong(itemData.getCreateTime()) * 1000, "yyyy.MM.dd HH:mm:ss"));

//        int itemSelectorColor = mContext.getResources().getColor(R.color.praise_item_selector_default);
//        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);
        final ReplyBean bean = itemData;
        String name = bean.getuName();
        String id = bean.getUid();
        String toReplyName = "";
        if (!TextUtils.isEmpty(bean.getReplyUserName())) {
            toReplyName = bean.getReplyUserName();
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name, id));

        if (!TextUtils.isEmpty(toReplyName)) {

            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName, bean.getReplyUser()));
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = bean.getContent();
        builder.append(UrlUtils.formatUrlString(contentBodyStr));
        holder.getTextView(R.id.remark_detail_item_user_name).setText(builder);

//        holder.getTextView(R.id.remark_detail_item_user_name).setMovementMethod(circleMovementMethod);
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(mContext.getResources().getColor(R.color.color_blue)) {
                                    @Override
                                    public void onClick(View widget) {
                                        Toast.makeText(GmatApplication.getInstance(), textStr + " &id = " + id, Toast.LENGTH_SHORT).show();
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }
}
