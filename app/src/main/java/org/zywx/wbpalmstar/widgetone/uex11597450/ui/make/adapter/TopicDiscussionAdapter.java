package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewHolder;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TopicDiscussionItemData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.test.TopicDiscussionActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CommentListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fire on 2017/8/21  09:24.
 */

public class TopicDiscussionAdapter extends BaseRecyclerViewAdapter<TopicDiscussionItemData> {

    public TopicDiscussionAdapter(Context context, List<TopicDiscussionItemData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.topic_discussion_item_adapter;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, final TopicDiscussionItemData itemData) {
        itemData.setRecyclePosition(position);

        holder.getTextView(R.id.topic_discussion_floor).setText(mContext.getString(R.string.str_topic_floor_hint, mData.size() - position));
        GlideUtil.load(RetrofitProvider.BASEURL + itemData.getPhoto(), holder.getCircleImageView(R.id.topic_discussion_head_iv));
        holder.getTextView(R.id.topic_discussion_user_name).setText(itemData.getNickname());
        String time = itemData.getC_time();
        String[] split = time.split(" ");
        holder.getTextView(R.id.topic_discussion_time).setText(split[0]);
        holder.getTextView(R.id.topic_discussion_content).setText(itemData.getContent());

        CommentListView commLV = holder.getCommLV(R.id.topic_discussion_comment_list);
        View line = holder.getView(R.id.topic_discussion_conn_line);
        Utils.setVisible(commLV, line);
        List<TopicDiscussionItemData> son = itemData.getMultSon();
        holder.getView(R.id.topic_discussion_comm_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalUser.getInstance().isAccountDataInvalid()) {
                    Utils.toastShort(mContext, R.string.str_no_login_tip);
                    return;
                }
                ((TopicDiscussionActivity) mContext).showOrHideEt(View.VISIBLE, itemData, 0, false);
            }
        });
        if (son == null || son.isEmpty()) {
            Utils.setGone(commLV, line);
        } else {
            commLV.setDatas(initComData(son));
            commLV.setOnItemClickListener(new CommentListView.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (GlobalUser.getInstance().isAccountDataInvalid()) {
                        Utils.toastShort(mContext, R.string.str_no_login_tip);
                        return;
                    }
                    String userId = GlobalUser.getInstance().getUserData().getUserid();
                    String itemUserId = itemData.getUserid();
                    if (TextUtils.equals(userId, itemUserId)) {
                        //不能自己回复自己
                        Utils.toastShort(mContext, R.string.str_no_replay_yourself);
                    } else {
                        ((TopicDiscussionActivity) mContext).showOrHideEt(View.VISIBLE, itemData, position, true);
                    }
                }
            });
        }

    }

    private List<ReplyBean> initComData(List<TopicDiscussionItemData> son) {
        List<ReplyBean> bean = new ArrayList<>();
        for (TopicDiscussionItemData data : son) {
            ReplyBean rb = new ReplyBean();
            rb.setuName(data.getNickname());
            String content = data.getContent();
            if (content.trim().startsWith("回复") && content.contains("：")) {
                int replyIndex = content.indexOf("回复");
                int colonIndex = content.indexOf("：");
                rb.setReplyUserName(content.substring(replyIndex + 2, colonIndex));
                rb.setContent(content.substring(colonIndex + 1));
            } else {
                rb.setContent(content);
            }

            bean.add(rb);
        }
        return bean;
    }
}
