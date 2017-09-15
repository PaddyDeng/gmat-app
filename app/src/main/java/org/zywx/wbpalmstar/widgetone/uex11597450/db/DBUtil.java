package org.zywx.wbpalmstar.widgetone.uex11597450.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalParseData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalQuestionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalSerial;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalSerialTiku;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalTikuData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.KnowsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.SectionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.TwoObjectData;
import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.NetParData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//         ["OG", "PREP", "讲义", "GWD"]
//        twoobjectidArray
//        [["1", "15"],
//        ["8", "10", "11"],
//        ["12"],
//        ["2"]]
//
//        ["SC", "CR", "RC", "PS", "DS"]
//        sectionidArray
//        ["6","8","7","4","5"]

//        1	600以下
//        2	600-650
//        3	650-680
//        4	680-700
//        5	700-730
//        6	730以上

public class DBUtil {
    private static SQLiteDatabase sqLiteDatabase;
    private static DBUtil instance = null;

    public static DBUtil getInstance() {
        if (instance == null) {
            synchronized (DBUtil.class) {
                if (instance == null) {
                    instance = new DBUtil(GmatApplication.getInstance());
                }
            }
        }
        return instance;
    }

    private DBUtil(Context mContext) {
        sqLiteDatabase = SQLiteDatabase.openDatabase(mContext.getDatabasePath(DBManager.DB_NAME).getPath(), null, mContext.MODE_PRIVATE);
    }

    public List<String> queryAllQuestionId() {
        List<String> lists = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("x2_questions", new String[]{"questionid"}, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                lists.add(String.valueOf(cursor.getInt(cursor.getColumnIndex("questionid"))));
            }
            cursor.close();
        }
        return lists;
    }

    public QuestionsData queryQuestionArticleTitles(int questionId) {
        Cursor cursor = sqLiteDatabase.query("x2_questions",
                new String[]{"articletitle"},
                "questionid=?", new String[]{String.valueOf(questionId)},
                null, null, null);
        QuestionsData questionsData = new QuestionsData();
        if (cursor != null) {
            if (cursor.moveToNext()) {
                questionsData.setQuestionid(questionId);
                questionsData.setArticletitle(cursor.getString(cursor.getColumnIndex("articletitle")));
            }
            cursor.close();
        }
        return questionsData;
    }


    public QuestionsData queryQuestionsTitle(int questionId) {
        Cursor cursor = sqLiteDatabase.query("x2_questions",
                new String[]{"questiontitle", "sectiontype", "questionanswer", "twoobjecttype"},
                "questionid=?", new String[]{String.valueOf(questionId)},
                null, null, null);
        QuestionsData questionsData = new QuestionsData();
        if (cursor != null) {
            if (cursor.moveToNext()) {
                questionsData.setSectionid(cursor.getInt(cursor.getColumnIndex("sectiontype")));
                questionsData.setTwoobjectid(cursor.getInt(cursor.getColumnIndex("twoobjecttype")));
                questionsData.setQuestiontitle(cursor.getString(cursor.getColumnIndex("questiontitle")));
                questionsData.setQuestionanswer(cursor.getString(cursor.getColumnIndex("questionanswer")));
            }
            cursor.close();
        }
        return questionsData;
    }

    public List<QuestionsData> fuzzyQueryQuestins(String content) {
        content = "%" + content + "%";
        List<QuestionsData> list = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("x2_questions", new String[]{"questionid", "questiontitle"},
                "questiontitle like ? or questionselect like ? or questionarticle like ? or articletitle like ?",
                new String[]{content, content, content, content}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QuestionsData data = new QuestionsData();
                data.setQuestionid(cursor.getInt(cursor.getColumnIndex("questionid")));
                data.setQuestiontitle(cursor.getString(cursor.getColumnIndex("questiontitle")));
                list.add(data);
            }
            cursor.close();
        }
        return list;
    }

    public boolean jugement(int questionId, String userAnswer) {
        boolean right = false;
        Cursor cursor = sqLiteDatabase.query("x2_questions", new String[]{"questionanswer"},
                "questionid = ?", new String[]{String.valueOf(questionId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                right = TextUtils.equals(cursor.getString(cursor.getColumnIndex("questionanswer")), userAnswer);
            }
            cursor.close();
        }
        return right;
    }

    public List<QuestionsData> fuzzyQueryQuestins(String content, int size) {
        List<QuestionsData> list = new ArrayList<>();
        if (TextUtils.isEmpty(content) || content.trim().length() <= 3 || size >= 10)
            return list;
        content = "%" + content.trim() + "%";
        Cursor cursor = sqLiteDatabase.query("x2_questions", new String[]{"questionid", "questiontitle"},
                "questiontitle like ? or questionselect like ? or questionarticle like ? or articletitle like ?",
                new String[]{content, content, content, content}, null, null, null, String.valueOf(10 - size));
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (list.size() >= 10) {
                    cursor.close();
                    break;
                }
                QuestionsData data = new QuestionsData();
                int questionid = cursor.getInt(cursor.getColumnIndex("questionid"));
                data.setQuestionid(questionid);
                data.setQuestiontitle(cursor.getString(cursor.getColumnIndex("questiontitle")));
                list.add(data);
            }
            cursor.close();
        }
        return list;
    }

    private List<QuestionsData> list;
    private String str;

    public List<QuestionsData> fuzzyQueryQuestins(List<String> contents, String content) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (TextUtils.equals(content, str)) {
            return list;
        }
        str = content;
        list.clear();
