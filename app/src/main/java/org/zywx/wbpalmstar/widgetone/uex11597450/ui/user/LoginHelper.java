package org.zywx.wbpalmstar.widgetone.uex11597450.ui.user;

import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.RequestCallback;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.Response;


public class LoginHelper {

//    private static CompositeDisposable mCompositeDisposable;

    public static void login(String account, String password, final RequestCallback<UserInfo> requestCallback) {
        HttpUtil.login(account, password)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        requestCallback.beforeRequest();
                    }
                })
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(@NonNull final UserInfo userInfo) throws Exception {
                        if (!Utils.getHttpMsgSu(userInfo.getCode())) {
                            requestCallback.requestFail(userInfo.getMessage());
                            return;
                        }
                        setSession(userInfo, SharedPref.getPassword(GmatApplication.getInstance()),
                                new ICallBack() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (TextUtils.isEmpty(userInfo.getNickname())) {
                                            requestCallback.otherDeal(userInfo);
                                        } else {
                                            requestCallback.requestSuccess(userInfo);
                                        }
                                    }

                                    @Override
                                    public void onFail() {
                                        requestCallback.requestFail("重置session失败");
                                    }

                                });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        requestCallback.requestFail(Utils.onError(throwable));
                    }
                });
    }

    public static void setSession(UserInfo mUserInfo, String pwd, final ICallBack mCallBack) {
        if (mUserInfo == null) {
            //重置失败
            mCallBack.onFail();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            pwd = mUserInfo.getPassword();
        }
        final Map param = new HashMap();
        param.put("uid", mUserInfo.getUid());
        param.put("username", mUserInfo.getUsername());
        param.put("password", pwd);
        param.put("email", mUserInfo.getEmail());
        param.put("phone", mUserInfo.getPhone());
        param.put("nickname", mUserInfo.getNickname());

        addToCompositeDis(Observable.just(1)
                .flatMap(new Function<Integer, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Integer integer) throws Exception {
                        return HttpUtil.toefl(param);
                    }
                }).flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> o) throws Exception {
                        if (o.isSuccessful()) {
                            return HttpUtil.gossip(param);
                        } else {
                            throw new IllegalArgumentException("toefl reset session fail");
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> o) throws Exception {
                        if (o.isSuccessful()) {
                            return HttpUtil.gmatl(param);
                        } else {
                            throw new IllegalArgumentException("gossip reset session fail");
                        }
                    }
                })
                .flatMap(new Function<Response<Void>, ObservableSource<Response<Void>>>() {
                    @Override
                    public ObservableSource<Response<Void>> apply(@NonNull Response<Void> o) throws Exception {
                        if (o.isSuccessful()) {
                            return HttpUtil.smartapply(param);
                        } else {
                            throw new IllegalArgumentException("gmatl reset session fail");
                        }
                    }
                })
                .subscribe(new Consumer<Response<Void>>() {
                    @Override
                    public void accept(@NonNull Response<Void> o) throws Exception {
                        if (o.isSuccessful()) {
                            mCallBack.onSuccess("");
                        } else {
                            throw new IllegalArgumentException("smartapply reset session fail" + o.code());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
                        mCallBack.onFail();
                    }
                }));
    }

    protected static void addToCompositeDis(Disposable disposable) {
//        if (mCompositeDisposable == null) {
//            mCompositeDisposable = new CompositeDisposable();
//        }
//        mCompositeDisposable.add(disposable);
    }

//    public static void onDestroy() {
//        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
//            mCompositeDisposable.dispose();
//            mCompositeDisposable = null;
//        }
//    }

}
