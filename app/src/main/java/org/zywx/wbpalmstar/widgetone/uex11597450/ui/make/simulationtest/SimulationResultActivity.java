package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.ScoreData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulatSimpleData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.EvaluationDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.ResetTipDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.ShareDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.helper.EvaluationProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestType;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.ScreenShotUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.WaveLoadingView;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SimulationResultActivity extends BaseActivity {

    public static void startSimulationResult(Context c, SimulationData data) {
        startSimulationResult(c, data, data.getMkscoreid());
    }

    public static void startSimulationResult(Context c, SimulationData data, String mkscoreid) {
        Intent intent = new Intent(c, SimulationResultActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.putExtra(Intent.EXTRA_INDEX, mkscoreid);
        c.startActivity(intent);
    }

    public static void startSearchResult(Context c, QuestionBankData questionBankData, @TestType.TestTypeChecker int type) {
        Intent intent = new Intent(c, SimulationResultActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, questionBankData);//本地来到查看结果页面
        intent.putExtra(Intent.EXTRA_TITLE, type);
        c.startActivity(intent);
    }

    private int type;
    private boolean isSerialMakeTopic;
    private SimulationData mSimulationData;
    private QuestionBankData questionBankData;
    private boolean fromLocal;
    private SimulatSimpleData resultBean;
    private ArrayList<Integer> recordMakeIds;
    private String mkscoreid;
    @BindView(R.id.wave_result)
    WaveLoadingView mWave;
    @BindView(R.id.language_result_tv)
    TextView langResultTv;
    @BindView(R.id.math_result_tv)
    TextView mathResultTv;
    @BindView(R.id.all_result_tv)
    TextView allResultTv;
    @BindView(R.id.result_correct_avg)
    TextView corResultTv;
    @BindView(R.id.result_use_time)
    TextView useTimeResultTv;
    @BindView(R.id.result_des_container)
    LinearLayout mContainer;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        if (intent.hasExtra(Intent.EXTRA_TITLE)) {
            questionBankData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            fromLocal = true;
            type = intent.getIntExtra(Intent.EXTRA_TITLE, TestType.SINGLE_PRACTICE);
            isSerialMakeTopic = type == TestType.ORDER_TEST;
            recordMakeIds = new ArrayList<>();
        } else {
            mSimulationData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            mkscoreid = intent.getStringExtra(Intent.EXTRA_INDEX);
        }
    }

    protected void refreshData(final boolean isSerialMakeTopic, final int stid, final ICallBack<Boolean> callBack) {

        showLoadDialog();
        Observable.just(1).flatMap(new Function<Integer, ObservableSource<Boolean>>() {
            @Override
            public ObservableSource<Boolean> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                        e.onNext(PracticeManager.getInstance().updataStatus(stid, isSerialMakeTopic));
                        e.onComplete();
                    }
                });
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                dismissLoadDialog();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                dismissLoadDialog();
            }
        }).compose(new SchedulerTransformer<Boolean>())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            callBack.onSuccess(aBoolean);
                        } else {
                            toastShort(R.string.str_make_topic_fail);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        callBack.onFail();
                    }
                });

    }

    @OnClick({R.id.simulation_again_start, R.id.result_detail_tv, R.id.result_share_iv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.result_share_iv:
                new ShareDialog().showDialog(getSupportFragmentManager());
                break;
            case R.id.simulation_again_start:
                if (fromLocal) {
                    if (questionBankData != null) {
                        refreshData(isSerialMakeTopic, questionBankData.getStid(), new ICallBack<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                //重置过了，需要刷新
                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_MAKE_RECORD));
                                TestActivity.startTestActivity(mContext, questionBankData, type);
                                finish();
                            }

                            @Override
                            public void onFail() {
                            }
                        });
                    }
                } else {
                    if (mSimulationData != null) {

                        ResetTipDialog.getInstance(new ICallBack<String>() {
                            @Override
                            public void onSuccess(String s) {
                                addToCompositeDis(HttpUtil.simulationReset(mSimulationData.getId())
                                        .doOnSubscribe(new Consumer<Disposable>() {
                                            @Override
                                            public void accept(@NonNull Disposable disposable) throws Exception {
                                                showLoadDialog();
                                            }
                                        })
                                        .subscribe(new Consumer<ResultBean>() {
                                            @Override
                                            public void accept(@NonNull ResultBean bean) throws Exception {
                                                dismissLoadDialog();
                                                if (getHttpResSuc(bean.getCode())) {
                                                    //重新开始
                                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_SIMULATION_INFO));
                                                    RxBus.get().post(C.SIMULATION_RECORD, mSimulationData.getType());
                                                    RxBus.get().post(C.SIMULATION_LIST_REFRESH, mSimulationData.getType());
                                                    SimulationStartActivity.startSimulationStart(mContext, mSimulationData);
                                                    finishWithAnim();
                                                } else {
                                                    toastShort(bean.getMessage());
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(@NonNull Throwable throwable) throws Exception {
                                                dismissLoadDialog();
                                                errorTip(throwable);
                                            }
                                        }));
                            }

                            @Override
                            public void onFail() {
                            }
                        }).showDialog(getSupportFragmentManager());

                    }
                }
                break;
            case R.id.result_detail_tv:
                if (fromLocal) {
                    //去结果详情页
                    SimulationResultDetailActivity.startDetailAct(mContext, recordMakeIds, questionBankData.getStid(), isSerialMakeTopic);
                } else {
                    if (resultBean == null) {
                        return;
                    }
                    if (!getHttpResSuc(resultBean.getCode())) {
                        toastShort(R.string.str_reuslt_detail_tip_no);
                        return;
                    }
                    SimulationResultDetailActivity.startDetailAct(mContext, mSimulationData);
                }
                break;
            default:
                break;
        }
    }

    private void asyncLocalData() {
        //知道本套题的所有题目id，查询id记录正确结果，计算正确率
        showLoadDialog();
        addToCompositeDis(Observable.just(1)
                .flatMap(new Function<Integer, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(@NonNull Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<String>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                                List<Integer> id = PracticeManager.getInstance().getQuestId(questionBankData.getStid(),
                                        questionBankData.getQuestionsid(), isSerialMakeTopic);//做过的题目
                                log("查询做过的题目" + id);
                                List<String> ids = new ArrayList<>();
                                for (int i : id) {
                                    ids.add(String.valueOf(i));
                                }
                                e.onNext(ids);
                                e.onComplete();
                            }
                        });
                    }
                })
