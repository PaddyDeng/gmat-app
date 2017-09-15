package org.zywx.wbpalmstar.widgetone.uex11597450.ui.user;

import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.PushProxy;

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

public class ReSetSessionProxy {

    private static ReSetSessionProxy proxy;

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static ReSetSessionProxy getInstance() {
        if (proxy == null) {
            synchronized (ReSetSessionProxy.class) {
                if (proxy == null) {
                    proxy = new ReSetSessionProxy();
                }
            }
        }
        return proxy;
    }


    protected void addToCompositeDis(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }


    public void login(String acc, final String pwd, final ICallBack callBack) {
        addToCompositeDis(HttpUtil.login(acc, pwd)
                .subscribe(new Consumer<UserInfo>() {
                    @Override
                    public void accept(@NonNull final UserInfo userInfo) throws Exception {
                        resetSession(userInfo, pwd, new ReSetSessionProxy.CallBack() {
                            @Override
                            public void onFail() {
                                PushProxy.setAlias(userInfo.getUid());
                                callBack.onFail();
                            }

                            @Override
                            public void onSuccess() {
                                callBack.onSuccess("");
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    public void resetSession(UserInfo mUserInfo, String pwd, final CallBack mCallBack) {
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
                            mCallBack.onSuccess();
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

    public void onDestroy() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    public interface CallBack {
        void onFail();

        void onSuccess();
    }
}
