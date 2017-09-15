package org.zywx.wbpalmstar.widgetone.uex11597450.base;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.RequestCallback;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.permission.RxPermissions;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.PushProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.load.WaitDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.LoginAactivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.LoginHelper;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.ReSetSessionProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.SetNickNameActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.JsonUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RecordUploadProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BaseActivity extends FragmentActivity {
    protected Context mContext;
    protected String TAG = BaseActivity.this.getClass().getSimpleName();
    //    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    protected ConcurrentMap<String, CompositeDisposable> mConcurrentMap = new ConcurrentHashMap<>();
    protected RxPermissions mRxPermissions;
    private Unbinder mUnbinder;

    protected void addToCompositeDis(Disposable disposable) {
        CompositeDisposable compositeDisposable = mConcurrentMap.get(TAG);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            mConcurrentMap.put(TAG, compositeDisposable);
        }
        compositeDisposable.add(disposable);
//        mCompositeDisposable.add(disposable);
    }

    protected void callPhone(final String phoneNumber) {
        mRxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Utils.callPhone(BaseActivity.this, phoneNumber);
                } else {
                    toastShort(R.string.str_call_phone_no_permisson);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CompositeDisposable disposable = mConcurrentMap.get(TAG);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (mUnbinder != null)
            mUnbinder.unbind();
        WaitDialog.destroyDialog(this);
    }

    protected void errorTip(Throwable throwable) {
        String s = Utils.onError(throwable);
        if (!TextUtils.isEmpty(s))
            toastShort(s);
    }

    protected String throwMsg(Throwable throwable) {
        return Utils.onError(throwable);
    }

