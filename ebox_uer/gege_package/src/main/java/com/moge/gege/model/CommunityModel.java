package com.moge.gege.model;

import java.io.Serializable;

public class CommunityModel extends BaseCommunityModel implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int pid;// 省ID
    private String province;// 省名称
    private int cid;// 市ID
    private String city;// 市名称
    private int did;// 地区ID
    private String district;// 地区名称
    private String street;// 小区所在街道
    private String address;// 小区地址
    private double geo[]; // 小区所在经纬度

    public int getPid()
    {
        return pid;
    }

    public void setPid(int pid)
    {
        this.pid = pid;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public int getCid()
    {
        return cid;
    }

    public void setCid(int cid)
    {
        this.cid = cid;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public int getDid()
    {
        return did;
    }

    public void setDid(int did)
    {
        this.did = did;
    }

    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public double[] getGeo()
    {
        return geo;
    }

    public void setGeo(double[] geo)
    {
        this.geo = geo;
    }

}
