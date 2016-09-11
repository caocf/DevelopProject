package com.moge.gege.model;

import java.util.List;

public class UserListModel
{
    private String previous_cursor;
    private String next_cursor;
    private List<UserModel> neighbors;
    private List<UserModel> friends;

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

    public List<UserModel> getNeighbors()
    {
        return neighbors;
    }

    public void setNeighbors(List<UserModel> neighbors)
    {
        this.neighbors = neighbors;
    }

    public List<UserModel> getFriends()
    {
        return friends;
    }

    public void setFriends(List<UserModel> friends)
    {
        this.friends = friends;
    }

}
