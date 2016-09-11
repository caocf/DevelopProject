package com.ebox.yc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.yc.model.ReqAnnDetail;
import com.ebox.yc.model.RspAnnDetail;
import com.ebox.yc.model.enums.Channel;
import com.ebox.yc.network.request.QryContentDetail;


public class AnnounceDetailActivity extends CommonActivity {
	private Tip tip;
	private Title title;
	private TitleData titleData;
	private DialogUtil dialogUtil;
	private Context mContext;
	
	private TextView tv_title;
	private TextView tv_create_time;
	private WebView web;
	
	private String contentId = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = AnnounceDetailActivity.this;
		setContentView(R.layout.yc_fg_announce_detail);
		MGViewUtil.scaleContentView(this,R.id.rootView);
		contentId = getIntent().getStringExtra("conent_id");
		initView();
		initData();
	}
	

	@SuppressLint("SetJavaScriptEnabled") 
	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.yc_announce_detail);
		title.setData(titleData, this);
		
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this, true);
		tv_title  = (TextView) findViewById(R.id.tv_ac_title);
		tv_create_time  = (TextView) findViewById(R.id.tv_create_time);
		web = (WebView) findViewById(R.id.web);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setUseWideViewPort(true);
		web.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		web.getSettings().setLoadWithOverviewMode(true);
		//web.setInitialScale(100);
		web.getSettings().setSupportZoom(true);
		web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		web.getSettings().setLoadWithOverviewMode(true);
		web.setHorizontalScrollBarEnabled(false);
		web.setVerticalScrollBarEnabled(false);
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
		
	}
	
	
	private void initData(){
		dialogUtil.showProgressDialog();
		QryContentDetail();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(tip != null)
		{
			tip.closeTip();
		}
		if(dialogUtil !=null){
			dialogUtil.closeProgressDialog();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		title.showTimer();
	}
	@Override
	public void onPause() {
		super.onPause();
		title.stopTimer();
	}


	private void showPrompt(int resId) {
		tip = new Tip(mContext, getResources().getString(
				resId), null);
		tip.show(0);
	}
	
	
	private void QryContentDetail(){
		ReqAnnDetail req = new ReqAnnDetail();
		req.setContentId(contentId);
		req.setChannel(Channel.Kd);
		req.setNodeId("B");
		QryContentDetail request = new QryContentDetail(req, new ResponseEventHandler<RspAnnDetail>(){

			@Override
			public void onResponseSuccess(RspAnnDetail result) {
				dialogUtil.closeProgressDialog();
				if(result.getResult() == 0){
					showData(result);
				}else {
					tip = new Tip(mContext, result.getResultMsg() , null);
					tip.show(0);
				}
				
			}

			@Override
			public void onResponseError(VolleyError error) {
				dialogUtil.closeProgressDialog();
				LogUtil.i("查询公告详情失败"+error.getMessage());
			}
			
		});
		
		RequestManager.addRequest(request, this);
	}
	
	private void showData(RspAnnDetail result){
		tv_title.setText(result.getTitle());
		tv_create_time.setText(result.getCreateDate());
		web.loadDataWithBaseURL("about:blank", result.getHtmlDetail(), "text/html", "utf-8", null);
	}
	
}
