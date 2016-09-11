package com.moge.gege.model;

import com.moge.gege.model.enums.IMMessageStatusType;

public class IMMessageModel
{
    private MessageModel msg;
    private int listType;
    private String avatar;
    private String dateStr;
    private int status;
    private int reqId;

    public IMMessageModel()
    {
        status = IMMessageStatusType.MSG_SEND_SUCCESS;
    }

    public MessageModel getMsg()
    {
        return msg;
    }

    public void setMsg(MessageModel msg)
    {
        this.msg = msg;
    }

    public int getListType()
    {
        return listType;
    }

    public void setListType(int listType)
    {
        this.listType = listType;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getDateStr()
    {
        return dateStr;
    }

    public void setDateStr(String dateStr)
    {
        this.dateStr = dateStr;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getReqId()
    {
        return reqId;
    }

    public void setReqId(int reqId)
    {
        this.reqId = reqId;
    }

}
