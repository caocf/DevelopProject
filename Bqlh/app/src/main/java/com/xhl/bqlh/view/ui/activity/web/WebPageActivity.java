package com.xhl.bqlh.view.ui.activity.web;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.xhl.bqlh.AppConfig.NetWorkConfig;
import com.xhl.bqlh.R;
import com.xhl.bqlh.view.base.BaseAppActivity;

public class WebPageActivity extends BaseAppActivity {

    private String url;

    public static final String TAG_URL = "url";
    public static final String TAG_TITLE = "title";
    public static final String TAG_COOKIE = "cookie";
    public static final String TAG_AREA = "area";

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected void initParams() {

    }

    private WebView mWebView;
    private ProgressBar mBar;
    private FrameLayout fl_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);


        url = getIntent().getStringExtra(TAG_URL);

        String title = getIntent().getStringExtra(TAG_TITLE);

        setCookie(getIntent().getStringExtra(TAG_COOKIE), getIntent().getStringExtra(TAG_AREA));

        super.initBackBar(title, true, false);

        initView();
    }

    private void setCookie(String cookie, String area) {
        //保存到浏览器中
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(NetWorkConfig.generalHost, cookie);
        cookieManager.setCookie(NetWorkConfig.generalHost, area);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }
    }

    private void initView() {
        mBar = _findViewById(R.id.progress_bar);
        fl_content = _findViewById(R.id.fl_content);

        mWebView = new WebView(getApplicationContext());

        fl_content.addView(mWebView);

        initWebView();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {

        this.mWebView.addJavascriptInterface(new RemoteInvokeService(this), "js_appInvoke");

        final WebSettings localWebSettings = this.mWebView.getSettings();
        localWebSettings.setSupportZoom(false);
        localWebSettings.setBuiltInZoomControls(false);
        localWebSettings.setDisplayZoomControls(false);
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setLoadsImagesAutomatically(true);
        localWebSettings.setDomStorageEnabled(true); // 开启DOM Storage api功能
        localWebSettings.setAppCacheEnabled(true);//H5缓存打开
        localWebSettings.setUseWideViewPort(true);//自适应屏幕大小
        localWebSettings.setLoadWithOverviewMode(true);

        localWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        mWebView.setOverScrollMode(2);

        mWebView.setWebChromeClient(chromeClient);
        mWebView.setWebViewClient(client);

        mWebView.loadUrl(url);

        //webView 调用js方法
//         mWebView.loadUrl("javascript:funFromjs()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    private WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mBar.setProgress(newProgress);
            if (newProgress == 100) {
                mBar.setVisibility(View.GONE);
            }
        }
    };

    private WebViewClient client = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            setTitle(view.getTitle());
        }
    };

    @Override
    protected void onDestroy() {
        try {
            mWebView.stopLoading();
            mWebView.removeAllViews();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);

            mWebView.destroy();
            mWebView = null;

            fl_content.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        finish();
    }
}
