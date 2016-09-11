package com.moge.gege.model;

public class MemberModel
{
    private String uid;
    private int integration_level;
    private String bid;
    private int integration;
    private int is_manager;
    private int is_default;
    private int is_vip;
    private long crts;
    private String _id;
    private long upts;

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public int getIntegration_level()
    {
        return integration_level;
    }

    public void setIntegration_level(int integration_level)
    {
        this.integration_level = integration_level;
    }

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public int getIntegration()
    {
        return integration;
    }

    public void setIntegration(int integration)
    {
        this.integration = integration;
    }

    public int getIs_manager()
    {
        return is_manager;
    }

    public void setIs_manager(int is_manager)
    {
        this.is_manager = is_manager;
    }

    public int getIs_default()
    {
        return is_default;
    }

    public void setIs_default(int is_default)
    {
        this.is_default = is_default;
    }

    public int getIs_vip()
    {
        return is_vip;
    }

    public void setIs_vip(int is_vip)
    {
        this.is_vip = is_vip;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public long getUpts()
    {
        return upts;
    }

    public void setUpts(long upts)
    {
        this.upts = upts;
    }
}
