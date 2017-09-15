package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.knowpoint;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RandomData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.KnowsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter.KnowPracticeAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public abstract class BaseKnowPrFragment extends BaseRefreshFragment<KnowsData> {

    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());

    @Override
    public void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    public BaseRecyclerViewAdapter<KnowsData> getAdapter() {
        return new KnowPracticeAdapter(getActivity(), null, manager);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return manager;
    }

    @Override
    public void setListener(List<KnowsData> data, int position) {
        super.setListener(data, position);
        KnowsData knowsData = data.get(position);
        KnowPointListActivity.startKnowListAct(getActivity(), knowsData.getKnowsid(), knowsData.getKnows());
    }

    @Override
    public void asyncRequest() {
        Observable.just(1).flatMap(new Function<Integer, ObservableSource<List<KnowsData>>>() {
            @Override
            public ObservableSource<List<KnowsData>> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<KnowsData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<KnowsData>> e) throws Exception {
                        //查询满足条件的
                        e.onNext(DBUtil.getInstance().queryKnows(getKnowssectionid()));
                        e.onComplete();
                    }
                });
            }
        }).flatMap(new Function<List<KnowsData>, ObservableSource<List<KnowsData>>>() {
            @Override
            public ObservableSource<List<KnowsData>> apply(@NonNull final List<KnowsData> datas) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<KnowsData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<KnowsData>> e) throws Exception {
                        for (KnowsData data : datas) {//查询每个分类下的大小
                            int size = 0;
                            List<QuestionBankData> questionBankDatas = DBUtil.getInstance().queryKnowPoint(data.getKnowsid());
                            for (QuestionBankData quData : questionBankDatas) {
                                size += quData.getQuestionList().size();
                            }
                            data.setQuestionSize(size);
                        }
                        e.onNext(datas);
                        e.onComplete();
                    }
                });
            }
        }).map(new Function<List<KnowsData>, List<KnowsData>>() {
            @Override
            public List<KnowsData> apply(@NonNull List<KnowsData> datas) throws Exception {
                List<KnowsData> lists = new ArrayList<>();
                for (KnowsData data : datas) {
                    if (data.getQuestionSize() > 0) {
                        lists.add(data);
                    }
                }
                return lists;
            }
        }).flatMap(new Function<List<KnowsData>, ObservableSource<List<KnowsData>>>() {
            @Override
            public ObservableSource<List<KnowsData>> apply(@NonNull final List<KnowsData> datas) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<KnowsData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<KnowsData>> e) throws Exception {
                        for (KnowsData data : datas) {
                            RandomData randomData = PracticeManager.getInstance().queryNumber(C.KNOW_POINT_ + data.getKnows(), C.KNOW_POINT_TYPE);
                            data.setMakeQueSize(randomData.getTimes());
                        }
                        e.onNext(datas);
                        e.onComplete();
                    }
                });
            }
        })
                .compose(new SchedulerTransformer<List<KnowsData>>()).
                subscribe(new Consumer<List<KnowsData>>() {
                    @Override
                    public void accept(@NonNull List<KnowsData> datas) throws Exception {
                        updateRecycleView(datas, "", InitDataType.TYPE_REFRESH_SUCCESS);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
//        Observable.just(1).flatMap(new Function<Integer, ObservableSource<List<KnowsData>>>() {
//            @Override
//            public ObservableSource<List<KnowsData>> apply(@NonNull Integer integer) throws Exception {
//                return Observable.create(new ObservableOnSubscribe<List<KnowsData>>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<List<KnowsData>> e) throws Exception {
//                        //查询所有的knows对应的类型
//                        e.onNext(DBUtil.getInstance().queryKnows());
//                        e.onComplete();
//                    }
//                });
//            }
//        }).map(new Function<List<KnowsData>, List<KnowsData>>() {
//            @Override
//            public List<KnowsData> apply(@NonNull List<KnowsData> datas) throws Exception {
//                List<KnowsData> list = new ArrayList<>();
//                for (KnowsData data : datas) {
//                    if (conformCondition(data)) {//过滤sectionid等于如：sc，ps等五种类型
//                        list.add(data);
//                    }
//                }
//                return list;
//            }
//        }).flatMap(new Function<List<KnowsData>, ObservableSource<List<KnowsData>>>() {
//            @Override
//            public ObservableSource<List<KnowsData>> apply(@NonNull final List<KnowsData> datas) throws Exception {
//                return Observable.create(new ObservableOnSubscribe<List<KnowsData>>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<List<KnowsData>> e) throws Exception {
//                        List<QuestionBankData> questionBankDatas = DBUtil.getInstance().queryQuestionBank();
//                        for (KnowsData data : datas) {//题库的knowsid等于kows表里面的knowsid才能用
//                            data.setQuestionSize(0);
//                            int knowsid = data.getKnowsid();
//                            for (QuestionBankData bankData : questionBankDatas) {
//                                if (bankData.getKnowsid() == knowsid) {
//                                    data.setQuestionSize(data.getQuestionSize() + bankData.getQuestionList().size());
//                                }
//                            }
//                        }
//                        e.onNext(datas);
//                        e.onComplete();
//                    }
//                });
//            }
//        }).map(new Function<List<KnowsData>, List<KnowsData>>() {
//            @Override
//            public List<KnowsData> apply(@NonNull List<KnowsData> datas) throws Exception {
//                List<KnowsData> lists = new ArrayList<>();
//                for (KnowsData data : datas) {
//                    if (data.getQuestionSize() > 0) {
//                        lists.add(data);
//                    }
//                }
//                return lists;
//            }
//        }).flatMap(new Function<List<KnowsData>, ObservableSource<List<KnowsData>>>() {
//            @Override
//            public ObservableSource<List<KnowsData>> apply(@NonNull final List<KnowsData> datas) throws Exception {
//                return Observable.create(new ObservableOnSubscribe<List<KnowsData>>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<List<KnowsData>> e) throws Exception {
//                        for (KnowsData data : datas) {
//                            RandomData randomData = PracticeManager.getInstance().queryNumber(C.KNOW_POINT_ + data.getKnows(), C.KNOW_POINT_TYPE);
//                            data.setMakeQueSize(randomData.getTimes());
//                        }
//                        e.onNext(datas);
//                        e.onComplete();
//                    }
//                });
//            }
//        })
//                .compose(new SchedulerTransformer<List<KnowsData>>()).
//                subscribe(new Consumer<List<KnowsData>>() {
//                    @Override
//                    public void accept(@NonNull List<KnowsData> datas) throws Exception {
//                        updateRecycleView(datas, "", InitDataType.TYPE_REFRESH_SUCCESS);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                });
    }

    /**
     * 符合条件返回true
     */
    protected abstract boolean conformCondition(KnowsData data);

    protected abstract int getKnowssectionid();
}
