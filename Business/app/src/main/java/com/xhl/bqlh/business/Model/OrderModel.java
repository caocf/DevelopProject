package com.xhl.bqlh.business.Model;

import com.xhl.bqlh.business.Model.Type.OrderType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglj
 * @version V1.0
 * @Description: 订单类实例
 * @date 2015年8月24日 上午11:42:38
 */
public class OrderModel implements Serializable {

    public boolean isChecked = false;//选择状态
    public boolean isNeedShowType = true;//订单类型状态

    /**
     * 主键
     */
    private String id;

    /**
     * 子订单编号
     */
    private String storeOrderCode;

    /**
     * 总订单编码
     */
    private String orderCode;

    /**
     * 订单类型（1：普通订单；2：团购订单；3：消费者促销订单；4：渠道促销订单；5订货会订单；6：积分订单）
     */
    private String orderType;

    /**
     * 商家编码
     */
    private String companyId;

    /**
     * 收货方编码
     */
    private String receivingId;

    private String retailerId;

    /**
     * 在线支付金额
     */
    private BigDecimal onlinePayMoney;

    /**
     * 货到付款金额
     */
    private BigDecimal codMoney;

    /**
     * 业务员编号id
     */
    private String salseManId;

    public String getSalseManId() {
        return salseManId;
    }

    public void setSalseManId(String salseManId) {
        this.salseManId = salseManId;
    }

    /**
     * 账期支付金额
     */
    private BigDecimal accountPayMoney;

    /**
     * 转账金额
     */
    private BigDecimal transferMoney;

    /**
     * 使用优惠劵金额
     */
    private BigDecimal couponsMoney;

    /**
     * 订单金额
     */
    private BigDecimal orderMoney;

    /**
     * 支付方式（1：在线支付；2：货到付款；3：账期支付；4.转账；5：积分）
     */
    private String payType;

    /**
     * 支付方式name
     */
    private String payTypeName;

    /**
     * 下单时间
     */
    private String orderTime;

    /**
     * 预计发货时间
     */
    private String predictArrivalTime;

    /**
     * 付款状态（1:未付款;2：待确认付款；3:已付款）
     */
    private String payStatus;

    /**
     * 付款状态name
     */
    private String payStatusName;

    /**
     * 付款时间
     */
    private String payTime;

    /**
     * 支付类型
     */
    private String payId;

    /**
     * 配送状态（1：未发货；2：已发货；3：已完成）
     */
    private String distributionStatus;

    /**
     * 配送状态name
     */
    private String distributionStatusName;

    /**
     * 退货状态（0：未申请  1：退货/等待卖家确认; 2:卖家不同意退货；3：货已收到等待退款；4：买家确认收款）
     */
    private String returnsStatus;

    /**
     * 退货状态name
     */
    private String returnsStatusName;

    /**
     * 退货类型（1:长时间未收到货物；2：货物有损坏；3：货物型号不正确；4：货物数量不对；5：不想要了；6：其他）
     */
    private String returnsType;

    /**
     * 退货类型name
     */
    private String returnsTypeName;

    /**
     * 退货申请时间
     */
    private String returnApplyTime;

    /**
     * 退货处理时间
     */
    private String returnHaddleTime;

    /**
     * 订单来源
     */
    private String orderSource;

    /**
     * 订单状态（0：正常；1：取消）
     */
    private String orderStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 不同意退货原因
     */
    private String disagreeReason;
    /**
     * 实付总额
     */
    private float realPayMoney;

    /**
     * 发货时间
     */
    private String deliveryTime;

    /**
     * 佣金
     */
    private BigDecimal commission;

    /**
     * 体积
     */
    private BigDecimal volume;

    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 异常状态(0：正常；1：订单量过大；2：发货延迟)
     */
    private String exceptionStatus;

    /**
     * 发送邮件（0：未发送；1：已发送）
     */
    private String sendEmail;

    /**
     * 异常状态name
     */
    private String exceptionStatusName;

    /**
     * 发送邮件name
     */
    private String sendEmailName;

    //买方卖方信息
    /**
     * 商家名称
     */
    private String companyName;

    /**
     * 商家最小起订金额
     */
    private BigDecimal minOrderPrice;

    /**
     * 收货方名称
     */
    private String receivingName;

    //责任人
    private String receivingPersonName;

    /**
     * 收货方电话
     */
    private String phone;

    /**
     * 收货方地址
     */
    private String address;

    /**
     * 商家电话
     */
    private String companyPhone;

    /**
     * 商家地址
     */
    private String companyAddress;

    /**
     * 检索下单开始时间
     */
    private String orderTimeStart;

    /**
     * 检索下单结束时间
     */
    private String orderTimeEnd;

    /**
     * 退货申请开始时间
     */
    private String returnApplyTimeStart;

    /**
     * 退货申请结束时间
     */
    private String returnApplyTimeEnd;

    /**
     * 页面类型（1：总后台；2：经销商；3：零售商）
     */
    private Integer roleType;


    /**
     * 地区编码id  对应地区字典表id
     */
    private String areaId;

    /**
     * 经销商发货区
     */
    private String region;

