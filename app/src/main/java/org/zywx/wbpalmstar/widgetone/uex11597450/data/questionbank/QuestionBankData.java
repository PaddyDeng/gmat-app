package org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class QuestionBankData implements Parcelable {
    private int stid;//表示 stid 或 xuhaoid
    private int twoobjectid;
    private int sectionid;
    private int levelid;
    private int knowsid;
    private String stname;
    private String questionsid;//以逗号隔开的问题id
    private String sectionStr;
    private String twoObjectTypeStr;
    private List<Integer> questionList;
    private int hasMakeTest;//是否做过测试，1 做过了，2 全部做完了    0默认，未开始 C.NO_START

    private int makeSize;//单项练习，已经做过了的题目

    private int knowPointMakeNum;//知识点人数随机数

    public int getMakeSize() {
        return makeSize;
    }

    public void setMakeSize(int makeSize) {
        this.makeSize = makeSize;
    }

    public int getKnowPointMakeNum() {
        return knowPointMakeNum;
    }

    public void setKnowPointMakeNum(int knowPointMakeNum) {
        this.knowPointMakeNum = knowPointMakeNum;
    }

    public int getStid() {
        return stid;
    }

    public void setStid(int stid) {
        this.stid = stid;
    }

    public int getHasMakeTest() {
        return hasMakeTest;
    }

    public void setHasMakeTest(int hasMakeTest) {
        this.hasMakeTest = hasMakeTest;
    }

    public int getLevelid() {
        return levelid;
    }

    public void setLevelid(int levelid) {
        this.levelid = levelid;
    }

    public int getKnowsid() {
        return knowsid;
    }

    public void setKnowsid(int knowsid) {
        this.knowsid = knowsid;
    }

    public List<Integer> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Integer> questionList) {
        this.questionList = questionList;
    }

    public String getSectionStr() {
        return sectionStr;
    }

    public void setSectionStr(String sectionStr) {
        this.sectionStr = sectionStr;
    }

    public String getTwoObjectTypeStr() {
        return twoObjectTypeStr;
    }

    public void setTwoObjectTypeStr(String twoObjectTypeStr) {
        this.twoObjectTypeStr = twoObjectTypeStr;
    }

    public int getTwoobjectid() {
        return twoobjectid;
    }

    public void setTwoobjectid(int twoobjectid) {
        this.twoobjectid = twoobjectid;
    }

    public int getSectionid() {
        return sectionid;
    }

    public void setSectionid(int sectionid) {
        this.sectionid = sectionid;
    }

    public String getStname() {
        return stname;
    }

    public void setStname(String stname) {
        this.stname = stname;
    }

    public String getQuestionsid() {
        return questionsid;
    }

    public void setQuestionsid(String questionsid) {
        this.questionsid = questionsid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.stid);
        dest.writeInt(this.twoobjectid);
        dest.writeInt(this.sectionid);
        dest.writeInt(this.levelid);
        dest.writeInt(this.knowsid);
        dest.writeString(this.stname);
        dest.writeString(this.questionsid);
        dest.writeString(this.sectionStr);
        dest.writeString(this.twoObjectTypeStr);
        dest.writeList(this.questionList);
        dest.writeInt(this.hasMakeTest);
        dest.writeInt(this.makeSize);
        dest.writeInt(this.knowPointMakeNum);
    }

    public QuestionBankData() {
    }

    protected QuestionBankData(Parcel in) {
        this.stid = in.readInt();
        this.twoobjectid = in.readInt();
        this.sectionid = in.readInt();
        this.levelid = in.readInt();
        this.knowsid = in.readInt();
        this.stname = in.readString();
        this.questionsid = in.readString();
        this.sectionStr = in.readString();
        this.twoObjectTypeStr = in.readString();
        this.questionList = new ArrayList<Integer>();
        in.readList(this.questionList, Integer.class.getClassLoader());
        this.hasMakeTest = in.readInt();
        this.makeSize = in.readInt();
        this.knowPointMakeNum = in.readInt();
    }

    public static final Parcelable.Creator<QuestionBankData> CREATOR = new Parcelable.Creator<QuestionBankData>() {
        @Override
        public QuestionBankData createFromParcel(Parcel source) {
            return new QuestionBankData(source);
        }

        @Override
        public QuestionBankData[] newArray(int size) {
            return new QuestionBankData[size];
        }
    };
}
