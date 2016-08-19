package com.xhl.bqlh.business.Model;

import com.xhl.bqlh.business.Db.Member;

/**
 * Created by Sum on 16/5/4.
 */
public class ShopFriendsModel {

    private Member member;

    private String id;//主键

    private String distributorId;//经销商id

    private String retailerId;//零售商id

    private String retailerCompanyName;//零售商企业名称

    private String retailerName;//零售商责任人姓名

    private String retailerPhone;//零售商责任人电话

    private String retailerAddress;//零售商店铺地址

    private String regId;//注册id关联bqlh_user

    private int state;//1为待审核，2为通过审核 3.拒绝

    private String salseManId;//存储是那个业务员为经销商添加的会员好友

    public String getId() {
        return id;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public String getRetailerCompanyName() {
        return retailerCompanyName;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public String getRetailerPhone() {
        return retailerPhone;
    }

    public String getRetailerAddress() {
        return retailerAddress;
    }

    public int getState(){
        return state;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
