package com.moge.gege.model;

import java.io.Serializable;

public class PushChatModel extends BasePushModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private MessageModel data;

    public MessageModel getData()
    {
        return data;
    }

    public void setData(MessageModel data)
    {
        this.data = data;
    }

}