//                .flatMap(new Function<List<Integer>, ObservableSource<List<String>>>() {
//                    @Override
//                    public ObservableSource<List<String>> apply(@NonNull final List<Integer> integers) throws Exception {
//                        return Observable.create(new ObservableOnSubscribe<List<String>>() {
//                            @Override
//                            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
//                                log("移除已经做过的题目的id，剩下未做题的题目id");
////                                本套题中的所有题目
//                                List<String> qIds = Utils.splitStrToList(questionBankData.getQuestionsid());
//                                log(questionBankData.getQuestionsid());
//                                //移除已经做过的题目的id，剩下未做题的题目id
//                                if (integers != null && !integers.isEmpty()) {
//                                    for (int id : integers) {
//                                        qIds.remove(String.valueOf(id));
//                                    }
//                                }
//                                e.onNext(qIds);
//                                e.onComplete();
//                            }
//                        });
//                    }
//                })
//
//                .flatMap(new Function<List<String>, ObservableSource<List<String>>>() {
//                    @Override
//                    public ObservableSource<List<String>> apply(@NonNull final List<String> integers) throws Exception {
//                        return Observable.create(new ObservableOnSubscribe<List<String>>() {
//                            @Override
//                            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
//                                //本套题中的所有题目
//                                List<Integer> list = questionBankData.getQuestionList();
//                                List<String> data = new ArrayList<>();
//                                for (int id : list) {
//                                    data.add(String.valueOf(id));
//                                }
//                                log("选出本套题中做过的题目");
//                                //data未做的题目
//                                if (integers != null && !integers.isEmpty()) {
//                                    for (String id : integers) {
//                                        data.remove(id);
//                                    }
//                                }
//                                e.onNext(data);
//                                e.onComplete();
//                            }
//                        });
//                    }
//                })
                .flatMap(new Function<List<String>, ObservableSource<List<PracticeRecordData>>>() {
                    @Override
                    public ObservableSource<List<PracticeRecordData>> apply(@NonNull final List<String> list) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<PracticeRecordData>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<PracticeRecordData>> e) throws Exception {

                                List<PracticeRecordData> practiceRecordDatas = new ArrayList<>();
                                log("查询记录的做题题目答案");
                                for (String id : list) {
                                    List<PracticeRecordData> datas = PracticeManager.getInstance()
                                            .queryMakeTopicDataThroughtId(id, String.valueOf(questionBankData.getStid()), isSerialMakeTopic);
                                    for (PracticeRecordData data : datas) {
                                        if (data.getExerciseState() == C.MAKE_TOPIC) {
                                            recordMakeIds.add(Integer.parseInt(id));
                                            practiceRecordDatas.add(data);
                                        }
                                    }
                                }
                                e.onNext(practiceRecordDatas);
                                e.onComplete();
                            }
                        });
                    }
                })
                .compose(new SchedulerTransformer<List<PracticeRecordData>>()).subscribe(new Consumer<List<PracticeRecordData>>() {
                    @Override
                    public void accept(@NonNull List<PracticeRecordData> datas) throws Exception {
                        dismissLoadDialog();
                        initView(datas);
                        shotScreen();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                }));
    }

    private void shotScreen() {
        langResultTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScreenShotUtil.shoot(SimulationResultActivity.this);
            }
        }, 100);

        showEvaluationDialog();
    }

    private void initView(List<PracticeRecordData> datas) {
        if (questionBankData == null || datas == null) return;
        int useeTime = 0;
        int makeTopicNum = datas.size();
        int correctTopic = 0;//正确的题目数
        for (PracticeRecordData data : datas) {
            useeTime += data.getUsetime() / 1000;
            if (data.getTestResult() == C.CORRECT)
                correctTopic++;
        }
        corResultTv.setText(((int) (correctTopic * 1.0f / makeTopicNum * 100)) + "%");
        useTimeResultTv.setText(Utils.format(useeTime));
        mWave.setAnimDuration(3000);
        mWave.setCenterTitle(getString(R.string.str_wave_center_des, String.valueOf(correctTopic), String.valueOf(makeTopicNum)));
        mWave.setProgressValue(Math.round(correctTopic * 1.0f / makeTopicNum * 100));

    }

    private void showEvaluationDialog() {
        if (fromLocal) return;
        EvaluationProxy.simulationShowEvalu(mContext, getSupportFragmentManager());
//        int simuEval = SharedPref.getSimuEval(mContext);
//        if ((simuEval == 5 || simuEval == 10) && SharedPref.isCanShow(mContext)) {//已经做了5套题或10套题
//            //已经显示，切没有做题，不能显示
//            SharedPref.setCanShow(mContext, false);
//            new EvaluationDialog().showDialog(getSupportFragmentManager());
//        }
    }

    @Override
    protected void asyncUiInfo() {
        if (fromLocal) {
            //加载本地数据库查看结果
            Utils.setInvisible(mContainer);
            asyncLocalData();
            return;
        }
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_SIMULATION_INFO));

        addToCompositeDis(HttpUtil
                .simulationResult(mSimulationData.getId(), mkscoreid)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<SimulatSimpleData>() {
                    @Override
                    public void accept(@NonNull SimulatSimpleData bean) throws Exception {
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_MAKE_RECORD));
                        dismissLoadDialog();
                        resultBean = bean;
                        refreshUi(bean);
                        shotScreen();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                        throwable.printStackTrace();
                        errorTip(throwable);
                    }
                }));

