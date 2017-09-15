package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.ShareProxy;

import butterknife.BindView;

public class ShareDialog extends BaseDialog {
    @BindView(R.id.wechar_friends_container)
    LinearLayout wechartContainer;
    @BindView(R.id.wechart_circle_container)
    LinearLayout circleContainer;
    @BindView(R.id.qq_container)
    LinearLayout qqContainer;
    @BindView(R.id.qzone_container)
    LinearLayout qzoneContainer;
    @BindView(R.id.sina_weibo_container)
    LinearLayout sinaContainer;
    private String mImagePath;

    @Override
    protected int getContentViewLayId() {
        return R.layout.dialog_new_share;
    }

    @Override
    protected int[] getWH() {
        return new int[]{MeasureUtil.getScreenSize(getActivity()).x, getDialog().getWindow().getAttributes().height};
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != getDialog().getWindow())
            getDialog().getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImagePath = getActivity().getFilesDir().getPath() + "share.png";
        wechartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//微信好友分享
                ShareProxy.getInstance().shareToWechat(mImagePath);
                dismiss();
            }
        });
        circleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//微信朋友圈分享
                ShareProxy.getInstance().shareWechatMoments(mImagePath);
                dismiss();
            }
        });
        qqContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//qq分享
                ShareProxy.getInstance().shareToQQ(mImagePath);
                dismiss();
            }
        });
        qzoneContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//qq空间分享
                ShareProxy.getInstance().shareToQzone(mImagePath);
                dismiss();
            }
        });
        sinaContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//新浪分享
                ShareProxy.getInstance().shareToSina(mImagePath);
                dismiss();
            }
        });
    }
}
