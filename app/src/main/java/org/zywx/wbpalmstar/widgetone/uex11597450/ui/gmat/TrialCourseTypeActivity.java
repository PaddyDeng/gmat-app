package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ListenData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.DealActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TrialCourseTypeActivity extends BaseActivity {

    private String TRIAL_SC = "";
    private String TRIAL_RC = "";
    private String TRIAL_CR = "";
    private String TRIAL_MATH = "";

    @BindView(R.id.trial_teach_sc)
    TextView trialScTeacherTv;
    @BindView(R.id.trial_teach_rc)
    TextView trialRcTeacherTv;
    @BindView(R.id.trial_teach_cr)
    TextView trialCrTeacherTv;
    @BindView(R.id.trial_teach_math)
    TextView trialMathTeacherTv;

    @BindView(R.id.trial_time_one)
    TextView trialScTimeTv;
    @BindView(R.id.trial_time_rc)
    TextView trialRcTimeTv;
    @BindView(R.id.trial_time_cr)
    TextView trialCrTimeTv;
    @BindView(R.id.trial_time_math)
    TextView trialMathTimeTv;

    private boolean asyncData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_course_type);
    }

    @Override
    protected void asyncUiInfo() {
        super.asyncUiInfo();
        if (asyncData) return;
        addToCompositeDis(HttpUtil
                .gmatFreeInfo()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<List<ListenData>>() {
                    @Override
                    public void accept(@NonNull List<ListenData> datas) throws Exception {
                        dismissLoadDialog();
                        for (ListenData ld : datas) {
                            String contenttitle = ld.getContenttitle();
                            if (contenttitle.contains(getString(R.string.str_gmat_sc))) {
                                TRIAL_SC = ld.getContentlink();
                                setFreeTxt(trialScTeacherTv, trialScTimeTv, ld.getTeacherName(), ld.getHour());
                            } else if (contenttitle.contains(getString(R.string.str_gmat_rc))) {
                                TRIAL_RC = ld.getContentlink();
                                setFreeTxt(trialRcTeacherTv, trialRcTimeTv, ld.getTeacherName(), ld.getHour());
                            } else if (contenttitle.contains(getString(R.string.str_gmat_free_cr))) {
                                TRIAL_CR = ld.getContentlink();
                                setFreeTxt(trialCrTeacherTv, trialCrTimeTv, ld.getTeacherName(), ld.getHour());
                            } else if (contenttitle.contains(getString(R.string.str_gmat_math))) {
                                TRIAL_MATH = ld.getContentlink();
                                setFreeTxt(trialMathTeacherTv, trialMathTimeTv, ld.getTeacherName(), ld.getHour());
                            }
                        }
                        asyncData = true;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                }));
    }

    private void setFreeTxt(TextView teacher, TextView time, String teacherTxt, String hour) {
        teacher.setText(getString(R.string.trial_teacher_des,teacherTxt));
        time.setText(getString(R.string.trial_use_time_des,hour));
    }

    private void startDeal(String title, String url) {
        if (!asyncData) return;
        DealActivity.startDealActivity(mContext, title, url, C.FREE_INTERCEPT_BUSY);
    }

    @OnClick({R.id.trial_sc_container, R.id.trial_rc_container, R.id.trial_cr_container, R.id.trial_math_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trial_sc_container:
                startDeal(getString(R.string.str_free_one_type), TRIAL_SC);
                break;
            case R.id.trial_rc_container:
                startDeal(getString(R.string.str_free_two_type), TRIAL_RC);
                break;
            case R.id.trial_cr_container:
                startDeal(getString(R.string.str_free_thr_type), TRIAL_CR);
                break;
            case R.id.trial_math_container:
                startDeal(getString(R.string.str_free_four_type), TRIAL_MATH);
                break;
            default:
                break;
        }
    }

}
