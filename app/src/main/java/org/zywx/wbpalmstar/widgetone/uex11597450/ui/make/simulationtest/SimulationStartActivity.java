package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.GmatTestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class SimulationStartActivity extends BaseActivity {

    @BindView(R.id.quant_part_container)
    LinearLayout quantContainer;
    @BindView(R.id.verbal_part_container)
    LinearLayout verbalContainer;
    @BindView(R.id.simulation_start_title)
    TextView titleTv;

    private SimulationData mSimulationData;

    public static void startSimulationStart(Context c, SimulationData data) {
        Intent intent = new Intent(c, SimulationStartActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_start);
    }

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null)
            return;
        mSimulationData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        if (mSimulationData == null) return;
        int type = mSimulationData.getType();
        if (type == C.LANGUAGE) {
            Utils.setGone(quantContainer);
            Utils.setVisible(verbalContainer);
        } else if (type == C.MATH) {
            Utils.setGone(verbalContainer);
            Utils.setVisible(quantContainer);
        } else if (type == C.ALL) {
            Utils.setVisible(verbalContainer, quantContainer);
        }
        String title = mSimulationData.getName();
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }

    @OnClick({R.id.simulation_click_start_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simulation_click_start_btn:
                if (mSimulationData == null) return;
                GmatTestActivity.startSimulationStart(mContext, mSimulationData);
                finish();
                break;
            default:
                break;
        }
    }
}
