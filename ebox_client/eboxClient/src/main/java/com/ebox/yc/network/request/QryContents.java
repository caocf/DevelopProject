package com.ebox.yc.network.request;

import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.yc.model.ReqAnnounce;
import com.ebox.yc.model.RspAnnounce;
import com.ebox.yc.network.BaseRequest;
import com.ebox.yc.network.YcNetworkConfig;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;


/*
 * 获取证照的所在的格子
 */

public class QryContents extends BaseRequest<RspAnnounce> {

	public QryContents(ReqAnnounce req,
			ResponseEventHandler<RspAnnounce> listener) {
		
		super(Method.POST, YcNetworkConfig.YCServiceAddress + YcNetworkConfig.qryContents, getRequestParam(req), RspAnnounce.class, listener);
	}
	
	
	private static String getRequestParam(ReqAnnounce req)
    {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("ccId", req.getCcId()+""));
        list.add(new BasicNameValuePair("nodeId", req.getNodeId()+""));
        list.add(new BasicNameValuePair("channel", req.getChannel()+""));
        list.add(new BasicNameValuePair("pageIndex", req.getPageIndex()+""));
        list.add(new BasicNameValuePair("pageSize", req.getPageSize()+""));

        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
	
}
