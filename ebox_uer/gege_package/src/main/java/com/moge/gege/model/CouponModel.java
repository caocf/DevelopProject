package com.moge.gege.model;

import java.io.Serializable;

public class CouponModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int expried_type;
    private int count;
    private String code;
    private int per_count;
    private String name;
    private int coupon_type;
    private int fee;
    private long start_time;
    private String descript;
    private int receive_type;
    private int min_total_fee;
    private int status;
    private int used_count;
    private String logo;
    private long expried_time;
    private String _id;

    public int getExpried_type()
    {
        return expried_type;
    }

    public void setExpried_type(int expried_type)
    {
        this.expried_type = expried_type;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getPer_count()
    {
        return per_count;
    }

    public void setPer_count(int per_count)
    {
        this.per_count = per_count;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getCoupon_type()
    {
        return coupon_type;
    }

    public void setCoupon_type(int coupon_type)
    {
        this.coupon_type = coupon_type;
    }

    public int getFee()
    {
        return fee;
    }

    public void setFee(int fee)
    {
        this.fee = fee;
    }

    public long getStart_time()
    {
        return start_time;
    }

    public void setStart_time(long start_time)
    {
        this.start_time = start_time;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public int getReceive_type()
    {
        return receive_type;
    }

    public void setReceive_type(int receive_type)
    {
        this.receive_type = receive_type;
    }

    public int getMin_total_fee()
    {
        return min_total_fee;
    }

    public void setMin_total_fee(int min_total_fee)
    {
        this.min_total_fee = min_total_fee;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getUsed_count()
    {
        return used_count;
    }

    public void setUsed_count(int used_count)
    {
        this.used_count = used_count;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public long getExpried_time()
    {
        return expried_time;
    }

    public void setExpried_time(long expried_time)
    {
        this.expried_time = expried_time;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

}
