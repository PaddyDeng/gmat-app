package org.zywx.wbpalmstar.widgetone.uex11597450.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;

public class DBService extends IntentService {

    public DBService() {
        super("databasecopy");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DBManager dbManager = new DBManager(getApplicationContext());
        boolean copyDatabase = dbManager.copyDatabase();
        if (copyDatabase) {
            SharedPref.saveDBStatus(getApplicationContext(), C.CONTROLLER_DB_UPDATE_VERSION);
        }
    }
}
