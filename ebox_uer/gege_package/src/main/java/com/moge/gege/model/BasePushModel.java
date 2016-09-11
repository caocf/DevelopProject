package com.moge.gege.model;

import java.io.Serializable;

public class BasePushModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String action;
    private int status;
    private String msg;
    private String version;

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

}
