package com.moge.gege.network.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespMerchantListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading create order
 */
public class TradingCreateOrderRequest extends
        BaseRequest<RespMerchantListModel>
{
    public TradingCreateOrderRequest(String content,
            ResponseEventHandler<RespMerchantListModel> listener)
    {
        super(Method.POST, getRequestUrl(), content,
                RespMerchantListModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.payAddress + "/v1/order";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
