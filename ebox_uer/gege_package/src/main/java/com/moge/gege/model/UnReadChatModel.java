package com.moge.gege.model;

import java.io.Serializable;

public class UnReadChatModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String from_uid;
    private int count;

    public String getFrom_uid()
    {
        return from_uid;
    }

    public void setFrom_uid(String from_uid)
    {
        this.from_uid = from_uid;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

}
