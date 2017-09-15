package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.difficultymake;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter.DiffDetailAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public abstract class BaseDiffFragment extends BaseRefreshFragment<QuestionBankData> {

    private RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

    @Override
    public void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    public BaseRecyclerViewAdapter getAdapter() {
        return new DiffDetailAdapter(getActivity(), null, manager);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return manager;
    }


    @Override
    public void asyncRequest() {
        Observable
                .just(1)
                .flatMap(new Function<Integer, ObservableSource<List<QuestionBankData>>>() {
                    @Override
                    public ObservableSource<List<QuestionBankData>> apply(@NonNull Integer integer) throws Exception {

                        return Observable
                                .create(new ObservableOnSubscribe<List<QuestionBankData>>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<List<QuestionBankData>> e) throws Exception {
                                        //查询题库
                                        e.onNext(DBUtil.getInstance().queryDiffQuestionBank(getLevelId(), getSectionId()));
                                        e.onComplete();
                                    }
                                });
                    }
                })
                .flatMap(new Function<List<QuestionBankData>, ObservableSource<List<QuestionBankData>>>() {
                    @Override
                    public ObservableSource<List<QuestionBankData>> apply(@NonNull final List<QuestionBankData> datas) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<QuestionBankData>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<QuestionBankData>> e) throws Exception {
//                        for (QuestionBankData quData : datas) {
//                            List<PracticeRecordData> practiceRecordDatas = PracticeManager.getInstance().queryPractice(quData.getStid(),quData.getQuestionsid());
//                            if (practiceRecordDatas.isEmpty()) {//未做题无记录
//                                quData.setHasMakeTest(C.NO_START);
//                                continue;
//                            }
//                            //这里表示这一套题做过，可能做完了。也有可能重新做题了
//                            boolean hasAgain = false;
//                            boolean hasStart = false;
//                            int makeSize = 0;
//                            for (PracticeRecordData data : practiceRecordDatas) {
//                                if (data.getExerciseState() == C.AGAIN_START) {//重新做题
//                                    hasStart = true;
//                                } else if (data.getExerciseState() == C.MAKE_TOPIC) {//做过题目
//                                    makeSize++;
//                                    hasAgain = true;
//                                }
//                            }
//                            quData.setMakeSize(makeSize);
//                            int size = quData.getQuestionList().size();
//                            if (size == practiceRecordDatas.size()) {
//                                if (hasAgain && hasStart) {//既有做过的，也有重置的，正在做
//                                    quData.setHasMakeTest(C.START);
//                                } else if (hasAgain && !hasStart) {//全部是做过的
//                                    quData.setHasMakeTest(C.END);
//                                } else if (!hasAgain && hasStart) {//全部重置的
//                                    quData.setHasMakeTest(C.NO_START);
//                                }
//                            } else {//意味着做题不全全，就有可能重置
//                                if (hasAgain && hasStart) {//既有做过的，也有重置的，正在做
//                                    quData.setHasMakeTest(C.START);
//                                } else if (hasAgain && !hasStart) {//全部是做过的,但是还没有做完
//                                    quData.setHasMakeTest(C.START);
//                                } else if (!hasAgain && hasStart) {//全部重置的,需要重新做题
//                                    quData.setHasMakeTest(C.NO_START);
//                                }
//                            }
//                        }
                                e.onNext(dealData(datas));
                                e.onComplete();
                            }
                        });
                    }
                }).compose(new SchedulerTransformer<List<QuestionBankData>>()).subscribe(new Consumer<List<QuestionBankData>>() {
            @Override
            public void accept(@NonNull List<QuestionBankData> datas) throws Exception {
                updateRecycleView(datas, "", InitDataType.TYPE_REFRESH_SUCCESS);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                updateRecycleView(null, "", InitDataType.TYPE_REFRESH_FAIL);
            }
        });
    }

    protected abstract int getSectionId();

    private int getLevelId() {
        return ((DiffDetailActivity) getActivity()).getLevelId();
    }
}
