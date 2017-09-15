package org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class QuestionsData implements Parcelable {
    private int questionid;
    private String question;
    private String questiontitle;
    private String questionselect;
    private int questionselectnumber;
    private String questionanswer;//答案
    private String questionarticle;//问题文章
    private String articletitle;//问题标题
    private List<NetParData> mNetParDatas;//答案解析
    private NetParData netParData;
    private int twoobjectid;
    private int sectionid;
    private String youAnswer;
    private String userTime;
    private boolean whetherCollection;//是否收藏
    private boolean youChooseResult;//你选择的结果

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getYouAnswer() {
        return youAnswer;
    }

    public void setYouAnswer(String youAnswer) {
        this.youAnswer = youAnswer;
    }

    public boolean isYouChooseResult() {
        return youChooseResult;
    }

    public void setYouChooseResult(boolean youChooseResult) {
        this.youChooseResult = youChooseResult;
    }

    public int getQuestionselectnumber() {
        return questionselectnumber;
    }

    public void setQuestionselectnumber(int questionselectnumber) {
        this.questionselectnumber = questionselectnumber;
    }

    public boolean isWhetherCollection() {
        return whetherCollection;
    }

    public void setWhetherCollection(boolean whetherCollection) {
        this.whetherCollection = whetherCollection;
    }

    public NetParData getNetParData() {
        return netParData;
    }

    public void setNetParData(NetParData netParData) {
        this.netParData = netParData;
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

    public List<NetParData> getNetParDatas() {
        return mNetParDatas;
    }

    public void setNetParDatas(List<NetParData> netParDatas) {
        mNetParDatas = netParDatas;
    }

    public String getQuestiontitle() {
        return questiontitle;
    }

    public void setQuestiontitle(String questiontitle) {
        this.questiontitle = questiontitle;
    }

    public String getQuestionarticle() {
        return questionarticle;
    }

    public void setQuestionarticle(String questionarticle) {
        this.questionarticle = questionarticle;
    }

    public String getArticletitle() {
        return articletitle;
    }

    public void setArticletitle(String articletitle) {
        this.articletitle = articletitle;
    }

    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionselect() {
        return questionselect;
    }

    public void setQuestionselect(String questionselect) {
        this.questionselect = questionselect;
    }

    public String getQuestionanswer() {
        return questionanswer;
    }

    public void setQuestionanswer(String questionanswer) {
        this.questionanswer = questionanswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.questionid);
        dest.writeString(this.question);
        dest.writeString(this.questiontitle);
        dest.writeString(this.questionselect);
        dest.writeInt(this.questionselectnumber);
        dest.writeString(this.questionanswer);
        dest.writeString(this.questionarticle);
        dest.writeString(this.articletitle);
        dest.writeTypedList(this.mNetParDatas);
        dest.writeParcelable(this.netParData, flags);
        dest.writeInt(this.twoobjectid);
        dest.writeInt(this.sectionid);
        dest.writeString(this.youAnswer);
        dest.writeString(this.userTime);
        dest.writeByte(this.whetherCollection ? (byte) 1 : (byte) 0);
        dest.writeByte(this.youChooseResult ? (byte) 1 : (byte) 0);
    }

    public QuestionsData() {
    }

    protected QuestionsData(Parcel in) {
        this.questionid = in.readInt();
        this.question = in.readString();
        this.questiontitle = in.readString();
        this.questionselect = in.readString();
        this.questionselectnumber = in.readInt();
        this.questionanswer = in.readString();
        this.questionarticle = in.readString();
        this.articletitle = in.readString();
        this.mNetParDatas = in.createTypedArrayList(NetParData.CREATOR);
        this.netParData = in.readParcelable(NetParData.class.getClassLoader());
        this.twoobjectid = in.readInt();
        this.sectionid = in.readInt();
        this.youAnswer = in.readString();
        this.userTime = in.readString();
        this.whetherCollection = in.readByte() != 0;
        this.youChooseResult = in.readByte() != 0;
    }

    public static final Parcelable.Creator<QuestionsData> CREATOR = new Parcelable.Creator<QuestionsData>() {
        @Override
        public QuestionsData createFromParcel(Parcel source) {
            return new QuestionsData(source);
        }

        @Override
        public QuestionsData[] newArray(int size) {
            return new QuestionsData[size];
        }
    };
}
