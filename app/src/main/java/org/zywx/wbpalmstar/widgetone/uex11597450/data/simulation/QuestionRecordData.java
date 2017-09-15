package org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation;

import android.os.Parcel;
import android.os.Parcelable;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.NetParData;

import java.util.List;

public class QuestionRecordData implements Parcelable {

    private String id;
    private String questionid;
    private String useranswer;
    private String userid;
    private String usertikuid;
    private String answertype;
    private String duration;
    private String answertime;
    private String score;
    private String mkid;
    private String mkscoreid;
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
    private String questionknowsid;
    private String questioncreatetime;
    private String questionstatus;
    private String questionhtml;
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
    private String questionNumber;
    private String level_s;
    private String qtitle;
//    private ParseBean parse;
    private List<QslctarrBean> qslctarr;
    private List<NetParData> mNetParDatas;//答案解析
    private boolean youChooseResult;//你选择的结果

    public List<NetParData> getNetParDatas() {
        return mNetParDatas;
    }

    public void setNetParDatas(List<NetParData> netParDatas) {
        mNetParDatas = netParDatas;
    }

    public boolean isYouChooseResult() {
        return youChooseResult;
    }

    public void setYouChooseResult(boolean youChooseResult) {
        this.youChooseResult = youChooseResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    public String getUseranswer() {
        return useranswer;
    }

    public void setUseranswer(String useranswer) {
        this.useranswer = useranswer;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsertikuid() {
        return usertikuid;
    }

    public void setUsertikuid(String usertikuid) {
        this.usertikuid = usertikuid;
    }

    public String getAnswertype() {
        return answertype;
    }

    public void setAnswertype(String answertype) {
        this.answertype = answertype;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAnswertime() {
        return answertime;
    }

    public void setAnswertime(String answertime) {
        this.answertime = answertime;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMkid() {
        return mkid;
    }

    public void setMkid(String mkid) {
        this.mkid = mkid;
    }

    public String getMkscoreid() {
        return mkscoreid;
    }

    public void setMkscoreid(String mkscoreid) {
        this.mkscoreid = mkscoreid;
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

    public String getQuestionknowsid() {
        return questionknowsid;
    }

    public void setQuestionknowsid(String questionknowsid) {
        this.questionknowsid = questionknowsid;
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

    public String getQuestionhtml() {
        return questionhtml;
    }

    public void setQuestionhtml(String questionhtml) {
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

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
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

//    public ParseBean getParse() {
//        return parse;
//    }

//    public void setParse(ParseBean parse) {
//        this.parse = parse;
//    }

    public List<QslctarrBean> getQslctarr() {
        return qslctarr;
    }

    public void setQslctarr(List<QslctarrBean> qslctarr) {
        this.qslctarr = qslctarr;
    }

    public static class ParseBean implements Parcelable {
        /**
         * parsid : 1988
         * userid : 133
         * p_time : 2015-11-01 18:03:55
         * p_content : 等边三角形），圆拂 AB, BC, CA的长度必须是相等的。
         * 元福ABC的长度是24的话，这三段元府分别是12，圆的周长就是就应该是段相加=36
         * 元的周长等于=2派r 那r=18/派
         * <p>
         * 那直径就应该等于2r=5.7几*2=11.4几 所以选C
         * p_audit : 1
         * price : 0
         * p_type : 0
         * p_questionid : 12626
         */

        private String parsid;
        private String userid;
        private String p_time;
        private String p_content;
        private String p_audit;
        private String price;
        private String p_type;
        private String p_questionid;

        public String getParsid() {
            return parsid;
        }

        public void setParsid(String parsid) {
            this.parsid = parsid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getP_time() {
            return p_time;
        }

        public void setP_time(String p_time) {
            this.p_time = p_time;
        }

        public String getP_content() {
            return p_content;
        }

        public void setP_content(String p_content) {
            this.p_content = p_content;
        }

        public String getP_audit() {
            return p_audit;
        }

        public void setP_audit(String p_audit) {
            this.p_audit = p_audit;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getP_type() {
            return p_type;
        }

        public void setP_type(String p_type) {
            this.p_type = p_type;
        }

        public String getP_questionid() {
            return p_questionid;
        }

        public void setP_questionid(String p_questionid) {
            this.p_questionid = p_questionid;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.parsid);
            dest.writeString(this.userid);
            dest.writeString(this.p_time);
            dest.writeString(this.p_content);
            dest.writeString(this.p_audit);
            dest.writeString(this.price);
            dest.writeString(this.p_type);
            dest.writeString(this.p_questionid);
        }

        public ParseBean() {
        }

        protected ParseBean(Parcel in) {
            this.parsid = in.readString();
            this.userid = in.readString();
            this.p_time = in.readString();
            this.p_content = in.readString();
            this.p_audit = in.readString();
            this.price = in.readString();
            this.p_type = in.readString();
            this.p_questionid = in.readString();
        }

        public static final Parcelable.Creator<ParseBean> CREATOR = new Parcelable.Creator<ParseBean>() {
            @Override
            public ParseBean createFromParcel(Parcel source) {
                return new ParseBean(source);
            }

            @Override
            public ParseBean[] newArray(int size) {
                return new ParseBean[size];
            }
        };
    }

    public static class QslctarrBean implements Parcelable {
        /**
         * name : A
         * select : 5
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.select);
        }

        public QslctarrBean() {
        }

        protected QslctarrBean(Parcel in) {
            this.name = in.readString();
            this.select = in.readString();
        }

        public static final Parcelable.Creator<QslctarrBean> CREATOR = new Parcelable.Creator<QslctarrBean>() {
            @Override
            public QslctarrBean createFromParcel(Parcel source) {
                return new QslctarrBean(source);
            }

            @Override
            public QslctarrBean[] newArray(int size) {
                return new QslctarrBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.questionid);
        dest.writeString(this.useranswer);
        dest.writeString(this.userid);
        dest.writeString(this.usertikuid);
        dest.writeString(this.answertype);
        dest.writeString(this.duration);
        dest.writeString(this.answertime);
        dest.writeString(this.score);
        dest.writeString(this.mkid);
        dest.writeString(this.mkscoreid);
        dest.writeString(this.questiontype);
        dest.writeString(this.question);
        dest.writeString(this.questiontitle);
        dest.writeString(this.questionuserid);
        dest.writeString(this.questionusername);
        dest.writeString(this.questionlastmodifyuser);
        dest.writeString(this.questionselect);
        dest.writeString(this.questionselectnumber);
        dest.writeString(this.questionanswer);
        dest.writeString(this.questiondescribe);
        dest.writeString(this.questionknowsid);
        dest.writeString(this.questioncreatetime);
        dest.writeString(this.questionstatus);
        dest.writeString(this.questionhtml);
        dest.writeString(this.questionparent);
        dest.writeString(this.questionsequence);
        dest.writeString(this.questionlevel);
        dest.writeString(this.oneobjecttype);
        dest.writeString(this.twoobjecttype);
        dest.writeString(this.subjecttype);
        dest.writeString(this.sectiontype);
        dest.writeString(this.questionarticle);
        dest.writeString(this.articletitle);
        dest.writeString(this.mathfoundation);
        dest.writeString(this.views);
        dest.writeString(this.comments);
        dest.writeString(this.discusstime);
        dest.writeString(this.discussmark);
        dest.writeString(this.readArticleId);
        dest.writeString(this.questionNumber);
        dest.writeString(this.level_s);
        dest.writeString(this.qtitle);
//        dest.writeParcelable(this.parse, flags);
        dest.writeTypedList(this.qslctarr);
        dest.writeTypedList(this.mNetParDatas);
        dest.writeByte(this.youChooseResult ? (byte) 1 : (byte) 0);
    }

    public QuestionRecordData() {
    }

    protected QuestionRecordData(Parcel in) {
        this.id = in.readString();
        this.questionid = in.readString();
        this.useranswer = in.readString();
        this.userid = in.readString();
        this.usertikuid = in.readString();
        this.answertype = in.readString();
        this.duration = in.readString();
        this.answertime = in.readString();
        this.score = in.readString();
        this.mkid = in.readString();
        this.mkscoreid = in.readString();
        this.questiontype = in.readString();
        this.question = in.readString();
        this.questiontitle = in.readString();
        this.questionuserid = in.readString();
        this.questionusername = in.readString();
        this.questionlastmodifyuser = in.readString();
        this.questionselect = in.readString();
        this.questionselectnumber = in.readString();
        this.questionanswer = in.readString();
        this.questiondescribe = in.readString();
        this.questionknowsid = in.readString();
        this.questioncreatetime = in.readString();
        this.questionstatus = in.readString();
        this.questionhtml = in.readString();
        this.questionparent = in.readString();
        this.questionsequence = in.readString();
        this.questionlevel = in.readString();
        this.oneobjecttype = in.readString();
        this.twoobjecttype = in.readString();
        this.subjecttype = in.readString();
        this.sectiontype = in.readString();
        this.questionarticle = in.readString();
        this.articletitle = in.readString();
        this.mathfoundation = in.readString();
        this.views = in.readString();
        this.comments = in.readString();
        this.discusstime = in.readString();
        this.discussmark = in.readString();
        this.readArticleId = in.readString();
        this.questionNumber = in.readString();
        this.level_s = in.readString();
        this.qtitle = in.readString();
//        this.parse = in.readParcelable(ParseBean.class.getClassLoader());
        this.qslctarr = in.createTypedArrayList(QslctarrBean.CREATOR);
        this.mNetParDatas = in.createTypedArrayList(NetParData.CREATOR);
        this.youChooseResult = in.readByte() != 0;
    }

    public static final Parcelable.Creator<QuestionRecordData> CREATOR = new Parcelable.Creator<QuestionRecordData>() {
        @Override
        public QuestionRecordData createFromParcel(Parcel source) {
            return new QuestionRecordData(source);
        }

        @Override
        public QuestionRecordData[] newArray(int size) {
            return new QuestionRecordData[size];
        }
    };
}
