package org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SimulationData implements Parcelable {

    private int type;//1 2 3 语文，数学，全部
    private String name;
    private String id;
    private String questionids;
    private String basedata;
    private String mkscoreid;
    private String mkscore;
    private String markquestion;//1 开始模考  2查看结果
    private int averscore;
    private int totalusernum;
    private int userlowertk;
    private List<String> basedata_arr;

    public int getUserlowertk() {
        return userlowertk;
    }

    public void setUserlowertk(int userlowertk) {
        this.userlowertk = userlowertk;
    }

    public String getMkscoreid() {
        return mkscoreid;
    }

    public void setMkscoreid(String mkscoreid) {
        this.mkscoreid = mkscoreid;
    }

    public String getMkscore() {
        return mkscore;
    }

    public void setMkscore(String mkscore) {
        this.mkscore = mkscore;
    }

    public String getMarkquestion() {
        return markquestion;
    }

    public void setMarkquestion(String markquestion) {
        this.markquestion = markquestion;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionids() {
        return questionids;
    }

    public void setQuestionids(String questionids) {
        this.questionids = questionids;
    }

    public String getBasedata() {
        return basedata;
    }

    public void setBasedata(String basedata) {
        this.basedata = basedata;
    }

    public int getAverscore() {
        return averscore;
    }

    public void setAverscore(int averscore) {
        this.averscore = averscore;
    }

    public int getTotalusernum() {
        return totalusernum;
    }

    public void setTotalusernum(int totalusernum) {
        this.totalusernum = totalusernum;
    }

    public List<String> getBasedata_arr() {
        return basedata_arr;
    }

    public void setBasedata_arr(List<String> basedata_arr) {
        this.basedata_arr = basedata_arr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.questionids);
        dest.writeString(this.basedata);
        dest.writeString(this.mkscoreid);
        dest.writeString(this.mkscore);
        dest.writeString(this.markquestion);
        dest.writeInt(this.averscore);
        dest.writeInt(this.totalusernum);
        dest.writeInt(this.userlowertk);
        dest.writeStringList(this.basedata_arr);
    }

    public SimulationData() {
    }

    protected SimulationData(Parcel in) {
        this.type = in.readInt();
        this.name = in.readString();
        this.id = in.readString();
        this.questionids = in.readString();
        this.basedata = in.readString();
        this.mkscoreid = in.readString();
        this.mkscore = in.readString();
        this.markquestion = in.readString();
        this.averscore = in.readInt();
        this.totalusernum = in.readInt();
        this.userlowertk = in.readInt();
        this.basedata_arr = in.createStringArrayList();
    }

    public static final Parcelable.Creator<SimulationData> CREATOR = new Parcelable.Creator<SimulationData>() {
        @Override
        public SimulationData createFromParcel(Parcel source) {
            return new SimulationData(source);
        }

        @Override
        public SimulationData[] newArray(int size) {
            return new SimulationData[size];
        }
    };
}
