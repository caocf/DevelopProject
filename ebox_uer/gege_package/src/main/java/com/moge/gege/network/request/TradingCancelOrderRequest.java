package com.moge.gege.network.request;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * trading cancel order
 */
public class TradingCancelOrderRequest extends BaseRequest<BaseModel>
{
    public TradingCancelOrderRequest(List<String> orderList,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(), getRequestParam(orderList),
                BaseModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.payAddress + "/v1/order/cancel";
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private static String getRequestParam(List<String> orderList)
    {
        String params = "";
        try
        {
            JSONObject orderObject = new JSONObject();
            JSONArray orderArray = new JSONArray();
            for (String order : orderList)
            {
                orderArray.put(order);
            }
            orderObject.put("order_ids", orderArray);
            params = orderObject.toString();
        }
        catch (JSONException e)
        {

        }

        return params;
    }
}
