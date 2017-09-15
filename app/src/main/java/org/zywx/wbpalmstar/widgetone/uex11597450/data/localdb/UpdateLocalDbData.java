package org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb;

import java.util.List;

/**
 * Created by fire on 2017/8/18  11:18.
 */

public class UpdateLocalDbData {
    private List<LocalQuestionData> question;
    private List<LocalParseData> parse;
    private List<LocalTikuData> tiku;
    private List<LocalSerialTiku> xuhao;
    private List<LocalSerial> xuhaoquestion;

    public List<LocalSerialTiku> getXuhao() {
        return xuhao;
    }

    public void setXuhao(List<LocalSerialTiku> xuhao) {
        this.xuhao = xuhao;
    }

    public List<LocalSerial> getXuhaoquestion() {
        return xuhaoquestion;
    }

    public void setXuhaoquestion(List<LocalSerial> xuhaoquestion) {
        this.xuhaoquestion = xuhaoquestion;
    }

    public List<LocalQuestionData> getQuestion() {
        return question;
    }

    public void setQuestion(List<LocalQuestionData> question) {
        this.question = question;
    }

    public List<LocalParseData> getParse() {
        return parse;
    }

    public void setParse(List<LocalParseData> parse) {
        this.parse = parse;
    }

    public List<LocalTikuData> getTiku() {
        return tiku;
    }

    public void setTiku(List<LocalTikuData> tiku) {
        this.tiku = tiku;
    }
}
