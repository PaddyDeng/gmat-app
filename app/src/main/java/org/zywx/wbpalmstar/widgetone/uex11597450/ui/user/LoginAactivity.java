package org.zywx.wbpalmstar.widgetone.uex11597450.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.RequestCallback;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.PushProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.LoginSuccessReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.ThrLoginReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RegexValidateUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.ShareLogin;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginAactivity extends BaseActivity {

    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.login_user)
    EditText loginAccount;
    @BindView(R.id.login_pwd)
    EditText loginPwd;

    private ThrLoginReceiver mReceiver = new ThrLoginReceiver() {
        @Override
        protected void performAction(Intent intent) {
            thrInfoLogin(SharedPref.getThrIcon(mContext), SharedPref.getThrId(mContext), SharedPref.getThrName(mContext));
        }
    };

    private LoginSuccessReceiver mSuccessReceiver = new LoginSuccessReceiver() {
        @Override
        protected void performAction(Intent intent) {
            finishWithAnim();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_aactivity);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, mReceiver.getIntentFilter());
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mSuccessReceiver, mSuccessReceiver.getIntentFilter());
    }

    @Override
    protected void onStart() {
        super.onStart();
        String account = SharedPref.getAccount(mContext);
        String password = SharedPref.getPassword(mContext);
        if (!Utils.isEmpty(account, password)) {
            loginAccount.setText(account);
            loginAccount.setSelection(account.length());
            loginPwd.setText(password);
            loginBtn.setClickable(true);
            loginBtn.setBackgroundResource(R.drawable.btn_enable_bg);
        }
    }

    @Override
    protected void initData() {
        super.initData();

        loginAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (RegexValidateUtil.checkEmail(str) || RegexValidateUtil.checkPhoneNumber(str)) {
                    enableClickRegister();
                }
            }
        });
        loginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableClickRegister();
            }

        });
        loginBtn.setClickable(false);

    }

    private void enableClickRegister() {
        if (judeChange()) {
            loginBtn.setClickable(true);
            loginBtn.setBackgroundResource(R.drawable.btn_enable_bg);
        } else {
            loginBtn.setClickable(false);
            loginBtn.setBackgroundResource(R.drawable.login_shape_bg);
        }
    }

    private boolean judeChange() {
        String ac = getEditText(loginAccount);
        if (TextUtils.isEmpty(ac)) {
            return false;
        }
        String pwd = getEditText(loginPwd);
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }
        if (pwd.length() < 5 || pwd.length() > 16) {
            return false;
        }
        return true;
    }

    @OnClick({R.id.immediately_regist, R.id.use_qq_login, R.id.forgive_pwd, R.id.login_btn})
    public void onCLick(View v) {
        switch (v.getId()) {
            case R.id.immediately_regist:
                forword(RegisterActivity.class);
                break;
            case R.id.use_qq_login:
                shareLogin();
                break;
            case R.id.forgive_pwd:
                forword(RetrievePwdActivity.class);
                break;
            case R.id.login_btn:
                login();
                break;
        }
    }

    private void login() {
        String name = getEditText(loginAccount);
        if (TextUtils.isEmpty(name)) {
            toastShort(R.string.str_name_not_null);
            return;
        }
        String pwd = getEditText(loginPwd);
        if (TextUtils.isEmpty(pwd)) {
            toastShort(R.string.str_pwd_not_null);
            return;
        }
        SharedPref.saveAccount(mContext, name);
        SharedPref.savePassword(mContext, pwd);
        LoginHelper.login(name, pwd, new RequestCallback<UserInfo>() {
            @Override
            public void beforeRequest() {
                showLoadDialog();
            }

            @Override
            public void requestFail(String msg) {
                dismissLoadDialog();
                toastShort(msg);
            }

            @Override
            public void requestSuccess(UserInfo userInfo) {
                getUserInfo(userInfo);
            }

            @Override
            public void otherDeal(UserInfo info) {
                dismissLoadDialog();
                getUserInfo(info);
                forword(SetNickNameActivity.class);
            }
        });
//        addToCompositeDis(HttpUtil.login(name, pwd)
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        showLoadDialog();
//                    }
//                }).doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        dismissLoadDialog();
//                    }
//                })
//                .subscribe(new Consumer<UserInfo>() {
//                    @Override
//                    public void accept(@NonNull final UserInfo userInfo) throws Exception {
//                        if (userInfo == null) {
//                            //提示用户登录失败
//                            dismissLoadDialog();
//                            return;
//                        }
//                        if (!getHttpResSuc(userInfo.getCode())) {
//                            toastShort(userInfo.getMessage());
//                            dismissLoadDialog();
//                            return;
//                        }
//                        ReSetSessionProxy.getInstance().resetSession(userInfo, SharedPref.getPassword(mContext), new ReSetSessionProxy.CallBack() {
//                            @Override
//                            public void onFail() {
//                                dismissLoadDialog();
//                            }
//
//                            @Override
//                            public void onSuccess() {
//                                getUserInfo(userInfo);
//                            }
//                        });
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
////                        throwable.printStackTrace();
//                    }
//                }));
    }


    private void getUserInfo(final UserInfo userInfo) {
        getUserInfo(new ICallBack<UserData>() {
            @Override
            public void onSuccess(UserData data) {
                dismissLoadDialog();
                toastShort(userInfo.getMessage());
                PushProxy.setAlias(userInfo.getUid());
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.LOGIN_SUCCESS));
                finishWithAnim();
            }

            @Override
            public void onFail() {
                dismissLoadDialog();
            }
        });
    }

    private void shareLogin() {
        ShareLogin.shareLogin(mContext, new ICallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> list) {
//                thrInfoLogin(list);
//                toastShort(getString(R.string.str_login_loading));
                SharedPref.saveAccount(mContext, "");
                SharedPref.savePassword(mContext, "");
//                setResult(RESULT_OK);
                finishWithAnim();
            }

            @Override
            public void onFail() {
                toastShort(getString(R.string.str_login_onfail));
            }
        });
    }

    private void thrInfoLogin(String iconUrl, String userId, String userName) {
        //开始登录了
        showLoadDialog();
        addToCompositeDis(HttpUtil.thrLogin(userId, userName, iconUrl)
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(@NonNull final UserInfo userInfo) throws Exception {
                        if (!Utils.getHttpMsgSu(userInfo.getCode())) {
                            dismissLoadDialog();
                            return;
                        }
                        ReSetSessionProxy.getInstance().resetSession(userInfo, null, new ReSetSessionProxy.CallBack() {
                            @Override
                            public void onFail() {
                                log("on fail");
                                dismissLoadDialog();
                            }

                            @Override
                            public void onSuccess() {
                                toastShort(userInfo.getMessage());
                                dismissLoadDialog();
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.LOGIN_SUCCESS));
//                                setResult(RESULT_OK);
//                                PushProxy.setAlias(userInfo.getUid());
                                //第三方登录
                                SharedPref.saveAccount(mContext, "");
                                SharedPref.savePassword(mContext, "");
                                finishWithAnim();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mSuccessReceiver);
        ShareLogin.dispose();
    }
}
