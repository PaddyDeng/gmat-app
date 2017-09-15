package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.adapter.SimuRecordAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.GmatTestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.SimuRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.SimulationResultActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.SimulationStartActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public abstract class BaseSimuRecordFragment extends BaseRefreshFragment<SimuRecordData> {

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(getActivity());
    private String pageSize = "15";
    private Observable<Integer> refreshObs;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshObs = RxBus.get().register(C.SIMULATION_RECORD, Integer.class);
        refreshObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer == getType()) {
                    asyncRequest();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshObs != null)
            RxBus.get().unregister(C.SIMULATION_RECORD, refreshObs);
    }

    @Override
    public void asyncLoadMore() {
        loadData(false);
    }

    @Override
    public BaseRecyclerViewAdapter<SimuRecordData> getAdapter() {
        return new SimuRecordAdapter(getActivity(), null, mManager);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    public void asyncRequest() {
        loadData(true);
    }

    private void loadData(final boolean isRefresh) {
        String type = C.VERBAL;
        switch (getType()) {
            case C.MATH:
                type = C.QUANT;
                break;
            case C.ALL:
                type = C.TYPE_ALL;
                break;
            case C.LANGUAGE:
            default:
                break;
        }
        addToCompositeDis(HttpUtil.getSimuRecord(type, getPageNumber(isRefresh), pageSize)
                .subscribe(new Consumer<ResultBean<List<SimuRecordData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<SimuRecordData>> bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {//请求数据成功
                            List<SimuRecordData> data = bean.getData();
                            if (isRefresh) {
                                if (data != null && !data.isEmpty()) {
                                    updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                                } else {
                                    updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_REFRESH_FAIL);
                                }
                            } else {
                                if (data != null && !data.isEmpty()) {
                                    updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                                } else {
                                    updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_LOAD_MORE_SUCCESS);
                                }
                            }
                        } else {//请求数据失败
                            if (isRefresh) {
                                updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_REFRESH_FAIL);
                            } else {
                                updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_LOAD_MORE_FAIL);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (isRefresh) {
                            updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_REFRESH_FAIL);
                        } else {
                            updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
                        }
                    }
                }));
    }

    @Override
    public void setListener(List<SimuRecordData> data, int position) {
        SimuRecordData simuRecordData = data.get(position);
        SimulationData sim = new SimulationData();
        sim.setType(getType());
        sim.setId(simuRecordData.getMkid());
        sim.setMkscoreid(simuRecordData.getMkscoreid());
        sim.setName(simuRecordData.getName());
        if (TextUtils.equals(simuRecordData.getMarkquestion(), C.MARK_QUESTION)) {
            //跳转到结果页
            SimulationResultActivity.startSimulationResult(getActivity(), sim);
        } else if (TextUtils.equals(simuRecordData.getMarkquestion(), C.GOON_MARK_QUESTION)) {
            GmatTestActivity.startSimulationStart(getActivity(), sim);
        } else {
            SimulationStartActivity.startSimulationStart(getActivity(), sim);
        }
    }

    protected abstract int getType();

    protected abstract String getPageNumber(boolean isRefresh);
}
