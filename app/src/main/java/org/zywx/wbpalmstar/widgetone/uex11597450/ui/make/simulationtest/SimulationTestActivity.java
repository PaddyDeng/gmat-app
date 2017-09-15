package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest;

import android.os.Bundle;
import android.view.View;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import butterknife.OnClick;

public class SimulationTestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_test);
    }

    @OnClick({R.id.simulation_language_container, R.id.simulation_math_container, R.id.simulation_all_container})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.simulation_language_container:
                SimulationTestListActivity.startSimulationActivity(mContext, C.LANGUAGE);
                break;
            case R.id.simulation_math_container:
                SimulationTestListActivity.startSimulationActivity(mContext, C.MATH);
                break;
            case R.id.simulation_all_container:
                SimulationTestListActivity.startSimulationActivity(mContext, C.ALL);
                break;
            default:
                break;
        }
    }

}
