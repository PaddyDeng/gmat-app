package org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.better.banner.BBanner;
import com.better.banner.ItemAdapter;
import com.better.banner.OnClickItemListener;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ListenData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.NewHot;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.NewHotData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.NewResultHot;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.PublicLessonData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RandomData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.Banner;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.GmatDetailData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.HotResultData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.gmat.TeacherData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.LoginSuccessReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.DealActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.LoginTipDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.adapter.CardAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.adapter.HotAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.adapter.LessonAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.gmat.adapter.TeacherAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.StrongRecycler;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class GmatFragment extends BaseFragment {

    @BindView(R.id.gmat_banner)
    BBanner bBanner;
    @BindView(R.id.card_recycler)
    StrongRecycler cardRecycler;
    @BindView(R.id.lesson_recycler)
    StrongRecycler lessonRecycler;
    @BindView(R.id.lesson_video_recycler)
    StrongRecycler videoRecycler;
    @BindView(R.id.teacher_recycler)
    StrongRecycler teacherRecycler;
    @BindView(R.id.hot_recycler)
    StrongRecycler hotRecycler;
    @BindView(R.id.gmat_live_lesson_tv)
    TextView liveTv;
    @BindView(R.id.gmat_video_lesson_tv)
    TextView videoTv;
    @BindView(R.id.gmat_free_one_random)
    TextView freeLabelOneRandom;
    @BindView(R.id.free_lesson_two_container)
    TextView freeLabelTwoRandom;
    @BindView(R.id.free_lesson_thr_container)
    TextView freeLabelThrRandom;
    @BindView(R.id.free_label_four_iv)
    TextView freeLabelfourRandom;
    @BindView(R.id.free_one_title_tv)
    TextView freeOneTitleTv;
    @BindView(R.id.free_two_title_tv)
    TextView freeTwoTitleTv;
    @BindView(R.id.free_thr_title_tv)
    TextView freeThrTitleTv;
    @BindView(R.id.free_four_title_tv)
    TextView freeFourTitleTv;
    @BindView(R.id.intro_one_title_tv)
    TextView introOneTitleTv;
    @BindView(R.id.gmat_intro_two_title)
    TextView introTwoTitleTv;
    @BindView(R.id.intro_thr_title_tv)
    TextView introThrTitleTv;
    @BindView(R.id.intro_four_title_tv)
    TextView introFourTitleTv;
    @BindView(R.id.intro_one_label)
    TextView introLabelOneRandom;
    @BindView(R.id.intro_two_label)
    TextView introLabelTwoRandom;
    @BindView(R.id.intro_thr_container)
    TextView introLabelThrRandom;
    @BindView(R.id.intro_four_container)
    TextView introLabelFourRandom;
    @BindView(R.id.gmat_title_menu)
    ImageView gmatMenu;
    @BindView(R.id.intro_one_course_time)
    TextView introOneTime;
    @BindView(R.id.intro_two_course_time)
    TextView introTwoTime;
    @BindView(R.id.intro_thr_course_time)
    TextView introThrTime;
    @BindView(R.id.intro_four_course_time)
    TextView introFourTime;

    @BindView(R.id.free_one_teacher_tv)
    TextView freeOneTeacherTv;
    @BindView(R.id.gmat_teacher_two)
    TextView freeTwoTeacherTv;
    @BindView(R.id.gmat_teacher_thr)
    TextView freeThrTeacherTv;
    @BindView(R.id.free_four_teacher_tv)
    TextView freeFourTeacherTv;
    @BindView(R.id.free_one_time_tv)
    TextView freeOneTimeTv;
    @BindView(R.id.free_two_time_tv)
    TextView freeTwoTimeTv;
    @BindView(R.id.free_thr_time_tv)
    TextView freeThrTimeTv;
    @BindView(R.id.free_four_time_tv)
    TextView freeFourTimeTv;

    private boolean hasAsyncHotData;
    private boolean hasAsyncOpenData;
    private boolean hasAsyncLiveData;
    private boolean hasAsyncVideoData;
    private boolean hasAsyncTeacherData;
    private boolean viewCreated;
    private boolean hasAsyncFree;
    private boolean hasAsyncIntro;
    private LinearLayoutManager cardLayoutManager;
    private LinearLayoutManager lessonLayoutManager;
    private LinearLayoutManager hotLayoutManager;
    private CardAdapter cardAdapter;
    private TeacherAdapter teacherAdapter;
    private LessonAdapter lessonAdapter;
    private LessonAdapter videoAdapter;
    private HotAdapter hotAdapter;

    private List<Banner> bannerDatas;

    private String INTRO_ZERO = "";//"http://bjsy.gensee.com/training/site/v/48510525?nickname=GMAT";
    private String INTRO_SC = "";//"http://bjsy.gensee.com/training/site/v/54744736?nickname=GMAT";
    private String INTRO_RC = "";//"http://bjsy.gensee.com/training/site/v/06286529?nickname=GMAT";
    private String INTRO_MATH = "";//"http://bjsy.gensee.com/training/site/v/63155729?nickname=GMAT";

    private String FREE_SC = "";
    private String FREE_RC = "";
    private String FREE_CR = "";
    private String FREE_MATH = "";

    private boolean viewCreate;
    private int currentId = 0;

    private LoginSuccessReceiver mReceiver = new LoginSuccessReceiver() {
        @Override
        protected void performAction(Intent intent) {
            if (viewCreate) {
                clickLesson(currentId);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).setInteceptEvent(true);
        if (viewCreated) return;
        viewCreated = true;

        /*
        queryRandromNumber(C.INTRO_MATH, C.INTRO_COURSE, new ICallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                introLabelFourRandom.setTag(integer);
                introLabelFourRandom.setTextContent(getString(R.string.str_play_times, integer));
            }

            @Override
            public void onFail() {
            }
        });
        queryRandromNumber(C.INTRO_RC, C.INTRO_COURSE, new ICallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                introLabelThrRandom.setTag(integer);
                introLabelThrRandom.setTextContent(getString(R.string.str_play_times, integer));
            }

            @Override
            public void onFail() {
            }
        });
        queryRandromNumber(C.INTRO_SC, C.INTRO_COURSE, new ICallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                introLabelTwoRandom.setTag(integer);
                introLabelTwoRandom.setTextContent(getString(R.string.str_play_times, integer));
            }

            @Override
            public void onFail() {
            }
        });

        queryRandromNumber(C.INTRO_CR, C.INTRO_COURSE, new ICallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                introLabelOneRandom.setTag(integer);
                introLabelOneRandom.setTextContent(getString(R.string.str_play_times, integer));
            }

            @Override
            public void onFail() {
            }
        });
        queryRandromNumber(C.FREE_SC, C.TRIAL_COURSE, new ICallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                freeLabelOneRandom.setTag(integer);
                freeLabelOneRandom.setTextContent(getString(R.string.str_play_times, integer));
            }

            @Override
            public void onFail() {
            }
        });
        queryRandromNumber(C.FREE_RC, C.TRIAL_COURSE, new ICallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                freeLabelTwoRandom.setTag(integer);
                freeLabelTwoRandom.setTextContent(getString(R.string.str_play_times, integer));
            }

            @Override
            public void onFail() {
            }
        });
        queryRandromNumber(C.FREE_CR, C.TRIAL_COURSE, new ICallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                freeLabelThrRandom.setTag(integer);
                freeLabelThrRandom.setTextContent(getString(R.string.str_play_times, integer));
            }

            @Override
            public void onFail() {
            }
        });
        queryRandromNumber(C.FREE_MATH, C.TRIAL_COURSE, new ICallBack<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                freeLabelfourRandom.setTag(integer);
                freeLabelfourRandom.setTextContent(getString(R.string.str_play_times, integer));
            }

            @Override
            public void onFail() {
            }
        });
        */
    }

    private void queryRandromNumber(final String courseId, final int type, final ICallBack<Integer> iCallBack) {
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
                        iCallBack.onSuccess(data.getTimes());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        iCallBack.onFail();
                    }
                }));
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_gmat_layout, container, false);
    }

    /**
     * 试听满了，弹框提示
     */
    private void startFreeDeal(String title, String url) {
        if (!hasAsyncFree) return;
        DealActivity.startDealActivity(getActivity(), title, url, C.FREE_INTERCEPT_BUSY);
    }

    private void startDeal(String title, String url) {
        if (!hasAsyncIntro) return;
        DealActivity.startDealActivity(getActivity(), title, url);
    }

    private void clickLesson(int viewId) {
        switch (viewId) {
            case R.id.intro_one_container:
                setLabel(introLabelOneRandom);
                startDeal(""/*getString(R.string.str_intro_one_type)*/, INTRO_ZERO);
                break;
            case R.id.intro_two_container:
                setLabel(introLabelTwoRandom);
                startDeal(""/*getString(R.string.str_intro_two_type)*/, INTRO_SC);
                break;
            case R.id.intro_thr_new_container:
                setLabel(introLabelThrRandom);
                startDeal(""/*getString(R.string.str_intro_thr_type)*/, INTRO_RC);
                break;
            case R.id.intro_four_new_container:
                setLabel(introLabelFourRandom);
                startDeal(""/*getString(R.string.str_intro_four_type)*/, INTRO_MATH);
                break;
            case R.id.free_lesson_one_container:
                setLabel(freeLabelOneRandom);
                startFreeDeal(""/*getString(R.string.str_free_one_type)*/, FREE_SC);
                break;
            case R.id.free_two_new_container:
                setLabel(freeLabelTwoRandom);
                startFreeDeal(""/*getString(R.string.str_free_two_type)*/, FREE_RC);
                break;
            case R.id.free_thr_new_container:
                setLabel(freeLabelThrRandom);
                startFreeDeal(""/*getString(R.string.str_free_thr_type)*/, FREE_CR);
                break;
            case R.id.free_lesson_four_container:
                setLabel(freeLabelfourRandom);
                startFreeDeal(""/*getString(R.string.str_free_four_type)*/, FREE_MATH);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.free_lesson_one_container, R.id.free_two_new_container,
            R.id.free_thr_new_container, R.id.free_lesson_four_container,
            R.id.intro_four_new_container, R.id.intro_thr_new_container,
            R.id.intro_two_container, R.id.intro_one_container})
    public void onClicik(View v) {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            new LoginTipDialog().showDialog(getChildFragmentManager());
            return;
        }
        currentId = v.getId();
        clickLesson(currentId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Utils.setVisible(bBanner, cardRecycler);
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.setInvisible(bBanner, cardRecycler);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreate = false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewCreate = true;

        bBanner.setOnItemClickListener(new OnClickItemListener() {
            @Override
            public void onClick(int i) {
                if (bannerDatas != null && bannerDatas.get(i) != null && !TextUtils.isEmpty(bannerDatas.get(i).getContentlink())) {
                    DealActivity.startDealActivity(getActivity(), bannerDatas.get(i).getContenttitle(), bannerDatas.get(i).getContentlink());
                }
            }
        });

        bBanner.setPageChangeDuration(1000);

        initFreeData();

        initIntroData();

        initHotData();

        initLiveData();

        initVideoData();

        initTeacherData();

        initOpenLessondata();

        gmatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).toggle();
            }
        });

    }

    private void initIntroData() {
        if (hasAsyncIntro) return;
        addToCompositeDis(HttpUtil
                .gmatIntroInfo()
                .subscribe(new Consumer<List<ListenData>>() {
                    @Override
                    public void accept(@NonNull List<ListenData> datas) throws Exception {
                        if (datas == null || datas.isEmpty()) return;
                        for (int i = 0, size = datas.size(); i < size; i++) {
                            ListenData ld = datas.get(i);
                            if (i == 0) {
                                setLabel(introLabelOneRandom, ld.getViews());
                                INTRO_ZERO = ld.getContentlink();
                                introOneTime.setText(ld.getHour());
                                introOneTitleTv.setText(ld.getContenttitle());
                            } else if (i == 1) {
                                setLabel(introLabelTwoRandom, ld.getViews());
                                INTRO_SC = ld.getContentlink();
                                introTwoTime.setText(ld.getHour());
                                introTwoTitleTv.setText(ld.getContenttitle());
                            } else if (i == 2) {
                                setLabel(introLabelThrRandom, ld.getViews());
                                INTRO_RC = ld.getContentlink();
                                introThrTime.setText(ld.getHour());
                                introThrTitleTv.setText(ld.getContenttitle());
                            } else if (i == 3) {
                                setLabel(introLabelFourRandom, ld.getViews());
                                INTRO_MATH = ld.getContentlink();
                                introFourTime.setText(ld.getHour());
                                introFourTitleTv.setText(ld.getContenttitle());
                            }
                        }
                        hasAsyncIntro = true;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    private void setLabel(View view) {
        Object tagObj = view.getTag();
        if (tagObj == null) return;
        int tag = (int) tagObj;
        setTxtTag(view, ++tag);
    }

    private void setTxtTag(View v, int tag) {
        v.setTag(tag);
        ((TextView) v).setText(getString(R.string.str_play_times, tag));
    }

    private void setLabel(View view, String views) {
        views = TextUtils.isEmpty(views) ? "0" : views;
        setTxtTag(view, Integer.parseInt(views));
    }

    private void initFreeData() {
        if (hasAsyncFree) return;
        addToCompositeDis(HttpUtil
                .gmatFreeInfo()
                .subscribe(new Consumer<List<ListenData>>() {
                    @Override
                    public void accept(@NonNull List<ListenData> datas) throws Exception {
                        if (datas == null || datas.isEmpty()) return;
                        for (int i = 0, size = datas.size(); i < size; i++) {
                            ListenData ld = datas.get(i);
                            if (i == 0) {
                                setLabel(freeLabelOneRandom, ld.getViews());
                                FREE_SC = ld.getContentlink();
                                setFreeTxt(freeOneTeacherTv, freeOneTimeTv, freeOneTitleTv, ld.getTeacherName(), ld.getHour(), ld.getContenttitle());
                            } else if (i == 1) {
                                setLabel(freeLabelTwoRandom, ld.getViews());
                                FREE_RC = ld.getContentlink();
                                setFreeTxt(freeTwoTeacherTv, freeTwoTimeTv, freeTwoTitleTv, ld.getTeacherName(), ld.getHour(), ld.getContenttitle());
                            } else if (i == 2) {
                                setLabel(freeLabelThrRandom, ld.getViews());
                                FREE_CR = ld.getContentlink();
                                setFreeTxt(freeThrTeacherTv, freeThrTimeTv, freeThrTitleTv, ld.getTeacherName(), ld.getHour(), ld.getContenttitle());
                            } else if (i == 3) {
                                setLabel(freeLabelfourRandom, ld.getViews());
                                FREE_MATH = ld.getContentlink();
                                setFreeTxt(freeFourTeacherTv, freeFourTimeTv, freeFourTitleTv, ld.getTeacherName(), ld.getHour(), ld.getContenttitle());
                            }
                        }
                        hasAsyncFree = true;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void setFreeTxt(TextView teacher, TextView time, TextView titleTv, String teacherTxt, String hour, String title) {
        teacher.setText(teacherTxt);
        time.setText(hour);
        titleTv.setText(title);
    }

    private void initOpenLessondata() {

        if (hasAsyncOpenData) return;

        cardLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        cardRecycler.setLayoutManager(cardLayoutManager);
//        cardAdapter = new CardAdapter(getList());
//        cardRecycler.setAdapter(cardAdapter);

        addToCompositeDis(HttpUtil.getOpenLesson().flatMap(new Function<ResultBean<List<PublicLessonData>>, ObservableSource<List<PublicLessonData>>>() {
            @Override
            public ObservableSource<List<PublicLessonData>> apply(@NonNull final ResultBean<List<PublicLessonData>> bean) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<PublicLessonData>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<PublicLessonData>> e) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            List<PublicLessonData> data = bean.getData();
                            if (data != null && !data.isEmpty()) {
                                for (PublicLessonData publicData : data) {
//                                    PUBLIC_COURSE
                                    RandomData randomData = PracticeManager.getInstance().queryNumber(C.PUBLIC_ + publicData.getId(), C.PUBLIC_COURSE);
                                    publicData.setRandomNumber(randomData.getTimes());
                                }
                                e.onNext(data);
                            } else {
                                e.onNext(null);
                            }
                        } else {
                            e.onNext(null);
                        }
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<PublicLessonData>>()).subscribe(new Consumer<List<PublicLessonData>>() {
            @Override
            public void accept(@NonNull final List<PublicLessonData> data) throws Exception {
                if (data != null && !data.isEmpty()) {
                    cardAdapter = new CardAdapter(data);
                    cardRecycler.setAdapter(cardAdapter);
                    cardAdapter.setItemListener(new OnItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            PublicLessonData lessonData = data.get(position);
                            startGmatDetail(RetrofitProvider.VIPLGW + lessonData.getImage(), lessonData.getName(),
                                    lessonData.getSentenceNumber(), String.valueOf(lessonData.getRandomNumber()));
                        }
                    });
                    hasAsyncOpenData = true;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }));
    }

    private void startGmatDetail(String headUrl, String title, String des, String number) {
        GmatDetailData data = new GmatDetailData();
        data.setHeadUrl(headUrl);
        data.setTitle(title);
        data.setDes(des);
        data.setNumber(number);
        GmatDetailActivity.startGmatDetail(getActivity(), data);
    }

    private void initTeacherData() {

        if (hasAsyncTeacherData) return;
        LinearLayoutManager teacherLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        teacherRecycler.setLayoutManager(teacherLayoutManager);

        addToCompositeDis(HttpUtil.getTeacherList()
                .flatMap(new Function<ResultBean<List<TeacherData>>, ObservableSource<List<TeacherData>>>() {
                    @Override
                    public ObservableSource<List<TeacherData>> apply(@NonNull final ResultBean<List<TeacherData>> bean) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<TeacherData>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<TeacherData>> e) throws Exception {
                                if (getHttpResSuc(bean.getCode())) {
                                    List<TeacherData> data = bean.getData();
                                    if (data == null && data.isEmpty()) {
                                        e.onNext(null);
                                    } else {
                                        for (TeacherData teacherData : data) {
                                            RandomData randomData = PracticeManager.getInstance().queryNumber(C.TEACHER_ + teacherData.getTeacherId(), C.TEACHER_COURSE);
                                            teacherData.setRandomNumber(randomData.getTimes());
                                        }
                                        e.onNext(data);
                                    }
                                } else {
                                    e.onNext(null);
                                }
                                e.onComplete();
                            }
                        });
                    }
                })
                .compose(new SchedulerTransformer<List<TeacherData>>())
                .subscribe(new Consumer<List<TeacherData>>() {
                    @Override
                    public void accept(@NonNull final List<TeacherData> data) throws Exception {
                        if (data != null && !data.isEmpty()) {
                            teacherAdapter = new TeacherAdapter(data);
                            teacherAdapter.setItemListener(new OnItemClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    TeacherDetailActivity.startTeacherDetail(getActivity(), data.get(position));
                                }
                            });
                            teacherRecycler.setAdapter(teacherAdapter);
                            hasAsyncTeacherData = true;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    private void initVideoData() {
        if (hasAsyncVideoData) return;

        LinearLayoutManager videoLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        videoRecycler.setLayoutManager(videoLayoutManager);

        addToCompositeDis(HttpUtil.getVideoLesson().subscribe(new Consumer<ResultBean<List<HotResultData>>>() {
            @Override
            public void accept(@NonNull ResultBean<List<HotResultData>> bean) throws Exception {
                if (getHttpResSuc(bean.getCode())) {
                    final List<HotResultData> data = bean.getData();
//                    if (data != null && !data.isEmpty()) {
//                        videoListDatas = data;
//                    }
                    if (data != null && !data.isEmpty()) {
                        videoAdapter = new LessonAdapter(data);
                        videoAdapter.setItemListener(new OnItemClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                HotResultData hotResultData = data.get(position);
                                startGmatDetail(RetrofitProvider.BASEURL + hotResultData.getContentthumb(), hotResultData.getContenttitle()
                                        , hotResultData.getContenttext(), hotResultData.getViews());
                            }
                        });
                        videoRecycler.setAdapter(videoAdapter);
                    }
//                    lessonAdapter = new LessonAdapter(bean.getData());
//                    lessonRecycler.setAdapter(lessonAdapter);
                    hasAsyncVideoData = true;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }));
    }

    private void initLiveData() {
        if (hasAsyncLiveData) return;

        lessonLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        lessonRecycler.setLayoutManager(lessonLayoutManager);

        addToCompositeDis(HttpUtil.getLiveLesson()
                .flatMap(new Function<ResultBean<List<HotResultData>>, ObservableSource<List<HotResultData>>>() {
                    @Override
                    public ObservableSource<List<HotResultData>> apply(@NonNull final ResultBean<List<HotResultData>> bean) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<List<HotResultData>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<HotResultData>> e) throws Exception {
                                if (getHttpResSuc(bean.getCode())) {
                                    List<HotResultData> data = bean.getData();
                                    if (data == null && data.isEmpty()) {
                                        e.onNext(null);
                                    } else {
//                                        for (HotResultData hot : data) {
////                                            LIVE_COURSE
//                                        }
                                        e.onNext(data);
                                    }
                                } else {
                                    e.onNext(null);
                                }
                                e.onComplete();
                            }
                        });
                    }
                }).compose(new SchedulerTransformer<List<HotResultData>>())
                .subscribe(new Consumer<List<HotResultData>>() {
                    @Override
                    public void accept(@NonNull final List<HotResultData> data) throws Exception {
                        if (data != null && !data.isEmpty()) {
//                        lessonListDatas = data;
//                        lessonAdapter = new LessonAdapter(lessonListDatas);
                            lessonAdapter = new LessonAdapter(data);
                            lessonAdapter.setItemListener(new OnItemClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    HotResultData hotResultData = data.get(position);
                                    startGmatDetail(RetrofitProvider.BASEURL + hotResultData.getContentthumb(), hotResultData.getContenttitle(),
                                            hotResultData.getContenttext(), hotResultData.getViews());
                                }
                            });
                            lessonRecycler.setAdapter(lessonAdapter);
                            hasAsyncLiveData = true;
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void initHotData() {
        if (hasAsyncHotData) return;

        hotLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        hotRecycler.setLayoutManager(hotLayoutManager);
//        hotAdapter = new HotAdapter(getHotDefault());
//        hotRecycler.setAdapter(hotAdapter);
//        initRecycler(hotRecycler, hotLayoutManager);

        addToCompositeDis(HttpUtil.getHostGmat()
                /*.flatMap(new Function<NewHot, ObservableSource<NewHot>>() {
                    @Override
                    public ObservableSource<NewHot> apply(@NonNull final NewHot bean) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<NewHot>() {
                            @Override
                            public void subscribe(ObservableEmitter<NewHot> e) throws Exception {
                                if (getHttpResSuc(bean.getCode())) {
                                    List<NewHotData> hotData = bean.getHotClass();
                                    if (hotData != null && !hotData.isEmpty()) {
//                                for (HotData hot : hotData) {
                                        for (int i = 0, size = hotData.size(); i < size; i++) {
                                            NewHotData hot = hotData.get(i);
//                                    RandomData randomData = PracticeManager.getInstance().queryNumber(C.HOT_ + hot.getCatid(), C.HOT_COURSE);
                                            RandomData randomData = PracticeManager.getInstance().queryNumber(C.HOT_ + i, C.HOT_COURSE);
                                            hot.setRandomNumber(randomData.getTimes());
                                        }
                                    }
                                }
                                e.onNext(bean);
                                e.onComplete();
                            }
                        });
                    }
                })*/
                .compose(new SchedulerTransformer<NewHot>())
                .subscribe(new Consumer<NewHot>() {
                    @Override
                    public void accept(@NonNull final NewHot bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            bannerDatas = bean.getBanner();
                            bBanner.setData(getChildFragmentManager(), new ItemAdapter() {
                                @Override
                                public Fragment getItem(int p) {
                                    return BannerDetailPager.getInstance(RetrofitProvider.BASEURL + bannerDatas.get(p).getContentthumb());
                                }

                                @Override
                                public int getCount() {
                                    return bannerDatas.size();
                                }
                            });
                            final List<NewHotData> hotClass = bean.getHotClass();
                            hotAdapter = new HotAdapter(hotClass);

                            hotAdapter.setItemListener(new OnItemClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    NewHotData hotData = hotClass.get(position);
                                    NewResultHot hotDataResult = hotData.getResult();
                                    startGmatDetail(RetrofitProvider.BASEURL + hotDataResult.getContentthumb(), hotDataResult.getContenttitle(),
                                            hotDataResult.getContenttext(), hotDataResult.getViews());
                                }
                            });

                            hotRecycler.setAdapter(hotAdapter);

                            hasAsyncHotData = true;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

}
