package com.ebox.ex.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ebox.R;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.service.AppService;
import com.ebox.pub.ui.RemoteInvokeService;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

/**
 * 支付宝扫码支付界面
 */
public class RechargeActivity extends CommonActivity {

    private String orderURL;
    private String url;
    private String cookie;
    private WebView web_view;
    private DialogUtil dialogUtil;
    private static final String TAG="RechargeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_activity_recharge_result);
        MGViewUtil.scaleContentView(this, R.id.rootView);
        orderURL = getIntent().getStringExtra("order_id");
        init();
    }

    private void init()
    {
        if (OperatorHelper.cacheCookie != null)
        {
            initView();
        } else
        {
            OperatorHelper.silenceLogin(new OperatorHelper.LoginResponseListener() {
                @Override
                public void success()
                {
                    initView();
                }
                @Override
                public void failed()
                {
                    dialogUtil.closeProgressDialog();
                    new Tip(RechargeActivity.this,getResources().getString(R.string.pub_connect_failed),null).show(0);
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTitle.showTimer();
        LogUtil.i(TAG+"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTitle.stopTimer();
        LogUtil.i(TAG + "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i(TAG + "onStop");
    }



    private void initView ()
    {
        cookie = OperatorHelper.cacheCookie;

        url = getDoMain(cookie);

        LogUtil.d("URL:"+orderURL);
        initTitle();
        web_view = (WebView) findViewById(R.id.web_view);
        initWebView();
    }

    private String getDoMain(String cookie)
    {
        String domain = ".dev.gegebox.com";

        String[] split = cookie.split(";");

        for (String value : split)
        {
            if (value.startsWith(" Domain")||value.startsWith("Domain"))
            {
                domain = value.substring(value.indexOf("=")+1,value.length());
                break;
            }
        }

        return domain;
    }

    private void sync() {
        CookieManager manager = CookieManager.getInstance();
        manager.setAcceptCookie(true);
        manager.removeAllCookie();
        manager.setCookie(url, cookie);
        CookieSyncManager.getInstance().sync();
    }

    private void clear() {
        CookieManager manager = CookieManager.getInstance();
        manager.setAcceptCookie(true);
        manager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView()
    {
        this.web_view.addJavascriptInterface(new RemoteInvokeService(
                RechargeActivity.this, web_view, null), "js_invoke");
        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setUseWideViewPort(true);
//        web_view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        web_view.getSettings().setLoadWithOverviewMode(true);
        web_view.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 4.2; en-us; moge Build/gegebox) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        web_view.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                dialogUtil.closeProgressDialog();
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               // LogUtil.e(url);
                if (url.startsWith("alipays:/")) {
                    return false;
                }
                dialogUtil.showProgressDialog();
                AppService.getIntance().hasOnKeyDown();
                view.loadUrl(url);
                return true;
            }
        });

        web_view.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mTitle.tv.setText(title);
            }
        });

        dialogUtil = new DialogUtil();
        dialogUtil.createProgressDialog(this, false);
        dialogUtil.showProgressDialog();

        sync();
        //web_view.loadUrl("http://m.dev.gegebox.com/user");
        web_view.loadUrl(orderURL);
    }


    private Title mTitle;
    private TitleData data;

    private void initTitle() {
        mTitle = (Title) findViewById(R.id.title);
        data = mTitle.new TitleData();
        data.backVisibility = 1;
        data.tvContent = "支付宝充值";
        data.tvVisibility = true;
        mTitle.setData(data, this);
    }

    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent(OperatorHomeActivity.BROADCAST_UPDATE_OPERATOR));
        clear();
        super.onDestroy();
        LogUtil.i(TAG + "onDestroy");
    }
}
