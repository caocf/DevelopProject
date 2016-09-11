package com.moge.gege.model;

public class TradingPromotionModel extends ImageModel
{
    private int status;
    private String style;
    private String name;
    private StyleInfoModel style_info;
    private int place;
    private String _id;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getStyle()
    {
        return style;
    }

    public void setStyle(String style)
    {
        this.style = style;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public StyleInfoModel getStyle_info()
    {
        return style_info;
    }

    public void setStyle_info(StyleInfoModel style_info)
    {
        this.style_info = style_info;
    }

    public int getPlace()
    {
        return place;
    }

    public void setPlace(int place)
    {
        this.place = place;
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
