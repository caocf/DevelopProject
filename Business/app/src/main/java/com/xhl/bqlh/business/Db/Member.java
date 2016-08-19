package com.xhl.bqlh.business.Db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Sum on 16/6/2.
 * 我的会员表
 */
@Table(name = "member")
public class Member implements Serializable{

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "retailerId")
    private String retailerId;//零售商id

    @Column(name = "state")
    private int state;//1 会员待审核，2 会员通过 3.会员拒绝

    public Member() {
    }

    public Member(String retailerId) {
        this.retailerId = retailerId;
        this.state = 0;
    }

    public Member(String retailerId, int state) {
        this.retailerId = retailerId;
        this.state = state;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
