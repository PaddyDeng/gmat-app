package org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo;


import android.os.Bundle;

import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

public class RCFragment extends BaseRecordFragment {
    private int type;

    public static RCFragment getInstance(int type) {
        RCFragment baseRecordFragment = new RCFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        baseRecordFragment.setArguments(bundle);
        return baseRecordFragment;
    }

    @Override
    protected void getArgs() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt("type");
        }
    }


    @Override
    protected int getSectionId() {
        return C.RC;
    }

    @Override
    protected int getType() {
        return type;
    }
}
