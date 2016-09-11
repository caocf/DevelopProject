package com.moge.gege.model;

import java.util.List;

public class FeedModel
{
    private int status;
    private String uid;
    private List<String> images;
    private String title;
    private String bid;
    private String descript;
    private long crts;
    private long upts;
    private int topic_type;
    private String tid;
    private int city_id;
    private String feeds_type;
    private String _id;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public List<String> getImages()
    {
        return images;
    }

    public void setImages(List<String> images)
    {
        this.images = images;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public long getUpts()
    {
        return upts;
    }

    public void setUpts(long upts)
    {
        this.upts = upts;
    }

    public int getTopic_type()
    {
        return topic_type;
    }

    public void setTopic_type(int topic_type)
    {
        this.topic_type = topic_type;
    }

    public String getTid()
    {
        return tid;
    }

    public void setTid(String tid)
    {
        this.tid = tid;
    }

    public int getCity_id()
    {
        return city_id;
    }

    public void setCity_id(int city_id)
    {
        this.city_id = city_id;
    }

    public String getFeeds_type()
    {
        return feeds_type;
    }

    public void setFeeds_type(String feeds_type)
    {
        this.feeds_type = feeds_type;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

}
