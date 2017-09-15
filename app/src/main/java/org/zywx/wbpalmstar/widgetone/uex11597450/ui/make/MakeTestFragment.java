package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RandomData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.localdb.LocalQuestionData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.DBUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.ocr.camera.CameraActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.permission.RxPermissions;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.LoginSuccessReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.LoginTipDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.update.UpdateDbDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.intelligent.OrderPracticeActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.knowpoint.KnowPointPracticeActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.search.SearchQuestionActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.search.TopicSearchActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.singlepractice.SinglePracticeActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.difficultymake.DiffMakeActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.simulationtest.SimulationTestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.FileUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RecordUploadProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class MakeTestFragment extends BaseFragment {


    private static final int REQUEST_CODE_GENERAL = 105;

    @BindView(R.id.make_topic_number_des)
    TextView topicNumebrTv;
    @BindView(R.id.make_test_person_number)
    TextView personNum;
    @BindView(R.id.make_dark_container)
    RelativeLayout mTopContainer;
    @BindView(R.id.make_test_des_container)
    RelativeLayout desContainer;
    @BindView(R.id.make_test_async_commit_local_db)
    ImageView asyncImg;
    @BindView(R.id.db_hint_update_container)
    LinearLayout mHintContainer;
    @BindView(R.id.update_content_des)
    TextView updateDes;
    private boolean viewCreate;
    private boolean seeReport;
    private boolean asynData;
    private Animation mAnimation;
    private Observable<Integer> uploadDbObs;
    private Observable<Boolean> updateLocakDbObs;
    private String dbUrl;
    private int lastUpdateTime;

    private LoginSuccessReceiver mReceiver = new LoginSuccessReceiver() {
        @Override
        protected void performAction(Intent intent) {
            if (!viewCreate) return;
            if (seeReport) {
                seeReport = false;
                startActivity(new Intent(getActivity(), GmatReportActivity.class));
            }
        }
    };

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
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_test, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewCreate = true;
        ((MainActivity) getActivity()).setInteceptEvent(false);
        asynData();

        updateLocalDB();

        queryRandromNumber(C.MAKE_TEST_, C.MAKE_TEST_PERSON);
        mTopContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mTopContainer, this);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) desContainer.getLayoutParams();
                params.height = mTopContainer.getMeasuredHeight();
                desContainer.requestLayout();
            }
        });

        mAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setRepeatCount(100);
        uploadDbObs = RxBus.get().register(C.UPLOAD_DB_TOPIC_SIZE, Integer.class);
        updateLocakDbObs = RxBus.get().register(C.UPDATE_LOCAL_DB, Boolean.class);
        uploadDbObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (mAnimation != null) {
                    mAnimation.cancel();
                }
            }
        });
        updateLocakDbObs.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    SharedPref.setLocalDBTime(getActivity(), lastUpdateTime);
                    Utils.setGone(mHintContainer);
                }
            }
        });
    }

    private void updateLocalDB() {
        addToCompositeDis(HttpUtil.updateDb(SharedPref.getLocalDBTime(getActivity()))
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        if (!getHttpResSuc(bean.getCode())) return;
                        lastUpdateTime = bean.getTime();
                        if (SharedPref.getLocalDBTime(getActivity()) != lastUpdateTime) {
                            dbUrl = bean.getUrl();
                            updateDes.setText(bean.getMessage());
                            Utils.setVisible(mHintContainer);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    private void asynData() {
        if (!asynData) {
            addToCompositeDis(HttpUtil.getSubjectTotal().subscribe(new Consumer<ResultBean>() {
                @Override
                public void accept(@NonNull ResultBean bean) throws Exception {
                    topicNumebrTv.setText(HtmlUtil.fromHtml(getString(R.string.str_make_test_topic_number, bean.getVerbalNum(), bean.getQuantNum())));
                    asynData = true;
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    asynData();
                }
            }));
        }
    }

    private void queryRandromNumber(final String courseId, final int type) {
        addToCompositeDis(Observable.just(1).flatMap(new Function<Integer, ObservableSource<RandomData>>() {
            @Override
            public ObservableSource<RandomData> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<RandomData>() {
                    @Override
                    public void subscribe(ObservableEmitter<RandomData> e) throws Exception {
                        RandomData randomData = PracticeManager.getInstance().queryNumber(courseId, type);
                        e.onNext(randomData);
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<RandomData>())
                .subscribe(new Consumer<RandomData>() {
                    @Override
                    public void accept(@NonNull RandomData data) throws Exception {
                        personNum.setText(HtmlUtil.fromHtml(getString(R.string.str_make_test_people_number, data.getTimes())));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }


    /**
     * 上传本地数据库
     */
    private void uploadLocalDb() {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            return;
        }
        RecordUploadProxy.uploadLocalDb(getActivity(), new ICallBack() {
            @Override
            public void onSuccess(Object o) {
//                mAnimation.cancel();
            }

            @Override
            public void onFail() {
                mAnimation.cancel();
            }
        });
    }

    @OnClick({R.id.single_contact_container, R.id.know_point_practice_container, R.id.diff_practice_container,
            R.id.simulation_test_container, R.id.make_test_intelligent_container, R.id.report_container,
            R.id.pic_search_container, R.id.language_search_container, R.id.find_search_container,
            R.id.make_test_async_commit_local_db, R.id.db_update_btn, R.id.db_uptade_cancel_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.db_uptade_cancel_btn:
                Utils.setGone(mHintContainer);
                break;
            case R.id.db_update_btn:
                updateDb();
                break;
            case R.id.make_test_async_commit_local_db:
                if (GlobalUser.getInstance().isAccountDataInvalid()) {
                    toastShort(R.string.str_no_login_tip);
                    return;
                }
//                Utils.setGone(asyncDbHint);
                asyncImg.startAnimation(mAnimation);
                uploadLocalDb();
                break;
            case R.id.find_search_container://搜索找题
                startActivity(new Intent(getActivity(), SearchQuestionActivity.class));
                break;
            case R.id.language_search_container://语音找题
                startActivity(new Intent(getActivity(), TopicSearchActivity.class));
                break;
            case R.id.pic_search_container://拍照找题
                picSearch();
                break;
            case R.id.single_contact_container:
                startActivity(new Intent(getActivity(), SinglePracticeActivity.class));
                break;
            case R.id.know_point_practice_container:
                startActivity(new Intent(getActivity(), KnowPointPracticeActivity.class));
                break;
            case R.id.diff_practice_container:
                startActivity(new Intent(getActivity(), DiffMakeActivity.class));
                break;
            case R.id.simulation_test_container:
                startActivity(new Intent(getActivity(), SimulationTestActivity.class));
                break;
            case R.id.make_test_intelligent_container:
                startActivity(new Intent(getActivity(), OrderPracticeActivity.class));
                //智能学习，做全部题，若未做完，做剩下的题
//                TestActivity.startTestActivity(getActivity(), new QuestionBankData(), TestType.INTELLIGENT_TEST);
                break;
            case R.id.report_container:
                if (GlobalUser.getInstance().isAccountDataInvalid()) {
                    seeReport = true;
                    new LoginTipDialog().showDialog(getChildFragmentManager());
                } else {
                    startActivity(new Intent(getActivity(), GmatReportActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private void updateDb() {
        UpdateDbDialog.getInstance(dbUrl).showDialog(getChildFragmentManager());
    }

    private void picSearch() {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getActivity()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_GENERAL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreate = false;
        if (uploadDbObs != null) {
            RxBus.get().unregister(C.UPLOAD_DB_TOPIC_SIZE, uploadDbObs);
        }
        if (updateLocakDbObs != null) {
            RxBus.get().unregister(C.UPDATE_LOCAL_DB, updateLocakDbObs);
        }
    }
}
