package org.zywx.wbpalmstar.widgetone.uex11597450.receiver;

import android.content.IntentFilter;

import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

@Deprecated
public abstract class CommunityReceiver extends CustomerRefreshReceiver {
    @Override
    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(C.POST_COMMUNITY_REMARK);
        return intentFilter;
    }
}
