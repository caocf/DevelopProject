package com.moge.gege.model;

public class TopicPostModel
{
    private AttachmentModel attachments; // 帖子图片

    private String ip;
    private int number;
    private String ref_descript;
    private String author_uid;
    private long crts;// 创建时间
    private long upts;// 更新时间
    private String ref_pid;
    private String network;
    private UserModel author;
    private String content;
    private String tid;
    private int status;
    private String bid;
    private String ref_uid;
    private int marked;
    private double geo[]; // 经纬度
    private String device_id;
    private int audit;
    private String _id;
    private String osmodel;
    private int ref_number;
    private String descript;
    private String osver;
    private String phonemodel;
    private int toped;
    private MemberModel member;

    // "topic_type": 2000,
    // "topic": {},

    public AttachmentModel getAttachments()
    {
        return attachments;
    }

    public void setAttachments(AttachmentModel attachments)
    {
        this.attachments = attachments;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public String getRef_descript()
    {
        return ref_descript;
    }

    public void setRef_descript(String ref_descript)
    {
        this.ref_descript = ref_descript;
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

    public String getRef_pid()
    {
        return ref_pid;
    }

    public void setRef_pid(String ref_pid)
    {
        this.ref_pid = ref_pid;
    }

    public String getNetwork()
    {
        return network;
    }

    public void setNetwork(String network)
    {
        this.network = network;
    }

    public UserModel getAuthor()
    {
        return author;
    }

    public void setAuthor(UserModel author)
    {
        this.author = author;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getTid()
    {
        return tid;
    }

    public void setTid(String tid)
    {
        this.tid = tid;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public String getRef_uid()
    {
        return ref_uid;
    }

    public void setRef_uid(String ref_uid)
    {
        this.ref_uid = ref_uid;
    }

    public int getMarked()
    {
        return marked;
    }

    public void setMarked(int marked)
    {
        this.marked = marked;
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

    public int getAudit()
    {
        return audit;
    }

    public void setAudit(int audit)
    {
        this.audit = audit;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public String getOsmodel()
    {
        return osmodel;
    }

    public void setOsmodel(String osmodel)
    {
        this.osmodel = osmodel;
    }

    public int getRef_number()
    {
        return ref_number;
    }

    public void setRef_number(int ref_number)
    {
        this.ref_number = ref_number;
    }

    public String getDescript()
    {
        return descript;
    }

    public void setDescript(String descript)
    {
        this.descript = descript;
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

    public int getToped()
    {
        return toped;
    }

    public void setToped(int toped)
    {
        this.toped = toped;
    }

    public MemberModel getMember()
    {
        return member;
    }

    public void setMember(MemberModel member)
    {
        this.member = member;
    }

}
