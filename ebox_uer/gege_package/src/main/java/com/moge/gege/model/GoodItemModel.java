package com.moge.gege.model;

import java.io.Serializable;

public class GoodItemModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String uid;
    private String order_id;
    private int price;
    private String mobile;
    private int num;
    private int total_fee;
    private String merchant_id;
    private String trading_id;
    private TradingModel trading;

    public TradingModel getTrading()
    {
        return trading;
    }

    public void setTrading(TradingModel trading)
    {
        this.trading = trading;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getOrder_id()
    {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getTotal_fee()
    {
        return total_fee;
    }

    public void setTotal_fee(int total_fee)
    {
        this.total_fee = total_fee;
    }

    public String getMerchant_id()
    {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id)
    {
        this.merchant_id = merchant_id;
    }

    public String getTrading_id()
    {
        return trading_id;
    }

    public void setTrading_id(String trading_id)
    {
        this.trading_id = trading_id;
    }

}
