package com.moge.gege.model;

import java.util.List;

public class TopicListModel
{
    private List<TopicModel> toped_topics;
    private List<TopicModel> topics;
    private List<TopicModel> favorites;
    private List<TopicModel> applys;
    private List<TopicModel> activitys;
    private List<TopicModel> living_services;
    private List<TopicModel> feeds;
    private String previous_cursor;
    private String next_cursor;

    public List<TopicModel> getToped_topics()
    {
        return toped_topics;
    }

    public void setToped_topics(List<TopicModel> toped_topics)
    {
        this.toped_topics = toped_topics;
    }

    public List<TopicModel> getTopics()
    {
        return topics;
    }

    public void setTopics(List<TopicModel> topics)
    {
        this.topics = topics;
    }

    public List<TopicModel> getFavorites()
    {
        return favorites;
    }

    public void setFavorites(List<TopicModel> favorites)
    {
        this.favorites = favorites;
    }

    public List<TopicModel> getApplys()
    {
        return applys;
    }

    public void setApplys(List<TopicModel> applys)
    {
        this.applys = applys;
    }

    public List<TopicModel> getActivitys()
    {
        return activitys;
    }

    public void setActivitys(List<TopicModel> activitys)
    {
        this.activitys = activitys;
    }

    public List<TopicModel> getLiving_services()
    {
        return living_services;
    }

    public void setLiving_services(List<TopicModel> living_services)
    {
        this.living_services = living_services;
    }

    public List<TopicModel> getFeeds()
    {
        return feeds;
    }

    public void setFeeds(List<TopicModel> feeds)
    {
        this.feeds = feeds;
    }

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

}
