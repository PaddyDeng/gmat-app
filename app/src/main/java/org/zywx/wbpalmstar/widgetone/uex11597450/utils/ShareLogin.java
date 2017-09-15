package org.zywx.wbpalmstar.widgetone.uex11597450.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import io.reactivex.disposables.Disposable;

public class ShareLogin {

    private static Disposable disposable;
    private static Context mContext;

    public static void shareLogin(final Context context, final ICallBack<List<String>> iCallBack) {
//        mContext = context;
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
//                arg2.printStackTrace();
            }

            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                //用户资源都保存到res
                //通过打印res数据看看有哪些数据是你想要的
                if (action == Platform.ACTION_USER_INFOR) {
                    PlatformDb platDB = platform.getDb();//获取数平台数据DB
//                    //通过DB获取各种数据
//                    Utils.logh("==========icon=============", platDB.getUserIcon());
//                    Utils.logh("==========id=============", platDB.getUserId());
//                    Utils.logh("==========name=============", platDB.getUserName());
                    String iconUrl = platDB.getUserIcon();
                    String userId = platDB.getUserId();
                    String userName = platDB.getUserName();
                    if (Utils.isEmpty(iconUrl, userId, userName)) {
//                        Utils.logh("==========iCallBack=============", "|onFail");
                        iCallBack.onFail();
                    } else {
                        SharedPref.saveThrIcon(context, iconUrl);
                        SharedPref.saveThrId(context, userId);
                        SharedPref.saveThrName(context, userName);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(C.THR_LOGIN_ACTION));
                    }
//                    List<String> list = new ArrayList<>();
//                    list.add(iconUrl);
//                    list.add(userId);
//                    list.add(userName);
//                    iCallBack.onSuccess(list);
//                    开始登录了
//                    disposable = HttpUtil.thrLogin(userId, userName, iconUrl)
//                            .subscribe(new Consumer<UserInfo>() {
//                                @Override
//                                public void accept(@NonNull final UserInfo userInfo) throws Exception {
//                                    if (!Utils.getHttpMsgSu(userInfo.getCode())) {
//                                        iCallBack.onFail();
//                                        Utils.toastShort(context, userInfo.getMessage());
//                                        return;
//                                    }
//                                    ReSetSessionProxy.getInstance().resetSession(userInfo, null, new ReSetSessionProxy.CallBack() {
//                                        @Override
//                                        public void onFail() {
//                                            iCallBack.onFail();
//                                        }
//
//                                        @Override
//                                        public void onSuccess() {
//                                            SharedPref.saveAccount(context, "");
//                                            SharedPref.savePassword(context, "");
//                                            GlobalUser.getInstance().setUid(userInfo.getUid());
//                                            GlobalUser.getInstance().resetUserInfoToSp(context);
//                                            iCallBack.onSuccess(null);
//                                        }
//                                    });
//                                }
//                            }, new Consumer<Throwable>() {
//                                @Override
//                                public void accept(@NonNull Throwable throwable) throws Exception {
//                                }
//                            });
                }
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
            }
        });
        //authorize与showUser单独调用一个即可
        //        qq.authorize();//单独授权,OnComplete返回的hashmap是空的
        qq.showUser(null);//授权并获取用户信息
        //        移除授权
        qq.removeAccount(true);
    }

    public static void dispose() {
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
//        mContext = null;
    }
}
