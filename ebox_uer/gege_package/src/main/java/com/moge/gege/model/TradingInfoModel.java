package com.moge.gege.model;

import java.io.Serializable;

public class TradingInfoModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String info;
    private String name;

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
