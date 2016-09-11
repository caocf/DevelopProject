package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.RspGetActive;
import com.ebox.ex.network.model.RspRechargeOrderId;
import com.ebox.ex.network.model.RspRechargePlayOrder;
import com.ebox.ex.network.model.base.type.AcitveType;
import com.ebox.ex.network.model.base.type.DeviceType;
import com.ebox.ex.network.model.req.ReqGetRechargePayOrder;
import com.ebox.ex.network.request.RechargeOrderIdRequest;
import com.ebox.ex.network.request.RechargePayOrderRequest;
import com.ebox.ex.network.request.RequestGetActive;
import com.ebox.ex.ui.RechargeActivity;
import com.ebox.ex.ui.adapter.ActiveAdapter;
import com.ebox.ex.ui.adapter.ActiveAdapter.activeOnItemClickListener;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.utils.DeviceInfoUtil;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.PackageUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * 原有支付界面
 */
public class RechargeLayout extends OperMainLayout implements OnClickListener,activeOnItemClickListener
{
	private Button bt_recharge;
	private TextView tv_account;
	private DialogUtil dialogUtil;
	private GridView mGridView;
	private ActiveAdapter mAdapter;
	private Tip tip;
	private Activity context;
	private LinearLayout ll_active;
	private int money;
    private String operatorId;
	
	private void initViews(final Context context) {
		this.context = (Activity) context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_m_recharge_layout, this, 
				true);
		bt_recharge = (Button)findViewById(R.id.bt_recharge);
		tv_account = (TextView)findViewById(R.id.tv_account);
		mGridView=(GridView) findViewById(R.id.GridView);
		ll_active=(LinearLayout) findViewById(R.id.ll_active);
		mAdapter=new ActiveAdapter(context);
		mAdapter.setListener(this);
		mGridView.setAdapter(mAdapter);
		if (operatorId!=null)
		{
			tv_account.setText(operatorId);
		}
		bt_recharge.setOnClickListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog((Activity)context);
		
