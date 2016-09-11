package com.moge.gege.model;

import java.util.List;

public class TradingBuyListModel
{
    private int buy_count;
    private int user_buy_count;
    private List<UserModel> users;

    public int getBuy_count()
    {
        return buy_count;
    }

    public void setBuy_count(int buy_count)
    {
        this.buy_count = buy_count;
    }

    public int getUser_buy_count()
    {
        return user_buy_count;
    }

    public void setUser_buy_count(int user_buy_count)
    {
        this.user_buy_count = user_buy_count;
    }

    public List<UserModel> getUsers()
    {
        return users;
    }

    public void setUsers(List<UserModel> users)
    {
        this.users = users;
    }

}
