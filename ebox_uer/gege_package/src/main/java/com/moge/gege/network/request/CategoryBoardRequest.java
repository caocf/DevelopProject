package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespBoardListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * board category query
 */
public class CategoryBoardRequest extends BaseRequest<RespBoardListModel>
{
    private int mPosition = 0;

    public CategoryBoardRequest(int position, String categoryId, String cursor,
            ResponseEventHandler<RespBoardListModel> listener)
    {
        super(Method.GET, getRequestUrl(categoryId, cursor), "",
                RespBoardListModel.class, listener);
        this.setEnableCookie(true);
        mPosition = position;
    }

    private static String getRequestUrl(String categoryId, String cursor)
    {
        return NetworkConfig.generalAddress + "/v1/board/category/"
                + categoryId + "?cursor=" + cursor;
    }

    public int getPosition()
    {
        return mPosition;
    }

}
