package com.moge.gege.model;

import java.io.Serializable;

public class MessageModel implements Serializable
{
    // "from_uid":string, 来源用户ID
    // "content":string, 消息内容
    // "msg_type":int, 消息类型 （0：文本消息， 1：图片消息）
    // "msg_id":string, 消息ID
    // "crts":int 消息发送时间

    private static final long serialVersionUID = 1L;
    private String from_uid;
    private String content;
    private int msg_type;
    private String msg_id;
    private double crts;

    private String _id;
    private double rdts;
    private String to_uid;

    public String getFrom_uid()
    {
        return from_uid;
    }

    public void setFrom_uid(String from_uid)
    {
        this.from_uid = from_uid;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getMsg_type()
    {
        return msg_type;
    }

    public void setMsg_type(int msg_type)
    {
        this.msg_type = msg_type;
    }

    public String getMsg_id()
    {
        return msg_id;
    }

    public void setMsg_id(String msg_id)
    {
        this.msg_id = msg_id;
    }

    public double getCrts()
    {
        return crts;
    }

    public void setCrts(double crts)
    {
        this.crts = crts;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public double getRdts()
    {
        return rdts;
    }

    public void setRdts(double rdts)
    {
        this.rdts = rdts;
    }

    public String getTo_uid()
    {
        return to_uid;
    }

    public void setTo_uid(String to_uid)
    {
        this.to_uid = to_uid;
    }

}
