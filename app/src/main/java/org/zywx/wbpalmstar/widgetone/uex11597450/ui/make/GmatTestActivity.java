package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationTopicData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeTable;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.LoginSuccessReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.JsonUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CustomerWebView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.GeneralView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.OptionView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.overscroll.FastAndOverScrollScrollView;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.SimpleShowDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.SimulationResultActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.LoginAactivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/*
接口1: Post http://www.gmatonline.cn/index.php?web/appapi/mokao 传参数 {"id":\模考id\}		//用来告诉服务器即将进行模考的mkid
接口2: Post http://www.gmatonline.cn/index.php?web/appapi/tishi 不传参数		// 用来告诉服务器即将进入模考开始的那个页面
接口3: Post http://www.gmatonline.cn/index.php?web/appapi/\接口2返回来的sign或者自己定义的sign 只有 verbal 和 quant 两个值\   这是一个url拼接  不传参        // 用来获取单个题目
接口4: Post http://www.gmatonline.cn/index.php?web/appapi/checkmkanswer 传参数 {"questionid":\问题id\, "answer":\用户答案\, "duration":\做题时间\}        // 用来提交答案
接口5: Post http://www.gmatonline.cn/index.php?web/appapi/mokaobreak 不传参        // 用来告诉服务器即将进行休息 8 分钟
接口6: Post http://www.gmatonline.cn/index.php?web/appapi/levelbreak 不传参        // 用来告诉服务器即将结束休息, 继续做题
接口7: Post http://www.gmatonline.cn/index.php?web/appapi/result 传参数 {"mkid":\模考的id\, "mkscoreid":\模考结果的id\}        // 用来获取模考的结果
接口8: Post http://www.gmatonline.cn/index.php?web/appapi/mokaoredo 传参数 {mkid:\模考的id\}        // 用来重置模考进度


1. 首先定义一个sign, 每一套模考都有一个单独的sign变量, 下面的操作都是对于sign的修改

2. 进入模考的界面(不管是继续还是开始模考)首先调用接口 1, 返回值处理
	firstcode = 接口1.response["code"]
	if (firstcode == 2) {
		mkscoreid = 接口1.response["mkscoreid"]
		跳转模考结果页, 根据 mkscoreid 访问接口 7
	} else if (firstcode == 3) {
		跳转模考开始页, 并访问接口2, 在离开模考开始页的时候, 接口 2 的返回值里面有sign, 把 sign 用来请求接口 3
	} else if (firstcode == 6) {
		利用 sign = verbal 访问接口 3
	} else if (firstcode == 4) {
		利用 sign = quant 访问接口 3
	} else if (firstcode == 5) {
		跳转模考休息页, 并访问接口 5, 在离开模考休息页的时候, 访问接口 6, 然后利用 sign = verbal 访问接口 3 取题
	}

3. 所有的拿题接口 3 的返回值处理
	secondcode = 接口3.response["code"]
	if (secondcode == 1) {
		showtime = 接口3.response["showtime"]
		正常拿到题目, 根据题目刷新界面, showtime 是模考剩余时间
	} else if (secondcode == 2) {
		mkscoreid = 接口1.response["mkscoreid"]
		跳转模考结果页, 根据 mkscoreid 访问接口 7
	} else if (secondcode == 5) {
		跳转模考休息页, 并访问接口 5, 在离开模考休息页的时候, 访问接口 6, 然后利用 sign = verbal 访问接口 3 取题
	}

4. 接口8 的调用时机是用户点击"重新模考"按钮的时候
 */
public class GmatTestActivity extends BaseActivity {