		initData();
	}
	

	private void initData()
	{
		ll_active.setVisibility(View.GONE);
		dialogUtil.showProgressDialog();
		if (OperatorHelper.cacheCookie != null)
		{
			getActive();
		} else {
			OperatorHelper.silenceLogin(new OperatorHelper.LoginResponseListener() {
				@Override
				public void success()
				{
					getActive();
				}

				@Override
				public void failed()
				{
					dialogUtil.closeProgressDialog();
					new Tip(context,getResources().getString(R.string.pub_connect_failed),null).show(0);
					ll_active.setVisibility(View.GONE);
				}
			});
		}
	}

	private void getActive() {

		RequestGetActive req=new RequestGetActive(new ResponseEventHandler<RspGetActive>() {

			@Override
			public void onResponseSuccess(RspGetActive result)
			{
				dialogUtil.closeProgressDialog();
				if (result.isSuccess())
				{
					showActive(result.getData().getActivities());
				}else
				{
					ll_active.setVisibility(View.GONE);
					new Tip(context, result.getMsg(),null).show(0);
				}
			}

			@Override
			public void onResponseError(VolleyError error)
			{
				dialogUtil.closeProgressDialog();
				ll_active.setVisibility(View.GONE);
				new Tip(context, getResources().getString(R.string.pub_connect_failed),null).show(0);
			}
		});
		RequestManager.addRequest(req, null);
	}

	protected void showActive(List<AcitveType> result)
	{
        mAdapter.addAll(result);
		//默认选择第一个
		if (result.size()>0)
		{
			money=result.get(0).getAct_value();
			ll_active.setVisibility(View.VISIBLE);
		}else {
			ll_active.setVisibility(View.GONE);
			new Tip(context, "网点没有充值活动",null).show(0);
		}
	}
	
	

	public RechargeLayout(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public RechargeLayout(Context context,String operatorId) {
		super(context);
        this.operatorId = operatorId;
		initViews(context);
	}
	
	
	public void saveParams() {
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
	}

	
	private void reqGrCode(){
		dialogUtil.showProgressDialog();

		RechargeOrderIdRequest orderIdRequest=new RechargeOrderIdRequest(money, new ResponseEventHandler<RspRechargeOrderId>() {
			@Override
			public void onResponseSuccess(RspRechargeOrderId result) {

				if (result.isSuccess())
				{
					createPayOrder(result.getData().order.order_id,result.getData().order.fee);
				} else
				{
					dialogUtil.closeProgressDialog();
					new Tip(context,result.getMsg(),null).show(0);
				}
			}

			@Override
			public void onResponseError(VolleyError error) {
				dialogUtil.closeProgressDialog();
				LogUtil.e(error.getMessage());
				new Tip(context,getResources().getString(R.string.pub_connect_failed),null).show(0);
			}
		});

		RequestManager.addRequest(orderIdRequest, null);
	}

	private void createPayOrder(final String orderid,Integer money) {

		ReqGetRechargePayOrder parms = new ReqGetRechargePayOrder();

		List<String> orders = new ArrayList<String>();
		orders.add(orderid);
		parms.order_ids = orders;
		parms.pay_type = 9;
		parms.total_fee = money;
		parms.total_num = 1;
		parms.device_info = getDeviceInfoObject(context);
		parms.service = "delivery_courier_recharge_order_service";
		parms.return_url = NetworkConfig.notify_address + "/kuaidi/pay/alipay/{pay_id}/redirect";

		RechargePayOrderRequest payOrderRequest = new RechargePayOrderRequest(parms, new ResponseEventHandler<RspRechargePlayOrder>() {
			@Override
			public void onResponseSuccess(RspRechargePlayOrder result)
			{
				dialogUtil.closeProgressDialog();
				if (result.isSuccess()) {
					String order = result.getData().order_info.params;

					Intent intent = new Intent(context, RechargeActivity.class);
					intent.putExtra("order_id", order);
					context.startActivity(intent);
				} else {
					new Tip(context, result.getMsg(), null).show(0);
				}
			}

			@Override
			public void onResponseError(VolleyError error) {
				dialogUtil.closeProgressDialog();
				LogUtil.e(error.getMessage());
				new Tip(context, getResources().getString(R.string.pub_connect_failed), null).show(0);
			}
		});
		RequestManager.addRequest(payOrderRequest, null);

	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_recharge:
			Recharge();
			break;
		}
	}
	
	private boolean checkPara()
	{

		 if (operatorId==null)
		 {
			 tip=new Tip(context, "账号异常", null);
			 tip.show(0);
			 return false;
		 }
		 if (money==0) {
			 tip=new Tip(context, "请选择充值金额", null);
			 tip.show(0);
			 return false;
		}
		
		return true;
	}
	
	private void Recharge()
	{
		if (checkPara()) {
			if (OperatorHelper.cacheCookie != null)
			{
				reqGrCode();
			} else
			{
				OperatorHelper.silenceLogin(new OperatorHelper.LoginResponseListener() {
					@Override
					public void success()
					{
						reqGrCode();
					}

					@Override
					public void failed()
					{
						dialogUtil.closeProgressDialog();
						new Tip(context,getResources().getString(R.string.pub_connect_failed),null).show(0);
					}
				});
			}

		}
	}

	@Override
	public void OnItemClickListener(int positon)
	{
		AcitveType item = mAdapter.getItem(positon);
		if (item!=null)
		{
			money=item.getAct_value();
		}
	}

	public static DeviceType getDeviceInfoObject(Context context) {
		DeviceType type=new DeviceType();
		type.udid=DeviceInfoUtil.getDeviceId(context);
		type.app_name="com.ebox";
		type.appver= PackageUtil.getVersionName(context);
		type.os="Android";
		type.phonemodel=DeviceInfoUtil.getDeviceName(context);
		type.network=DeviceInfoUtil.getNetType(context);
		type.osver=DeviceInfoUtil.getSysVersion(context);

		return type;
	}
	
}

