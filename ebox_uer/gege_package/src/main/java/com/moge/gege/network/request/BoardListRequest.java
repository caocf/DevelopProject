package com.moge.gege.network.request;

import android.text.TextUtils;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespMyBoardListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * my join board, bodyType: 0 - all; 1 - owner board; 2 - interest board
 * 
 */
public class BoardListRequest extends BaseRequest<RespMyBoardListModel>
{
    public BoardListRequest(String uid, int boardType,
            ResponseEventHandler<RespMyBoardListModel> listener)
    {
        super(Method.GET, getRequestUrl(uid, boardType), "",
                RespMyBoardListModel.class, listener);
        this.setEnableCookie(true);
    }

    private static String getRequestUrl(String uid, int boardType)
    {
        if (TextUtils.isEmpty(uid))
        {
            return NetworkConfig.generalAddress + "/v1/user/board";
        }
        else
        {
            return NetworkConfig.generalAddress + "/v1/user/" + uid
                    + "/board?board_type=" + boardType;
        }
    }
}
