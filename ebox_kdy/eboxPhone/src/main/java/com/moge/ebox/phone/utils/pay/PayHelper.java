 
package com.moge.ebox.phone.utils.pay;

import android.app.Activity;
import android.app.ProgressDialog;

import com.google.gson.Gson;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.model.PayResultModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class PayHelper
{
    private Activity mContext;
    private PayResultModel mPayResultModel;
    private PayHelperResultLinstener mListener;
    private int mPayType;
    private ProgressDialog mProgressDialog;

    //服务
    public static final String trading_order_service="trading_order_service:";  //购买商品,
    public static final String recharge_order_service="recharge_order_service"; //在线充值
    public static final String delivery_courier_recharge_order_service="delivery_courier_recharge_order_service";   //快递员充值

    //支付类型
    public static final int pay_type_alipay=1;
    public static final int pay_type_union=2;
    public static final int pay_type_aliwap=3;
    public static final int pay_type_weiwap=4;
    public static final int pay_type_weixin=5;

    public PayHelper(Activity context, PayHelperResultLinstener listener)
    {
        mContext = context;
        mListener = listener;
        mProgressDialog = UIHelper.showProgress(mContext,null,
                mContext.getResources().getString(R.string.create_pay_order),false);
    }

    public boolean payForNewOrder(String phone,String money)
    {
        mProgressDialog.show();
        createOrder(phone,  money);
        return true;
    }

	private void createOrder(String phone, String money) {
		Map params=new HashMap();
        float pay_money=(Float.valueOf(money).floatValue())*100;
        params.put("fee", pay_money);
        Logger.i("Payhelper price"+pay_money);
        /**
         * 充值
         */
        ApiClient.getOperatorChargeInfor(EboxApplication.getInstance(), params, new ClientCallback() {
			
			@Override
			public void onSuccess(JSONArray data,int code) {
				UIHelper.dismissProgress(mProgressDialog);
                Logger.i("Payhelper 充值订单"+data.toString());
				checkData(data);
			}
			
			@Override
			public void onFailed(byte[] data,int code) {
				UIHelper.dismissProgress(mProgressDialog);
			}
		});
	}
  
    protected void checkData(JSONArray data) {
    	try {
			JSONObject json = data.getJSONObject(0).getJSONObject("order");
			PayResultModel model = new Gson().fromJson(json.toString(), PayResultModel.class);
			notifyPayResult(model);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void notifyPayResult(PayResultModel result)
    {
        if (mListener != null)
        {
            mListener.onPayResult(result);
        }
    }

    public interface PayHelperResultLinstener
    {
        public void onPayResult(PayResultModel result);
    }

}
