package com.moge.gege.model;

public class TopicDetailModel
{
    private String _id;// 帖子ID
    private AttachmentModel attachments; // 帖子图片
    private String author_uid;// 作者ID
    private int like_count;// 赞数
    private int favorite_count;// 收藏数
    private long crts;// 发帖时间
    private long upts;// 更新时间
    private String title;// 帖子标题
    private int post_count;// 回复总数
    private double geo[];// 发帖时经纬度
    private String content; // 帖子详情
    private String post_uid;// 最后回复用户ID
    private String bid;// 圈子ID
    private int marked;// 精华状态
    private int count;// 楼层总数
    private UserModel author;
    private String descript;// 帖子描述
    private long post_upts;// 最后回复时间
    private int toped;// 置顶状态

    private String osmodel;
    private String osver;
    private String phonemodel;
    private int offer_reward;
    private int topic_type;
    private String type_id;
    private String activity_ts;
    private SimpleActivityModel activity;
    private MemberModel member;
    private SimpleServiceModel living_service;

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public AttachmentModel getAttachments()
    {
        return attachments;
    }

    public void setAttachments(AttachmentModel attachments)
    {
        this.attachments = attachments;
    }

    public String getAuthor_uid()
    {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid)
    {
        this.author_uid = author_uid;
    }

    public int getLike_count()
    {
        return like_count;
    }

    public void setLike_count(int like_count)
    {
        this.like_count = like_count;
    }

    public int getFavorite_count()
    {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count)
    {
        this.favorite_count = favorite_count;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public long getUpts()
    {
        return upts;
    }

    public void setUpts(long upts)
    {
        this.upts = upts;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getPost_count()
    {
        return post_count;
    }

    public void setPost_count(int post_count)
    {
        this.post_count = post_count;
    }

    public double[] getGeo()
    {
        return geo;
    }

    public void setGeo(double[] geo)
    {
        this.geo = geo;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getPost_uid()
    {
        return post_uid;
    }

    public void setPost_uid(String post_uid)
    {
        this.post_uid = post_uid;
    }

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public int getMarked()
    {
        return marked;
    }

    public void setMarked(int marked)
    {
        this.marked = marked;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public UserModel getAuthor()
    {
        return author;
    }

    public void setAuthor(UserModel author)
    {
        this.author = author;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public long getPost_upts()
    {
        return post_upts;
    }

    public void setPost_upts(long post_upts)
    {
        this.post_upts = post_upts;
    }

    public int getToped()
    {
        return toped;
    }

    public void setToped(int toped)
    {
        this.toped = toped;
    }

    public String getOsmodel()
    {
        return osmodel;
    }

    public void setOsmodel(String osmodel)
    {
        this.osmodel = osmodel;
    }

    public String getOsver()
    {
        return osver;
    }

    public void setOsver(String osver)
    {
        this.osver = osver;
    }

    public String getPhonemodel()
    {
        return phonemodel;
    }

    public void setPhonemodel(String phonemodel)
    {
        this.phonemodel = phonemodel;
    }

    public int getOffer_reward()
    {
        return offer_reward;
    }

    public void setOffer_reward(int offer_reward)
    {
        this.offer_reward = offer_reward;
    }

    public int getTopic_type()
    {
        return topic_type;
    }

    public void setTopic_type(int topic_type)
    {
        this.topic_type = topic_type;
    }

    public String getType_id()
    {
        return type_id;
    }

    public void setType_id(String type_id)
    {
        this.type_id = type_id;
    }

    public String getActivity_ts()
    {
        return activity_ts;
    }

    public void setActivity_ts(String activity_ts)
    {
        this.activity_ts = activity_ts;
    }

    public SimpleActivityModel getActivity()
    {
        return activity;
    }

    public void setActivity(SimpleActivityModel activity)
    {
        this.activity = activity;
    }

    public MemberModel getMember()
    {
        return member;
    }

    public void setMember(MemberModel member)
    {
        this.member = member;
    }

    public SimpleServiceModel getLiving_service()
    {
        return living_service;
    }

    public void setLiving_service(SimpleServiceModel living_service)
    {
        this.living_service = living_service;
    }

}
