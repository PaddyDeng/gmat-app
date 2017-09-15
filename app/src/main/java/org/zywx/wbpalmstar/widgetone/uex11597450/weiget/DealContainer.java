package org.zywx.wbpalmstar.widgetone.uex11597450.weiget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


public class DealContainer extends RelativeLayout {
    public DealContainer(Context context) {
        super(context);
    }

    public DealContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DealContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DealContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int mLastXIntercept;
    private int mLastYIntercept;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) ev.getX() - mLastXIntercept;
                int deltaY = (int) ev.getY() - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {//横向滑动
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {//纵向滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastXIntercept = x;
        mLastYIntercept = y;

        return super.dispatchTouchEvent(ev);
    }
}
