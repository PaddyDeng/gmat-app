package org.zywx.wbpalmstar.widgetone.uex11597450.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

public class SharedPref {
    private static String TAG = "SharedPref";
    protected static String PREFS_NAME = "SHAREDPREF_GMAT";
    public static final String PREFS_STR_INVALID = "";
    // 登录信息，避免每次进程被系统清除后，每次都请求登录
    private final static String PREFS_KEY_LOGIN_INFO = "prefs_key_login_first_info";
    private final static String PREFS_KEY_USER_NAME = "prefs_key_user_name";
    private final static String PREFS_KEY_USER_PWD = "prefs_key_user_pwd";
    private final static String PREFS_KEY_COOKIE = "prefs_key_cookie";
    private final static String PREFS_KEY_STID = "prefs_key_stid";
    private final static String PREFS_KEY_GO_MSG_LIST = "prefs_key_go_msg_list";
    private final static String PREFS_KEY_NEW_STATUS = "prefs_key_new_status";
    private final static String PREFS_KEY_WRITE_TO_ONE_NEW_DB = "prefs_key_new_db";

    private final static String PREFS_KEY_THR_ICON_URL = "prefs_key_thr_icon_url";
    private final static String PREFS_KEY_THR_ID = "prefs_key_thr_id";
    private final static String PREFS_KEY_THR_NAME = "prefs_key_thr_name";
    private final static String PREFS_LAST_RECORD = "prefs_last_record";
    private final static String PREFS_LAST_RECORD_TYPE = "prefs_last_record_type";
    private final static String PREFS_FONT_SIZE_RECORD = "prefs_font_size_record";
    private final static String PREFS_OFFLINE_TIP_NEED = "prefs_offline_tip_need";
    private final static String PREFS_CNETER_MAKE_NUM = "prefs_cneter_make_num";
    private final static String PREFS_CNETER_MAKE_AVG = "prefs_cneter_make_avg";
    private final static String PREFS_RECORD_ASYNC_HINT = "prefs_record_async_hint";
    private final static String PREFS_SHOW_EVALUATION_KEY = "prefs_show_evaluation_key";
    private final static String PREFS_SHOW_EVALUATION_SIMULATION_KEY = "prefs_show_evaluation_simulation_key";
    private final static String PREFS_SHOW_EVALUATION_SIMULATION_CONAL_KEY = "prefs_show_evaluation_simulation_conal_key";
    private final static String PREFS_UPDATE_LOCAL_DB = "prefs_update_local_db";
    private final static String PREFS_SHOW_ACTION_TIMES = "prefs_show_action_times";
    private final static String PREFS_SHOW_ACTION_ID = "prefs_show_action_id";

    public static String getActionId(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getString(PREFS_SHOW_ACTION_ID,"");
    }

    public static void setActionId(Context c, String id) {
        setString(PREFS_SHOW_ACTION_ID, id, c);
    }

    public static int getCurrentTIMES(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getInt(PREFS_SHOW_ACTION_TIMES, 0);
    }

    public static void setCurrentTIMES(Context c, int time) {
        setInt(PREFS_SHOW_ACTION_TIMES, time, c);
    }

    public static int getLocalDBTime(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getInt(PREFS_UPDATE_LOCAL_DB, 0);
    }

    public static void setLocalDBTime(Context c, int time) {
        setInt(PREFS_UPDATE_LOCAL_DB, time, c);
    }

    public static boolean isCanShow(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getBoolean(PREFS_SHOW_EVALUATION_SIMULATION_CONAL_KEY, false);
    }

    public static void setCanShow(Context c, boolean show) {
        setBoolean(PREFS_SHOW_EVALUATION_SIMULATION_CONAL_KEY, show, c);
    }

    public static int getSimuEval(Context c) {//默认字号未14sp
        return c.getSharedPreferences(PREFS_NAME, 0).getInt(PREFS_SHOW_EVALUATION_SIMULATION_KEY, 0);
    }

    public static void setSimuEval(Context c, int index) {
        setInt(PREFS_SHOW_EVALUATION_SIMULATION_KEY, index, c);
    }

    public static boolean isHint(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getBoolean(PREFS_RECORD_ASYNC_HINT, false);
    }

    public static void saveHintWhetherShow(Context c, boolean show) {
        setBoolean(PREFS_RECORD_ASYNC_HINT, show, c);
    }

    public static String getEval(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getString(PREFS_SHOW_EVALUATION_KEY, "");
    }

    public static void setEval(Context c, String value) {
        setString(PREFS_SHOW_EVALUATION_KEY, value, c);
    }

    public static String getMakeAvg(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getString(PREFS_CNETER_MAKE_AVG, "");
    }

    public static void setMakeAvg(Context c, String index) {
        setString(PREFS_CNETER_MAKE_AVG, index, c);
    }

    public static String getMakeNum(Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getString(PREFS_CNETER_MAKE_NUM, "0");
    }

    public static void setMakeNum(Context c, String index) {
        setString(PREFS_CNETER_MAKE_NUM, index, c);
    }

    public static int getFontSize(Context c) {//默认字号未14sp
        return c.getSharedPreferences(PREFS_NAME, 0).getInt(PREFS_FONT_SIZE_RECORD, 2);
    }

