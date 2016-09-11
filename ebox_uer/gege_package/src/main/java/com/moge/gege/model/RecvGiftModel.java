package com.moge.gege.model;

public class RecvGiftModel
{
    private int status;
    private String uid;
    private GiftModel gift;
    private String gid;
    private String fuid;
    private long crts;
    private int gtype;
    private String _id;
    private UserModel user;

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

    public GiftModel getGift()
    {
        return gift;
    }

    public void setGift(GiftModel gift)
    {
        this.gift = gift;
    }

    public String getGid()
    {
        return gid;
    }

    public void setGid(String gid)
    {
        this.gid = gid;
    }

    public String getFuid()
    {
        return fuid;
    }

    public void setFuid(String fuid)
    {
        this.fuid = fuid;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public int getGtype()
    {
        return gtype;
    }

    public void setGtype(int gtype)
    {
        this.gtype = gtype;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }

}
