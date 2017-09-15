package org.zywx.wbpalmstar.widgetone.uex11597450.weiget;

import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.ControlTextView;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.fromHtml;


public class NetParView extends RelativeLayout {

    private ControlTextView desTxt;
    private ControlTextView titTxt;
    private Context mContext;

    public NetParView(Context context) {
        this(context, null);
    }

    public NetParView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetParView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.corrent_des_item_layout, null, false);
        desTxt = (ControlTextView) view.findViewById(R.id.corrent_des_tv);
        titTxt = (ControlTextView) view.findViewById(R.id.corrent_title_tv);
        int fontSize = SharedPref.getFontSize(mContext);
        desTxt.setFontSize(fontSize);
        titTxt.setFontSize(fontSize);
        addView(view);
    }

    public void setNetParContent(String timeStr, int userId, String des) {
        String[] time = timeStr.split(" ");
        titTxt.setText(fromHtml(mContext.getString(R.string.str_test_user_des, userId, time[0])));
//                generalView.setText(netP.getP_content());
        String content = fromHtml(des).toString();
        if (content.contains("\\n")) {
            content = content.replace("\\n", "<br/>");
        }
        Spanned spanned = fromHtml(content);
        desTxt.setText(spanned);
    }

}
