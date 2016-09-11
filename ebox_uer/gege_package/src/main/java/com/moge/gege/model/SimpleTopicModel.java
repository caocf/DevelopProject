package com.moge.gege.model;

public class SimpleTopicModel
{
    private int count;
    private String _id;
    private String type_id;
    private int post_count;
    private String bid;
    private long activity_ts;
    long post_upts;
    long crts;
    int topic_type;
    int like_count;
    String osver;
    int marked;
    String post_uid;
    int offer_reward;
    String phonemodel;
    int toped;
    long upts;
    String osmodel;
    int favorite_count;

    private AttachmentModel attachments;
    private double geo[];
    private String author_uid;
    private String title;
    private String descript;

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public String getType_id()
    {
        return type_id;
    }

    public void setType_id(String type_id)
    {
        this.type_id = type_id;
    }

    public int getPost_count()
    {
        return post_count;
    }

    public void setPost_count(int post_count)
    {
        this.post_count = post_count;
    }

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public long getActivity_ts()
    {
        return activity_ts;
    }

    public void setActivity_ts(long activity_ts)
    {
        this.activity_ts = activity_ts;
    }

    public long getPost_upts()
    {
        return post_upts;
    }

    public void setPost_upts(long post_upts)
    {
        this.post_upts = post_upts;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public int getTopic_type()
    {
        return topic_type;
    }

    public void setTopic_type(int topic_type)
    {
        this.topic_type = topic_type;
    }

    public int getLike_count()
    {
        return like_count;
    }

    public void setLike_count(int like_count)
    {
        this.like_count = like_count;
    }

    public String getOsver()
    {
        return osver;
    }

    public void setOsver(String osver)
    {
        this.osver = osver;
    }

    public int getMarked()
    {
        return marked;
    }

    public void setMarked(int marked)
    {
        this.marked = marked;
    }

    public String getPost_uid()
    {
        return post_uid;
    }

    public void setPost_uid(String post_uid)
    {
        this.post_uid = post_uid;
    }

    public int getOffer_reward()
    {
        return offer_reward;
    }

    public void setOffer_reward(int offer_reward)
    {
        this.offer_reward = offer_reward;
    }

    public String getPhonemodel()
    {
        return phonemodel;
    }

    public void setPhonemodel(String phonemodel)
    {
        this.phonemodel = phonemodel;
    }

    public int getToped()
    {
        return toped;
    }

    public void setToped(int toped)
    {
        this.toped = toped;
    }

    public long getUpts()
    {
        return upts;
    }

    public void setUpts(long upts)
    {
        this.upts = upts;
    }

    public String getOsmodel()
    {
        return osmodel;
    }

    public void setOsmodel(String osmodel)
    {
        this.osmodel = osmodel;
    }

    public int getFavorite_count()
    {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count)
    {
        this.favorite_count = favorite_count;
    }

    public AttachmentModel getAttachments()
    {
        return attachments;
    }

    public void setAttachments(AttachmentModel attachments)
    {
        this.attachments = attachments;
    }

    public double[] getGeo()
    {
        return geo;
    }

    public void setGeo(double[] geo)
    {
        this.geo = geo;
    }

    public String getAuthor_uid()
    {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid)
    {
        this.author_uid = author_uid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

}
