package org.zywx.wbpalmstar.widgetone.uex11597450.callback.listener;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkData;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class PriaseListener implements View.OnClickListener {
    private BaseActivity baseActivity;
    private RemarkData mRemarkData;

    public PriaseListener(Context context, RemarkData mRemarkData) {
        baseActivity = (BaseActivity) context;
        this.mRemarkData = mRemarkData;
    }

    @Override
    public void onClick(View v) {
        setPriase((TextView) v);
        priaseRemark(v);
    }

    private void priaseRemark(final View v) {
        HttpUtil.praiseOrCancel(mRemarkData.getId()).subscribe(new Consumer<ResultBean>() {
            @Override
            public void accept(@NonNull ResultBean bean) throws Exception {
                if (baseActivity.needAgainLogin(bean, new ICallBack() {
                    @Override
                    public void onSuccess(Object o) {
                        priaseRemark(v);
                    }

                    @Override
                    public void onFail() {
                        setPriase((TextView) v);
                    }
                })) {
                } else {
                    //没有过期，不用重新登录
                    if (bean.getCode() == 1 || bean.getCode() == 2) {
                        //1点赞成功 2取消点赞成功
                    } else {
                        setPriase((TextView) v);
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        });
    }

    private void setPriase(TextView v) {

        int num = Integer.parseInt(mRemarkData.getLikeNum());
        TextView tv = v;
        if (mRemarkData.isLikeId()) {
            num = num == 0 ? 0 : --num;
            mRemarkData.setLikeId(false);
        } else {
            mRemarkData.setLikeId(true);
            ++num;
        }
        v.setSelected(mRemarkData.isLikeId());
        mRemarkData.setLikeNum(String.valueOf(num));
        tv.setText(String.valueOf(num));
    }


}
