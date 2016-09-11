package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespGiftListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * other gift list query
 */
public class OtherGiftListRequest extends BaseRequest<RespGiftListModel>
{
    public OtherGiftListRequest(String uid, String cursor,
            ResponseEventHandler<RespGiftListModel> listener)
    {
        super(Method.GET, getRequestUrl(uid, cursor), "",
                RespGiftListModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String uid, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/user/" + uid + "/gift"
                + "?cursor=" + cursor;
    }

}
