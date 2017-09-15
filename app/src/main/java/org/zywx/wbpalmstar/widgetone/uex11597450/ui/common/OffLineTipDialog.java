package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;

import butterknife.BindView;

public class OffLineTipDialog extends BaseDialogView {

    @BindView(R.id.close_offline_tip_iv)
    ImageView closeTv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setWindowAnimations(R.style.offline_style);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        closeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_offline_tip_layout;
    }

    @Override
    protected int[] getWH() {
        int[] wh = {(MeasureUtil.getScreenSize(getActivity()).x), getDialog().getWindow().getAttributes().height};
        return wh;
    }

    @Override
    protected boolean isNoTitle() {
        return true;
    }
}
