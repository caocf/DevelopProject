package com.moge.gege.model;

public class UserCouponModel
{
    private String _id;
    private long start_time;
    private long expried_time;
    private boolean expried;
    private String coupon_id;
    private String code;
    private CouponModel coupon;

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public long getStart_time()
    {
        return start_time;
    }

    public void setStart_time(long start_time)
    {
        this.start_time = start_time;
    }

    public long getExpried_time()
    {
        return expried_time;
    }

    public void setExpried_time(long expried_time)
    {
        this.expried_time = expried_time;
    }

    public boolean isExpried()
    {
        return expried;
    }

    public void setExpried(boolean expried)
    {
        this.expried = expried;
    }

    public String getCoupon_id()
    {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id)
    {
        this.coupon_id = coupon_id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public CouponModel getCoupon()
    {
        return coupon;
    }

    public void setCoupon(CouponModel coupon)
    {
        this.coupon = coupon;
    }

}
