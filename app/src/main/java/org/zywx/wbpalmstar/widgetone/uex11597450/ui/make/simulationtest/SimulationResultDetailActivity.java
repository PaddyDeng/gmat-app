package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.BaseResultFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.OptionInfoData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.QuestionRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeTable;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.LocalResultFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.adapter.InfoDetailAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.adapter.SerialAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SimulationResultDetailActivity extends BaseActivity {

    public static void startDetailAct(Context c, ArrayList<Integer> ids, int stid, boolean isSerialTkId) {
        Intent intent = new Intent(c, SimulationResultDetailActivity.class);
        intent.putIntegerArrayListExtra(Intent.EXTRA_TEXT, ids);
        intent.putExtra(Intent.EXTRA_INDEX, isSerialTkId);
        intent.putExtra(Intent.EXTRA_REFERRER, stid);
        c.startActivity(intent);
    }

    public static void startDetailAct(Context c, SimulationData mSimulationData) {
        Intent intent = new Intent(c, SimulationResultDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, mSimulationData);
        c.startActivity(intent);
    }

    @BindView(R.id.topic_serial_num_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.question_vp)
    ViewPager mViewPager;
    @BindView(R.id.result_answer_detail_btn)
    ImageView answerDetailIv;
    @BindView(R.id.result_collection_btn)
    ImageView collectionIv;

    private SimulationData mSimulationData;
    private List<OptionInfoData> optionInfoDatas;
    private SerialAdapter adapter;
    private ArrayList<Integer> ids;
    private boolean fromLocal;
    private int currentPage;
    private int stid;
    private boolean isSerialTkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation_result_detail);
    }

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        if (intent.hasExtra(Intent.EXTRA_INDEX)) {
            fromLocal = true;
            isSerialTkId = intent.getBooleanExtra(Intent.EXTRA_INDEX, false);
            ids = intent.getIntegerArrayListExtra(Intent.EXTRA_TEXT);
            stid = intent.getIntExtra(Intent.EXTRA_REFERRER, 0);
        } else {
            mSimulationData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }
    }

    @Override
    protected void initData() {
        if (fromLocal) {
            //查询本地做题记录,刷新ui
            asyncLocalRecord();
            return;
        }
        addToCompositeDis(HttpUtil.getSimulationRestul(mSimulationData.getId(), mSimulationData.getMkscoreid())
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
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        refreshUI(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void asyncLocalRecord() {
        if (ids == null || ids.isEmpty()) return;
        Observable.just(ids).flatMap(new Function<ArrayList<Integer>, ObservableSource<List<PracticeRecordData>>>() {
            @Override
            public ObservableSource<List<PracticeRecordData>> apply(@NonNull final ArrayList<Integer> integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<PracticeRecordData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<PracticeRecordData>> e) throws Exception {
                        List<PracticeRecordData> datas = new ArrayList<>();
                        for (int id : integer) {
                            List<PracticeRecordData> practiceRecordDatas = PracticeManager.getInstance()
                                    .queryMakeTopicDataThroughtId(String.valueOf(id), String.valueOf(stid), isSerialTkId);
                            datas.addAll(practiceRecordDatas);
                        }
                        e.onNext(datas);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<PracticeRecordData>, ObservableSource<List<QuestionsData>>>() {
            @Override
            public ObservableSource<List<QuestionsData>> apply(@NonNull final List<PracticeRecordData> datas) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<QuestionsData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<QuestionsData>> e) throws Exception {
                        List<QuestionsData> questionsDatas = new ArrayList<>();
                        for (PracticeRecordData data : datas) {
                            QuestionsData questionsData = DBUtil.getInstance().queryQuestionsThroughtId(String.valueOf(data.getQuestionid()));
                            questionsData.setYouAnswer(data.getYouanswer());
                            questionsData.setUserTime(Utils.format((int) (data.getUsetime() / 1000)));
                            questionsData.setYouChooseResult(data.getTestResult() == C.CORRECT ? true : false);
                            //是否被收藏
                            questionsData.setWhetherCollection(PracticeManager.getInstance().queryWhetherCollection(data.getQuestionid()));
                            questionsDatas.add(questionsData);
                        }
                        e.onNext(questionsDatas);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<QuestionsData>, ObservableSource<List<QuestionsData>>>() {
            @Override
            public ObservableSource<List<QuestionsData>> apply(@NonNull final List<QuestionsData> datas) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<QuestionsData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<QuestionsData>> e) throws Exception {
                        //查询评价
                        for (QuestionsData data : datas) {
                            data.setNetParDatas(DBUtil.getInstance()
                                    .queryNetParThId(data.getQuestionid()));
                        }
                        e.onNext(datas);
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<QuestionsData>>())
                .subscribe(new Consumer<List<QuestionsData>>() {
                    @Override
                    public void accept(@NonNull List<QuestionsData> datas) throws Exception {
                        refreshLocalData(datas);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }

    private void refreshLocalData(final List<QuestionsData> datas) {
        if (datas == null || datas.isEmpty()) return;
        //初始化recycler
        Observable.just(1).map(new Function<Integer, List<OptionInfoData>>() {
            @Override
            public List<OptionInfoData> apply(@NonNull Integer integer) throws Exception {
                int i = 0;
                List<OptionInfoData> optInfoList = new ArrayList<>();
                for (QuestionsData data : datas) {
                    OptionInfoData infoData = new OptionInfoData();
                    if (i == 0) {
                        infoData.setSelected(true);
                    }
                    infoData.setTopicNum(++i);
                    infoData.setCorrect(data.isYouChooseResult());
                    optInfoList.add(infoData);
                }
                return optInfoList;
            }
        }).compose(new SchedulerTransformer<List<OptionInfoData>>()).subscribe(new Consumer<List<OptionInfoData>>() {
            @Override
            public void accept(@NonNull List<OptionInfoData> datas) throws Exception {
                if (datas != null) {
                    optionInfoDatas = datas;
                    adapter = new SerialAdapter(mContext, datas);
                    adapter.setListener(new OnItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            mViewPager.setCurrentItem(position, false);
                            resetSelected(position);
                            initTitleStatus(position);
                        }
                    });
                    mRecyclerView.setAdapter(adapter);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
        //初始化viewpager
        Observable.just(1)
                .map(new Function<Integer, List<Fragment>>() {
                    @Override
                    public List<Fragment> apply(@NonNull Integer integer) throws Exception {
                        List<Fragment> list = new ArrayList<>();
                        for (QuestionsData data : datas) {
                            list.add(LocalResultFragment.getInstance(data));
                        }
                        return list;
                    }
                })
                .compose(new SchedulerTransformer<List<Fragment>>())
                .subscribe(new Consumer<List<Fragment>>() {
                    @Override
                    public void accept(@NonNull List<Fragment> fragments) throws Exception {
                        mViewPager.setAdapter(new InfoDetailAdapter(getSupportFragmentManager(), fragments));
                        initTitleStatus(0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
        addListener();
    }

    private void refreshUI(ResultBean bean) {
        if (bean == null) return;
        final ArrayList<QuestionRecordData> questionrecord = bean.getQuestionrecord();
        if (questionrecord == null && questionrecord.isEmpty()) return;
//        初始化recycler
        Observable.just(1).map(new Function<Integer, List<OptionInfoData>>() {
            @Override
            public List<OptionInfoData> apply(@NonNull Integer integer) throws Exception {
                int i = 0;
                List<OptionInfoData> optInfoList = new ArrayList<>();
                for (QuestionRecordData data : questionrecord) {
                    OptionInfoData infoData = new OptionInfoData();
                    if (i == 0) {
                        infoData.setSelected(true);
                    }
                    infoData.setTopicNum(++i);
                    infoData.setCorrect(TextUtils.equals(data.getUseranswer(), data.getQuestionanswer()));
                    optInfoList.add(infoData);
                }
                return optInfoList;
            }
        }).compose(new SchedulerTransformer<List<OptionInfoData>>()).subscribe(new Consumer<List<OptionInfoData>>() {
            @Override
            public void accept(@NonNull List<OptionInfoData> datas) throws Exception {
                if (datas != null) {
                    optionInfoDatas = datas;
                    adapter = new SerialAdapter(mContext, datas);
                    adapter.setListener(new OnItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            mViewPager.setCurrentItem(position, false);
                            resetSelected(position);
                            initTitleStatus(position);
                        }
                    });
                    mRecyclerView.setAdapter(adapter);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
        //初始化viewpager
        Observable.just(1)
                .map(new Function<Integer, List<Fragment>>() {
                    @Override
                    public List<Fragment> apply(@NonNull Integer integer) throws Exception {
                        List<Fragment> list = new ArrayList<Fragment>();
                        for (QuestionRecordData data : questionrecord) {
                            data.setNetParDatas(DBUtil.getInstance()
                                    .queryNetParThId(Integer.parseInt(data.getQuestionid())));
                            list.add(SimulationResultFragment.getInstance(data));
                        }
                        return list;
                    }
                })
                .compose(new SchedulerTransformer<List<Fragment>>())
                .subscribe(new Consumer<List<Fragment>>() {
                    @Override
                    public void accept(@NonNull List<Fragment> fragments) throws Exception {
                        mViewPager.setAdapter(new InfoDetailAdapter(getSupportFragmentManager(), fragments));
                        initTitleStatus(0);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
        addListener();
    }

    @OnClick({R.id.result_answer_detail_btn, R.id.result_collection_btn})
    public void onClick(View v) {
        InfoDetailAdapter infoAdapter = (InfoDetailAdapter) mViewPager.getAdapter();
        if (infoAdapter == null) return;
        List<Fragment> data = infoAdapter.getData();
        if (data == null || data.isEmpty()) return;
        BaseResultFragment currentFrag = (BaseResultFragment) data.get(currentPage);
        switch (v.getId()) {
            case R.id.result_answer_detail_btn:
                //点击查看答案详情
                currentFrag.toggleAnswerDetailContainer();//点击先切换状态，在设置按钮背景
                answerDetailIv.setSelected(currentFrag.isShow());
                break;
            case R.id.result_collection_btn:
                //点击了是否收藏功能
                int questionId = currentFrag.getQuestionId();
                final ContentValues values = new ContentValues();
                if (!GlobalUser.getInstance().isAccountDataInvalid()) {
                    values.put(PracticeTable.USERID, GlobalUser.getInstance().getUserData().getUserid());
                } else {
                    values.put(PracticeTable.USERID, PracticeTable.DEFAULT_UID);
                }
                values.put(PracticeTable.QUESTIONID, questionId);
                final PracticeManager manager = PracticeManager.getInstance();
                if (manager.queryWhetherCollection(questionId)) {//是收藏的
                    manager.dropCollectionData(questionId);
                    collectionIv.setSelected(false);
                } else {//未收藏
                    manager.insertCollectionData(values);
                    collectionIv.setSelected(true);
                }
                break;
            default:
                break;
        }
    }

    private void addListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //选择当前项，情况其他项。
//                if (optionInfoDatas == null || optionInfoDatas.isEmpty()) return;
                resetSelected(position);
                mRecyclerView.smoothScrollToPosition(position);
                currentPage = position;
                initTitleStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTitleStatus(int position) {
        InfoDetailAdapter infoAdapter = (InfoDetailAdapter) mViewPager.getAdapter();
        List<Fragment> data = infoAdapter.getData();
        BaseResultFragment currentFrag = (BaseResultFragment) data.get(position);
        answerDetailIv.setSelected(currentFrag.isShow());
        collectionIv.setSelected(currentFrag.isCollection());
    }

    private void resetSelected(int position) {
        for (int i = 0, size = optionInfoDatas.size(); i < size; i++) {
            OptionInfoData data = optionInfoDatas.get(i);
            if (i == position) {
                data.setSelected(true);
            } else {
                data.setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    }
}
