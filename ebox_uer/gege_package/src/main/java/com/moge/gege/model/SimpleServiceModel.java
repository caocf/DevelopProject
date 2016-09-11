package com.moge.gege.model;

public class SimpleServiceModel
{
    ServiceInfoModel info;
    private String _id;
    private int service_type;
    private int source_type;
    private int need_type;
    private String end_location;
    private long start_time;
    private long end_time;
    private String start_location;
    private float price;

    public ServiceInfoModel getInfo()
    {
        return info;
    }

    public void setInfo(ServiceInfoModel info)
    {
        this.info = info;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public int getService_type()
    {
        return service_type;
    }

    public void setService_type(int service_type)
    {
        this.service_type = service_type;
    }

    public int getSource_type()
    {
        return source_type;
    }

    public void setSource_type(int source_type)
    {
        this.source_type = source_type;
    }

    public int getNeed_type()
    {
        return need_type;
    }

    public void setNeed_type(int need_type)
    {
        this.need_type = need_type;
    }

    public String getEnd_location()
    {
        return end_location;
    }

    public void setEnd_location(String end_location)
    {
        this.end_location = end_location;
    }

    public long getStart_time()
    {
        return start_time;
    }

    public void setStart_time(long start_time)
    {
        this.start_time = start_time;
    }

    public long getEnd_time()
    {
        return end_time;
    }

    public void setEnd_time(long end_time)
    {
        this.end_time = end_time;
    }

    public String getStart_location()
    {
        return start_location;
    }

    public void setStart_location(String start_location)
    {
        this.start_location = start_location;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(float price)
    {
        this.price = price;
    }

}
