package com.ebox.yc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.ebox.yc.model.ReqWorkItems;
import com.ebox.yc.model.RspWorkItems;
import com.ebox.yc.model.WorkItem;
import com.ebox.yc.model.WorkWin;
import com.ebox.yc.model.enums.Channel;
import com.ebox.yc.network.request.QryWorkItems;
import com.ebox.yc.ui.adapter.WorkItemsAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;


public class WorkItemActivity extends CommonActivity implements OnClickListener{
	private PullToRefreshListView wilist;
	private Tip tip;
	private Button btn_left;
	private Button btn_right;
	
	private WorkItemsAdapter mAdapter;
	private ArrayList<WorkItem> pList = new ArrayList<WorkItem>();
	
	private TextView tv_wi_name;
	
	private Title title;
	private TitleData titleData;
	private DialogUtil dialogUtil;
	private Context mContext;
	
	private int pageIndex = 0;
	private int pageSize = 6;
	private boolean hasNext = false;
	
	private WorkWin mWorkWin;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = WorkItemActivity.this;
		setContentView(R.layout.yc_fg_workitem);
		MGViewUtil.scaleContentView(this,R.id.rootView);
		mWorkWin = (WorkWin)getIntent().getSerializableExtra("workWin");
		initView();
		initData();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = mWorkWin.getWindowName();
		title.setData(titleData, this);
		
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this, true);
		wilist  = (PullToRefreshListView) findViewById(R.id.wilist);
		btn_left = (Button) findViewById(R.id.btn_left);
		btn_right = (Button) findViewById(R.id.btn_right);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		tv_wi_name = (TextView) findViewById(R.id.tv_wi_name);
		tv_wi_name.setText(mWorkWin.getWindowName());
		mAdapter=new WorkItemsAdapter(this);
		mAdapter.addAll(pList);
		wilist.setAdapter(mAdapter);
		wilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				WorkItem info = pList.get(position - 1);
				startDeal(info);
			}
		});

		btn_left.setBackgroundResource(R.drawable.yc_left_pressed);
		btn_left.setClickable(false);
	}
	
	private void startDeal(WorkItem info)
	{
		Intent intent = new Intent(this,WorkGuildeActivity.class);
		intent.putExtra("itemId",info.getItemId());
		startActivity(intent);
		
	}
	
	private void initData(){
		dialogUtil.showProgressDialog();
		qryWorkItem();
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


	private void qryWorkItem(){
		
			ReqWorkItems req = new ReqWorkItems();
			req.setChannel(Channel.Kd);
			req.setPageIndex(pageIndex+"");
			req.setPageSize(pageSize+"");
			req.setWindowId(mWorkWin.getWindowId());
			QryWorkItems request = new QryWorkItems(req, new ResponseEventHandler<RspWorkItems>(){

				@Override
				public void onResponseSuccess(RspWorkItems result) {
					dialogUtil.closeProgressDialog();
					showData(result);
				}

				@Override
				public void onResponseError(VolleyError error) {
					dialogUtil.closeProgressDialog();
					LogUtil.i("查询办事项目失败" + error.getMessage());
					
				}
				
			});
			RequestManager.addRequest(request, this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_left:
			setLeftBtn();
			break;
		case R.id.btn_right:
			setRightBtn();
			break;

		default:
			break;
		}
		
	}
	
	private void showData(RspWorkItems result){
		if(result.getResult()==0){
			hasNext = false;
			if(result.getWorkItems().size()==0){
				tip = new Tip(mContext, "已经是最后一页" , null);
				tip.show(0);
				setRightState(false);
				--pageIndex;
			} else {
				hasNext = true;
				pList.clear();
				for(int i =0; i<result.getWorkItems().size();i++){
					pList.add(result.getWorkItems().get(i));
				}
				mAdapter.clear();
				mAdapter.addAll(pList);
				mAdapter.notifyDataSetChanged();
			}
			
		} else {
			tip = new Tip(mContext, result.getResultMsg() , null);
			tip.show(0);
		}
	}
	private void setLeftState(boolean show) {
		if (show) {
			btn_left.setBackgroundResource(R.drawable.yc_btn_left);
		} else {
			btn_left.setBackgroundResource(R.drawable.yc_left_pressed);
		}
		btn_left.setClickable(show);

	}
	private void setLeftBtn(){
		setRightState(true);
		pageIndex--;
		initData();
		if(pageIndex == 0)
		{
			setLeftState(false);
		}
	}

	private void setRightState(boolean show) {
		if (show) {
			btn_right.setBackgroundResource(R.drawable.yc_btn_right);
		} else {
			btn_right.setBackgroundResource(R.drawable.yc_right_pressed);
		}
		btn_right.setClickable(show);
	}

	private void setRightBtn(){
		if(hasNext){
			setLeftState(true);
			pageIndex++;
			initData();
		}else {
			setRightState(false);
		}
	}
	
	
}
