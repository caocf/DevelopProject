package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

public class ScanOpenDoorRequest extends BaseRequest<BaseModel>
{
    public ScanOpenDoorRequest(String access_token, String order_id,
                               ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(), getRequestParam(access_token, order_id),
                BaseModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/user/delivery/terminal/box/open";
    }

    private static String getRequestParam(String access_token, String order_id)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("access_token", access_token));
        list.add(new BasicNameValuePair("order_id", order_id));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
