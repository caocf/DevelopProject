package com.moge.gege.model;

public class BaseOptionModel
{
    private int resId;
    private String name;
    private String _id;
    private String icon;

    private String logo;

    public BaseOptionModel()
    {

    }

    public BaseOptionModel(int resId, String name, String id)
    {
        this.resId = resId;
        this.name = name;
        this._id = id;
    }

    public int getResId()
    {
        return resId;
    }

    public void setResId(int resId)
    {
        this.resId = resId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
