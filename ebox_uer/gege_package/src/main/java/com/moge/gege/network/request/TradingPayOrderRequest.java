package com.moge.gege.network.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespPayOrderModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading pay order
 */
public class TradingPayOrderRequest extends BaseRequest<RespPayOrderModel>
{
    public TradingPayOrderRequest(String content,
            ResponseEventHandler<RespPayOrderModel> listener)
    {
        super(Method.POST, getRequestUrl(), content, RespPayOrderModel.class,
                listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.payAddress + "/v1/pay";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
