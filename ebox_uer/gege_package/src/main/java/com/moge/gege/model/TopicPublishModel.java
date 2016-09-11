package com.moge.gege.model;

import java.util.List;

import org.apache.http.NameValuePair;

public class TopicPublishModel
{
    private int topicType;
    private List<AlbumItemModel> imageList;
    private List<NameValuePair> paramList;

    public int getTopicType()
    {
        return topicType;
    }

    public void setTopicType(int topicType)
    {
        this.topicType = topicType;
    }

    public List<AlbumItemModel> getImageList()
    {
        return imageList;
    }

    public void setImageList(List<AlbumItemModel> imageList)
    {
        this.imageList = imageList;
    }

    public List<NameValuePair> getParamList()
    {
        return paramList;
    }

    public void setParamList(List<NameValuePair> paramList)
    {
        this.paramList = paramList;
    }

}
