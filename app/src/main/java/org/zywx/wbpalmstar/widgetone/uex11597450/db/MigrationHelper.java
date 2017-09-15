package org.zywx.wbpalmstar.widgetone.uex11597450.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by fire on 2017/9/12  16:37.
 */
public class MigrationHelper {

    private static MigrationHelper instance;

    public static MigrationHelper getInstance() {
        if (instance == null) {
            instance = new MigrationHelper();
        }
        return instance;
    }

    public void upgradeTables(SQLiteDatabase db, String[] tableName, String[] columns) {
        try {
            db.beginTransaction();
            int size = tableName.length;
            String[] tempTable = new String[size];
            //1 将表A重命名，改为_temp临时表
            for (int i = 0; i < size; i++) {
                tempTable[i] = tableName[i] + "_temp";
                String sql = "alter table " + tableName[i] + " RENAME TO " + tempTable[i];
                db.execSQL(sql);
            }
            //2 创建新表
            onCreate(db, true);

            //3 将临时表的数据添加新建表中
            for (int i = 0; i < size; i++) {
                String sql = "INSERT INTO " + tableName[i] +
                        " (" + columns[i] + ") " +
                        " SELECT " + columns[i] + " FROM " + tempTable[i];
                db.execSQL(sql);

                //4 将临时表删除
                db.execSQL("drop table " + tempTable[i]);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void onCreate(SQLiteDatabase db, boolean ifNotExists) {
        String cond = ifNotExists ? "IF NOT EXISTS " : " ";
        String randomSql = "CREATE TABLE " + cond + PracticeTable.RANDOM_NAME + " (ID INT PRIMARY KEY,"
                + PracticeTable.TIMES + " int,"
                + PracticeTable.TYPE + " int,"
                + PracticeTable.DATE + " text,"
                + PracticeTable.COURSEID + " text,"
                + PracticeTable.OPENAPPTIMES + " int"
                + ");";
        String collectSql = "CREATE TABLE " + cond + PracticeTable.COLLECTION_NAME + " (ID INT PRIMARY KEY,"
                + PracticeTable.USERID + " text,"
                + PracticeTable.QUESTIONID + " int"
                + ");";
        String sql = "CREATE TABLE " + cond + PracticeTable.TABLE_NAME + " (ID INT PRIMARY KEY,"
                + PracticeTable.STID + " int,"
                + PracticeTable.QUESTIONID + " int,"
                + PracticeTable.USERID + " text,"
                + PracticeTable.TESTRESULT + " int,"
                + PracticeTable.NEWSTATUS + " int,"
                + PracticeTable.STARTMAKE + " long,"
                + PracticeTable.USETIME + " long,"
                + PracticeTable.YOUANSWER + " text,"
                + PracticeTable.SERIALTID + " int,"
                + PracticeTable.EXERCISESTATE + " int"
                + ");";
        db.execSQL(sql);
        db.execSQL(randomSql);
        db.execSQL(collectSql);
    }

}
