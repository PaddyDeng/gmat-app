package org.zywx.wbpalmstar.widgetone.uex11597450.receiver;

import android.content.IntentFilter;

import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

public abstract class ThrLoginReceiver extends CustomerRefreshReceiver {
    @Override
    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(C.THR_LOGIN_ACTION);
        return intentFilter;
    }
}
