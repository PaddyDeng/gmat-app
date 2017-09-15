package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;

import butterknife.BindView;

public class SimpleShowDialog extends BaseDialog {

    private static ICallBack<String> mCallBack;
    private static final String SIMPLE_DIALOG_TITLE = "simple_dialog_title";
    private static final String SIMPLE_DIALOG_CONTENT = "simple_dialog_content";

    public static SimpleShowDialog getInstance(String title, String content, ICallBack<String> callBack) {
        SimpleShowDialog simpleDialog = new SimpleShowDialog();
        simpleDialog.mCallBack = callBack;
        Bundle bundle = new Bundle();
        bundle.putString(SIMPLE_DIALOG_TITLE, title);
        bundle.putString(SIMPLE_DIALOG_CONTENT, content);
        simpleDialog.setArguments(bundle);
        return simpleDialog;
    }

    @BindView(R.id.dialog_simple_btn_cancel)
    TextView cancelTxt;
    @BindView(R.id.dialog_simple_btn_confirm)
    TextView confirmTxt;
    @BindView(R.id.simple_dialog_title)
    TextView titleTv;
    @BindView(R.id.simple_dialog_content)
    TextView contentTv;

    private String title;

    @Override
    protected int getContentViewLayId() {
        return R.layout.simple_show_dialog;
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle arguments = getArguments();
        if (arguments == null) return;
        title = arguments.getString(SIMPLE_DIALOG_TITLE);
        contentTv.setText(arguments.getString(SIMPLE_DIALOG_CONTENT));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        } else {
            Utils.setGone(titleTv);
        }
        confirmTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onSuccess("");
                    mCallBack = null;
                }
                dismiss();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null)
            mCallBack = null;
    }
}
