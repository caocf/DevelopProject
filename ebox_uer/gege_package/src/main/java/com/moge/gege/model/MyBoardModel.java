package com.moge.gege.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyBoardModel extends BaseBoardModel implements Parcelable
{
    private String uid;// 用户ID
    private String name;// 圈子名称
    private String logo;// 圈子logo
    private int member_count;// 圈子人数
    private int topic_count;// 圈子帖子数
    private long crts;// 圈子创建时间
    private int is_default;// 是否是用户所在小区圈子
    private int is_manager;// 是否是管理员
    private int is_vip;// 是否是认证会员
    private int integration;// 用户在该圈子的积分
    private long join_crts;// 用户加入该圈子的时间
    private int integration_level; // 积分等级
    private String _id;// 圈子id

    // other interface result
    private int newTopicCount;

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public int getMember_count()
    {
        return member_count;
    }

    public void setMember_count(int member_count)
    {
        this.member_count = member_count;
    }

    public int getTopic_count()
    {
        return topic_count;
    }

    public void setTopic_count(int topic_count)
    {
        this.topic_count = topic_count;
    }

    public long getCrts()
    {
        return crts;
    }

    public void setCrts(long crts)
    {
        this.crts = crts;
    }

    public int getIs_default()
    {
        return is_default;
    }

    public void setIs_default(int is_default)
    {
        this.is_default = is_default;
    }

    public int getIs_manager()
    {
        return is_manager;
    }

    public void setIs_manager(int is_manager)
    {
        this.is_manager = is_manager;
    }

    public int getIs_vip()
    {
        return is_vip;
    }

    public void setIs_vip(int is_vip)
    {
        this.is_vip = is_vip;
    }

    public int getIntegration()
    {
        return integration;
    }

    public void setIntegration(int integration)
    {
        this.integration = integration;
    }

    public long getJoin_crts()
    {
        return join_crts;
    }

    public void setJoin_crts(long join_crts)
    {
        this.join_crts = join_crts;
    }

    public int getIntegration_level()
    {
        return integration_level;
    }

    public void setIntegration_level(int integration_level)
    {
        this.integration_level = integration_level;
    }

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public int getNewTopicCount()
    {
        return newTopicCount;
    }

    public void setNewTopicCount(int newTopicCount)
    {
        this.newTopicCount = newTopicCount;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(uid);
        dest.writeString(name);
        dest.writeString(logo);
        dest.writeInt(member_count);
        dest.writeInt(topic_count);
        dest.writeLong(crts);
        dest.writeInt(is_default);
        dest.writeInt(is_manager);
        dest.writeInt(is_vip);
        dest.writeInt(integration);
        dest.writeLong(join_crts);
        dest.writeInt(integration_level);
        dest.writeString(_id);
    }

    public static final Parcelable.Creator<MyBoardModel> CREATOR = new Creator<MyBoardModel>()
    {
        @Override
        public MyBoardModel createFromParcel(Parcel source)
        {
            MyBoardModel myboard = new MyBoardModel();
            myboard.uid = source.readString();
            myboard.name = source.readString();
            myboard.logo = source.readString();
            myboard.member_count = source.readInt();
            myboard.topic_count = source.readInt();
            myboard.crts = source.readLong();
            myboard.is_default = source.readInt();
            myboard.is_manager = source.readInt();
            myboard.is_vip = source.readInt();
            myboard.integration = source.readInt();
            myboard.join_crts = source.readLong();
            myboard.integration_level = source.readInt();
            myboard._id = source.readString();
            return myboard;
        }

        @Override
        public MyBoardModel[] newArray(int size)
        {
            return new MyBoardModel[size];
        }
    };
}
