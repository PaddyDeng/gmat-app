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
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.RequestCallback;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.PushProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.feature.AuthCode;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RegexValidateUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RetrievePwdActivity extends BaseActivity {
    private AuthCode mAuthCode;
    @BindView(R.id.retrieve_authcode)
    TextView retrieveAuthcode;
    @BindView(R.id.retreve_edit_account)
    EditText account;
    @BindView(R.id.authcode_value)
    EditText authCdoeEdit;
    @BindView(R.id.retri_new_pwd)
    EditText newPwd;
    @BindView(R.id.retri_ensure_pwd)
    EditText ensPwd;
    @BindView(R.id.register_btn)
    Button registBtn;
    private String newStr;
    private String ensStr;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_pwd);

    }

    @Override
    protected void initData() {
        super.initData();
        mAuthCode = new AuthCode(60 * 1000, 1000, mContext, retrieveAuthcode);

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
        authCdoeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.length() != 0) {
                    enableClickRegister();
                }
            }
        });
        newPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newStr = s.toString();
                enableClickRegister();
            }
        });
        ensPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ensStr = s.toString();
                enableClickRegister();
            }
        });

        registBtn.setClickable(false);
    }

    private void enableClickRegister() {
        if (judeChange()) {
            registBtn.setClickable(true);
            registBtn.setBackgroundResource(R.drawable.btn_enable_bg);
        } else {
            registBtn.setClickable(false);
            registBtn.setBackgroundResource(R.drawable.login_shape_bg);
        }
    }

    private boolean judeChange() {
        if (TextUtils.isEmpty(getEditText(account))) {
            return false;
        }
        if (TextUtils.isEmpty(getEditText(authCdoeEdit))) {
            return false;
        }
        if (TextUtils.isEmpty(newStr) || TextUtils.isEmpty(ensStr)) {
            return false;
        }
        if (!TextUtils.equals(newStr, ensStr)) {
            return false;
        }
        if (newStr.length() < 5 || newStr.length() > 16) {
            return false;
        }
        return true;
    }


    @OnClick({R.id.retrieve_authcode, R.id.register_btn, R.id.retrieve_has_account})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrieve_has_account:
                finishWithAnim();
                break;
            case R.id.retrieve_authcode:
                clickGetAuthCode();
                break;
            case R.id.register_btn:
                retrievePwd();
                break;
        }
    }

    private void retrievePwd() {
        final String registerStr = getEditText(account);
        if (TextUtils.isEmpty(registerStr)) {
            toastShort(R.string.str_account_error);
            return;
        }
        if (TextUtils.isEmpty(getEditText(authCdoeEdit))) {
            toastShort(R.string.str_enter_auth_code);
            return;
        }
        if (TextUtils.isEmpty(newStr) || TextUtils.isEmpty(ensStr)) {
            toastShort(R.string.str_pwd_not_null);
            return;
        }
        if (!TextUtils.equals(newStr, ensStr)) {
            toastShort(R.string.str_pwd_no_com);
            return;
        }
        if (newStr.length() < 5 || newStr.length() > 16) {
            toastShort(R.string.str_login_pw_hint);
            return;
        }
        if (RegexValidateUtil.checkPhoneNumber(registerStr)) {
            type = 1;
        } else if (RegexValidateUtil.checkEmail(registerStr)) {
            type = 2;
        }
        if (type != 0)
            addToCompositeDis(HttpUtil.retrievePwd(String.valueOf(type), registerStr, newStr, getEditText(authCdoeEdit))
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
                                //重新存储找回成功的用户信息
                                SharedPref.saveAccount(mContext, registerStr);
                                SharedPref.savePassword(mContext, newStr);
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
//
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
        if (RegexValidateUtil.checkPhoneNumber(ac)) {
            type = 1;
            addToCompositeDis(HttpUtil.numGetAuthCode(ac, C.RETRIEVE_TYPE).subscribe(new Consumer<ResultBean>() {
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
            type = 2;
            addToCompositeDis(HttpUtil.emailGetAuthCode(ac, C.RETRIEVE_TYPE).subscribe(new Consumer<ResultBean>() {
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
