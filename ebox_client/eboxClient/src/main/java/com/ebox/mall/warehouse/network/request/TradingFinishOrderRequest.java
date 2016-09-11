package com.ebox.mall.warehouse.network.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.ebox.mall.warehouse.model.RespFinishOrderModel;
import com.ebox.mall.warehouse.network.WHNetworkConfig;
import com.ebox.mall.warehouse.network.util.BaseRequest;
import com.ebox.Anetwork.ResponseEventHandler;

/**
 * pay order finish
 */
public class TradingFinishOrderRequest extends
        BaseRequest<RespFinishOrderModel>
{
    public TradingFinishOrderRequest(String orderId, String content,
            ResponseEventHandler<RespFinishOrderModel> listener)
    {
        super(Method.POST, getRequestUrl(orderId), content,
                RespFinishOrderModel.class, listener);
    }

    private static String getRequestUrl(String orderId)
    {
        return WHNetworkConfig.payAddress + "/v1/delivery/pay/" + orderId;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
