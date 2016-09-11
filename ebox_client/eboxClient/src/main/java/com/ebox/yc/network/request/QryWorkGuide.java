package com.ebox.yc.network.request;

import com.android.volley.AuthFailureError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.yc.model.ReqWorkGuide;
import com.ebox.yc.model.RspWorkGuide;
import com.ebox.yc.network.BaseRequest;
import com.ebox.yc.network.YcNetworkConfig;
import com.google.gson.Gson;

import java.util.Map;

/*
 * 获取证照的所在的格子
 */

public class QryWorkGuide extends BaseRequest<RspWorkGuide> {

	public QryWorkGuide(ReqWorkGuide req,
			ResponseEventHandler<RspWorkGuide> listener) {
		
		super(Method.POST, YcNetworkConfig.YCServiceAddress + YcNetworkConfig.qryWorkGuide, getRequestParam(req), RspWorkGuide.class, listener);
	}
	
	
	private static String getRequestParam(ReqWorkGuide req)
    {
		Gson gson = new Gson();
		String parmas = gson.toJson(req);
		return parmas;
    }
	
	@Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }
	
}
