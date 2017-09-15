package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.update;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalParseData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalQuestionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalSerial;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalSerialTiku;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalTikuData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.UpdateLocalDbData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.BaseNoBackgDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.List;

import butterknife.BindView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fire on 2017/8/17  16:17.
 */

public class UpdateDbDialog extends BaseNoBackgDialog implements View.OnClickListener {

    public static UpdateDbDialog getInstance(String url) {
        UpdateDbDialog updateDbDialog = new UpdateDbDialog();
        Bundle bundle = new Bundle();
        bundle.putString("URL_DB", url);
        updateDbDialog.setArguments(bundle);
        return updateDbDialog;
    }

    @BindView(R.id.db_load_fail_container)
    RelativeLayout failContainer;
    @BindView(R.id.db_update_fail_close)
    TextView updateFailClose;
    @BindView(R.id.down_status_tv)
    TextView statusTv;
    @BindView(R.id.update_fail_des)
    TextView failDesTv;
    @BindView(R.id.download_ing_container)
    RelativeLayout downloadingContainer;
    @BindView(R.id.db_update_progress_des_txt)
    TextView laodScheduleTxt;
    @BindView(R.id.db_update_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.update_dialog_container)
    LinearLayout allContainer;
    private String url;
    private boolean updateEnd = false;
//    private RxDownload mRxDownload;

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle bundle = getArguments();
        if (bundle == null) return;
        url = bundle.getString("URL_DB");
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_db_update_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateFailClose.setOnClickListener(this);
//        mRxDownload = RxDownload.getInstance(getActivity());
//        mRxDownload.defaultSavePath(FileUtil.getDBDownloadPath(getActivity()));
        Utils.setVisible(downloadingContainer);
        Utils.setGone(failContainer, failDesTv);
        addToCompositeDis(HttpUtil
                .updateLocalData(url)
                .subscribe(new Consumer<UpdateLocalDbData>() {
                    @Override
                    public void accept(@NonNull UpdateLocalDbData data) throws Exception {
                        async(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateFail();
                    }
                }));
        allContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateEnd) dismiss();
            }
        });

    }

    private int getCount(List<?> list, int size) {
        return (list == null || list.isEmpty()) ? size : size + list.size();
    }

    private void async(UpdateLocalDbData data) {

        final List<LocalQuestionData> question = data.getQuestion();
        final List<LocalTikuData> tiku = data.getTiku();
        final List<LocalParseData> parse = data.getParse();
        final List<LocalSerialTiku> serialTikus = data.getXuhao();
        final List<LocalSerial> serials = data.getXuhaoquestion();
        int size = 0;
        size = getCount(question, size);
        size = getCount(tiku, size);
        size = getCount(parse, size);
        size = getCount(serialTikus, size);
        size = getCount(serials, size);
//        if (question != null && !question.isEmpty()) size += question.size();
//        if (tiku != null && !tiku.isEmpty()) size += tiku.size();
//        if (parse != null && !parse.isEmpty()) size += parse.size();
        if (size == 0) dismiss();
        mProgressBar.setMax(size);

        addToCompositeDis(Flowable
                .create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(final FlowableEmitter<Integer> e) throws Exception {

                        DBUtil.getInstance().updateLocalDb(question, tiku, parse, serialTikus, serials,
                                new ICallBack<Integer>() {
                                    @Override
                                    public void onSuccess(Integer integer) {
                                        e.onNext(integer.intValue());
                                    }

                                    @Override
                                    public void onFail() {
                                        e.onError(new Exception());
                                    }
                                });
                        e.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        setSchedule(integer.intValue());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateFail();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        statusTv.setText(getString(R.string.str_update_end));
                        updateEnd = true;
                        RxBus.get().post(C.UPDATE_LOCAL_DB, true);
                        setCancelable(true);
                    }
                }));
//
//        addToCompositeDis(Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                int progress = 0;
//                for (LocalQuestionData q : question) {
//                    DBUtil.getInstance().updateOrInsertQuestion(q);
//                    e.onNext(++progress);
//                }
//                for (LocalTikuData ti : tiku) {
//                    DBUtil.getInstance().updateOrInsertTiku(ti);
//                    e.onNext(++progress);
//                }
//                for (LocalParseData pa : parse) {
//                    DBUtil.getInstance().updateOrInsertParse(pa);
//                    e.onNext(++progress);
//                }
//                e.onComplete();
//            }
//        }).compose(new SchedulerTransformer<Integer>())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(@NonNull Integer integer) throws Exception {
//                        setSchedule(integer.intValue());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        updateFail();
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        statusTv.setText(getString(R.string.str_update_end));
//                        updateEnd = true;
//                        RxBus.get().post(C.UPDATE_LOCAL_DB, true);
//                        setCancelable(true);
//                    }
//                }));
    }

    private void setSchedule(int progress) {
        mProgressBar.setProgress(progress);
        int schedule = (int) (progress * 1.0 / mProgressBar.getMax() * 100);
        laodScheduleTxt.setText(schedule + "%");
    }

    private void updateFail() {
        Utils.setGone(downloadingContainer);
        Utils.setVisible(failContainer, failDesTv);
        statusTv.setText(getString(R.string.str_update_fail));
    }

//    private String copyDb() {
//        File[] files = mRxDownload.getRealFiles(url);
//        if (files != null) {
//            File file = files[0];
//            if (!file.exists()) {
//                return "";
//            }
//            //            files[0] 下载的文件
//            BufferedReader br = null;
//            try {
//                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//                StringBuffer sb = new StringBuffer();
//                String line = "";
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }
//                return sb.toString();
//            } catch (FileNotFoundException e) {
//                log("FileNotFoundException");
//                return "";
//            } catch (IOException e) {
//                log("IOException");
//                return "";
//            } finally {
//                if (br != null) {
//                    try {
//                        br.close();
//                    } catch (IOException e) {
//                        log("close BufferReader IOException");
//                        return "";
//                    }
//                }
//            }
//        }
//        return "";
//    }
//     addToCompositeDis(mRxDownload.download(url)
//              .compose(new SchedulerTransformer<DownloadStatus>())
//              .subscribe(new Consumer<DownloadStatus>() {
//                  @Override
//                  public void accept(@NonNull DownloadStatus status) throws Exception {
//                  }
//              }, new Consumer<Throwable>() {
//                  @Override
//                  public void accept(@NonNull Throwable throwable) throws Exception {
//                      updateFail();
//                  }
//              }, new Action() {
//                  @Override
//                  public void run() throws Exception {
//                      //下载完成
//                      statusTv.setText(getString(R.string.str_update_ing));
////                        打开下载文本，读取内容更新数据库
//                      String dbInfo = copyDb();
//                      if (TextUtils.isEmpty(dbInfo)) {
//                          updateFail();
//                          return;
//                      }
//                      UpdateLocalDbData updateLocalDbData = JsonUtil.fromJson(dbInfo, new TypeToken<UpdateLocalDbData>() {
//                      }.getType());
//                      log(updateLocalDbData.getParse().size() + "   " + updateLocalDbData.getQuestion().size() + "   " + updateLocalDbData.getTiku().size());
//                  }
//              }));

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.db_update_fail_close:
                dismiss();
                break;
            default:
                break;
        }
    }
}
