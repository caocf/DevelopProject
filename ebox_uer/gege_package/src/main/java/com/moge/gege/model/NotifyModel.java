package com.moge.gege.model;

import java.io.Serializable;

public class NotifyModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    // "uid":string, 用户ID
    // "title":string 通知标题（可空）
    // "content":string 通知内容
    // "content_type":string 内容类型
    // "notify_type":string 通知类型
    // "notify_name":string, 通知标题
    // "notify_logo":string, 通知logo
    // "notify_descript":string, 通知描述
    // "level":int, 通知级别
    // "crts":int 通知发送时间
    // "rdts":int 通知阅读时间

    private String _id;
    private String uid;
    private String title;
    private String content;
    private String content_type;
    private String notify_type;
    private String notify_name;
    private String notify_logo;
    private String notify_descript;
    private int level;
    private long crts;
    private long rdts;

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent_type()
    {
        return content_type;
    }

    public void setContent_type(String content_type)
    {
        this.content_type = content_type;
    }

    public String getNotify_type()
    {
        return notify_type;
    }

    public void setNotify_type(String notify_type)
    {
        this.notify_type = notify_type;
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

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public long getRdts()
    {
        return rdts;
    }

    public void setRdts(long rdts)
    {
        this.rdts = rdts;
    }

}
