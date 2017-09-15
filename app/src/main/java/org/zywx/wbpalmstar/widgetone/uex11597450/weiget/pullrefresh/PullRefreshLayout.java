package org.zywx.wbpalmstar.widgetone.uex11597450.weiget.pullrefresh;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;


public class PullRefreshLayout extends LinearLayout implements NestedScrollingParent, NestedScrollingChild {

    private PullRefreshHeader mPullRefreshHeader;//头
    private RecyclerView mRecyclerView;
    private int mHeaderViewHeight;//mPullRefreshHeader高度
    private double rate = 0.4;//阻率
    private OverScroller mScroller;
    public final static int SCROLL_DURATION = 500;//回滚时间
    private int freshHeight;//下拉多长距离刷新  一定要小于 mHeaderViewHeight
    private int headContentHeight;//头部内容高度  即回弹停留高度
    private OnRefreshListener mOnRefreshListener;
    private boolean isOnTouch = false;//是否处于触摸状态
    private boolean isExcuting = false;//是否处于执行下拉状态
    private boolean isFirst = true;
    private int mState = PullRefreshHeader.STATE_ARROWS_TO_DOWN;//状态
    private boolean finishScrollLock = false;//完成滚动锁

    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    private NestedScrollingParentHelper mNestedScrollingParentHelper;

    private NestedScrollingChildHelper mNestedScrollingChildHelper;

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        addView(
                new PullRefreshHeader(context),
                0
        );
        mScroller = new OverScroller(context);
        freshHeight = context.getResources().getDimensionPixelSize(R.dimen.pullrefresh_fresh_height);
        headContentHeight = context.getResources().getDimensionPixelSize(R.dimen.pullrefresh_head_content_height);

        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPullRefreshHeader = (PullRefreshHeader) getChildAt(0);//获取头部
        View view = getChildAt(1);//获取recycleView
        if (!(view instanceof RecyclerView)) {
            throw new RuntimeException(
                    "只支持RecyclerView");
        }
        mRecyclerView = (RecyclerView) view;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //不限制顶部的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
        params.height = getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(), mPullRefreshHeader.getMeasuredHeight() + mRecyclerView.getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeaderViewHeight = mPullRefreshHeader.getMeasuredHeight();
        initView();
    }


    private void initView() {
        if (isFirst)
            scrollTo(mHeaderViewHeight, false);
        isFirst = false;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mHeaderViewHeight) {
            y = mHeaderViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
        if (y >= mHeaderViewHeight) {
            finishScrollLock = false;//解锁
        }
    }

    /**
     * onStartNestedScroll
     * 需要研究
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        return true;
        return isEnabled() && !finishScrollLock
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp(target)) {
//            canRefresh = true;
        } else {
//            canRefresh = false;
        }
    }

//    private boolean canRefresh = false;

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * @param target   在这里是recyclerview
     * @param dx
     * @param dy       向下滑动大于零
     * @param consumed re滑动时getscrolly固定。 pu滑动，getscrolly变化
     *                 ViewCompat.canScrollVertically（target,-1)=》 检测的是target是否可向上滑动，true可以。反之。
     *                 ViewCompat.canScrollVertically（target,1) =》 检测的是target是否可向下滑动，true可以。反之。
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hiddenTop = dy > 0 && getScrollY() < mHeaderViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !ViewCompat.canScrollVertically(target, -1);
        if (!finishScrollLock && (isOnTouch && isExcuting) && (hiddenTop || showTop)/* && canRefresh*/) {

            if (dy < -1)
                dy = (int) (dy * rate + 0.5);
            scrollBy(0, dy);//控制速度
            consumed[0] = 0;
            consumed[1] = dy;
        }
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }


    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //down - //up+
//        if (getScrollY() >= mHeaderViewHeight) return false;
//        return true;
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (finishScrollLock) {
            return super.dispatchTouchEvent(ev);
        }
        String action = "";
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                isOnTouch = true;
                isExcuting = true;
                break;

            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";

                if (getScrollY() <= mHeaderViewHeight - freshHeight) {
                    execute(PullRefreshHeader.STATE_ARROWS_TO_TOP);
                } else {
                    execute(PullRefreshHeader.STATE_ARROWS_TO_DOWN);
                }
                break;
            // case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";

                //退回
                if (getScrollY() < mHeaderViewHeight) {

                    if (mState == PullRefreshHeader.STATE_FRESHING || mPullRefreshHeader.getState() == PullRefreshHeader.STATE_FRESHING) {//正在刷新中
                        if (getScrollY() <= mHeaderViewHeight - freshHeight) {
                            scrollTo(mHeaderViewHeight - headContentHeight, true);
                        } else {

                        }
                    } else {
                        if (getScrollY() <= mHeaderViewHeight - freshHeight) {//满足刷新条件
                            execute(PullRefreshHeader.STATE_FRESHING);
                        } else {
                            execute(PullRefreshHeader.ACTION_FINISH);
                        }
                    }


                }
                isOnTouch = false;
                break;

            default:
                action = "null";
        }
        //    Log.e("onInterceptTouchEvent", action + ":" + ev.getAction() + ",result: " + super.dispatchTouchEvent(ev));
        return super.dispatchTouchEvent(ev);
    }


    //转态改变
    private void execute(int state) {
        myHandler.sendEmptyMessage(state);

    }

    //完成刷新
    public void finishRefresh() {
        finishScrollLock = true;
        scrollTo(mHeaderViewHeight, true);
        mState = PullRefreshHeader.STATE_ARROWS_TO_DOWN;
    }

    //用于状态处理
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PullRefreshHeader.ACTION_FINISH:
                    scrollTo(mHeaderViewHeight, true);
                    mState = PullRefreshHeader.STATE_ARROWS_TO_DOWN;
                    break;
                case PullRefreshHeader.STATE_FRESHING:
                    scrollTo(mHeaderViewHeight - headContentHeight, true);
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.refresh();
                    }
                    mState = PullRefreshHeader.STATE_FRESHING;

                    Log.e("STATE_FRESHING", "refresh");
                    break;
                case PullRefreshHeader.STATE_ARROWS_TO_TOP:
                    mState = PullRefreshHeader.STATE_ARROWS_TO_TOP;
                    break;
                case PullRefreshHeader.STATE_ARROWS_TO_DOWN:
                    mState = PullRefreshHeader.STATE_ARROWS_TO_DOWN;
                    break;
                default:

            }
            mPullRefreshHeader.setState(msg.what);
        }
    };

    //滚到到某个位置
    private void scrollTo(int endY, boolean isAnimation) {
        if (isAnimation) {
            mScroller.startScroll(0, getScrollY(), 0, endY - getScrollY(), SCROLL_DURATION);
            invalidate();
        } else {
            scrollTo(0, endY);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
            //状态复原
            if (mScroller.getCurrY() >= mHeaderViewHeight) {
                mPullRefreshHeader.initViewState();
                isExcuting = false;
            }

        }
    }

    public void setOnRefreshListener(OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }


    public interface OnRefreshListener {
        void refresh();
    }

//    =============================== NestedScrollingChild 实现的方法  ===========================================

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable @Size(value = 2) int[] consumed, @Nullable @Size(value = 2) int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(
                dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
