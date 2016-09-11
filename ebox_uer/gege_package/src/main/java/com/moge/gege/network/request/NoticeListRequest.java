package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespNoticeListModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * query chat list, heve read : false - not read ; true - all
 */
public class NoticeListRequest extends BaseRequest<RespNoticeListModel>
{
    public NoticeListRequest(boolean haveRead, String noticeType, String msgId,
            ResponseEventHandler<RespNoticeListModel> listener)
    {
        super(Method.GET, getRequestUrl(haveRead, noticeType, msgId), "",
                RespNoticeListModel.class, listener);
    }

    private static String getRequestUrl(boolean haveRead, String noticeType,
            String msgId)
    {
        if (haveRead)
        {
            return NetworkConfig.chatAddress + "/v1/notice" + "?notice_type="
                    + noticeType + "&msg_id=" + msgId + "&unread=0";
        }
        else
        {
            return NetworkConfig.chatAddress + "/v1/notice" + "?notice_type="
                    + noticeType;
        }

    }

}
