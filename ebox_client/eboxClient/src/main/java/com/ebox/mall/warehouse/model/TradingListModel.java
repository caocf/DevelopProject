package com.ebox.mall.warehouse.model;

import java.util.List;

public class TradingListModel
{
    private List<TradingModel> tradings;
    private String previous_cursor;
    private String next_cursor;

    public List<TradingModel> getTradings()
    {
        return tradings;
    }

    public void setTradings(List<TradingModel> tradings)
    {
        this.tradings = tradings;
    }

    public String getPrevious_cursor()
    {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor)
    {
        this.previous_cursor = previous_cursor;
    }

    public String getNext_cursor()
    {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor)
    {
        this.next_cursor = next_cursor;
    }

}
