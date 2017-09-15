package org.zywx.wbpalmstar.widgetone.uex11597450.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    private static String DBHELPER_NAME = "record.db";

    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DBHELPER_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MigrationHelper.getInstance().onCreate(db, false);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            MigrationHelper.getInstance().onCreate(db, true);
            db.execSQL("ALTER TABLE " + PracticeTable.TABLE_NAME + " ADD " + PracticeTable.SERIALTID + " int");
        }
    }
}
