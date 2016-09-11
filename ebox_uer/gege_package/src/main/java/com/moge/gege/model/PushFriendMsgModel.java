package com.moge.gege.model;

import java.io.Serializable;

public class PushFriendMsgModel extends BasePushModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private FriendMsgModel data;

    public FriendMsgModel getData()
    {
        return data;
    }

    public void setData(FriendMsgModel data)
    {
        this.data = data;
    }

}
