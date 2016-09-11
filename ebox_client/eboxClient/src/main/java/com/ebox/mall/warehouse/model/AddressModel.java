package com.ebox.mall.warehouse.model;

import java.io.Serializable;

public class AddressModel implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String delivery_address;
    private String name;
    private String mobile;
    private String delivery_name;
    private DistrictModel district;
    private int province_id;
    private String uid;
    private int city_id;
    private String _id;
    int delivery_id;
    String community_id;
    int district_id;
    String street_id;
    String address;
    int address_type; // 0 - 送到快递柜; 1 - 送到住址

    public String getDelivery_address()
    {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address)
    {
        this.delivery_address = delivery_address;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getDelivery_name()
    {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name)
    {
        this.delivery_name = delivery_name;
    }

    public DistrictModel getDistrict()
    {
        return district;
    }

    public void setDistrict(DistrictModel district)
    {
        this.district = district;
    }

    public int getProvince_id()
    {
        return province_id;
    }

    public void setProvince_id(int province_id)
    {
        this.province_id = province_id;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public int getCity_id()
    {
        return city_id;
    }

    public void setCity_id(int city_id)
    {
        this.city_id = city_id;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public int getDelivery_id()
    {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id)
    {
        this.delivery_id = delivery_id;
    }

    public String getCommunity_id()
    {
        return community_id;
    }

    public void setCommunity_id(String community_id)
    {
        this.community_id = community_id;
    }

    public int getDistrict_id()
    {
        return district_id;
    }

    public void setDistrict_id(int district_id)
    {
        this.district_id = district_id;
    }

    public String getStreet_id()
    {
        return street_id;
    }

    public void setStreet_id(String street_id)
    {
        this.street_id = street_id;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public int getAddress_type()
    {
        return address_type;
    }

    public void setAddress_type(int address_type)
    {
        this.address_type = address_type;
    }

}
