package com.moge.gege.model;

import java.io.Serializable;

public class NewMessageModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private boolean haveRead;
    private String avatar;
    private int count;
    private String title;
    private String content;
    private long time;
    private boolean isNotify;

    // tag for message
    int msgType;
    private String tag; // uid,notice_type,notify_type

    public NewMessageModel()
    {
        haveRead = false;
        avatar = "";
        count = 0;
        title = "";
        content = "";
        time = 0;
        isNotify = true;
    }

    public boolean isHaveRead()
    {
        return haveRead;
    }

    public void setHaveRead(boolean haveRead)
    {
        this.haveRead = haveRead;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
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

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public int getMsgType()
    {
        return msgType;
    }

    public void setMsgType(int msgType)
    {
        this.msgType = msgType;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public boolean isNotify()
    {
        return isNotify;
    }

    public void setNotify(boolean isNotify)
    {
        this.isNotify = isNotify;
    }

}
