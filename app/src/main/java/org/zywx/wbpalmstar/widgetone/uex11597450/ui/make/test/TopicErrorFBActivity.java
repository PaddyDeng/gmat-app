package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.FeedItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TopicErrorFBActivity extends BaseActivity {

    public static void startTopicError(Context c, int questionId) {
        Intent intent = new Intent(c, TopicErrorFBActivity.class);
        intent.putExtra("QUESTION_ID", questionId);
        c.startActivity(intent);
    }

    @BindView(R.id.topic_error_des)
    EditText et;
    @BindView(R.id.another_error_fiv)
    FeedItemView anotherErrFiv;
    @BindView(R.id.topic_error_fiv)
    FeedItemView topicErrFiv;
    @BindView(R.id.format_error_fiv)
    FeedItemView formatErrFiv;
    @BindView(R.id.answer_error_fiv)
    FeedItemView answerErrFiv;
    List<FeedItemView> errorLists;
    private int questionId;
    private Observable<String> obs;
    private String type;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        questionId = intent.getIntExtra("QUESTION_ID", 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_error_fb);
        if (errorLists == null) {
            errorLists = new ArrayList<>();
            errorLists.add(answerErrFiv);
            errorLists.add(formatErrFiv);
            errorLists.add(topicErrFiv);
            errorLists.add(anotherErrFiv);
        }
        obs = RxBus.get().register(C.TOPIC_ERROR_TAG, String.class);
        obs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                log(s);
                for (FeedItemView fiv : errorLists) {
                    boolean equals = TextUtils.equals(s, fiv.getDesText());
                    fiv.setChecked(equals);
                    if (equals) type = fiv.getDesText();
                }
            }
        });
    }

    @OnClick({R.id.topic_error_commit, R.id.topic_error_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topic_error_commit:
                commitBug();
                break;
            case R.id.topic_error_cancel:
                finishWithAnim();
                break;
            default:
                break;
        }
    }

    private void commitBug() {
        if (TextUtils.isEmpty(type)) {
            toastShort(R.string.str_topic_error_type);
            return;
        }
        addToCompositeDis(HttpUtil
                .commitTopicBug(questionId, type, getEditText(et))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        toastShort(bean.getMsg());
                        if (getHttpResSuc(bean.getCode())) {
                            finishWithAnim();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (obs != null)
            RxBus.get().unregister(C.TOPIC_ERROR_TAG, obs);
    }
}
