package com.moge.gege.model;

import java.io.Serializable;

public class ChatSendResultModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int result;
    private int send_type;
    private String msg_id;
    private int req_id;

    public int getResult()
    {
        return result;
    }

    public void setResult(int result)
    {
        this.result = result;
    }

    public int getSend_type()
    {
        return send_type;
    }

    public void setSend_type(int send_type)
    {
        this.send_type = send_type;
    }

    public String getMsg_id()
    {
        return msg_id;
    }

    public void setMsg_id(String msg_id)
    {
        this.msg_id = msg_id;
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
