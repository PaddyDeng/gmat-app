package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.search;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.RecycleViewLinearDivider;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestType;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter.SearchQuestionAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
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
import io.reactivex.plugins.RxJavaPlugins;

public class PicSearchActivity extends BaseActivity {


    public static void startAct(Context c, String content) {
        Intent intent = new Intent(c, PicSearchActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        c.startActivity(intent);
    }

    @BindView(R.id.question_search_input)
    EditText inputEdt;
    @BindView(R.id.question_search_shadow)
    RelativeLayout shadow;
    @BindView(R.id.question_search_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_no_result_msg)
    LinearLayout noResult;

    private SearchQuestionAdapter mAdapter;
    private List<QuestionsData> mQuestionsDatas;
    private List<String> recordQuestionIds;
    private String content;
    private static final int SEARCH_WHAT = 99;//搜索题目
    private static final int SEARCH_WAIT = SEARCH_WHAT + 1;//等待搜索
    private static final int WAIT_SEARCH_TIME = 400;//等待搜索时间
    private String needSearchStr;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int what = msg.what;
            if (what == SEARCH_WHAT) {
                String str = (String) msg.obj;
                if (!TextUtils.equals(needSearchStr, str)) {
                    needSearchStr = str;
                }
            }
            switch (what) {
                case SEARCH_WHAT:
                    if (mHandler.hasMessages(SEARCH_WAIT)) {
                        mHandler.removeMessages(SEARCH_WAIT);
                    }
                    mHandler.sendEmptyMessageDelayed(SEARCH_WAIT, WAIT_SEARCH_TIME);
                    break;
                case SEARCH_WAIT:
                    searchQuestion(needSearchStr);
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        content = intent.getStringExtra(Intent.EXTRA_TEXT);
        log(content);
        inputEdt.setText(content);
        inputEdt.setSelection(content.length());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_search);

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    protected void initView() {
        inputEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                log(s.toString());
                //内容改变
                Message message = mHandler.obtainMessage();
                message.what = SEARCH_WHAT;
                message.obj = s.toString();
                mHandler.sendMessage(message);
//                searchQuestion(s.toString());
            }
        });
        mAdapter = new SearchQuestionAdapter(null);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(mContext, LinearLayoutManager.VERTICAL, R.drawable.gray_one_height_divider));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                QuestionsData data = mQuestionsDatas.get(position);
                int questionid = data.getQuestionid();
                List<Integer> list = new ArrayList<>();
                list.add(questionid);
                QuestionBankData questionBankData = new QuestionBankData();
                questionBankData.setQuestionList(list);
                TestActivity.startTestActivity(mContext, questionBankData, TestType.SEARCH_QUESTION);
            }
        });
        if (!TextUtils.isEmpty(content)) {
            inputEdt.setText(content);
            inputEdt.setSelection(content.length());
        }
        shadow.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.keyBordShowFromWindow(mContext, shadow);
            }
        }, 200);
    }

    private void searchQuestion(final String s) {
        if (TextUtils.isEmpty(s)) {
            Utils.setVisible(noResult);
            Utils.setGone(shadow, mRecyclerView);
            return;
        }
        String[] split = s.split("\\n");
        List<String> sentences = new ArrayList<>();
        for (String sentence : split) {
            if (!TextUtils.isEmpty(sentence)) {
                if (sentence.contains(",")) {
                    String[] strings = sentence.split(",");
                    sentences.addAll(Arrays.asList(strings));
                } else if (sentence.contains("?")) {
                    String[] strings = sentence.split("\\?");
                    sentences.addAll(Arrays.asList(strings));
                } else if (sentence.contains(".")) {
                    String[] strings = sentence.split("\\?");
                    sentences.addAll(Arrays.asList(strings));
                } else {
                    sentences.add(sentence);
                }
            }
        }

        addToCompositeDis(Observable.just(sentences)
                .flatMap(new Function<List<String>, ObservableSource<List<QuestionsData>>>() {
                    @Override
                    public ObservableSource<List<QuestionsData>> apply(@NonNull final List<String> strings) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<QuestionsData>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<QuestionsData>> e) throws Exception {
                                int size = 0;
                                for (String str : strings) {
                                    List<QuestionsData> datas = DBUtil.getInstance().fuzzyQueryQuestins(str, size);
                                    if (datas == null || datas.isEmpty()) continue;
                                    size += datas.size();
                                    e.onNext(datas);
                                    if (size > 10) break;
                                }
                                e.onComplete();
                            }
                        });
                    }
                })
                .compose(new SchedulerTransformer<List<QuestionsData>>())
                .subscribe(new Consumer<List<QuestionsData>>() {
                    @Override
                    public void accept(@NonNull List<QuestionsData> datas) throws Exception {
                        if (mQuestionsDatas == null || mQuestionsDatas.isEmpty()) {
                            mQuestionsDatas = datas;
                            Utils.setVisible(mRecyclerView);
                            Utils.setGone(shadow, noResult);
                            mAdapter.setData(datas);
                            recordQuestionIds = new ArrayList<>();
                            for (QuestionsData d : datas) {
                                recordQuestionIds.add(String.valueOf(d.getQuestionid()));
                            }
                        } else {
                            for (Iterator<QuestionsData> it = datas.iterator(); it.hasNext(); ) {
                                String questionid = String.valueOf(it.next().getQuestionid());
                                if (recordQuestionIds.contains(questionid)) {
                                    it.remove();
                                } else {
                                    recordQuestionIds.add(questionid);
                                }
                            }
                            mQuestionsDatas.addAll(datas);
                            mAdapter.updateData();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (mQuestionsDatas == null || mQuestionsDatas.isEmpty()) {
                            Utils.setVisible(noResult);
                            Utils.setGone(shadow, mRecyclerView);
                        }
                    }
                }));
/*
        addToCompositeDis(Observable.just(sentences).flatMap(new Function<List<String>, ObservableSource<List<QuestionsData>>>() {
            @Override
            public ObservableSource<List<QuestionsData>> apply(@NonNull final List<String> strings) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<QuestionsData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<QuestionsData>> e) throws Exception {
                        List<QuestionsData> questionsDatas = DBUtil.getInstance().fuzzyQueryQuestins(strings, s);
                        if (!e.isDisposed()) {
                            if (questionsDatas.isEmpty()) {
                                e.onError(new RuntimeException("list is null"));
                            } else {
                                e.onNext(questionsDatas);
                                e.onComplete();
                            }
                        }
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<QuestionsData>>())
                .subscribe(new Consumer<List<QuestionsData>>() {
                    @Override
                    public void accept(@NonNull List<QuestionsData> datas) throws Exception {
                        mQuestionsDatas = datas;
                        Utils.setVisible(mRecyclerView);
                        Utils.setGone(shadow, noResult);
                        mAdapter.setData(datas);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        Utils.setVisible(noResult);
                        Utils.setGone(shadow, mRecyclerView);
                    }
                }));
                */
    }

    @OnClick({R.id.question_search_cancel_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击“取消”按钮，及半透明背景，均退出页面
            case R.id.question_search_cancel_btn:
                finish();
                break;
        }
    }
}
