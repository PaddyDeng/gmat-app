package org.zywx.wbpalmstar.widgetone.uex11597450.weiget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ClickCallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.http.RetrofitProvider;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.common.DealActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.ImagePagerActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.adapter.DownloadListAdapter;
import org.zywx.wbpalmstar.widgetone.uex11597450.ui.remark.community.DownloadListActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.SharedPref;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.fromHtml;
import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.replaceSpace;
import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.repairContent;
import static org.zywx.wbpalmstar.widgetone.uex11597450.utils.HtmlUtil.getHtml;

public class GeneralView extends RelativeLayout {
    private LayoutInflater layoutInflater;
    private WebView webView;
    private Context mContext;
    private List<String> titles;//下载所需
    private List<String> links;//下载所需
    private int isReply;//1是可以下载的。
    private ClickCallBack<String> mClickCallBack;

    public GeneralView(Context context) {
        this(context, null);
    }

    public GeneralView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeneralView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.comment_view_layout, null, false);
        webView = (WebView) view.findViewById(R.id.comment_web_view);
        addView(view);
        initWebView();
    }

    public void setSimulation(String html) {
        html = repairContent(html, RetrofitProvider.BASEURL);
        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
    }

    //    public void setSimulationTestQuestion(String html) {
//        html = repairContent(html, RetrofitProvider.BASEURL);
//        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
//    }
    public void setGmatDetailText(String html) {
        html = html.replace("text-indent: 2em;", "");
        html = replaceSpace(replace(fromHtml(html).toString()));
        html = repairContent(html, RetrofitProvider.BASEURL);
        html = rep(html);
        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
    }

    public void setText(String html) {
        html = replaceSpace(replace(fromHtml(html).toString()));
        html = repairContent(html, RetrofitProvider.BASEURL);
        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
    }

    public void setHighDetalText(String html) {
        html = repairContent(html, RetrofitProvider.BASEURL);
        setLoadWebView(getHtml(rep(html)));
    }

//    public void setTestQuestion(String html) {
//        html = replaceSpace(replace(fromHtml(html).toString()));
//        html = repairContent(html, RetrofitProvider.BASEURL);
//        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
//    }

    //      <a style="font-size:14px; color:#0066cc;" href="/files/attach/file/20170705/1499241703280757.pdf"title="7.1换库数学鸡精整理（更新至第47题.pdf">7.1换库数学鸡精整理（更新至第47题.pdf</a>===0===
//            log(matcher.group(0)+"===0===");
//      "/files/attach/file/20170705/1499241703280757.pdf"===1===
//            log(matcher.group(1)+"===1===");
//      /files/attach/file/20170705/1499241703280757.pdf===2===
//            log(matcher.group(2)+"===2===");
//      null===3===
//            log(matcher.group(3)+"===3===");
//      null===4===
//            log(matcher.group(4)+"===4===");
//      7.1换库数学鸡精整理（更新至第47题.pdf===5===
//            log(matcher.group(5)+"===5===");
    private String repairHref(String replaceHttp, String html) {
        String patternStr = "<a[^>]*href=(\\\"([^\\\"]*)\\\"|\\'([^\\']*)\\'|([^\\\\s>]*))[^>]*>(.*?)</a>";
        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        String result = html;
        titles = new ArrayList<>();
        links = new ArrayList<>();
        while (matcher.find()) {
            titles.add(matcher.group(5));
            String src = matcher.group(2);
            links.add(src);
            String replaceSrc = "";
//            if (src.lastIndexOf(".") > 0) {
//                replaceSrc = src.substring(0, src.lastIndexOf(".")) + src.substring(src.lastIndexOf("."));
//            }
            if (!src.startsWith("http://") && !src.startsWith("https://")) {
                replaceSrc = replaceHttp + src;
            } else {
                replaceSrc = src;
            }
            result = result.replaceAll(src, replaceSrc);
        }
        return result;
    }


    public void setTestInfomationText(String replaceHost, String html) {
        String result = repairContent(fromHtml(html).toString(), replaceHost);
        result = repairHref(replaceHost, result);
        result = rep(result.replace("&nbsp;", " "));
        setLoadWebView(getHtml(result, SharedPref.getFontSize(mContext)));
    }

    //        <p><span style="font-size: 12px; font-family: 微软雅黑, &#39;Microsoft YaHei&#39;;"><br/></span></p>
