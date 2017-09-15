package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.intelligent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter.OrderPracticeAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by fire on 2017/8/1  14:54.
 * 将序号题库的id存成了stid，后面涉及到的序号做题都以这种方式来存的。
 */
public abstract class BaseOrderPracticeFragment extends BaseRefreshFragment<QuestionBankData> {

    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());

    @Override
    public void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    public BaseRecyclerViewAdapter<QuestionBankData> getAdapter() {
        OrderPracticeAdapter adapter = new OrderPracticeAdapter(getActivity(), null, manager);
        adapter.setIndex(getType());
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return manager;
    }

    @Override
    public void asyncRequest() {
        asyncQueryOrderPracticeData().subscribe(new Consumer<List<QuestionBankData>>() {
            @Override
            public void accept(@NonNull List<QuestionBankData> datas) throws Exception {
                updateRecycleView(datas, getString(R.string.str_no_data_tip), InitDataType.TYPE_REFRESH_SUCCESS, true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
                updateRecycleView(null, "", InitDataType.TYPE_REFRESH_FAIL);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        asyncRequest();
    }

    @Override
    protected boolean isAuto() {
        return false;
    }

    private Observable<List<QuestionBankData>> asyncQueryOrderPracticeData() {
        return Observable.just(1)
                .flatMap(new Function<Integer, ObservableSource<List<QuestionBankData>>>() {
                    @Override
                    public ObservableSource<List<QuestionBankData>> apply(@NonNull Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<QuestionBankData>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<QuestionBankData>> e) throws Exception {
                                int selectionId = ((OrderPracticeActivity) getActivity()).getSelectionId();
                                int[] twoObjIds = getTwoObjId();
                                int size = twoObjIds.length;
                                int index = 0;
                                List<QuestionBankData> datas = new ArrayList<>();
                                while (index < size) {
                                    datas.addAll(DBUtil.getInstance().queryXuHaoTiKu(selectionId, twoObjIds[index]));
                                    index++;
                                }
                                e.onNext(datas);
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
                                //查询序号题库对应的question
                                for (QuestionBankData data : datas) {
                                    List<Integer> list = DBUtil.getInstance().querySerialQuestionId(data.getStid());
                                    data.setQuestionList(list);
                                    StringBuffer sb = new StringBuffer();
                                    for (Iterator it = list.iterator(); it.hasNext(); ) {
                                        sb.append(it.next());
                                        if (it.hasNext())
                                            sb.append(",");
                                    }
                                    data.setQuestionsid(sb.toString());
                                }
                                e.onNext(datas);
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
                                for (QuestionBankData quData : datas) {
                                    //通过stid查询同类别下做过的所有题目
                                    List<PracticeRecordData> practiceRecordDatas = PracticeManager.getInstance().queryPractice(quData.getStid());
                                    if (practiceRecordDatas.isEmpty()) {
                                        quData.setHasMakeTest(C.NO_START);
                                        continue;
                                    }
                                    //这里表示这一套题做过，可能做完了。也有可能重新做题了
                                    boolean hasAgain = false;
                                    boolean hasStart = false;
                                    int makeSize = 0;
                                    for (PracticeRecordData data : practiceRecordDatas) {
                                        if (data.getExerciseState() == C.AGAIN_START) {//重新做题
                                            hasStart = true;
                                        } else if (data.getExerciseState() == C.MAKE_TOPIC) {//做过题目
                                            makeSize++;
                                            hasAgain = true;
                                        }
                                    }
                                    quData.setMakeSize(makeSize);
                                    int size = quData.getQuestionList().size();
                                    if (size == practiceRecordDatas.size()) {
                                        if (hasAgain && hasStart) {//既有做过的，也有重置的，正在做
                                            quData.setHasMakeTest(C.START);
                                        } else if (hasAgain && !hasStart) {//全部是做过的
                                            quData.setHasMakeTest(C.END);
                                        } else if (!hasAgain && hasStart) {//全部重置的
                                            quData.setHasMakeTest(C.NO_START);
                                        }
                                    } else {//意味着做题不全全，就有可能重置
                                        if (hasAgain && hasStart) {//既有做过的，也有重置的，正在做
                                            quData.setHasMakeTest(C.START);
                                        } else if (hasAgain && !hasStart) {//全部是做过的,但是还没有做完
                                            quData.setHasMakeTest(C.START);
                                        } else if (!hasAgain && hasStart) {//全部重置的,需要重新做题
                                            quData.setHasMakeTest(C.NO_START);
                                        }
                                    }
                                }
                                e.onNext(datas);
                                e.onComplete();
                            }
                        });
                    }
                }).compose(new SchedulerTransformer<List<QuestionBankData>>());
    }

    protected abstract int[] getTwoObjId();

    protected int getType() {
        return 0;
    }
}
