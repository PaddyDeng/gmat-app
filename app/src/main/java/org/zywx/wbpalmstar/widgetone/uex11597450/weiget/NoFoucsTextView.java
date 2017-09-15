package org.zywx.wbpalmstar.widgetone.uex11597450.weiget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.MeasureUtil;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.ControlTextView;
import org.zywx.wbpalmstar.widgetone.uex11597450.weiget.font.FontCache;

public class NoFoucsTextView extends ControlTextView {

    private URLDrawable urlDrawable;
    private Context mContext;

    private Html.ImageGetter getter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            if (source.contains("data:image/png;base64,")) {
                byte[] byteIcon;
                source = source.substring("data:image/png;base64,".length());
                byteIcon = Base64.decode(source, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteIcon, 0, byteIcon.length);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                drawable.setBounds(0, 0, MeasureUtil.dip2px(mContext, drawable.getIntrinsicWidth()) * 3 / 4,
                        MeasureUtil.dip2px(mContext, drawable.getIntrinsicHeight()) * 3 / 4);
                return drawable;
            } else {
                Utils.logh("NoFoucsTextView: source   ", source);
                urlDrawable = new URLDrawable();
                Glide.with(mContext).load(RetrofitProvider.BASEURL + source).asBitmap().fitCenter().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        urlDrawable.bitmap = resource;
                        urlDrawable.setBounds(0, 0, MeasureUtil.px2dip(mContext, resource.getWidth()),
                                MeasureUtil.dip2px(mContext, resource.getHeight()));
                        invalidate();
                        setText(getText());//不加这句显示不出来图片，原因不详
                    }
                });
                return urlDrawable;
            }
        }
    };

    public class URLDrawable extends BitmapDrawable {
        public Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (bitmap != null) {
                //srcRect绘制Bitmap的哪一部分
                Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                //dstRecF绘制的Bitmap拉伸到哪里
                RectF dst = new RectF(0, 0, bitmap.getWidth() * 2, canvas.getHeight());
                canvas.drawBitmap(bitmap, src, dst, getPaint());
            }
        }
    }

    public NoFoucsTextView(Context context) {
        this(context, null);
    }

    public NoFoucsTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoFoucsTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
//        setGravity(Gravity.CENTER_VERTICAL);
//        applyCustomFont(context,attrs);
    }

//    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    private void applyCustomFont(Context context, AttributeSet attrs) {

//        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = FontCache.getTypeface(FontCache.FONT_NAME, context);
        setTypeface(customFont);
    }

//    private Typeface selectTypeface(Context context, int textStyle) {
//        /*
//        * information about the TextView textStyle:
//        * http://developer.android.com/reference/android/R.styleable.html#TextView_textStyle
//        */
//        switch (textStyle) {
//            case Typeface.BOLD: // bold
//                return FontCache.getTypeface("SourceSansPro-Bold.ttf", context);
//
//            case Typeface.ITALIC: // italic
//                return FontCache.getTypeface("SourceSansPro-Italic.ttf", context);
//
//            case Typeface.BOLD_ITALIC: // bold italic
//                return FontCache.getTypeface("SourceSansPro-BoldItalic.ttf", context);
//
//            case Typeface.NORMAL: // regular
//            default:
//                return FontCache.getTypeface("SourceSansPro-Regular.ttf", context);
//        }
//    }

    public void setOptionContent(String content) {
        int top = 0;
        int bottom = 0;
        if (content.contains("<sup>")) {
            top = 4;
        }
        if (content.contains("<sub>")) {
            bottom = 4;
        }
        setPadding(0, top, 0, bottom);
        CharSequence charSequence = Html.fromHtml(content, getter, null);
        setText(charSequence);
        setFontSize(SharedPref.getFontSize(mContext));
//        setMovementMethod(LinkMovementMethod.getInstance());
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        getParent().requestDisallowInterceptTouchEvent(false);
//        return super.dispatchTouchEvent(event);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return false;
//    }


}
