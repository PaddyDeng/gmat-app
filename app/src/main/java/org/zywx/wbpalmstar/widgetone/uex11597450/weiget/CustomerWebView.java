package org.zywx.wbpalmstar.widgetone.uex11597450.weiget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.adapter.callback.OnItemClickListener;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.fromHtml;
import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.repairContent;
import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.getHtml;


public class CustomerWebView extends WebView {

    private Context mContext;
    public CustomerWebView(Context context) {
        this(context, null);
    }

    public CustomerWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        MOVE_THRESHOLD_DP = 20.0F * context.getResources().getDisplayMetrics().density;
        initWebView();
    }

    private void initWebView() {
        WebSettings mWebSettings = getSettings();
        mWebSettings.setJavaScriptEnabled(true);
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        requestFocus();
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    public void setSimulationText(String html) {
        html = repairContent(html, RetrofitProvider.BASEURL);
        setLoadWebView(getHtml(html,SharedPref.getFontSize(mContext)));
    }


//    public void setSimulationTestTitle(String html) {
//        html = repairContent(html, RetrofitProvider.BASEURL);
//        setLoadWebView(getHtml(html,SharedPref.getFontSize(mContext)));
//    }

//    public void setTestTitle(String html){
//        html = replace(fromHtml(html).toString());
//        html = repairContent(html, RetrofitProvider.BASEURL);
//        setLoadWebView(getHtml(html,SharedPref.getFontSize(mContext)));
//    }

    public void setText(String html) {
        log(html);
        html = replace(fromHtml(html).toString());
        html = repairContent(html, RetrofitProvider.BASEURL);
        setLoadWebView(getHtml(html,SharedPref.getFontSize(mContext)));
//        setLoadWebView(getHtml(replaceSpace(replace(fromHtml(contentSetHost(html)).toString()))));
    }

//    </p>\r\n\r\n<p><br />\r\n  <br />\r\n<br />\r\n  </p>\r\n\r\n<p><br />\r\n

    private String replace(String content) {
        if (content.contains("&amp;")) {
            content = content.replace("&amp;", "&");
        }
        if (content.contains("&nbsp;")) {
            content = content.replace("&nbsp;", " ");
        }
        if (content.contains("\\r\\n")) {
            content = content.replace("\\r\\n", "<br />").trim();
        }
        content = content.replaceAll("(<br />)+", "<br />");
//        if (content.contains("<p><br />")) {
//            content = content.replace("<p><br />", "<p>").trim();
//        }
        content = rep(content, regs, rep);
        if(content.contains("</p><br /><p><br />")){
            content = content.replace("</p><br /><p><br />", "</p><p>").trim();
        }

        log(content);

        return content.trim();
    }

    String[] regs = new String[]{"<(p|P)>(\\s*)<br/>", "</(p|P)>(\\s*)<br/>", "<br/>(\\s*)<(p|P)>", "<br/>(\\s*)</(p|P)>", "<(p|P)>(\\s*)</(p|P)>"};
    String[] rep = new String[]{"<p>", "</p>", "<p>", "</p>", ""};

    private String rep(String html, String[] reg, String[] rep) {
        for (int i = 0; i < reg.length; i++) {
            html = html.replaceAll(reg[i], rep[i]);
        }
        return html;
    }

    /**
     * NSArray *patternStringArray = @[@"<(p|P)>(\\s*)<br/>",
     *
     * @"</(p|P)>(\\s*)<br/>",
     * @"<br/>(\\s*)<(p|P)>",
     * @"<br/>(\\s*)</(p|P)>",
     * @"<(p|P)>(\\s*)</(p|P)>"]; NSArray *templateStringArray = @[@"];
     */


    private void setLoadWebView(String html) {
        loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (intercept) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        return super.dispatchTouchEvent(ev);
    }

    //    private boolean
    private boolean intercept = true;

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }


    private float mDownPosX;
    private float mDownPosY;
    private float mUpPosX;
    private float mUpPosY;

    private float MOVE_THRESHOLD_DP;
    private OnItemClickListener clickListener;

    private final int CLICK_ON_WEBVIEW = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CLICK_ON_WEBVIEW:
                    if (clickListener != null) {
                        clickListener.onClick(CustomerWebView.this, 0);
                    }
                    break;
            }
            return false;
        }
    });

    public void setOnCustomeClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void log(String msg) {
        Utils.logh("========", msg);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.mDownPosX = event.getX();
                this.mDownPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
                this.mUpPosX = event.getX();
                this.mUpPosY = event.getY();
                if ((Math.abs(mUpPosX - this.mDownPosX) < MOVE_THRESHOLD_DP) && (Math.abs(mUpPosY - this.mDownPosY) < MOVE_THRESHOLD_DP)) {
                    if (!mHandler.hasMessages(CLICK_ON_WEBVIEW)) {
                        mHandler.sendEmptyMessage(CLICK_ON_WEBVIEW);
                    }
                }
                break;

//            case MotionEvent.ACTION_CANCEL://被拦截了执行的语句
//                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
