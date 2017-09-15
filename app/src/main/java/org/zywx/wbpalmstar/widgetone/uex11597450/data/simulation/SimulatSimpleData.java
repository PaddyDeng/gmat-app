package org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation;

public class SimulatSimpleData {
    private ScoreData credit;
    private int code;
    private String totletime;
    private double correct;
    private String qyesnum;//所有的题目
    private String Qtruenum;//正确的题目

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ScoreData getCredit() {
        return credit;
    }

    public void setCredit(ScoreData credit) {
        this.credit = credit;
    }

    public String getTotletime() {
        return totletime;
    }

    public void setTotletime(String totletime) {
        this.totletime = totletime;
    }

    public double getCorrect() {
        return correct;
    }

    public void setCorrect(double correct) {
        this.correct = correct;
    }

    public String getQyesnum() {
        return qyesnum;
    }

    public void setQyesnum(String qyesnum) {
        this.qyesnum = qyesnum;
    }

    public String getQtruenum() {
        return Qtruenum;
    }

    public void setQtruenum(String qtruenum) {
        Qtruenum = qtruenum;
    }
}
