package org.zywx.wbpalmstar.widgetone.uex11597450.ui.center;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.reflect.TypeToken;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseFragment;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserRecord;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.SchedulerTransformer;
import org.zywx.wbpalmstar.widgetone.uex11597450.push.PushProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.collect.CollectionActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.EvaluationDialog;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.helper.EvaluationProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.TestType;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.setting.SettingActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.user.LoginAactivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.JsonUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RecordUploadProxy;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.PracticeRecordData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.questionbank.QuestionBankData;
import org.zywx.wbpalmstar.widgetone.uex11597450.db.PracticeManager;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.receiver.LoginSuccessReceiver;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.center.buyrecord.BuyRecordActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CircleImageView;

import java.util.ArrayList;
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

public class CenterFragment extends BaseFragment {
    @BindView(R.id.center_user_name_tv)
    TextView userName;
    @BindView(R.id.center_login_or_continue_make)
    TextView loginOrMakeBtn;
    @BindView(R.id.center_make_num)
    TextView makeTiNum;
    @BindView(R.id.center_avg_per)
    TextView avgTxt;
    @BindView(R.id.center_user_make_info_tv)
    TextView makeTestTv;
    @BindView(R.id.async_commit_local_db)
    ImageView comImg;
    @BindView(R.id.center_user_img)
    CircleImageView headImg;
    //    @BindView(R.id.center_hint_async_make_data)
//    RelativeLayout asyncDbHint;
    private Animation mAnimation;
    private boolean viewCreate;
    private boolean asyncData;
    private int stid;
    private boolean loginsuccess;
    private boolean isAsyncDb;
    private int type;
    private QuestionBankData bankData;
    private Observable<Integer> uploadDbObs;
    private Observable<Boolean> exitObs;
    private Observable<Integer> changeObs;

    private LoginSuccessReceiver mSuccessReceiver = new LoginSuccessReceiver() {
        @Override
        protected void performAction(Intent intent) {
            if (viewCreate) {
                loginsuccess = true;
                refreshUi();
            } else {
                asyncData = false;
            }
        }
    };


//    private ExitLoginReceiver mReceiver = new ExitLoginReceiver() {
//        @Override
//        protected void performAction(Intent intent) {
//            if (viewCreate) {
//                setUi();
//            }
//        }
//    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, mReceiver.getIntentFilter());
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSuccessReceiver, mSuccessReceiver.getIntentFilter());
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mSuccessReceiver);
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_center_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setInteceptEvent(false);
        viewCreate = true;
        if (!asyncData) {
            refreshUi();
//            uploadLocalDb();
        } else {
            initMakeInfo();
        }
