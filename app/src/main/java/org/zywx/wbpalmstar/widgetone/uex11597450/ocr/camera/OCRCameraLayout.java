/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package org.zywx.wbpalmstar.widgetone.uex11597450.ocr.camera;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.R;


public class OCRCameraLayout extends FrameLayout {

    public static int ORIENTATION_PORTRAIT = 0;
    public static int ORIENTATION_HORIZONTAL = 1;

    private int orientation = ORIENTATION_PORTRAIT;
    private View contentView;
    private View centerView;
    private View leftDownView;
    private View rightUpView;
    private View contentLine;
    private View topView;
    private View desView;

    private int contentViewId;
    private int centerViewId;
    private int leftDownViewId;
    private int rightUpViewId;
    private int contentLineId;
    private int topViewId;
    private int desViewId;

    public void setOrientation(int orientation) {
        if (this.orientation == orientation) {
            return;
        }
        this.orientation = orientation;
        requestLayout();
    }

    public OCRCameraLayout(Context context) {
        super(context);
    }

    public OCRCameraLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttrs(attrs);
    }

    public OCRCameraLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(attrs);
    }

    {
        setWillNotDraw(false);
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.OCRCameraLayout,
                0, 0);
        try {
            contentViewId = a.getResourceId(R.styleable.OCRCameraLayout_contentView, -1);
            centerViewId = a.getResourceId(R.styleable.OCRCameraLayout_centerView, -1);
            leftDownViewId = a.getResourceId(R.styleable.OCRCameraLayout_leftDownView, -1);
            rightUpViewId = a.getResourceId(R.styleable.OCRCameraLayout_rightUpView, -1);
            contentLineId = a.getResourceId(R.styleable.OCRCameraLayout_matchLineView, -1);
            topViewId = a.getResourceId(R.styleable.OCRCameraLayout_topLeftView, -1);
            desViewId = a.getResourceId(R.styleable.OCRCameraLayout_centerDesView, -1);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        contentView = findViewById(contentViewId);
        if (centerViewId != -1) {
            centerView = findViewById(centerViewId);
        }
        if (desViewId != -1) {
            desView = findViewById(desViewId);
        }
        if (topViewId != -1) {
            topView = findViewById(topViewId);
        }
        if (contentLineId != -1) {
            contentLine = findViewById(contentLineId);
        }
        leftDownView = findViewById(leftDownViewId);
        rightUpView = findViewById(rightUpViewId);
    }

    private Rect backgroundRect = new Rect();
    private Paint paint = new Paint();

    {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.TRANSPARENT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int height = getHeight();
        int left;
        int top;

        MarginLayoutParams leftDownViewLayoutParams = (MarginLayoutParams) leftDownView.getLayoutParams();
        MarginLayoutParams rightUpViewLayoutParams = (MarginLayoutParams) rightUpView.getLayoutParams();
        MarginLayoutParams topViewLayoutParams = null;
        if (topView != null) {
            topViewLayoutParams = (MarginLayoutParams) topView.getLayoutParams();
        }
        if (r < b) {
            hide(l, t, r, width, height, leftDownViewLayoutParams, rightUpViewLayoutParams);
        } else {
            int contentWidth = height * 4 / 3;
            int widthLeft = width - contentWidth;
            contentView.layout(l, t, width, height);
            if (contentLine != null) {
                contentLine.layout(l, t, width, height);
            }


            backgroundRect.left = contentWidth;
            backgroundRect.top = 0;
            backgroundRect.right = width;
            backgroundRect.bottom = height;

            if (desView != null) {
                left = (width - desView.getMeasuredWidth()) / 2;
                top = (height - desView.getMeasuredHeight()) / 2;
                desView.layout(left, top, left + desView.getMeasuredWidth(), top + desView.getMeasuredHeight());
            }

            // layout centerView
            if (centerView != null) {
                left = contentWidth + (widthLeft - centerView.getMeasuredWidth()) / 2;
                top = (height - centerView.getMeasuredHeight()) / 2;
                centerView
                        .layout(left, top, left + centerView.getMeasuredWidth(), top + centerView.getMeasuredHeight());
            }
            // layout leftDownView
            left = contentWidth + (widthLeft - leftDownView.getMeasuredWidth()) / 2;
            top = height - leftDownView.getMeasuredHeight() - leftDownViewLayoutParams.bottomMargin;
            leftDownView
                    .layout(left, top, left + leftDownView.getMeasuredWidth(), top + leftDownView.getMeasuredHeight());

            if (topView != null && topViewLayoutParams != null) {
                left = topView.getMeasuredWidth() / 2;
                top = height - topView.getMeasuredHeight() - topViewLayoutParams.bottomMargin;
                topView.layout(left, top, left + topView.getMeasuredWidth(), top + topView.getMeasuredHeight());
            }

            // layout rightUpView
            left = contentWidth + (widthLeft - rightUpView.getMeasuredWidth()) / 2;

            top = rightUpViewLayoutParams.topMargin;
            rightUpView.layout(left, top, left + rightUpView.getMeasuredWidth(), top + rightUpView.getMeasuredHeight());


        }
    }

    private void hide(int l, int t, int r, int width, int height, MarginLayoutParams leftDownViewLayoutParams, MarginLayoutParams rightUpViewLayoutParams) {
        int left;
        int top;
        int contentHeight = width * 4 / 3;
        int heightLeft = height - contentHeight;
        contentView.layout(l, t, r, contentHeight);

        backgroundRect.left = 0;
        backgroundRect.top = contentHeight;
        backgroundRect.right = width;
        backgroundRect.bottom = height;

        // layout centerView;
        if (centerView != null) {
            left = (width - centerView.getMeasuredWidth()) / 2;
            top = contentHeight + (heightLeft - centerView.getMeasuredHeight()) / 2;
            centerView
                    .layout(left, top, left + centerView.getMeasuredWidth(), top + centerView.getMeasuredHeight());
        }
        // layout leftDownView

        left = leftDownViewLayoutParams.leftMargin;
        top = contentHeight + (heightLeft - leftDownView.getMeasuredHeight()) / 2;
        leftDownView
                .layout(left, top, left + leftDownView.getMeasuredWidth(), top + leftDownView.getMeasuredHeight());
        // layout rightUpView
        left = width - rightUpView.getMeasuredWidth() - rightUpViewLayoutParams.rightMargin;
        top = contentHeight + (heightLeft - rightUpView.getMeasuredHeight()) / 2;
        rightUpView.layout(left, top, left + rightUpView.getMeasuredWidth(), top + rightUpView.getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(backgroundRect, paint);
    }
}
