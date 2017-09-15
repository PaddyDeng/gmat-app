package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AskQuestionActivity extends BaseActivity {

    public static void startAskQuestionAct(FragmentActivity c, int questionId) {
        Intent intent = new Intent(c, AskQuestionActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, questionId);
        c.startActivityForResult(intent, C.ASK_QUESTION_CODE);
    }

    @BindView(R.id.ask_question_et)
    EditText et;
    private int questionId;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        questionId = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
    }

    @OnClick({R.id.ask_question_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ask_question_commit:
                commit();
                break;
            default:
                break;
        }
    }

    private void commit() {
        String text = getEditText(et);
        if (TextUtils.isEmpty(text)) {
            toastShort(R.string.str_post_remark_enter_content);
            return;
        }
        addToCompositeDis(HttpUtil
                .addComment(text, questionId, "0")
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
                        toastShort(bean.getMessage());
                        if (getHttpResSuc(bean.getCode())) {
                            setResult(RESULT_OK);
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

}
