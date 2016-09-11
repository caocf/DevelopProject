package com.ebox.yc.network.request;

import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.yc.model.ReqAnnDetail;
import com.ebox.yc.model.RspAnnDetail;
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

public class QryContentDetail extends BaseRequest<RspAnnDetail> {

	public QryContentDetail(ReqAnnDetail req,
			ResponseEventHandler<RspAnnDetail> listener) {
		
		super(Method.POST, YcNetworkConfig.YCServiceAddress+ YcNetworkConfig.qryContentDetail, getRequestParam(req), RspAnnDetail.class, listener);
	}
	
	
	private static String getRequestParam(ReqAnnDetail req)
    {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("contentId", req.getContentId()));
        list.add(new BasicNameValuePair("nodeId", req.getNodeId()+""));
        list.add(new BasicNameValuePair("channel", req.getChannel()+""));

        return URLEncodedUtils.format(list, HTTP.UTF_8);

    }

	
}
