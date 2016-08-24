package com.xhl.world.model.serviceOrder;

import com.xhl.world.config.NetWorkConfig;

import java.io.Serializable;

/**
 * 订单详情类实例
 */
public class OrderDetail implements Serializable, Cloneable {

    /**
     * 主键
     */
    private String id;

    // 总订单编号
    private String orderCode;

    /**
     * 子订单编号
     */
    private String storeOrderCode;

    /**
     * 商品编码
     */
    private String goodId;

    //商品名称
    private String goodName;

    /**
     * SKU码
     */
    private String skuCode;

    //商品分类
    private String categoryId;
    //毛重
    private String grossWeight;
    //净重
    private String netWeight;
    //商品上架中文名称
    private String productChineseName;
    //英文名称
    private String productForeignName;
    //货号
    private String styleID;
    //申报品名  productName
    private String productName2;
    //规格型号
    private String model;
    //HS编码
    private String HSCode;
    //行邮税号
    private String mailTax;

    /**
     * 单价
     */
    private String unitPrice;

    /**
     * 类型（1：普通订单；2：团购订单；3：消费者促销订单；4：渠道促销订单；5订货会订单；6：积分订单）
     */
    private String type;

    /**
     * 数量
     */
    private String num;

    /**
     * 商品总价
     */
    private String totalPrice;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 体积
     */
    private String volume;

    /**
     * 佣金
     */
    private String commission;

    /**
     * 重量
     */
    private String weight;

    //对应商品表信息
    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品封面图片
     */
    private String productPic;

    /**
     * 订货计划id
     */
    private String shoppingCartId;

    /**
     * 说明
     */
    private String remark;

    /**
     * 赠送数量
     */
    private Integer giveNum;

    private String taxprice;   // 税价，通过dutiablePrice*tariff确定

    /**
     * 评价状态
     */
    private String evaluateStatus;

    /**
     * 退货状态（0：未申请  1：退货/等待卖家确认; 2:卖家不同意退货；3：货已收到等待退款； 5:同意退货等待退货 ；6：完成退货）
     */
    private String returnStatus;

    private int returnNum;

    private String returnId;

    private int applyReturnNum;

    private String returnHaddleTime;


    @Override
    public OrderDetail clone() throws CloneNotSupportedException {
        return (OrderDetail) super.clone();
    }

    public String getReturnHaddleTime() {
        return returnHaddleTime;
    }

    public void setReturnHaddleTime(String returnHaddleTime) {
        this.returnHaddleTime = returnHaddleTime;
    }

    public int getApplyReturnNum() {
        return applyReturnNum;
    }

    public void setApplyReturnNum(int applyReturnNum) {
        this.applyReturnNum = applyReturnNum;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public int getReturnNum() {
        return returnNum;
    }

    public void setReturnNum(int returnNum) {
        this.returnNum = returnNum;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public String getEvaluateStatus() {
        return evaluateStatus;
    }

    public void setEvaluateStatus(String evaluateStatus) {
        this.evaluateStatus = evaluateStatus;
    }

    public String getTaxprice() {
        return taxprice;
    }

    public void setTaxprice(String taxprice) {
        this.taxprice = taxprice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getGiveNum() {
        return giveNum;
    }

    public void setGiveNum(Integer giveNum) {
        this.giveNum = giveNum;
    }

    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(String shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPic() {
        return NetWorkConfig.imageHost + productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
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

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    /**
     * 返回 总订单编号
     *
     * @return 总订单编号
     */

    public String getOrderCode() {
        return orderCode;
    }


    /**
     * 设置 总订单编号
     *
     * @param orderCode 总订单编号
     */

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }


    /**
     * 返回 商品名称
     *
     * @return 商品名称
     */

    public String getGoodName() {
        return goodName;
    }


    /**
     * 设置 商品名称
     *
     * @param goodName 商品名称
     */

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    /**
     * 返回 商品分类
     *
     * @return 商品分类
     */

    public String getCategoryId() {
        return categoryId;
    }


    /**
     * 设置 商品分类
     *
     * @param categoryId 商品分类
     */

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    /**
     * 返回 毛重
     *
     * @return 毛重
     */

    public String getGrossWeight() {
        return grossWeight;
    }


    /**
     * 设置 毛重
     *
     * @param grossWeight 毛重
     */

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }


    /**
     * 返回 净重
     *
     * @return 净重
     */

    public String getNetWeight() {
        return netWeight;
    }


    /**
     * 设置 净重
     *
     * @param netWeight 净重
     */

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    /**
     * 返回 商品上架中文名称
     *
     * @return 商品上架中文名称
     */

    public String getProductChineseName() {
        return productChineseName;
    }


    /**
     * 设置 商品上架中文名称
     *
     * @param productChineseName 商品上架中文名称
     */

    public void setProductChineseName(String productChineseName) {
        this.productChineseName = productChineseName;
    }

    public String getStyleID() {
        return styleID;
    }


    public void setStyleID(String styleID) {
        this.styleID = styleID;
    }

    public String getProductName2() {
        return productName2;
    }


    public void setProductName2(String productName2) {
        this.productName2 = productName2;
    }

    public String getModel() {
        return model;
    }


    public void setModel(String model) {
        this.model = model;
    }

    public String getHSCode() {
        return HSCode;
    }


    public void setHSCode(String hSCode) {
        HSCode = hSCode;
    }

    public String getMailTax() {
        return mailTax;
    }


    public void setMailTax(String mailTax) {
        this.mailTax = mailTax;
    }

    /**
     * 返回 英文名称
     *
     * @return 英文名称
     */

    public String getProductForeignName() {
        return productForeignName;
    }


    /**
     * 设置 英文名称
     *
     * @param productForeignName 英文名称
     */

    public void setProductForeignName(String productForeignName) {
        this.productForeignName = productForeignName;
    }


}