    public static void startSimulationStart(Context c, SimulationData data) {
        Intent intent = new Intent(c, GmatTestActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        c.startActivity(intent);
    }

    private SimulationData mSimulationData;
    // mark 语文, 数学, 全套, 分别对应 type = "verbal" | "quant" | "all"
    private String mark;
    private LayoutInflater inflater;
    @BindView(R.id.gmat_test_webview)
    GeneralView mWebView;
    @BindView(R.id.gmat_title_webview)
    CustomerWebView mTitleWebView;
    @BindView(R.id.gmat_test_next)
    TextView nextBtn;
    @BindView(R.id.gmat_option_contaienr)
    LinearLayout optionContainer;
    @BindView(R.id.gmat_test_clock)
    TextView clockTv;
    @BindView(R.id.gmat_test_title)
    TextView titleTv;
    @BindView(R.id.gmat_test_back)
    ImageView backTv;
    @BindView(R.id.test_nested_scroll_view)
    FastAndOverScrollScrollView mNestedScrollView;
    @BindView(R.id.rest_container)
    RelativeLayout restContainer;
    @BindView(R.id.rest_time_min_ge)
    TextView min;
    @BindView(R.id.rest_time_sec_shi)
    TextView secTen;
    @BindView(R.id.rest_time_sec_ge)
    TextView secBit;
    @BindView(R.id.gmat_test_collection_btn)
    ImageView collectBtn;
    @BindView(R.id.gmat_simulation_show_time_img)
    ImageView clockImg;

    private String useranswer;
    private int questionid;

    private int type;
    private int time;
    private String allMark = "allmark";
    private boolean startRecordTime = false;
    private int restTime = 8 * 60;
    //    private int restTime = 5;
    private int titleWebViewHeight;
    private boolean startRecordTestTime;
    private long blackStartScreenTime;
    private long startShowTopicTime;
    private String getQuestionUrl;

    protected LoginSuccessReceiver mReceiver = new LoginSuccessReceiver() {
        @Override
        protected void performAction(Intent intent) {
            asyncUiInfo();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmat_test);
        setNextStat(false);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, mReceiver.getIntentFilter());
    }


    private void setNextStat(boolean enable) {
        if (enable) {
            nextBtn.setClickable(true);
            nextBtn.setSelected(true);
        } else {
            nextBtn.setClickable(false);
            nextBtn.setSelected(false);
        }
    }

