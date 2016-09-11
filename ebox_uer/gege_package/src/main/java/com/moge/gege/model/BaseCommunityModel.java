package com.moge.gege.model;

import java.io.Serializable;

public class BaseCommunityModel implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String _id;// 小区ID
    private String name;// 小区名称

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
