package com.xhl.bqlh.view.ui.activity.web;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xhl.bqlh.AppConfig.NetWorkConfig;
import com.xhl.bqlh.AppDelegate;
import com.xhl.bqlh.R;
import com.xhl.bqlh.view.base.BaseAppActivity;

public class WebPageActivity extends BaseAppActivity {

    private String url;

    public static final String TAG_URL = "url";
    public static final String TAG_TITLE = "title";

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected void initParams() {

    }

    private WebView mWebView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);

        setCookie();

        url = getIntent().getStringExtra(TAG_URL);

        String title = getIntent().getStringExtra(TAG_TITLE);

        super.initBackBar(title, true, false);

        initView();
    }

    private void setCookie() {
        //保存到浏览器中
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);

        String cookie = AppDelegate.appContext.mCookie;
        String area = AppDelegate.appContext.mArea;
        cookieManager.setCookie(NetWorkConfig.generalHost, cookie);
        cookieManager.setCookie(NetWorkConfig.generalHost, area);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }
    }

    private void initView() {
        mWebView = (WebView) this.findViewById(R.id.web_view);

        initWebView();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {

        this.mWebView.addJavascriptInterface(new RemoteInvokeService(
                WebPageActivity.this, mWebView, url), "js_appInvoke");

        final WebSettings localWebSettings = this.mWebView.getSettings();
        localWebSettings.setSupportZoom(false);
        localWebSettings.setBuiltInZoomControls(true);
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setLoadsImagesAutomatically(true);
        localWebSettings.setDomStorageEnabled(true); // 开启DOM Storage api功能

        localWebSettings.setAllowFileAccess(true);
        localWebSettings.setLoadWithOverviewMode(true);

        localWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        localWebSettings.setDisplayZoomControls(false);
        this.mWebView.setOverScrollMode(2);
        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView paramAnonymousWebView,
                                       String paramAnonymousString) {
                setTitle(paramAnonymousWebView.getTitle());
                if (!localWebSettings.getLoadsImagesAutomatically()) {
                    localWebSettings.setLoadsImagesAutomatically(true);
                }
            }

            public void onPageStarted(WebView paramAnonymousWebView,
                                      String paramAnonymousString, Bitmap paramAnonymousBitmap) {
                super.onPageStarted(paramAnonymousWebView,
                        paramAnonymousString, paramAnonymousBitmap);
            }

            public boolean shouldOverrideUrlLoading(
                    WebView paramAnonymousWebView, String paramAnonymousString) {
                Uri localUri = Uri.parse(paramAnonymousString);
                String str = localUri.getScheme();
                if ((TextUtils.isEmpty(str)) || ("http".equals(str))
                        || ("https".equals(str))) {
                    mWebView.loadUrl(paramAnonymousString);
                    return false;
                }
                return true;
            }

        });
        mWebView.loadUrl(url);

        //webView 调用js方法
        // mWebView.loadUrl("javascript:funFromjs()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        mWebView.destroy();
        super.onDestroy();
        mWebView = null;
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
