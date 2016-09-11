package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.BaseModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * join or quit board
 */
public class JoinBoardRequest extends BaseRequest<BaseModel>
{
    public JoinBoardRequest(boolean join, String boardId,
            ResponseEventHandler<BaseModel> listener)
    {
        super(join ? Method.POST : Method.DELETE, getRequestUrl(boardId), "",
                BaseModel.class,
                listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String boardId)
    {
        return NetworkConfig.generalAddress + "/v1/user/board/" + boardId;
    }

}