//        List<QuestionsData> list = new ArrayList<>();
        List<String> recordQuestionId = new ArrayList<>();
        int index = 0;
        for (String seem : contents) {
            if (index > 8) break;
            if (list.size() >= 10) {
                break;
            }
            if (TextUtils.isEmpty(seem) || seem.trim().length() <= 3)
                continue;
            seem = "%" + seem.trim() + "%";
            Cursor cursor = sqLiteDatabase.query("x2_questions", new String[]{"questionid", "questiontitle"},
                    "questiontitle like ? or questionselect like ? or questionarticle like ? or articletitle like ?",
                    new String[]{seem, seem, seem, seem}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (list.size() >= 10) {
                        cursor.close();
                        break;
                    }
                    QuestionsData data = new QuestionsData();
                    int questionid = cursor.getInt(cursor.getColumnIndex("questionid"));
                    if (!recordQuestionId.contains(String.valueOf(questionid))) {
                        recordQuestionId.add(String.valueOf(questionid));
                        data.setQuestionid(questionid);
                        data.setQuestiontitle(cursor.getString(cursor.getColumnIndex("questiontitle")));
                        list.add(data);
                    }
                }
                cursor.close();
            }
            index++;
        }
        return list;
    }


    public QuestionsData queryQuestionsThroughtId(String questionId) {
        Cursor cursor = sqLiteDatabase.query("x2_questions", null, "questionid=?", new String[]{questionId}, null, null, null);
        QuestionsData data = new QuestionsData();
        if (cursor != null) {
            if (cursor.moveToNext()) {
                data.setQuestionid(cursor.getInt(cursor.getColumnIndex("questionid")));
                data.setQuestionselectnumber(cursor.getInt(cursor.getColumnIndex("questionselectnumber")));
                data.setQuestion(cursor.getString(cursor.getColumnIndex("question")));
                data.setQuestionanswer(cursor.getString(cursor.getColumnIndex("questionanswer")));
                data.setQuestionselect(cursor.getString(cursor.getColumnIndex("questionselect")));
                data.setQuestionarticle(cursor.getString(cursor.getColumnIndex("questionarticle")));
                data.setQuestiontitle(cursor.getString(cursor.getColumnIndex("questiontitle")));
                data.setArticletitle(cursor.getString(cursor.getColumnIndex("articletitle")));
            }
            cursor.close();
        }
        return data;
    }

    public QuestionBankData queryQuestionBankThroughtStid(int stid) {
        QuestionBankData questionBankData = new QuestionBankData();
        Cursor cursor = sqLiteDatabase.query("x2_lower_tiku", null, "stid=?", new String[]{String.valueOf(stid)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                questionBankData.setStid(cursor.getInt(cursor.getColumnIndex("stid")));
                questionBankData.setQuestionsid(cursor.getString(cursor.getColumnIndex("questionsid")));
                questionBankData.setStname(cursor.getString(cursor.getColumnIndex("stname")));
                questionBankData.setTwoobjectid(cursor.getInt(cursor.getColumnIndex("twoobjectid")));
                questionBankData.setSectionid(cursor.getInt(cursor.getColumnIndex("sectionid")));
                questionBankData.setLevelid(cursor.getInt(cursor.getColumnIndex("levelid")));
                questionBankData.setKnowsid(cursor.getInt(cursor.getColumnIndex("knowsid")));
                questionBankData.setQuestionList(Utils.splitStr(questionBankData.getQuestionsid()));
            }
            cursor.close();
        }
        return questionBankData;
    }

    public List<QuestionBankData> queryKnowPoint(int knowId) {
        Cursor cursor = sqLiteDatabase.query("x2_lower_tiku", new String[]{"questionsid"}, "knowsid = ? and type != ?",
                new String[]{String.valueOf(knowId), "7"}, null, null, null);
        List<QuestionBankData> questionBankDataList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QuestionBankData questionBankData = new QuestionBankData();
                questionBankData.setQuestionList(Utils.splitStr(cursor.getString(cursor.getColumnIndex("questionsid"))));
                questionBankDataList.add(questionBankData);
            }
            cursor.close();
        }
        return questionBankDataList;
    }

    public List<Integer> querySerialQuestionId(int serialId) {
        List<Integer> questionIds = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("x2_xuhao_question", new String[]{"questionid"}, "tikuId = ?",
                new String[]{String.valueOf(serialId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                questionIds.add(cursor.getInt(cursor.getColumnIndex("questionid")));
            }
        }
        return questionIds;
    }

    public List<String> queryQuestion(List<String> questionIds) {
        List<String> datas = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0, size = questionIds.size(); i < size; i++) {
            sb.append(" questionid = ? ");
            if (i < size - 1)
                sb.append(" or ");
        }
        String[] ids = new String[questionIds.size()];
        ids = questionIds.toArray(ids);
        Cursor cursor = sqLiteDatabase.query("x2_questions", new String[]{"questionid", "questionNumber"},
                sb.toString(), ids, null, null, " questionNumber asc ");//asc 降序  asce 升序
        if (cursor != null) {
            while (cursor.moveToNext()) {
                datas.add(String.valueOf(cursor.getInt(cursor.getColumnIndex("questionid"))));
            }
            cursor.close();
        }
        return datas;
    }

    protected void log(Object obj) {
        Utils.logh("DBUtil : ", obj.toString());
    }

    public List<QuestionBankData> queryXuHaoTiKu(int sectionId, int twoobjId) {
        Cursor cursor = sqLiteDatabase.query("x2_xuhao_tiku", new String[]{"id", "number"}, "section = ? and twoobject = ?",
                new String[]{String.valueOf(sectionId), String.valueOf(twoobjId)}, null, null, null);
        List<QuestionBankData> questionBankDataList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QuestionBankData questionBankData = new QuestionBankData();
                questionBankData.setStid(cursor.getInt(cursor.getColumnIndex("id")));
                questionBankData.setSectionid(sectionId);
                questionBankData.setTwoobjectid(twoobjId);
                questionBankData.setStname(cursor.getString(cursor.getColumnIndex("number")));
                questionBankDataList.add(questionBankData);
            }
            cursor.close();
        }
        if (!questionBankDataList.isEmpty()) {
            String section = querySection(sectionId);
            String twoObject = queryTwoObject(String.valueOf(twoobjId));
            for (QuestionBankData data : questionBankDataList) {
                data.setSectionStr(section);
                data.setTwoObjectTypeStr(twoObject);
            }
//            mKnowDatas = queryKnows();
        }
        return questionBankDataList;
    }


    public List<QuestionBankData> queryOrderPracticeData(int sectionId, int twoobjId) {
        String twoIdStr = String.valueOf(twoobjId);
        Cursor cursor = sqLiteDatabase.query("x2_lower_tiku", null, "sectionid = ? and twoobjectid = ? and type = ?",
                new String[]{String.valueOf(sectionId), twoIdStr, "7"}, null, null, null);
        List<QuestionBankData> questionBankDataList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QuestionBankData questionBankData = new QuestionBankData();
                questionBankData.setStid(cursor.getInt(cursor.getColumnIndex("stid")));
                questionBankData.setQuestionsid(cursor.getString(cursor.getColumnIndex("questionsid")));
                questionBankData.setStname(cursor.getString(cursor.getColumnIndex("stname")));
                questionBankData.setTwoobjectid(cursor.getInt(cursor.getColumnIndex("twoobjectid")));
                questionBankData.setSectionid(cursor.getInt(cursor.getColumnIndex("sectionid")));
                questionBankData.setLevelid(cursor.getInt(cursor.getColumnIndex("levelid")));
                questionBankData.setKnowsid(cursor.getInt(cursor.getColumnIndex("knowsid")));
                questionBankData.setQuestionList(Utils.splitStr(questionBankData.getQuestionsid()));
                questionBankDataList.add(questionBankData);
            }
            cursor.close();
        }
        if (!questionBankDataList.isEmpty()) {
            String section = querySection(sectionId);
            String twoObject = queryTwoObject(twoIdStr);
            for (QuestionBankData data : questionBankDataList) {
                data.setSectionStr(section);
                data.setTwoObjectTypeStr(twoObject);
            }
//            mKnowDatas = queryKnows();
        }
        return questionBankDataList;
    }

    public List<QuestionBankData> querySingleBank(int sectionId, int twoobjId, int versionType) {
        String twoIdStr = String.valueOf(twoobjId);
        Cursor cursor = sqLiteDatabase.query("x2_lower_tiku", null, "sectionid = ? and twoobjectid = ? and type = ?",
                new String[]{String.valueOf(sectionId), twoIdStr, String.valueOf(versionType)}, null, null, null);
        List<QuestionBankData> questionBankDataList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QuestionBankData questionBankData = new QuestionBankData();
                questionBankData.setStid(cursor.getInt(cursor.getColumnIndex("stid")));
                questionBankData.setQuestionsid(cursor.getString(cursor.getColumnIndex("questionsid")));
                questionBankData.setStname(cursor.getString(cursor.getColumnIndex("stname")));
                questionBankData.setTwoobjectid(cursor.getInt(cursor.getColumnIndex("twoobjectid")));
                questionBankData.setSectionid(cursor.getInt(cursor.getColumnIndex("sectionid")));
                questionBankData.setLevelid(cursor.getInt(cursor.getColumnIndex("levelid")));
                questionBankData.setKnowsid(cursor.getInt(cursor.getColumnIndex("knowsid")));
                questionBankData.setQuestionList(Utils.splitStr(questionBankData.getQuestionsid()));
                questionBankDataList.add(questionBankData);
            }
            cursor.close();
        }
        if (!questionBankDataList.isEmpty()) {
            String section = querySection(sectionId);
            String twoObject = queryTwoObject(twoIdStr);
            for (QuestionBankData data : questionBankDataList) {
                data.setSectionStr(section);
                data.setTwoObjectTypeStr(twoObject);
            }
//            mKnowDatas = queryKnows();
        }
        return questionBankDataList;
    }


    /**
     * 查询twoObj表中的str
     */
    private String queryTwoObject(String twoObjId) {
        String twoobjecttype = "";
        Cursor cursor = sqLiteDatabase.query("x2_twoobject", new String[]{"twoobjecttype"}, "twoobjectid = ?", new String[]{twoObjId}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TwoObjectData twoObj = new TwoObjectData();
                twoobjecttype = cursor.getString(cursor.getColumnIndex("twoobjecttype"));
            }
            cursor.close();
        }
        return twoobjecttype;
    }

    private String querySection(int sectionId) {
        String section = "";
        Cursor cursor = sqLiteDatabase.query("x2_sections", new String[]{"section"}, "sectionid = ?", new String[]{String.valueOf(sectionId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                section = cursor.getString(cursor.getColumnIndex("section"));
            }
            cursor.close();
        }
        return section;
    }

    public List<QuestionBankData> queryKnowsQuestionBank(int knowsid) {
        Cursor cursor = sqLiteDatabase.query("x2_lower_tiku", null, "knowsid = ?",
                new String[]{String.valueOf(knowsid)}, null, null, null);
        return dealQuestionBackData(cursor);
    }

    public List<QuestionBankData> queryDiffQuestionBank(int levelId, int sectionId) {
        Cursor cursor = sqLiteDatabase.query("x2_lower_tiku", null, "levelid = ? and sectionid = ?",
                new String[]{String.valueOf(levelId), String.valueOf(sectionId)}, null, null, null);
        return dealQuestionBackData(cursor);
    }

    private List<QuestionBankData> dealQuestionBackData(Cursor cursor) {
        List<QuestionBankData> questionBankDataList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                QuestionBankData questionBankData = new QuestionBankData();
                questionBankData.setStid(cursor.getInt(cursor.getColumnIndex("stid")));
                questionBankData.setQuestionsid(cursor.getString(cursor.getColumnIndex("questionsid")));
                questionBankData.setStname(cursor.getString(cursor.getColumnIndex("stname")));
                questionBankData.setTwoobjectid(cursor.getInt(cursor.getColumnIndex("twoobjectid")));
                questionBankData.setSectionid(cursor.getInt(cursor.getColumnIndex("sectionid")));
                questionBankData.setLevelid(cursor.getInt(cursor.getColumnIndex("levelid")));
                questionBankData.setKnowsid(cursor.getInt(cursor.getColumnIndex("knowsid")));
                questionBankData.setQuestionList(Utils.splitStr(questionBankData.getQuestionsid()));
                questionBankDataList.add(questionBankData);
            }
            cursor.close();
        }
        if (!questionBankDataList.isEmpty()) {
            List<SectionData> SectionDatas = querySection();
            questionBankDataList = resetSectionType(questionBankDataList, SectionDatas);
            List<TwoObjectData> mTwoObjectData = queryTwoObject();
            questionBankDataList = resetTwoObjType(questionBankDataList, mTwoObjectData);
        }
        return questionBankDataList;
    }

    private List<QuestionBankData> resetTwoObjType(List<QuestionBankData> list, List<TwoObjectData> mTwoObjectData) {
        for (QuestionBankData data : list) {
            for (TwoObjectData sect : mTwoObjectData) {
                if (data.getTwoobjectid() == sect.getTwoobjectid()) {
                    data.setTwoObjectTypeStr(sect.getTwoobjecttype());
                }
            }
        }
        return list;
    }

    private List<QuestionBankData> resetSectionType(List<QuestionBankData> questionBankDataList, List<SectionData> sectionDatas) {
        for (QuestionBankData data : questionBankDataList) {
            for (SectionData sect : sectionDatas) {
                if (data.getSectionid() == sect.getSectionid()) {
                    data.setSectionStr(sect.getSection());
                }
            }
        }
        return questionBankDataList;
    }

    public List<NetParData> queryNetParThId(int questionId) {
        List<NetParData> ls = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("x2_net_pars", null, "p_questionid=?", new String[]{String.valueOf(questionId)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                NetParData knowData = new NetParData();
                String p_content = cursor.getString(cursor.getColumnIndex("p_content"));
                if (p_content.contains("/files/")) {
                    continue;
                }
                knowData.setP_questionid(cursor.getInt(cursor.getColumnIndex("p_questionid")));
                knowData.setUserid(cursor.getInt(cursor.getColumnIndex("userid")));
                knowData.setP_content(p_content);
                knowData.setP_time(cursor.getString(cursor.getColumnIndex("p_time")));
                ls.add(knowData);
//                if (ls.size() > 3) {
//                    break;
//                }
            }
            cursor.close();
        }
        return ls;
    }


    public List<KnowsData> queryKnows(int knowssectionid) {
        List<KnowsData> mKnowDatas = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("x2_knows", new String[]{"knowsid", "knows"},
                "knowssectionid = ?", new String[]{String.valueOf(knowssectionid)}, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                KnowsData knowData = new KnowsData();
                knowData.setKnowsid(cursor.getInt(cursor.getColumnIndex("knowsid")));
                knowData.setKnows(cursor.getString(cursor.getColumnIndex("knows")));
                mKnowDatas.add(knowData);
            }
            cursor.close();
        }
        return mKnowDatas;
    }


    /**
     * 通过sectionid查询section
     */
    public String getSection(int sectionId) {
        String section = "";
        Cursor cursor = sqLiteDatabase.query("x2_sections", new String[]{"section"}, "sectionid=?", new String[]{String.valueOf(sectionId)}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                section = cursor.getString(cursor.getColumnIndex("section"));
            }
            cursor.close();
        }
        return section;
    }

    private List<SectionData> querySection() {
        List<SectionData> mSectionData = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("x2_sections", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SectionData sectionData = new SectionData();
                sectionData.setSection(cursor.getString(cursor.getColumnIndex("section")));
                sectionData.setSectionid(cursor.getInt(cursor.getColumnIndex("sectionid")));
                mSectionData.add(sectionData);
            }
            cursor.close();
        }
        return mSectionData;
    }

    /**
     * 查询twoObj表中的str
     */
    private List<TwoObjectData> queryTwoObject() {
        List<TwoObjectData> mTwoObjectData = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query("x2_twoobject", null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TwoObjectData twoObj = new TwoObjectData();
                twoObj.setTwoobjectid(cursor.getInt(cursor.getColumnIndex("twoobjectid")));
                twoObj.setTwoobjecttype(cursor.getString(cursor.getColumnIndex("twoobjecttype")));
                mTwoObjectData.add(twoObj);
            }
            cursor.close();
        }
        return mTwoObjectData;
    }

    private boolean isEmptyOrNull(List<?> list) {
        return list != null && !list.isEmpty();
    }

    private int dealUpdateDb(List<?> list, ICallBack<Integer> iCallBack, int progress) {
        if (isEmptyOrNull(list)) {
            for (Iterator it = list.iterator(); it.hasNext(); ) {
                Object o = it.next();
                if (o instanceof LocalQuestionData)
                    updateOrInsertQuestion((LocalQuestionData) o);
                else if (o instanceof LocalTikuData)
                    updateOrInsertTiku((LocalTikuData) o);
                else if (o instanceof LocalParseData)
                    updateOrInsertParse((LocalParseData) o);
                else if (o instanceof LocalSerialTiku)
                    updateOrInsertSerialTiku((LocalSerialTiku) o);
                else if (o instanceof LocalSerial)
                    updateOrInsertSerial((LocalSerial) o);

                iCallBack.onSuccess(++progress);
            }
        }
        return progress;
    }

    public void updateLocalDb(List<LocalQuestionData> questionDatas, List<LocalTikuData> tikuDatas, List<LocalParseData> parseDatas,
                              List<LocalSerialTiku> serialTikus, List<LocalSerial> serials, ICallBack<Integer> iCallBack) {
        sqLiteDatabase.beginTransaction();
        try {
            int progress = 0;
            progress = dealUpdateDb(questionDatas, iCallBack, progress);
            progress = dealUpdateDb(tikuDatas, iCallBack, progress);
            progress = dealUpdateDb(parseDatas, iCallBack, progress);
            progress = dealUpdateDb(serialTikus, iCallBack, progress);
            progress = dealUpdateDb(serials, iCallBack, progress);
//            if (isEmptyOrNull(questionDatas))
//                for (LocalQuestionData q : questionDatas) {
//                    updateOrInsertQuestion(q);
//                    iCallBack.onSuccess(++progress);
//                }
//            if (isEmptyOrNull(tikuDatas))
//                for (LocalTikuData ti : tikuDatas) {
//                    updateOrInsertTiku(ti);
//                    iCallBack.onSuccess(++progress);
//                }
//            if (isEmptyOrNull(parseDatas))
//                for (LocalParseData pa : parseDatas) {
//                    updateOrInsertParse(pa);
//                    iCallBack.onSuccess(++progress);
//                }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            iCallBack.onFail();
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public void updateOrInsertSerial(LocalSerial localSerial) {
        ContentValues values = new ContentValues();
        values.put("id", localSerial.getId());
        values.put("tikuId", localSerial.getTikuId());
        values.put("questionid", localSerial.getQuestionid());
        values.put("createTIme", localSerial.getCreateTIme());

        insertOrUpdate("x2_xuhao_question", values, "id", localSerial.getId());
    }

    public void updateOrInsertSerialTiku(LocalSerialTiku serialTiku) {
        ContentValues values = new ContentValues();
        values.put("id", serialTiku.getId());
        values.put("name", serialTiku.getName());
        values.put("twoobject", serialTiku.getTwoobject());
        values.put("section", serialTiku.getSection());
        values.put("createTime", serialTiku.getCreateTime());
        values.put("number", serialTiku.getNumber());
        values.put("sort", serialTiku.getSort());
        values.put("type", serialTiku.getType());

        insertOrUpdate("x2_xuhao_tiku", values, "id", serialTiku.getId());
    }

    public void updateOrInsertParse(LocalParseData parseData) {
        ContentValues values = new ContentValues();
        values.put("parsid", parseData.getParsid());
        values.put("userid", parseData.getParsid());
        values.put("p_time", parseData.getParsid());
        values.put("p_content", parseData.getParsid());
        values.put("p_audit", parseData.getParsid());
        values.put("price", parseData.getParsid());
        values.put("p_type", parseData.getParsid());
        values.put("p_questionid", parseData.getParsid());

        insertOrUpdate("x2_net_pars", values, "parsid", parseData.getParsid());
    }

    public void updateOrInsertTiku(LocalTikuData tikuData) {
        ContentValues values = new ContentValues();

        values.put("stid", tikuData.getStid());
        values.put("subjectid", tikuData.getSubjectid());
        values.put("twoobjectid", tikuData.getTwoobjectid());
        values.put("sectionid", tikuData.getSectionid());
        values.put("knowsid", tikuData.getKnowsid());
        values.put("stname", tikuData.getStname());
        values.put("questionsid", tikuData.getQuestionsid());
        values.put("type", tikuData.getType());
        values.put("levelid", tikuData.getLevelid());

        insertOrUpdate("x2_lower_tiku", values, "stid", tikuData.getStid());
    }

    public void updateOrInsertQuestion(LocalQuestionData questionData) {
        ContentValues values = new ContentValues();

        values.put("questionid", questionData.getQuestionid());
        values.put("questiontype", questionData.getQuestiontype());
        values.put("question", questionData.getQuestion());
        values.put("questiontitle", questionData.getQuestiontitle());
        values.put("questionuserid", questionData.getQuestionuserid());
        values.put("questionusername", questionData.getQuestionusername());
        values.put("questionlastmodifyuser", questionData.getQuestionlastmodifyuser());
        values.put("questionselect", questionData.getQuestionselect());
        values.put("questionselectnumber", questionData.getQuestionselectnumber());
        values.put("questionanswer", questionData.getQuestionanswer());
        values.put("questiondescribe", questionData.getQuestiondescribe());
        values.put("questionknowsid", questionData.getQuestionknowsid());
        values.put("questioncreatetime", questionData.getQuestioncreatetime());
        values.put("questionstatus", questionData.getQuestionstatus());
        values.put("questionhtml", questionData.getQuestionhtml());
        values.put("questionparent", questionData.getQuestionparent());
        values.put("questionsequence", questionData.getQuestionsequence());
        values.put("questionlevel", questionData.getQuestionlevel());
        values.put("oneobjecttype", questionData.getOneobjecttype());
        values.put("twoobjecttype", questionData.getTwoobjecttype());
        values.put("subjecttype", questionData.getSubjecttype());
        values.put("sectiontype", questionData.getSectiontype());
        values.put("questionarticle", questionData.getQuestionarticle());
        values.put("articletitle", questionData.getArticletitle());
        values.put("mathfoundation", questionData.getMathfoundation());
        values.put("views", questionData.getViews());
        values.put("comments", questionData.getComments());
        values.put("discusstime", questionData.getDiscusstime());
        values.put("discussmark", questionData.getDiscussmark());
        values.put("readArticleId", questionData.getReadArticleId());
        values.put("articleContent", questionData.getArticleContent());
        values.put("questionNumber", questionData.getQuestionNumber());


        insertOrUpdate("x2_questions", values, "questionid", questionData.getQuestionid());

    }

    private void insertOrUpdate(String table, ContentValues values, String whereClause, String whereArgs) {
        int update = sqLiteDatabase.update(table, values, whereClause + "=?", new String[]{whereArgs});
        if (update == 0) {
            sqLiteDatabase.insert(table, null, values);
        }
    }

}
