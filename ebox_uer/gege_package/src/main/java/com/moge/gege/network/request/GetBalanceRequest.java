package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespBalanceModel;
import com.moge.gege.model.RespTradingDetailModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * balance query
 */
public class GetBalanceRequest extends BaseRequest<RespBalanceModel>
{
    public GetBalanceRequest(ResponseEventHandler<RespBalanceModel> listener)
    {
        super(Method.GET, getRequestUrl(), "",
                RespBalanceModel.class, listener);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.payAddress + "/v1/balance";
    }

}
