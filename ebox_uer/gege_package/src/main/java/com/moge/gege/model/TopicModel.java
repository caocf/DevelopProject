package com.moge.gege.model;

public class TopicModel
{
    private String _id;// 帖子ID
    private String bid;// 圈子ID
    private String title;// 帖子标题
    private String descript;// 帖子描述
    private AttachmentModel attachments; // 帖子图片
    private int topic_type;// 帖子类型
    private int status;// 帖子状态
    private int toped;// 置顶状态
    private int marked;// 精华状态
    private String author_uid;// 作者ID
    private UserModel author;
    private int count;// 楼层总数
    private int post_count;// 回复总数
    private String post_uid;// 最后回复用户ID
    private long post_upts;// 最后回复时间
    private int like_count;// 赞数
    private int favorite_count;// 收藏数
    private double geo[];// 发帖时经纬度
    private long crts;// 发帖时间
    private long upts;// 更新时间
    private BoardModel board;
    private BaseCommunityModel community; // only for service list

    // only for topic detail
    private String content;
    private MemberModel member;
    private String location;
    private long apply_start_time;
    private long apply_end_time;
    private int apply_num;

    private boolean liked;
    private int city_id;
    private int need_type;
    private String start_location;
    private String category;
    private String end_location;
    private double community_geo[];
    private String street_id;
    private int service_type;
    private int price;
    private ServiceInfoModel info;
    private long start_time;
    private String community_id;
    private int source_type;
    private int province_id;
    private String osmodel;
    private int district_id;
    private String osver;
    private long end_time;
    private String phonemodel;

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
    }

    public AttachmentModel getAttachments()
    {
        return attachments;
    }

    public void setAttachments(AttachmentModel attachments)
    {
        this.attachments = attachments;
    }

    public int getTopic_type()
    {
        return topic_type;
    }

    public void setTopic_type(int topic_type)
    {
        this.topic_type = topic_type;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getToped()
    {
        return toped;
    }

    public void setToped(int toped)
    {
        this.toped = toped;
    }

    public int getMarked()
    {
        return marked;
    }

    public void setMarked(int marked)
    {
        this.marked = marked;
    }

    public String getAuthor_uid()
    {
        return author_uid;
    }

    public void setAuthor_uid(String author_uid)
    {
        this.author_uid = author_uid;
    }

    public UserModel getAuthor()
    {
        return author;
    }

    public void setAuthor(UserModel author)
    {
        this.author = author;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public int getPost_count()
    {
        return post_count;
    }

    public void setPost_count(int post_count)
    {
        this.post_count = post_count;
    }

    public String getPost_uid()
    {
        return post_uid;
    }

    public void setPost_uid(String post_uid)
    {
        this.post_uid = post_uid;
    }

    public long getPost_upts()
    {
        return post_upts;
    }

    public void setPost_upts(long post_upts)
    {
        this.post_upts = post_upts;
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

    public double[] getGeo()
    {
        return geo;
    }

    public void setGeo(double[] geo)
    {
        this.geo = geo;
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

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public MemberModel getMember()
    {
        return member;
    }

    public void setMember(MemberModel member)
    {
        this.member = member;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public long getApply_start_time()
    {
        return apply_start_time;
    }

    public void setApply_start_time(long apply_start_time)
    {
        this.apply_start_time = apply_start_time;
    }

    public long getApply_end_time()
    {
        return apply_end_time;
    }

    public void setApply_end_time(long apply_end_time)
    {
        this.apply_end_time = apply_end_time;
    }

    public int getApply_num()
    {
        return apply_num;
    }

    public void setApply_num(int apply_num)
    {
        this.apply_num = apply_num;
    }

    public boolean isLiked()
    {
        return liked;
    }

    public void setLiked(boolean liked)
    {
        this.liked = liked;
    }

    public int getCity_id()
    {
        return city_id;
    }

    public void setCity_id(int city_id)
    {
        this.city_id = city_id;
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

    public String getEnd_location()
    {
        return end_location;
    }

    public void setEnd_location(String end_location)
    {
        this.end_location = end_location;
    }

    public double[] getCommunity_geo()
    {
        return community_geo;
    }

    public void setCommunity_geo(double[] community_geo)
    {
        this.community_geo = community_geo;
    }

    public String getStreet_id()
    {
        return street_id;
    }

    public void setStreet_id(String street_id)
    {
        this.street_id = street_id;
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

    public ServiceInfoModel getInfo()
    {
        return info;
    }

    public void setInfo(ServiceInfoModel info)
    {
        this.info = info;
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

    public int getProvince_id()
    {
        return province_id;
    }

    public void setProvince_id(int province_id)
    {
        this.province_id = province_id;
    }

    public String getOsmodel()
    {
        return osmodel;
    }

    public void setOsmodel(String osmodel)
    {
        this.osmodel = osmodel;
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

    public BoardModel getBoard()
    {
        return board;
    }

    public void setBoard(BoardModel board)
    {
        this.board = board;
    }

    public BaseCommunityModel getCommunity()
    {
        return community;
    }

    public void setCommunity(BaseCommunityModel community)
    {
        this.community = community;
    }

}
