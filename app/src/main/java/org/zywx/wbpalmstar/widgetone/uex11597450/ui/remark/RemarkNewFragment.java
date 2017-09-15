package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.community.CommunityNewFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CommentListView;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RemarkNewFragment extends BaseFragment {
    @BindView(R.id.new_reamrk_tablayout)
    TabLayout mTableLayout;
    @BindView(R.id.new_remark_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.remark_new_title_bar)
    RelativeLayout newRemarkTitleBar;

    @BindView(R.id.editTextBodyLl)
    LinearLayout etContainer;
    @BindView(R.id.circleEt)
    EditText remarkEt;
    @BindView(R.id.sendIv)
    ImageView sendPost;
    @BindView(R.id.remark_new_container)
    RelativeLayout mContainer;
    @BindView(R.id.remark_new_write_img)
    ImageView mWrite;

    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    private List<Fragment> list;
    public int height;
    private RemarkData mRemarkData;
    private int replyFlooIndex;
    private int currentPage;
    private boolean isReplyUser;


    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_remark_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    public int getEtStatus() {
        if (etContainer == null) {
            return View.GONE;
        }
        return etContainer.getVisibility();
    }

    @OnClick({R.id.remark_new_write_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remark_new_write_img:
//                DownloadListActivity.startDownloadAct(getActivity(), "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk", "QQ");
                if (currentPage == 0) {
                    //发布八卦
                    startActivity(new Intent(getActivity(), PostRemarkActivity.class));
                } else if (currentPage == 1) {
                    //社区发帖
                    PostRemarkActivity.startNewPostRemark(getActivity(), true);
                }
                break;
        }
    }

    @Override
    public void setLayoutModeAndGravity(TabLayout mTabLayout) {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
    }

    @Override
    protected void initWhenRootViewNull(Bundle savedInstanceState) {
        newRemarkTitleBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(newRemarkTitleBar, this);
                height = newRemarkTitleBar.getHeight();
            }
        });
        initTabAdapter(getPagerAdapter(), mViewPager, mTableLayout);
        setViewPagerListener();
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
        String content = remarkEt.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            toastShort(R.string.str_no_login_tip);
            return;
        }
        UserData data = GlobalUser.getInstance().getUserData();
        String uName = data.getNickname();
        if (TextUtils.isEmpty(uName)) {
            uName = data.getUsername();
        }
        addToCompositeDis(HttpUtil.reply(content, mRemarkData.getId(), mRemarkData.getUid(), uName, data.getPhoto())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        if (loginExpired(bean)) {//登录过期去登陆
                            againLogin(new ICallBack() {
                                @Override
                                public void onFail() {
                                    dismissLoadDialog();
                                }

                                @Override
                                public void onSuccess(Object o) {
                                    replyRemark();
                                }
                            });
                        } else {//评论结果
                            RemarkFragment fragment = (RemarkFragment) list.get(0);
                            fragment.netRequest(1, true);
                            remarkEt.setText("");
                            dismissLoadDialog();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void postFloor() {
        String content = remarkEt.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        List<ReplyBean> reply = mRemarkData.getReply();
        if (reply == null || reply.isEmpty()) return;
        UserData data = GlobalUser.getInstance().getUserData();
        String name = data.getUsername();
        if (!TextUtils.isEmpty(data.getNickname())) {
            name = data.getNickname();
        }
        ReplyBean bean = reply.get(replyFlooIndex);
        addToCompositeDis(HttpUtil.replyFloor(content, mRemarkData.getId(), mRemarkData.getUid(), name, data.getPhoto(), bean.getUid(), bean.getuName())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            RemarkFragment fragment = (RemarkFragment) list.get(0);
                            fragment.netRequest(1, true);
                            remarkEt.setText("");
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    private void setViewPagerListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (position == 2) {
                    Utils.setGone(mWrite);
                } else {
                    Utils.setVisible(mWrite);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setInteceptEvent(true);
    }

    public void showOrHideEt(int visibility, RemarkData remarkData) {
        showOrHideEt(visibility, remarkData, 0, isReplyUser);
    }

    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = mContainer.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mContainer == null) return;
                Rect r = new Rect();
                mContainer.getWindowVisibleDisplayFrame(r);
                int statusBarH = MeasureUtil.getStatusBarHeight(getActivity());//状态栏高度
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
                RemarkFragment remarkFragment = (RemarkFragment) list.get(0);
                LinearLayoutManager manager = remarkFragment.getManager();
                if (manager != null && mRemarkData != null) {
                    manager.scrollToPositionWithOffset(mRemarkData.getRecyclePosition(),
                            getListviewOffset(mRemarkData, screenHeight, selectCircleItemH, currentKeyboardH, editTextBodyHeight, selectCommentItemOffset) - height);
                }
            }
        });
    }

    public int getListviewOffset(RemarkData commentConfig, int screenHeight, int selectCircleItemH, int currentKeyboardH, int editTextBodyHeight, int selectCommentItemOffset) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight;//- titleBar.getHeight();
//        if(commentConfig.getRecyclePosition() == CommentConfig.Type.REPLY){
        //回复评论的情况
        if (isReplyUser)
            listviewOffset = listviewOffset + selectCommentItemOffset;
//        }
        return listviewOffset;
    }

    public boolean getEtContainerStatus() {
        return etContainer == null ? false : etContainer.getVisibility() == View.VISIBLE;
    }

    private void measureCircleItemHighAndCommentItemOffset(RemarkData commentConfig, int index) {
        if (commentConfig == null)
            return;
        LinearLayoutManager manager = ((RemarkFragment) list.get(0)).getManager();
        int firstPosition = manager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = manager.getChildAt(commentConfig.getRecyclePosition() - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        //回复评论的情况
        CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.remark_comment_list);
        if (commentLv != null) {
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

    public void showOrHideEt(int visibility, RemarkData remarkData, int position, boolean replyUser) {
        isReplyUser = replyUser;
        mRemarkData = remarkData;
        replyFlooIndex = position;
        if (etContainer == null) return;
        etContainer.setVisibility(visibility);
        measureCircleItemHighAndCommentItemOffset(remarkData, position);

        MainActivity activity = (MainActivity) getActivity();
        if (visibility == View.VISIBLE) {
            activity.setNavContainerVisibility(View.GONE);
            remarkEt.requestFocus();
            if (isReplyUser) {
                List<ReplyBean> reply = remarkData.getReply();
                if (reply != null && !reply.isEmpty()) {
                    remarkEt.setHint("@" + reply.get(position).getuName());
                }
            } else {
                remarkEt.setHint(getString(R.string.str_remark_detail_et_hint));
            }
            Utils.keyBordShowFromWindow(remarkEt.getContext(), remarkEt);
        } else if (visibility == View.GONE) {
            activity.setNavContainerVisibility(View.VISIBLE);
            Utils.keyBordHideFromWindow(remarkEt.getContext(), remarkEt);
        }
    }


    public PagerAdapter getPagerAdapter() {
        list = new ArrayList<>();
        list.add(new RemarkFragment());
        list.add(new CommunityNewFragment());
        list.add(new HighScoreStoryFragment());
        return new TabPagerAdapter(getChildFragmentManager(), getResources().getStringArray(R.array.remark_tab_arr)) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
    }
}
