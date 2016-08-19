package com.xhl.bqlh.business.Model;

import com.xhl.bqlh.business.Db.Member;

import java.io.Serializable;

/**
 * Created by Sum on 16/5/4.
 */
public class ShopApplyModel implements Serializable {

    //状态信息
    private Member member;

    /**
     * id
     */
    private String uid;

    //关联系统用户表的零售商id
    private String retailerId;
    /**
     * 登录账号
     */
    private String account;

    /**
     * 登录表id
     */
    private String id;

    /**
     * 所属平台  0 系统 2 经销商
     */
    private Integer platform;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 1 男 2 女
     */
    private Integer sex;

    /**
     * 区域字典id
     */
    private String areaId;

    /**
     * 详细收发货地址
     */
    private String address;

    /**
     * 发货区
     */
    private String region;

    /**
     * 地标描述
     */
    private String landMark;

    /**
     * 坐标经度
     */
    private double coordinateX;

    /**
     * 坐标纬度
     */
    private double coordinateY;

    /**
     * 企业字号
     */
    private String companyName;

    /**
     * 办公地址
     */
    private String companyAddress;

    /**
     * 工商注册名称
     */
    private String industyRegisterName;

    /**
     * 工商证件号
     */
    private String industyNum;

    /**
     * 企业法人代表
     */
    private String artificialPerson;

    /**
     * 法人身份证号
     */
    private String artificialPersonCard;

    /**
     * 法人身份证照片
     */
    private String artificialPersonPicture;

    /**
     * 法人电话
     */
    private String artificialPersonPhone;

    /**
     * 法人邮箱
     */
    private String artificialPersonMail;

    /**
     * 企业固话
     */
    private String companyTel;

    /**
     * 营业执照
     */
    private String businessLicence;

    /**
     * 税务登记证
     */
    private String taxRegistrationCertificate;

    /**
     * 组织机构代码
     */
    private String organizationCode;

    /**
     * 法律责任人  或 品牌商品牌商 、产商的联系人
     */
    private String liableName;

    /**
     * 登录用户名
     */
    private String loginUserName;

    /**
     * 责任人身份证号
     */
    private String liableNamePersonCard;

    /**
     * 责任人电话 或 品牌商 、产商的联系人电话
     */
    private String liablePhone;

    /**
     * 责任人邮箱
     */
    private String liableMail;

    /**
     * 传真
     */
    private String companyFax;

    /**
     * 企业业态编码（对应业态表Id）
     */
    private String companyTypeId;

    private String companyTypeName;

    /**
     * 所属路线
     */
    private String line;

    /**
     * 路线名称
     */
    private String routeName;

    /**
     * 业务员编号
     */
    private String salesmanNum;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 虚拟币数量
     */
    private Integer virtualCurr;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 用户类型(1 经销商；2 零售商；)
     */
    private Integer type;

    /**
     * 完整地址
     */
    private String area;

    /**
     * 用户存放的角色
     */
    private String roleNames;

    /**
     * 角色Id
     */
    private String roleIds;

    /**
     * 密码
     */
    private String password;

    /**
     * 业务员所属经销商id
     */
    private String dealer;

    /**
     * 业务员负责人
     */
    private String mainPerson;

    /**
     * 用户审核状态
     */
    private Integer operatorState;

    /**
     * 用户登录表id
     */
    private String LoginId;

    /**
     * 经销商分配的优惠券
     */
    private Integer count;

    private String sku;

    /**
     * 代理品牌
     */
    private String cache;

    /**
     * 验证码
     */
    private String rdnCode;

    /**
     * 发货区域名称
     */
    private String deliverName;

    /**
     * 更新时间
     */
    private String updateTimeForMat;
    /**
     * 门头照
     */
    private String retailerImg;
    /**
     * 营业执照编码
     */
    private String businessLicenceId;

    public String getAccount() {
        return account;
    }

    public String getId() {
        return id;
    }

    public Integer getPlatform() {
        return platform;
    }

    public Integer getSex() {
        return sex;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getRegion() {
        return region;
    }

    public String getLandMark() {
        return landMark;
    }

    public Double getCoordinateX() {
        return coordinateX;
    }

    public Double getCoordinateY() {
        return coordinateY;
    }

    public String getIndustyRegisterName() {
        return industyRegisterName;
    }

    public String getIndustyNum() {
        return industyNum;
    }

    public String getArtificialPerson() {
        return artificialPerson;
    }

    public String getArtificialPersonCard() {
        return artificialPersonCard;
    }

    public String getArtificialPersonPicture() {
        return artificialPersonPicture;
    }

    public String getArtificialPersonPhone() {
        return artificialPersonPhone;
    }

    public String getArtificialPersonMail() {
        return artificialPersonMail;
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public String getTaxRegistrationCertificate() {
        return taxRegistrationCertificate;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public String getLiableNamePersonCard() {
        return liableNamePersonCard;
    }

    public String getLiableMail() {
        return liableMail;
    }

    public String getCompanyFax() {
        return companyFax;
    }

    public String getCompanyTypeId() {
        return companyTypeId;
    }

    public String getLine() {
        return line;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getSalesmanNum() {
        return salesmanNum;
    }

    public String getBrandName() {
        return brandName;
    }

    public Integer getVirtualCurr() {
        return virtualCurr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public Integer getType() {
        return type;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public String getPassword() {
        return password;
    }

    public String getDealer() {
        return dealer;
    }

    public String getMainPerson() {
        return mainPerson;
    }

    public Integer getOperatorState() {
        return operatorState;
    }

    public String getLoginId() {
        return LoginId;
    }

    public Integer getCount() {
        return count;
    }

    public String getSku() {
        return sku;
    }

    public String getCache() {
        return cache;
    }

    public String getRdnCode() {
        return rdnCode;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public String getUpdateTimeForMat() {
        return updateTimeForMat;
    }

    public String getRetailerImg() {
        return retailerImg;
    }

    public String getBusinessLicenceId() {
        return businessLicenceId;
    }

    public String getRegSalesmanId() {
        return regSalesmanId;
    }

    /**
     * 注册零售商业务员ID
     */
    private String regSalesmanId;

    private int shstate;//0待审核 1审核通过 2不通过

    public String getUid() {
        return uid;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public String getLiableName() {
        return liableName;
    }

    public String getLiablePhone() {
        return liablePhone;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public int getState() {
        return shstate;
    }

    public void setState(int shstate) {
        this.shstate = shstate;
    }

    public String getCompanyTypeName() {
        return companyTypeName;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }
}
