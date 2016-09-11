package com.moge.gege.model;

import java.util.List;

public class ConvenienceModel
{
    private int service_type;
    List<ContactModel> contacts;
    private String title;
    private String _id;
    private int dial_count;
    private int business_type;
    private String address;
    private String business_time;

    private String business_district;
    private String content;

    public int getService_type()
    {
        return service_type;
    }

    public void setService_type(int service_type)
    {
        this.service_type = service_type;
    }

    public List<ContactModel> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<ContactModel> contacts)
    {
        this.contacts = contacts;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public int getDial_count()
    {
        return dial_count;
    }

    public void setDial_count(int dial_count)
    {
        this.dial_count = dial_count;
    }

    public int getBusiness_type()
    {
        return business_type;
    }

    public void setBusiness_type(int business_type)
    {
        this.business_type = business_type;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getBusiness_time()
    {
        return business_time;
    }

    public void setBusiness_time(String business_time)
    {
        this.business_time = business_time;
    }

    public String getBusiness_district()
    {
        return business_district;
    }

    public void setBusiness_district(String business_district)
    {
        this.business_district = business_district;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

}
