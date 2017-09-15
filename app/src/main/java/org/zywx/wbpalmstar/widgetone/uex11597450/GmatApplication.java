package org.zywx.wbpalmstar.widgetone.uex11597450;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.caimuhao.rxpicker.RxPicker;
import com.facebook.stetho.Stetho;
import com.iflytek.cloud.SpeechUtility;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.AppTracker;
import org.zywx.wbpalmstar.widgetone.uex11597450.service.DBService;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.GlideImageLoader;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;


public class GmatApplication extends Application {

    private static Context mContext;
    private RefWatcher refWatcher;

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {//4.0以上
            registerActivityLifecycleCallbacks(AppTracker.getInstance().getActivityLifecycleCallbacks());
        }
        mContext = this;
        ShareSDK.initSDK(this);
        RxPicker.init(new GlideImageLoader());
        //默认的版本是-1   最新设置的版本为1  为默认就更新数据库，反之不更新
        if (SharedPref.getDBStatus(this) < C.CONTROLLER_DB_UPDATE_VERSION) {
            //开启服务,将外部数据库写入本地包下
            deleteDatabase(DBManager.DB_NAME);
            startService(new Intent(this, DBService.class));
        }

        SpeechUtility.createUtility(this, getString(R.string.app_id));
        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush


        if (BuildConfig.appType == C.APP_TYPE_DEV)  //开发打印log
            Stetho.initializeWithDefaults(this);
        else    //上线开启crash日志
            CrashReport.initCrashReport(getApplicationContext(), "3bb8466ec5", false);

        refWatcher = LeakCanary.install(this);
    }


    public static RefWatcher getRefWatcher(Context context) {
        GmatApplication application = (GmatApplication) context.getApplicationContext();
        return application.refWatcher;
    }

}
