package com.moge.gege.model;

import java.io.Serializable;

public class PushNotifyModel extends BasePushModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private NotifyModel data;

    public NotifyModel getData()
    {
        return data;
    }

    public void setData(NotifyModel data)
    {
        this.data = data;
    }

}
