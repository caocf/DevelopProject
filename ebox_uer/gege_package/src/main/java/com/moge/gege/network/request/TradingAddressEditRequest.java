package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.model.RespAddressItemModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

/**
 * trading address edit
 */
public class TradingAddressEditRequest extends BaseRequest<BaseModel>
{
    public TradingAddressEditRequest(String addressId, int pid, int cid, int did, String name,
                                     String mobile, String address,
                                     ResponseEventHandler<BaseModel> listener)
    {
        super(Method.PUT, getRequestUrl(addressId), getRequestParam(pid, cid, did,
                name, mobile, address), BaseModel.class, listener);
    }

    private static String getRequestUrl(String addressId)
    {
        return NetworkConfig.generalAddress + "/v1/trading/address/" + addressId;
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
