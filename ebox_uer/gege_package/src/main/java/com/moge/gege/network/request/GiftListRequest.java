package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespGiftListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * gift list query
 */
public class GiftListRequest extends BaseRequest<RespGiftListModel>
{
    public GiftListRequest(String cursor,
            ResponseEventHandler<RespGiftListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "", RespGiftListModel.class,
                listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/gift" + "?cursor=" + cursor;
    }

}
