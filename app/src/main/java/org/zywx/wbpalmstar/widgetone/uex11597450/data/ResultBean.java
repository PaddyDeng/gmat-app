package org.zywx.wbpalmstar.widgetone.uex11597450.data;


import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.QuestionRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.ScoreData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.Banner;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.HotData;

import java.util.ArrayList;
import java.util.List;

public class ResultBean<T> {
    private int code;
    private String message;
    private T data;
    private String image;
    private String msg;

    private String url;//点击广告跳转链接    //更新本地数据库返回来的url
    private int time;//显示时间倒计时       //更新本地数据库返回来的更新时间
    private boolean judge;//判断广告是否需要显示，true显示

    //========做题备考title=======
    private String verbalNum;
    private String quantNum;
    //===============
    private String recordPath;

    private List<Banner> banner;

    private ScoreData credit;
    private String totletime;
    private double correct;
    private String qyesnum;//所有的题目
    private String Qtruenum;//正确的题目
    private ArrayList<QuestionRecordData> questionrecord;//已做过的题目记录
    private List<HotData> hotClass;
    private List<MsgData> messageslist;
    private String num;
    private String sign;//继续模考
    private int showtime;//模考休息时间

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getShowtime() {
        return showtime;
    }

    public void setShowtime(int showtime) {
        this.showtime = showtime;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isJudge() {
        return judge;
    }

    public void setJudge(boolean judge) {
        this.judge = judge;
    }

    public String getVerbalNum() {
        return verbalNum;
    }

    public void setVerbalNum(String verbalNum) {
        this.verbalNum = verbalNum;
    }

    public String getQuantNum() {
        return quantNum;
    }

    public void setQuantNum(String quantNum) {
        this.quantNum = quantNum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<MsgData> getMessageslist() {
        return messageslist;
    }

    public void setMessageslist(List<MsgData> messageslist) {
        this.messageslist = messageslist;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    public List<HotData> getHotClass() {
        return hotClass;
    }

    public void setHotClass(List<HotData> hotClass) {
        this.hotClass = hotClass;
    }

    public ArrayList<QuestionRecordData> getQuestionrecord() {
        return questionrecord;
    }

    public void setQuestionrecord(ArrayList<QuestionRecordData> questionrecord) {
        this.questionrecord = questionrecord;
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

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
