package org.zywx.wbpalmstar.widgetone.uex11597450.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.CommitData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DownDbService extends Service {

    private String url;

    public DownDbService() {
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        onHandleIntent(intent, startId);
    }

    private void onHandleIntent(Intent intent, int id) {
        if (intent == null) stopSelf();
        url = intent.getStringExtra(C.SYNC_SERVICE_DB_URL_KEY);
        if (TextUtils.isEmpty(url)) {
            stopSelf();
        }

        HttpUtil.download(url)
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<CommitData>>() {
                    @Override
                    public void accept(@NonNull List<CommitData> datas) throws Exception {
                        if (datas != null && !datas.isEmpty()) {
                            PracticeManager.getInstance().deletRecordData();
                            RxBus.get().post(C.UPLOAD_DB_TOPIC_SIZE, datas.size());
//                            RxBus.get().post("MAKE_DB", datas.size());
                            //同步服务器的数据到本地
                            PracticeManager.getInstance().updateData(datas);
                        } else {
                            RxBus.get().post(C.UPLOAD_DB_TOPIC_SIZE, 0);
                        }
                        stopSelf();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        stopSelf();
                    }
                });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("download db service error");
    }
}
