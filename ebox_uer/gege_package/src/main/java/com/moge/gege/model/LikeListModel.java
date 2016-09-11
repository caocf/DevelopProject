package com.moge.gege.model;

import java.util.List;

public class LikeListModel
{
    private boolean liked;
    private List<LikeModel> likes;

    public boolean isLike()
    {
        return liked;
    }

    public void setLike(boolean liked)
    {
        this.liked = liked;
    }

    public List<LikeModel> getLikes()
    {
        return likes;
    }

    public void setLikes(List<LikeModel> likes)
    {
        this.likes = likes;
    }

}
