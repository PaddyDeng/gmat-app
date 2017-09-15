package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.JsonUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 简单的编辑对话框
 */
public class SimpleEditDialog extends BaseDialog {

    private static ICallBack<String> mCallBack;

    public static SimpleEditDialog getInstance(ICallBack<String> callBack) {
        SimpleEditDialog simpleEditDialog = new SimpleEditDialog();
        simpleEditDialog.mCallBack = callBack;
        return simpleEditDialog;
    }

    @BindView(R.id.simple_dialog_et_content)
    EditText etName;
    @BindView(R.id.dialog_simple_btn_cancel)
    TextView cancelTxt;
    @BindView(R.id.dialog_simple_btn_confirm)
    TextView confirmTxt;

    @Override
    protected int getContentViewLayId() {
        return R.layout.modify_nick_dialog_bg;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null)
            mCallBack = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkName(etName)) {
                    updateNickName(etName);
                }
            }
        });
        cancelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onFail();
                    mCallBack = null;
                }
                dismiss();
            }
        });
    }


    private void updateNickName(EditText etName) {
        final String name = Utils.getEditTextString(etName);
        showLoadDialog();
        addToCompositeDis(HttpUtil.modifyName(name)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoadDialog();
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
                        if (Utils.getHttpMsgSu(bean.getCode())) {
                            if (null != mCallBack) {
                                mCallBack.onSuccess(name);
                                mCallBack = null;
                            }
                            saveUserInf(name);
                            dismiss();
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
        SharedPref.saveLoginInfo(getActivity(), JsonUtil.toJson(GlobalUser.getInstance().getUserData()));
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

}
