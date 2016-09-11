package com.moge.gege.model;

import java.io.Serializable;

public class NoticeModel implements Serializable
{
    // "uid":string, 用户ID
    // "title":string 提醒标题（可空）
    // "content":string 提醒内容
    // "content_type":string 内容格式
    // "notice_type":string 提醒类型
    // "notice_name":string, 提醒名称
    // "notice_logo":string, 提醒logo
    // "notice_descript":string, 提醒描述
    // "level":int, 提醒级别
    // "crts":int 提醒发送时间
    // "rdts":int 提醒阅读时间

    private static final long serialVersionUID = 1L;
    private String uid;
    private String title;
    private String content;
    private String content_type;
    private String notice_type;
    private String notice_name;
    private String notice_logo;
    private String notice_descript;
    private int level;
    private long crts;
    private long rdts;

    private String _id;

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

    public String getNotice_type()
    {
        return notice_type;
    }

    public void setNotice_type(String notice_type)
    {
        this.notice_type = notice_type;
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
