package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import butterknife.BindView;

public class DesActivity extends BaseActivity {

    @BindView(R.id.des_title)
    TextView titleTv;
    @BindView(R.id.des_content)
    TextView contentTv;

    private String title;
    private String content;

    public static void startDes(Context c, String title, String content) {
        Intent intent = new Intent(c, DesActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, title);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        title = intent.getStringExtra(Intent.EXTRA_INDEX);
        content = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void initView() {
        super.initView();
        titleTv.setText(title);
        contentTv.setText(content);
    }
}
