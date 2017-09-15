package org.zywx.wbpalmstar.widgetone.uex11597450.weiget.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import org.zywx.wbpalmstar.widgetone.uex11597450.GmatApplication;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;

public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = R.color.color_blue;
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = GmatApplication.getInstance().getResources().getColor(DEFAULT_COLOR_ID);
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }
}
