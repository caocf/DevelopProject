package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * board up
 */
public class BoardUpRequest extends BaseRequest<BaseModel>
{
    public BoardUpRequest(String boardId,
            ResponseEventHandler<BaseModel> listener)
    {
        super(Method.POST, getRequestUrl(boardId), "", BaseModel.class,
                listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String boardId)
    {
        return NetworkConfig.generalAddress + "/v1/board/apply/" + boardId
                + "/up";
    }
}
