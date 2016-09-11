package com.moge.gege.model;

import java.util.List;

public class ConvenienceListModel
{
    private List<ConvenienceModel> services;
    private String previous_cursor;
    private String next_cursor;

    public List<ConvenienceModel> getServices()
    {
        return services;
    }

    public void setServices(List<ConvenienceModel> services)
    {
        this.services = services;
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
