package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspGetActive;
import com.ebox.ex.network.model.base.BaseReq;

public class RequestGetActive extends BaseRequest<RspGetActive> {

	public RequestGetActive(ResponseEventHandler<RspGetActive> listener) {
		super(Method.GET,url(), body(), RspGetActive.class, listener);
		setEnableCookie(true);
	}

	private static String url() {

		return NetworkConfig.BoxServiceAddress + "/v2/operator/recharge/activity";
	}

	private static String body(){

		String json = JsonSerializeUtil.bean2Json(new BaseReq());

		return json;
	}


}
