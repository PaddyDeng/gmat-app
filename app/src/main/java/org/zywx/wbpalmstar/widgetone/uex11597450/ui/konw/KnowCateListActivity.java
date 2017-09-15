package org.zywx.wbpalmstar.widgetone.uex11597450.ui.konw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.KnowRecyclerViewAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import java.util.List;

import butterknife.BindView;

import static org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowData.CategoryTypeBean;
import static org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowData.CategoryTypeBean.CategoryContentBean;

public class KnowCateListActivity extends BaseActivity {

    public static void startKnowCate(Context c, CategoryTypeBean bean) {
        Intent intent = new Intent(c, KnowCateListActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, bean);
        c.startActivity(intent);
    }

    private CategoryTypeBean mBean;
    private List<CategoryContentBean> mContentBeens;
    @BindView(R.id.know_content_title)
    TextView contentTitle;
    @BindView(R.id.know_recycler)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_cate_content);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mBean = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void initView() {
        super.initView();
        if (!TextUtils.isEmpty(mBean.getCatname())) {
            contentTitle.setText(mBean.getCatname());
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        KnowRecyclerViewAdapter adapter = new KnowRecyclerViewAdapter(mBean.getCategoryContent(), mContext);
        adapter.setonClickListener(new KnowRecyclerViewAdapter.RecyClick() {
            @Override
            public void OnClick(View view, int position) {
                //跳转到详情页
                CategoryContentBean bean = mBean.getCategoryContent().get(position);
                KnowDetailActivity.startKnowDetail(KnowCateListActivity.this, bean);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }
}
