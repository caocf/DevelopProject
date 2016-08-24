package com.xhl.world.ui.webUi;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.xhl.world.AppApplication;
import com.xhl.world.R;
import com.xhl.world.ui.activity.BaseAppActivity;
import com.xhl.world.ui.event.EventBusHelper;
import com.xhl.world.ui.utils.DialogMaker;
import com.xhl.world.ui.utils.SnackMaker;

public class RemoteInvokeService {
    private Activity context;
    private String url;
    private WebView webView;

    public RemoteInvokeService(Activity paramActivity, WebView paramWebView,
                               String paramString1) {
        this.context = paramActivity;
        this.webView = paramWebView;
        this.url = paramString1;
    }

    /**
     * Android 4.2以上需要添加注解@JavascriptInterface
     */
    @JavascriptInterface
    public void close() {
        context.finish();
    }

    @JavascriptInterface
    public void login() {
        EventBusHelper.postReLogin();
    }

    @JavascriptInterface
    public void productDetails(String productId) {
        EventBusHelper.postProductDetails(productId);
    }

    @JavascriptInterface
    public void toast(String msg) {
        SnackMaker.shortShow(webView, msg);
    }

    @JavascriptInterface
    public void dialog(String title, String msg) {
        if (webView == null) {
            return;
        }
        AlertDialog.Builder dialog = DialogMaker.getDialog(webView.getContext());
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setNegativeButton(R.string.dialog_ok, null);
        dialog.create().show();
    }

    @JavascriptInterface
    public String cookie() {
        String cookie = AppApplication.appContext.mCookie;
        return cookie;
    }


    @JavascriptInterface
    public void reload() {
//        this.webView.loadUrl(this.url);
    }

    @JavascriptInterface
    public void showLoadingView() {
        if (context instanceof BaseAppActivity) {
            ((BaseAppActivity) context).showLoadingDialog();
        }
    }

    @JavascriptInterface
    public void hideLoadingView() {
        if (context instanceof BaseAppActivity) {
            ((BaseAppActivity) context).hideLoadingDialog();
        }
    }

}