    /**
     * 所属线路id
     */
    private String line;

    //商品详情3个字段

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 数量
     */
    private String num;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 订单详情list
     */
    private List<OrderDetail> orderDetailList;
    //赠品数据
    private List<GiftModel> giftDetailList;

    /**
     * 支付方式List
     */
    private List<Map<String, String>> payTypeList;

    /**
     * 订单明细
     */
    private String attrOrderDetail;

    /**
     * 优惠券主题id
     */
    private String couId;

    /**
     * 责任人邮箱
     */
    private String liableMail;

    /**
     * 评价状态
     */
    private String evaluateStatus;

    /**
     * 银行返回流水
     */
    private String bankReturnWater;

    /**
     * 页面类型（1：全部页面；2：代付款页面；3：待收货页面；4：待评价页面）
     */
    private String pageType;

    /**
     * 退货说明
     */
    private String returnExplain;

    /**
     * 投诉状态（0：未投诉；1：已投诉）
     */
    private String complaintStatus;

    //积分商品封面图片
    private String coverPic;

    /**
     * 商品类型
     */
    private Integer productType;

    /**
     * 备注说明
     */
    private String remark;

    /**
     * 自动取消订单时间间隔
     */
    private Integer timeLag;

    /**
     * 确认收货时间
     */
    private String confirmGoodTime;

    /**
     * 自动取消标志（1：自动取消；其他）
     */
    private String autoCancel;

    /**
     * 自动确认收货标志（1：自动取消；其他）
     */
    private String autoConfirmGood;

    /**
     * 报表订单状态（1：取消；2：未支付；3：已支付）
     */
    private String status;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 支付类型
     */
    private String payIdName;

    /**
     * 手续费
     */
    private BigDecimal factorage;

    /**
     * 农行费率
     */
    private BigDecimal abcRate;

    private float arrears;//赊账金额

    /**
     * 银联费率费率
     */
    private BigDecimal unionRate;

    public BigDecimal getAbcRate() {
        return abcRate;
    }

    public void setAbcRate(BigDecimal abcRate) {
        this.abcRate = abcRate;
    }

    public BigDecimal getUnionRate() {
        return unionRate;
    }

    public void setUnionRate(BigDecimal unionRate) {
        this.unionRate = unionRate;
    }

    public BigDecimal getFactorage() {
        return factorage;
    }

    public void setFactorage(BigDecimal factorage) {
        this.factorage = factorage;
    }

    public String getPayIdName() {
        return payIdName;
    }

