package org.zywx.wbpalmstar.widgetone.uex11597450.ui.konw;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.ControlTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


public class KnowFragment extends BaseFragment/* implements SwipeRefreshLayout.OnRefreshListener*/ {
    private List<KnowData> knowDatas;

//    @BindView(R.id.know_swipe)
//    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.logic_cr_control_tv)
    ControlTextView logicCrTv;
    @BindView(R.id.grammar_sc_control_tv)
    ControlTextView grammarScTv;
    @BindView(R.id.read_rc_control_tv)
    ControlTextView readRcTv;
    @BindView(R.id.math_q_control_tv)
    ControlTextView mathQTv;
    @BindView(R.id.frag_know_container)
    RelativeLayout mContainer;
    @BindView(R.id.know_content_container)
    LinearLayout mContentContainer;
    @BindView(R.id.logic_des)
    TextView logicDesTxt;
    @BindView(R.id.grammar_des)
    TextView grammarDesTxt;
    @BindView(R.id.read_des)
    TextView readDesTxt;
    @BindView(R.id.math_des)
    TextView mathDesTxt;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_know, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setInteceptEvent(false);
        mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mContainer, this);
                int singleHeight = mContainer.getMeasuredHeight() / 4;
                int count = mContentContainer.getChildCount();
                for (int i = 0; i < count; i++) {
                    RelativeLayout child = (RelativeLayout) mContentContainer.getChildAt(i);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                    params.height = singleHeight;
                    child.requestLayout();
                }
            }
        });
//        控制字体大小
//        int fontSize = SharedPref.getFontSize(getActivity());
//        logicCrTv.setFontSize(fontSize);
//        grammarScTv.setFontSize(fontSize);
//        readRcTv.setFontSize(fontSize);
//        mathQTv.setFontSize(fontSize);
        initHttp();
    }

    private void initHttp() {
        addToCompositeDis(HttpUtil.getKnowBase().subscribe(new Consumer<ResultBean<List<KnowData>>>() {
            @Override
            public void accept(@NonNull ResultBean<List<KnowData>> bean) throws Exception {
                if (!getHttpResSuc(bean.getCode())) {
                    initHttp();
                    return;
                }
                if (!bean.getData().isEmpty()) {
                    knowDatas = bean.getData();
                    refreshUi();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                initHttp();
            }
        }));
    }

    @Override
    protected void refreshUi() {
        super.refreshUi();
        if (knowDatas == null || knowDatas.isEmpty()) return;
        for (int i = 0; i < knowDatas.size(); i++) {
            KnowData data = knowDatas.get(i);
            if (i == 0) {
                setText(logicDesTxt, data);
            } else if (i == 1) {
                setText(grammarDesTxt, data);
            } else if (i == 2) {
                setText(readDesTxt, data);
            } else if (i == 3) {
                setText(mathDesTxt, data);
            }
        }

    }

    private void setText(TextView tv, KnowData data) {
        tv.setText(HtmlUtil.fromHtml(getString(R.string.str_know_people_num_des, data.getSum(), data.getViews())));
    }

    @OnClick({R.id.know_login_container, R.id.know_math_container, R.id.know_grammer_container, R.id.know_read_container})
    public void onClick(View v) {
        if (knowDatas == null || knowDatas.isEmpty()) {
            return;
        }
        switch (v.getId()) {
            case R.id.know_login_container:
                KnowTypeActivity.startKnow(getActivity(), knowDatas.get(0), 0);
                break;
            case R.id.know_grammer_container:
                if (knowDatas.size() >= 2)
                    KnowTypeActivity.startKnow(getActivity(), knowDatas.get(1), 1);
                break;
            case R.id.know_read_container:
                if (knowDatas.size() >= 3)
                    KnowTypeActivity.startKnow(getActivity(), knowDatas.get(2), 2);
                break;
            case R.id.know_math_container:
                if (knowDatas.size() >= 4)
                    KnowTypeActivity.startKnow(getActivity(), knowDatas.get(3), 3);
                break;
            default:
                break;
        }
    }

}
