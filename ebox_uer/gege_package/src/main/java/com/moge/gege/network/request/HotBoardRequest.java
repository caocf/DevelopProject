package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespBoardListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * hot board
 */
public class HotBoardRequest extends BaseRequest<RespBoardListModel>
{
    public HotBoardRequest(String cursor,
            ResponseEventHandler<RespBoardListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "", RespBoardListModel.class,
                listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/board/hot" + "?cursor="
                + cursor;
    }
}
