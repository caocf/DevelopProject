package com.ebox.yc.network.request;

import com.android.volley.AuthFailureError;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.yc.model.ReqWorkItems;
import com.ebox.yc.model.RspWorkItems;
import com.ebox.yc.network.BaseRequest;
import com.ebox.yc.network.YcNetworkConfig;
import com.google.gson.Gson;

import java.util.Map;

/*
 * 获取证照的所在的格子
 */

public class QryWorkItems extends BaseRequest<RspWorkItems> {

	public QryWorkItems(ReqWorkItems req,
			ResponseEventHandler<RspWorkItems> listener) {
		
		super(Method.POST, YcNetworkConfig.YCServiceAddress + YcNetworkConfig.qryWorkItems, getRequestParam(req), RspWorkItems.class, listener);
	}
	
	
	private static String getRequestParam(ReqWorkItems req)
    {
//		List<NameValuePair> list = new ArrayList<NameValuePair>();
//        list.add(new BasicNameValuePair("channel", req.getChannel()+""));
//        list.add(new BasicNameValuePair("windowId", req.getWindowId()));
//        list.add(new BasicNameValuePair("pageIndex", req.getPageIndex()+""));
//        list.add(new BasicNameValuePair("pageSize", req.getPageSize()+""));
//
//        return URLEncodedUtils.format(list, HTTP.UTF_8);
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
