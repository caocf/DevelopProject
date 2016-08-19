package com.xhl.bqlh.view.ui.activity.web;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.xhl.bqlh.R;
import com.xhl.bqlh.model.event.CommonEvent;
import com.xhl.bqlh.view.helper.DialogMaker;
import com.xhl.bqlh.view.helper.EventHelper;


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
        EventHelper.postCommonEvent(CommonEvent.ET_RELOGIN);
    }

    @JavascriptInterface
    public void productDetail(String productId) {
        EventHelper.postProduct(productId);
    }

    @JavascriptInterface
    public void shopDetail(String shopId) {
        EventHelper.postShop(shopId);
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

}
