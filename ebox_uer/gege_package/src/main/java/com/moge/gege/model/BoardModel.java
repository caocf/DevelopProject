package com.moge.gege.model;

import java.util.List;

public class BoardModel extends BaseBoardModel
{
    private String _id;// 圈子ID
    private String name;// 圈子名称
    private String descript;// 圈子简介
    private String creator_uid;// 创建者ID
    private String manager_uid;// 管理用户ID
    private String logo;// 圈子logo
    private int theme;// 圈子主题ID
    private int type;// 圈子类型: 1 - 业主圈； 2 - 兴趣圈
    private String category;// 圈子类别
    private int province_id;// 省
    private int city_id;// 市
    private String community_id;// 小区ID
    private double geo[]; // 圈子所在经纬度
    private int topic_count;// 帖子数
    private int member_count;// 用户数
    private long crts;// 创建时间
    private long upts;// 更新时间
    private double distance;
    private List<String> managers;

    private boolean isBoardMember;

    public boolean isBoardMember()
    {
        return isBoardMember;
    }

    public void setBoardMember(boolean isBoardMember)
    {
        this.isBoardMember = isBoardMember;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public String getCreator_uid()
    {
        return creator_uid;
    }

    public void setCreator_uid(String creator_uid)
    {
        this.creator_uid = creator_uid;
    }

    public String getManager_uid()
    {
        return manager_uid;
    }

    public void setManager_uid(String manager_uid)
    {
        this.manager_uid = manager_uid;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public int getTheme()
    {
        return theme;
    }

    public void setTheme(int theme)
    {
        this.theme = theme;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getProvince_id()
    {
        return province_id;
    }

    public void setProvince_id(int province_id)
    {
        this.province_id = province_id;
    }

    public int getCity_id()
    {
        return city_id;
    }

    public void setCity_id(int city_id)
    {
        this.city_id = city_id;
    }

    public String getCommunity_id()
    {
        return community_id;
    }

    public void setCommunity_id(String community_id)
    {
        this.community_id = community_id;
    }

    public double[] getGeo()
    {
        return geo;
    }

    public void setGeo(double[] geo)
    {
        this.geo = geo;
    }

    public int getTopic_count()
    {
        return topic_count;
    }

    public void setTopic_count(int topic_count)
    {
        this.topic_count = topic_count;
    }

    public int getMember_count()
    {
        return member_count;
    }

    public void setMember_count(int member_count)
    {
        this.member_count = member_count;
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

    public double getDistance()
    {
        return distance;
    }

    public void setDistance(double distance)
    {
        this.distance = distance;
    }

    public List<String> getManagers()
    {
        return managers;
    }

    public void setManagers(List<String> managers)
    {
        this.managers = managers;
    }

}
