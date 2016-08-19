package com.xhl.bqlh.business.Model;

import android.text.TextUtils;

import com.xhl.bqlh.business.AppConfig.NetWorkConfig;
import com.xhl.bqlh.business.R;

import java.math.BigDecimal;

/**
 * Created by Sum on 16/4/14.
 */
public class ProductModel implements Cloneable{

    public ProductModel clone() throws CloneNotSupportedException {
        return (ProductModel) super.clone();
    }

    public int curNum;//临时记录数量
    public boolean checked;//是否选中
    public String applyId;//车削申请的id
    private SkuModel skuResult;

    private String id;
    private String SKU;//sku编码
    private String productName;//商品名称
    private String storeId;//商户id
    private String storeName;//商户名称
    private int productType;//1-普通商品 2-渠道促销商品 3-订货会商品 4-团购商品  5-消费者促销商品
    private String productPic;//商品封面图片
    private Integer productStatus;//商品上下架状态 1-上架  2-下架 3-强制下架
    private Integer checkStatus;//1-待提交 2-待审核 3-审核通过 4-已驳回
    private BigDecimal originalPrice;//商品原价
    private BigDecimal promoteSale;//活动价格 促销价 团购价
    private int stock;//库存
    private Integer stockWarn;//库存预警
    private String productDesc;//商品描述
    private String deliveryTime;//订购会商品预计发货时间
    private String promiseTime;//承诺到货时间
    private String startDate;//活动商品开始时间
    private String endDate;//活动商品结束时间
    private String checkTime;//审核时间
    private String downShelvesTime;//下架时间
    private String createTime;
    private String createUser;
    private String promoteRemark;//促销说明
    private String promoteMinNum;//促销赠送起订量
    private String promoteGavingNum;//促销赠送数量
    private String productUnit;//最小单位

    //非数据库字段
    private String categoryName;//商品所属分类
    private String brandId;//商品所属品牌id
    private BigDecimal bussinessPrice;//业态价格
    private BigDecimal activityPrice;//活动价格
    private BigDecimal orderMaxNum;//最大订单量
    private BigDecimal orderMinNum;//最小起订量

    private String shopId;
    private String shopName;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPromoteRemark() {
        if (TextUtils.isEmpty(promoteRemark)) {
            return "";
        }
        return "【促销】"+promoteRemark;
    }

    public String getPromoteGavingNum() {
        return promoteGavingNum;
    }

    public void setPromoteGavingNum(String promoteGavingNum) {
        this.promoteGavingNum = promoteGavingNum;
    }

    public void setPromoteRemark(String promoteRemark) {
        this.promoteRemark = promoteRemark;
    }

    public String getPromoteMinNum() {
        return promoteMinNum;
    }

    public void setPromoteMinNum(String promoteMinNum) {
        this.promoteMinNum = promoteMinNum;
    }

    public BigDecimal getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(BigDecimal activityPrice) {
        this.activityPrice = activityPrice;
    }

    public BigDecimal getOrderMaxNum() {
        return orderMaxNum;
    }

    public void setOrderMaxNum(BigDecimal orderMaxNum) {
        this.orderMaxNum = orderMaxNum;
    }

    public BigDecimal getOrderMinNum() {
        return orderMinNum;
    }

    public void setOrderMinNum(BigDecimal orderMinNum) {
        this.orderMinNum = orderMinNum;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String sKU) {
        SKU = sKU;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getProductPic() {
        return NetWorkConfig.imageHost + productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getPromoteSale() {
        return promoteSale;
    }

    public void setPromoteSale(BigDecimal promoteSale) {
        this.promoteSale = promoteSale;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Integer getStockWarn() {
        return stockWarn;
    }

    public void setStockWarn(Integer stockWarn) {
        this.stockWarn = stockWarn;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPromiseTime() {
        return promiseTime;
    }

    public void setPromiseTime(String promiseTime) {
        this.promiseTime = promiseTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getDownShelvesTime() {
        return downShelvesTime;
    }

    public void setDownShelvesTime(String downShelvesTime) {
        this.downShelvesTime = downShelvesTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public BigDecimal getBussinessPrice() {
        return bussinessPrice;
    }

    public void setBussinessPrice(BigDecimal bussinessPrice) {
        this.bussinessPrice = bussinessPrice;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public BigDecimal getProductPrice() {
        if (bussinessPrice == null) {
            return originalPrice;
        }
        return bussinessPrice;
    }

    public int getProductPriceHint() {
        if (bussinessPrice != null) {
            return R.string.product_price;
        }
        return R.string.product_price_or;
    }

    public SkuModel getSkuResult() {
        return skuResult;
    }

    public void setSkuResult(SkuModel skuResult) {
        this.skuResult = skuResult;
    }
}
