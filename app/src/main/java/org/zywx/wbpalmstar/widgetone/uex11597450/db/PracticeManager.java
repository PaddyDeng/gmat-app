package org.zywx.wbpalmstar.widgetone.uex11597450.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RandomData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PracticeManager {
    private static DBHelper mDBHelper = null;
    private static PracticeManager instance;
    private static SQLiteDatabase writableDatabase;
    private static SQLiteDatabase readableDatabase;
    private static Random random;

    public static PracticeManager getInstance() {
        if (null == instance) {
            synchronized (PracticeManager.class) {
                if (null == instance) {
                    instance = new PracticeManager();
                }
            }
        }
        return instance;
    }

    private PracticeManager() {
        if (mDBHelper == null) {
            mDBHelper = new DBHelper(GmatApplication.getInstance());
            writableDatabase = mDBHelper.getWritableDatabase();
            readableDatabase = mDBHelper.getReadableDatabase();
            random = new Random();
        }
    }

    public RandomData queryNumber(String courseId, int type) {
        if (type == C.TRIAL_COURSE) {
            return queryFreeRandomNumber(courseId, type);
        } else if (type == C.INTRO_COURSE) {
            return queryIntroRandomNumber(courseId, type);
        } else if (type == C.PUBLIC_COURSE) {
            return queryPublicCourseRandomNumber(courseId, type);
        } else if (type == C.HOT_COURSE) {
            return queryHotRandomNumber(courseId, type);
        } else if (type == C.TEACHER_COURSE) {
            return queryTeacherRandomNumber(courseId, type);
        } else if (type == C.MAKE_TEST_PERSON) {
            return queryMakeTestRandomNumber(courseId, type);
        } else if (type == C.KNOW_POINT_TYPE) {
            return queryKnowPoint(courseId, type);
        } else if (type == C.KNOW_POINT_TYPE_ITEM) {
            return queryKnowPointTypeNum(courseId, type);
        }
        return null;
    }

    public RandomData queryKnowPointTypeNum(String courseId, int type) {
        RandomData randomData = queryRandomNumber(courseId, type);
        incrementRandomNum(randomData, 1000, 103, courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }

    public RandomData queryKnowPoint(String courseId, int type) {
        RandomData randomData = queryRandomNumber(courseId, type);
        incrementRandomNum(randomData, 1000, 103, courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }


    public RandomData queryMakeTestRandomNumber(String courseId, int type) {
        RandomData randomData = queryRandomNumber(courseId, type);
        incrementNumNewRule(randomData, 39351, 128, courseId);
//        incrementRandomNum(randomData, 39000, 100 + random.nextInt(100), courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }

    public RandomData queryTeacherRandomNumber(String courseId, int type) {
        RandomData randomData = queryRandomNumber(courseId, type);
        incrementRandomNum(randomData, 30 + random.nextInt(160), 5, courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }


    public RandomData queryHotRandomNumber(String courseId, int type) {
        RandomData randomData = queryRandomNumber(courseId, type);
        int baseNum = 0;
        int incrementNum = 27;
        if (TextUtils.equals(courseId, C.HOT_ + 0)) {
            baseNum = 203;
        } else if (TextUtils.equals(courseId, C.HOT_ + 1)) {
            baseNum = 335;
        } else if (TextUtils.equals(courseId, C.HOT_ + 2)) {
            baseNum = 289;
        }
        incrementNumNewRule(randomData, baseNum, incrementNum, courseId);
//        incrementRandomNum(randomData, 100 + random.nextInt(160), 5, courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }

    public RandomData queryPublicCourseRandomNumber(String courseId, int type) {
        RandomData randomData = queryRandomNumber(courseId, type);
        incrementRandomNum(randomData, 30 + random.nextInt(170), 10, courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }

    public RandomData queryIntroRandomNumber(String courseId, int type) {
        RandomData randomData = queryRandomNumber(courseId, type);
        int baseNum = 0;
        int incrementNum = 104;

        if (TextUtils.equals(courseId, C.INTRO_CR)) {
            baseNum = 3300;
        } else if (TextUtils.equals(courseId, C.INTRO_SC)) {
            baseNum = 4300;
        } else if (TextUtils.equals(courseId, C.INTRO_RC)) {
            baseNum = 3100;
        } else if (TextUtils.equals(courseId, C.INTRO_MATH)) {
            baseNum = 2890;
        }
        incrementNumNewRule(randomData, baseNum, incrementNum, courseId);
//        incrementRandomNum(randomData, 3288, 100, courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }

    public RandomData queryRandomNumber(String courseId, int type) {//试听课随机数
        RandomData randomData = new RandomData();
        Cursor cursor = readableDatabase.query(PracticeTable.RANDOM_NAME, null, PracticeTable.COURSEID + "=? and " + PracticeTable.TYPE + "=?",
                new String[]{courseId, String.valueOf(type)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                randomData.setTimes(cursor.getInt(cursor.getColumnIndex(PracticeTable.TIMES)));
                randomData.setIncrementTimes(cursor.getInt(cursor.getColumnIndex(PracticeTable.OPENAPPTIMES)));
                randomData.setDate(cursor.getString(cursor.getColumnIndex(PracticeTable.DATE)));
            }
            cursor.close();
        }
        return randomData;
    }

    public RandomData queryFreeRandomNumber(String courseId, int type) {//试听课随机数
        RandomData randomData = queryRandomNumber(courseId, type);
        int baseNum = 0;
        int incrementNum = 103;
        if (TextUtils.equals(courseId, C.FREE_SC)) {
            baseNum = 1300;
        } else if (TextUtils.equals(courseId, C.FREE_RC)) {
            baseNum = 1125;
        } else if (TextUtils.equals(courseId, C.FREE_CR)) {
            baseNum = 1562;
        } else if (TextUtils.equals(courseId, C.FREE_MATH)) {
            baseNum = 1023;
        }
        incrementNumNewRule(randomData, baseNum, incrementNum, courseId);
        //存储或更新数据库
        insertOrUpdateData(randomData, courseId, type);
        return randomData;
    }

    /**
     * 新的自增规则
     * APP抢先学课，按照日期固定增长：从6.20开始。
     * 4个免费试听课程：6.20基数分别为1300、1125、1562、1023，每日固定增长103，以此类推。
     * 4个入门课程：6.20基数3300、4300、3100、2890，每日增长104，以此类推。
     * 3个本月热门课程：6.20基数203、335、289，每日增长27，以此类推。
     * 人在雷哥网备考：6.20基数39351，每日增长128，比如6.21即为39479，以此类推。
     * 专家讲师和公开课不统一了。。。
     */
    private void incrementNumNewRule(RandomData randomData, int baseNum, int incrementTimes, String courseId) {
        int times = randomData.getTimes();//已有的数

        Date date = new Date();
        //判读是否是同一天
        String newDate = TimeUtils.longToString(date.getTime(), "yyyy年MM月dd日");
        if (!TextUtils.equals(randomData.getDate(), newDate)) {//XXXX年XX月XX日 旧数据日期存储格式
            //不是同一天，根据天数差，自增
            randomData.setDate(newDate);
            long baseDate = TimeUtils.stringToLong("2017年06月20日", "yyyy年MM月dd日");
            int diff = TimeUtils.dateLess(baseDate, date.getTime());

            times = baseNum;
            times = times + incrementTimes * diff;
        } /*else {
            //是同一天，不增加
        }*/
        randomData.setTimes(times);
    }

    /**
     * 自增数字
     */
    private void incrementRandomNum(RandomData randomData, int baseNum, int dayMax, String courseId) {
        int times = randomData.getTimes();//已有的数
        int incrementTimes = randomData.getIncrementTimes();//每天自增的固定数
        if (times == 0) {
            times = baseNum;
            incrementTimes = dayMax;
        }
        int nextInt;
        Date date = new Date();
        //判读是否是同一天
        String newDate = TimeUtils.longToString(date.getTime(), "yyyy年MM月dd日");
        if (!TextUtils.equals(randomData.getDate(), newDate)) {//是同一天，不增加
            times += ((dayMax - incrementTimes > 0) ? (dayMax - incrementTimes) : 0);//头一天增长数必须达到一定数额
            incrementTimes = 0;
            randomData.setDate(newDate);
        }

//        Log.e("TAG", "incrementRandomNum:times " + times + " courseId " + courseId);
        if (incrementTimes < dayMax) {//每日增长数限制
            nextInt = random.nextInt(dayMax - incrementTimes + 2);
        } else {
            nextInt = (dayMax / 10 > 1) ? random.nextInt(dayMax / 10) : 1;
        }
        times += nextInt;
        randomData.setIncrementTimes(incrementTimes + nextInt);
        randomData.setTimes(times);
//        Log.e("TAG", "incrementRandomNum:nextInt " + nextInt + " courseId " + courseId);
    }

    public void insertOrUpdateData(RandomData randomData, String courseId, int type) {
        ContentValues values = new ContentValues();
        values.put(PracticeTable.TIMES, randomData.getTimes());
        values.put(PracticeTable.OPENAPPTIMES, randomData.getIncrementTimes());
        values.put(PracticeTable.DATE, randomData.getDate());
        values.put(PracticeTable.COURSEID, courseId);
        values.put(PracticeTable.TYPE, type);
        int update = writableDatabase.update(PracticeTable.RANDOM_NAME, values, PracticeTable.COURSEID + "=? and " + PracticeTable.TYPE + "=?",
                new String[]{courseId, String.valueOf(type)});
        if (update != 1) {
            writableDatabase.insert(PracticeTable.RANDOM_NAME, null, values);
        }
    }

    public List<CommitData> queryAllData(String userId) {
        List<CommitData> practiceRecordDatas = new ArrayList<>();
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME, null, PracticeTable.USERID + "=? and " + PracticeTable.NEWSTATUS + "=?"
                , new String[]{userId, String.valueOf(C.NEED_UPLOAD)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CommitData practiceRecordData = new CommitData();
                practiceRecordData.setUserid(cursor.getString(cursor.getColumnIndex(PracticeTable.USERID)));
                practiceRecordData.setStid(cursor.getInt(cursor.getColumnIndex(PracticeTable.STID)));
                practiceRecordData.setQuestionId(cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID)));
                int questionIsRight = cursor.getInt(cursor.getColumnIndex(PracticeTable.TESTRESULT));
                int isRight = 0;// 0 默认表示错误
                if (questionIsRight == C.CORRECT) {
                    isRight = 0;
                } else if (questionIsRight == C.ERROR) {
                    isRight = 1;
                }
                practiceRecordData.setQuestionIsRight(isRight);
                long duration = (long) (cursor.getLong(cursor.getColumnIndex(PracticeTable.USETIME)) / 1000.0f);
                duration = duration > 0 ? duration : 1;
                practiceRecordData.setDuration(duration);
                practiceRecordData.setQuestionSubmitTime(cursor.getLong(cursor.getColumnIndex(PracticeTable.STARTMAKE)) / 1000);
                practiceRecordData.setExerciseState(cursor.getInt(cursor.getColumnIndex(PracticeTable.EXERCISESTATE)));
                practiceRecordData.setQuestionUserAnswer(cursor.getString(cursor.getColumnIndex(PracticeTable.YOUANSWER)));
                practiceRecordData.setXuhaotikuId(cursor.getInt(cursor.getColumnIndex(PracticeTable.SERIALTID)));
                practiceRecordDatas.add(practiceRecordData);
            }
            cursor.close();
        }
        return practiceRecordDatas;
    }

    /**
     * 查询所有做题记录
     */
    public List<PracticeRecordData> getAllMakeTopic() {
        List<PracticeRecordData> practiceRecordDatas = new ArrayList<>();

        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME,
                new String[]{PracticeTable.YOUANSWER, PracticeTable.STARTMAKE, PracticeTable.STID,
                        PracticeTable.USETIME, PracticeTable.QUESTIONID, PracticeTable.SERIALTID},
                PracticeTable.USERID + "=? and " + PracticeTable.EXERCISESTATE + "=?",
                new String[]{String.valueOf(userId), String.valueOf(C.MAKE_TOPIC)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PracticeRecordData practiceRecordData = new PracticeRecordData();
                practiceRecordData.setYouanswer(cursor.getString(cursor.getColumnIndex(PracticeTable.YOUANSWER)));
                practiceRecordData.setStartmake(cursor.getLong(cursor.getColumnIndex(PracticeTable.STARTMAKE)));
                practiceRecordData.setUsetime(cursor.getLong(cursor.getColumnIndex(PracticeTable.USETIME)));
                practiceRecordData.setQuestionid(cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID)));
                practiceRecordData.setStid(cursor.getInt(cursor.getColumnIndex(PracticeTable.STID)));
                practiceRecordData.setXuhaotikuId(cursor.getInt(cursor.getColumnIndex(PracticeTable.SERIALTID)));
                practiceRecordDatas.add(practiceRecordData);
            }
            cursor.close();
        }
        return practiceRecordDatas;
    }


    public List<PracticeRecordData> queryMakeTopicDataThroughtId(String questionId, String stid, boolean isSerialId) {
        List<PracticeRecordData> practiceRecordDatas = new ArrayList<>();

        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        String tikuId = isSerialId ? PracticeTable.SERIALTID : PracticeTable.STID;
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME,
                new String[]{PracticeTable.TESTRESULT, PracticeTable.YOUANSWER,
                        PracticeTable.USETIME, PracticeTable.EXERCISESTATE, PracticeTable.SERIALTID},
                PracticeTable.USERID + "=? and " + PracticeTable.QUESTIONID + "=? and " + tikuId + "=?",
                new String[]{String.valueOf(userId), questionId, stid}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PracticeRecordData practiceRecordData = new PracticeRecordData();
                practiceRecordData.setQuestionid(Integer.parseInt(questionId));
                practiceRecordData.setYouanswer(cursor.getString(cursor.getColumnIndex(PracticeTable.YOUANSWER)));
                practiceRecordData.setTestResult(cursor.getInt(cursor.getColumnIndex(PracticeTable.TESTRESULT)));
                practiceRecordData.setUsetime(cursor.getLong(cursor.getColumnIndex(PracticeTable.USETIME)));
                practiceRecordData.setExerciseState(cursor.getInt(cursor.getColumnIndex(PracticeTable.EXERCISESTATE)));
                practiceRecordData.setXuhaotikuId(cursor.getInt(cursor.getColumnIndex(PracticeTable.SERIALTID)));
                practiceRecordDatas.add(practiceRecordData);
            }
            cursor.close();
        }
        return practiceRecordDatas;
    }

    public List<PracticeRecordData> queryMakeTopicDataThroughtId(String questionId, String stid) {
        return queryMakeTopicDataThroughtId(questionId, stid, false);
//        List<PracticeRecordData> practiceRecordDatas = new ArrayList<>();
//
//        String userId = PracticeTable.DEFAULT_UID;
//        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
//            userId = GlobalUser.getInstance().getUserData().getUserid();
//        }
//        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME,
//                new String[]{PracticeTable.TESTRESULT, PracticeTable.YOUANSWER,
//                        PracticeTable.USETIME, PracticeTable.EXERCISESTATE},
//                PracticeTable.USERID + "=? and " + PracticeTable.QUESTIONID + "=? and " + PracticeTable.STID + "=?",
//                new String[]{String.valueOf(userId), questionId, stid}, null, null, null);
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                PracticeRecordData practiceRecordData = new PracticeRecordData();
//                practiceRecordData.setQuestionid(Integer.parseInt(questionId));
//                practiceRecordData.setYouanswer(cursor.getString(cursor.getColumnIndex(PracticeTable.YOUANSWER)));
//                practiceRecordData.setTestResult(cursor.getInt(cursor.getColumnIndex(PracticeTable.TESTRESULT)));
//                practiceRecordData.setUsetime(cursor.getLong(cursor.getColumnIndex(PracticeTable.USETIME)));
//                practiceRecordData.setExerciseState(cursor.getInt(cursor.getColumnIndex(PracticeTable.EXERCISESTATE)));
//                practiceRecordDatas.add(practiceRecordData);
//            }
//            cursor.close();
//        }
//        return practiceRecordDatas;
    }

    public List<PracticeRecordData> queryMakeTopicDataThroughtId(String questionId) {
        List<PracticeRecordData> practiceRecordDatas = new ArrayList<>();

        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME,
                new String[]{PracticeTable.TESTRESULT, PracticeTable.YOUANSWER,
                        PracticeTable.USETIME, PracticeTable.EXERCISESTATE},
                PracticeTable.USERID + "=? and " + PracticeTable.QUESTIONID + "=?",
                new String[]{String.valueOf(userId), questionId}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PracticeRecordData practiceRecordData = new PracticeRecordData();
                practiceRecordData.setQuestionid(Integer.parseInt(questionId));
                practiceRecordData.setYouanswer(cursor.getString(cursor.getColumnIndex(PracticeTable.YOUANSWER)));
                practiceRecordData.setTestResult(cursor.getInt(cursor.getColumnIndex(PracticeTable.TESTRESULT)));
                practiceRecordData.setUsetime(cursor.getLong(cursor.getColumnIndex(PracticeTable.USETIME)));
                practiceRecordData.setExerciseState(cursor.getInt(cursor.getColumnIndex(PracticeTable.EXERCISESTATE)));
                practiceRecordDatas.add(practiceRecordData);
            }
            cursor.close();
        }
        return practiceRecordDatas;
    }

    /**
     * 查询所有错题id
     */
    public List<PracticeRecordData> getAllErrorTopic() {
        List<PracticeRecordData> practiceRecordDatas = new ArrayList<>();
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME, new String[]{PracticeTable.YOUANSWER, PracticeTable.STARTMAKE,
                        PracticeTable.USETIME, PracticeTable.QUESTIONID, PracticeTable.STID, PracticeTable.SERIALTID},
                PracticeTable.USERID + "= ? and " + PracticeTable.TESTRESULT + "= ? and " + PracticeTable.EXERCISESTATE + "=?",
                new String[]{String.valueOf(userId), String.valueOf(C.ERROR), String.valueOf(C.MAKE_TOPIC)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PracticeRecordData practiceRecordData = new PracticeRecordData();
                practiceRecordData.setYouanswer(cursor.getString(cursor.getColumnIndex(PracticeTable.YOUANSWER)));
                practiceRecordData.setStartmake(cursor.getLong(cursor.getColumnIndex(PracticeTable.STARTMAKE)));
                practiceRecordData.setUsetime(cursor.getLong(cursor.getColumnIndex(PracticeTable.USETIME)));
                practiceRecordData.setQuestionid(cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID)));
                practiceRecordData.setStid(cursor.getInt(cursor.getColumnIndex(PracticeTable.STID)));
                practiceRecordData.setXuhaotikuId(cursor.getInt(cursor.getColumnIndex(PracticeTable.SERIALTID)));
                practiceRecordDatas.add(practiceRecordData);
            }
            cursor.close();
        }
        return practiceRecordDatas;
    }

    /**
     * 根据类型，查看已经做过的问题的题目id
     */
    public List<Integer> getQuestId(int stid, String questionIds, boolean isSerialId) {
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        String tikuId = isSerialId ? PracticeTable.SERIALTID : PracticeTable.STID;
        List<Integer> ids = new ArrayList<>();
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME, new String[]{PracticeTable.QUESTIONID},
                tikuId + "=? and " + PracticeTable.USERID + "=? and " + PracticeTable.EXERCISESTATE + "=?", new String[]{/*uid,*/ String.valueOf(stid), userId, String.valueOf(1)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int questionId = cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID));
                if (!questionIds.contains(String.valueOf(questionId)))
                    continue;
                ids.add(questionId);
            }
            cursor.close();
        }
        return ids;
    }

    /**
     * 根据类型，查看已经做过的问题的题目id
     */
    public List<Integer> getQuestId(int stid, boolean isSerialTiKu) {
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        List<Integer> ids = new ArrayList<>();
        String tikuId = isSerialTiKu ? PracticeTable.SERIALTID : PracticeTable.STID;
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME, new String[]{PracticeTable.QUESTIONID},
                tikuId + "=? and " + PracticeTable.USERID + "=? and " + PracticeTable.EXERCISESTATE + "=?", new String[]{/*uid,*/ String.valueOf(stid), userId, String.valueOf(C.MAKE_TOPIC)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID)));
            }
            cursor.close();
        }
        return ids;
    }

    public boolean updataStatus(int stid, boolean isSerialMakeTopic) {
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        ContentValues values = new ContentValues();
        values.put(PracticeTable.EXERCISESTATE, C.AGAIN_START);
        values.put(PracticeTable.NEWSTATUS, C.NEED_UPLOAD);
        String tikuId = isSerialMakeTopic ? PracticeTable.SERIALTID : PracticeTable.STID;

        writableDatabase.update(PracticeTable.TABLE_NAME, values, tikuId + "=? and " + PracticeTable.USERID
                + "=?", new String[]{String.valueOf(stid), userId});

        return true;
    }


    /**
     * 获取所有的做题题目
     */
    public List<Integer> getMakeQuestId() {
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        List<Integer> ids = new ArrayList<>();
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME, new String[]{PracticeTable.QUESTIONID},
                PracticeTable.EXERCISESTATE + "=? and " + PracticeTable.USERID + "=?", new String[]{String.valueOf(C.MAKE_TOPIC), userId}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ids.add(cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID)));
            }
            cursor.close();
        }
        return ids;
    }

    public List<PracticeRecordData> queryPractice(int serialTiku) {
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        List<PracticeRecordData> practiceRecordDatas = new ArrayList<>();
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME, new String[]{PracticeTable.QUESTIONID,
                PracticeTable.EXERCISESTATE}, PracticeTable.SERIALTID
                + "=? and " + PracticeTable.USERID + "=?", new String[]{String.valueOf(serialTiku), userId}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                PracticeRecordData practiceRecordData = new PracticeRecordData();
                practiceRecordData.setQuestionid(cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID)));
                practiceRecordData.setExerciseState(cursor.getInt(cursor.getColumnIndex(PracticeTable.EXERCISESTATE)));
                practiceRecordDatas.add(practiceRecordData);
            }
            cursor.close();
        }
        return practiceRecordDatas;
    }

    /**
     * 查询已做题目
     */
    public List<PracticeRecordData> queryPractice(int stid, String questionIds) {
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        List<PracticeRecordData> practiceRecordDatas = new ArrayList<>();
        Cursor cursor = readableDatabase.query(PracticeTable.TABLE_NAME, new String[]{PracticeTable.QUESTIONID,
                PracticeTable.EXERCISESTATE}, PracticeTable.STID
                + "=? and " + PracticeTable.USERID + "=?", new String[]{String.valueOf(stid), userId}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int questionId = cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID));
                if (!questionIds.contains(String.valueOf(questionId)))
                    continue;
                PracticeRecordData practiceRecordData = new PracticeRecordData();
                practiceRecordData.setQuestionid(cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID)));
                practiceRecordData.setExerciseState(cursor.getInt(cursor.getColumnIndex(PracticeTable.EXERCISESTATE)));
                practiceRecordDatas.add(practiceRecordData);
            }
            cursor.close();
        }
        return practiceRecordDatas;
    }

    public synchronized void deletRecordData() {
        writableDatabase.delete(PracticeTable.TABLE_NAME, null, null);
    }

    /**
     * 将服务器的数据库数据，同步到本地
     */
    public synchronized void updateData(List<CommitData> datas) {
        Utils.logh("服务器上的数据==>", datas.size() + "");
        String userid = GlobalUser.getInstance().getUserData().getUserid();
        writableDatabase.beginTransaction();
        try {
            for (CommitData data : datas) {
                ContentValues values = new ContentValues();
                values.put(PracticeTable.STID, data.getStid());
                values.put(PracticeTable.USERID, userid);
                values.put(PracticeTable.QUESTIONID, data.getQuestionId());
                values.put(PracticeTable.NEWSTATUS, C.OLD_DATA);
                values.put(PracticeTable.EXERCISESTATE, C.MAKE_TOPIC);//处理过的题目
                values.put(PracticeTable.USETIME, data.getDuration() * 1000);//本地存储毫秒，服务器用秒
                values.put(PracticeTable.STARTMAKE, data.getQuestionSubmitTime() * 1000);//服务器基本单位秒
                values.put(PracticeTable.YOUANSWER, data.getQuestionUserAnswer());
                values.put(PracticeTable.SERIALTID, data.getXuhaotikuId());
                //questionIsRight 取反
//            int questionIsRight = data.getQuestionIsRight();//远程取回来只有  0 ，1  远程0表示正确 1表示错误
                boolean jugement = DBUtil.getInstance().jugement(data.getQuestionId(), data.getQuestionUserAnswer());
                if (jugement) {
                    values.put(PracticeTable.TESTRESULT, C.CORRECT);
                } else {
                    values.put(PracticeTable.TESTRESULT, C.ERROR);
                }
                writableDatabase.insert(PracticeTable.TABLE_NAME, null, values);
            }
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writableDatabase.endTransaction();
        }

    }

    public void insertData(ContentValues values) {
        int insertQueId = (int) values.get(PracticeTable.QUESTIONID);
        int stid = (int) values.get(PracticeTable.STID);
        int serialId = (int) values.get(PracticeTable.SERIALTID);
        String user = (String) values.get(PracticeTable.USERID);
        int update = writableDatabase.update(PracticeTable.TABLE_NAME, values, PracticeTable.QUESTIONID + "=? and " +
                        PracticeTable.USERID + "=? and " + PracticeTable.STID + "=? and " + PracticeTable.SERIALTID + "=?",
                new String[]{String.valueOf(insertQueId), user, String.valueOf(stid), String.valueOf(serialId)});
        if (update == 0) {
            writableDatabase.insert(PracticeTable.TABLE_NAME, null, values);
        }
    }

    public List<Integer> queryCollectionId() {
        List<Integer> list = new ArrayList<>();
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        Cursor cursor = readableDatabase.query(PracticeTable.COLLECTION_NAME, null, PracticeTable.USERID + "=?",
                new String[]{userId}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursor.getInt(cursor.getColumnIndex(PracticeTable.QUESTIONID)));
            }
            cursor.close();
        }
        return list;
    }

    public boolean queryWhetherCollection(int questionId) {
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        boolean isCollection = false;
        Cursor query = readableDatabase.query(PracticeTable.COLLECTION_NAME, null, PracticeTable.QUESTIONID + "=? and " + PracticeTable.USERID + "=?",
                new String[]{String.valueOf(questionId), userId}, null, null, null);
        if (query != null) {
            if (query.moveToNext()) {
                isCollection = true;
            }
            query.close();
        }
        return isCollection;
    }

    public void dropCollectionData(int questionId) {
        String userId = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userId = GlobalUser.getInstance().getUserData().getUserid();
        }
        writableDatabase.delete(PracticeTable.COLLECTION_NAME, PracticeTable.QUESTIONID + "=? and " +
                PracticeTable.USERID + "=?", new String[]{String.valueOf(questionId), userId});
    }


    public void insertCollectionData(ContentValues values) {
        int insertQueId = (int) values.get(PracticeTable.QUESTIONID);
        String userId = (String) values.get(PracticeTable.USERID);
        int update = writableDatabase.update(PracticeTable.COLLECTION_NAME, values, PracticeTable.QUESTIONID + "=? and " +
                PracticeTable.USERID + "=?", new String[]{String.valueOf(insertQueId), userId});
//        Log.e("TAG", "insertCollectionData: " + update);
        if (update == 0) {
            long insert = writableDatabase.insert(PracticeTable.COLLECTION_NAME, null, values);
//        Log.e("TAG", "insertCollectionData: " + insert);
        }
    }

}
