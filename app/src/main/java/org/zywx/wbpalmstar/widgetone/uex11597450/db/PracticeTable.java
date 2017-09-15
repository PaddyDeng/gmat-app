package org.zywx.wbpalmstar.widgetone.uex11597450.db;

public class PracticeTable {
    public static String DEFAULT_UID = "default_userid";

    public static String TABLE_NAME = "practice";
    public static String RANDOM_NAME = "random_name";
    public static String COLLECTION_NAME = "collection_name";

    //===================================================================================
    //随机数表字段
    public static String COURSEID = "course_id";//存储的数据标识text
    public static String TIMES = "times";//已有的基础次数int
    public static String TYPE = "type";//随机数所属类别 试听课为 1 公开课为 2
    public static String DATE = "date";//日期存储格式 string XX年XX月XX日
    public static String OPENAPPTIMES = "open_app_times";//int 今天剩余可增长数

    //===================================================================================

    //    同步数据新增字段（序号做题）2017.9.12
    public static String SERIALTID = "xuhaotikuId";//序号题库id
    //===================================================================================

    public static String STID = "stid";//题库中的stid，同步题库需要
    public static String QUESTIONID = "questionid";//问题id
    public static String USERID = "userid";//用户id  未登录不记录，登录记录   主要涉及到换号的时候。
    public static String TESTRESULT = "testresult";//int  1正确，2错误
    public static String STARTMAKE = "startmake";//开始做题时间  long
    public static String USETIME = "usetime";//用时 基本单位 毫秒 类型 long

    public static String YOUANSWER = "youanswer";//你的答案 text
    public static String EXERCISESTATE = "exerciseState";//重新做题置为2，默认置未1（重新做题，每做一道题目，置1）,服务器用作删除的。

    public static String NEWSTATUS = "newstatus";//int 2表示同步下来的数据。1表示新做的题目。需要同步上去用以更新服务器

}
