package com.ebox.mall.warehouse.network.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.ebox.mall.warehouse.model.RespPayOrderModel;
import com.ebox.mall.warehouse.network.WHNetworkConfig;
import com.ebox.mall.warehouse.network.util.BaseRequest;
import com.ebox.Anetwork.ResponseEventHandler;


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
        return WHNetworkConfig.payAddress + "/v1/delivery/pay";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
