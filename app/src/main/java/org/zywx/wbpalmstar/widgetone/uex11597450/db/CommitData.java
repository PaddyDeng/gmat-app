package org.zywx.wbpalmstar.widgetone.uex11597450.db;

import java.io.Serializable;

public class CommitData implements Serializable {

    private static final long serialVersionUID = 4929663094L;

    private String userid;
    private int stid;
    private long questionSubmitTime;
    private long duration;
    private int questionIsRight;//0表示false
    private int questionId;
    private String questionUserAnswer;
    private int exerciseState;
    private int xuhaotikuId;

    public int getXuhaotikuId() {
        return xuhaotikuId;
    }

    public void setXuhaotikuId(int xuhaotikuId) {
        this.xuhaotikuId = xuhaotikuId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getStid() {
        return stid;
    }

    public void setStid(int stid) {
        this.stid = stid;
    }

    public long getQuestionSubmitTime() {
        return questionSubmitTime;
    }

    public void setQuestionSubmitTime(long questionSubmitTime) {
        this.questionSubmitTime = questionSubmitTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getQuestionIsRight() {
        return questionIsRight;
    }

    public void setQuestionIsRight(int questionIsRight) {
        this.questionIsRight = questionIsRight;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionUserAnswer() {
        return questionUserAnswer;
    }

    public void setQuestionUserAnswer(String questionUserAnswer) {
        this.questionUserAnswer = questionUserAnswer;
    }

    public int getExerciseState() {
        return exerciseState;
    }

    public void setExerciseState(int exerciseState) {
        this.exerciseState = exerciseState;
    }
}
