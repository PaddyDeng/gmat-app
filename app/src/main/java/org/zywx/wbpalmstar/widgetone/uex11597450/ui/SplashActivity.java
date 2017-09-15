package org.zywx.wbpalmstar.widgetone.uex11597450.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxHelper;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity {

    private static final int TIME_LOAD_MIN = 2000;
    private static final int TIME_LOAD_MAX = 10;
    private long loadTime = 0, startTime;
    private ResultBean mBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        jump(TIME_LOAD_MAX, true);
    }

    protected void jump(int time, final boolean jumpMain) {
        addToCompositeDis(RxHelper.delay(time).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (jumpMain) {
                    goMain();
                } else {
                    goAdv();
                }
            }
        }));
    }

    @Override
    protected void asyncUiInfo() {
        super.asyncUiInfo();
        startTime = System.currentTimeMillis();
        addToCompositeDis(HttpUtil.getAdvertising().subscribe(new Consumer<ResultBean>() {
            @Override
            public void accept(@NonNull ResultBean bean) throws Exception {
                mBean = bean;
                judge();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                startMainAct();
            }
        }));
    }

    private void judge() {
        if (mBean.isJudge()) {
            //去广告页
            startAdvertising();
        } else {
            //去首页
            startMainAct();
        }
    }

    private void startMainAct() {
        loadTime = System.currentTimeMillis() - startTime;
        if (TIME_LOAD_MIN > loadTime) {
            int l = (int) ((TIME_LOAD_MAX - loadTime) / 1000);
            jump(l <= 0 ? 1 : 0, true);
        } else {
            goMain();
        }
    }

    private void goAdv() {
        if (mBean == null) {
            goMain();
        } else {
            AdvertisingActivity.setAdvert(mContext, mBean.getImage(), mBean.getTime(), mBean.getUrl());
            finish();
        }
    }

    private void goMain() {
        forword(MainActivity.class);
        finish();
    }

    private void startAdvertising() {
        loadTime = System.currentTimeMillis() - startTime;
        if (TIME_LOAD_MIN > loadTime) {
            int l = (int) ((TIME_LOAD_MAX - loadTime) / 1000);
            jump(l <= 0 ? 1 : l, false);
        } else {
            goAdv();
        }
    }

}
