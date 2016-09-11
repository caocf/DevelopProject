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
 * trading address query
 */
public class TradingAddressDeleteRequest extends BaseRequest<BaseModel>
{

    private String mAddresId;

    public TradingAddressDeleteRequest(String addressId,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.DELETE, getRequestUrl(addressId), "", BaseModel.class, listener);
        mAddresId = addressId;
    }

    private static String getRequestUrl(String addressId)
    {
        return NetworkConfig.generalAddress + "/v1/trading/address/" + addressId;
    }

    public String getAddresId()
    {
        return mAddresId;
    }
}
