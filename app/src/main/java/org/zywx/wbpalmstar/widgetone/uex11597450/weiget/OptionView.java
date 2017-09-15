package org.zywx.wbpalmstar.widgetone.uex11597450.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.ControlTextView;


public class OptionView extends RelativeLayout {

    private LayoutInflater layoutInflater;
    private ControlTextView optionTv;
    private NoFoucsTextView OptionContent;
    private LinearLayout mContentContainer;

    public OptionView(Context context) {
        this(context, null);
    }

    public OptionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.new_test_option_item_layout, null, false);
        OptionContent = (NoFoucsTextView) view.findViewById(R.id.option_content);
        optionTv = (ControlTextView) view.findViewById(R.id.new_option_sele_tv);
        mContentContainer = (LinearLayout) view.findViewById(R.id.new_option_container_ll);
        addView(view);
    }

    public void setErrorBg() {
        optionTv.setBackgroundResource(R.drawable.option_bg_red);
        mContentContainer.setBackgroundResource(R.drawable.option_error_bg);
    }

    public void setDetailBg() {
        optionTv.setBackgroundResource(R.drawable.answer_detail_bg_green);
        mContentContainer.setBackgroundResource(R.drawable.option_corr_bg);
    }

    public void setSelectedBg(boolean selected) {
        optionTv.setSelected(selected);
        mContentContainer.setSelected(selected);
    }

    public void setSimulationText(String option, String content) {
        optionTv.setText(option);
//        int fontSize = SharedPref.getFontSize(mContext);
//        optionTv.setFontSize(fontSize);
//        optionTv.setOptionWidthH(fontSize);
        //        mNoFoucsWebView.setSimulationText(content);
        OptionContent.setOptionContent(content.trim());
    }


}
