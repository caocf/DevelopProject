package com.ebox.yc.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;

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
import com.ebox.yc.model.ReqWorkWin;
import com.ebox.yc.model.RspWorkWin;
import com.ebox.yc.model.WorkWin;
import com.ebox.yc.model.enums.Channel;
import com.ebox.yc.network.request.QryWorkWin;
import com.ebox.yc.ui.adapter.WorkWinAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;


public class WorkWinActivity extends CommonActivity implements OnClickListener{
	private PullToRefreshGridView wwlist;
	private Button btn_left;
	private Button btn_right;
	
	private Tip tip;
	
	private WorkWinAdapter mAdapter;
	private ArrayList<WorkWin> pList = new ArrayList<WorkWin>();
	
	private Title title;
	private TitleData titleData;
	private DialogUtil dialogUtil;
	private Context mContext;
	
	private int pageIndex = 0;
	private int pageSize = 6;
	private boolean hasNext = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = WorkWinActivity.this;
		setContentView(R.layout.yc_fg_workwin);
		MGViewUtil.scaleContentView(this,R.id.rootView);
		initView();
		initData();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.yc_work_win);
		title.setData(titleData, this);
		
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this, true);
		wwlist  = (PullToRefreshGridView) findViewById(R.id.wwlist);
		mAdapter=new WorkWinAdapter(this);
		mAdapter.addAll(pList);
		wwlist.setAdapter(mAdapter);
		wwlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WorkWin info = pList.get(position);
				startDeal(info);
			}
		});
		
		btn_left = (Button) findViewById(R.id.btn_left);
		btn_right = (Button) findViewById(R.id.btn_right);
		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
		setLeftState(false);
	}
	
	private void startDeal(WorkWin info)
	{
		Intent intent = new Intent(this,WorkItemActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("workWin",info);
		intent.putExtras(mBundle);
		startActivity(intent);
	}
	
	private void initData(){
		dialogUtil.showProgressDialog();
		qryWorkWin();
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


	private void qryWorkWin(){
		
			ReqWorkWin req = new ReqWorkWin();
			req.setChannel(Channel.Kd);
			req.setPageIndex(Long.parseLong(pageIndex+""));
			req.setPageSize(Long.parseLong(pageSize + ""));
			QryWorkWin request = new QryWorkWin(req, new ResponseEventHandler<RspWorkWin>(){

				@Override
				public void onResponseSuccess(RspWorkWin result) {
					dialogUtil.closeProgressDialog();
					showData(result);
				}

				@Override
				public void onResponseError(VolleyError error) {
					dialogUtil.closeProgressDialog();
					LogUtil.i("查询公告失败" + error.getMessage());
					
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
	
	private void showData(RspWorkWin result){
		if(result.getResult()==0){
			hasNext = false;
			if(result.getWorkWins().size()==0){
				tip = new Tip(mContext, "已经是最后一页" , null);
				tip.show(0);
				setRightState(false);
				--pageIndex;
			} else {
				if(result.getWorkWins() == null || result.getWorkWins().size()==0){
					tip = new Tip(mContext, "没有数据了" , null);
					tip.show(0);
					hasNext = false;
					return;
				}
				
				hasNext = true;
				pList.clear();
				for(int i =0; i<result.getWorkWins().size();i++){
					pList.add(result.getWorkWins().get(i));
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
	
	private void setLeftBtn(){
			setRightState(true);
			pageIndex--;
			initData();
			if(pageIndex == 0)
			{
				setLeftState(false);
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

	private void setRightBtn(){
		if(hasNext){
			setLeftState(true);
			pageIndex++;
			initData();
		}else {
			setRightState(false);
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
}
