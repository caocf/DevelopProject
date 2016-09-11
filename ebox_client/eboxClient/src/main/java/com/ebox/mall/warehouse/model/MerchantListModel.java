package com.ebox.mall.warehouse.model;

import java.util.List;

public class MerchantListModel
{
    private List<MerchantModel> merchants;
    private int total_fee;
    private int total_num;

    public List<MerchantModel> getMerchants()
    {
        return merchants;
    }

    public void setMerchants(List<MerchantModel> merchants)
    {
        this.merchants = merchants;
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
}
