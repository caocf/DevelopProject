package com.moge.gege.model;

import java.io.Serializable;

public class PushChatSendResultModel extends BasePushModel implements
        Serializable
{
    private static final long serialVersionUID = 1L;

    private ChatSendResultModel data;

    public ChatSendResultModel getData()
    {
        return data;
    }

    public void setData(ChatSendResultModel data)
    {
        this.data = data;
    }

}
