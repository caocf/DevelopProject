package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspRechargePlayOrder;
import com.ebox.ex.network.model.req.ReqGetRechargePayOrder;

public class RechargePayOrderRequest extends BaseRequest<RspRechargePlayOrder> {

	public RechargePayOrderRequest(ReqGetRechargePayOrder order,
								   ResponseEventHandler<RspRechargePlayOrder> listener) {
		super(Method.POST, url(), body(order), RspRechargePlayOrder.class, listener);
		setEnableCookie(true);
	}

	private static String url() {
		return NetworkConfig.pay_address + "/v1/pay";
	}


	private static String body(Object parms){

		String json = JsonSerializeUtil.bean2Json(parms);

		return json;
	}

}
