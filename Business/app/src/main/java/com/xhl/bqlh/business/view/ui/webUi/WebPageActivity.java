package com.xhl.bqlh.business.view.ui.webUi;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.xhl.bqlh.business.view.base.BaseAppActivity;


public class WebPageActivity extends BaseAppActivity {

    private String url;
    private String title;

    public static final String TAG_URL = "url";
    public static final String TAG_TITLE = "title";
    public static final String TAG_QUIT = "quit";

    public static final String TAG_SHARE_URL = "share_url";
    public static final String TAG_SHARE_ICON = "share_icon";

    public static final String right_button_style = "right_button_style";

    public static final int rightButtonStyleDefault = 0;//默认按钮
    public static final int rightButtonStyleShare = 1;//分享样式

    @Override
    public boolean isNeedInject() {
        return false;
    }

    @Override
    protected void initParams() {

    }

    private WebView mWebView;
    private ImageView mRightBtnIcon;
    private View mBack;
    private View mQuit;
    private TextView mTitle;
    private boolean mShowQuit;
    private int mBtnStyle = 0;
    private String mShareUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_webpage);

        url = getIntent().getStringExtra(TAG_URL);

        title = getIntent().getStringExtra(TAG_TITLE);

        mShareUrl = getIntent().getStringExtra(TAG_SHARE_URL);

        mBtnStyle = getIntent().getIntExtra(right_button_style, 0);

        if (mBtnStyle != rightButtonStyleDefault) {
            mShowQuit = getIntent().getBooleanExtra(TAG_QUIT, true);
        }

        initView();
        setCookie();
    }

    private void setCookie() {
//        CookieSyncManager.createInstance(this);
//        CookieSyncManager.getInstance().sync();

        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeSessionCookie();
        cookieManager.setAcceptCookie(true);
//        cookieManager.setCookie(url, AppApplication.appContext.getCookie());//cookies
//        cookieManager.setCookie(NetWorkConfig.generalHost, AppApplication.appContext.getCookie());//cookies
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        }
    }

    private void initView() {
//        mWebView = (WebView) this.findViewById(R.id.web_view);
      /*  mTitle = (TextView) findViewById(R.id.title_name);

        mRightBtnIcon = (ImageView) findViewById(R.id.title_right_icon);
        //分享样式
        if (mBtnStyle == rightButtonStyleShare) {
            mRightBtnIcon.setImageResource(R.drawable.icon_share);
        }*/
     /*   //退出
        mQuit = findViewById(R.id.title_quit);
        //返回
        mBack = findViewById(R.id.title_back);
*/
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }

        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (mShowQuit) {
            mQuit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    rightBtnClick();
                }
            });
        }
        initWebView();
        if (!mShowQuit) {
            mQuit.setVisibility(View.INVISIBLE);
        }
    }

    private void rightBtnClick() {
        //默认样式
        if (mBtnStyle == 0) {
            finish();
        } else if (mBtnStyle == 1) {
//            share();
        }

    }

    //分享按钮
   /* private void share() {
        String title = mTitle.getText().toString();
        ShareParam param = new ShareParam();
        param.setTitle(title);
        param.setContent(title);
        //分享的图标
        String icon = getIntent().getStringExtra(TAG_SHARE_ICON);
        param.setImageUrl(icon);

        if (!TextUtils.isEmpty(mShareUrl)) {
            param.setTargetUrl(mShareUrl);
        } else {
            param.setTargetUrl(url);
        }

        CustomShareBoard shareBoard = new CustomShareBoard(this, param);
        shareBoard.show();
    }*/

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWebView() {

        this.mWebView.addJavascriptInterface(new RemoteInvokeService(
                WebPageActivity.this, mWebView, url), "js_appInvoke");

        final WebSettings localWebSettings = this.mWebView.getSettings();
        localWebSettings.setSupportZoom(true);
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
                if (TextUtils.isEmpty(title)) {
                    mTitle.setText(paramAnonymousWebView.getTitle());
                }
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
