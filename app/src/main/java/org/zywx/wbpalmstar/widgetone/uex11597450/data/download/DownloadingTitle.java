package org.zywx.wbpalmstar.widgetone.uex11597450.data.download;

import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.DownloadListAdapter;

public class DownloadingTitle implements DownloadType {
    private String mDownloadingTitle;

    public String getDownloadingTitle() {
        return mDownloadingTitle;
    }

    public void setDownloadingTitle(String downloadingTitle) {
        mDownloadingTitle = downloadingTitle;
    }

    @Override
    public int getType() {
        return DownloadListAdapter.DOWNLOADING_TITLE;
    }
}
