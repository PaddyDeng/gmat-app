package org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimulationTopicData {

    private String showfly;
    private int mkid;
    private MktimeBean mktime;
    private Object step;
    private DataBean data;
    private String qnonum;
    private int qnyesum;
    private String qallnum;
    private String mark;
    private int code;
    private String mkscoreid;
    private int hrefType;

    private String name;
    private int showtime;

    @SerializedName("do")
    private int doMakeNum;
    private int all;

    public String getMkscoreid() {
        return mkscoreid;
    }

    public void setMkscoreid(String mkscoreid) {
        this.mkscoreid = mkscoreid;
    }

    public int getDoMakeNum() {
        return doMakeNum;
    }

    public void setDoMakeNum(int doMakeNum) {
        this.doMakeNum = doMakeNum;
    }

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShowtime() {
        return showtime;
    }

    public void setShowtime(int showtime) {
        this.showtime = showtime;
    }

    public int getHrefType() {
        return hrefType;
    }

    public void setHrefType(int hrefType) {
        this.hrefType = hrefType;
    }

    public String getShowfly() {
        return showfly;
    }

    public void setShowfly(String showfly) {
        this.showfly = showfly;
    }

    public int getMkid() {
        return mkid;
    }

    public void setMkid(int mkid) {
        this.mkid = mkid;
    }

    public MktimeBean getMktime() {
        return mktime;
    }

    public void setMktime(MktimeBean mktime) {
        this.mktime = mktime;
    }

    public Object getStep() {
        return step;
    }

    public void setStep(Object step) {
        this.step = step;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getQnonum() {
        return qnonum;
    }

    public void setQnonum(String qnonum) {
        this.qnonum = qnonum;
    }

    public int getQnyesum() {
        return qnyesum;
    }

    public void setQnyesum(int qnyesum) {
        this.qnyesum = qnyesum;
    }

    public String getQallnum() {
        return qallnum;
    }

    public void setQallnum(String qallnum) {
        this.qallnum = qallnum;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

//    public UserDataBean getUserData() {
//        return userData;
//    }
//
//    public void setUserData(UserDataBean userData) {
//        this.userData = userData;
//    }

    public static class MktimeBean {

        private int lasttime;
        private String day;
        private String hour;
        private int minute;
        private int second;

        public int getLasttime() {
            return lasttime;
        }

        public void setLasttime(int lasttime) {
            this.lasttime = lasttime;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }

    public static class DataBean {
        private Object answerid;
        private Object qanswer;
        private Object answertime;
        private String questionid;
        private String questiontype;
        private String question;
        private String questiontitle;
        private String questionuserid;
        private String questionusername;
        private String questionlastmodifyuser;
        private String questionselect;
        private String questionselectnumber;
        private String questionanswer;
        private String questiondescribe;
        private String questioncreatetime;
        private String questionstatus;
        private boolean questionhtml;
        private String questionparent;
        private String questionsequence;
        private String questionlevel;
        private String oneobjecttype;
        private String twoobjecttype;
        private String subjecttype;
        private String sectiontype;
        private String questionarticle;
        private String articletitle;
        private String mathfoundation;
        private String views;
        private String comments;
        private String discusstime;
        private String discussmark;
        private String readArticleId;
        private Object articleContent;
        private String questionNumber;
        private String twoname;
        private String sections;
        private String level;
        private String level_s;
        private String qtitle;
        private int collect;
        private List<QslctarrBean> qslctarr;

        public Object getAnswerid() {
            return answerid;
        }

        public void setAnswerid(Object answerid) {
            this.answerid = answerid;
        }

        public Object getQanswer() {
            return qanswer;
        }

        public void setQanswer(Object qanswer) {
            this.qanswer = qanswer;
        }

        public Object getAnswertime() {
            return answertime;
        }

        public void setAnswertime(Object answertime) {
            this.answertime = answertime;
        }

        public String getQuestionid() {
            return questionid;
        }

        public void setQuestionid(String questionid) {
            this.questionid = questionid;
        }

        public String getQuestiontype() {
            return questiontype;
        }

        public void setQuestiontype(String questiontype) {
            this.questiontype = questiontype;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getQuestiontitle() {
            return questiontitle;
        }

        public void setQuestiontitle(String questiontitle) {
            this.questiontitle = questiontitle;
        }

        public String getQuestionuserid() {
            return questionuserid;
        }

        public void setQuestionuserid(String questionuserid) {
            this.questionuserid = questionuserid;
        }

        public String getQuestionusername() {
            return questionusername;
        }

        public void setQuestionusername(String questionusername) {
            this.questionusername = questionusername;
        }

        public String getQuestionlastmodifyuser() {
            return questionlastmodifyuser;
        }

        public void setQuestionlastmodifyuser(String questionlastmodifyuser) {
            this.questionlastmodifyuser = questionlastmodifyuser;
        }

        public String getQuestionselect() {
            return questionselect;
        }

        public void setQuestionselect(String questionselect) {
            this.questionselect = questionselect;
        }

        public String getQuestionselectnumber() {
            return questionselectnumber;
        }

        public void setQuestionselectnumber(String questionselectnumber) {
            this.questionselectnumber = questionselectnumber;
        }

        public String getQuestionanswer() {
            return questionanswer;
        }

        public void setQuestionanswer(String questionanswer) {
            this.questionanswer = questionanswer;
        }

        public String getQuestiondescribe() {
            return questiondescribe;
        }

        public void setQuestiondescribe(String questiondescribe) {
            this.questiondescribe = questiondescribe;
        }

        public String getQuestioncreatetime() {
            return questioncreatetime;
        }

        public void setQuestioncreatetime(String questioncreatetime) {
            this.questioncreatetime = questioncreatetime;
        }

        public String getQuestionstatus() {
            return questionstatus;
        }

        public void setQuestionstatus(String questionstatus) {
            this.questionstatus = questionstatus;
        }

        public boolean isQuestionhtml() {
            return questionhtml;
        }

        public void setQuestionhtml(boolean questionhtml) {
            this.questionhtml = questionhtml;
        }

        public String getQuestionparent() {
            return questionparent;
        }

        public void setQuestionparent(String questionparent) {
            this.questionparent = questionparent;
        }

        public String getQuestionsequence() {
            return questionsequence;
        }

        public void setQuestionsequence(String questionsequence) {
            this.questionsequence = questionsequence;
        }

        public String getQuestionlevel() {
            return questionlevel;
        }

        public void setQuestionlevel(String questionlevel) {
            this.questionlevel = questionlevel;
        }

        public String getOneobjecttype() {
            return oneobjecttype;
        }

        public void setOneobjecttype(String oneobjecttype) {
            this.oneobjecttype = oneobjecttype;
        }

        public String getTwoobjecttype() {
            return twoobjecttype;
        }

        public void setTwoobjecttype(String twoobjecttype) {
            this.twoobjecttype = twoobjecttype;
        }

        public String getSubjecttype() {
            return subjecttype;
        }

        public void setSubjecttype(String subjecttype) {
            this.subjecttype = subjecttype;
        }

        public String getSectiontype() {
            return sectiontype;
        }

        public void setSectiontype(String sectiontype) {
            this.sectiontype = sectiontype;
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

        public String getMathfoundation() {
            return mathfoundation;
        }

        public void setMathfoundation(String mathfoundation) {
            this.mathfoundation = mathfoundation;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getDiscusstime() {
            return discusstime;
        }

        public void setDiscusstime(String discusstime) {
            this.discusstime = discusstime;
        }

        public String getDiscussmark() {
            return discussmark;
        }

        public void setDiscussmark(String discussmark) {
            this.discussmark = discussmark;
        }

        public String getReadArticleId() {
            return readArticleId;
        }

        public void setReadArticleId(String readArticleId) {
            this.readArticleId = readArticleId;
        }

        public Object getArticleContent() {
            return articleContent;
        }

        public void setArticleContent(Object articleContent) {
            this.articleContent = articleContent;
        }

        public String getQuestionNumber() {
            return questionNumber;
        }

        public void setQuestionNumber(String questionNumber) {
            this.questionNumber = questionNumber;
        }

        public String getTwoname() {
            return twoname;
        }

        public void setTwoname(String twoname) {
            this.twoname = twoname;
        }

        public String getSections() {
            return sections;
        }

        public void setSections(String sections) {
            this.sections = sections;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getLevel_s() {
            return level_s;
        }

        public void setLevel_s(String level_s) {
            this.level_s = level_s;
        }

        public String getQtitle() {
            return qtitle;
        }

        public void setQtitle(String qtitle) {
            this.qtitle = qtitle;
        }

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public List<QslctarrBean> getQslctarr() {
            return qslctarr;
        }

        public void setQslctarr(List<QslctarrBean> qslctarr) {
            this.qslctarr = qslctarr;
        }

        public static class QslctarrBean {
            /**
             * name : A
             * select : Statement (1) ALONE is sufficient, but statement (2) alone is not sufficient.
             */

            private String name;
            private String select;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSelect() {
                return select;
            }

            public void setSelect(String select) {
                this.select = select;
            }
        }
    }

//    public static class UserDataBean {
//        private String userid;
//        private String username;
//        private String useremail;
//        private String userpassword;
//        private String userpwd;
//        private String usercoin;
//        private String userregip;
//        private String userregtime;
//        private String userlogtime;
//        private String usergroupid;
//        private String usermoduleid;
//        private String manager_apps;
//        private String photo;
//        private String usertruename;
//        private String normal_favor;
//        private String teacher_subjects;
//        private String ssss_xxx;
//        private String webstatus;
//        private String nickname;
//        private String phone;
//        private Object school;
//        private Object professional;
//        private Object grade;
//        private Object place;
//        private Object personstatus;
//        private String lastlogintime;
//        private String reviewdays;
//        private Object qq;
//        private String remark;
//        private String openid;
//        private String bbs;
//        private String chepin;
//        private String appAccountTest;
//        private String machineCode;
//        private String deviceToken;
//        private String uid;
//        private String videocard;
//        private String phonevideocard;
//        private String ipadvideocard;
//        private TotalnumBean totalnum;
//        private String correct;
//        private int score;
//        private String messagenum;
//        private String rank;
//        private String level;
//
//        public String getUserid() {
//            return userid;
//        }
//
//        public void setUserid(String userid) {
//            this.userid = userid;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getUseremail() {
//            return useremail;
//        }
//
//        public void setUseremail(String useremail) {
//            this.useremail = useremail;
//        }
//
//        public String getUserpassword() {
//            return userpassword;
//        }
//
//        public void setUserpassword(String userpassword) {
//            this.userpassword = userpassword;
//        }
//
//        public String getUserpwd() {
//            return userpwd;
//        }
//
//        public void setUserpwd(String userpwd) {
//            this.userpwd = userpwd;
//        }
//
//        public String getUsercoin() {
//            return usercoin;
//        }
//
//        public void setUsercoin(String usercoin) {
//            this.usercoin = usercoin;
//        }
//
//        public String getUserregip() {
//            return userregip;
//        }
//
//        public void setUserregip(String userregip) {
//            this.userregip = userregip;
//        }
//
//        public String getUserregtime() {
//            return userregtime;
//        }
//
//        public void setUserregtime(String userregtime) {
//            this.userregtime = userregtime;
//        }
//
//        public String getUserlogtime() {
//            return userlogtime;
//        }
//
//        public void setUserlogtime(String userlogtime) {
//            this.userlogtime = userlogtime;
//        }
//
//        public String getUsergroupid() {
//            return usergroupid;
//        }
//
//        public void setUsergroupid(String usergroupid) {
//            this.usergroupid = usergroupid;
//        }
//
//        public String getUsermoduleid() {
//            return usermoduleid;
//        }
//
//        public void setUsermoduleid(String usermoduleid) {
//            this.usermoduleid = usermoduleid;
//        }
//
//        public String getManager_apps() {
//            return manager_apps;
//        }
//
//        public void setManager_apps(String manager_apps) {
//            this.manager_apps = manager_apps;
//        }
//
//        public String getPhoto() {
//            return photo;
//        }
//
//        public void setPhoto(String photo) {
//            this.photo = photo;
//        }
//
//        public String getUsertruename() {
//            return usertruename;
//        }
//
//        public void setUsertruename(String usertruename) {
//            this.usertruename = usertruename;
//        }
//
//        public String getNormal_favor() {
//            return normal_favor;
//        }
//
//        public void setNormal_favor(String normal_favor) {
//            this.normal_favor = normal_favor;
//        }
//
//        public String getTeacher_subjects() {
//            return teacher_subjects;
//        }
//
//        public void setTeacher_subjects(String teacher_subjects) {
//            this.teacher_subjects = teacher_subjects;
//        }
//
//        public String getSsss_xxx() {
//            return ssss_xxx;
//        }
//
//        public void setSsss_xxx(String ssss_xxx) {
//            this.ssss_xxx = ssss_xxx;
//        }
//
//        public String getWebstatus() {
//            return webstatus;
//        }
//
//        public void setWebstatus(String webstatus) {
//            this.webstatus = webstatus;
//        }
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        public void setNickname(String nickname) {
//            this.nickname = nickname;
//        }
//
//        public String getPhone() {
//            return phone;
//        }
//
//        public void setPhone(String phone) {
//            this.phone = phone;
//        }
//
//        public Object getSchool() {
//            return school;
//        }
//
//        public void setSchool(Object school) {
//            this.school = school;
//        }
//
//        public Object getProfessional() {
//            return professional;
//        }
//
//        public void setProfessional(Object professional) {
//            this.professional = professional;
//        }
//
//        public Object getGrade() {
//            return grade;
//        }
//
//        public void setGrade(Object grade) {
//            this.grade = grade;
//        }
//
//        public Object getPlace() {
//            return place;
//        }
//
//        public void setPlace(Object place) {
//            this.place = place;
//        }
//
//        public Object getPersonstatus() {
//            return personstatus;
//        }
//
//        public void setPersonstatus(Object personstatus) {
//            this.personstatus = personstatus;
//        }
//
//        public String getLastlogintime() {
//            return lastlogintime;
//        }
//
//        public void setLastlogintime(String lastlogintime) {
//            this.lastlogintime = lastlogintime;
//        }
//
//        public String getReviewdays() {
//            return reviewdays;
//        }
//
//        public void setReviewdays(String reviewdays) {
//            this.reviewdays = reviewdays;
//        }
//
//        public Object getQq() {
//            return qq;
//        }
//
//        public void setQq(Object qq) {
//            this.qq = qq;
//        }
//
//        public String getRemark() {
//            return remark;
//        }
//
//        public void setRemark(String remark) {
//            this.remark = remark;
//        }
//
//        public String getOpenid() {
//            return openid;
//        }
//
//        public void setOpenid(String openid) {
//            this.openid = openid;
//        }
//
//        public String getBbs() {
//            return bbs;
//        }
//
//        public void setBbs(String bbs) {
//            this.bbs = bbs;
//        }
//
//        public String getChepin() {
//            return chepin;
//        }
//
//        public void setChepin(String chepin) {
//            this.chepin = chepin;
//        }
//
//        public String getAppAccountTest() {
//            return appAccountTest;
//        }
//
//        public void setAppAccountTest(String appAccountTest) {
//            this.appAccountTest = appAccountTest;
//        }
//
//        public String getMachineCode() {
//            return machineCode;
//        }
//
//        public void setMachineCode(String machineCode) {
//            this.machineCode = machineCode;
//        }
//
//        public String getDeviceToken() {
//            return deviceToken;
//        }
//
//        public void setDeviceToken(String deviceToken) {
//            this.deviceToken = deviceToken;
//        }
//
//        public String getUid() {
//            return uid;
//        }
//
//        public void setUid(String uid) {
//            this.uid = uid;
//        }
//
//        public String getVideocard() {
//            return videocard;
//        }
//
//        public void setVideocard(String videocard) {
//            this.videocard = videocard;
//        }
//
//        public String getPhonevideocard() {
//            return phonevideocard;
//        }
//
//        public void setPhonevideocard(String phonevideocard) {
//            this.phonevideocard = phonevideocard;
//        }
//
//        public String getIpadvideocard() {
//            return ipadvideocard;
//        }
//
//        public void setIpadvideocard(String ipadvideocard) {
//            this.ipadvideocard = ipadvideocard;
//        }
//
//        public TotalnumBean getTotalnum() {
//            return totalnum;
//        }
//
//        public void setTotalnum(TotalnumBean totalnum) {
//            this.totalnum = totalnum;
//        }
//
//        public String getCorrect() {
//            return correct;
//        }
//
//        public void setCorrect(String correct) {
//            this.correct = correct;
//        }
//
//        public int getScore() {
//            return score;
//        }
//
//        public void setScore(int score) {
//            this.score = score;
//        }
//
//        public String getMessagenum() {
//            return messagenum;
//        }
//
//        public void setMessagenum(String messagenum) {
//            this.messagenum = messagenum;
//        }
//
//        public String getRank() {
//            return rank;
//        }
//
//        public void setRank(String rank) {
//            this.rank = rank;
//        }
//
//        public String getLevel() {
//            return level;
//        }
//
//        public void setLevel(String level) {
//            this.level = level;
//        }
//
//        public static class TotalnumBean {
//            /**
//             * num : 0
//             */
//
//            private String num;
//
//            public String getNum() {
//                return num;
//            }
//
//            public void setNum(String num) {
//                this.num = num;
//            }
//        }
//    }
}
