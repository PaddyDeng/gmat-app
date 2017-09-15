package org.zywx.wbpalmstar.widgetone.uex11597450.weiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

/**
 * Created by fire on 2017/8/22  11:23.
 */

public class FeedItemView extends RelativeLayout {

    private String des;
    private TextView desTv;
    private CheckBox mCheckBox;

    public FeedItemView(Context context) {
        this(context, null);
    }

    public FeedItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
        addView(context);
    }

    private void addView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item_layout, null);
        desTv = (TextView) view.findViewById(R.id.feed_item_des_tv);
        mCheckBox = (CheckBox) view.findViewById(R.id.feed_item_check_box);
//        log(des);
        desTv.setText(des);
        addView(view);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    RxBus.get().post(C.TOPIC_ERROR_TAG, getDesText());
                }
            }
        });
    }

    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
    }

    public boolean isChecked() {
        return mCheckBox == null ? false : mCheckBox.isChecked();
    }

    public String getDesText() {
        return des;
    }

    private void log(String msg) {
        Utils.logh("FeedItemView ", msg);
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FeedItemView, defStyleAttr, 0);
        des = a.getString(R.styleable.FeedItemView_feedViewTxt);
        a.recycle();
    }

}
