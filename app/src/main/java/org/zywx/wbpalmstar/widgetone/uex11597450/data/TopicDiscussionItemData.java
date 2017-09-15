package org.zywx.wbpalmstar.widgetone.uex11597450.data;

import java.util.List;

/**
 * Created by fire on 2017/8/24  15:41.
 */

public class TopicDiscussionItemData {

    private String commentid;
    private String userid;
    private String username;
    private String content;
    private String c_time;
    private String img;
    private String questionid;
    private String pid;
    private String nickname;
    private int num;
    private String photo;
    private List<TopicDiscussionItemData> son;
    private List<TopicDiscussionItemData> multSon;
    private int recyclePosition;

    public int getRecyclePosition() {
        return recyclePosition;
    }

    public void setRecyclePosition(int recyclePosition) {
        this.recyclePosition = recyclePosition;
    }

    public List<TopicDiscussionItemData> getMultSon() {
        return multSon;
    }

    public void setMultSon(List<TopicDiscussionItemData> multSon) {
        this.multSon = multSon;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getC_time() {
        return c_time;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<TopicDiscussionItemData> getSon() {
        return son;
    }

    public void setSon(List<TopicDiscussionItemData> son) {
        this.son = son;
    }

}
