package org.zywx.wbpalmstar.widgetone.uex11597450.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.zywx.wbpalmstar.widgetone.uex11597450.BuildConfig;
import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.HttpException;

import static android.widget.Toast.makeText;

public class Utils {

    public static boolean LOG_H = false;

    static {
        switch (BuildConfig.appType) {
            case C.APP_TYPE_DEV:
                LOG_H = true;
                break;
            case C.APP_TYPE_PRO:
                LOG_H = false;
                break;
            default:
                LOG_H = false;
                break;
        }
    }

    public static void logh(String tag, String msg) {
        if (LOG_H) {
            Log.d(tag, msg);
        }
    }

    public static boolean isEmpty(String... key) {
        String[] str = key;
        for (int i = 0, size = str.length; i < size; i++) {
            if (TextUtils.isEmpty(str[i])) {
                return true;
            }
        }
        return false;
    }

    public static String formatMakeTime(int time, boolean noHour) {
        StringBuffer sb = new StringBuffer();
        int hour = time / 3600;
        int newTime = time % 3600;
        if (!noHour) {
            sb.append(contactStr(hour));
            sb.append(":");
        }
        int min = newTime / 60;
        int sec = newTime % 60;
        sb.append(contactStr(min));
        sb.append(":");
        sb.append(contactStr(sec));
        return sb.toString();
    }

    public static void removeOnGlobleListener(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public static String formatMakeTime(int time) {
        return formatMakeTime(time, false);
    }

    private static Object contactStr(int time) {
        if (time > 9) {
            return time;
        } else {
            return "0" + time;
        }
    }

    public static String format(int time) {
        //以秒为单位
        StringBuffer sb = new StringBuffer();
        int hour = time / 3600;
        int newTime = time % 3600;
        if (hour > 0) {
            sb.append(hour);
            sb.append("h");
        }
        int min = newTime / 60;
        int sec = newTime % 60;
        if (min > 0) {
            sb.append(min);
            sb.append("m");
        }
        sb.append(sec);
        sb.append("s");
        return sb.toString();
    }

    public static boolean isBelowAndroidVersion(int version) {
        return Build.VERSION.SDK_INT < version;
    }

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getRandomNum(Random random) {
        return random.nextInt(2) + 65;
    }


    public static String onError(Throwable throwable) {
        String errorMsg = "";
        if (throwable instanceof HttpException) {
            switch (((HttpException) throwable).code()) {
                case 403:
                    errorMsg = "没有权限访问此链接！";
                    break;
                case 504:
                    if (isConnected(GmatApplication.getInstance())) {
                        errorMsg = "网络连接超时！";
                    } else {
                        errorMsg = "没有联网哦！";
                    }
                    break;
                default:
                    errorMsg = ((HttpException) throwable).message();
                    break;
            }
        } else if (throwable instanceof UnknownHostException) {
            if (isConnected(GmatApplication.getInstance())) {
                errorMsg = "网络连接超时！";
            } else {
                errorMsg = "没有联网哦！";
            }
        } else if (throwable instanceof SocketTimeoutException) {
            errorMsg = "网络连接超时！";
        }
        return errorMsg;
    }

    public static void copy(String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    public static List<Integer> splitStr(String questionsid) {
        String[] ids = questionsid.split(",");
        List<Integer> arr = new ArrayList<>();
        if (ids != null || ids.length > 0) {
            for (String id : ids) {
                arr.add(Integer.parseInt(id));
            }
        }
        return arr;
    }

    /**
     * 应用市场
     */
    public static void goAppPlay(Context mContext) {
        //这里开始执行一个应用市场跳转逻辑，默认this为Context上下文对象
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + mContext.getPackageName())); //跳转到应用市场，非Google Play市场一般情况也实现了这个接口
        //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
        if (intent.resolveActivity(mContext.getPackageManager()) != null) { //可以接收
            mContext.startActivity(intent);
        } else { //没有应用市场，我们通过浏览器跳转到Google Play
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
            //这里存在一个极端情况就是有些用户浏览器也没有，再判断一次
            if (intent.resolveActivity(mContext.getPackageManager()) != null) { //有浏览器
                mContext.startActivity(intent);
            } else { //天哪，这还是智能手机吗？
                Toast.makeText(mContext, "天啊，您没安装应用市场，连浏览器也没有，您买个手机干啥？", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 计算百分比整数，四舍五入
     */
    public static int caclRount(int fz, int fm) {
        float num = fz * 1.0f / fm;
        return Math.round(num * 100);
    }

    public static String getCurrentVersion(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getCurrentVersionNum(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void callPhone(Activity context, String num) {
        if (TextUtils.isEmpty(num)) return;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + num));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Utils.toastShort(context, "没有拨打电话应用");
        }
    }

    public static List<String> splitStrToList(String questionsid) {
        String[] ids = questionsid.split(",");
        List<String> arr = new ArrayList<>();
        if (ids != null || ids.length > 0) {
            for (String id : ids) {
                arr.add(id);
            }
        }
        return arr;
    }

    public static String getEditTextString(EditText et) {
        Editable text = et.getText();
        return text != null && text.toString().trim().length() != 0 ? text.toString().trim() : null;
    }

    public static void toastShort(Context context, int id) {
        makeText(context, context.getString(id), Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String msg) {
        makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(Context context, String msg, int time) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setDuration(time);
        toast.show();
    }

    public static boolean getHttpMsgSu(int code) {
        if (code == C.REQUEST_RESULT_SUCCESS) {
            return true;
        }
        return false;
    }

    public static void setGone(View... views) {
        if (views != null && views.length > 0) {
            View[] v = views;
            int size = views.length;

            for (int i = 0; i < size; ++i) {
                View view = v[i];
                if (view != null && view.getVisibility() != View.GONE) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    public static void setVisible(View... views) {
        if (views != null && views.length > 0) {
            View[] v = views;
            int size = views.length;

            for (int i = 0; i < size; ++i) {
                View view = v[i];
                if (view != null && view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    public static void setInvisible(View... views) {
        if (views != null && views.length > 0) {
            View[] var4 = views;
            int var3 = views.length;

            for (int var2 = 0; var2 < var3; ++var2) {
                View view = var4[var2];
                if (view != null && view.getVisibility() != View.INVISIBLE) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }

    }


    public static String split(String url) {
        if (TextUtils.isEmpty(url))
            return "";
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        return url;
    }


    public static void keyBordHideFromWindow(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    public static void keyBordShowFromWindow(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void controlTvFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
}
