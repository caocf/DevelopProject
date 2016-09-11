package com.moge.gege.model;

import java.io.Serializable;
import java.util.List;

public class MerchantModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private List<GoodModel> goods;
    private String order_id;
    private int delivery_fee;
    private int num;
    private int total_fee;
    private String merchant_id;

    public List<GoodModel> getGoods()
    {
        return goods;
    }

    public void setGoods(List<GoodModel> goods)
    {
        this.goods = goods;
    }

    public String getOrder_id()
    {
        return order_id;
    }

    public void setOrder_id(String order_id)
    {
        this.order_id = order_id;
    }

    public int getDelivery_fee()
    {
        return delivery_fee;
    }

    public void setDelivery_fee(int delivery_fee)
    {
        this.delivery_fee = delivery_fee;
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
}
