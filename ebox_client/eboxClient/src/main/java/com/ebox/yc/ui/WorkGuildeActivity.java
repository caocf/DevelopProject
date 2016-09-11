package com.ebox.yc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.ebox.yc.model.ReqWorkGuide;
import com.ebox.yc.model.RspWorkGuide;
import com.ebox.yc.model.enums.Channel;
import com.ebox.yc.network.request.QryWorkGuide;


public class WorkGuildeActivity extends CommonActivity implements OnClickListener{
	private Title title;
	private TitleData titleData;
	private DialogUtil dialogUtil;
	private Context mContext;
	
	private TextView tv_guide_title;
	private TextView tv_fwzt;//服务主体
	private TextView tv_sbyj;//申办依据
	private TextView tv_sbtj;//申办条件
	private TextView tv_bscl;//办事材料
	private TextView tv_blcx;//办事程序
	private TextView tv_blsx;//办理时限
	private TextView tv_zflx;//支付类型
	private TextView tv_dz;//地址
	private TextView tv_lxdh;//联系电话
	private TextView tv_ms;//描述
	
	
	private String itemId;
	
	private Tip tip;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = WorkGuildeActivity.this;
		setContentView(R.layout.yc_fg_work_guide);
		MGViewUtil.scaleContentView(this,R.id.rootView);
		itemId = getIntent().getStringExtra("itemId");
		initView();
		initData();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.yc_work_guide);
		title.setData(titleData, this);
		
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this, true);
		tv_guide_title = (TextView) findViewById(R.id.tv_guide_title);
		tv_fwzt = (TextView) findViewById(R.id.tv_fwzt);
		tv_sbyj = (TextView) findViewById(R.id.tv_sbyj);
		tv_sbtj = (TextView) findViewById(R.id.tv_sbtj);
		tv_bscl = (TextView) findViewById(R.id.tv_bscl);
		tv_blcx = (TextView) findViewById(R.id.tv_blcx);
		tv_blsx = (TextView) findViewById(R.id.tv_blsx);
		tv_zflx = (TextView) findViewById(R.id.tv_zflx);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		tv_lxdh = (TextView) findViewById(R.id.tv_lxdh);
		tv_ms = (TextView) findViewById(R.id.tv_ms);
		
		
	}
	
	
	private void initData(){
		dialogUtil.showProgressDialog();
		qryWorkGuilde();
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


	private void qryWorkGuilde(){
		
			ReqWorkGuide req = new ReqWorkGuide();
			req.setChannel(Channel.Kd);
			req.setItemId(itemId);
			
			QryWorkGuide request = new QryWorkGuide(req, new ResponseEventHandler<RspWorkGuide>(){

				@Override
				public void onResponseSuccess(RspWorkGuide result) {
					dialogUtil.closeProgressDialog();
					showData(result);
				}

				@Override
				public void onResponseError(VolleyError error) {
					dialogUtil.closeProgressDialog();
					LogUtil.i("查询办事指南失败" + error.getMessage());
					
				}
				
			});
			RequestManager.addRequest(request, this);
		
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {


		default:
			break;
		}
		
	}
	
	private void showData(RspWorkGuide result){
		if(result.getResult()==0){
			if(result.getItemName()!=null && !result.getItemName().equals("")){
				tv_guide_title.setText(result.getItemName());
			}
			if(result.getServiceTheme()!=null && !result.getServiceTheme().equals("")){
				tv_fwzt.setText(result.getServiceTheme());
			}
			if(result.getBasis()!=null && !result.getBasis().equals("")){
				tv_sbyj.setText(result.getBasis());
			}
			
			if(result.getCondition()!=null && !result.getCondition().equals("")){
				tv_sbtj.setText(result.getCondition());
			}
			
			if(result.getMeterial()!=null && !result.getMeterial().equals("")){
				tv_bscl.setText(result.getMeterial());
			}
			
			if(result.getProgram()!=null && !result.getProgram().equals("")){
				tv_blcx.setText(result.getProgram());
			}
			
			if(result.getLimitDate()!=null){
				tv_blsx.setText(result.getLimitDate());
			}
			
			if(result.getPaymentType()!=null){
				tv_zflx.setText(result.getPaymentType());
			}
			
			if(result.getAddress()!=null){
				tv_dz.setText(result.getAddress());
			}
			
			if(result.getContactPhone()!=null){
				tv_lxdh.setText(result.getContactPhone());
			}
			
			if(result.getComments()!=null){
				tv_ms.setText(result.getComments());
			}
			
		} else {
			tip = new Tip(mContext, "查询失败，请稍后再试！" , null);
			tip.show(0);
		}
	}
	
	
	
	
}
