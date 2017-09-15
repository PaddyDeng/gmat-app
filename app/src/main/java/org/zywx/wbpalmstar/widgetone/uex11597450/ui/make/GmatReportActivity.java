package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.ChartView;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReportBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReportData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.PopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GmatReportActivity extends BaseActivity {

    @BindView(R.id.chart_avg_view)
    ChartView avgChartView;
    @BindView(R.id.chart_pace_view)
    ChartView paceChartView;
    @BindView(R.id.sin_chart_correct)
    ChartView singleChartView;
    @BindView(R.id.sin_chart_pace)
    ChartView singlePaceChartView;

    @BindView(R.id.correct_avg)
    TextView sinAvg;
    @BindView(R.id.pace_min_des)
    TextView sinPace;
    @BindView(R.id.q_pace)
    TextView qPace;
    @BindView(R.id.q_avg)
    TextView qAvg;

    @BindView(R.id.sc_avg_tv)
    TextView scAvgTv;
    @BindView(R.id.sc_pace_tv)
    TextView scPaceTv;
    @BindView(R.id.rc_avg_tv)
    TextView rcAvgTv;
    @BindView(R.id.rc_pace_tv)
    TextView rcPaceTv;
    @BindView(R.id.cr_avg_tv)
    TextView crAvgTv;
    @BindView(R.id.cr_pace_tv)
    TextView crPaceTv;
    @BindView(R.id.know_point_detail_container)
    LinearLayout kpDetailContainer;

    @BindView(R.id.sc_level_one_tv)
    TextView scLevelOneTv;
    @BindView(R.id.sc_level_two_tv)
    TextView scLevelTwoTv;
    @BindView(R.id.sc_level_thr_tv)
    TextView scLevelthrTv;
    @BindView(R.id.sc_level_four_tv)
    TextView scLevelFourTv;

    @BindView(R.id.rc_level_one_tv)
    TextView rcLevelOneTv;
    @BindView(R.id.rc_level_two_tv)
    TextView rcLevelTwoTv;
    @BindView(R.id.rc_level_thr_tv)
    TextView rcLevelthrTv;
    @BindView(R.id.rc_level_four_tv)
    TextView rcLevelFourTv;

    @BindView(R.id.cr_level_one_tv)
    TextView crLevelOneTv;
    @BindView(R.id.cr_level_two_tv)
    TextView crLevelTwoTv;
    @BindView(R.id.cr_level_thr_tv)
    TextView crLevelthrTv;
    @BindView(R.id.cr_level_four_tv)
    TextView crLevelFourTv;

    @BindView(R.id.whole_one_des)
    TextView wholeOneTv;
    @BindView(R.id.whole_two_des)
    TextView wholeTwoTv;
    @BindView(R.id.whole_thr_des)
    TextView wholeThrTv;
    @BindView(R.id.whole_four_des)
    TextView wholeFourTv;

    @BindView(R.id.all_des_tv)
    TextView allDesTv;
    @BindView(R.id.review_detail_tv)
    TextView reviewDetailTv;

    @BindView(R.id.modify_sc_tv)
    TextView modifyTv;
    @BindView(R.id.read_rc_tv)
    TextView readTv;
    @BindView(R.id.l_cr_tv)
    TextView lTv;

    @BindView(R.id.all_sc_tv)
    TextView scTv;
    @BindView(R.id.all_rc_tv)
    TextView rcTv;
    @BindView(R.id.all_cr_tv)
    TextView crTv;
    @BindView(R.id.all_q_tv)
    TextView qTv;

    @BindView(R.id.single_circle_container)
    LinearLayout sinContainer;
    @BindView(R.id.correct_no_q_container)
    LinearLayout corNoQContainer;
    @BindView(R.id.know_point_des_title)
    TextView kpDesTitle;
    @BindView(R.id.know_point_anal_des)
    TextView analDes;
    @BindView(R.id.modify_read_l_container)
    LinearLayout allContainer;
    @BindView(R.id.report_diff_level_tv)
    TextView diffLevelTv;
    @BindView(R.id.report_diff_level_tab_container)
    LinearLayout diffLevelContainer;
    @BindView(R.id.quant_part_tv)
    TextView quantPartTv;
    @BindView(R.id.quant_part_circle_container)
    LinearLayout quantContainer;
    @BindView(R.id.report_gat_container)
    LinearLayout gapContainer;
    @BindView(R.id.report_title_tv)
    TextView titleTv;
    @BindView(R.id.report_part_tv)
    TextView reportPartTv;
    @BindView(R.id.show_pop_img)
    ImageView showPopImg;

    private int popWidth;

    private Animation rotate;

    private LayoutInflater mInflater;
    private List<List<String>> sc_acc;
    private List<List<String>> rc_acc;
    private List<List<String>> cr_acc;
    private List<List<String>> q_acc;

    private int scCorrect;
    private int rcCorrect;
    private int crCorrect;
    private int qCorrect;

    private ResultBean<ReportData> mResultBean;
    private PopWindow mPopWindow;

    private int CURRENTREPORT = ReportType.ALL_REPORT;

    @OnClick({R.id.read_rc_tv, R.id.l_cr_tv, R.id.modify_sc_tv, R.id.all_sc_tv, R.id.all_rc_tv, R.id.all_cr_tv, R.id.all_q_tv
            , R.id.show_pop_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_rc_tv:
            case R.id.l_cr_tv:
            case R.id.modify_sc_tv:
                resetStatus(view);
                break;
            case R.id.all_sc_tv:
            case R.id.all_rc_tv:
            case R.id.all_cr_tv:
            case R.id.all_q_tv:
                resetAllStatus(view);
                break;
            case R.id.show_pop_img:
                startAnim(view);
                if (mPopWindow != null) {
                    int[] location = new int[2];
                    view.getLocationOnScreen(location);
                    Point size = MeasureUtil.getScreenSize(mContext);
                    if (popWidth == 0) {
                        mPopWindow.showAsDropDown(view, location[0] - size.x, 0);
                    } else {
                        mPopWindow.showAsDropDown(view, -popWidth + view.getMeasuredWidth() * 3 / 2 - 5, 0);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void startAnim(View view) {
        if (rotate == null) return;
        rotate.setFillAfter(!rotate.getFillAfter());
        view.startAnimation(rotate);
    }


    private void resetAllStatus(View view) {
        scTv.setSelected(false);
        rcTv.setSelected(false);
        crTv.setSelected(false);
        qTv.setSelected(false);
        view.setSelected(true);
        if (view == scTv) {
            setScDes();
        } else if (view == rcTv) {
            setRcDes();
        } else if (view == crTv) {
            setCrDes();
        } else if (view == qTv) {
            setQDes();
        }
    }

    private void resetStatus(View view) {
        modifyTv.setSelected(false);
        readTv.setSelected(false);
        lTv.setSelected(false);
        view.setSelected(true);
        if (view == modifyTv) {
            setKnowPointInfo(sc_acc);
        } else if (view == readTv) {
            setKnowPointInfo(rc_acc);
        } else if (view == lTv) {
            setKnowPointInfo(cr_acc);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmat_report);
    }

    @Override
    protected void initData() {
        mInflater = LayoutInflater.from(mContext);

        addToCompositeDis(HttpUtil.getReportData().doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                showLoadDialog();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                dismissLoadDialog();
            }
        }).subscribe(new Consumer<ResultBean<ReportData>>() {
            @Override
            public void accept(@NonNull ResultBean<ReportData> bean) throws Exception {
                dismissLoadDialog();
                if (getHttpResSuc(bean.getCode())) {
                    mResultBean = bean;
                    refreshUi(bean);
                } else {
                    toastShort(bean.getMessage());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                errorTip(throwable);
            }
        }));
        if (mPopWindow == null) {
            View view = mInflater.inflate(R.layout.pop_layout, null);
            view.findViewById(R.id.pop_all).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CURRENTREPORT = ReportType.ALL_REPORT;
                    titleTv.setText(getString(R.string.str_test_gmat_report));
                    reportPartTv.setText(getString(R.string.str_report_verbal));
                    refreshUi(mResultBean);
                    mPopWindow.dismiss();
                }
            });
            view.findViewById(R.id.pop_sc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CURRENTREPORT = ReportType.SC_REPORT;
                    titleTv.setText(getString(R.string.str_sc_gmat_report));
                    reportPartTv.setText(getString(R.string.str_report_sc_data));
                    refreshUi(mResultBean);
                    mPopWindow.dismiss();
                }
            });
            view.findViewById(R.id.pop_rc).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CURRENTREPORT = ReportType.RC_REPORT;
                    titleTv.setText(getString(R.string.str_rc_gmat_report));
                    reportPartTv.setText(getString(R.string.str_report_rc_data));
                    refreshUi(mResultBean);
                    mPopWindow.dismiss();
                }
            });
            view.findViewById(R.id.pop_cr).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CURRENTREPORT = ReportType.CR_REPORT;
                    titleTv.setText(getString(R.string.str_cr_gmat_report));
                    reportPartTv.setText(getString(R.string.str_report_cr_data));
                    refreshUi(mResultBean);
                    mPopWindow.dismiss();
                }
            });
            view.findViewById(R.id.pop_q).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CURRENTREPORT = ReportType.QUANT_REPORT;
                    titleTv.setText(getString(R.string.str_q_gmat_report));
                    reportPartTv.setText(getString(R.string.str_report_q_data));
                    refreshUi(mResultBean);
                    mPopWindow.dismiss();
                }
            });
            mPopWindow = new PopWindow(mContext, view);
            final View contentView = mPopWindow.getContentView();
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Utils.removeOnGlobleListener(contentView,this);
                    popWidth = contentView.getMeasuredWidth();
                }
            });
            rotate = AnimationUtils.loadAnimation(mContext, R.anim.arrow_rotate);//创建动画
            rotate.setInterpolator(new LinearInterpolator());
            mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    startAnim(showPopImg);
                }
            });
        }
    }

    private void setKnowPointInfo(List<List<String>> data) {
        kpDetailContainer.removeAllViews();
        if (data != null && !data.isEmpty()) {
            for (List<String> scLists : data) {
                if (scLists == null || scLists.isEmpty() || scLists.size() < 2) continue;
                String knowPointDes = scLists.get(0);
                String avgPointDes = scLists.get(1);
                View view = mInflater.inflate(R.layout.report_know_point_item_layout, null);
                TextView desTv = (TextView) view.findViewById(R.id.know_point_detail_title_des);
                TextView corTv = (TextView) view.findViewById(R.id.know_point_detail_value_des);
                ProgressBar pb = (ProgressBar) view.findViewById(R.id.know_point_detail_progress);
                desTv.setText(getString(R.string.str_kp_des, knowPointDes));
                corTv.setText(getString(R.string.str_avg_des, avgPointDes) + "%");
                pb.setProgress(Integer.parseInt(avgPointDes));
                kpDetailContainer.addView(view);
            }
        }
    }

    private void refreshUi(ResultBean<ReportData> bean) {
        if (CURRENTREPORT == ReportType.ALL_REPORT) {
            Utils.setGone(sinContainer);
            Utils.setVisible(corNoQContainer, kpDesTitle, analDes, allContainer, diffLevelTv, diffLevelContainer, quantPartTv, quantContainer, gapContainer);
        } else {
            Utils.setGone(corNoQContainer, kpDesTitle, analDes, allContainer, diffLevelTv, diffLevelContainer, quantPartTv, quantContainer, gapContainer);
            Utils.setVisible(sinContainer);
        }
        if (bean == null) return;
        ReportData data = bean.getData();
        if (data == null) return;
        ReportBean sc_data = data.getSc_data();
        if (sc_data != null) {
            scAvgTv.setText(getAvg(sc_data.getCorrectAll()));
            scPaceTv.setText(getPace(sc_data.getAverageTime()));
        }
        ReportBean cr_data = data.getCr_data();
        if (cr_data != null) {
            crAvgTv.setText(getAvg(cr_data.getCorrectAll()));
            crPaceTv.setText(getPace(cr_data.getAverageTime()));
        }
        ReportBean rc_data = data.getRc_data();
        if (rc_data != null) {
            rcAvgTv.setText(getAvg(rc_data.getCorrectAll()));
            rcPaceTv.setText(getPace(rc_data.getAverageTime()));
        }
        ReportBean quant_data = data.getQuant_data();
        if (quant_data != null && cr_data != null && sc_data != null && rc_data != null) {
            qCorrect = quant_data.getCorrectAll();
            rcCorrect = rc_data.getCorrectAll();
            crCorrect = cr_data.getCorrectAll();
            scCorrect = sc_data.getCorrectAll();
            double sum = (sc_data.getCorrectAll() + cr_data.getCorrectAll() + rc_data.getCorrectAll() + quant_data.getCorrectAll()) / 4.0f;
            if (sum >= 70) {
                allDesTv.setText(getString(R.string.better));
            } else if (sum >= 50) {
                allDesTv.setText(getString(R.string.more_than));
            } else {
                allDesTv.setText(getString(R.string.loss));
            }
        }
        //pace
//        quant_data.getAverageTime();
        List<ChartView.PieceDataHolder> paceHolder = new ArrayList<>();
        int pace = quant_data.getAverageTime();
        if (pace >= 60) {
            pace = 60;
        }
        paceHolder.add(new ChartView.PieceDataHolder(60 - pace, Color.WHITE, ""));
        paceHolder.add(new ChartView.PieceDataHolder(pace, getResources().getColor(R.color.color_orange), String.valueOf(pace)));
        paceChartView.setData(paceHolder);
        qPace.setText(getString(R.string.str_pace_place_min_des, Utils.format(pace)));

        List<ChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();
        pieceDataHolders.add(new ChartView.PieceDataHolder(360 - qCorrect * 3.6f, Color.WHITE, ""));
        pieceDataHolders.add(new ChartView.PieceDataHolder(qCorrect * 3.6f, Color.BLUE, getAvg(qCorrect)));
        avgChartView.setData(pieceDataHolders);
        qAvg.setText(getString(R.string.str_correct_place_avg_des, qCorrect + "%"));

        rc_acc = data.getRc_acc();
        cr_acc = data.getCr_acc();
        q_acc = data.getQuant_acc();

        sc_acc = data.getSc_acc();
        switch (CURRENTREPORT) {
            case ReportType.ALL_REPORT:
            case ReportType.SC_REPORT:
                modifyTv.setSelected(true);
                scTv.setSelected(true);
                setSingleChartView(sc_data.getCorrectAll(), sc_data.getAverageTime());
                setKnowPointInfo(sc_acc);
                setScDes();
                break;
            case ReportType.RC_REPORT:
                setSingleChartView(rc_data.getCorrectAll(), rc_data.getAverageTime());
                setKnowPointInfo(rc_acc);
                setRcDes();
                break;
            case ReportType.CR_REPORT:
                setSingleChartView(cr_data.getCorrectAll(), cr_data.getAverageTime());
                setKnowPointInfo(cr_acc);
                setCrDes();
                break;
            case ReportType.QUANT_REPORT:
                setSingleChartView(quant_data.getCorrectAll(), quant_data.getAverageTime());
                setKnowPointInfo(q_acc);
                setQDes();
                break;
            default:
                break;
        }

        ReportBean whole_diffculty600 = data.getWhole_diffculty600();
        ReportBean whole_diffculty650 = data.getWhole_diffculty650();
        ReportBean whole_diffculty700 = data.getWhole_diffculty700();
        ReportBean whole_diffculty750 = data.getWhole_diffculty750();
        if (whole_diffculty600 != null && whole_diffculty650 != null && whole_diffculty700 != null && whole_diffculty750 != null) {
            wholeOneTv.setText(getString(R.string.str_whole_one_des, getAvg(whole_diffculty600.getCorrectAll())));
            wholeTwoTv.setText(getString(R.string.str_whole_two_des, getAvg(whole_diffculty650.getCorrectAll())));
            wholeThrTv.setText(getString(R.string.str_whole_thr_des, getAvg(whole_diffculty700.getCorrectAll())));
            wholeFourTv.setText(getString(R.string.str_whole_four_des, getAvg(whole_diffculty750.getCorrectAll())));
        }

        ReportBean diffculty600 = data.getSc_diffculty600();
        ReportBean diffculty650 = data.getSc_diffculty650();
        ReportBean diffculty700 = data.getSc_diffculty700();
        ReportBean diffculty750 = data.getSc_diffculty750();
        if (diffculty600 != null) {
            setLevelTxt(scLevelOneTv, diffculty600.getCorrectAll());
        }
        if (diffculty650 != null) {
            setLevelTxt(scLevelTwoTv, diffculty650.getCorrectAll());
        }
        if (diffculty700 != null) {
            setLevelTxt(scLevelthrTv, diffculty700.getCorrectAll());
        }
        if (diffculty750 != null) {
            setLevelTxt(scLevelFourTv, diffculty750.getCorrectAll());
        }

        ReportBean rc_diffculty600 = data.getRc_diffculty600();
        ReportBean rc_diffculty650 = data.getRc_diffculty650();
        ReportBean rc_diffculty700 = data.getRc_diffculty700();
        ReportBean rc_diffculty750 = data.getRc_diffculty750();
        if (rc_diffculty600 != null) {
            setLevelTxt(rcLevelOneTv, rc_diffculty600.getCorrectAll());
        }
        if (rc_diffculty650 != null) {
            setLevelTxt(rcLevelTwoTv, rc_diffculty650.getCorrectAll());
        }
        if (rc_diffculty700 != null) {
            setLevelTxt(rcLevelthrTv, rc_diffculty700.getCorrectAll());
        }
        if (rc_diffculty750 != null) {
            setLevelTxt(rcLevelFourTv, rc_diffculty750.getCorrectAll());
        }

        ReportBean cr_diffculty600 = data.getCr_diffculty600();
        ReportBean cr_diffculty650 = data.getCr_diffculty650();
        ReportBean cr_diffculty700 = data.getCr_diffculty700();
        ReportBean cr_diffculty750 = data.getCr_diffculty750();
        if (cr_diffculty600 != null) {
            setLevelTxt(crLevelOneTv, cr_diffculty600.getCorrectAll());
        }
        if (cr_diffculty650 != null) {
            setLevelTxt(crLevelTwoTv, cr_diffculty650.getCorrectAll());
        }
        if (cr_diffculty700 != null) {
            setLevelTxt(crLevelthrTv, cr_diffculty700.getCorrectAll());
        }
        if (cr_diffculty750 != null) {
            setLevelTxt(crLevelFourTv, cr_diffculty750.getCorrectAll());
        }

        setScDes();
    }

    private void setSingleChartView(int correct, int pace) {
        if (pace >= 60) {
            pace = 60;
        }
        List<ChartView.PieceDataHolder> paceHolder = new ArrayList<>();
        paceHolder.add(new ChartView.PieceDataHolder(60 - pace, Color.WHITE, ""));
        paceHolder.add(new ChartView.PieceDataHolder(pace, getResources().getColor(R.color.color_orange), String.valueOf(pace)));
        singlePaceChartView.setData(paceHolder);
        sinPace.setText(getString(R.string.str_pace_place_min_des, Utils.format(pace)));

        List<ChartView.PieceDataHolder> pieceDataHolders = new ArrayList<>();
        pieceDataHolders.add(new ChartView.PieceDataHolder(360 - correct * 3.6f, Color.WHITE, ""));
        pieceDataHolders.add(new ChartView.PieceDataHolder(correct * 3.6f, Color.BLUE, getAvg(correct)));
        singleChartView.setData(pieceDataHolders);
        sinAvg.setText(getString(R.string.str_correct_place_avg_des, correct + "%"));
    }

    private void setScDes() {
        if (scCorrect >= 80) {
            reviewDetailTv.setText(getString(R.string.str_sc_e));
        } else if (scCorrect >= 65) {
            reviewDetailTv.setText(getString(R.string.str_sc_s));
        } else if (scCorrect >= 50) {
            reviewDetailTv.setText(getString(R.string.str_sc_f));
        } else {
            reviewDetailTv.setText(getString(R.string.str_sc_l));
        }
    }

    private void setQDes() {
        if (qCorrect >= 90) {
            reviewDetailTv.setText(getString(R.string.str_q_n));
        } else if (qCorrect >= 80) {
            reviewDetailTv.setText(getString(R.string.str_q_e));
        } else if (qCorrect >= 60) {
            reviewDetailTv.setText(getString(R.string.str_q_f));
        } else {
            reviewDetailTv.setText(getString(R.string.str_q_l));
        }
    }

    private void setRcDes() {
        if (rcCorrect >= 60) {
            reviewDetailTv.setText(getString(R.string.str_rc_s));
        } else if (rcCorrect >= 40) {
            reviewDetailTv.setText(getString(R.string.str_rc_f));
        } else {
            reviewDetailTv.setText(getString(R.string.str_rc_l));
        }
    }

    private void setCrDes() {
        if (crCorrect >= 70) {
            reviewDetailTv.setText(getString(R.string.str_cr_s));
        } else if (crCorrect >= 50) {
            reviewDetailTv.setText(getString(R.string.str_cr_f));
        } else {
            reviewDetailTv.setText(getString(R.string.str_cr_l));
        }
    }

    private void setLevelTxt(TextView tv, int value) {
        tv.setText(getAvg(value));
    }

    private String getPace(int value) {
        return Utils.format(value);
    }

    private String getAvg(int value) {
        return value + "%";
    }
}
