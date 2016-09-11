package com.moge.gege.model;

public class ReadMessageModel
{
    // "msg_id":string, 消息ID
    // "from_uid":string 来源用户ID

    private String from_uid;
    private String msg_id;

    public String getFrom_uid()
    {
        return from_uid;
    }

    public void setFrom_uid(String from_uid)
    {
        this.from_uid = from_uid;
    }

    public String getMsg_id()
    {
        return msg_id;
    }

    public void setMsg_id(String msg_id)
    {
        this.msg_id = msg_id;
    }

}
