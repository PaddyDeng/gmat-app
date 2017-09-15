package org.zywx.wbpalmstar.widgetone.uex11597450.data;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.PushProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.JsonUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;

public class GlobalUser {
    private static final String TAG = "GlobalUser";
    private static GlobalUser instance;
    private UserData userData;

    private GlobalUser() {
        //希望初始化的时候就初始化userInfo
        if (null == userData)
            resetUserDataBySharedPref(SharedPref.getLoginInfo(GmatApplication.getInstance()));
        if (null == userData)
            userData = new UserData();
    }

    public static GlobalUser getInstance() {
        if (instance == null) {
            synchronized (GlobalUser.class) {
                if (instance == null) {
                    instance = new GlobalUser();
                }
            }
        }
        return instance;
    }

    public UserData getUserData() {
        if (isAccountDataInvalid()) {
            // 同步，避免线程被关闭后，多处调用，多次执行重置
            synchronized (GlobalUser.class) {
                if (isAccountDataInvalid()) {
                    resetUserDataBySharedPref(SharedPref.getLoginInfo(GmatApplication.getInstance()));
                }
            }
        }
        return userData;
    }

    public void setUserData(UserData bean) {
        this.userData = bean;
    }

    public void setNickName(String name) {
        userData.setNickname(name);
    }


    public void setPhone(String phone) {
        userData.setPhone(phone);
    }

    public void setEmail(String email) {
        userData.setUseremail(email);
    }

    public void setPhotoUrl(String url) {
        userData.setPhoto(url);
    }

    public void resetUserInfoToSp(Context c) {
        SharedPref.saveLoginInfo(c, JsonUtil.toJson(GlobalUser.getInstance().getUserData()));
    }

    public void exitLogin(Context mContext) {
        SharedPref.saveLoginInfo(mContext, "");
        GlobalUser.getInstance().clearData();
        SharedPref.saveCookie(mContext, "");
//        SharedPref.saveThrIcon(mContext, "");
//        SharedPref.saveThrId(mContext, "");
//        SharedPref.saveThrName(mContext, "");
        try {
            if (PushProxy.isConnected(mContext))
                PushProxy.stopPush();
        } catch (Exception e) {
        }
//        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.EXIT_LOGIN_ACTION));
        RxBus.get().post(C.EXIT_LOGIN_ACTION, true);
    }

    /**
     * 账户数据是否失效：被系统回收？
     *
     * @return true 无效
     */
    public boolean isAccountDataInvalid() {
        if (null == userData || TextUtils.isEmpty(userData.getUid())) {//===================0合法不
//            Utils.logh(TAG, "getUserData invalid, reset GlobalData");
            instance = getInstance();
            return true;
        }
        return false;
    }

    public void setUid(String uid) {
        userData.setUid(uid);
    }

    /**
     * 直接重置账户信息
     *
     * @param info
     */
    public void resetUserDataBySharedPref(String info) {
        if (!TextUtils.isEmpty(info)) {
            setUserData((UserData) JsonUtil.fromJson(info, new TypeToken<UserData>() {
            }.getType()));
        }
    }

    /**
     * 注销登录时清空数据
     */
    public void clearData() {
        if (instance != null) {
            instance = null;
        }
    }
}
