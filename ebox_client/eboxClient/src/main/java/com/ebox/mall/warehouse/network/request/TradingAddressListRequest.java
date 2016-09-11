package com.ebox.mall.warehouse.network.request;

import com.ebox.mall.warehouse.model.RespAddressListModel;
import com.ebox.mall.warehouse.network.WHNetworkConfig;
import com.ebox.mall.warehouse.network.util.BaseRequest;
import com.ebox.Anetwork.ResponseEventHandler;


/**
 * trading address query
 */
public class TradingAddressListRequest extends
        BaseRequest<RespAddressListModel>
{
    public TradingAddressListRequest(String terminal_code,
            ResponseEventHandler<RespAddressListModel> listener)
    {
        super(Method.GET, getRequestUrl(terminal_code), "", RespAddressListModel.class,
                listener);
    }

    private static String getRequestUrl(String terminal_code)
    {
        return WHNetworkConfig.generalAddress + "/v1/trading/address?terminal_code="+terminal_code;
    }

}
