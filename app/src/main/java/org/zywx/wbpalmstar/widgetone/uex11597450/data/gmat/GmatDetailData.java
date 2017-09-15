package org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat;

import android.os.Parcel;
import android.os.Parcelable;

public class GmatDetailData implements Parcelable {

    private String headUrl;
    private String title;
    private String number;
    private int rating;
    private String des;
    private String url;//试听地址

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headUrl);
        dest.writeString(this.title);
        dest.writeString(this.number);
        dest.writeInt(this.rating);
        dest.writeString(this.des);
        dest.writeString(this.url);
    }

    public GmatDetailData() {
    }

    protected GmatDetailData(Parcel in) {
        this.headUrl = in.readString();
        this.title = in.readString();
        this.number = in.readString();
        this.rating = in.readInt();
        this.des = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<GmatDetailData> CREATOR = new Parcelable.Creator<GmatDetailData>() {
        @Override
        public GmatDetailData createFromParcel(Parcel source) {
            return new GmatDetailData(source);
        }

        @Override
        public GmatDetailData[] newArray(int size) {
            return new GmatDetailData[size];
        }
    };
}
