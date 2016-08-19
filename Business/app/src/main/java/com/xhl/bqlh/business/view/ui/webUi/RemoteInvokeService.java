package com.xhl.bqlh.business.view.ui.webUi;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.xhl.bqlh.business.R;
import com.xhl.bqlh.business.view.base.BaseAppActivity;
import com.xhl.bqlh.business.view.helper.DialogMaker;


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
//        EventBusHelper.postReLogin();
    }

    @JavascriptInterface
    public void productDetails(String productId) {
//        EventBusHelper.postProductDetails(productId);
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
