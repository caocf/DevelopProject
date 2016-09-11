package com.moge.gege.model;

public class ReqChatModel extends BasePostModel
{
    private ChatSendModel data;

    public ChatSendModel getData()
    {
        return data;
    }

    public void setData(ChatSendModel data)
    {
        this.data = data;
    }

}
