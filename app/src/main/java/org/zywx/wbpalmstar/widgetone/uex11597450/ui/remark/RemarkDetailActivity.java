package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.listener.PriaseListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.RemarkData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.RemarkComAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.RecycleViewLinearDivider;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.PhotoInfo;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.UserData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.MultiImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class RemarkDetailActivity extends BaseActivity {
    @BindView(R.id.remark_head_img)
    ImageView headImg;
    @BindView(R.id.remark_user_name)
    TextView userName;
    @BindView(R.id.remark_time)
    TextView comTime;
    @BindView(R.id.remark_title)
    TextView comTitle;
    @BindView(R.id.remark_content)
    TextView comContent;
    @BindView(R.id.remark_multiImagView)
    MultiImageView mMultiImageView;
    @BindView(R.id.remark_comm_praise)
    TextView comPraise;
    @BindView(R.id.remark_comm_write)
    TextView comWrite;
    private RemarkData mRemarkData;
    @BindView(R.id.remakr_detail_recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.remark_me_head)
    ImageView meHeadIv;
    @BindView(R.id.remark_me_content_et)
    EditText et;
    private String id;
    private boolean formMsgList;//来自消息列表
    private int floorIndex;

    public static void startRemarkDetail(Activity c, String id, int requestCode) {
        Intent intent = new Intent(c, RemarkDetailActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        c.startActivityForResult(intent, requestCode);
    }

    /**
     * 从消息列表过来的
     */
    public static void startRemarkDetail(Activity c, String id) {
        Intent intent = new Intent(c, RemarkDetailActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        intent.putExtra(Intent.EXTRA_TEXT, true);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_detail);
    }

    @Override
    protected void asyncUiInfo() {
        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil.getRemarkDetail(id).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                showLoadDialog();
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                dismissLoadDialog();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                dismissLoadDialog();
            }
        }).subscribe(new Consumer<RemarkData>() {
            @Override
            public void accept(@NonNull RemarkData remarkData) throws Exception {
                mRemarkData = remarkData;
                initView();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }));
    }

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        id = intent.getStringExtra(Intent.EXTRA_INDEX);
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            formMsgList = intent.getBooleanExtra(Intent.EXTRA_TEXT, false);
        }
    }

    @OnClick({R.id.remark_comm_write})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remark_comm_write:
                et.requestFocus();
                Utils.keyBordShowFromWindow(mContext, et);
                et.setHint(getString(R.string.str_remark_detail_et_hint));
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            //设置头像
            GlideUtil.loadDefault(RetrofitProvider.BASEURL + GlobalUser.getInstance().getUserData().getPhoto(), meHeadIv, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        }

        Utils.controlTvFocus(comTime);
        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    //评论
                    String hint = et.getHint().toString();
                    if (hint.contains("@")) {
                        replyFloor();
                    } else {
                        asyncPostReplay();
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected boolean preBackExitPage() {
        if (!formMsgList) {
            setResult(RESULT_OK);
        }
        return super.preBackExitPage();
    }

    private void asyncPostReplay() {
        String content = getEditText(et);
        if (TextUtils.isEmpty(content) || mRemarkData == null) {
            return;
        }
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            toastShort(R.string.str_no_login_tip);
            return;
        }
        UserData data = GlobalUser.getInstance().getUserData();
        String uName = data.getNickname();
        if (TextUtils.isEmpty(uName)) {
            uName = data.getUsername();
        }
        addToCompositeDis(HttpUtil.reply(content, mRemarkData.getId(), mRemarkData.getUid(), uName, data.getPhoto())
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
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        if (loginExpired(bean)) {//登录过期去登陆
                            againLogin(new ICallBack() {
                                @Override
                                public void onFail() {
                                    dismissLoadDialog();
                                }

                                @Override
                                public void onSuccess(Object o) {
                                    asyncPostReplay();
                                }
                            });
                        } else {//评论结果
                            et.setText("");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REMARK_REFRESH_ACTION));
                            dismissLoadDialog();
                            asyncUiInfo();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    protected void initView() {
        if (mRemarkData == null) {
            return;
        }
        String icon = Utils.split(mRemarkData.getIcon());
        String headUrl = RetrofitProvider.BASEURL + icon;
        GlideUtil.loadDefault(headUrl, headImg, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        userName.setText(mRemarkData.getPublisher());
        comTime.setText(TimeUtils.longToString(Long.parseLong(mRemarkData.getCreateTime()) * 1000, "yyyy.MM.dd HH:mm:ss"));
        if (TextUtils.isEmpty(mRemarkData.getTitle())) {
            Utils.setGone(comTitle);
        } else {
            Utils.setVisible(comTitle);
            comTitle.setText(mRemarkData.getTitle());
        }
        if (TextUtils.isEmpty(mRemarkData.getContent())) {
            Utils.setGone(comContent);
        } else {
            Utils.setVisible(comContent);
            comContent.setText(mRemarkData.getContent().replace("&nbsp;", " "));
        }
        comPraise.setText(mRemarkData.getLikeNum());
        comPraise.setOnClickListener(new PriaseListener(mContext, mRemarkData));
        comWrite.setText(String.valueOf(mRemarkData.getReply().size()));
//        List<String> images = mRemarkData.getImage();
        Object obj = mRemarkData.getImage();
        List<String> images = new ArrayList<>();
        if (obj != null) {
            if (obj instanceof String) {
                images.add((String) obj);
            } else if (obj instanceof List) {
                images.addAll((List<String>) obj);
            }
        }
        if (images != null && !images.isEmpty()) {
            Utils.setVisible(mMultiImageView);
            final List<PhotoInfo> photos = new ArrayList<>();
            for (String url : images) {
                PhotoInfo pInfo = new PhotoInfo();
                pInfo.url = RetrofitProvider.GOSSIPURL + Utils.split(url);
                photos.add(pInfo);
            }
            mMultiImageView.setList(photos);
            mMultiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                    List<String> photoUrls = new ArrayList<String>();
                    for (PhotoInfo photoInfo : photos) {
                        photoUrls.add(photoInfo.url);
                    }
                    ImagePagerActivity.startImagePagerActivity(mContext, photoUrls, position, imageSize);
                }
            });
        } else {
            Utils.setGone(mMultiImageView);
        }

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(mContext, LinearLayoutManager.VERTICAL, R.drawable.remark_com_divider));

        manager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        RemarkComAdapter adapter = new RemarkComAdapter(mContext, mRemarkData.getReply());
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //让et获得焦点。提示@某人
                et.requestFocus();
                Utils.keyBordShowFromWindow(mContext, et);
                floorIndex = position;
                et.setHint("@" + mRemarkData.getReply().get(position).getuName());
            }
        });
        Utils.setVisible(mRecyclerView);
        if (mRemarkData.getReply() == null || mRemarkData.getReply().isEmpty()) {
            Utils.setGone(mRecyclerView);
        }

    }

    private void replyFloor() {
        String content = getEditText(et);
        if (TextUtils.isEmpty(content)) {
            return;
        }
        List<ReplyBean> reply = mRemarkData.getReply();
        if (reply == null || reply.isEmpty()) return;
        UserData data = GlobalUser.getInstance().getUserData();
        String name = data.getUsername();
        if (!TextUtils.isEmpty(data.getNickname())) {
            name = data.getNickname();
        }
        ReplyBean bean = reply.get(floorIndex);
        addToCompositeDis(HttpUtil.replyFloor(content, mRemarkData.getId(), mRemarkData.getUid(), name, data.getPhoto(), bean.getUid(), bean.getuName())
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
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.REMARK_REFRESH_ACTION));
                            asyncUiInfo();
                            et.setText("");
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }
}