//        addToCompositeDis(HttpUtil.getSimulationRestulShow(mSimulationData.getId(), mSimulationData.getMkscoreid())
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        showLoadDialog();
//                    }
//                })
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        dismissLoadDialog();
//                    }
//                })
//                .subscribe(new Consumer<SimulatSimpleData>() {
//                    @Override
//                    public void accept(@NonNull SimulatSimpleData bean) throws Exception {
//                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_MAKE_RECORD));
//                        dismissLoadDialog();
//                        resultBean = bean;
//                        refreshUi(bean);
//                        shotScreen();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
//                        errorTip(throwable);
//                    }
//                }));

    }

    private void refreshUi(SimulatSimpleData bean) {
        if (mSimulationData == null || bean == null) return;
        corResultTv.setText(String.valueOf((int) Math.round(bean.getCorrect()) + "%"));
        useTimeResultTv.setText(bean.getTotletime());
        String qtruenum = bean.getQtruenum();
        String qyesnum = bean.getQyesnum();
        mWave.setAnimDuration(3000);
        mWave.setCenterTitle(getString(R.string.str_wave_center_des, qtruenum, qyesnum));
        mWave.setProgressValue((int) (Double.parseDouble(qtruenum) / Double.parseDouble(qyesnum) * 100));
        ScoreData credit = bean.getCredit();
        if (credit == null) return;
        Utils.setGone(allResultTv, mathResultTv, langResultTv);
        String qScore = String.valueOf(credit.getQ_score());
        if (Integer.parseInt(qScore) <= 30) {
//            qScore = getString(R.string.str_less_t);
            qScore = "&lt;30";
        }
        String vScore = credit.getV_score();
        if (Integer.parseInt(vScore) <= 25) {
//            vScore = getString(R.string.str_less_tf);
            vScore = "&lt;25";
        }
        if (mSimulationData.getType() == C.LANGUAGE) {
            Utils.setVisible(langResultTv);
            langResultTv.setText(HtmlUtil.fromHtml(getString(R.string.str_lang_result, vScore)));
        } else if (mSimulationData.getType() == C.MATH) {
            Utils.setVisible(mathResultTv);
            mathResultTv.setText(HtmlUtil.fromHtml(getString(R.string.str_math_result, qScore)));
        } else if (mSimulationData.getType() == C.ALL) {
            Utils.setVisible(allResultTv, mathResultTv, langResultTv);
            String totalScore = String.valueOf(credit.getTotalscore());
            if (credit.getTotalscore() <= 470) {
                totalScore = "&lt;470";
            } else {
                int total = credit.getTotalscore();
                if (type == C.ALL) {
                    int i = total % 10;
                    if (i < 4) {
                        i = 0;
                    } else {
                        i = 10;
                    }
                    int num = total / 10;
                    total = num * 10 + i;
                }
                totalScore = String.valueOf(total);
            }
            langResultTv.setText(HtmlUtil.fromHtml(getString(R.string.str_lang_result, vScore)));
            mathResultTv.setText(HtmlUtil.fromHtml(getString(R.string.str_math_result, qScore)));
            allResultTv.setText(HtmlUtil.fromHtml(getString(R.string.str_all_result, totalScore)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_result);
    }
}
