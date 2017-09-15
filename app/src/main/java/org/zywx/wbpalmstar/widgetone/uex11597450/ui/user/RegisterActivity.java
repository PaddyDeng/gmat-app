package org.zywx.wbpalmstar.widgetone.uex11597450.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.RequestCallback;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.PushProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RegexValidateUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.feature.AuthCode;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RegisterActivity extends BaseActivity {

    private AuthCode mAuthCode;
    @BindView(R.id.auth_code_register)
    EditText authCode;
    @BindView(R.id.user_account)
    EditText account;
    @BindView(R.id.register_authcode)
    TextView registAuthCode;
    //    @BindView(R.id.user_name)
//    EditText userName;
    @BindView(R.id.user_psw)
    EditText userPwd;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.user_protocol_check_box)
    CheckBox mCheckBox;
    private String oldAccount;
    private int recoding;//记录用户输入的是电话还是邮箱

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initData() {
        super.initData();
        mAuthCode = new AuthCode(60 * 1000, 1000, mContext, registAuthCode);
        authCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    enableClickRegister();
                }
            }
        });
        account.addTextChangedListener(new TextWatcher() {
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
//        userName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String str = s.toString();
//                if (str.length() != 0) {
//                    enableClickRegister();
//                }
//            }
//        });
        userPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 5 || s.length() > 16) {
                } else {
                    enableClickRegister();
                }
            }
        });
        registerBtn.setClickable(false);
    }

    private void enableClickRegister() {
        if (judeChange()) {
            registerBtn.setClickable(true);
            registerBtn.setBackgroundResource(R.drawable.btn_enable_bg);
        } else {
            registerBtn.setClickable(false);
            registerBtn.setBackgroundResource(R.drawable.login_shape_bg);
        }
    }

    private boolean judeChange() {
        String ac = getEditText(account);
        if (TextUtils.isEmpty(ac)) {
            return false;
        }
        if (TextUtils.isEmpty(getEditText(authCode))) {
            return false;
        }
//        if (TextUtils.isEmpty(getEditText(userName))) {
//            return false;
//        }
        String pwd = getEditText(userPwd);
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }
        if (pwd.length() < 5 || pwd.length() > 16) {
            return false;
        }
        return true;
    }

    @OnClick({R.id.register_authcode, R.id.register_btn, R.id.register_have_account, R.id.user_protocol_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_protocol_tv:
                forword(UserProtocolDealActivity.class);
                break;
            case R.id.register_have_account:
                finishWithAnim();
                break;
            case R.id.register_authcode:
                clickGetAuthCode();
                break;
            case R.id.register_btn:
                registerBtn();
                break;
        }
    }

    private void registerBtn() {
        String ac = getEditText(account);
        if (!mCheckBox.isChecked()) {
            toastShort(R.string.str_read_and_check);
            return;
        }
        if (TextUtils.isEmpty(ac) || !TextUtils.equals(ac, oldAccount)) {
            toastShort(R.string.str_account_error);
            return;
        }
        if (TextUtils.isEmpty(getEditText(authCode))) {
            toastShort(R.string.str_enter_auth_code);
            return;
        }
//        if (TextUtils.isEmpty(getEditText(userName))) {
//            toastShort(R.string.str_name_not_null);
//            return;
//        }
        final String pwd = getEditText(userPwd);
        if (TextUtils.isEmpty(pwd)) {
            toastShort(R.string.str_pwd_not_null);
            return;
        }
        if (pwd.length() < 5 || pwd.length() > 16) {
            toastShort(R.string.str_login_pw_hint);
            return;
        }
        if (RegexValidateUtil.checkPhoneNumber(ac)) {
            recoding = 1;
        } else if (RegexValidateUtil.checkEmail(ac)) {
            recoding = 2;
        }
        if (recoding != 0)
            addToCompositeDis(HttpUtil.register(String.valueOf(recoding), oldAccount, pwd, getEditText(authCode), "android001", "1")
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
                            toastShort(bean.getMessage());
                            if (getHttpResSuc(bean.getCode())) {
                                //需要跳转到主页面
                                SharedPref.saveAccount(mContext, oldAccount);
                                SharedPref.savePassword(mContext, pwd);
                                login(new ICallBack<UserInfo>() {
                                    @Override
                                    public void onSuccess(UserInfo userInfo) {
                                        getUserInfo(userInfo);
                                    }

                                    @Override
                                    public void onFail() {
                                        dismissLoadDialog();
                                    }
                                });
//                                addToCompositeDis(HttpUtil.login(SharedPref.getAccount(mContext), SharedPref.getPassword(mContext))
//                                        .subscribe(new Consumer<UserInfo>() {
//                                            @Override
//                                            public void accept(@NonNull final UserInfo userInfo) throws Exception {
//                                                ReSetSessionProxy.getInstance().resetSession(userInfo, SharedPref.getPassword(mContext), new ReSetSessionProxy.CallBack() {
//                                                    @Override
//                                                    public void onFail() {
//                                                        dismissLoadDialog();
//                                                    }
//
//                                                    @Override
//                                                    public void onSuccess() {
//                                                        getUserInfo(userInfo);
//                                                    }
//                                                });
//                                            }
//                                        }, new Consumer<Throwable>() {
//                                            @Override
//                                            public void accept(@NonNull Throwable throwable) throws Exception {
//                                                dismissLoadDialog();
//                                            }
//                                        }));
                            } else {
                                dismissLoadDialog();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {

                        }
                    }));

    }

    private void getUserInfo(final UserInfo userInfo) {
        getUserInfo(new ICallBack<UserData>() {
            @Override
            public void onSuccess(UserData data) {
                dismissLoadDialog();
                PushProxy.setAlias(userInfo.getUid());
                toastShort(userInfo.getMessage());
                if (getHttpResSuc(userInfo.getCode())) {
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.LOGIN_SUCCESS));
                    finishWithAnim();
//                    forword(MainActivity.class);
                }
            }

            @Override
            public void onFail() {
                dismissLoadDialog();
            }
        });
    }

    public void clickGetAuthCode() {
        String ac = getEditText(account);
        if (TextUtils.isEmpty(ac)) {
            toastShort(R.string.str_register_account);
            return;
        }
        mAuthCode.start();
        oldAccount = ac;
        if (RegexValidateUtil.checkPhoneNumber(ac)) {
            recoding = 1;
            addToCompositeDis(HttpUtil.numGetAuthCode(ac, C.REGISTER_TYPE).subscribe(new Consumer<ResultBean>() {
                @Override
                public void accept(@NonNull ResultBean o) throws Exception {
                    if (!getHttpResSuc(o.getCode())) {
                        mAuthCode.sendAgain();
                        toastShort(o.getMessage());
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    errorTip(throwable);
                    mAuthCode.sendAgain();
                }
            }));
        } else if (RegexValidateUtil.checkEmail(ac)) {
            recoding = 2;
            addToCompositeDis(HttpUtil.emailGetAuthCode(ac, C.REGISTER_TYPE).subscribe(new Consumer<ResultBean>() {
                @Override
                public void accept(@NonNull ResultBean o) throws Exception {
                    if (!getHttpResSuc(o.getCode())) {
                        mAuthCode.sendAgain();
                        toastShort(o.getMessage());
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    errorTip(throwable);
                    mAuthCode.sendAgain();
                }
            }));
        } else {
            mAuthCode.sendAgain();
            recoding = 0;
            toastShort(R.string.str_account_format_error);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthCode != null)
            mAuthCode.destory();
    }
}
