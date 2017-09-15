package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make;

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

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.NetParData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionsData;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SelectedUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CustomerWebView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.GeneralView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.NetParView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.OptionView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.overscroll.FastAndOverScrollScrollView;

import java.util.List;

import butterknife.BindView;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.fromHtml;

public class LocalResultFragment extends BaseResultFragment {

    private static final String KEY_RESULT = "key_local_result";
    private QuestionsData data;
    private LayoutInflater inflater;
    @BindView(R.id.frag_detail_local_test_nested_scroll_view)
    FastAndOverScrollScrollView mNestedScrollView;
    @BindView(R.id.frag_detail_gmat_test_webview)
    GeneralView mWebView;
    @BindView(R.id.frag_detail_gmat_option_contaienr)
    LinearLayout optionContainer;
    @BindView(R.id.answer_correct_des)
    TextView answerDes;
    @BindView(R.id.custom_result_web_view)
    CustomerWebView mTitleWebView;
    @BindView(R.id.corrent_des_container)
    LinearLayout answerDetailContainer;
    private int titleWebViewHeight;
    private boolean isShow;

    private String[] options = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};

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
        return data == null ? 0 : data.getQuestionid();
    }

    public static LocalResultFragment getInstance(QuestionsData data) {
        LocalResultFragment frag = new LocalResultFragment();
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
        return inflater.inflate(R.layout.local_result_layout, container, false);
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
        if (TextUtils.isEmpty(data.getQuestionarticle())) {
            Utils.setGone(mTitleWebView);
        } else {
            Utils.setVisible(mTitleWebView);
        }
        mTitleWebView.setText(data.getQuestionarticle());
        mWebView.setText(data.getQuestion());
        String useranswer = data.getYouAnswer();
        String questionanswer = data.getQuestionanswer();

//        answerDes.setText(getString(R.string.str_record_correct_answer, questionanswer));
        answerDes.setText(getString(R.string.str_answer_use_time, data.getUserTime()));

        optionContainer.removeAllViews();

        String questionselect = data.getQuestionselect();
        int selectNumber = data.getQuestionselectnumber();

        optionContainer.removeAllViews();
        setOptionContent(questionselect, selectNumber, useranswer, questionanswer);

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

    private void setOptionContent(String questionselect, int number, String useranswer, String questionanswer) {
        List<String> list = SelectedUtils.selectedArray(fromHtml(questionselect).toString(), number);
        optionContainer.removeAllViews();
        for (int i = 0; i < number; i++) {
            final OptionView optionView = new OptionView(getActivity());
            optionView.setSimulationText(options[i], list.get(i).trim());
            if (TextUtils.equals(useranswer.trim(), questionanswer.trim())) {//选择正确
                if (TextUtils.equals(options[i], useranswer)) {
//                    optionView.setSelectedBg(true);
                    optionView.setDetailBg();
                } else {
                    optionView.setSelectedBg(false);
                }
            } else {
                //选择错误
                if (TextUtils.equals(options[i], useranswer)) {
                    optionView.setErrorBg();
                } else if (TextUtils.equals(options[i], questionanswer)) {
//                    optionView.setSelectedBg(true);
                    optionView.setDetailBg();
                } else {
                    optionView.setSelectedBg(false);
                }
            }
            optionContainer.addView(optionView);
        }
    }
}
