package com.moge.gege.model;

import java.io.Serializable;

public class PushNoticeModel extends BasePushModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private NoticeModel data;

    public NoticeModel getData()
    {
        return data;
    }

    public void setData(NoticeModel data)
    {
        this.data = data;
    }

}