    public void setPayIdName(String payIdName) {
        this.payIdName = payIdName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfirmGoodTime() {
        return confirmGoodTime;
    }

    public void setConfirmGoodTime(String confirmGoodTime) {
        this.confirmGoodTime = confirmGoodTime;
    }

    public String getAutoCancel() {
        return autoCancel;
    }

    public void setAutoCancel(String autoCancel) {
        this.autoCancel = autoCancel;
    }

    public String getAutoConfirmGood() {
        return autoConfirmGood;
    }

    public void setAutoConfirmGood(String autoConfirmGood) {
        this.autoConfirmGood = autoConfirmGood;
    }

    public Integer getTimeLag() {
        return timeLag;
    }

    public void setTimeLag(Integer timeLag) {
        this.timeLag = timeLag;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }


    public String getReturnExplain() {
        return returnExplain;
    }

    public void setReturnExplain(String returnExplain) {
        this.returnExplain = returnExplain;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getBankReturnWater() {
        return bankReturnWater;
    }

    public void setBankReturnWater(String bankReturnWater) {
        this.bankReturnWater = bankReturnWater;
    }

    public String getEvaluateStatus() {
        return evaluateStatus;
    }

    public void setEvaluateStatus(String evaluateStatus) {
        this.evaluateStatus = evaluateStatus;
    }

    public String getLiableMail() {
        return liableMail;
    }

    public void setLiableMail(String liableMail) {
        this.liableMail = liableMail;
    }

    public String getCouId() {
        return couId;
    }

    public void setCouId(String couId) {
        this.couId = couId;
    }

    public String getAttrOrderDetail() {
        return attrOrderDetail;
    }

    public void setAttrOrderDetail(String attrOrderDetail) {
        this.attrOrderDetail = attrOrderDetail;
    }

    public List<Map<String, String>> getPayTypeList() {
        return payTypeList;
    }

    public void setPayTypeList(List<Map<String, String>> payTypeList) {
        this.payTypeList = payTypeList;
    }

    public String getExceptionStatusName() {
        return exceptionStatusName;
    }

    public void setExceptionStatusName(String exceptionStatusName) {
        this.exceptionStatusName = exceptionStatusName;
    }

    public String getSendEmailName() {
        return sendEmailName;
    }

    public void setSendEmailName(String sendEmailName) {
        this.sendEmailName = sendEmailName;
    }

    public String getExceptionStatus() {
        return exceptionStatus;
    }

    public void setExceptionStatus(String exceptionStatus) {
        this.exceptionStatus = exceptionStatus;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }


    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getDisagreeReason() {
        return disagreeReason;
    }

    public void setDisagreeReason(String disagreeReason) {
        this.disagreeReason = disagreeReason;
    }

    public String getReturnApplyTimeStart() {
        return returnApplyTimeStart;
    }

    public void setReturnApplyTimeStart(String returnApplyTimeStart) {
        this.returnApplyTimeStart = returnApplyTimeStart;
    }

    public String getReturnApplyTimeEnd() {
        return returnApplyTimeEnd;
    }

    public void setReturnApplyTimeEnd(String returnApplyTimeEnd) {
        this.returnApplyTimeEnd = returnApplyTimeEnd;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getReturnsTypeName() {
        return returnsTypeName;
    }

    public void setReturnsTypeName(String returnsTypeName) {
        this.returnsTypeName = returnsTypeName;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getPayStatusName() {
        return payStatusName;
    }

    public void setPayStatusName(String payStatusName) {
        this.payStatusName = payStatusName;
    }

    public String getDistributionStatusName() {
        return distributionStatusName;
    }

    public void setDistributionStatusName(String distributionStatusName) {
        this.distributionStatusName = distributionStatusName;
    }

    public String getReturnsStatusName() {
        return returnsStatusName;
    }

    public void setReturnsStatusName(String returnsStatusName) {
        this.returnsStatusName = returnsStatusName;
    }

    public String getOrderTimeStart() {
        return orderTimeStart;
    }

    public void setOrderTimeStart(String orderTimeStart) {
        this.orderTimeStart = orderTimeStart;
    }

    public String getOrderTimeEnd() {
        return orderTimeEnd;
    }

    public void setOrderTimeEnd(String orderTimeEnd) {
        this.orderTimeEnd = orderTimeEnd;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getReceivingName() {
        return receivingName;
    }

    public void setReceivingName(String receivingName) {
        this.receivingName = receivingName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRealPayMoney() {
        return realPayMoney;
    }

    public void setRealPayMoney(float realPayMoney) {
        this.realPayMoney = realPayMoney;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreOrderCode() {
        return storeOrderCode;
    }

    public void setStoreOrderCode(String storeOrderCode) {
        this.storeOrderCode = storeOrderCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getReceivingId() {
        return receivingId;
    }

    public void setReceivingId(String receivingId) {
        this.receivingId = receivingId;
    }

    public BigDecimal getOnlinePayMoney() {
        return onlinePayMoney;
    }

    public void setOnlinePayMoney(BigDecimal onlinePayMoney) {
        this.onlinePayMoney = onlinePayMoney;
    }

    public BigDecimal getCodMoney() {
        return codMoney;
    }

    public void setCodMoney(BigDecimal codMoney) {
        this.codMoney = codMoney;
    }

    public BigDecimal getAccountPayMoney() {
        return accountPayMoney;
    }

    public void setAccountPayMoney(BigDecimal accountPayMoney) {
        this.accountPayMoney = accountPayMoney;
    }

    public BigDecimal getTransferMoney() {
        return transferMoney;
    }

    public void setTransferMoney(BigDecimal transferMoney) {
        this.transferMoney = transferMoney;
    }

    public BigDecimal getCouponsMoney() {
        return couponsMoney;
    }

    public void setCouponsMoney(BigDecimal couponsMoney) {
        this.couponsMoney = couponsMoney;
    }

    public BigDecimal getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(BigDecimal orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPredictArrivalTime() {
        return predictArrivalTime;
    }

    public void setPredictArrivalTime(String predictArrivalTime) {
        this.predictArrivalTime = predictArrivalTime;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public String getReturnsStatus() {
        return returnsStatus;
    }

    public void setReturnsStatus(String returnsStatus) {
        this.returnsStatus = returnsStatus;
    }

    public String getReturnsType() {
        return returnsType;
    }

    public void setReturnsType(String returnsType) {
        this.returnsType = returnsType;
    }

    public String getReturnApplyTime() {
        return returnApplyTime;
    }

    public void setReturnApplyTime(String returnApplyTime) {
        this.returnApplyTime = returnApplyTime;
    }

    public String getReturnHaddleTime() {
        return returnHaddleTime;
    }

    public void setReturnHaddleTime(String returnHaddleTime) {
        this.returnHaddleTime = returnHaddleTime;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    //支付方式（1：在线支付；2：货到付款；3：账期支付；4.转账；5：积分）
    //订单金额
    public BigDecimal getRealOrderMoney() {

        if (payType.equals("2")) {
            return codMoney;
        } else {
            return orderMoney;
        }
    }

    //付款金额
    public BigDecimal getRealPayOrderMoney() {
        if (payType.equals("2")) {
            return transferMoney;
        } else {
            return onlinePayMoney;
        }
    }

    public String getOrderTypeDesc() {
        if (orderType.equals(String.valueOf(OrderType.order_type_car))) {
            return "C";
        } else if (orderType.equals(String.valueOf(OrderType.order_type_self))) {
            return "B";
        } else {
            return "M";
        }
    }

    public float getArrears() {
        return arrears;
    }

    public String getReceivingPersonName() {
        return receivingPersonName;
    }

    public List<GiftModel> getGiftDetailList() {
        return giftDetailList;
    }

    public String getRetailerId() {
        return retailerId;
    }
}
