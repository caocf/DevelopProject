package com.moge.ebox.phone.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.fragment.FragmentActive;
import com.moge.ebox.phone.model.PayResultModel;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.utils.DeviceInfoUtil;
import com.moge.ebox.phone.utils.PackageUtil;
import com.moge.ebox.phone.utils.ToastUtil;
import com.moge.ebox.phone.utils.pay.PayHelper;
import com.moge.ebox.phone.utils.pay.PayHelper.PayHelperResultLinstener;
import com.moge.ebox.phone.utils.pay.PayResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FastRechargeActivity extends BaseActivity {

	private Button mbtnRecharge;
	private Head mHead;
	private RelativeLayout rl_zfb, rl_wx;
	private ImageView iv_zfb, iv_wx;
	private EditText ed_zfb, ed_wx;
	private int curChoose;
	private static PayResult mAliResult;

	private  Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1: {
				mAliResult = new PayResult((String) msg.obj);
				Log.i("main", "mAliResult:" + mAliResult.getResult());
				int result=PayResult.PAY_SUCCESS;
				String error_msg="";
				if (mAliResult.getResultStatus().equals(PayResult.SUCCESS))
				{
					result=PayResult.PAY_SUCCESS;
					error_msg="";
					ToastUtil.showToastLong("付款成功,请您注意短信提醒 !");
				}else if(mAliResult.getResultStatus().equals(
						PayResult.CANCEL)){
					result=PayResult.PAY_CANCEL;
					error_msg=mAliResult.getErrorMsg();
					ToastUtil.showToastShort(mAliResult.getErrorMsg());
				}
				else if (mAliResult.getResultStatus().equals(
						PayResult.DEALING)) {
					result=PayResult.PAY_FAILED;
					error_msg=mAliResult.getErrorMsg();
					ToastUtil.showToastShort(mAliResult.getErrorMsg());
				} else {
					result=PayResult.PAY_FAILED;
					error_msg=mAliResult.getErrorMsg();
					ToastUtil.showToastShort(mAliResult.getErrorMsg());
				}

				//上报订单信息
				doGetPayOrderOver(result, error_msg);
			}
				break;

			case 2: {
				boolean b = (Boolean) msg.obj;
				if (b) {
					payMoney();
				} else {
					ToastUtil.showToastShort("请先安装支付宝钱包");
				}
			}
				break;
			default:
				break;
			}
		}
	};

	private void doGetPayOrderOver(int result,String error_msg) {
		HashMap params=new HashMap();
		params.put("pay_id",pay_id);
		params.put("pay_type",pay_type);
		params.put("result",result);	//支付结果
		params.put("error_msg",error_msg);	//错误信息
		JSONObject pay_info=new JSONObject();
		params.put("pay_info",pay_info);
		ApiClient.getPayOrderOver(EboxApplication.getInstance(), params, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(JSONArray data, int code) {

			}

			@Override
			public void onFailed(byte[] data, int code) {

			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fast_recharge);
		initView();
		showAcitve();
	}

	private void initView() {
		mbtnRecharge = findviewById_(R.id.btn_recharge);
		mbtnRecharge.setOnClickListener(mClickListener);
		rl_zfb = findviewById_(R.id.rl_zfb_recharge);
		rl_zfb.setOnClickListener(mClickListener);
		iv_zfb = findviewById_(R.id.iv_zfb_state);
		ed_zfb = findviewById_(R.id.ed_zfb_money);

		rl_wx = findviewById_(R.id.rl_wx_recharge);
		rl_wx.setOnClickListener(mClickListener);
		iv_wx = findviewById_(R.id.iv_wx_state);
		ed_wx = findviewById_(R.id.ed_wx_money);

		initHead();
	}

	private void initHead() {
		mHead = findviewById_(R.id.title);
		HeadData data = mHead.new HeadData();
		data.backVisibility = 1;
		data.tvVisibility = true;
		data.tvContent = getResources().getString(R.string.fast_recharege);
		mHead.setData(data, this);
	}

	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rl_zfb_recharge:
				changeChoose(0);
				break;

			case R.id.rl_wx_recharge:
				changeChoose(1);
				break;

			case R.id.btn_recharge:
				payMoney();
				break;
			}
		}
	};

	protected void changeChoose(int choose) {
		curChoose = choose;
		if (choose == 0) 
		{
			iv_zfb.setImageResource(R.drawable.img_agree);
			iv_wx.setImageResource(R.drawable.img_disagree);
		}
		else if (choose == 1) 
		{
			iv_zfb.setImageResource(R.drawable.img_disagree);
			iv_wx.setImageResource(R.drawable.img_agree);
		}
	}
	
	public void setPayMoney(String money){
		if (curChoose==0) {
			ed_zfb.setText(money);
		}
		else if(curChoose==1){
			ed_wx.setText(money);
		}
	}

	public  void payMoney() {

		String money = null;
		String phone = EboxApplication.getInstance().getLoginPhone();

		if (curChoose == 0) {
			money = checkMoney(ed_zfb);
		} else if (curChoose == 1) {
			money = checkMoney(ed_wx);
		}
		
		if (TextUtils.isEmpty(money)) {
			ToastUtil.showToastShort("请输入充值金额");
			return ;
		}
		Integer valueOf = Integer.valueOf(money);
		if (valueOf==0) {
			ToastUtil.showToastShort("充值金额不能为0");
			return;
		}
		
        if (TextUtils.isEmpty(phone)) {
			ToastUtil.showToastShort(R.string.pay_order_failed);
			return ;
		}
		final String finalMoney = money;
		PayHelper helper = new PayHelper(this, new PayHelperResultLinstener() {

			@Override
			public void onPayResult(PayResultModel result) {
				//获取了订单详情
				//获得订单详情后需要通过order_id获得支付单信息
				int moneyInt=Integer.valueOf(finalMoney).intValue();
				getPayOrderInfo(moneyInt, 1, result.order_id);

			}
		});
		//money="0.01";
		helper.payForNewOrder(phone, money);
	}

	/**
	 * 获得支付单信息
	 */
	private String pay_id;
	private int pay_type;
	public void getPayOrderInfo(int total_fee,int total_num,int order_id){
		HashMap params=new HashMap();
		params.put("total_fee",total_fee);
		params.put("total_num",total_num);
		params.put("service",PayHelper.delivery_courier_recharge_order_service);
		params.put("pay_type",PayHelper.pay_type_alipay);
		JSONArray orderIdArray = new JSONArray();
		orderIdArray.put(order_id);
		params.put("order_ids", orderIdArray);
		params.put("device_info",getDeviceInfoObject(FastRechargeActivity.this));
		ApiClient.getPayOrder(EboxApplication.getInstance(), params, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(JSONArray data, int code) {
				//支付单获得成功，便可以向pay sdk发送数据
				try {
					String order_info = data.getJSONObject(0).getJSONObject("order_info").toString();
					pay_id = data.getJSONObject(0).getJSONObject("pay_id").toString();
					pay_type = Integer.valueOf(data.getJSONObject(0).getJSONObject("pay_type").toString()).intValue();
					toPayOrder(FastRechargeActivity.this, order_info);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailed(byte[] data, int code) {

			}
		});
	}

	/**
	 * 获得设备信息
	 * @param context
	 * @return
	 */
	public static JSONObject getDeviceInfoObject(Context context)
	{
		JSONObject deviceObject = new JSONObject();
		try
		{
			deviceObject.put("udid", DeviceInfoUtil.getDeviceId(context));
			deviceObject.put("appver", PackageUtil.getVersionName(context));
			deviceObject.put("os", "android");
			deviceObject.put("phonemodel",
					DeviceInfoUtil.getDeviceName(context));
			deviceObject.put("network", DeviceInfoUtil.getNetType(context));
			deviceObject.put("app_name", "gege");
			deviceObject.put("osver", DeviceInfoUtil.getSysVersion(context));
			return deviceObject;
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}

		return deviceObject;
	}


	private String checkMoney(EditText editText) {
		String money = editText.getText().toString().trim();
		if (money.contains(".")) {
			int indexOf = money.indexOf(".");
			String mo = money.substring(0, indexOf);
			String res = Integer.valueOf(mo).toString();
			editText.setText(res);
			return res;
		}
		return money;
	}

	public void toPayOrder(final Activity context, final String orderParams) {
		if (TextUtils.isEmpty(orderParams)) 
		{
			ToastUtil.showToastShort("生成订单失败");
			return;
		}
		new Thread() {
			public void run() {
				PayTask alipay = new PayTask(context);
				String result = alipay.pay(orderParams);
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	public void checkalipay() {
		Runnable checkRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(FastRechargeActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();
				Message msg = new Message();
				msg.what = 2;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};
		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
		getSDKVersion();
	}
	
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		ViewUtils.closeInput(this, ed_zfb);
		ViewUtils.closeInput(this, ed_wx);
		return super.dispatchTouchEvent(ev);
	}
	
	private void showAcitve(){
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		transaction.replace(R.id.fl_active, new FragmentActive()).commit();
	}
	
}
