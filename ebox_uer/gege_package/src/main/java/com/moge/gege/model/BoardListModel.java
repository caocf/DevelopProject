package com.moge.gege.model;

import java.util.List;

public class BoardListModel
{
    private String previous_cursor;
    private String next_cursor;
    private List<BoardModel> boards;

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

    public List<BoardModel> getBoards()
    {
        return boards;
    }

    public void setBoards(List<BoardModel> boards)
    {
        this.boards = boards;
    }

}
