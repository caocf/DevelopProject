package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespNewBoardListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * hot board
 */
public class LatestBoardRequest extends BaseRequest<RespNewBoardListModel>
{
    public LatestBoardRequest(String cursor,
            ResponseEventHandler<RespNewBoardListModel> listener)
    {
        super(Method.GET, getRequestUrl(cursor), "",
                RespNewBoardListModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/board/latest" + "?cursor="
                + cursor;
    }
}
