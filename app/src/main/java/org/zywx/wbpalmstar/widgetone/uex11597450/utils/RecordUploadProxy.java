package org.zywx.wbpalmstar.widgetone.uex11597450.utils;

import android.content.Context;
import android.content.Intent;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.CommitData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeTable;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.service.DownDbService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RecordUploadProxy {

    private static File writeToFile(Context context) {
        File file = null;
        String userid = PracticeTable.DEFAULT_UID;
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            userid = GlobalUser.getInstance().getUserData().getUserid();
        }

        final List<CommitData> datas = PracticeManager.getInstance().queryAllData(userid);
        String json = JsonUtil.toJson(datas);
        //写进文件，然后在上传文件。
        String path = context.getFilesDir().getPath();
        file = new File(path, "db.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.append(json);
            bw.flush();
            return file;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 上传本地数据库
     */
    public static void uploadLocalDb(final Context context, final ICallBack iCallBack) {
        SharedPref.saveHintWhetherShow(context, false);//同步过了本地的,隐藏同步提示
        Observable.just(1).flatMap(new Function<Integer, ObservableSource<File>>() {
            @Override
            public ObservableSource<File> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<File>() {
                    @Override
                    public void subscribe(ObservableEmitter<File> e) throws Exception {
                        File file = writeToFile(context);
                        if (file == null) {
                            e.onError(new FileNotFoundException("write make record fail"));
                        } else {
                            e.onNext(file);
                        }
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<File, ObservableSource<ResultBean>>() {
            @Override
            public ObservableSource<ResultBean> apply(@NonNull final File file) throws Exception {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
                return HttpUtil.fileRecord(body);
            }
        }).compose(new SchedulerTransformer<ResultBean>())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        if (iCallBack != null)
                            iCallBack.onSuccess(null);
//                        toastShort(bean.getMessage());
                        if (Utils.getHttpMsgSu(bean.getCode())) {
                            Intent intent = new Intent(context, DownDbService.class);
                            intent.putExtra(C.SYNC_SERVICE_DB_URL_KEY, RetrofitProvider.BASEURL + bean.getRecordPath());
                            context.startService(intent);
                        } else {
                            if (iCallBack != null)
                                Utils.toastShort(context, bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        if (iCallBack != null)
                            iCallBack.onFail();
                    }
                });

//        File file = writeToFile(context);
//
//        if (file == null) {
//            if (iCallBack != null)
//                iCallBack.onFail();
//            return;
//        }
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), requestBody);
//        HttpUtil.fileRecord(body)
//                .subscribe(new Consumer<ResultBean>() {
//                    @Override
//                    public void accept(@NonNull ResultBean bean) throws Exception {
//                        if (iCallBack != null)
//                            iCallBack.onSuccess(null);
////                        toastShort(bean.getMessage());
//                        if (Utils.getHttpMsgSu(bean.getCode())) {
//                            Intent intent = new Intent(context, DownDbService.class);
//                            intent.putExtra(C.SYNC_SERVICE_DB_URL_KEY, RetrofitProvider.BASEURL + bean.getRecordPath());
//                            context.startService(intent);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        if (iCallBack != null)
//                            iCallBack.onFail();
//                    }
//                });
    }

}
