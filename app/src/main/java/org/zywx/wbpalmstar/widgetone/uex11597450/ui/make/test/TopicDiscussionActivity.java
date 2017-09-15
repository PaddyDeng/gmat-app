package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseListPullActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.RecycleViewLinearDivider;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TopicDiscussionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TopicDiscussionItemData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter.TopicDiscussionAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CommentListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 题目讨论 2017.8.21
 */
public class TopicDiscussionActivity extends BaseListPullActivity<TopicDiscussionItemData> {

    public static void startTopicDiscussionAct(Context c, int questionId) {
        Intent intent = new Intent(c, TopicDiscussionActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, questionId);
        c.startActivity(intent);
    }

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(mContext);
    protected int page = 1;
    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;
    private int commentLvHeight;

    public int height;
    private TopicDiscussionItemData mRemarkData;
    private int replyFlooIndex;
    private boolean isReplyUser;

    private int questionId;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        questionId = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
    }

    @Override
    protected void initView() {
        super.initView();
        Utils.setVisible(findViewById(R.id.ask_question_tv));
        titleTxt.setText(R.string.str_topic_discuss);
        setViewTreeObserver();
        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemarkData == null) return;
                if (isReplyUser) {
                    postFloor();//回复楼层
                } else {
                    //评论
                    replyRemark();
                }
            }
        });
    }

    private void replyRemark() {
        String content = getEditText(remarkEt);
        if (TextUtils.isEmpty(content)) {
            return;
        }
        reply(content, mRemarkData.getCommentid());
    }

    private void reply(String content, String userId) {
        addToCompositeDis(HttpUtil
                .addComment(content, questionId, userId)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        toastShort(bean.getMessage());
                        if (getHttpResSuc(bean.getCode())) {
                            remarkEt.setText("");
                            asyncRequest();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    private void postFloor() {
        String content = getEditText(remarkEt);
        if (TextUtils.isEmpty(content)) {
            return;
        }
        String commentid = mRemarkData.getMultSon().get(replyFlooIndex).getCommentid();
        reply(content, commentid);
    }

    @OnClick({R.id.ask_question_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ask_question_tv:
                AskQuestionActivity.startAskQuestionAct(this, questionId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == C.ASK_QUESTION_CODE) {
            asyncRequest();
        }
    }

    private void setViewTreeObserver() {
        titleContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(titleContainer,this);
                height = titleContainer.getHeight();
            }
        });
        final ViewTreeObserver swipeRefreshLayoutVTO = mContainer.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mContainer.getWindowVisibleDisplayFrame(r);
                int statusBarH = MeasureUtil.getStatusBarHeight(mContext);//状态栏高度
                int screenH = mContainer.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = etContainer.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    showOrHideEt(View.GONE, null);
                    return;
                }
                //偏移listview
                LinearLayoutManager manager = (LinearLayoutManager) mManager;
                if (manager != null && mRemarkData != null) {
                    manager.scrollToPositionWithOffset(mRemarkData.getRecyclePosition(),
                            getListviewOffset(mRemarkData));
                }
            }
        });
    }

    public void showOrHideEt(int visibility, TopicDiscussionItemData remarkData) {
        showOrHideEt(visibility, remarkData, 0, isReplyUser);
    }

    private void measureCircleItemHighAndCommentItemOffset(TopicDiscussionItemData commentConfig, int index) {
        if (commentConfig == null)
            return;
        LinearLayoutManager manager = (LinearLayoutManager) mManager;
        int firstPosition = manager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = manager.getChildAt(commentConfig.getRecyclePosition() - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }
        commentLvHeight = 0;
        //回复评论的情况
        CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.topic_discussion_comment_list);
        if (commentLv != null) {
            commentLvHeight = commentLv.getHeight();
            //找到要回复的评论view,计算出该view距离所属动态底部的距离
            View selectCommentItem = commentLv.getChildAt(index);
            if (selectCommentItem != null) {
                //选择的commentItem距选择的CircleItem底部的距离
                selectCommentItemOffset = 0;
                View parentView = selectCommentItem;
                do {
                    int subItemBottom = parentView.getBottom();
                    parentView = (View) parentView.getParent();
                    if (parentView != null) {
                        selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                    }
                } while (parentView != null && parentView != selectCircleItem);
            }
        }
    }

    public void showOrHideEt(int visibility, TopicDiscussionItemData remarkData, int position, boolean replyUser) {
        isReplyUser = replyUser;
        mRemarkData = remarkData;
        replyFlooIndex = position;
        etContainer.setVisibility(visibility);
        measureCircleItemHighAndCommentItemOffset(remarkData, position);

        if (visibility == View.VISIBLE) {
            remarkEt.requestFocus();
            if (isReplyUser) {
                String nickname = remarkData.getMultSon().get(position).getNickname();
                if (!TextUtils.isEmpty(nickname)) {
                    remarkEt.setHint("@" + nickname);
                }
            } else {
                remarkEt.setHint(getString(R.string.str_remark_detail_et_hint));
            }
            Utils.keyBordShowFromWindow(mContext, titleTxt);
        } else if (visibility == View.GONE) {
            Utils.keyBordHideFromWindow(mContext, titleTxt);
        }
    }


    public int getListviewOffset(TopicDiscussionItemData commentConfig) {
        if (commentConfig == null)
            return 0;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight - height;
//        if(commentConfig.getRecyclePosition() == CommentConfig.Type.REPLY){
        if (isReplyUser) {
            listviewOffset = listviewOffset + selectCommentItemOffset;
        } else {
            listviewOffset = listviewOffset + commentLvHeight;
        }
        //回复评论的情况
//        }
        return listviewOffset;
    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        super.initRecyclerViewItemDecoration(mRecyclerView);
//        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(mContext, LinearLayoutManager.VERTICAL, R.drawable.gray_divider));
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (etContainer.getVisibility() == View.VISIBLE) {
                    showOrHideEt(View.GONE, null);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void asyncRequest() {
        asyncData(page = 1, true);
    }

    protected void asyncData(int page, final boolean isRefresh) {
//        questionId  8572
        addToCompositeDis(HttpUtil
                .discussion(questionId, page)
                .subscribe(new Consumer<TopicDiscussionData>() {
                    @Override
                    public void accept(@NonNull TopicDiscussionData data) throws Exception {
                        if (data == null) {
                            refreshUi(null, isRefresh);
                            return;
                        }
                        List<TopicDiscussionItemData> itemDatas = data.getData();
//                            refreshUi(itemDatas, isRefresh);
                        if (itemDatas == null || itemDatas.isEmpty()) {
                            refreshUi(itemDatas, isRefresh);
                        } else {
                            dealData(itemDatas, isRefresh);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        refreshFail(throwable, isRefresh);
                        setFail();
                    }
                }));
    }

    private void dealData(List<TopicDiscussionItemData> datas, boolean isRefresh) {
        List<TopicDiscussionItemData> newItemDatas = new ArrayList<>();
        for (TopicDiscussionItemData data : datas) {
            List<TopicDiscussionItemData> son = data.getSon();

            if (son == null || son.isEmpty()) {
                newItemDatas.add(data);
                continue;
            }

            List<TopicDiscussionItemData> multSon = data.getMultSon();

            if (multSon == null) multSon = new ArrayList<>();

            recursiveFun(son, multSon);

            if (!multSon.isEmpty()) {
                data.setMultSon(multSon);
            }
            newItemDatas.add(data);
        }

        refreshUi(newItemDatas, isRefresh);
    }

    public void recursiveFun(List<TopicDiscussionItemData> data, List<TopicDiscussionItemData> multSon) {
        for (TopicDiscussionItemData sd : data) {
            List<TopicDiscussionItemData> sonData = sd.getSon();
            multSon.add(sd);
            if (sonData != null && !sonData.isEmpty()) {
                recursiveFun(sonData, multSon);
            }
        }
    }

    private void setFail() {
        if (page != 1) {
            page--;
        }
    }

    @Override
    public void asyncLoadMore() {
        asyncData(++page, false);
    }

    @Override
    public BaseRecyclerViewAdapter<TopicDiscussionItemData> getAdapter() {
        return new TopicDiscussionAdapter(mContext, null, mManager);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }
}
