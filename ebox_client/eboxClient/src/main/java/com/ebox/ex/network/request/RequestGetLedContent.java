package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.ex.network.model.RspLedContent;
import com.ebox.ex.network.model.base.BaseReq;

public class RequestGetLedContent extends BaseRequest<RspLedContent> {

	public RequestGetLedContent(ResponseEventHandler<RspLedContent> listener) {
		super(Method.POST, url(), getPrams(), RspLedContent.class, listener);
		setNeedMainThread(false);
	}

	private static String url() {

		return NetworkConfig.BoxServiceAddress + "/v2/terminal/app/update/4";
	}

	public static String getPrams()
	{
		return JsonSerializeUtil.bean2Json(new BaseReq());
	}


}
