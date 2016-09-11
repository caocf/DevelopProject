package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespCategoryListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * board category query
 */
public class BoardCategoryRequest extends BaseRequest<RespCategoryListModel>
{
    public BoardCategoryRequest(
            ResponseEventHandler<RespCategoryListModel> listener)
    {
        super(Method.GET, getRequestUrl(), "", RespCategoryListModel.class,
                listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl()
    {
        return NetworkConfig.generalAddress + "/v1/board/category";
    }

}
