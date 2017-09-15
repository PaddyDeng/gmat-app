package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class PrepFragment extends BaseSimulationFragment {

    private boolean canRe;

    @Override
    protected boolean couldRefresh() {
        return canRe;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        canRe = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canRe = false;
    }

    @Override
    protected String getTypeClass() {
        return "p";
    }
}
