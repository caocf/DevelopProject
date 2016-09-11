package com.ebox.ex.ui;

import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.request.RequestReSendSms;
import com.ebox.ex.ui.adapter.QueryItemAdapter;
import com.ebox.ex.ui.bar.QueryItemBar.QueryItemBarListener;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;

public class ItemQueryRstActivity extends CommonActivity implements
	QueryItemBarListener,ResponseEventHandler<BaseRsp>{
	private ListView item_list;
	private DialogUtil dialogUtil;
	private QueryItemAdapter adapter;
	private ArrayList<OrderLocalInfo> list;
	private String phone;
	private Tip tip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		phone = getIntent().getStringExtra("phone");
		setContentView(R.layout.ex_activity_item_query_rst);
        MGViewUtil.scaleContentView(this, R.id.rootView);
		if (phone != null)
		{
			list = OrderLocalInfoOp.getAllOrderLocalsByMobilePhone(phone);
		}
		initView();
	}


	@Override
	protected void onResume() {
		super.onResume();
		title.showTimer();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		title.stopTimer();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	
	private void initView()
	{
		item_list = (ListView) findViewById(R.id.item_list);
		
		adapter = new QueryItemAdapter(this);
		item_list.setAdapter(adapter);
		item_list.setCacheColorHint(0);
//		item_list.setDivider(null);
		item_list.setSelector(R.color.transparent);
		item_list.setOnItemClickListener(adapter);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);


		adapter.setData(list, this);
		adapter.notifyDataSetChanged();
		
		initTitle();
	}
	
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.ex_i_query);
		data.tvVisibility=true;
		title.setData(data, this);
	}
	

	@Override
	public void onDealClick(OrderLocalInfo item) {
		dialogUtil.showProgressDialog();

		RequestReSendSms requestReSendSms=new RequestReSendSms(item.getOrder_id(),this);
		executeRequest(requestReSendSms);
	}

	@Override
	public void onResponseSuccess(BaseRsp result) {
		dialogUtil.closeProgressDialog();
		if (result.isSuccess()) {
			tip = new Tip(ItemQueryRstActivity.this, getResources().getString(R.string.ex_send_sucess), null);
			tip.show(0);
		} else {
			tip = new Tip(ItemQueryRstActivity.this, result.getMsg(), null);
			tip.show(0);
		}

	}

	@Override
	public void onResponseError(VolleyError error) {
		dialogUtil.closeProgressDialog();
		tip = new Tip(ItemQueryRstActivity.this, getResources().getString(R.string.pub_connect_failed), null);
		tip.show(0);
	}
}
