package com.moge.gege.model;

import java.util.List;

public class ServiceListModel
{
    // private List<ServiceModel> livings;
    private List<TopicModel> livings;
    private String previous_cursor;
    private String next_cursor;

    // public List<ServiceModel> getLivings()
    // {
    // return livings;
    // }
    //
    // public void setLivings(List<ServiceModel> livings)
    // {
    // this.livings = livings;
    // }

    public List<TopicModel> getLivings()
    {
        return livings;
    }

    public void setLivings(List<TopicModel> livings)
    {
        this.livings = livings;
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
