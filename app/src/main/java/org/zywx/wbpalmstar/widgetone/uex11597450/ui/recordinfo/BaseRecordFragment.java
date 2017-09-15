package org.zywx.wbpalmstar.widgetone.uex11597450.ui.recordinfo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.TopicRecordDetailActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter.RecordAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public abstract class BaseRecordFragment extends BaseRefreshFragment<PracticeRecordData> /*implements SwipeRefreshLayout.OnRefreshListener*/ {

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(getActivity());

    @Override
    public void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    public BaseRecyclerViewAdapter<PracticeRecordData> getAdapter() {
        return new RecordAdapter(getActivity(), null, mManager);
    }

    @Override
    public void setListener(List<PracticeRecordData> data, int position) {
        super.setListener(data, position);
        PracticeRecordData practiceRecordData = data.get(position);
        TopicRecordDetailActivity.startTopicRecordDetailAct(getActivity(), practiceRecordData.getQuestionid(),
                practiceRecordData.getStid(), practiceRecordData.getXuhaotikuId());
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    public void asyncRequest() {
        Observable.just(1).flatMap(new Function<Integer, ObservableSource<List<PracticeRecordData>>>() {
            @Override
            public ObservableSource<List<PracticeRecordData>> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<PracticeRecordData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<PracticeRecordData>> e) throws Exception {
                        List<PracticeRecordData> dataList = new ArrayList<PracticeRecordData>();
                        if (getType() == C.ERROR_TOPIC) {
                            dataList = PracticeManager.getInstance().getAllErrorTopic();
                        } else if (getType() == C.ALL_TOPIC) {
                            dataList = PracticeManager.getInstance().getAllMakeTopic();
                        }
                        e.onNext(dataList);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<PracticeRecordData>, ObservableSource<List<PracticeRecordData>>>() {
            @Override
            public ObservableSource<List<PracticeRecordData>> apply(@NonNull final List<PracticeRecordData> datas) throws Exception {
                //通过questionid去查询题目questiontitle，界面显示需要
                return Observable.create(new ObservableOnSubscribe<List<PracticeRecordData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<PracticeRecordData>> e) throws Exception {
                        for (PracticeRecordData data : datas) {
                            QuestionsData questionsData = DBUtil.getInstance().queryQuestionsTitle(data.getQuestionid());
                            data.setQuestiontitle(questionsData.getQuestiontitle());
                            data.setSectionId(questionsData.getSectionid());
                            data.setQuestionanswer(questionsData.getQuestionanswer());
                        }
                        e.onNext(datas);
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<PracticeRecordData>, ObservableSource<List<PracticeRecordData>>>() {
            @Override
            public ObservableSource<List<PracticeRecordData>> apply(@NonNull final List<PracticeRecordData> datas) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<PracticeRecordData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<PracticeRecordData>> e) throws Exception {
                        //通过sectionid过滤掉符合条件的
                        List<PracticeRecordData> list = new ArrayList<>();
                        for (PracticeRecordData data : datas) {
                            if (data.getSectionId() == getSectionId()) {
                                list.add(data);
                            }
                        }
                        e.onNext(list);
                        e.onComplete();
                    }
                });
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
        }).compose(new SchedulerTransformer<List<PracticeRecordData>>())
                .subscribe(new Consumer<List<PracticeRecordData>>() {
                    @Override
                    public void accept(@NonNull List<PracticeRecordData> datas) throws Exception {
                        if (datas.isEmpty()) {
                            updateRecycleView(null, getString(R.string.str_no_data_tip), InitDataType.TYPE_REFRESH_FAIL);
                        } else {
//                            Collections.reverse(datas);
                            Collections.sort(datas, new Comparator<PracticeRecordData>() {
                                @Override
                                public int compare(PracticeRecordData o1, PracticeRecordData o2) {
                                    return o2.getStartmake() > o1.getStartmake() ? 1 : -1;
                                }
                            });
                            updateRecycleView(datas, "", InitDataType.TYPE_REFRESH_SUCCESS);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    protected abstract int getSectionId();

    protected abstract int getType();
}
