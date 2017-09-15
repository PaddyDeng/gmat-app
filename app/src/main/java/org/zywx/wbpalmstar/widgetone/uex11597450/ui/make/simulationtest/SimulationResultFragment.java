package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.simulation.QuestionRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.BaseResultFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.NetParData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CustomerWebView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.GeneralView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.NetParView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.OptionView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.overscroll.FastAndOverScrollScrollView;

import java.util.List;

import butterknife.BindView;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.fromHtml;

public class SimulationResultFragment extends BaseResultFragment {
    private static final String KEY_RESULT = "key_result";
    private QuestionRecordData data;
    private LayoutInflater inflater;
    @BindView(R.id.frag_detail_simulation_test_nested_scroll_view)
    FastAndOverScrollScrollView mNestedScrollView;
    @BindView(R.id.frag_detail_gmat_test_webview)
    GeneralView mWebView;
    @BindView(R.id.frag_detail_gmat_option_contaienr)
    LinearLayout optionContainer;
    @BindView(R.id.answer_correct_des)
    TextView answerDes;
    @BindView(R.id.corrent_des_container)
    LinearLayout answerDetailContainer;
    @BindView(R.id.gmat_result_title_webview)
    CustomerWebView mTitleWebView;
    private boolean isShow;
    private int titleWebViewHeight;

    @Override
    public boolean isShow() {
        return isShow;
    }

    @Override
    public void toggleAnswerDetailContainer() {
        if (answerDetailContainer.getVisibility() == View.GONE) {
            isShow = true;
            Utils.setVisible(answerDetailContainer);
            mNestedScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mNestedScrollView.scrollTo(0, mNestedScrollView.getHeight() - answerDetailContainer.getMeasuredHeight() / 3);
                }
            });
        } else if (answerDetailContainer.getVisibility() == View.VISIBLE) {
            Utils.setGone(answerDetailContainer);
            isShow = false;
        }
    }

    @Override
    public int getQuestionId() {
        return Integer.parseInt(data == null ? "0" : data.getQuestionid());
    }

    public static SimulationResultFragment getInstance(QuestionRecordData data) {
        SimulationResultFragment frag = new SimulationResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_RESULT, data);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    protected void getArgs() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            data = arguments.getParcelable(KEY_RESULT);
        }
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_result_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inflater = LayoutInflater.from(getContext());

        mTitleWebView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mTitleWebView, this);
                titleWebViewHeight = mTitleWebView.getMeasuredHeight();
            }
        });

        mTitleWebView.setOnCustomeClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //全部显示。
                int height = mTitleWebView.getMeasuredHeight();

                int contentHeight = (int) (mTitleWebView.getContentHeight() * mTitleWebView.getScale());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleWebView.getLayoutParams();
                if (height == contentHeight) {
                    params.height = titleWebViewHeight;
                    mTitleWebView.setIntercept(true);
                } else {
                    params.height = contentHeight;
                    mTitleWebView.setIntercept(false);
                }
                mTitleWebView.requestLayout();
                mTitleWebView.pageUp(true);
            }
        });

        if (data != null) {
            refreshUi();
        }
    }


    @Override
    protected void refreshUi() {

        mWebView.setSimulation(data.getQuestion());

        if (!TextUtils.isEmpty(data.getQuestionarticle())) {
            Utils.setVisible(mTitleWebView);
            mTitleWebView.setSimulationText(data.getQuestionarticle());
        } else {
            Utils.setGone(mTitleWebView);
        }

        String useranswer = data.getUseranswer();
        String questionanswer = data.getQuestionanswer();
        String duration = data.getDuration();
        duration = Utils.format(Integer.parseInt(duration));
//        answerDes.setText(getString(R.string.str_record_correct_answer, questionanswer));
        answerDes.setText(getString(R.string.str_answer_use_time, duration));
        boolean isCorrert = false;
        //
        if (TextUtils.equals(useranswer.trim(), questionanswer.trim())) {
            isCorrert = true;
        }

        List<QuestionRecordData.QslctarrBean> qslctarr = data.getQslctarr();
        if (qslctarr == null) return;
        setOptionContent(useranswer, questionanswer, isCorrert, qslctarr);

        answerDetailContainer.removeAllViews();
        List<NetParData> netParDataList = data.getNetParDatas();
        if (netParDataList != null && !netParDataList.isEmpty()) {
            for (NetParData netP : netParDataList) {
                NetParView netParView = new NetParView(getActivity());
                netParView.setNetParContent(netP.getP_time(), netP.getUserid(), netP.getP_content());
                answerDetailContainer.addView(netParView);
            }
        }

    }

    private void setOptionContent(String useranswer, String questionanswer, boolean isCorrert, List<QuestionRecordData.QslctarrBean> qslctarr) {
        optionContainer.removeAllViews();
        for (int i = 0, size = qslctarr.size(); i < size; i++) {
            QuestionRecordData.QslctarrBean qslctarrBean = qslctarr.get(i);

            final OptionView optionView = new OptionView(getActivity());
            optionView.setTag(qslctarrBean.getName());
            optionView.setSimulationText(qslctarrBean.getName(), qslctarrBean.getSelect());
            if (isCorrert) {
                if (TextUtils.equals(useranswer.trim(), qslctarrBean.getName())) {
//                    optionView.setSelectedBg(true);
                    optionView.setDetailBg();
                } else {
                    optionView.setSelectedBg(false);
                }
            } else {
                if (TextUtils.equals(useranswer.trim(), qslctarrBean.getName())) {
                    //错误答案
                    optionView.setErrorBg();
                } else if (TextUtils.equals(questionanswer.trim(), qslctarrBean.getName())) {
                    //正确答案
//                    optionView.setSelectedBg(true);
                    optionView.setDetailBg();
                } else {
                    //其余选项
                    optionView.setSelectedBg(false);
                }
            }
            optionContainer.addView(optionView);
        }
    }

    private String getHtml(String content) {

        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<meta charset=\"UTF-8\">");
        sb.append("<title>Title</title>");
        sb.append("<style>");
        sb.append("body{");
        sb.append("word-wrap: break-word;");
        sb.append("font-family: Arial;");
        sb.append("}");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append(content);
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

}
