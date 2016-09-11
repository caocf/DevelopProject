package com.moge.gege.model;

import java.io.Serializable;

public class FriendMsgModel implements Serializable
{
    // "uid":string, 用户ID
    // "fuid":string, 好友ID
    // "action":string, 动作（apply：申请， agree：同意， disagree：拒绝）
    // "crts":int, 发送时间
    // "rdts":int, 阅读时间

    private static final long serialVersionUID = 1L;

    private String uid;
    private String fuid;
    private String action;
    private long crts;
    private long rdts;
    private String _id;
    private int treated; // 0 - not deal; 1 - agree; 2 - disagree

    public int getTreated()
    {
        return treated;
    }

    public void setTreated(int treated)
    {
        this.treated = treated;
    }

    private UserModel friend;

    public UserModel getFriend()
    {
        return friend;
    }

    public void setFriend(UserModel friend)
    {
        this.friend = friend;
    }

    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getFuid()
    {
        return fuid;
    }

    public void setFuid(String fuid)
    {
        this.fuid = fuid;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public long getRdts()
    {
        return rdts;
    }

    public void setRdts(long rdts)
    {
        this.rdts = rdts;
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
