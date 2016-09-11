package com.moge.gege.network.request;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespAddressItemModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading address query
 */
public class TradingAddressAddRequest extends BaseRequest<RespAddressItemModel>
{
    public TradingAddressAddRequest(int pid, int cid, int did, String name,
            String mobile, String address,
            ResponseEventHandler<RespAddressItemModel> listener)
    {
        super(Method.POST, getRequestUrl(), getRequestParam(pid, cid, did,
                name, mobile, address), RespAddressItemModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/trading/address";
    }

    private static String getRequestParam(int pid, int cid, int did,
            String name, String mobile, String address)
    {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        list.add(new BasicNameValuePair("province_id", String.valueOf(pid)));
        list.add(new BasicNameValuePair("city_id", String.valueOf(cid)));
        list.add(new BasicNameValuePair("district_id", String.valueOf(did)));
        list.add(new BasicNameValuePair("name", name));
        list.add(new BasicNameValuePair("mobile", mobile));
        list.add(new BasicNameValuePair("address", address));

        return URLEncodedUtils.format(list, HTTP.UTF_8);
    }

}
