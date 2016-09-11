package com.moge.gege.model;

public class ServiceModel
{
    private boolean liked;
    private AttachmentModel attachments; // 帖子图片
    private int city_id;
    private String ip;
    private String author_uid;// 作者ID
    private long crts;// 发帖时间
    private long upts;// 更新时间
    private int need_type;
    private String start_location;
    private String category;
    private int province_id;
    private String network;
    private String title;// 帖子标题
    private String content;
    private double community_geo[];
    private int service_type;
    private int price;
    private boolean favorited;
    private long start_time;
    private String community_id;
    private int source_type;
    private String end_location;
    private double geo[];// 发帖时经纬度
    private String device_id;
    private ServiceInfoModel info;
    private String osmodel;
    private String descript;// 帖子描述
    private int district_id;
    private String osver;
    private long end_time;
    private String phonemodel;
    private UserModel author;
    private String _id;// 帖子ID
    private SimpleTopicModel topic;

    public boolean isLiked()
    {
        return liked;
    }

    public void setLiked(boolean liked)
    {
        this.liked = liked;
    }

    public AttachmentModel getAttachments()
    {
        return attachments;
    }

    public void setAttachments(AttachmentModel attachments)
    {
        this.attachments = attachments;
    }

    public int getCity_id()
    {
        return city_id;
    }

    public void setCity_id(int city_id)
    {
        this.city_id = city_id;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getAuthor_uid()
    {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid)
    {
        this.author_uid = author_uid;
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

    public int getNeed_type()
    {
        return need_type;
    }

    public void setNeed_type(int need_type)
    {
        this.need_type = need_type;
    }

    public String getStart_location()
    {
        return start_location;
    }

    public void setStart_location(String start_location)
    {
        this.start_location = start_location;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public int getProvince_id()
    {
        return province_id;
    }

    public void setProvince_id(int province_id)
    {
        this.province_id = province_id;
    }

    public String getNetwork()
    {
        return network;
    }

    public void setNetwork(String network)
    {
        this.network = network;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public double[] getCommunity_geo()
    {
        return community_geo;
    }

    public void setCommunity_geo(double[] community_geo)
    {
        this.community_geo = community_geo;
    }

    public int getService_type()
    {
        return service_type;
    }

    public void setService_type(int service_type)
    {
        this.service_type = service_type;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public boolean isFavorited()
    {
        return favorited;
    }

    public void setFavorited(boolean favorited)
    {
        this.favorited = favorited;
    }

    public long getStart_time()
    {
        return start_time;
    }

    public void setStart_time(long start_time)
    {
        this.start_time = start_time;
    }

    public String getCommunity_id()
    {
        return community_id;
    }

    public void setCommunity_id(String community_id)
    {
        this.community_id = community_id;
    }

    public int getSource_type()
    {
        return source_type;
    }

    public void setSource_type(int source_type)
    {
        this.source_type = source_type;
    }

    public String getEnd_location()
    {
        return end_location;
    }

    public void setEnd_location(String end_location)
    {
        this.end_location = end_location;
    }

    public double[] getGeo()
    {
        return geo;
    }

    public void setGeo(double[] geo)
    {
        this.geo = geo;
    }

    public String getDevice_id()
    {
        return device_id;
    }

    public void setDevice_id(String device_id)
    {
        this.device_id = device_id;
    }

    public ServiceInfoModel getInfo()
    {
        return info;
    }

    public void setInfo(ServiceInfoModel info)
    {
        this.info = info;
    }

    public String getOsmodel()
    {
        return osmodel;
    }

    public void setOsmodel(String osmodel)
    {
        this.osmodel = osmodel;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public int getDistrict_id()
    {
        return district_id;
    }

    public void setDistrict_id(int district_id)
    {
        this.district_id = district_id;
    }

    public String getOsver()
    {
        return osver;
    }

    public void setOsver(String osver)
    {
        this.osver = osver;
    }

    public long getEnd_time()
    {
        return end_time;
    }

    public void setEnd_time(long end_time)
    {
        this.end_time = end_time;
    }

    public String getPhonemodel()
    {
        return phonemodel;
    }

    public void setPhonemodel(String phonemodel)
    {
        this.phonemodel = phonemodel;
    }

    public UserModel getAuthor()
    {
        return author;
    }

    public void setAuthor(UserModel author)
    {
        this.author = author;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public SimpleTopicModel getTopic()
    {
        return topic;
    }

    public void setTopic(SimpleTopicModel topic)
    {
        this.topic = topic;
    }

}
