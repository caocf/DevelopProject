package com.moge.gege.model;

import java.io.Serializable;

public class GoodModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int price; // fen
    private int num;
    private int total_fee;
    private String trading_id;

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
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

    public String getTrading_id()
    {
        return trading_id;
    }

    public void setTrading_id(String trading_id)
    {
        this.trading_id = trading_id;
    }

}
