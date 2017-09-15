package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ActionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;

import butterknife.BindView;

/**
 * Created by fire on 2017/8/30  14:51.
 */

public class ActionDialog extends BaseNoBackgDialog {

    @BindView(R.id.action_iv)
    ImageView actionIv;
    @BindView(R.id.action_close)
    ImageView actionClose;
    private ActionData mActionData;

    public static ActionDialog getInstance(ActionData actionData) {
        ActionDialog dialog = new ActionDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("action_data", actionData);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle bundle = getArguments();
        if (bundle == null) return;
        mActionData = bundle.getParcelable("action_data");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mActionData != null)
            GlideUtil.loadNoDefalut(RetrofitProvider.BASEURL + mActionData.getImage(), actionIv);

        actionIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionData == null) return;
                DealActivity.startDealActivity(getActivity(), "", mActionData.getUrl());
                dismiss();
            }
        });
        actionClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_action_layout;
    }

    @Override
    protected int[] getWH() {
        int[] wh = {(int) (MeasureUtil.getScreenSize(getActivity()).x * 0.8), getDialog().getWindow().getAttributes().height};
        return wh;
    }

}
