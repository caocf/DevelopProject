package com.xhl.bqlh.business.Model.App;

import java.io.Serializable;

/**
 * Created by Sum on 16/4/22.
 */
public class RegisterModel implements Serializable{

    public boolean fixInfo;//是否是修改零售商信息
    public String uid;//零售商id
    public String companyName;
    public String companyAddress;//公司地址
    public String address;//收货详细地址
    public String location;//收货地址
    public String areaId;//地区编码id  对应地区字典表id
    public String line;//线路
    public String companyTypeId;//业态ID

    public String liableName;
    public String liablePhone;
    public String loginUserName;
    public String password = "666666";

    public String businessLicenceId;//营业执照号
    public String businessLicence;//图片
    public String businessLicencePath;//本地路径

    public String retailerImg;//门头照
    public String retailerImgPath;//本地路径
    public double coordinateX;//经度
    public double coordinateY;//维度

    public String province;//省份
    public String city;//城市
    public String district;//区域
    public String street;//街道

}