    public static void saveFontSize(Context c, int index) {
        setInt(PREFS_FONT_SIZE_RECORD, index, c);
    }

    public static int getLastRecordType(Context c) {
        return getInt(PREFS_LAST_RECORD_TYPE, c);
    }

    public static void saveLastRecordType(Context c, int lastRecord) {
        setInt(PREFS_LAST_RECORD_TYPE, lastRecord, c);
    }

    public static String getLastRecord(Context c) {
        return getString(PREFS_LAST_RECORD, c);
    }

    public static void saveLastRecord(Context c, String lastRecord) {
        setString(PREFS_LAST_RECORD, lastRecord, c);
    }

    public static String getThrName(Context c) {
        return getString(PREFS_KEY_THR_NAME, c);
    }

    public static void saveThrName(Context c, String informType) {
        setString(PREFS_KEY_THR_NAME, informType, c);
    }

    public static String getThrId(Context c) {
        return getString(PREFS_KEY_THR_ID, c);
    }

    public static void saveThrId(Context c, String informType) {
        setString(PREFS_KEY_THR_ID, informType, c);
    }

    public static String getThrIcon(Context c) {
        return getString(PREFS_KEY_THR_ICON_URL, c);
    }

    public static void saveThrIcon(Context c, String informType) {
        setString(PREFS_KEY_THR_ICON_URL, informType, c);
    }

    private SharedPref() {
    }

    public static int getDBStatus(Context c) {
        return getInt(PREFS_KEY_WRITE_TO_ONE_NEW_DB, c);
    }


    public static void saveDBStatus(Context c, int version) {
        setInt(PREFS_KEY_WRITE_TO_ONE_NEW_DB, version, c);
    }

    public static boolean getOfflineTip(Context c) {
        return getBoolean(PREFS_OFFLINE_TIP_NEED, c);
    }

    public static void saveOfflineTip(Context c, boolean status) {
        setBoolean(PREFS_OFFLINE_TIP_NEED, status, c);
    }

    public static String getOrder(Context c) {
        return getString(PREFS_KEY_GO_MSG_LIST, c);
    }

    public static void saveOrder(Context c, String informType) {
        setString(PREFS_KEY_GO_MSG_LIST, informType, c);
    }

    public static int getNewStatus(Context c) {
        return getInt(PREFS_KEY_NEW_STATUS, c);
    }

    public static void saveNewStatus(Context c, int num) {
        setInt(PREFS_KEY_NEW_STATUS, num, c);
    }

    public static int getStid(Context c) {
        return getInt(PREFS_KEY_STID, c);
    }

    public static void saveStid(Context c, int stid) {
        setInt(PREFS_KEY_STID, stid, c);
    }

    public static void saveCookie(Context c, String cookie) {
        setString(PREFS_KEY_COOKIE, new String(Base64.encodeToString(cookie.getBytes(),
                Base64.DEFAULT)), c);
    }

    public static String getCookie(Context c) {
        String pwd = getString(PREFS_KEY_COOKIE, c);
        if (isInvalidPrefString(pwd)) {
            return PREFS_STR_INVALID;
        }
        pwd = new String(Base64.decode(pwd.getBytes(),
                Base64.DEFAULT));
        return pwd;
    }

    public static void savePassword(Context c, String pwd) {
        setString(PREFS_KEY_USER_PWD, new String(Base64.encodeToString(pwd.getBytes(),
                Base64.DEFAULT)), c);
    }

    public static String getPassword(Context c) {
        String pwd = getString(PREFS_KEY_USER_PWD, c);
        if (isInvalidPrefString(pwd)) {
            return PREFS_STR_INVALID;
        }
        pwd = new String(Base64.decode(pwd.getBytes(),
                Base64.DEFAULT));
        return pwd;
    }

    public static void saveAccount(Context c, String account) {
        setString(PREFS_KEY_USER_NAME, new String(Base64.encodeToString(account.getBytes(),
                Base64.DEFAULT)), c);
    }

    public static String getAccount(Context c) {
        String account = getString(PREFS_KEY_USER_NAME, c);
        if (isInvalidPrefString(account)) {
            return PREFS_STR_INVALID;
        }
        account = new String(Base64.decode(account.getBytes(),
                Base64.DEFAULT));
        return account;
    }

    /**
     * 保存LoginInfo
     */
    public static void saveLoginInfo(Context c, String loginInfo) {
        setString(PREFS_KEY_LOGIN_INFO, new String(Base64.encodeToString(loginInfo.getBytes(),
                Base64.DEFAULT)), c);
    }

    public static String getLoginInfo(Context c) {
        String loginInfo = getString(PREFS_KEY_LOGIN_INFO, c);
        if (isInvalidPrefString(loginInfo)) {
            return PREFS_STR_INVALID;
        }
        loginInfo = new String(Base64.decode(loginInfo.getBytes(),
                Base64.DEFAULT));
        return loginInfo;
    }

    public static void setBoolean(String key, boolean value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getBoolean(key, false);
    }

    public static void setString(String key, String value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getString(key, "");
    }

    public static boolean isInvalidPrefString(String value) {
        return value == null || "".equals(value);
    }

    public static void setInt(String key, int value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(String key, Context c) {
        return c.getSharedPreferences(PREFS_NAME, 0).getInt(key, -1);
    }

}
