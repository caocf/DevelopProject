package com.moge.gege.model;

import java.io.Serializable;

public class UnReadNotifyModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String notify_type;
    private int count;
    private String notify_name;
    private String notify_logo;
    private String notify_descript;

    public String getNotify_type()
    {
        return notify_type;
    }

    public void setNotify_type(String notify_type)
    {
        this.notify_type = notify_type;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String getNotify_name()
    {
        return notify_name;
    }

    public void setNotify_name(String notify_name)
    {
        this.notify_name = notify_name;
    }

    public String getNotify_logo()
    {
        return notify_logo;
    }

    public void setNotify_logo(String notify_logo)
    {
        this.notify_logo = notify_logo;
    }

    public String getNotify_descript()
    {
        return notify_descript;
    }

    public void setNotify_descript(String notify_descript)
    {
        this.notify_descript = notify_descript;
    }

}
