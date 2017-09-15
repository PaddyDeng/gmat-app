package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.setting.FeedBackActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import butterknife.BindView;

/**
 * Created by fire on 2017/8/1  17:53.
 */

public class EvaluationDialog extends BaseNoBackgDialog {

    @BindView(R.id.go_app_play_btn)
    TextView goPlay;
    @BindView(R.id.go_feed_back)
    TextView gofeedBack;
    @BindView(R.id.close_evaluation_dialog)
    TextView closeDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.goAppPlay(getActivity());
                dismiss();
            }
        });
        gofeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), FeedBackActivity.class));
                dismiss();
            }
        });
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_evaluation_layout;
    }

    @Override
    protected int[] getWH() {
        int[] wh = {(int) (MeasureUtil.getScreenSize(getActivity()).x * 0.95), getDialog().getWindow().getAttributes().height};
        return wh;
    }
}
