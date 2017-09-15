package org.zywx.wbpalmstar.widgetone.uex11597450.ui.make.difficultymake;

import android.os.Bundle;
import android.view.View;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

import butterknife.OnClick;

public class DiffMakeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_make);
    }

    //        1	600以下
//        2	600-650
//        3	650-680
//        4	680-700
//        5	700-730
//        6	730以上
    @OnClick({R.id.diff_one_item_container, R.id.diff_two_item_container, R.id.diff_thr_item_container,
            R.id.diff_four_item_container, R.id.diff_five_item_container, R.id.diff_six_item_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diff_one_item_container:
                DiffDetailActivity.startDiffDetail(mContext, getString(R.string.str_diff_one_item), 1);
                break;
            case R.id.diff_two_item_container:
                DiffDetailActivity.startDiffDetail(mContext, getString(R.string.str_diff_two_item), 2);
                break;
            case R.id.diff_thr_item_container:
                DiffDetailActivity.startDiffDetail(mContext, getString(R.string.str_diff_thr_item), 3);
                break;
            case R.id.diff_four_item_container:
                DiffDetailActivity.startDiffDetail(mContext, getString(R.string.str_diff_four_item), 4);
                break;
            case R.id.diff_five_item_container:
                DiffDetailActivity.startDiffDetail(mContext, getString(R.string.str_diff_five_item), 5);
                break;
            case R.id.diff_six_item_container:
                DiffDetailActivity.startDiffDetail(mContext, getString(R.string.str_diff_six_item), 6);
                break;
            default:
                break;
        }
    }
}
