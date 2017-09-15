package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.ReSetSessionProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ModifyPwdDialog extends BaseDialog {

    private static ICallBack<String> mCallBack;

    public static ModifyPwdDialog getInstance(ICallBack<String> callBack) {
        ModifyPwdDialog simpleEditDialog = new ModifyPwdDialog();
        simpleEditDialog.mCallBack = callBack;
        return simpleEditDialog;
    }

    @BindView(R.id.modify_old_pwd)
    EditText oldPwd;
    @BindView(R.id.modify_new_pwd)
    EditText newPwd;
    @BindView(R.id.dialog_simple_btn_confirm)
    TextView confirm;
    @BindView(R.id.dialog_simple_btn_cancel)
    TextView cancel;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null)
            mCallBack = null;
    }

    @Override
    protected int getContentViewLayId() {
        return R.layout.modify_pwd_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onFail();
                    mCallBack = null;
                }
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(newPwd, oldPwd)) {
                    modifyPwd();
                }
            }
        });
    }

    private void modifyPwd() {
        UserData data = GlobalUser.getInstance().getUserData();
        showLoadDialog();
        addToCompositeDis(HttpUtil.modifyPwd(data.getUid(), getEditTxt(oldPwd), getEditTxt(newPwd))
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull final ResultBean bean) throws Exception {
                        String newPwdStr = getEditTxt(newPwd);
                        if (getHttpCodeSucc(bean.getCode())) {
                            if (mCallBack != null) {
                                mCallBack.onSuccess(newPwdStr);
                                mCallBack = null;
                            }
                            SharedPref.savePassword(getActivity(), newPwdStr);
                            //重新登录
                            ReSetSessionProxy.getInstance().login(SharedPref.getAccount(getActivity()), newPwdStr, new ICallBack() {
                                @Override
                                public void onSuccess(Object o) {
                                    toastShort(bean.getMessage());
                                    dismissLoadDialog();
                                    dismiss();
                                }

                                @Override
                                public void onFail() {
                                    dismissLoadDialog();
                                    dismiss();
                                }
                            });
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    private boolean check(EditText newPwd, EditText olePwd) {
        String newStr = getEditTxt(newPwd);
        String oldStr = getEditTxt(olePwd);
        if (TextUtils.isEmpty(oldStr)) {
            toastShort(R.string.str_set_modify_old_pwd);
            return false;
        } else if (TextUtils.isEmpty(newStr)) {
            toastShort(R.string.str_set_modify_new_pwd);
            return false;
        } else if (TextUtils.equals(newStr, oldStr)) {
            toastShort(R.string.str_set_modify_pwd_judge);
            return false;
        }
        return true;
    }
}
