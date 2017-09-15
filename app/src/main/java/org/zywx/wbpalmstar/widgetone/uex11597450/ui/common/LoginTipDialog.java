package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.LoginAactivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;

import butterknife.BindView;

public class LoginTipDialog extends BaseDialogView {
    @Override
    protected int getRootViewId() {
        return R.layout.login_tip_dialog_layout;
    }

    @BindView(R.id.place_close)
    ImageView close;
    @BindView(R.id.user_login_btn)
    TextView login;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginAactivity.class));
                dismiss();
            }
        });
    }

    @Override
    protected int[] getWH() {
        int[] wh = {(int) (MeasureUtil.getScreenSize(getActivity()).x * 0.6), getDialog().getWindow().getAttributes().height};
        return wh;
    }

    @Override
    protected boolean isNoTitle() {
        return true;
    }

}