    @OnClick({R.id.gmat_test_next, R.id.simulation_jump_rest_btn, R.id.gmat_test_collection_btn,
            R.id.gmat_simulation_show_time_img, R.id.gmat_test_clock})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gmat_test_clock:
                Utils.setGone(clockTv);
                Utils.setVisible(clockImg);
                break;
            case R.id.gmat_simulation_show_time_img:
                Utils.setGone(clockImg);
                Utils.setVisible(clockTv);
                break;
            case R.id.gmat_test_next:
                clickNext();
                break;
            case R.id.simulation_jump_rest_btn:
                jumpRestGoonMake();
                break;
            case R.id.gmat_test_collection_btn:
                if (questionid != 0)
                    collection();
                break;
            default:
                break;
        }
    }

    private void collection() {
        final ContentValues values = new ContentValues();
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            values.put(PracticeTable.USERID, GlobalUser.getInstance().getUserData().getUserid());
        } else {
            values.put(PracticeTable.USERID, PracticeTable.DEFAULT_UID);
        }
        values.put(PracticeTable.QUESTIONID, questionid);
        final PracticeManager manager = PracticeManager.getInstance();
        if (manager.queryWhetherCollection(questionid)) {//是收藏的
            manager.dropCollectionData(questionid);
            collectBtn.setSelected(false);
        } else {//未收藏
            manager.insertCollectionData(values);
            collectBtn.setSelected(true);
        }
    }

    //跳过休息继续做题
    private void jumpRestGoonMake() {
        hideTopic(false);
        restEndRequestTopic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (startRecordTime) {
            long rest = (System.currentTimeMillis() - blackStartScreenTime) / 1000;
            long hasTime = restTime - rest;
            if (hasTime < 0) {
                jumpRestGoonMake();
            } else {
                restTime = (int) hasTime;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (startRecordTime) {
            blackStartScreenTime = System.currentTimeMillis();
        }
    }

    private void hideTopic(boolean hide) {
        if (hide) {
            Utils.setGone(backTv, clockTv, nextBtn, mNestedScrollView, collectBtn);
            Utils.setVisible(restContainer);
            startRecordTime = true;
        } else {
            Utils.setVisible(backTv, clockTv, nextBtn, mNestedScrollView, collectBtn);
            Utils.setGone(restContainer);
            startRecordTime = false;
        }
    }

    private Observable<SimulationTopicData> comAnsReqTopic() {
        return HttpUtil.commitAnswer(String.valueOf(questionid), useranswer)
                .flatMap(new Function<ResultBean, ObservableSource<SimulationTopicData>>() {
                    @Override
                    public ObservableSource<SimulationTopicData> apply(@NonNull ResultBean bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            return HttpUtil.getSimulationNextTopic(mark, allMark);
                        } else {
                            throw new RuntimeException(bean.getMessage());
                        }
                    }
                });
    }

    private Observable<SimulationTopicData> requestNextTopic() {
        return HttpUtil.getSimulationNextTopic(mark, allMark).flatMap(new Function<SimulationTopicData, ObservableSource<SimulationTopicData>>() {
            @Override
            public ObservableSource<SimulationTopicData> apply(@NonNull SimulationTopicData data) throws Exception {
                return HttpUtil.getSimulationNextTopic(mark, allMark);
            }
        });
    }

//
//    //是否提交答案，全套模考用
//    private void clickNext(final boolean comAns) {
//        Observable<SimulationTopicData> observable = null;
//        if (comAns) {
//            observable = comAnsReqTopic();
//        } else {
//            observable = requestNextTopic();
//        }
//        addToCompositeDis(observable
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
//                .subscribe(new Consumer<SimulationTopicData>() {
//                    @Override
//                    public void accept(@NonNull SimulationTopicData data) throws Exception {
//                        if (data == null) {
//                            dismissLoadDialog();
//                            return;
//                        }
//                        if (!getHttpResSuc(data.getCode()) && data.getHrefType() == 0) {
//                            againLogin(new ICallBack() {
//                                @Override
//                                public void onSuccess(Object o) {
//                                    clickNext(comAns);
//                                }
//
//                                @Override
//                                public void onFail() {
//                                    dismissLoadDialog();
//                                }
//                            });
//                            return;
//                        }
//                        dismissLoadDialog();
//                        if (data.getHrefType() == 5 && type == C.ALL) {
//                            //全套题，需要休息八分钟
//                            jumpRest();
//                        } else if (data.getHrefType() == 3 || data.getHrefType() == 4 || data.getCode() == 3 || data.getCode() == 4) {
//                            //去结果页
////                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_SIMULATION_INFO));
//                            goResultAct();
//                            finish();
//                        } else if (data.getHrefType() == 1 || data.getHrefType() == 2) {
//                            clickNext();
//                        } else {
//                            allMark = "allmark";
////                            refresh(data);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        if (throwable instanceof RuntimeException) {
//                            RuntimeException throwable1 = (RuntimeException) throwable;
//                            toastShort(throwable1.getMessage());
//                        } else {
//                            errorTip(throwable);
//                        }
//                        setNextStat(true);
//                    }
//                }));
//    }

    private void goResultAct(String mkscoreid) {
        RxBus.get().post(C.SIMULATION_RECORD,type);
        RxBus.get().post(C.SIMULATION_LIST_REFRESH,type);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_SIMULATION_INFO));
        //去结果页时，都要记录下来，当达到做模考5套，10套时提示用户去评价对话框。在结果页中判断是否显示
        SharedPref.setSimuEval(mContext, SharedPref.getSimuEval(mContext) + 1);
        SharedPref.setCanShow(mContext, true);
        SimulationResultActivity.startSimulationResult(mContext, mSimulationData, mkscoreid);
        finish();
    }

    private void clickNext() {
        //提交答案，并请求下一个题目
        setNextStat(false);
//        clickNext(true);
        commitAndRequestNext();
    }

    private void commitAndRequestNext() {
        showLoadDialog();
        int makeTime = (int) ((System.currentTimeMillis() - startShowTopicTime) / 1000);
        addToCompositeDis(HttpUtil
                .commitSimulationAnswer(questionid, useranswer, makeTime)
                .flatMap(new Function<ResultBean, ObservableSource<SimulationTopicData>>() {
                    @Override
                    public ObservableSource<SimulationTopicData> apply(@NonNull ResultBean bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            return HttpUtil.simulationPostSign(getQuestionUrl);
                        }
                        throw new RuntimeException(bean.getMessage());
                    }
                })
                .compose(new SchedulerTransformer<SimulationTopicData>())
                .subscribe(new Consumer<SimulationTopicData>() {
                    @Override
                    public void accept(@NonNull SimulationTopicData data) throws Exception {
                        dismissLoadDialog();
                        dealSimulationTopic(data);
                        RxBus.get().post(C.SIMULATION_RECORD,type);
                        RxBus.get().post(C.SIMULATION_LIST_REFRESH,type);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                        throwable.printStackTrace();
                    }
                }));
    }

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        mSimulationData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void initView() {
        inflater = LayoutInflater.from(mContext);
        mTitleWebView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mTitleWebView,this);
                titleWebViewHeight = mTitleWebView.getMeasuredHeight();
            }
        });

        mTitleWebView.setOnCustomeClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //全部显示。
                int height = mTitleWebView.getMeasuredHeight();

                int contentHeight = (int) (mTitleWebView.getContentHeight() * mTitleWebView.getScale());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleWebView.getLayoutParams();
                if (height == contentHeight) {
                    params.height = titleWebViewHeight;
                    mTitleWebView.setIntercept(true);
                } else {
                    params.height = contentHeight;
                    mTitleWebView.setIntercept(false);
                }
                mTitleWebView.requestLayout();
                mTitleWebView.pageUp(true);
            }
        });
    }

    @Override
    protected void initData() {
        if (mSimulationData == null) return;
        type = mSimulationData.getType();
        if (type == C.LANGUAGE) {
            mark = C.VERBAL;
        } else if (type == C.MATH) {
            mark = C.QUANT;
        } else if (type == C.ALL) {
            mark = C.TYPE_ALL;
        }
    }

    @Override
    protected void asyncUiInfo() {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
//            toastShort(getString(R.string.str_no_login_tip));
            SimpleShowDialog.getInstance("", getString(R.string.str_need_login_tip), new ICallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    forword(LoginAactivity.class);
                }

                @Override
                public void onFail() {

                }
            }).showDialog(getSupportFragmentManager());
            return;
        }
        UserData data = GlobalUser.getInstance().getUserData();
        if (TextUtils.isEmpty(data.getUseremail()) && TextUtils.isEmpty(data.getPhone())) {
            toastShort(R.string.str_set_account_info);
            return;
        }

