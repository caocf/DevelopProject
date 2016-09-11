package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspGetShowConfig;
import com.ebox.ex.network.model.base.BaseReq;

public class RequestGetShowConfig extends BaseRequest<RspGetShowConfig> {

	public RequestGetShowConfig(ResponseEventHandler<RspGetShowConfig> listener) {
		super(Method.GET,url(), body(), RspGetShowConfig.class, listener);
		setEnableCookie(true);
	}

	private static String url() {
		return NetworkConfig.BoxServiceAddress + "/v2/terminal/sync/config";
	}

	private static String body(){

		String json = JsonSerializeUtil.bean2Json(new BaseReq());

		return json;
	}


}
