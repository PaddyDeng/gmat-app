package org.zywx.wbpalmstar.widgetone.uex11597450.callback;

import android.view.View;

public interface OnAdapterListener<T> {

    void onClick(View view, int position, T t);

    void onClick(View view, int position);
}
