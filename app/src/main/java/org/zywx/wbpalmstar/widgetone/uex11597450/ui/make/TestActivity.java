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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.EvaluationDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.helper.EvaluationProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.helper.PopHelper;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.test.TopicDiscussionActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.test.TopicErrorFBActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.setting.FontSizeSettingActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.JsonUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SelectedUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.NetParData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeTable;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.SimulationResultActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.GeneralView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CustomerWebView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.NetParView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.OptionView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.overscroll.FastAndOverScrollScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.fromHtml;

/**
 * 先记录所有的问题id。与已经做过的问题id判断，排除已经做过的问题题目。接着打乱问题id，然后一道一道的查找，并做题。
 */
public class TestActivity extends BaseActivity {

    public static void startTestActivity(Context c, QuestionBankData data, @TestType.TestTypeChecker int type) {
        Intent intent = new Intent(c, TestActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.putExtra(Intent.EXTRA_INDEX, type);
        c.startActivity(intent);
    }

    public static void startTestFromCenter(Context c, int stid) {
        Intent intent = new Intent(c, TestActivity.class);
        intent.putExtra(Intent.EXTRA_TITLE, stid);
        c.startActivity(intent);
    }

    @BindView(R.id.test_next)
    TextView nextBtn;
    @BindView(R.id.corrent_answer_tv)
    TextView corAnsTv;
    @BindView(R.id.test_title)
    TextView testTitleTv;
    @BindView(R.id.test_webview)
    GeneralView mWebView;
    @BindView(R.id.title_webview)
    CustomerWebView mTitleWebView;
    @BindView(R.id.option_contaienr)
    LinearLayout optionContainer;
    @BindView(R.id.corrent_answer_container)
    LinearLayout correntAnsContainer;
    @BindView(R.id.corrent_des_container)
    LinearLayout corDesContainer;
    @BindView(R.id.test_collection_btn)
    ImageView collectionBtn;
    private String[] options = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
    private int nextAns;
    @BindView(R.id.test_nested_scroll_view)
    FastAndOverScrollScrollView mNestedScrollView;
    //    @BindView(R.id.test_answer_detail_btn)
//    ImageView answerDetailiIv;
    @BindView(R.id.test_clock_tv)
    TextView clockTv;
    @BindView(R.id.show_time_img)
    ImageView clockImg;
    private int optionRecord;
    private List<String> questionIds;
    private QuestionsData needRecordQuestionsData;
    private QuestionBankData mQuestionBankData;
    private int testType = TestType.SINGLE_PRACTICE;
    private boolean makeTest;
    private int totalQuestion;

    private long currentTime;//记录当前时间
    private int titleWebViewHeight;
    private final int HANDLER_INTERVAL = 100;
    private int makeTime;
    private PopHelper mPopHelper;
    private Observable<Boolean> fontSizeObs;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TITLE)) {
                mQuestionBankData = DBUtil.getInstance().queryQuestionBankThroughtStid(intent.getIntExtra(Intent.EXTRA_TITLE, 0));
                if (mQuestionBankData.getQuestionList() != null) {
                    totalQuestion = mQuestionBankData.getQuestionList().size();
                }
                return;
            }
            mQuestionBankData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            testType = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
            switch (testType) {
                case TestType.INTELLIGENT_TEST:
                    break;
                default:
                    if (mQuestionBankData.getQuestionList() != null) {
                        totalQuestion = mQuestionBankData.getQuestionList().size();
                    }
                    break;
            }
        }
    }

    @OnClick({/*R.id.test_answer_detail_btn,*/ R.id.test_next, R.id.test_collection_btn,
            R.id.show_time_img, R.id.test_clock_tv, R.id.thr_dot_iv, R.id.join_discuss_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_discuss_tv:
                if (needRecordQuestionsData == null) return;
                TopicDiscussionActivity.startTopicDiscussionAct(mContext, needRecordQuestionsData.getQuestionid());
                break;
            case R.id.thr_dot_iv:
                if (mPopHelper != null)
                    mPopHelper.showPop(v);
                break;
            case R.id.test_clock_tv:
                Utils.setGone(clockTv);
                Utils.setVisible(clockImg);
                break;
            case R.id.show_time_img:
                Utils.setGone(clockImg);
                Utils.setVisible(clockTv);
                break;
//            case R.id.test_answer_detail_btn:
//                changeAnswerStatus();
//                break;
            case R.id.test_next:
                if (clickNextBefore()) {
                    new EvaluationDialog().showDialog(getSupportFragmentManager());
                    return;
                }
                if (correntAnsContainer.getVisibility() == View.VISIBLE) {
//                    findViewById(R.id.test_answer_detail_btn).setSelected(false);
                    Utils.setGone(correntAnsContainer);
                }
                clickNext();
                break;
            case R.id.test_collection_btn:
                if (needRecordQuestionsData == null) return;
                collection();
                break;
            default:
                break;
        }
    }

    private boolean clickNextBefore() {
        List<Integer> makeTopic = PracticeManager.getInstance().getMakeQuestId();//做过的所有题目
        int makeTopicSize = makeTopic.size();
        int makeNum = Integer.parseInt(SharedPref.getMakeNum(mContext));
        if (makeNum >= makeTopicSize)
            makeTopicSize = makeNum;
        return EvaluationProxy.topicShowEvalu(String.valueOf(makeTopicSize + 1), mContext);
    }

    private void showResultAnalyze() {
        Utils.setVisible(correntAnsContainer);
        mNestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                mNestedScrollView.scrollTo(0, mNestedScrollView.getHeight());
            }
        });
    }

    private void changeAnswerStatus() {
        if (correntAnsContainer.getVisibility() == View.GONE) {
//            findViewById(R.id.test_answer_detail_btn).setSelected(true);
            showResultAnalyze();
        } else if (correntAnsContainer.getVisibility() == View.VISIBLE) {
//            findViewById(R.id.test_answer_detail_btn).setSelected(false);
            Utils.setGone(correntAnsContainer);
        }
    }

    /**
     * 收藏
     */
    private void collection() {
        final ContentValues values = new ContentValues();
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            values.put(PracticeTable.USERID, GlobalUser.getInstance().getUserData().getUserid());
        } else {
            values.put(PracticeTable.USERID, PracticeTable.DEFAULT_UID);
        }
        values.put(PracticeTable.QUESTIONID, needRecordQuestionsData.getQuestionid());
        final PracticeManager manager = PracticeManager.getInstance();
        if (manager.queryWhetherCollection(needRecordQuestionsData.getQuestionid())) {//是收藏的
            manager.dropCollectionData(needRecordQuestionsData.getQuestionid());
            collectionBtn.setSelected(false);
        } else {//未收藏
            manager.insertCollectionData(values);
            collectionBtn.setSelected(true);
        }
    }

    private void clickNext() {
        setNextStat(false);
        //不计时了
        mHandler.removeMessages(HANDLER_INTERVAL);
        Utils.setGone(correntAnsContainer);
        SharedPref.saveHintWhetherShow(mContext, true);//已经有做题记录了，要显示
        makeTest = true;
        recordTestResult(needRecordQuestionsData);
        searchQuestion(++nextAns);
        //最后一次做题
        saveLastRecord();
    }

    private void saveLastRecord() {
        if (mQuestionBankData == null) return;
        String makeRecord = JsonUtil.toJson(mQuestionBankData);
        SharedPref.saveLastRecord(mContext, makeRecord);
        SharedPref.saveLastRecordType(mContext, testType);
    }

    private void recordTestResult(QuestionsData data) {
        Date date = new Date();
        long useTime = date.getTime() - currentTime;
        final PracticeManager manager = PracticeManager.getInstance();
        final ContentValues values = new ContentValues();
        //顺序做题的stid默认存0
        if (testType == TestType.ORDER_TEST) {
            values.put(PracticeTable.SERIALTID, mQuestionBankData.getStid());
            values.put(PracticeTable.STID, 0);
        } else {
            values.put(PracticeTable.SERIALTID, 0);
            values.put(PracticeTable.STID, mQuestionBankData.getStid());
        }
        values.put(PracticeTable.EXERCISESTATE, C.MAKE_TOPIC);
        values.put(PracticeTable.NEWSTATUS, C.NEED_UPLOAD);
        values.put(PracticeTable.USETIME, useTime);
        values.put(PracticeTable.STARTMAKE, currentTime);
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            values.put(PracticeTable.USERID, GlobalUser.getInstance().getUserData().getUserid());
        } else {
            values.put(PracticeTable.USERID, PracticeTable.DEFAULT_UID);
        }
        values.put(PracticeTable.QUESTIONID, data.getQuestionid());

        if (TextUtils.equals(options[optionRecord], data.getQuestionanswer())) {
            values.put(PracticeTable.TESTRESULT, C.CORRECT);
        } else {
            values.put(PracticeTable.TESTRESULT, C.ERROR);
        }
        values.put(PracticeTable.YOUANSWER, options[optionRecord]);
        Observable.just(1).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                manager.insertData(values);
                return integer;
            }
        }).compose(new SchedulerTransformer<>()).subscribe();
    }

    @Override
    protected void initView() {
        super.initView();
        if (testType == TestType.SEARCH_QUESTION) {
            //隐藏下一题按钮。来自搜索题目，做一题就够了。
            //搜索题目，隐藏title的眼睛，显示scrollview中的解析 clockTv
            Utils.setGone(nextBtn/*, answerDetailiIv*/, clockTv);
        }

        mTitleWebView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mTitleWebView, this);
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

    private void setTitleWebView(String html) {
        mTitleWebView.setText(html);
    }

    private void setLoadWebView(String html) {
        mWebView.setText(html);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setNextStat(false);
        mPopHelper = new PopHelper(mContext);
        mPopHelper.setListene(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.pop_see_analyze:
                        showResultAnalyze();
                        break;
                    case R.id.pop_txt_size:
                        forword(FontSizeSettingActivity.class);
                        break;
                    case R.id.pop_feed_back:
                        if (needRecordQuestionsData == null) return;
                        TopicErrorFBActivity.startTopicError(mContext, needRecordQuestionsData.getQuestionid());
                        break;
                    default:
                        break;
                }
                mPopHelper.dismiss();
            }
        });
        fontSizeObs = RxBus.get().register(C.FONT_SIEZ_CHANGE, Boolean.class);
        fontSizeObs.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (needRecordQuestionsData != null)
                    refreshUi(needRecordQuestionsData);
            }
        });
    }

    @Override
    protected boolean preBackExitPage() {
        if (makeTest && testType != TestType.INTELLIGENT_TEST) {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_MAKE_RECORD));
        }
        return super.preBackExitPage();
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

    private void searchQuestion(int index) {
        //查询题目前
        //未登录，只能做5道题,之后就要登录才能做了。
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            //查询题目总数
            List<PracticeRecordData> topic = PracticeManager.getInstance().getAllMakeTopic();
            if (topic.size() < 4) {
                //可以继续做题
            } else {
                dismissLoadDialog();
                // TODO 弹框提示
                toastShort(getString(R.string.str_no_login_tip));
                return;
            }
        }

        if (index >= questionIds.size()) {
            if (makeTest && testType != TestType.INTELLIGENT_TEST) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REFRESH_MAKE_RECORD));
            }
            SimulationResultActivity.startSearchResult(mContext, mQuestionBankData, testType);
            finishWithAnim();
