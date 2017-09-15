package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import org.w3c.dom.Text;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreDetailData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.HighScoreItemData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.GeneralView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HighScoreDetailActivity extends BaseActivity {

    public static void startHighScoreDetailAct(Context c, String contentId) {
        Intent intent = new Intent(c, HighScoreDetailActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, contentId);
        c.startActivity(intent);
    }

    private String contentId;
    @BindView(R.id.high_score_detail_general_view)
    GeneralView mGeneralView;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        contentId = intent.getStringExtra(Intent.EXTRA_INDEX);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_detail);
    }

    @Override
    protected void asyncUiInfo() {
        super.asyncUiInfo();
        if (TextUtils.isEmpty(contentId)) return;
        addToCompositeDis(HttpUtil
                .highScoreDetail(contentId)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<HighScoreDetailData>() {
                    @Override
                    public void accept(@NonNull HighScoreDetailData bean) throws Exception {
                        dismissLoadDialog();
                        refreshUi(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorTip(throwable);
                    }
                }));
    }

    @BindView(R.id.high_score_user_name_tv)
    TextView uName;
    @BindView(R.id.high_score_base_tv)
    TextView baseTv;
    @BindView(R.id.high_score_out_time_tv)
    TextView outTimeTv;
    @BindView(R.id.high_score_class_type_tv)
    TextView classTypeTv;
    @BindView(R.id.test_times_tv)
    TextView testTimesTv;
    @BindView(R.id.test_score_tv)
    TextView testScoreTv;
    @BindView(R.id.high_score_title)
    TextView highScoreTitle;

    private void refreshUi(HighScoreDetailData bean) {
        if (bean == null) return;
        List<HighScoreItemData> details = bean.getDetails();
        if (details == null || details.isEmpty()) return;
        HighScoreItemData itemData = details.get(0);
        highScoreTitle.setText(itemData.getContenttitle());
        mGeneralView.setHighDetalText(itemData.getDetails());
        uName.setText(getString(R.string.str_high_score_user_name, itemData.getFullName()));
        baseTv.setText(getString(R.string.str_high_score_base, itemData.getBasics()));
        outTimeTv.setText(getString(R.string.str_high_score_out_time, itemData.getOutTime()));
        classTypeTv.setText(getString(R.string.str_high_score_class_type, itemData.getClassType()));
        testTimesTv.setText(getString(R.string.str_high_score_test_temes, itemData.getExa()));
        testScoreTv.setText(getString(R.string.str_high_score_test_score, itemData.getFraction()));
    }
}
