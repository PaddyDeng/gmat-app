package org.zywx.wbpalmstar.widgetone.uex11597450.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fire on 2017/8/30  14:56.
 */

public class ActionData implements Parcelable {

    /**
     * image : /files/attach/images/20170830/1504065358383998.png
     * word :
     * url : http://m.gmatonline.cn/wap/infoDetails-threeL.html?contentid=1000
     * time : 1504065572
     * maxdisplay : 2
     * id : 1007
     * judge : true
     */

    private String image;
    private String word;
    private String url;
    private String time;
    private int maxdisplay;
    private String id;
    private boolean judge;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMaxdisplay() {
        return maxdisplay;
    }

    public void setMaxdisplay(int maxdisplay) {
        this.maxdisplay = maxdisplay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isJudge() {
        return judge;
    }

    public void setJudge(boolean judge) {
        this.judge = judge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeString(this.word);
        dest.writeString(this.url);
        dest.writeString(this.time);
        dest.writeInt(this.maxdisplay);
        dest.writeString(this.id);
        dest.writeByte(this.judge ? (byte) 1 : (byte) 0);
    }

    public ActionData() {
    }

    protected ActionData(Parcel in) {
        this.image = in.readString();
        this.word = in.readString();
        this.url = in.readString();
        this.time = in.readString();
        this.maxdisplay = in.readInt();
        this.id = in.readString();
        this.judge = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ActionData> CREATOR = new Parcelable.Creator<ActionData>() {
        @Override
        public ActionData createFromParcel(Parcel source) {
            return new ActionData(source);
        }

        @Override
        public ActionData[] newArray(int size) {
            return new ActionData[size];
        }
    };
}
