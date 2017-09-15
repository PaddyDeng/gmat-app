package org.zywx.wbpalmstar.widgetone.uex11597450.ui.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.PopWindow;

/**
 * Created by fire on 2017/8/17  13:54.
 */

public class PopHelper {
    private Context mContext;
    private View mView;
    private PopWindow mPopWindow;

    public PopHelper(Context context) {
        this.mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.menu_item_layout, null, false);
        mPopWindow = new PopWindow(mContext, mView);
    }

    public void setListene(View.OnClickListener listene) {
        if (mView == null) return;
        mView.findViewById(R.id.pop_see_analyze).setOnClickListener(listene);
        mView.findViewById(R.id.pop_txt_size).setOnClickListener(listene);
        mView.findViewById(R.id.pop_feed_back).setOnClickListener(listene);
    }

    public void showPop(View view) {
        if (mPopWindow == null) return;
        mPopWindow.showAsDropDown(view);
    }

    public void dismiss() {
        if (mPopWindow == null) return;
        mPopWindow.dismiss();
    }

}
