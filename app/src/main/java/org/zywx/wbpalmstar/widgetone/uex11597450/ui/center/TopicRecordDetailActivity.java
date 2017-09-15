package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeTable;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.BaseResultFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.LocalResultFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

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

public class TopicRecordDetailActivity extends BaseActivity {


    private final static int CONTAINER = R.id.topic_detail_container;

    public static void startTopicRecordDetailAct(Context c, int questionId, int stid, int serialId) {
        Intent intent = new Intent(c, TopicRecordDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, questionId);
        intent.putExtra(Intent.EXTRA_INDEX, stid);
        intent.putExtra(Intent.EXTRA_TITLE, serialId);
        c.startActivity(intent);
    }

    private int questionId;
    private int stid;
    private int serialId;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        questionId = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
        stid = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
        serialId = intent.getIntExtra(Intent.EXTRA_TITLE, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_record);
    }

    @Override
    protected void asyncUiInfo() {
        Observable.just(questionId).flatMap(new Function<Integer, ObservableSource<PracticeRecordData>>() {
            @Override
            public ObservableSource<PracticeRecordData> apply(@NonNull final Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<PracticeRecordData>() {
                    @Override
                    public void subscribe(ObservableEmitter<PracticeRecordData> e) throws Exception {
                        List<PracticeRecordData> practiceRecordDatas = PracticeManager.getInstance()
                                .queryMakeTopicDataThroughtId(String.valueOf(integer),
                                        stid == 0 ? String.valueOf(serialId) : String.valueOf(stid), serialId != 0);
                        e.onNext(practiceRecordDatas.get(0));
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<PracticeRecordData, ObservableSource<QuestionsData>>() {
            @Override
            public ObservableSource<QuestionsData> apply(@NonNull final PracticeRecordData datas) throws Exception {
                return Observable.create(new ObservableOnSubscribe<QuestionsData>() {
                    @Override
                    public void subscribe(ObservableEmitter<QuestionsData> e) throws Exception {
                        QuestionsData questionsData = DBUtil.getInstance().queryQuestionsThroughtId(String.valueOf(datas.getQuestionid()));
                        questionsData.setYouAnswer(datas.getYouanswer());
                        questionsData.setQuestionid(datas.getQuestionid());
                        questionsData.setUserTime(Utils.format((int) (datas.getUsetime() / 1000)));
                        questionsData.setYouChooseResult(datas.getTestResult() == C.CORRECT ? true : false);
                        e.onNext(questionsData);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<QuestionsData, ObservableSource<QuestionsData>>() {
            @Override
            public ObservableSource<QuestionsData> apply(@NonNull final QuestionsData datas) throws Exception {
                return Observable.create(new ObservableOnSubscribe<QuestionsData>() {
                    @Override
                    public void subscribe(ObservableEmitter<QuestionsData> e) throws Exception {
                        //查询评价
                        datas.setNetParDatas(DBUtil.getInstance()
                                .queryNetParThId(datas.getQuestionid()));
                        e.onNext(datas);
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<QuestionsData>())
                .subscribe(new Consumer<QuestionsData>() {
                    @Override
                    public void accept(@NonNull QuestionsData datas) throws Exception {
                        mResultFragment = LocalResultFragment.getInstance(datas);
                        replaceFragment(getSupportFragmentManager(), mResultFragment);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }

    BaseResultFragment mResultFragment;
    @BindView(R.id.test_answer_detail_btn)
    ImageView answerDetailIv;
    @BindView(R.id.test_collection_btn)
    ImageView collectionIv;

    @OnClick({R.id.test_answer_detail_btn, R.id.test_collection_btn})
    public void onClick(View v) {
        if (mResultFragment == null) return;
        switch (v.getId()) {
            case R.id.test_collection_btn:
                int questionId = mResultFragment.getQuestionId();
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
            case R.id.test_answer_detail_btn:
                //点击查看答案详情
                mResultFragment.toggleAnswerDetailContainer();//点击先切换状态，在设置按钮背景
                answerDetailIv.setSelected(mResultFragment.isShow());
                break;
            default:
                break;
        }
    }


    private void replaceFragment(FragmentManager fm, Fragment fragment) {
        String tag = "topic_record_detail";
        FragmentTransaction transaction = fm.beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            transaction.replace(CONTAINER, fragment, tag);
            transaction.addToBackStack(tag);
        } else {
            transaction.replace(CONTAINER, getSupportFragmentManager().findFragmentByTag(tag), tag);
        }
        transaction.commitAllowingStateLoss();

    }

}
