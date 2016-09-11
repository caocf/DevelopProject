package com.moge.gege.model;

public class SimpleActivityModel
{
    private float fee;
    private long start_time;
    private int source_type;
    private int num;
    private String location;
    private long apply_start_time;
    private long apply_end_time;
    private String _id;
    private long end_time;

    private String category;
    private int apply_num;

    public float getFee()
    {
        return fee;
    }

    public void setFee(float fee)
    {
        this.fee = fee;
    }

    public long getStart_time()
    {
        return start_time;
    }

    public void setStart_time(long start_time)
    {
        this.start_time = start_time;
    }

    public int getSource_type()
    {
        return source_type;
    }

    public void setSource_type(int source_type)
    {
        this.source_type = source_type;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public long getApply_start_time()
    {
        return apply_start_time;
    }

    public void setApply_start_time(long apply_start_time)
    {
        this.apply_start_time = apply_start_time;
    }

    public long getApply_end_time()
    {
        return apply_end_time;
    }

    public void setApply_end_time(long apply_end_time)
    {
        this.apply_end_time = apply_end_time;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public long getEnd_time()
    {
        return end_time;
    }

    public void setEnd_time(long end_time)
    {
        this.end_time = end_time;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getApply_num()
    {
        return apply_num;
    }

    public void setApply_num(int apply_num)
    {
        this.apply_num = apply_num;
    }
}
