package com.ebox.pub.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.ebox.AppApplication;
import com.ebox.ex.ui.OperatorHomeActivity;

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
	 *Android 4.2以上需要添加注解@JavascriptInterface
	 * */
	@JavascriptInterface
	public void close() {
		Log.i("main", "close webView : "+context.getClass().getName());
		context.finish();
		AppApplication.getInstance().sendBroadcast(new Intent(OperatorHomeActivity.BROADCAST_CHANGE_TO_FIRST));
	}
	@JavascriptInterface
	public void reload() {
		this.webView.loadUrl(this.url);
	}
}
