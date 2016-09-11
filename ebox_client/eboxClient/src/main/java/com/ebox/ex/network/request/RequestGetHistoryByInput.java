package com.ebox.ex.network.request;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.NetworkConfig;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.RspOperatorQueryItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestGetHistoryByInput extends BaseRequest<RspOperatorQueryItem> {

	public RequestGetHistoryByInput(HashMap<String, Object> parms,
									ResponseEventHandler<RspOperatorQueryItem> listener)
	{
		super(Method.GET, getURL(parms), null, RspOperatorQueryItem.class, listener);

		setEnableCookie(true);
	}

	private static String getURL(HashMap<String, Object> parms) {

		String u= NetworkConfig.BoxServiceAddress + "/v2/delivery/public?";

		StringBuilder parm = new StringBuilder(u);

		Set<Map.Entry<String, Object>> entries = parms.entrySet();

		for (Map.Entry<String, Object> m : entries)
		{
			String key = m.getKey();
			Object value = m.getValue();
			parm.append(key).append("=").append(value).append("&");
		}
		String url = parm.toString();

		return url.substring(0,url.length()-1);

	}

}
