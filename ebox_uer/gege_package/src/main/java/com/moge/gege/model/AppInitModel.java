package com.moge.gege.model;

public class AppInitModel
{
    private String newest_version;
    private int update;
    private String url;
    private String msg;
    private ConfigModel config;

    public ConfigModel getConfig()
    {
        return config;
    }

    public void setConfig(ConfigModel config)
    {
        this.config = config;
    }

    public String getNewest_version()
    {
        return newest_version;
    }

    public void setNewest_version(String newest_version)
    {
        this.newest_version = newest_version;
    }

    public int getUpdate()
    {
        return update;
    }

    public void setUpdate(int update)
    {
        this.update = update;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

}
