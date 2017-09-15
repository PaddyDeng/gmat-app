package org.zywx.wbpalmstar.widgetone.uex11597450.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.zywx.wbpalmstar.widgetone.uex11597450.base.BaseActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.callback.ICallBack;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.C;
import org.zywx.wbpalmstar.widgetone.uex11597450.MainActivity;
import org.zywx.wbpalmstar.widgetone.uex11597450.R;
import org.zywx.wbpalmstar.widgetone.uex11597450.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class DealActivity extends BaseActivity {

    public static void startDealActivity(Context activity, String titleName, String url, int type) {
        Intent intent = new Intent(activity, DealActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, titleName);
        intent.putExtra(Intent.EXTRA_INDEX, url);
        intent.putExtra(C.DEAL_TYPE, type);
        activity.startActivity(intent);
    }

    public static void startDealActivity(Context activity, String titleName, String url) {
//        Intent intent = new Intent(activity, DealActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, titleName);
//        intent.putExtra(Intent.EXTRA_INDEX, url);
//        activity.startActivity(intent);
        startDealActivity(activity, titleName, url, 0);//默认的类型，其余的见C
    }

    @BindView(R.id.deal_title)
    TextView tvCenter;
    @BindView(R.id.deal_more_content_title)
    TextView moreContentTitle;
    @BindView(R.id.deal_webview)
    WebView webView;
    @BindView(R.id.deal_progress_bar)
    ProgressBar mProgressBar;

    private String mTitleName;
    private String mUrl;
    private int type;
    private Map<String, String> map;
    private InternetBusyTipDialog mDialog;
    private boolean showing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent != null) {
            mUrl = intent.getStringExtra(Intent.EXTRA_INDEX);
            mTitleName = intent.getStringExtra(Intent.EXTRA_TEXT);
            type = intent.getIntExtra(C.DEAL_TYPE, 0);
        }
    }

    @Override
    protected void initData() {

        if (type == C.DEAL_ADD_HEADER) {//这个类型是在请求头里面添加referer
            map = new HashMap<>();
            map.put("Referer", "http://www.gmatonline.cn/");
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                  http://static.gensee.com/webcast/static/zh_CN/ipad-error.html?
//                  msg=%E5%BE%88%E9%81%97%E6%86%BE%EF%BC%8C%E4%BA%BA%E6%95%B0%E5%B7%B2%E6%BB%A1%EF%BC%8C%E6%82%A8%E
//                  6%97%A0%E6%B3%95%E5%8A%A0%E5%85%A5%E3%80%82%E8%AF%B7%E8%81%94%E7%B3%BB%E6%B4%BB%E5%8A%A8%E4%B8%BB%E5%8A%9E%E6%96%B9%E3%80%82
                if (type == C.FREE_INTERCEPT_BUSY && url.startsWith("http://static.gensee.com/")) {
                    showBusyDialog();
                    return true;//试听课程，人数满了之后的弹窗提醒
                } else if (type == C.DEAL_ADD_HEADER) {
                    view.loadUrl(url, map);
                } else {
                    view.loadUrl(url);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Utils.setVisible(tvCenter, moreContentTitle);
                if (!TextUtils.isEmpty(title)) {
                    moreContentTitle.setText(title);
                    Utils.setGone(tvCenter);
                } else if (!TextUtils.isEmpty(mTitleName)) {
                    tvCenter.setText(mTitleName);
                    Utils.setGone(moreContentTitle);
                } else {
                    Utils.setGone(moreContentTitle);
                    tvCenter.setText(R.string.app_name);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mProgressBar == null) return;
                if (newProgress == 100) {
                    Utils.setGone(mProgressBar);
                } else {
                    Utils.setVisible(mProgressBar);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
        if (type == C.DEAL_ADD_HEADER) {
            webView.loadUrl(mUrl, map);
        } else {
            webView.loadUrl(mUrl);
        }
    }

    private void showBusyDialog() {
        if (showing) return;
        if (mDialog == null) {
            mDialog = InternetBusyTipDialog.getInstance(new ICallBack() {
                @Override
                public void onSuccess(Object o) {
                    finishWithAnim();
                }

                @Override
                public void onFail() {
                }
            });
            mDialog.setCancelable(false);
        }
        showing = true;
        if (!mDialog.isAdded())
            mDialog.showDialog(getSupportFragmentManager());
    }

    @Override
    protected boolean preBackExitPage() {
        if (null != webView && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        if (type == C.DEAL_GO_MAIN) {
            forword(MainActivity.class);
            finishWithAnim();
        }
        return super.preBackExitPage();
    }

    @Override
    protected void onDestroy() {
        if (null != webView) {
            if (null != webView.getParent()) {
                ((ViewGroup) webView.getParent()).removeView(webView);
            }
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }
}
