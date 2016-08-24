package com.xhl.world.model;

import java.io.Serializable;

/**
 * Created by Sum on 15/12/28.
 */
public class AddressModel implements Serializable {

    private String id;
    private String userId;
    private String area;
    private String address;
    private String consigneeName;
    private String postCode;
    private String telephone;
    private String idCard;
    private String defaultAddress; //1 默认 0 非默认

    private String result;//增删改数据操作结果 200：成功

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getArea() {
        return area;
    }

    public String getAddress() {
        return address;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public boolean isDefault() {
        return defaultAddress.equals("1");
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