//    protected boolean needDispose() {
//        return false;
//    }

    public void showLoadDialog() {
        WaitDialog.getInstance(mContext).showWaitDialog();
    }

    public void dismissLoadDialog() {
        WaitDialog.getInstance(mContext).dismissWaitDialog();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mUnbinder = ButterKnife.bind(this);
        mRxPermissions = new RxPermissions(this);
        getArgs();
        initData();
        initView();
        asyncUiInfo();
    }

    protected void initView() {

    }


    protected void getUserInfo(final ICallBack<UserData> iCallBack) {
        addToCompositeDis(HttpUtil.getUserCenterDetailInfo()
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
                .subscribe(new Consumer<ResultBean<UserData>>() {
                    @Override
                    public void accept(@NonNull ResultBean<UserData> bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            UserData data = bean.getData();
                            String userJson = JsonUtil.toJson(data);
                            SharedPref.saveLoginInfo(mContext, userJson);
                            GlobalUser.getInstance().resetUserDataBySharedPref(userJson);
                            GlobalUser.getInstance().setUserData(data);
                            if (iCallBack != null)
                                iCallBack.onSuccess(data);
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorTip(throwable);
                        if (iCallBack != null)
                            iCallBack.onFail();
                    }
                }));
    }


    /**
     * true 登录过期
     */
    protected boolean loginExpired(ResultBean bean) {
        if (bean.getMessage().contains(getString(R.string.str_no_login_one)) || bean.getMessage().contains(getString(R.string.str_no_login_two))) {
            return true;
        }
        return false;
    }

    public boolean needAgainLogin(ResultBean bean, ICallBack iCallBack) {
        if (loginExpired(bean)) {
            againLogin(iCallBack);
            return true;
        }
        return false;
    }

    private void thrInfoLogin(final String iconUrl, String userId, String userName, final ICallBack iCallBack) {
        //开始登录了
        addToCompositeDis(HttpUtil.thrLogin(userId, userName, iconUrl)
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(@NonNull final UserInfo userInfo) throws Exception {
                        if (!Utils.getHttpMsgSu(userInfo.getCode())) {
                            toastShort(userInfo.getMessage());
                            iCallBack.onFail();
                            return;
                        }
                        ReSetSessionProxy.getInstance().resetSession(userInfo, null, new ReSetSessionProxy.CallBack() {
                            @Override
                            public void onFail() {
                                iCallBack.onFail();
                            }

                            @Override
                            public void onSuccess() {

                                iCallBack.onSuccess(userInfo);
                                PushProxy.setAlias(userInfo.getUid());
                                //第三方登录
                                SharedPref.saveAccount(mContext, "");
                                SharedPref.savePassword(mContext, "");
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        iCallBack.onFail();
                    }
                }));
    }


    /**
     * 自动登录
     * 登录过期，重新登录  密码找回，注册成功
     */
    protected void againLogin(final ICallBack iCallBack) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            toastShort(getString(R.string.str_no_login_tip));
            iCallBack.onFail();
            return;
        }

        if (TextUtils.isEmpty(SharedPref.getAccount(mContext)) || TextUtils.isEmpty(SharedPref.getPassword(mContext))) {
            //可能不是账号秘密登录
            //检测是否是三方登录
            if (Utils.isEmpty(SharedPref.getThrIcon(mContext), SharedPref.getThrId(mContext), SharedPref.getThrName(mContext))) {//至少有一个为空
                iCallBack.onFail();
                return;
            } else {
                //用第三方登录
                thrInfoLogin(SharedPref.getThrIcon(mContext), SharedPref.getThrId(mContext), SharedPref.getThrName(mContext), iCallBack);
            }
        } else {
            login(iCallBack);
//            addToCompositeDis(HttpUtil.login(SharedPref.getAccount(mContext), SharedPref.getPassword(mContext))
//                    .subscribe(new Consumer<UserInfo>() {
//                        @Override
//                        public void accept(@NonNull final UserInfo userInfo) throws Exception {
//                            ReSetSessionProxy.getInstance().resetSession(userInfo, SharedPref.getPassword(mContext), new ReSetSessionProxy.CallBack() {
//                                @Override
//                                public void onFail() {
//                                    iCallBack.onFail();
//                                }
//
//                                @Override
//                                public void onSuccess() {
//                                    RecordUploadProxy.uploadLocalDb(mContext, null);
//                                    PushProxy.setAlias(userInfo.getUid());
//                                    iCallBack.onSuccess(userInfo);
//                                }
//                            });
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(@NonNull Throwable throwable) throws Exception {
//                            iCallBack.onFail();
//                        }
//                    }));
        }
    }

    protected void login(final ICallBack<UserInfo> iCallBack) {
        LoginHelper.login(SharedPref.getAccount(mContext), SharedPref.getPassword(mContext), new RequestCallback<UserInfo>() {
            @Override
            public void beforeRequest() {

            }

            @Override
            public void requestFail(String msg) {
                iCallBack.onFail();
            }

            @Override
            public void requestSuccess(UserInfo userInfo) {
                RecordUploadProxy.uploadLocalDb(mContext, null);
                PushProxy.setAlias(userInfo.getUid());
                iCallBack.onSuccess(userInfo);
            }

            @Override
            public void otherDeal(final UserInfo info) {
                RecordUploadProxy.uploadLocalDb(mContext, null);
                PushProxy.setAlias(info.getUid());
                iCallBack.onSuccess(info);
                forword(SetNickNameActivity.class);
            }
        });
    }


    protected void log(String msg) {
        Utils.logh(TAG, msg);
    }

    protected String getEditText(EditText et) {
        return Utils.getEditTextString(et);
    }

    protected boolean getHttpResSuc(int code) {
        if (Utils.getHttpMsgSu(code)) {
            return true;
        }
        return false;
    }

    /**
     * 获取传递的数据
     */
    protected void getArgs() {

    }

    protected void initData() {

    }

    protected void toastShort(int id) {
        Utils.toastShort(mContext, id);
    }

    protected void toastShort(String msg) {
        Utils.toastShort(mContext, msg);
    }

    /**
     * 获取基本数据后刷新
     */
    protected void asyncUiInfo() {

    }

    protected void forword(Class<?> c) {
        startActivity(new Intent(this, c));
    }

    protected boolean isAccountInvalid(int requestCode) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            //无效，去登录
            Intent intent = new Intent(mContext, LoginAactivity.class);
            startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    /**
     * 退出之前，如果需要额外处理，调用此方法
     *
     * @return {@link #onKeyDown(int, KeyEvent) onKeyDown}后续执行
     * true：	直接返回，停留在当前页面；
     * false：	继续执行退出后续操作。
     */
    protected boolean preBackExitPage() {
        return false;
    }

    /*
    * 左上角back按钮
    */
    public void leftBack(View view) {
        if (preBackExitPage()) {
            return;
        }
        finishWithAnim();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (preBackExitPage()) {
                return true;
            }
            finishWithAnim();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void finishWithAnim() {
        switch (getAnimType()) {
            case ANIM_TYPE_RIGHT_IN:
                finishWithAnimRightOut();
                break;
            case ANIM_TYPE_UP_IN:
                finishWithAnimDownOut();
                break;
            case ANIM_TYPE_DOWN_IN:
                finishWithAnimUpOut();
                break;
            case ANIM_TYPE_SCALE_CENTER:
                finishWithAnimScaleCenter();
                break;
            default:
                finish();
                break;
        }
    }


    private void finishWithAnimRightOut() {
        finish();
        overridePendingTransition(R.anim.ac_slide_left_in, R.anim.ac_slide_right_out);
    }

    private void finishWithAnimUpOut() {
        finish();
        overridePendingTransition(0, R.anim.ac_slide_up_out);
    }

    private void finishWithAnimDownOut() {
        finish();
        overridePendingTransition(0, R.anim.ac_slide_down_out);
    }

    private void finishWithAnimScaleCenter() {
        finish();
        overridePendingTransition(0, R.anim.ac_scale_shrink_center);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (getAnimType()) {
            case ANIM_TYPE_RIGHT_IN:
                overridePendingTransition(R.anim.ac_slide_right_in, R.anim.ac_slide_left_out);
                break;
            case ANIM_TYPE_UP_IN:
                overridePendingTransition(R.anim.ac_slide_up_in, 0);
                break;
            case ANIM_TYPE_DOWN_IN:
                overridePendingTransition(R.anim.ac_slide_down_in, 0);
                break;
            case ANIM_TYPE_SCALE_CENTER:
                overridePendingTransition(R.anim.ac_scale_magnify_center, 0);
                break;
            default:
                break;
        }
        mContext = this;
    }

    public enum AnimType {
        ANIM_TYPE_DOWN_IN,
        ANIM_TYPE_RIGHT_IN, // 右侧滑动进入
        ANIM_TYPE_UP_IN, //
        ANIM_TYPE_SCALE_CENTER // 中心缩放显示/隐藏
    }

    public AnimType getAnimType() {
        return AnimType.ANIM_TYPE_RIGHT_IN;
    }
}
