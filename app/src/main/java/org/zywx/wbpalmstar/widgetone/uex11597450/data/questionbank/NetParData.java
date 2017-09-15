package org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank;

import android.os.Parcel;
import android.os.Parcelable;

public class NetParData implements Parcelable {
    private String p_content;
    private int p_questionid;
    private int userid;
    private String p_time;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getP_time() {
        return p_time;
    }

    public void setP_time(String p_time) {
        this.p_time = p_time;
    }

    public String getP_content() {
        return p_content;
    }

    public void setP_content(String p_content) {
        this.p_content = p_content;
    }

    public int getP_questionid() {
        return p_questionid;
    }

    public void setP_questionid(int p_questionid) {
        this.p_questionid = p_questionid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.p_content);
        dest.writeInt(this.p_questionid);
        dest.writeInt(this.userid);
        dest.writeString(this.p_time);
    }

    public NetParData() {
    }

    protected NetParData(Parcel in) {
        this.p_content = in.readString();
        this.p_questionid = in.readInt();
        this.userid = in.readInt();
        this.p_time = in.readString();
    }

    public static final Parcelable.Creator<NetParData> CREATOR = new Parcelable.Creator<NetParData>() {
        @Override
        public NetParData createFromParcel(Parcel source) {
            return new NetParData(source);
        }

        @Override
        public NetParData[] newArray(int size) {
            return new NetParData[size];
        }
    };
}
