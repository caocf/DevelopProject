package com.moge.gege.model;

import java.util.List;

public class GiftListModel
{
    private String previous_cursor;
    private String next_cursor;
    private List<GiftModel> gift;
    private List<RecvGiftModel> gifts;

    public String getPrevious_cursor()
    {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor)
    {
        this.previous_cursor = previous_cursor;
    }

    public String getNext_cursor()
    {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor)
    {
        this.next_cursor = next_cursor;
    }

    public List<GiftModel> getGift()
    {
        return gift;
    }

    public void setGift(List<GiftModel> gift)
    {
        this.gift = gift;
    }

    public List<RecvGiftModel> getGifts()
    {
        return gifts;
    }

    public void setGifts(List<RecvGiftModel> gifts)
    {
        this.gifts = gifts;
    }

}
