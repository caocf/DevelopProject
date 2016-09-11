package com.moge.gege.model;

public class CouponInfoModel
{
    private String _id;
    private String coupon_id;
    private boolean expried;
    private CouponModel coupon;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public boolean isExpried() {
        return expried;
    }

    public void setExpried(boolean expried) {
        this.expried = expried;
    }

    public CouponModel getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponModel coupon) {
        this.coupon = coupon;
    }
}
