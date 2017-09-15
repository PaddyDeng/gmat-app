
package org.zywx.wbpalmstar.widgetone.uex11597450.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class CustomerRefreshReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        performAction(intent);
    }

    public abstract IntentFilter getIntentFilter();

    protected abstract void performAction(Intent intent);
}
