package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.RecycleViewGridCommissionDivider;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.CommunityData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyData;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ResultBean;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.TestInfomationData;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.adapter.CommDetailAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.GlideUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.TimeUtils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.CircleImageView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.GeneralView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CommunityDetailActivity extends BaseActivity {

    public static void startCommunity(Context c, String postId) {
        Intent intent = new Intent(c, CommunityDetailActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, postId);
        c.startActivity(intent);
    }

    private String postId;
    @BindView(R.id.conn_detail_title)
    TextView detailTitle;
    @BindView(R.id.comm_detail_view_count)
    TextView detailViewCount;
//    @BindView(R.id.comm_reply_count)
//    TextView replyCount;
    @BindView(R.id.comm_detail_post_time)
    TextView postTime;
    @BindView(R.id.comm_detail_deneral_veiw)
    GeneralView mGeneralView;
//    @BindView(R.id.comm_detail_recycler)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.remark_new_detail_me_head)
//    CircleImageView mHeadView;
//    @BindView(R.id.remark_new_me_content_et)
//    EditText newRemarkEt;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getStringExtra(Intent.EXTRA_INDEX);
        }
    }

    @Override
    protected void initView() {
//        LinearLayoutManager manager = new LinearLayoutManager(mContext);
//        manager.setAutoMeasureEnabled(true);
//        mRecyclerView.addItemDecoration(new RecycleViewGridCommissionDivider(mContext, R.drawable.remark_com_divider));
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);
//        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
//            GlideUtil.loadDefault(RetrofitProvider.BASEURL + GlobalUser.getInstance().getUserData().getPhoto(), mHeadView, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
//        }
//        newRemarkEt.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
//                    //评论
//                    asyncReplay();
//                }
//                return false;
//            }
//        });
    }

//    private void asyncReplay() {
//        String text = getEditText(newRemarkEt);
//        if (TextUtils.isEmpty(text)) {
//            return;
//        }
//        if (GlobalUser.getInstance().isAccountDataInvalid()) {
//            toastShort(R.string.str_no_login_tip);
//            return;
//        }
//        addToCompositeDis(HttpUtil.postReply(postId, text)
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        showLoadDialog();
//                    }
//                })
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        dismissLoadDialog();
//                    }
//                })
//                .subscribe(new Consumer<ResultBean>() {
//                    @Override
//                    public void accept(@NonNull ResultBean bean) throws Exception {
//                        dismissLoadDialog();
//                        toastShort(bean.getMessage());
//                        if (getHttpResSuc(bean.getCode())) {
//                            newRemarkEt.setText("");
//                            asyncUiInfo();
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                }));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
    }

    @Override
    protected void asyncUiInfo() {
//        addToCompositeDis(HttpUtil.getPostDeail(postId)
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        showLoadDialog();
//                    }
//                })
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        dismissLoadDialog();
//                    }
//                })
//                .subscribe(new Consumer<CommunityData>() {
//                    @Override
//                    public void accept(@NonNull CommunityData data) throws Exception {
//                        dismissLoadDialog();
//                        refreshUi(data);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                }));
        addToCompositeDis(HttpUtil.getTestInfomationDetail(postId)
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
                .subscribe(new Consumer<TestInfomationData>() {
                    @Override
                    public void accept(@NonNull TestInfomationData data) throws Exception {
                        dismissLoadDialog();
                        refreshUi(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    //    private void refreshUi(CommunityData data) {
//        detailTitle.setText(data.getTitle());
//        detailViewCount.setText(data.getViewCount());
//        List<ReplyData> reply = data.getReply();
//        int size = 0;
//        if (reply != null && !reply.isEmpty())
//            size = reply.size();
//        replyCount.setText(String.valueOf(size));
//        postTime.setText(getString(R.string.str_community_post, data.getDateTime()));
//        mGeneralView.setText(RetrofitProvider.GOSSIPURL, data.getContent());
////        mGeneralView.setTitle(data.get);
////        mRecyclerView.setAdapter(new CommDetailAdapter(data.getReply()));
//    }
    private void refreshUi(TestInfomationData data) {
        detailTitle.setText(data.getContenttitle());
        detailViewCount.setText(data.getViews());
//        List<ReplyData> reply = data.getReply();
//        int size = 0;
//        if (reply != null && !reply.isEmpty())
//            size = reply.size();
//        replyCount.setText(String.valueOf(size));
        postTime.setText(getString(R.string.str_community_post,
                TimeUtils.longToString(Long.parseLong(data.getContentinputtime()) * 1000, "yyyy-MM-dd")));
        mGeneralView.setTestInfomationText(RetrofitProvider.BASEURL, data.getContenttext());
//        mRecyclerView.setAdapter(new CommDetailAdapter(data.getReply()));
    }
}
