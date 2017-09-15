package org.zywx.wbpalmstar.widgetone.uex11597450.data.download;

import java.util.List;

public class FileTypeData implements FileLevel {
    private boolean isExpand = true;
    private String typeStr;
    private List<FileInfoData> mFileInfoDataList;

    public List<FileInfoData> getFileInfoDataList() {
        return mFileInfoDataList;
    }

    public void setFileInfoDataList(List<FileInfoData> fileInfoDataList) {
        mFileInfoDataList = fileInfoDataList;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    @Override
    public int getType() {
        return 1;//1级
    }
}
