package org.zywx.wbpalmstar.widgetone.uex11597450.ui.konw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.data.KnowData;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.ControlTextView;

import java.util.List;

import butterknife.BindView;

public class KnowTypeActivity extends BaseActivity {
    public static void startKnow(Context c, KnowData mKnow, int index) {
        Intent intent = new Intent(c, KnowTypeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, mKnow);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        c.startActivity(intent);
    }

    private KnowData mKnowData;
    private int index;
    @BindView(R.id.know_type_title)
    TextView titleName;
    @BindView(R.id.know_type_container)
    LinearLayout container;

    private int[] one;
    private int[] two;
    private int[] thr;
    private int[] four;
    int[] colorRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_type);
    }

    @Override
    protected void initData() {
        super.initData();
        int[] one = new int[]{R.color.color_know_one_o, R.color.color_know_one_to, R.color.color_know_one_tr, R.color.color_know_one_f};
        int[] two = new int[]{R.color.color_know_two_o, R.color.color_know_two_to, R.color.color_know_two_tr, R.color.color_know_two_f};
        int[] thr = new int[]{R.color.color_know_thr_o, R.color.color_know_thr_to, R.color.color_know_thr_tr, R.color.color_know_thr_f};
        int[] four = new int[]{R.color.color_know_four_o, R.color.color_know_four_to, R.color.color_know_four_tr, R.color.color_know_four_f};
        if (index == 0) {
            colorRes = one;
        } else if (index == 1) {
            colorRes = two;
        } else if (index == 2) {
            colorRes = thr;
        } else if (index == 3) {
            colorRes = four;
        }
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        mKnowData = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        if (mKnowData == null) {
            return;
        }
        index = getIntent().getIntExtra(Intent.EXTRA_INDEX, 0);

    }

    @Override
    protected void initView() {
        super.initView();
        String catname = mKnowData.getCatname();
        if (!TextUtils.isEmpty(catname)) {
            titleName.setText(catname);
        }
        List<KnowData.CategoryTypeBean> type = mKnowData.getCategoryType();
        if (type.isEmpty()) return;
        int i = 0;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int fontSize = SharedPref.getFontSize(mContext);
        for (final KnowData.CategoryTypeBean bean : type) {
            ControlTextView tv = (ControlTextView) inflater.inflate(R.layout.tv_layout, null, false);
            tv.setFontSize(fontSize);
            tv.setText(bean.getCatname());
            tv.setBackgroundResource(colorRes[i++]);
            container.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KnowCateListActivity.startKnowCate(mContext, bean);
                }
            });
        }
    }

}
