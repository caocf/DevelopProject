package com.moge.gege.model;

import java.util.List;

public class PayResultModel
{
    private int pay_type;
    private List<String> order_ids;
    private String pay_id;
    private int total_fee;
    private int total_num;
    private PayOrderInfoModel order_info;

    public int getPay_type()
    {
        return pay_type;
    }

    public void setPay_type(int pay_type)
    {
        this.pay_type = pay_type;
    }

    public List<String> getOrder_ids()
    {
        return order_ids;
    }

    public void setOrder_ids(List<String> order_ids)
    {
        this.order_ids = order_ids;
    }

    public String getPay_id()
    {
        return pay_id;
    }

    public void setPay_id(String pay_id)
    {
        this.pay_id = pay_id;
    }

    public int getTotal_fee()
    {
        return total_fee;
    }

    public void setTotal_fee(int total_fee)
    {
        this.total_fee = total_fee;
    }

    public int getTotal_num()
    {
        return total_num;
    }

    public void setTotal_num(int total_num)
    {
        this.total_num = total_num;
    }

    public PayOrderInfoModel getOrder_info()
    {
        return order_info;
    }

    public void setOrder_info(PayOrderInfoModel order_info)
    {
        this.order_info = order_info;
    }

}
