package com.xhl.world.model.user;


public class UserInfo extends BaseEntity<UserInfo> {

    public String id; //唯一标示
    public String userId;
    public String telephone;
    public String userName;
    public String email;
    public String qq;
    public String sex;
    public String password;
    public String vipPic;//头像路径
    public String type;//6:推手表示
    public String area;
    public String areaAddress;
    public String birthday;
    public String wxOpenID;//绑定的微信id
    public String qqOpenID;//Q绑定的Qid

}
