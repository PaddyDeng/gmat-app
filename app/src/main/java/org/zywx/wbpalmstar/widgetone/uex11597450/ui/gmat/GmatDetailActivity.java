package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.GmatDetailData;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.DealActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.GeneralView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.ObservableScrollView;

import butterknife.BindView;
import butterknife.OnClick;

public class GmatDetailActivity extends BaseActivity {

    public static void startGmatDetail(Context c, GmatDetailData data) {
        Intent intent = new Intent(c, GmatDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        c.startActivity(intent);
    }

    @BindView(R.id.gmat_detail_title_container)
    RelativeLayout mTitleContainer;
    @BindView(R.id.gmat_detail_img_header)
    ImageView headImg;
    @BindView(R.id.gmat_detail_content_title_tv)
    TextView contentTitleTv;
//    @BindView(R.id.gmat_detail_des_tv)
//    TextView desTv;
    @BindView(R.id.gamt_detai_general_view)
    GeneralView mGeneralView;
    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.header)
    ImageView headerImg;
    @BindView(R.id.rl_header_container)
    RelativeLayout headerContainer;
    @BindView(R.id.gmat_detail_num_tv)
    TextView detailNumTv;

    private GmatDetailData mGmatDetailData;
    private float mActionBarSize;
    private float titleBarSize;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent != null)
            mGmatDetailData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void initData() {
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        mActionBarSize = styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        titleBarSize = getResources().getDimension(R.dimen.header_height);

        scrollView.setOnScrollListener(new ObservableScrollView.ScrollViewListener() {

            @Override
            public void onScroll(int oldy, int dy, boolean isUp) {
                if (dy < 0) {
                    dy = 0;
                }
                float move_distance = titleBarSize - mActionBarSize;
                if (!isUp && dy <= move_distance) {
                    //标题栏逐渐从透明变成不透明
                    mTitleContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_black));
                    TitleAlphaChange(dy, move_distance);//标题栏渐变
                    HeaderTranslate(dy);//图片视差平移

                } else if (!isUp && dy > move_distance) {//手指往上滑,距离超过200dp
                    TitleAlphaChange(1, 1);//设置不透明百分比为100%，防止因滑动速度过快，导致距离超过200dp,而标题栏透明度却还没变成完全不透的情况。

                    HeaderTranslate(titleBarSize);//这里也设置平移，是因为不设置的话，如果滑动速度过快，会导致图片没有完全隐藏。

                } else if (isUp && dy > move_distance) {//返回顶部，但距离头部位置大于200dp
                    //不做处理

                } else if (isUp && dy <= move_distance) {//返回顶部，但距离头部位置小于200dp
                    //标题栏逐渐从不透明变成透明
                    TitleAlphaChange(dy, move_distance);//标题栏渐变
                    HeaderTranslate(dy);//图片视差平移
                }
            }
        });

    }

    private void HeaderTranslate(float distance) {
        headerContainer.setTranslationY(-distance);
        headerImg.setTranslationY(distance / 2);
    }

    private void TitleAlphaChange(int dy, float mHeaderHeight_px) {
        float percent = (float) Math.abs(dy) / Math.abs(mHeaderHeight_px);
        int alpha = (int) (percent * 255);
        mTitleContainer.getBackground().setAlpha(alpha);
    }


    @Override
    protected void initView() {
        if (mGmatDetailData == null) return;
        GlideUtil.loadDefault(mGmatDetailData.getHeadUrl(), headImg, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        contentTitleTv.setText(mGmatDetailData.getTitle());
//        desTv.setText(formatStr(mGmatDetailData.getDes()));
        mGeneralView.setGmatDetailText(mGmatDetailData.getDes());
        detailNumTv.setText(getString(R.string.str_gmat_detail_number, mGmatDetailData.getNumber()));
    }

    private Spanned formatStr(String des) {
        log(des);
        Spanned spanned = HtmlUtil.fromHtml(des);
        String s = spanned.toString();
        if (s.contains("&nbsp;")) {
            return HtmlUtil.fromHtml(HtmlUtil.replaceSpace(s));
        }
        return spanned;
    }

    @OnClick({R.id.go_advisory, R.id.gmat_detail_trial_course_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_advisory:
                DealActivity.startDealActivity(mContext, "", "http://p.qiao.baidu.com/im/index?siteid=7905926&ucid=18329536&cp=&cr=&cw=", C.DEAL_ADD_HEADER);
                break;
            case R.id.gmat_detail_trial_course_btn:
                forword(TrialCourseTypeActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gmat_detail);
        mTitleContainer.getBackground().setAlpha(0);
    }
}
