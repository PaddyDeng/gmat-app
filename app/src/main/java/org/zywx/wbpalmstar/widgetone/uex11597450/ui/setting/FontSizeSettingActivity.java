package org.zywx.wbpalmstar.widgetone.uex11597450.ui.setting;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.ViewTreeObserver;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.RxBus;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.ControlTextView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.FontControlContainer;

import butterknife.BindView;

public class FontSizeSettingActivity extends BaseActivity implements FontControlContainer.FontSizeChangeListener {

    @BindView(R.id.change_font_size_container)
    FontControlContainer changeFontSize;
    @BindView(R.id.simple_word)
    ControlTextView mControlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size_setting);
    }

    @Override
    protected void initView() {
        int index = SharedPref.getFontSize(mContext);
        changeFontSize.setThumbLeftMargin(index);
        mControlTextView.setFontSize(index);
    }

    @Override
    protected void initData() {
        changeFontSize.setFontSizeChangeListener(this);
    }

    @Override
    public void fontSize(int index) {
        SharedPref.saveFontSize(mContext, index);
        mControlTextView.setFontSize(index);
        RxBus.get().post(C.FONT_SIEZ_CHANGE, true);
    }
}