//        <p><br/></p><p></p><p></p>
    String[] regs = new String[]{
            "<p><span ([^>]*)>\\s*<br/>\\s*</span></p>",
            "<p><span ([^>]*)>\\s*</span>\\s*<br/>\\s*</p>",
            "<p ([^>]*)><span ([^>]*)>\\s*<br/>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<br ([^>]*)>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<br ([^>]*)>\\s*<span ([^>]*)>\\s*</span>\\s*</p>",
            "(<p><br/></p>)+",
            "(<p></p>)+",
            "<p><span ([^>]*)>\\s*</span></p>",
            "<p><span ([^>]*)></span>\\s*</p>",
            "(<p ([^>]*)>\\s*<br/>\\s*</p>)+",
            "<p ([^>]*)>\\s*<br ([^>]*)>\\s*</p>",
            "(<p>\\s*<br ([^>]*)>\\s*</p>)+",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong>\\s*<br ([^>]*)>\\s*</strong>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong>\\s*<br/>\\s*</strong>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong ([^>]*)>\\s*<br/>\\s*</strong>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong ([^>]*)><span ([^>]*)>\\s*<br ([^>]*)>\\s*</span>\\s*</strong>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong><span ([^>]*)>\\s*<br ([^>]*)>\\s*</span>\\s*</strong>\\s*</span>\\s*</p>"
    };
//    String[] rep = new String[]{"", "", "", "", "", "", "", ""};

    private String rep(String html) {
        for (int i = 0; i < regs.length; i++) {
            html = html.replaceAll(regs[i], "");
        }
        log(html);
        return html;
    }

    private void log(String msg) {
        Utils.logh("GeneralView", msg);
    }

    public void setText(String replaceHost, String html) {
        String result = repairContent(html, replaceHost);
        result = repairHref(replaceHost, result);
        setLoadWebView(getHtml(result, SharedPref.getFontSize(mContext)));
    }

    public void setTitle(int isReply) {
        this.isReply = isReply;
    }

    public void setKnowDetailText(String html, int fontSize) {
        setText(RetrofitProvider.BASEURL, html, fontSize);
    }

    private void setText(String replaceHost, String html, int fontSize) {
        String result = repairContent(html, replaceHost);
        setLoadWebView(getHtml(result, fontSize));
    }

    public String rS(String content) {
        if (content.contains("&nbsp;")) {
            content = content.replace("&nbsp;", " ");
        }
        return content.trim();
    }


    private String replace(String html) {
        if (html.contains("\\n")) {
            return html.replace("\\n", "<br/>");
        }
        return html;
    }


    private void setLoadWebView(String html) {
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    private void initWebView() {
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
//        mWebSettings.setBlockNetworkImage(false);
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebSettings.setUseWideViewPort(true);
//        mWebSettings.setLoadWithOverviewMode(true);
        webView.requestFocus();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                addImageClickListner();
                addGeneralViewHeight();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (selfDeal(url)) {
//                    mContext.startActivity(new Intent(mContext, DownloadListActivity.class));
                    if (titles != null && links != null && !links.isEmpty() && !titles.isEmpty()) {
                        int index = -1;
                        for (int i = 0, size = links.size(); i < size; i++) {
                            if (url.contains(links.get(i))) {
                                index = i;
                                break;
                            }
                        }
                        if (index != -1) {
                            if (isReply == 1) {
                                DownloadListActivity.startDownloadAct(mContext, url, titles.get(index));
                            } else {
                                if (mClickCallBack != null) {
                                    mClickCallBack.onClick(url);
                                }
                            }
                        }
                    }
                    return true;
                } else {
                    DealActivity.startDealActivity(mContext, "", url);
                    return true;
                }
//                view.loadUrl(url);
//                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.addJavascriptInterface(new JavascriptInterface(mContext), "imagelistner");
        webView.addJavascriptInterface(new GeneralViewHeight(), "App");
    }

    private void addGeneralViewHeight() {
        webView.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
    }

    // js通信接口
    public class GeneralViewHeight {

        public GeneralViewHeight() {
        }

        @android.webkit.JavascriptInterface
        public void resize(final float height) {
            ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //此处的 layoutParmas 需要根据父控件类型进行区分，这里为了简单就不这么做了
                    webView.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels,
                            (int) (Math.ceil(height * getResources().getDisplayMetrics().density)) + 50));
                }
            });
        }
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String imgUrl) {
            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(-2, -2);
            List<String> imgs = new ArrayList<>();
            imgs.add(imgUrl);
            ImagePagerActivity.startImagePagerActivity(context, imgs, 0, imageSize);
//            Intent intent = new Intent();
//            intent.putExtra("image", imgUrl);
//            intent.setClass(context, ShowWebImageActivity.class);
//            context.startActivity(intent);
        }
    }

    public void setOnClickListener(ClickCallBack<String> clickCallBack) {
        mClickCallBack = clickCallBack;
    }

    private boolean selfDeal(String url) {
        url = url.toLowerCase();
        List<String> res = new ArrayList<>();
        res.add(".doc");
        res.add(".docx");
        res.add(".xls");
        res.add(".xlsx");
        res.add(".pdf");
        res.add(".ppt");
        res.add(".pptx");
        res.add(".mp3");
        res.add(".mp4");
        res.add(".txt");
        for (String r : res) {
            if (url.endsWith(r)) {
                return true;
            }
        }
        return false;
    }

}
