package org.zywx.wbpalmstar.widgetone.uex11597450;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ActionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.thrlib.OCRProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.CenterFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.ContactUsActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.ActionDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.DesActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.OffLineTipDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.update.DownloadApk;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.update.SimpleUpdateApk;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.GmatFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.helper.MainNavHelper;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.konw.KnowFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.MakeTestFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.RemarkNewFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.setting.SettingActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.SlidingMenu;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


public class MainActivity extends BaseActivity implements DownloadApk.OnDownloadApkListener {

    @BindView(R.id.slidingmenu)
    SlidingMenu slidingMenu;
    @BindView(R.id.nav_containre)
    LinearLayout navContainer;
    private MainNavHelper mHelper;
    private SimpleUpdateApk mSimpleUpdateApk;
    private Observable<Integer> makeNumObs;

    public void setNavContainerVisibility(int visibility) {
        navContainer.setVisibility(visibility);
    }

    @Override
    protected void asyncUiInfo() {
        mSimpleUpdateApk = new SimpleUpdateApk(MainActivity.this);
        mSimpleUpdateApk.checkVersionUpdate();
        action();
    }

    private void action() {
        addToCompositeDis(HttpUtil
                .action()
                .subscribe(new Consumer<ActionData>() {
                    @Override
                    public void accept(@NonNull ActionData actionData) throws Exception {
                        if (actionData == null) return;
                        String id = SharedPref.getActionId(mContext);
                        int times = SharedPref.getCurrentTIMES(mContext);
                        if (!TextUtils.equals(id, actionData.getId())) {
                            times = 0;
                            SharedPref.setActionId(mContext, actionData.getId());
                        }
                        if (actionData.isJudge() && times < actionData.getMaxdisplay()) {
                            SharedPref.setCurrentTIMES(mContext, ++times);
                            ActionDialog.getInstance(actionData).showDialog(getSupportFragmentManager());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OCRProxy.orcRelease();
        if (mSimpleUpdateApk != null) {
            mSimpleUpdateApk.onDestory();
            mSimpleUpdateApk = null;
        }
        if (makeNumObs != null) {
            RxBus.get().unregister(C.UPLOAD_DB_TOPIC_SIZE, makeNumObs);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new MainNavHelper(this, R.id.main_contaier);
        setContentView(R.layout.activity_main);
        OCRProxy.initToken(mContext);

        makeNumObs = RxBus.get().register(C.UPLOAD_DB_TOPIC_SIZE, Integer.class);
        makeNumObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer.intValue() == 0) return;
                SharedPref.setMakeNum(mContext, String.valueOf(integer));
            }
        });

        //本地未退出登录才自动登录
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            againLogin(new ICallBack() {
                @Override
                public void onSuccess(Object o) {
                }

                @Override
                public void onFail() {
                }
            });
        }
    }

    public void setInteceptEvent(boolean event) {
        slidingMenu.setInterceptEvent(event);
    }

    @Override
    protected void initData() {
        super.initData();

        mHelper.addFragment(new GmatFragment(), R.id.main_nav_gmat);
        mHelper.addFragment(new MakeTestFragment(), R.id.main_nav_make);
        mHelper.addFragment(new KnowFragment(), R.id.main_nav_know);
//        addFragment(new RemarkFragment(), R.id.main_nav_pro);
        mHelper.addFragment(new RemarkNewFragment(), R.id.main_nav_pro);
        mHelper.addFragment(new CenterFragment(), R.id.main_nav_center);

        mHelper.replaceFragment(getSupportFragmentManager(), R.id.main_nav_gmat);

    }

    @Override
    protected void onStart() {
        super.onStart();
        String order = SharedPref.getOrder(mContext);
        if (!TextUtils.isEmpty(order) && TextUtils.equals(order, C.REPLY)) {
            SharedPref.saveOrder(mContext, "");
            mHelper.replaceFragment(getSupportFragmentManager(), R.id.main_nav_pro);
            //去刷新列表
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REMARK_REFRESH_ACTION));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            RemarkNewFragment fragment = (RemarkNewFragment) mHelper.getNaviMapFragment().get(R.id.main_nav_pro);
            if (fragment.getEtStatus() == View.VISIBLE) {
                fragment.showOrHideEt(View.GONE, null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showOrHideEt(int visibility, RemarkData remarkData, int position) {
        showOrHideEt(visibility, remarkData, position, true);
    }

    /**
     * @param reply 回复评论
     */
    public void showOrHideEt(int visibility, RemarkData remarkData, int position, boolean reply) {
        RemarkNewFragment fragment = (RemarkNewFragment) mHelper.getNaviMapFragment().get(R.id.main_nav_pro);
        fragment.showOrHideEt(visibility, remarkData, position, reply);
    }

    public boolean getEtContainerStatus() {
        RemarkNewFragment fragment = (RemarkNewFragment) mHelper.getNaviMapFragment().get(R.id.main_nav_pro);
        return fragment.getEtContainerStatus();
    }


    @OnClick({R.id.main_nav_make, R.id.main_nav_know, R.id.main_nav_pro, R.id.main_nav_gmat, R.id.main_nav_center,
            R.id.main_left_about_us, R.id.main_left_personal_setting, R.id.main_left_contact_us,
            R.id.main_left_copyright_des})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_nav_make:
            case R.id.main_nav_know:
            case R.id.main_nav_pro:
            case R.id.main_nav_gmat:
            case R.id.main_nav_center:
                mHelper.switchFragment(getSupportFragmentManager(), view);
                break;
            case R.id.main_left_personal_setting:
                forword(SettingActivity.class);
                delayToggle(view);
                break;
            case R.id.main_left_about_us:
                DesActivity.startDes(mContext, getString(R.string.str_main_left_about_us), getString(R.string.str_about_us_des));
                delayToggle(view);
                break;
            case R.id.main_left_contact_us:
                forword(ContactUsActivity.class);
                delayToggle(view);
                break;
            case R.id.main_left_copyright_des:
                DesActivity.startDes(mContext, getString(R.string.str_main_left_copyright_des), getString(R.string.str_copyright_des));
                delayToggle(view);
                break;
            default:
                break;
        }
    }

    private void delayToggle(View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggle();
            }
        }, 500);
    }

    public void toggle() {
        slidingMenu.toggleMenu();
    }

    @Override
    public void onDownError() {
        toastShort(R.string.str_update_apk_fail);
    }
}
