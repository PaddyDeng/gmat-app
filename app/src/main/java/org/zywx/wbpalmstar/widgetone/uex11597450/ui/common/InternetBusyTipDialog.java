package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.update.UpdateNewDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;

import butterknife.BindView;

/**
 * Created by fire on 2017/8/1  16:42.
 */

public class InternetBusyTipDialog extends BaseNoBackgDialog {

    private static ICallBack mCallBack;

    public static InternetBusyTipDialog getInstance(ICallBack callBack) {
        InternetBusyTipDialog simpleDialog = new InternetBusyTipDialog();
        simpleDialog.mCallBack = callBack;
        return simpleDialog;
    }

    @BindView(R.id.internet_poor_iv)
    ImageView img;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onSuccess("");
                    mCallBack = null;
                }
                dismiss();
            }
        });
    }

    @Override
    protected int[] getWH() {
        int[] wh = {MeasureUtil.getScreenSize(getActivity()).x, MeasureUtil.getScreenSize(getActivity()).y};
        return wh;
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_internet_busy_layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCallBack != null) {
            mCallBack = null;
        }
    }
}
