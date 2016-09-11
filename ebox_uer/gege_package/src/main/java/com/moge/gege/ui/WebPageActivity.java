package com.moge.gege.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.ZoomButtonsController;

import com.moge.gege.R;
import com.moge.gege.config.GlobalConfig;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

public class WebPageActivity extends BaseActivity {
    private Context mContext;
    private WebView mWebView;
    private String mUrl;
    private String mHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webpage);

        mUrl = getIntent().getStringExtra("web_url");
        mHtml = getIntent().getStringExtra("web_html");

        mContext = WebPageActivity.this;
        initView();
    }

    @Override
    protected void initView() {
        super.initView();

        this.setHeaderLeftImage(R.drawable.icon_back);

        mWebView = (WebView) this.findViewById(R.id.webview);
        initWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    private void initWebView() {
        WebSettings localWebSettings = this.mWebView.getSettings();
        localWebSettings.setSupportZoom(true);
        localWebSettings.setBuiltInZoomControls(true);
        localWebSettings.setJavaScriptEnabled(true);
        localWebSettings.setLoadsImagesAutomatically(true);
        localWebSettings.setDomStorageEnabled(true);
        localWebSettings.setAppCacheEnabled(false);
        localWebSettings.setAllowFileAccess(true);
        localWebSettings.setLoadWithOverviewMode(true);

        //用于判断是否为Android 3.0系统, 然后隐藏缩放控件
        if (Build.VERSION.SDK_INT >= 11) {
            localWebSettings.setDisplayZoomControls(false);
            this.mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        } else {
            this.setZoomControlGone(mWebView);
        }

        this.mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if(!TextUtils.isEmpty(mUrl)) {
                    setHeaderLeftTitle(view.getTitle());
                }
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri localUri = Uri.parse(url);
                String str = localUri.getScheme();
                if ((TextUtils.isEmpty(str)) || ("http".equals(str))
                        || ("https".equals(str))) {
                    mWebView.loadUrl(url);
                    return false;
                }
                return true;
            }
        });

        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }

        if (!TextUtils.isEmpty(mHtml)) {
            this.setHeaderLeftTitle(R.string.image_text_detail);
            mWebView.loadDataWithBaseURL(null,
                    GlobalConfig.DELETE_WEBVIEW_PADDING
                            + mHtml,
                    "text/html", "UTF-8", null);
        }
    }

    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        finish();
    }

    //Android 3.0(11) 以下使用以下方法:
    //利用java的反射机制
    public void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
                    view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
