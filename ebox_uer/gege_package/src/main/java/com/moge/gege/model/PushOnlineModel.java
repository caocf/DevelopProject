package com.moge.gege.model;

import java.io.Serializable;

public class PushOnlineModel extends BasePushModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private OnlineModel data;

    public OnlineModel getData()
    {
        return data;
    }

    public void setData(OnlineModel data)
    {
        this.data = data;
    }

}
