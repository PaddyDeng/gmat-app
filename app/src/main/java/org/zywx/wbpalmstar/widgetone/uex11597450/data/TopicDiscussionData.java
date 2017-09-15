package org.zywx.wbpalmstar.widgetone.uex11597450.data;

import java.util.List;

/**
 * Created by fire on 2017/8/22  17:07.
 */

public class TopicDiscussionData {

    private List<TopicDiscussionItemData> data;
    private boolean pages;
    private String number;

    public List<TopicDiscussionItemData> getData() {
        return data;
    }

    public void setData(List<TopicDiscussionItemData> data) {
        this.data = data;
    }

    public boolean isPages() {
        return pages;
    }

    public void setPages(boolean pages) {
        this.pages = pages;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
