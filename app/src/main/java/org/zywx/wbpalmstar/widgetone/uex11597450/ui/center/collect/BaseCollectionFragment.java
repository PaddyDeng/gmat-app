package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.collect;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestType;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.RecycleViewLinearDivider;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter.CollectionAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public abstract class BaseCollectionFragment extends BaseRefreshFragment<PracticeRecordData> {

    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());

    @Override
    public BaseRecyclerViewAdapter<PracticeRecordData> getAdapter() {
//        single_contact_item_layout
        return new CollectionAdapter(getActivity(), null, manager);
    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        super.initRecyclerViewItemDecoration(mRecyclerView);
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_height_divider));
    }

    @Override
    public void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    public void setListener(List<PracticeRecordData> data, int position) {
        PracticeRecordData practiceRecordData = data.get(position);
        int questionid = practiceRecordData.getQuestionid();
        List<Integer> list = new ArrayList<>();
        list.add(questionid);
        QuestionBankData questionBankData = new QuestionBankData();
        questionBankData.setQuestionList(list);
        TestActivity.startTestActivity(getActivity(), questionBankData, TestType.SEARCH_QUESTION);
    }


    @Override
    public void asyncRequest() {
        asyncQueryCollectionData().subscribe(new Consumer<List<PracticeRecordData>>() {
            @Override
            public void accept(@NonNull List<PracticeRecordData> datas) throws Exception {
                if (datas.isEmpty()) {
                    updateRecycleView(datas, getString(R.string.str_no_data_tip), InitDataType.TYPE_REFRESH_SUCCESS, true);
                } else {
                    Collections.reverse(datas);
                    updateRecycleView(datas, getString(R.string.str_no_data_tip), InitDataType.TYPE_REFRESH_SUCCESS);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
                updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_REFRESH_FAIL);
            }
        });
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return manager;
    }

    protected Observable<List<PracticeRecordData>> asyncQueryCollectionData() {
        return Observable.just(1).flatMap(new Function<Integer, ObservableSource<List<Integer>>>() {
            @Override
            public ObservableSource<List<Integer>> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<Integer>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Integer>> e) throws Exception {
                        e.onNext(PracticeManager.getInstance().queryCollectionId());//查询收藏题目的id
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<Integer>, ObservableSource<List<PracticeRecordData>>>() {
            @Override
            public ObservableSource<List<PracticeRecordData>> apply(@NonNull final List<Integer> integers) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<PracticeRecordData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<PracticeRecordData>> e) throws Exception {
                        List<PracticeRecordData> list = new ArrayList<>();
                        for (int i : integers) {
                            PracticeRecordData data = new PracticeRecordData();
                            data.setQuestionid(i);
                            QuestionsData questionsData = DBUtil.getInstance().queryQuestionsTitle(i);
                            data.setQuestiontitle(questionsData.getQuestiontitle());
                            data.setSectionId(questionsData.getSectionid());
                            data.setTwoObjectId(questionsData.getTwoobjectid());
                            list.add(data);
                        }
                        e.onNext(list);
                        e.onComplete();
                    }
                });
            }
        }).map(new Function<List<PracticeRecordData>, List<PracticeRecordData>>() {
            @Override
            public List<PracticeRecordData> apply(@NonNull List<PracticeRecordData> datas) throws Exception {
                List<PracticeRecordData> list = new ArrayList<>();
                //获取通过sectionid和twoobjid过滤题库中的题目
                for (PracticeRecordData data : datas) {
                    if (conformCondition(data) && data.getSectionId() == ((CollectionActivity) getActivity()).getSelectionId()) {
                        list.add(data);
                    }
                }
                return list;
            }
        }).flatMap(new Function<List<PracticeRecordData>, ObservableSource<List<PracticeRecordData>>>() {
            @Override
            public ObservableSource<List<PracticeRecordData>> apply(@NonNull final List<PracticeRecordData> datas) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<PracticeRecordData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<PracticeRecordData>> e) throws Exception {
                        //通过sectionid去查询section
                        for (PracticeRecordData data : datas) {
                            data.setSection(DBUtil.getInstance().getSection(data.getSectionId()));
                        }
                        e.onNext(datas);
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<PracticeRecordData>>());
    }

    /**
     * 符合条件返回true
     */
    protected abstract boolean conformCondition(PracticeRecordData data);
}