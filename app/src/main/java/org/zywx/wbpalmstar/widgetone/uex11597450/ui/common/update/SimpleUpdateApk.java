package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.update;

import android.Manifest;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.VersionInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.permission.RxPermissions;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SimpleUpdateApk implements DownloadApk.OnDownloadApkListener {

    private FragmentActivity mActivity;
    private DownloadApk downloadApk;
    protected CompositeDisposable mCompositeDisposable;
    private boolean showLatest;

    public SimpleUpdateApk(FragmentActivity activity) {
        this(activity, false);//默认不显示最新应用提示
    }

    public SimpleUpdateApk(FragmentActivity mActivity, boolean showLatest) {
        this.mActivity = mActivity;
        downloadApk = new DownloadApk(mActivity);
        downloadApk.setOnDownloadApkListener(this);
        mCompositeDisposable = new CompositeDisposable();
        this.showLatest = showLatest;
    }

    public void checkVersionUpdate() {
        mCompositeDisposable.add(asyncVersionInfo());
    }

    public void onDestory() {
        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
            mCompositeDisposable = null;
        }
        mActivity = null;
        downloadApk = null;
    }

    private Disposable asyncVersionInfo() {
        return HttpUtil.getUpdate()
                .subscribe(new Consumer<VersionInfo>() {
                    @Override
                    public void accept(@NonNull final VersionInfo bean) throws Exception {
                        if (Utils.getCurrentVersionNum(mActivity) < bean.getVersions()) {
                            //弹框提示用户是否需要更新
                            showTipDialog(bean);
                        } else {
                            if (showLatest) {
                                showToast(R.string.str_is_new_apk);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }

    private void showToast(@StringRes int resId) {
        Utils.toastShort(mActivity, mActivity.getString(resId));
    }

    private void showTipDialog(final VersionInfo info) {
        UpdateNewDialog.getInstance(new ICallBack<String>() {
            @Override
            public void onSuccess(String s) {
                new RxPermissions(mActivity).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            downloadApk.downloadApk(info.getApk());
                        } else {
                            showToast(R.string.str_need_sdcard_permission);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
            }

            @Override
            public void onFail() {
            }
        }).showDialog(mActivity.getSupportFragmentManager());
      /*  UpdateApkDialog.getInstance(info.getText(), new ICallBack<String>() {
            @Override
            public void onSuccess(String s) {
                new RxPermissions(mActivity).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            downloadApk.downloadApk(info.getApk());
                        } else {
                            showToast(R.string.str_need_sdcard_permission);
//                            Utils.toastShort(mActivity, mActivity.getString(R.string.str_need_sdcard_permission));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        Utils.onError(throwable);
                    }
                });
            }

            @Override
            public void onFail() {
            }
        }).showDialog(mActivity.getSupportFragmentManager());
*/
    }

    @Override
    public void onDownError() {
//        Utils.toastShort(mActivity, mActivity.getString(R.string.str_update_apk_fail));
        showToast(R.string.str_update_apk_fail);
    }
}