//            toastShort(R.string.str_end_make_test);
            return;
        }
        Observable.just(questionIds.get(index)).flatMap(new Function<String, ObservableSource<QuestionsData>>() {
            @Override
            public ObservableSource<QuestionsData> apply(@NonNull final String integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<QuestionsData>() {
                    @Override
                    public void subscribe(ObservableEmitter<QuestionsData> e) throws Exception {

                        QuestionsData questionsData = DBUtil.getInstance().queryQuestionsThroughtId(integer);
                        e.onNext(questionsData);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<QuestionsData, ObservableSource<QuestionsData>>() {
            @Override
            public ObservableSource<QuestionsData> apply(@NonNull final QuestionsData data) throws Exception {
                return Observable.create(new ObservableOnSubscribe<QuestionsData>() {
                    @Override
                    public void subscribe(ObservableEmitter<QuestionsData> e) throws Exception {
                        //是否被收藏,ture表示被收藏，否则未被收藏
                        data.setWhetherCollection(PracticeManager.getInstance().queryWhetherCollection(data.getQuestionid()));
                        e.onNext(data);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<QuestionsData, ObservableSource<QuestionsData>>() {
            @Override
            public ObservableSource<QuestionsData> apply(@NonNull final QuestionsData questionsData) throws Exception {
                return Observable.create(new ObservableOnSubscribe<QuestionsData>() {
                    @Override
                    public void subscribe(ObservableEmitter<QuestionsData> e) throws Exception {
                        questionsData.setNetParDatas(DBUtil.getInstance()
                                .queryNetParThId(questionsData.getQuestionid()));
                        e.onNext(questionsData);
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<QuestionsData>()).subscribe(new Consumer<QuestionsData>() {
            @Override
            public void accept(@NonNull QuestionsData questionsData) throws Exception {
                dismissLoadDialog();
                needRecordQuestionsData = questionsData;
                refreshUi(questionsData);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }


    /**
     * 加载非智能学习题目
     * List<QuestionsData>
     */
    private Observable<List<String>> asyncTestRequest() {
        return Observable.just(1).flatMap(new Function<Integer, ObservableSource<List<Integer>>>() {
            @Override
            public ObservableSource<List<Integer>> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<Integer>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Integer>> e) throws Exception {
                        //查找已经做过的题目问题id
                        List<Integer> id = PracticeManager.getInstance().getQuestId(mQuestionBankData.getStid(), testType == TestType.ORDER_TEST);
                        e.onNext(id);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<Integer>, ObservableSource<List<String>>>() {
            @Override
            public ObservableSource<List<String>> apply(@NonNull final List<Integer> integers) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
//                        List<Integer> resultIds = new ArrayList<>();
                        //记录可以做的题目的id,

                        List<String> qIds = Utils.splitStrToList(mQuestionBankData.getQuestionsid());//题库中可以做题的题目
                        if (integers != null && !integers.isEmpty()) {
                            for (int id : integers) {
                                qIds.remove(String.valueOf(id));
                            }
                        }
                        if (testType == TestType.ORDER_TEST) {
                            //顺序练习 升序
                            qIds = DBUtil.getInstance().queryQuestion(qIds);
                        } else {
                            Collections.shuffle(qIds);
                        }
                        e.onNext(qIds);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<String>, ObservableSource<List<String>>>() {
            @Override
            public ObservableSource<List<String>> apply(@NonNull final List<String> list) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                        if (testType != TestType.ORDER_TEST) {
                            //记录有文章的不被打乱
                            List<QuestionsData> dataList = new ArrayList<>();
                            for (String id : list) {
                                QuestionsData questionsData = DBUtil.getInstance().queryQuestionArticleTitles(Integer.parseInt(id));
                                dataList.add(questionsData);
                            }
                            //文章相同，分到一堆。
                            List<QuestionsData> newList = new ArrayList<>();
                            for (int i = 0, size = dataList.size(); i < size; i++) {
                                QuestionsData outData = dataList.get(i);
                                if (newList.contains(outData)) {
                                    continue;
                                }
                                newList.add(outData);
                                for (int j = i + 1; j < size; j++) {
                                    QuestionsData inData = dataList.get(j);
                                    if (TextUtils.equals(outData.getArticletitle(), inData.getArticletitle())) {
                                        if (newList.contains(inData)) {
                                            continue;
                                        }
                                        newList.add(inData);
                                    }
                                }
                            }
                            List<String> ids = new ArrayList<>();
                            for (QuestionsData qtd : newList) {
                                ids.add(String.valueOf(qtd.getQuestionid()));
                            }
                            e.onNext(ids);
                        } else {
                            e.onNext(list);
                        }
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<String>>());
        //通过可以做题的ids去查找题目。
    }

    /**
     * 加载智能学习所有题目
     */
    private Observable<List<String>> asyncIntelligentRequest() {
        return Observable.just(1).flatMap(new Function<Integer, ObservableSource<List<Integer>>>() {
            @Override
            public ObservableSource<List<Integer>> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<Integer>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Integer>> e) throws Exception {
                        //查询已经做过的题目的问题id 智能练习，排除已经做过的题目
                        List<Integer> id = PracticeManager.getInstance().getMakeQuestId();
                        e.onNext(id);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<Integer>, ObservableSource<List<String>>>() {
            @Override
            public ObservableSource<List<String>> apply(@NonNull final List<Integer> integers) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                        //查询所有的问题id
                        List<String> allIds = DBUtil.getInstance().queryAllQuestionId();
                        totalQuestion = allIds.size();
                        if (integers.isEmpty()) {
                        } else {
                            for (int id : integers) {
                                allIds.remove(String.valueOf(id));
                            }
                        }
                        Collections.shuffle(allIds);
                        e.onNext(allIds);
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<String>>());
    }

    private Observable<List<String>> async() {
        if (testType == TestType.INTELLIGENT_TEST) {
            return asyncIntelligentRequest();
        }
        return asyncTestRequest();
    }

    @Override
    protected void asyncUiInfo() {
        showLoadDialog();
        if (testType == TestType.SEARCH_QUESTION) {//来自搜索题目。做一题就够了
            List<Integer> list = mQuestionBankData.getQuestionList();
            questionIds = new ArrayList<>();
            for (int id : list) {
                questionIds.add(String.valueOf(id));
            }
            searchQuestion(0);
            return;
        }
        async().subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(@NonNull List<String> integers) throws Exception {
                questionIds = integers;
                nextAns = 0;
                searchQuestion(nextAns);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (fontSizeObs != null) {
            RxBus.get().unregister(C.FONT_SIEZ_CHANGE, fontSizeObs);
            fontSizeObs = null;
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_INTERVAL:
                    clockTv.setText(formatMakeTime(makeTime++));
                    mHandler.sendEmptyMessageDelayed(HANDLER_INTERVAL, 1000);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 格式化成00:00:00
     */
    private String formatMakeTime(int time) {
        return Utils.formatMakeTime(time, true);
    }

    private void recordMakeTime() {
        mHandler.removeMessages(HANDLER_INTERVAL);
        makeTime = 0;
        mHandler.sendEmptyMessage(HANDLER_INTERVAL);
    }

    private void refreshUi(QuestionsData data) {
        Date date = new Date();
        currentTime = date.getTime();
        recordMakeTime();
        List<QuestionsData> datas = new ArrayList<>();
        datas.add(data);
        refreshUI(datas, 0);
    }

    private void initTitleWebViewHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleWebView.getLayoutParams();
        params.height = titleWebViewHeight;
        mTitleWebView.setIntercept(true);
        mTitleWebView.requestLayout();
    }

    private void refreshUI(List<QuestionsData> datas, int index) {
        if (index >= datas.size()) {
            return;
        }
        if (testType == TestType.SEARCH_QUESTION) {
            Utils.setVisible(correntAnsContainer);
        }
        testTitleTv.setText(getString(R.string.str_test_act_title, totalQuestion - questionIds.size() + 1 + nextAns, totalQuestion));
        QuestionsData data = datas.get(index);

        if (!TextUtils.isEmpty(data.getQuestionarticle())) {
            initTitleWebViewHeight();
            Utils.setVisible(mTitleWebView);
            setTitleWebView(data.getQuestionarticle());
        } else {
            Utils.setGone(mTitleWebView);
        }
        setLoadWebView(data.getQuestion());

        //true表示被收藏
        collectionBtn.setSelected(data.isWhetherCollection());

        corAnsTv.setText(getString(R.string.str_test_corrent_answer, data.getQuestionanswer()));
        List<NetParData> netParDataList = data.getNetParDatas();
        corDesContainer.removeAllViews();
        if (netParDataList != null && !netParDataList.isEmpty()) {
            for (NetParData netP : netParDataList) {
                NetParView netParView = new NetParView(mContext);
                netParView.setNetParContent(netP.getP_time(), netP.getUserid(), netP.getP_content());
                corDesContainer.addView(netParView);
            }
        }

        String questionselect = data.getQuestionselect();
        int selectNumber = data.getQuestionselectnumber();

        setOptionContent(questionselect, selectNumber);
    }

    private void setOptionContent(String questionselect, int selectNumber) {

        List<String> list = SelectedUtils.selectedArray(fromHtml(questionselect).toString(), selectNumber);
        optionContainer.removeAllViews();
        for (int i = 0; i < selectNumber; i++) {
            final OptionView optionView = new OptionView(mContext);
            optionView.setTag(i);
            optionView.setSimulationText(options[i], list.get(i).trim());
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
                    optionRecord = (int) v.getTag();
                }
            });
            optionContainer.addView(optionView);
        }

    }

}
