package org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.GlobalUser;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.ReplyData;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.RemarkMsgAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.HttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class RemarkNewMsgActivity extends BaseActivity {

    @BindView(R.id.remark_msg_recycler)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_new_msg);
    }
//    头像：userimage
//gossipId 点击进入详情页


    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void asyncUiInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", GlobalUser.getInstance().getUserData().getUid());
        addToCompositeDis(HttpUtil.replyList(map).subscribe(new Consumer<List<ReplyData>>() {
            @Override
            public void accept(@NonNull final List<ReplyData> datas) throws Exception {
                RemarkMsgAdapter adapter = new RemarkMsgAdapter(datas);
                adapter.setItemListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ReplyData data = datas.get(position);
                        RemarkDetailActivity.startRemarkDetail(RemarkNewMsgActivity.this, data.getGossipId());
                    }
                });
                mRecyclerView.setAdapter(adapter);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }));
    }
}
