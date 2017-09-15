package org.zywx.wbpalmstar.widgetone.uex11597450.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.type.InitDataType;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.persenter.BaseListPresenter;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.persenter.BaseListPresenterImpl;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.view.BaseListView;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.RefreshMakeRecordReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.pullrefresh.PullRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseRefreshFragment<T> extends BaseFragment implements BaseListView<T> {
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    protected PullRefreshLayout mPullRefreshLayout;
    @BindView(R.id.base_refresh_title_container)
    protected RelativeLayout titleContainer;
    @BindView(R.id.title_centertxt)
    protected TextView titleTxt;

    protected BaseListPresenter<T> mListPresenter;

    private RefreshMakeRecordReceiver mReceiver = new RefreshMakeRecordReceiver() {
        @Override
        protected void performAction(Intent intent) {
            if (isReceiver())
                asyncRequest();
        }
    };

    /**
     * 默认不接收
     */
    protected boolean isReceiver() {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(mReceiver, mReceiver.getIntentFilter());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_base, container, false);
        ButterKnife.bind(this, view);
        mListPresenter = new BaseListPresenterImpl(this);
        mListPresenter.view(mPullRefreshLayout, mRecyclerView);
        mListPresenter.initView();
        return view;
    }

    @Override
    public boolean canAutoLoadMore() {
        return true;
    }

    protected boolean isAuto() {
        return true;
    }

    @Override
    public boolean autoAsyncData() {
        return isAuto();
    }

    public void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type) {
        if (mListPresenter != null)
            mListPresenter.updateRecycleView(list, msg, type);
    }

    public void updateRecycleView(List<T> list, String msg, @InitDataType.InitDataTypeChecker int type, boolean mustShowNull) {
        if (mListPresenter != null)
            mListPresenter.updateRecycleView(list, msg, type, mustShowNull);
    }

    @Override
    public void setListener(List<T> data, int position) {

    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {

    }

    @Override
    public void asyncRequestBefore() {

    }

    //
    protected List<QuestionBankData> dealData(List<QuestionBankData> datas) {
        for (QuestionBankData quData : datas) {
            //通过stid查询同类别下做过的所有题目
            List<PracticeRecordData> practiceRecordDatas = PracticeManager.getInstance().queryPractice(quData.getStid(), quData.getQuestionsid());
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
        return datas;
    }

}