//        进入模考的界面(不管是继续还是开始模考)首先调用接口 1, 返回值处理
        addToCompositeDis(HttpUtil
                .simulationTest(mSimulationData.getId())
                .flatMap(new Function<ResultBean, ObservableSource<ResultBean>>() {
                    @Override
                    public ObservableSource<ResultBean> apply(@NonNull ResultBean bean) throws Exception {
                        if (bean.getCode() == 3) {
                            return HttpUtil.simulationHint();
                        } else if (bean.getCode() == 4) {
                            return sign("quant");
                        } else if (bean.getCode() == 6) {
                            return sign("verbal");
                        } else if (bean.getCode() == 5) {
                            return resetCode(5);//跳转模考休息页, 并访问接口 5, 在离开模考休息页的时候, 访问接口 6, 然后利用 sign = verbal 访问接口 3 取题
                        } else if (bean.getCode() == 2) {
                            return resetCode(2);
                        }
                        return null;
                    }
                })
                .flatMap(new Function<ResultBean, ObservableSource<SimulationTopicData>>() {
                    @Override
                    public ObservableSource<SimulationTopicData> apply(@NonNull final ResultBean bean) throws Exception {
                        if (bean.getCode() == 5 || bean.getCode() == 2) {
                            return Observable.create(new ObservableOnSubscribe<SimulationTopicData>() {
                                @Override
                                public void subscribe(ObservableEmitter<SimulationTopicData> e) throws Exception {
                                    SimulationTopicData sim = new SimulationTopicData();
                                    sim.setCode(bean.getCode());
                                    e.onNext(sim);
                                    e.onComplete();
                                }
                            });
                        }
                        getQuestionUrl = "http://www.gmatonline.cn/index.php?web/appapi/" + bean.getSign();
                        return HttpUtil.simulationPostSign(getQuestionUrl);
                    }
                })
                .compose(new SchedulerTransformer<SimulationTopicData>())
                .subscribe(new Consumer<SimulationTopicData>() {
                    @Override
                    public void accept(@NonNull SimulationTopicData bean) throws Exception {
                        dealSimulationTopic(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }));

//        oldGetFirstTopic();
    }


    private void restEndRequestTopic() {
//          在离开模考休息页的时候, 访问接口 6,
//          然后利用 sign = verbal 访问接口 3 取题
        addToCompositeDis(HttpUtil
                .simulationResetEnd()
                .flatMap(new Function<ResultBean, ObservableSource<SimulationTopicData>>() {
                    @Override
                    public ObservableSource<SimulationTopicData> apply(@NonNull ResultBean bean) throws Exception {
                        getQuestionUrl = "http://www.gmatonline.cn/index.php?web/appapi/verbal";
                        return HttpUtil.simulationPostSign(getQuestionUrl);
                    }
                })
                .compose(new SchedulerTransformer<SimulationTopicData>())
                .subscribe(new Consumer<SimulationTopicData>() {
                    @Override
                    public void accept(@NonNull SimulationTopicData data) throws Exception {
                        dealSimulationTopic(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }


    private void dealSimulationTopic(@NonNull SimulationTopicData bean) {
        if (getHttpResSuc(bean.getCode())) {
            newRefresh(bean);
        } else if (bean.getCode() == 2) {
//                            mkscoreid = 接口1.response["mkscoreid"]
//                            跳转模考结果页, 根据 mkscoreid 访问接口 7
            goResultAct(bean.getMkscoreid());
        } else if (bean.getCode() == 5) {
//          跳转模考休息页, 并访问接口 5,
//          在离开模考休息页的时候, 访问接口 6,
//          然后利用 sign = verbal 访问接口 3 取题
            jumpRest();
            //请求休息时间
            requestRestTime();
        }
    }

    private void requestRestTime() {
        addToCompositeDis(HttpUtil
                .simulationReset()
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
                            restTime = bean.getShowtime();
                            if (!startRecordTestTime) {
                                startRecordTestTime = true;
                                handler.sendEmptyMessageDelayed(1, 1000);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                }));
    }


    private Observable<ResultBean> resetCode(final int code) {
        return Observable.create(new ObservableOnSubscribe<ResultBean>() {
            @Override
            public void subscribe(ObservableEmitter<ResultBean> e) throws Exception {
                ResultBean bean = new ResultBean();
                bean.setCode(code);
                e.onNext(bean);
                e.onComplete();
            }
        });
    }

    private Observable<ResultBean> sign(final String sign) {
        return Observable.create(new ObservableOnSubscribe<ResultBean>() {
            @Override
            public void subscribe(ObservableEmitter<ResultBean> e) throws Exception {
                ResultBean bean = new ResultBean();
                bean.setSign(sign);
                e.onNext(bean);
                e.onComplete();
            }
        });
    }

    private void newRefresh(SimulationTopicData bean) {
        if (bean == null) return;
        startShowTopicTime = System.currentTimeMillis();
        titleTv.setText(getString(R.string.str_wave_center_des, String.valueOf(bean.getDoMakeNum()), String.valueOf(bean.getAll())));
        time = bean.getShowtime();
        if (!startRecordTestTime) {
            startRecordTestTime = true;
            handler.sendEmptyMessageDelayed(1, 1000);
        }
        SimulationTopicData.DataBean data = bean.getData();
        if (data == null) return;

        questionid = Integer.parseInt(data.getQuestionid());

        final PracticeManager manager = PracticeManager.getInstance();
        if (manager.queryWhetherCollection(questionid)) {//是收藏的
            collectBtn.setSelected(true);
        } else {
            collectBtn.setSelected(false);
        }
        mWebView.setSimulation(data.getQuestion());

        if (!TextUtils.isEmpty(data.getQuestionarticle())) {
            initTitleWebViewHeight();
            Utils.setVisible(mTitleWebView);
            mTitleWebView.setSimulationText(data.getQuestionarticle());
        } else {
            Utils.setGone(mTitleWebView);
        }

        List<SimulationTopicData.DataBean.QslctarrBean> qslctarr = data.getQslctarr();
        if (qslctarr == null && qslctarr.isEmpty()) return;
        optionContainer.removeAllViews();
        setOptionInfo(qslctarr);
    }


//    private void oldGetFirstTopic() {
//        addToCompositeDis(HttpUtil.getSimulationFirstTopic(mSimulationData.getId(), mark)
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
//                .subscribe(new Consumer<SimulationTopicData>() {
//                    @Override
//                    public void accept(@NonNull SimulationTopicData bean) throws Exception {
//                        if (getHttpResSuc(bean.getCode())) {
//                            dismissLoadDialog();
//                            refresh(bean);
//                        } else {
//                            againLogin(new ICallBack() {
//                                @Override
//                                public void onSuccess(Object o) {
//                                    asyncUiInfo();//登录成功去重新获取题目
//                                }
//
//                                @Override
//                                public void onFail() {
//                                    dismissLoadDialog();
//                                }
//                            });
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        throwable.printStackTrace();
//                    }
//                }));
//    }


    private String format(int time) {
        //以秒为单位
        StringBuffer sb = new StringBuffer();
        int hour = time / 3600;
        int newTime = time % 3600;
        if (hour > 0) {
            sb.append("0");
            sb.append(hour);
            sb.append(":");
        }
        int min = newTime / 60;
        int sec = newTime % 60;
        if (min < 10) {
            sb.append("0");
        }
        sb.append(min);
        sb.append(":");
        if (sec < 10) {
            sb.append("0");
        }
        sb.append(sec);
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    private void jumpRest() {
        allMark = "";
        hideTopic(true);
        time = 75 * 60;//跳转后设置个过渡时间段
    }

    final Handler handler = new Handler() {          // handle
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (time > 0) {
                        clockTv.setText(format(--time));
                    } else {//考试时间过了
//                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_SIMULATION_INFO));
//                        goResultAct();
//                        finish();


//                        if (type == C.LANGUAGE || type == C.MATH) {
//                            //去结果页
////                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_SIMULATION_INFO));
//                            goResultAct();
//                            finish();
//                        } else if (type == C.ALL) {
//                            if (restTime > 0) {
//                                jumpRest();
//                            } else {
//                                //去结果页
////                                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_SIMULATION_INFO));
//                                goResultAct();
//                                finish();
//                            }
//                        }
                    }
                    if (startRecordTime) {
                        --restTime;
                        int sec = restTime % 60;
                        min.setText(String.valueOf(restTime / 60));
                        secTen.setText(String.valueOf(sec / 10));
                        secBit.setText(String.valueOf(sec % 10));
                        if (restTime == 0) {
                            jumpRestGoonMake();
                        }
                    }
                    handler.sendEmptyMessageDelayed(1, 1000);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void initTitleWebViewHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleWebView.getLayoutParams();
        params.height = titleWebViewHeight;
        mTitleWebView.setIntercept(true);
        mTitleWebView.requestLayout();
    }
//
//    private void refresh(SimulationTopicData bean) {
//        if (bean == null) return;
//        titleTv.setText(getString(R.string.str_wave_center_des, String.valueOf(bean.getQnyesum()), bean.getQallnum()));
//        SimulationTopicData.MktimeBean mktime = bean.getMktime();
//        time = (Integer.parseInt("01") * 60 + mktime.getMinute()) * 60 + mktime.getSecond();
//        if (!startRecordTestTime) {
//            startRecordTestTime = true;
//            handler.sendEmptyMessageDelayed(1, 1000);
//        }
//        SimulationTopicData.DataBean data = bean.getData();
//        if (data == null) return;
//
//        questionid = Integer.parseInt(data.getQuestionid());
//
//        final PracticeManager manager = PracticeManager.getInstance();
//        if (manager.queryWhetherCollection(questionid)) {//是收藏的
//            collectBtn.setSelected(true);
//        } else {
//            collectBtn.setSelected(false);
//        }
//        mWebView.setSimulation(data.getQuestion());
////        mWebView.setSimulationTestQuestion(data.getQuestion());
//
//        if (!TextUtils.isEmpty(data.getQuestionarticle())) {
//            initTitleWebViewHeight();
//            Utils.setVisible(mTitleWebView);
//            mTitleWebView.setSimulationText(data.getQuestionarticle());
////            mTitleWebView.setSimulationTestTitle(data.getQuestionarticle());
//        } else {
//            Utils.setGone(mTitleWebView);
//        }
//
//        List<SimulationTopicData.DataBean.QslctarrBean> qslctarr = data.getQslctarr();
//        if (qslctarr == null && qslctarr.isEmpty()) return;
//        optionContainer.removeAllViews();
//
//        setOptionInfo(qslctarr);
//
//    }

    private void setOptionInfo(List<SimulationTopicData.DataBean.QslctarrBean> qslctarr) {
        for (int i = 0, size = qslctarr.size(); i < size; i++) {
            SimulationTopicData.DataBean.QslctarrBean qslctarrBean = qslctarr.get(i);
            final OptionView optionView = new OptionView(mContext);
            optionView.setTag(qslctarrBean.getName());
            optionView.setSimulationText(qslctarrBean.getName(), qslctarrBean.getSelect());
            optionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNextStat(true);
                    ViewGroup parent = (ViewGroup) v.getParent();
                    int count = parent.getChildCount();
                    for (int i = 0; i < count; i++) {
                        OptionView ov = (OptionView) parent.getChildAt(i);
                        ov.setSelectedBg(false);
                    }
                    ((OptionView) v).setSelectedBg(true);
                    useranswer = (String) v.getTag();
                }
            });
            optionContainer.addView(optionView);
        }
    }

}
