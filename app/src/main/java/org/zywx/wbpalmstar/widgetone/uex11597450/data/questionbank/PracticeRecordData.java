package org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank;


public class PracticeRecordData {
    private String section;
    private String questionanswer;
    private String questiontitle;
    private String question;

    private int stid;
    private int questionid;
    private String userId;
    private int exerciseState;
    private String youanswer;
    private int testResult;//正确或错误
    private long usetime;
    private long startmake;
    private int sectionId;
    private int twoObjectId;

    private int xuhaotikuId;//序号题库id  若是序号做题时，stid应该传成0

    public int getXuhaotikuId() {
        return xuhaotikuId;
    }

    public void setXuhaotikuId(int xuhaotikuId) {
        this.xuhaotikuId = xuhaotikuId;
    }

    public int getTestResult() {
        return testResult;
    }

    public void setTestResult(int testResult) {
        this.testResult = testResult;
    }

    public int getTwoObjectId() {
        return twoObjectId;
    }

    public void setTwoObjectId(int twoObjectId) {
        this.twoObjectId = twoObjectId;
    }


    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getStid() {
        return stid;
    }

    public void setStid(int stid) {
        this.stid = stid;
    }

    public int getExerciseState() {
        return exerciseState;
    }

    public void setExerciseState(int exerciseState) {
        this.exerciseState = exerciseState;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestiontitle() {
        return questiontitle;
    }

    public void setQuestiontitle(String questiontitle) {
        this.questiontitle = questiontitle;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public int getQuestionid() {
        return questionid;
    }

    public void setQuestionid(int questionid) {
        this.questionid = questionid;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getQuestionanswer() {
        return questionanswer;
    }

    public void setQuestionanswer(String questionanswer) {
        this.questionanswer = questionanswer;
    }

    public String getYouanswer() {
        return youanswer;
    }

    public void setYouanswer(String youanswer) {
        this.youanswer = youanswer;
    }

    public long getUsetime() {
        return usetime;
    }

    public void setUsetime(long usetime) {
        this.usetime = usetime;
    }

    public long getStartmake() {
        return startmake;
    }

    public void setStartmake(long startmake) {
        this.startmake = startmake;
    }
}
