package com.moge.gege.model;

public class NewBoardModel
{
    private String category;// 圈子类别
    private int status;
    private String name;// 圈子名称
    private String creator_uid;// 创建者ID
    private long crts;// 创建时间
    private long upts;// 更新时间
    private String descript;// 圈子简介
    private String logo;// 圈子logo
    private String _id;// 圈子ID
    private double geo[]; // 圈子所在经纬度
    private int up_count;

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCreator_uid()
    {
        return creator_uid;
    }

    public void setCreator_uid(String creator_uid)
    {
        this.creator_uid = creator_uid;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public long getUpts()
    {
        return upts;
    }

    public void setUpts(long upts)
    {
        this.upts = upts;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public double[] getGeo()
    {
        return geo;
    }

    public void setGeo(double[] geo)
    {
        this.geo = geo;
    }

    public int getUp_count()
    {
        return up_count;
    }

    public void setUp_count(int up_count)
    {
        this.up_count = up_count;
    }

}
