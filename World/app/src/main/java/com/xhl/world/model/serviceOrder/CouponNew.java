package com.xhl.world.model.serviceOrder;


import java.io.Serializable;


public class CouponNew implements Serializable
{

    /**
     * 意义，目的和功能，以及被用到的地方<br>
     */
    private static final long serialVersionUID = 5732166097649395862L;

    /**
     * 优惠券主键id
     */
    private String id;

    private String storeOrderCode;//订单编号

    /**
     * 优惠券创建者id
     */
    private String createUser;

    private String sendUser;

    private String useUser;

    private String couponId;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 优惠券类型  0 店铺优惠券 1  商品优惠券 2 品类优惠券
     */
    private Integer type;

    /**
     * 优惠券状态   0、未付款 或未审核     1、已付款 或已审核  2、提交审核  3、已领取  4、已使用  5、失效
     */
    private Integer status;

    /**
     * 优惠券所属  0 、品牌商或厂商  1 经销商
     */
    private Integer belong;

    /**
     * 优惠券开始时间
     */
    private String startTime;

    /**
     * 优惠券结束时间
     */
    private String endTime;

    /**
     * 单张面值
     */
    private Integer faceValue;

    /**
     * 关联主键 （产品分类，产品，店铺） 
     */
    private String relationId;

    public String getRelationId()
    {
        return relationId;
    }

    public void setRelationId(String relationId)
    {
        this.relationId = relationId;
    }

    /**
     * 使用量
     */
    private String useNum;

    /**
     * 优惠券说明
     */
    private String instruction;

    /**
     * 使用规则
     */
    private Integer useCondition;

    /**
     * 优惠券批次号
     */
    private String batchNum;

    /**
     * 优惠券创建时间
     */
    private String createTime;

    /**
     * 发行总量
     */
    private Integer count;

    /////////////////////////////////////////
    private String sid;//店铺ID

    private String uid;//经销商ID

    private String categoryName;//品类名称

    private String productName;//商品名称

    private String shopName;//店铺名称

    private String SKU;//商品SKU

    private String dealerName;

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getBelong()
    {
        return belong;
    }

    public void setBelong(Integer belong)
    {
        this.belong = belong;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public Integer getFaceValue()
    {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue)
    {
        this.faceValue = faceValue;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    public String getInstruction()
    {
        return instruction;
    }

    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }

    public Integer getUseCondition()
    {
        return useCondition;
    }

    public void setUseCondition(Integer useCondition)
    {
        this.useCondition = useCondition;
    }

    public String getBatchNum()
    {
        return batchNum;
    }

    public void setBatchNum(String batchNum)
    {
        this.batchNum = batchNum;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getSid()
    {
        return sid;
    }

    public void setSid(String sid)
    {
        this.sid = sid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getSendUser()
    {
        return sendUser;
    }

    public void setSendUser(String sendUser)
    {
        this.sendUser = sendUser;
    }

    public String getUseUser()
    {
        return useUser;
    }

    public void setUseUser(String useUser)
    {
        this.useUser = useUser;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getSKU()
    {
        return SKU;
    }

    public void setSKU(String sKU)
    {
        SKU = sKU;
    }

    public String getDealerName()
    {
        return dealerName;
    }

    public void setDealerName(String dealerName)
    {
        this.dealerName = dealerName;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public String getUseNum()
    {
        return useNum;
    }

    public void setUseNum(String useNum)
    {
        this.useNum = useNum;
    }

    public String getStoreOrderCode()
    {
        return storeOrderCode;
    }

    public void setStoreOrderCode(String storeOrderCode)
    {
        this.storeOrderCode = storeOrderCode;
    }

    public String getCouponId()
    {
        return couponId;
    }

    public void setCouponId(String couponId)
    {
        this.couponId = couponId;
    }

    @Override
    public String toString()
    {
        return "CouponNew [id=" + id + ", storeOrderCode=" + storeOrderCode + ", createUser="
               + createUser + ", sendUser=" + sendUser + ", useUser=" + useUser + ", couponId="
               + couponId + ", name=" + name + ", type=" + type + ", status=" + status
               + ", belong=" + belong + ", startTime=" + startTime + ", endTime=" + endTime
               + ", faceValue=" + faceValue + ", relationId=" + relationId + ", useNum=" + useNum
               + ", instruction=" + instruction + ", useCondition=" + useCondition + ", batchNum="
               + batchNum + ", createTime=" + createTime + ", count=" + count
               + ", sid=" + sid + ", uid=" + uid + ", categoryName=" + categoryName
               + ", productName=" + productName + ", shopName=" + shopName + ", SKU=" + SKU
               + ", dealerName=" + dealerName + "]";
    }

}
