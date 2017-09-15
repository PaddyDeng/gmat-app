package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.intelligent;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by fire on 2017/8/1  14:54.
 */

public class OgNewOrderFragment extends BaseOrderPracticeFragment {

    private boolean viewCreate;

    @Override
    protected boolean isReceiver() {
        return viewCreate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewCreate = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreate = false;
    }

    @Override
    protected int[] getTwoObjId() {
        return new int[]{18};
    }
}
