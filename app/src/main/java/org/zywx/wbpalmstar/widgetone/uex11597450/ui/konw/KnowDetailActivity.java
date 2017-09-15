package org.zywx.wbpalmstar.widgetone.uex11597450.ui.konw;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowDetailInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SwipeInit;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.GeneralView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.overscroll.FastAndOverScrollScrollView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowData.CategoryTypeBean.CategoryContentBean;

public class KnowDetailActivity extends BaseActivity /*implements SwipeRefreshLayout.OnRefreshListener */ {

    public static void startKnowDetail(Context c, CategoryContentBean bean) {
        Intent intent = new Intent(c, KnowDetailActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, bean);
        c.startActivity(intent);
    }

    private CategoryContentBean bean;
    private String contentId;
    private String title;

    //    @BindView(R.id.know_swipe)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.know_detail_title)
    TextView titleTxt;
    @BindView(R.id.know_general_view)
    GeneralView mGeneralView;
    @BindView(R.id.load_container)
    RelativeLayout mLoadContainer;
    @BindView(R.id.scroll_view)
    FastAndOverScrollScrollView mScrollView;
    @BindView(R.id.again_load_des)
    TextView loadDes;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private int fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_detail);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        bean = getIntent().getParcelableExtra(Intent.EXTRA_INDEX);
        contentId = bean.getContentid();
        title = bean.getContenttitle();
    }

    @OnClick({R.id.load_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_container:
                asyncUiInfo();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (TextUtils.isEmpty(contentId))
            return;
        if (TextUtils.isEmpty(title))
            return;
        titleTxt.setText(title);
//        SwipeInit.setRefreshColor(mSwipeRefreshLayout);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void asyncUiInfo() {
        super.asyncUiInfo();
        fontSize = SharedPref.getFontSize(mContext);
        Utils.setVisible(mLoadContainer, mProgressBar);
        Utils.setGone(mScrollView, loadDes);
//        mSwipeRefreshLayout.setRefreshing(true);
        addToCompositeDis(HttpUtil.getKnowInfo(contentId)
                .subscribe(new Consumer<ResultBean<KnowDetailInfo>>() {
                               @Override
                               public void accept(@NonNull ResultBean<KnowDetailInfo> bean) throws Exception {
//                                   mSwipeRefreshLayout.setRefreshing(false);
                                   if (!getHttpResSuc(bean.getCode())) return;
                                   Utils.setVisible(mScrollView);
                                   Utils.setGone(mLoadContainer);
                                   String contenttext = bean.getData().getContenttext();
                                   if (!TextUtils.isEmpty(contenttext)) {
                                       mGeneralView.setKnowDetailText(contenttext, fontSize);
                                   }
                                   log(contenttext);
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(@NonNull Throwable throwable) throws Exception {
//                                   mSwipeRefreshLayout.setRefreshing(false);
//                                   asyncUiInfo();
                                   Utils.setVisible(mLoadContainer, loadDes);
                                   Utils.setGone(mScrollView, mProgressBar);
                               }
                           }
                ));
    }
//
//    @Override
//    public void onRefresh() {
//        asyncUiInfo();
//    }
}
