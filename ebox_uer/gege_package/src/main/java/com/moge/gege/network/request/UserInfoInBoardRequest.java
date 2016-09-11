package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespMemberInfoModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * user info in board
 */
public class UserInfoInBoardRequest extends BaseRequest<RespMemberInfoModel>
{
    public UserInfoInBoardRequest(String boardId,
            ResponseEventHandler<RespMemberInfoModel> listener)
    {
        super(Method.GET, getRequestUrl(boardId), "",
                RespMemberInfoModel.class, listener);
    }

    private static String getRequestUrl(String boardId)
    {
        return NetworkConfig.generalAddress + "/v1/user/board/" + boardId;
    }

}
