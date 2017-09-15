package org.zywx.wbpalmstar.widgetone.uex11597450.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.TabPagerAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.RequestCallback;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.load.WaitDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.PushProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.LoginAactivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.LoginHelper;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.ReSetSessionProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.SetNickNameActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.JsonUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RecordUploadProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    private String TAG = BaseFragment.this.getClass().getSimpleName();
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Unbinder mUnbinder;

    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    protected void log(String msg) {
        Utils.logh(TAG, msg);
    }


    protected void showLoadDialog() {
        WaitDialog.getInstance(getActivity()).showWaitDialog();
    }

    protected void dismissLoadDialog() {
        WaitDialog.getInstance(getActivity()).dismissWaitDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
        WaitDialog.destroyDialog(getActivity());
        if (mUnbinder != null)
            mUnbinder.unbind();
        RefWatcher refWatcher = GmatApplication.getRefWatcher(getActivity());
        if (null != refWatcher) refWatcher.watch(this);
    }


    protected String errorTip(Throwable throwable) {
        return Utils.onError(throwable);
    }

    public boolean needAgainLogin(ResultBean bean, ICallBack iCallBack) {
        if (loginExpired(bean)) {
            againLogin(iCallBack);
            return true;
        }
        return false;
    }

    protected void initTabAdapter(PagerAdapter adapter, ViewPager mViewPager, TabLayout mTabLayout) {
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setLayoutModeAndGravity(mTabLayout);
    }

    public void setLayoutModeAndGravity(TabLayout mTabLayout) {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
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

    private void thrInfoLogin(final String iconUrl, String userId, String userName, final ICallBack iCallBack) {
        //开始登录了
        showLoadDialog();
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
                                SharedPref.saveAccount(getActivity(), "");
                                SharedPref.savePassword(getActivity(), "");
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                }));
    }


    protected void againLogin(final ICallBack iCallBack) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            iCallBack.onFail();
            toastShort(getString(R.string.str_no_login_tip));
            return;
        }
        if (TextUtils.isEmpty(SharedPref.getAccount(getActivity())) || TextUtils.isEmpty(SharedPref.getPassword(getActivity()))) {
            //可能不是账号秘密登录
            //检测是否是三方登录
            if (Utils.isEmpty(SharedPref.getThrIcon(getActivity()), SharedPref.getThrId(getActivity()), SharedPref.getThrName(getActivity()))) {//至少有一个为空
                iCallBack.onFail();
                return;
            } else {
                //用第三方登录
                thrInfoLogin(SharedPref.getThrIcon(getActivity()), SharedPref.getThrId(getActivity()), SharedPref.getThrName(getActivity()), iCallBack);
            }
        } else {
            login(iCallBack);
//            addToCompositeDis(HttpUtil.login(SharedPref.getAccount(getActivity()), SharedPref.getPassword(getActivity()))
//                    .subscribe(new Consumer<UserInfo>() {
//                        @Override
//                        public void accept(@NonNull final UserInfo userInfo) throws Exception {
//                            ReSetSessionProxy.getInstance().resetSession(userInfo, SharedPref.getPassword(getActivity()), new ReSetSessionProxy.CallBack() {
//                                @Override
//                                public void onFail() {
//                                    iCallBack.onFail();
//                                }
//
//                                @Override
//                                public void onSuccess() {
////                                    RecordUploadProxy.uploadLocalDb(getActivity(), null);
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
        LoginHelper.login(SharedPref.getAccount(getActivity()), SharedPref.getPassword(getActivity()), new RequestCallback<UserInfo>() {
            @Override
            public void beforeRequest() {

            }

            @Override
            public void requestFail(String msg) {
                iCallBack.onFail();
            }

            @Override
            public void requestSuccess(UserInfo userInfo) {
                PushProxy.setAlias(userInfo.getUid());
                iCallBack.onSuccess(userInfo);
            }

            @Override
            public void otherDeal(UserInfo info) {
                PushProxy.setAlias(info.getUid());
                iCallBack.onSuccess(info);
                startActivity(new Intent(getActivity(), SetNickNameActivity.class));
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final boolean rootNull = mRootView == null;
        if (rootNull) {
            mRootView = onCreateViewInit(inflater, container,
                    savedInstanceState);
        }
        mUnbinder = ButterKnife.bind(this, mRootView);
        getArgs();
        if (rootNull) {
            initWhenRootViewNull(savedInstanceState);
        }
        return mRootView;
    }

    /**
     * 获取传递的参数
     */
    protected void getArgs() {
    }

    /**
     * 在这里初始化数据
     */
    protected void initWhenRootViewNull(Bundle savedInstanceState) {
    }

    /**
     * true 字符串至少有一个为空
     * false 字符串不为空
     */
    protected boolean isEmpty(String... str) {
        return Utils.isEmpty(str);
    }

    /**
     * 刷新界面
     */
    protected void refreshUi() {

    }

    /**
     * 重写此函数来获取view
     */
    protected abstract View onCreateViewInit(LayoutInflater inflater,
                                             ViewGroup container, Bundle savedInstanceState);


    protected void toastShort(int id) {
        Utils.toastShort(getActivity(), id);
    }

    protected void toastShort(String msg) {
        Utils.toastShort(getActivity(), msg);
    }

    protected boolean isAccountInvalid(int requestCode) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            //无效，去登录
            Intent intent = new Intent(getActivity(), LoginAactivity.class);
            startActivityForResult(intent, requestCode);
            return true;
        }
        return false;
    }

    protected boolean getHttpResSuc(int code) {
        if (Utils.getHttpMsgSu(code)) {
            return true;
        }
        return false;
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
                            SharedPref.saveLoginInfo(getActivity(), userJson);
                            GlobalUser.getInstance().resetUserDataBySharedPref(userJson);
                            GlobalUser.getInstance().setUserData(data);
                            if (iCallBack != null)
                                iCallBack.onSuccess(data);
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

}
