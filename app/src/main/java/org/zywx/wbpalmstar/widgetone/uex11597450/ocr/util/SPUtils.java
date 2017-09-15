package org.zywx.wbpalmstar.widgetone.uex11597450.ocr.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    private static final String OCR_SP_NAME = "ocr_sp_name";

    public static void setBoolean(String key, boolean value, Context c) {
        SharedPreferences.Editor editor = c.getSharedPreferences(OCR_SP_NAME, 0).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, Context c) {
        return c.getSharedPreferences(OCR_SP_NAME, 0).getBoolean(key, false);
    }
}
