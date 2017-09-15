package org.zywx.wbpalmstar.widgetone.uex11597450.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.JsonUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SetNickNameActivity extends BaseActivity {

    @BindView(R.id.act_set_nick_name_tv)
    EditText setNickTv;
    @BindView(R.id.act_set_nick_confirm_btn)
    TextView confirmBtn;
    private boolean hasNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nick_name);
    }

    @Override
    protected void initView() {
        String nickname = GlobalUser.getInstance().getUserData().getNickname();
        if (!TextUtils.isEmpty(nickname)) {
            hasNickName = true;
        }
        setNickTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enable(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void enable(String s) {
        if (s.length() >= 2 && s.length() <= 8) {
            confirmBtn.setClickable(true);
            confirmBtn.setTextColor(getResources().getColor(R.color.color_white));
        } else {
            confirmBtn.setClickable(false);
            confirmBtn.setTextColor(getResources().getColor(R.color.color_gray));
        }
    }

    @OnClick({R.id.act_set_nick_confirm_btn, R.id.act_set_nick_cancel_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.act_set_nick_confirm_btn:
                setNickName();
                break;
            case R.id.act_set_nick_cancel_btn:
                if (!hasNickName) {
                    toastShort(R.string.str_set_nick_title);
                    return;
                }
                finishWithAnim();
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean preBackExitPage() {
        if (!hasNickName) {
            toastShort(R.string.str_set_nick_title);
            return true;
        }
        return super.preBackExitPage();
    }

    private boolean checkName(final EditText editText) {
        String name = Utils.getEditTextString(editText);
        if (TextUtils.isEmpty(name)) {
            toastShort(R.string.str_set_nick_name_tip);
            return false;
        } else if (name.equals(GlobalUser.getInstance().getUserData().getNickname())) {
            toastShort(R.string.str_set_modify_com);
            return false;
        }
        return true;
    }

    private void setNickName() {
        if (!checkName(setNickTv))
            return;
        final String nickName = getEditText(setNickTv);
        addToCompositeDis(HttpUtil.newModifyName(nickName)
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
                        setResult(RESULT_OK);
                        dismissLoadDialog();
                        toastShort(bean.getMessage());
                        if (Utils.getHttpMsgSu(bean.getCode())) {
                            saveUserInf(nickName);
                            hasNickName = true;
                            finishWithAnim();
                            RxBus.get().post(C.CENTER_CHANGE, C.NICKNAME_CHANGE);
//                            resetSession();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void saveUserInf(String name) {
        if (!TextUtils.isEmpty(name)) {
            GlobalUser.getInstance().setNickName(name);
        }
        SharedPref.saveLoginInfo(mContext, JsonUtil.toJson(GlobalUser.getInstance().getUserData()));
    }

    private void resetSession() {
        UserInfo userInfo = new UserInfo();
        UserData data = GlobalUser.getInstance().getUserData();
        userInfo.setUid(data.getUid());
        userInfo.setUsername(data.getUsername());
        userInfo.setPassword(SharedPref.getPassword(mContext));
        userInfo.setEmail(data.getUseremail());
        userInfo.setPhone(data.getPhone());
        userInfo.setNickname(data.getNickname());
        ReSetSessionProxy.getInstance().resetSession(userInfo, SharedPref.getPassword(mContext), new ReSetSessionProxy.CallBack() {
            @Override
            public void onFail() {
                dismissLoadDialog();
            }

            @Override
            public void onSuccess() {
                dismissLoadDialog();
                finishWithAnim();
            }
        });
    }
}
