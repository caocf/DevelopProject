package com.moge.gege.model;

import java.io.Serializable;
import java.util.List;

public class OnlineModel implements Serializable
{
    private static final long serialVersionUID = 1L;

    private List<UnReadChatModel> unread_chat;
    private List<UnReadNoticeModel> unread_notice;
    private List<UnReadNotifyModel> unread_notify;
    private int unread_friend;

    public List<UnReadChatModel> getUnread_chat()
    {
        return unread_chat;
    }

    public void setUnread_chat(List<UnReadChatModel> unread_chat)
    {
        this.unread_chat = unread_chat;
    }

    public List<UnReadNoticeModel> getUnread_notice()
    {
        return unread_notice;
    }

    public void setUnread_notice(List<UnReadNoticeModel> unread_notice)
    {
        this.unread_notice = unread_notice;
    }

    public List<UnReadNotifyModel> getUnread_notify()
    {
        return unread_notify;
    }

    public void setUnread_notify(List<UnReadNotifyModel> unread_notify)
    {
        this.unread_notify = unread_notify;
    }

    public int getUnread_friend()
    {
        return unread_friend;
    }

    public void setUnread_friend(int unread_friend)
    {
        this.unread_friend = unread_friend;
    }

}
