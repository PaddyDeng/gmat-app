package org.zywx.wbpalmstar.widgetone.uex11597450.data.download;

import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.DownloadListAdapter;

public class DownloadEnd implements DownloadType {
    private String title;
    private String savePath;
    private String fileSize;
    private String url;
    private long downloadTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    @Override
    public int getType() {
        return DownloadListAdapter.DOWNLOAD_END_CONTENT;
    }
}
