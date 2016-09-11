package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespAppInitModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

public class GetPayCodeRequest extends BaseRequest<BaseModel>
{
    public GetPayCodeRequest(String payId, int retry,
                             ResponseEventHandler<BaseModel> listener)
    {
        super(Method.GET, getRequestUrl(payId, retry), "",
                BaseModel.class, listener);
    }

    private static String getRequestUrl(String payId, int retry)
    {
        return String.format("%s/v1/pay/%s/balance/code?%s", NetworkConfig.payAddress, payId, getRequestParam(retry));
    }

    protected static String getRequestParam(int retry)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("source", "login"));
        list.add(new BasicNameValuePair("retry", String.valueOf(retry)));
        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }
}
