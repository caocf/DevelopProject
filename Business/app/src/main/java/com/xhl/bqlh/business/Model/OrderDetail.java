package com.xhl.bqlh.business.Model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhanglj
 * @version V1.0
 * @Description: 订单详情类实例
 * @date 2015年8月24日 上午11:42:38
 */
public class OrderDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4142774811569924950L;

    /**
     * 主键
     */
    private String id;

    private SkuModel skuResult;

    /**
     * 子订单编号
     */
    private String storeOrderCode;

    private String salseOrderId;

    /**
     * 商品编码
     */
    private String goodId;

    /**
     * SKU码
     */
    private String skuCode;

    //商品优惠金额
    private float discount;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 类型（1：普通订单；2：团购订单；3：消费者促销订单；4：渠道促销订单；5订货会订单；6：积分订单）
     */
    private String type;

    /**
     * 数量
     */
    private BigDecimal num;

    /**
     * 商品总价
     */
    private BigDecimal totalPrice;

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
    private BigDecimal volume;

    /**
     * 佣金
     */
    private BigDecimal commission;

    /**
     * 重量
     */
    private BigDecimal weight;

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

    /**
     * SKU名称
     */
    private String skuName;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 店铺名称
     */
    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPic() {
        return productPic;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
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

    public String getSalseOrderId() {
        return salseOrderId;
    }

    public SkuModel getSkuResult() {
        if (skuResult == null) {
            return DEFAULT;
        }
        return skuResult;
    }

    public void setSkuResult(SkuModel skuResult) {
        this.skuResult = skuResult;
    }

    public float getDiscount() {
        return discount;
    }

    public static final SkuModel DEFAULT = new SkuModel();
}
