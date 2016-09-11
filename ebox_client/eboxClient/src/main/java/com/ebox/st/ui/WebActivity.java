package com.ebox.st.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.ui.bar.TimeLeftBar;
import com.ebox.ex.network.model.enums.DotType;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;

public class WebActivity extends CommonActivity implements OnClickListener{
	private WebView web;
	private Button btn_exit;
	private Button btn_back;
	private TextView tv_timer;
	private TextView tv_title;
	private TimeLeftBar timeLeft;
	private DialogUtil dialogUtil;
	private String url;
	private String title;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.st_activity_web);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		initView();
	}
	

	@Override
	public void onPause() {
		super.onPause();
		timeLeft.stop();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(dialogUtil !=null){
			dialogUtil.closeProgressDialog();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		timeLeft.show();
	}
	
	private void initView()
	{
		web = (WebView) findViewById(R.id.web);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setUseWideViewPort(true);
		web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		web.getSettings().setLoadWithOverviewMode(true);
		web.setWebViewClient(new WebViewClient(){ 
			 @Override  
             public void onPageFinished(WebView view,String url)  
             {  
				 dialogUtil.closeProgressDialog();
				 LogUtil.d("web page loading finish!");
             }  
            public boolean shouldOverrideUrlLoading(WebView view, String url) {       
                view.loadUrl(url);   
                dialogUtil.showProgressDialog();
                return true;       
            }       
		}); 
		
		btn_exit = (Button) findViewById(R.id.btn_exit);
		btn_back = (Button) findViewById(R.id.bt_back);
		btn_back.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
		tv_timer=(TextView) findViewById(R.id.tv_timer);
        tv_title=(TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
		timeLeft = new TimeLeftBar(tv_timer);
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this, true);
		dialogUtil.showProgressDialog();
		web.loadUrl(url);
		LogUtil.d("web page loading in---" + url);
		//银川主题
		if (GlobalField.config.getDot() == DotType.YINCHUAN)
		{
			btn_exit.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.bt_back:
			if(web.canGoBack())
			{
				web.goBack(); 
			}
			else
			{
				finish();
			}
			break;
		case R.id.btn_exit:
			finish();
			break;
		}
		
		
	}
	
}
