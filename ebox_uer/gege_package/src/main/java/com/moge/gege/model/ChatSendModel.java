package com.moge.gege.model;

public class ChatSendModel
{
    // "to_uid":string, 用户ID
    // "content":string, 消息内容
    // "msg_type":int, 消息类型 （0：文本消息， 1：图片消息）
    // "req_id":int 请求ID

    private String to_uid;
    private String content;
    private int msg_type;
    private int req_id;

    public String getTo_uid()
    {
        return to_uid;
    }

    public void setTo_uid(String to_uid)
    {
        this.to_uid = to_uid;
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

    public int getReq_id()
    {
        return req_id;
    }

    public void setReq_id(int req_id)
    {
        this.req_id = req_id;
    }

}
