package com.moge.gege.model;

import java.io.Serializable;

public class UnReadNoticeModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String notice_type;
    private int count;
    private String notice_name;
    private String notice_logo;
    private String notice_descript;

    public String getNotice_type()
    {
        return notice_type;
    }

    public void setNotice_type(String notice_type)
    {
        this.notice_type = notice_type;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String getNotice_name()
    {
        return notice_name;
    }

    public void setNotice_name(String notice_name)
    {
        this.notice_name = notice_name;
    }

    public String getNotice_logo()
    {
        return notice_logo;
    }

    public void setNotice_logo(String notice_logo)
    {
        this.notice_logo = notice_logo;
    }

    public String getNotice_descript()
    {
        return notice_descript;
    }

    public void setNotice_descript(String notice_descript)
    {
        this.notice_descript = notice_descript;
    }

}
