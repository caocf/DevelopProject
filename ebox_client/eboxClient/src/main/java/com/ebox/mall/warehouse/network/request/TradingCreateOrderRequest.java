package com.ebox.mall.warehouse.network.request;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.ebox.mall.warehouse.model.RespMerchantListModel;
import com.ebox.mall.warehouse.network.WHNetworkConfig;
import com.ebox.mall.warehouse.network.util.BaseRequest;
import com.ebox.Anetwork.ResponseEventHandler;

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
        return WHNetworkConfig.payAddress + "/v1/delivery/order";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
