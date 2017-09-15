package org.zywx.wbpalmstar.widgetone.uex11597450.data.download;

import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.DownloadListAdapter;

public class DownloadEndTitle implements DownloadType {
    private String mDownloadEndTitle;

    public String getDownloadEndTitle() {
        return mDownloadEndTitle;
    }

    public void setDownloadEndTitle(String downloadEndTitle) {
        mDownloadEndTitle = downloadEndTitle;
    }

    @Override
    public int getType() {
        return DownloadListAdapter.DOWNLOAD_END_TITLE;
    }
}
