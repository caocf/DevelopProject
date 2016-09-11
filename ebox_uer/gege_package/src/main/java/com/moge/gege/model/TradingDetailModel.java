package com.moge.gege.model;

import java.io.Serializable;
import java.util.List;

public class TradingDetailModel extends TradingBaseModel implements
        Serializable
{
    private static final long serialVersionUID = 1L;

    private int appraise_count; // all comment
    private int city_id;
    private long crts;
    private int favorite_count;
    private String category;
    private double geo[];
    private long allow_start_time;
    private long allow_end_time;
    private double community_geo[];
    private int recommended;
    private String street_id;
    private long start_time;
    private int buy_user_count;
    private String community_id;
    private int hoped;
    private int source_type;
    private int province_id;
    private long upts;
    private int toped;
    private String osmodel;
    private String descript;
    private int district_id;
    private int like_count;
    private String osver;
    private long end_time;
    private int favorable_count; // good comment
    private String phonemodel;
    private int trading_type; // 0 - 普通；1 - 活动商品 ； 2 - 秒杀


    private String ip;
    private String network;
    private String content;
    private List<TradingInfoModel> info;
    private String device_id;
    private int audit;
    private UserModel author;

    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public int getAppraise_count()
    {
        return appraise_count;
    }

    public void setAppraise_count(int appraise_count)
    {
        this.appraise_count = appraise_count;
    }

    public int getCity_id()
    {
        return city_id;
    }

    public void setCity_id(int city_id)
    {
        this.city_id = city_id;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public int getFavorite_count()
    {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count)
    {
        this.favorite_count = favorite_count;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public double[] getGeo()
    {
        return geo;
    }

    public void setGeo(double[] geo)
    {
        this.geo = geo;
    }

    public long getAllow_start_time()
    {
        return allow_start_time;
    }

    public void setAllow_start_time(long allow_start_time)
    {
        this.allow_start_time = allow_start_time;
    }

    public long getAllow_end_time()
    {
        return allow_end_time;
    }

    public void setAllow_end_time(long allow_end_time)
    {
        this.allow_end_time = allow_end_time;
    }

    public double[] getCommunity_geo()
    {
        return community_geo;
    }

    public void setCommunity_geo(double[] community_geo)
    {
        this.community_geo = community_geo;
    }

    public int getRecommended()
    {
        return recommended;
    }

    public void setRecommended(int recommended)
    {
        this.recommended = recommended;
    }

    public String getStreet_id()
    {
        return street_id;
    }

    public void setStreet_id(String street_id)
    {
        this.street_id = street_id;
    }

    public long getStart_time()
    {
        return start_time;
    }

    public void setStart_time(long start_time)
    {
        this.start_time = start_time;
    }

    public int getBuy_user_count()
    {
        return buy_user_count;
    }

    public void setBuy_user_count(int buy_user_count)
    {
        this.buy_user_count = buy_user_count;
    }

    public String getCommunity_id()
    {
        return community_id;
    }

    public void setCommunity_id(String community_id)
    {
        this.community_id = community_id;
    }

    public int getHoped()
    {
        return hoped;
    }

    public void setHoped(int hoped)
    {
        this.hoped = hoped;
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

    public long getUpts()
    {
        return upts;
    }

    public void setUpts(long upts)
    {
        this.upts = upts;
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

    public int getLike_count()
    {
        return like_count;
    }

    public void setLike_count(int like_count)
    {
        this.like_count = like_count;
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

    public int getFavorable_count()
    {
        return favorable_count;
    }

    public void setFavorable_count(int favorable_count)
    {
        this.favorable_count = favorable_count;
    }

    public String getPhonemodel()
    {
        return phonemodel;
    }

    public void setPhonemodel(String phonemodel)
    {
        this.phonemodel = phonemodel;
    }

    public int getTrading_type()
    {
        return trading_type;
    }

    public void setTrading_type(int trading_type)
    {
        this.trading_type = trading_type;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getNetwork()
    {
        return network;
    }

    public void setNetwork(String network)
    {
        this.network = network;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public List<TradingInfoModel> getInfo()
    {
        return info;
    }

    public void setInfo(List<TradingInfoModel> info)
    {
        this.info = info;
    }

    public String getDevice_id()
    {
        return device_id;
    }

    public void setDevice_id(String device_id)
    {
        this.device_id = device_id;
    }

    public int getAudit()
    {
        return audit;
    }

    public void setAudit(int audit)
    {
        this.audit = audit;
    }

    public UserModel getAuthor()
    {
        return author;
    }

    public void setAuthor(UserModel author)
    {
        this.author = author;
    }
}
