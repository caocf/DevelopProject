package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspRechargeOrderId;

import java.util.HashMap;

/**
 *
 */
public class RechargeOrderIdRequest extends BaseRequest<RspRechargeOrderId> {

	public RechargeOrderIdRequest(int money,
								  ResponseEventHandler<RspRechargeOrderId> listener) {
		super(Method.POST, url(), body(money), RspRechargeOrderId.class, listener);
		setEnableCookie(true);
	}

	private static String url() {
		return NetworkConfig.BoxServiceAddress + "/v2/operator/recharge/order";
	}

	private static String body(int money) {

		HashMap<String, Integer> p = new HashMap<String, Integer>();
		p.put("fee",money);
		String json = JsonSerializeUtil.bean2Json(p);

		return json;
	}
}