//        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
//            getLastRecord();
//        }
        stid = 0;
        initLocalLastRecord();
        mAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setDuration(1000);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setRepeatCount(100);
        uploadDbObs = RxBus.get().register(C.UPLOAD_DB_TOPIC_SIZE, Integer.class);
        uploadDbObs.compose(new SchedulerTransformer<Integer>()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (mAnimation != null) {
                    mAnimation.cancel();
                }
                if (integer.intValue() != 0 && makeTiNum != null)
                    makeTiNum.setText(String.valueOf(integer.intValue()));
            }
        });
        exitObs = RxBus.get().register(C.EXIT_LOGIN_ACTION, Boolean.class);
        exitObs.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                setUi();
            }
        });

        changeObs = RxBus.get().register(C.CENTER_CHANGE, Integer.class);
        changeObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {

                if (integer == C.HEADER_CHANGE) {
                    if (GlobalUser.getInstance().isAccountDataInvalid()) {
                        return;
                    }
                    String photo = GlobalUser.getInstance().getUserData().getPhoto();
                    if (!TextUtils.isEmpty(photo)) {
                        GlideUtil.load(RetrofitProvider.BASEURL + photo, headImg);
                    }
                } else if (integer == C.NICKNAME_CHANGE) {
                    setNickName(GlobalUser.getInstance().getUserData().getNickname());
                }
            }
        });
    }

    private void initLocalLastRecord() {
        Utils.setGone(makeTestTv);
        String lastRecord = SharedPref.getLastRecord(getActivity());
        type = SharedPref.getLastRecordType(getActivity());
        bankData = JsonUtil.fromJson(lastRecord, new TypeToken<QuestionBankData>() {
        }.getType());
        if (bankData == null) return;
        Utils.setVisible(makeTestTv);
        switch (type) {
            case TestType.INTELLIGENT_TEST:
                makeTestTv.setText(getString(R.string.str_center_tip_intelligent));
                break;
            default:
                makeTestTv.setText(getString(R.string.str_center_tip, bankData.getSectionStr(), bankData.getStname()));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewCreate = false;
        if (uploadDbObs != null)
            RxBus.get().unregister(C.UPLOAD_DB_TOPIC_SIZE, uploadDbObs);
        if (exitObs != null)
            RxBus.get().unregister(C.EXIT_LOGIN_ACTION, exitObs);
        if (changeObs != null)
            RxBus.get().unregister(C.CENTER_CHANGE, changeObs);
    }

    @OnClick({R.id.center_title_menu, R.id.center_login_or_continue_make, R.id.simulation_record_container, R.id.async_commit_local_db,
            R.id.center_user_img, R.id.center_title_bar_setting, R.id.wrong_record_container, R.id.center_make_record_container,
            R.id.center_msg_container, R.id.set_collection_container, R.id.center_buy_record_container})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.center_buy_record_container:
                startActivity(new Intent(getActivity(), BuyRecordActivity.class));
                break;
            case R.id.set_collection_container:
                startActivity(new Intent(getActivity(), CollectionActivity.class));
//                startActivity(new Intent(getActivity(), CollectionSlideActivity.class));
                break;
            case R.id.async_commit_local_db:
                if (GlobalUser.getInstance().isAccountDataInvalid()) {
                    toastShort(R.string.str_no_login_tip);
                    return;
                }
//                Utils.setGone(asyncDbHint);
                comImg.startAnimation(mAnimation);
                uploadLocalDb();
//                isAsyncDb = true;
//                refreshUi();
                break;
            case R.id.center_title_menu:
                ((MainActivity) getActivity()).toggle();
                break;
            case R.id.center_login_or_continue_make:
                if (!isAccountInvalid(C.LOGIN_REQUEST_CODE)) {
                    if (!Utils.isConnected(getActivity())) {
                        //没有连接网络
                        //如果是登录就去登录，如果是继续做题，就继续做题
                        String text = loginOrMakeBtn.getText().toString();
                        if (TextUtils.equals(text, getString(R.string.str_login))) {
                            Intent intent = new Intent(getActivity(), LoginAactivity.class);
                            startActivityForResult(intent, C.LOGIN_REQUEST_CODE);
                            return;
                        }
                    }
                    //账户有效 继续做题
                    if (stid != 0) {
                        TestActivity.startTestFromCenter(getActivity(), stid);
                        return;
                    }
                    if (bankData != null) {
                        TestActivity.startTestActivity(getActivity(), bankData, type);
                    }
                }
//                Intent intent = new Intent(getActivity(), LoginAactivity.class);
//                startActivityForResult(intent, C.LOGIN_REQUEST_CODE);
                break;
            case R.id.center_user_img:
//                startActivityForResult(new Intent(getActivity(), SettingActivity.class), C.SETTING_RESET_TXT_CODE);
//                break;
            case R.id.center_title_bar_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.wrong_record_container:
                startActivity(new Intent(getActivity(), WrongRecordActivity.class));
                break;
            case R.id.center_make_record_container:
                startActivity(new Intent(getActivity(), MakeRecordActivity.class));
                break;
            case R.id.simulation_record_container:
                startActivity(new Intent(getActivity(), SimulationRecordActivity.class));
                break;
            case R.id.center_msg_container:
//                startActivity(new Intent(getActivity(), MsgActivity.class));
                startActivity(new Intent(getActivity(), FileListActivity.class));
                break;
            default:
                break;
        }
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
                getLastRecord();
            }

            @Override
            public void onFail() {
                mAnimation.cancel();
            }
        });
    }

    /**
     * 本地做题，自动登录通过本地做题数来显示
     */
    private void showEvaluationDialog(String makeNum) {
        if (EvaluationProxy.topicShowEvalu(makeNum, getActivity())) {
            new EvaluationDialog().showDialog(getChildFragmentManager());
        }
//        String eval = SharedPref.getEval(getActivity());
//        if (TextUtils.equals(eval, "100") || TextUtils.equals(makeNum, "500") || TextUtils.equals(eval, "1000")) {
//            return;
//        }
//        if (TextUtils.equals(makeNum, "100") || TextUtils.equals(makeNum, "500") || TextUtils.equals(makeNum, "1000")) {
//            //显示好评
//            SharedPref.setEval(getActivity(), makeNum);
//            new EvaluationDialog().showDialog(getChildFragmentManager());
//        }
    }

    @Override
    protected void refreshUi() {
        //获取用户信息
        addToCompositeDis(HttpUtil.getUserCenterDetailInfo()
                .subscribe(new Consumer<ResultBean<UserData>>() {
                    @Override
                    public void accept(@NonNull ResultBean<UserData> bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {

                            UserData data = bean.getData();
                            String userJson = JsonUtil.toJson(data);
                            SharedPref.saveLoginInfo(getActivity(), userJson);
                            GlobalUser.getInstance().resetUserDataBySharedPref(userJson);
                            GlobalUser.getInstance().setUserData(data);
                            SharedPref.setMakeNum(getActivity(), data.getNum());
                            SharedPref.setMakeAvg(getActivity(), data.getAccuracy());
                            makeTiNum.setText(data.getNum());
                            setAvgTxt(data.getAccuracy());

                            if (isAsyncDb) {//点击同步数据
                                isAsyncDb = false;
                                return;
                            }

                            if (loginsuccess) {
                                loginsuccess = false;
                                uploadLocalDb();
                                showEvaluationDialog(data.getNum());
                            }
                            //获取个人信息后，更新用户信息
                            setUi();
                            asyncData = true;
                            PushProxy.setAlias(data.getUid());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void initMakeInfo() {
        //设置做题数和正确率
        Observable.just(1).flatMap(new Function<Integer, ObservableSource<List<Integer>>>() {
            @Override
            public ObservableSource<List<Integer>> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<Integer>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Integer>> e) throws Exception {
                        //0 表示错题数  1表示所有做题数
                        List<PracticeRecordData> errorTopic = PracticeManager.getInstance().getAllErrorTopic();//所有的错题
                        List<PracticeRecordData> makeTopic = PracticeManager.getInstance().getAllMakeTopic();//做过的所有题目

                        List<Integer> list = new ArrayList<>();
                        list.add(errorTopic.size());
                        list.add(makeTopic.size());
                        e.onNext(list);
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<List<Integer>>())
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> integers) throws Exception {
                        String makeNum = SharedPref.getMakeNum(getActivity());
                        if (!TextUtils.isEmpty(makeNum)) {//本地未做题，且未同步完成，显示存储的值
                            int allNum = Integer.parseInt(makeNum);
                            if (allNum >= integers.get((1)) && !GlobalUser.getInstance().isAccountDataInvalid()) {//表示还在同步中
                                makeTiNum.setText(makeNum);
                                setAvgTxt(SharedPref.getMakeAvg(getActivity()));
                                showEvaluationDialog(makeNum);
                                return;
                            }
                        }
                        int errorNum = integers.get(0);
                        int allNum = integers.get(1);
                        //本地做题后，来显示
                        showEvaluationDialog(String.valueOf(allNum));
//                        setAvgTxt(((int) ((allNum - errorNum) * 1.0f / allNum * 100)) + "%");
                        setAvgTxt(Utils.caclRount(allNum - errorNum, allNum) + "%");
                        makeTiNum.setText(String.valueOf(allNum));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        if (GlobalUser.getInstance().isAccountDataInvalid()) {
//                            setAvgTxt(getString(R.string.str_main_center_make_avg_num));
//                            makeTiNum.setText(getString(R.string.str_main_center_make_num));
//                            return;
//                        }
//                        UserData user = GlobalUser.getInstance().getUserData();
//                        makeTiNum.setText(user.getNum());
//                        setAvgTxt(user.getAccuracy());
                    }
                });
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            setNickName(GlobalUser.getInstance().getUserData().getNickname());
        }
    }

    private void setNickName(String nickname) {
        if (!isEmpty(nickname)) {
            userName.setText(nickname);
        } else {
            userName.setText(R.string.str_no_nick_name);
        }
    }

    private void setUi() {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            setAvgTxt(getString(R.string.str_main_center_make_avg_num));
            makeTiNum.setText(getString(R.string.str_main_center_make_num));
            //初始化数据
            userName.setText(getString(R.string.str_tourist));
            loginOrMakeBtn.setText(getString(R.string.str_login));
            Utils.setGone(makeTestTv);
            headImg.setImageResource(R.drawable.place_img);
            return;
        }
        UserData user = GlobalUser.getInstance().getUserData();
        loginOrMakeBtn.setText(R.string.str_main_center_continue_make);
        setNickName(user.getNickname());
//        if (!isEmpty(user.getNickname())) {
//            userName.setText(user.getNickname());
//        } else {
//            userName.setText(R.string.str_no_nick_name);
//        }
        if (!TextUtils.isEmpty(user.getPhone())) {
            GlideUtil.load(RetrofitProvider.BASEURL + user.getPhoto(), headImg);
        }
    }


    private void setAvgTxt(String aTxt) {
        SpannableString span = new SpannableString(aTxt);
        span.setSpan(new RelativeSizeSpan(0.7f), span.length() - 1, span.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        avgTxt.setText(span);
    }

    public void getLastRecord() {
        Utils.setGone(makeTestTv);
        addToCompositeDis(HttpUtil.getUserRecord().subscribe(new Consumer<ResultBean<UserRecord>>() {
            @Override
            public void accept(@NonNull ResultBean<UserRecord> bean) throws Exception {
                if (!getHttpResSuc(bean.getCode())) {
                    return;
                }
                Object stidObj = bean.getData().getRecord().getStid();
                if (stidObj instanceof Boolean) {
                    return;
                } else if (stidObj instanceof Integer) {
                    stid = (int) stidObj;
                    String recordMakeT = bean.getData().getUser_tikuname();
                    if (!TextUtils.isEmpty(recordMakeT)) {
                        Utils.setVisible(makeTestTv);
                        makeTestTv.setText(recordMakeT);
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                initLocalLastRecord();
            }
        }));
    }
}
