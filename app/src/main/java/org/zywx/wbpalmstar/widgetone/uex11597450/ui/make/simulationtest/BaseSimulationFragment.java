package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.BaseRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.GmatTestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter.SimulationListAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseRefreshFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.SimulationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.LoginSuccessReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.SimulationRefreshReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public abstract class BaseSimulationFragment extends BaseRefreshFragment<SimulationData> {

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(getActivity());
    private int type = 1;//默认语文套题
    protected Observable<Integer> refreshObs;
    protected SimulationListAdapter adapter;

    private LoginSuccessReceiver mReceiver = new LoginSuccessReceiver() {
        @Override
        protected void performAction(Intent intent) {
            if (couldRefresh()) {
                asyncRequest();
            }
        }
    };

    protected abstract boolean couldRefresh();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mReceiver.getIntentFilter());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    @Override
    public void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    public BaseRecyclerViewAdapter<SimulationData> getAdapter() {
        adapter = new SimulationListAdapter(getActivity(), null, mManager);
        return adapter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        asyncRequest();
        refreshObs = RxBus.get().register(C.SIMULATION_LIST_REFRESH, Integer.class);
        refreshObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer == type) {
                    asyncRequest();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshObs != null) {
            RxBus.get().unregister(C.SIMULATION_LIST_REFRESH, refreshObs);
        }
    }

    @Override
    protected boolean isAuto() {
        return false;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    public void setListener(List<SimulationData> data, int position) {
        SimulationData sim = data.get(position);
        sim.setType(type);
        if (TextUtils.equals(sim.getMarkquestion(), C.MARK_QUESTION)) {
            //跳转到结果页
            SimulationResultActivity.startSimulationResult(getActivity(), sim);
        } else if (TextUtils.equals(sim.getMarkquestion(), C.GOON_MARK_QUESTION)) {
            GmatTestActivity.startSimulationStart(getActivity(), sim);
        } else {
            SimulationStartActivity.startSimulationStart(getActivity(), sim);
        }
    }

    @Override
    protected void getArgs() {
        SimulationTestListActivity simuListAct = (SimulationTestListActivity) getActivity();
        type = simuListAct.getType();
    }

    @Override
    public void asyncRequest() {
        addToCompositeDis(HttpUtil.getTestList(String.valueOf(type), getTypeClass())
                .subscribe(new Consumer<ResultBean<List<SimulationData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<SimulationData>> bean) throws Exception {
                        adapter.setType(type);
                        List<SimulationData> data = bean.getData();
                        if (getHttpResSuc(bean.getCode()) && data != null && !data.isEmpty()) {
                            updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                        } else {
                            updateRecycleView(null, bean.getMessage(), InitDataType.TYPE_REFRESH_FAIL);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
    }

    protected abstract String getTypeClass();
}
