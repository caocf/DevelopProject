package com.moge.gege.network.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespFinishOrderModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * pay order finish
 */
public class TradingFinishOrderRequest extends
        BaseRequest<RespFinishOrderModel>
{
    public TradingFinishOrderRequest(String payId, String content,
            ResponseEventHandler<RespFinishOrderModel> listener)
    {
        super(Method.POST, getRequestUrl(payId), content,
                RespFinishOrderModel.class, listener);
    }

    private static String getRequestUrl(String payId)
    {
        return NetworkConfig.payAddress + "/v1/pay/" + payId;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
