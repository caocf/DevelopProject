package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespAppInitModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

public class GetCheckCodeRequest extends BaseRequest<RespAppInitModel>
{
    public GetCheckCodeRequest(String mobile, int retry,
            ResponseEventHandler<RespAppInitModel> listener)
    {
        super(Method.GET, getRequestUrl(mobile, retry), "",
                RespAppInitModel.class, listener);
    }

    private static String getRequestUrl(String mobile, int retry)
    {
        return NetworkConfig.generalAddress + "/v1/mobile/send/code/" + mobile
                + "?" + getRequestParam(retry);
    }

    protected static String getRequestParam(int retry)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("source", "login"));
        list.add(new BasicNameValuePair("retry", String.valueOf(retry)));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
