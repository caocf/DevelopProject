package com.xhl.world.model.serviceOrder;

import com.xhl.world.config.NetWorkConfig;

import java.io.Serializable;

/**
 * 商品实体对象
 */
public class Product implements Serializable {

    private String id;
    private String SKU;// sku编码
    private String productName;// 商品名称
    private String storeId;// 商户id
    private String storeName;// 商户名称
    private String shopId;
    private String shopName;//店铺名
    private String carriage;//运费
    private Integer productType;// 1-普通商品 2-渠道促销商品 3-订货会商品 4-团购商品 5-消费者促销商品 6-季节产品 7-新品上市
    private String productProperty;//商品类型：行邮商品、大贸商品、直邮商品
    private String productBillMode;//商品模式：1、集货 2备货
    private String productPic;// 商品封面图片
    private Integer productStatus;// 商品上下架状态 1-上架 2-下架 3-强制下架
    private Integer checkStatus;// 1-待提交 2-待审核 3-审核通过 4-已驳回
    private String commodityNum;//海关检验序号
    private String dutyNum;//完税商品分类编号
    private String pname;//完税商品分类品名
    private Integer stock;// 库存
    private Integer stockWarn;// 库存预警
    private String productDesc;// 商品描述
    private String promiseTime;// 承诺到货时间
    private String checkTime;// 审核时间
    private String checkRemark;// 审核备注
    private String downShelvesTime;// 下架时间
    private String createTime;
    private String updateTime;
    private String createUser;
    private String updateUser;
    private String delFlag;// 删除标志
    private Integer sortField;// 排序
    private Integer shopSortField;// 商铺排序
    //销量
    private Integer volume;

//	private String startDate;// 活动商品开始时间
//	private String endDate;// 活动商品结束时间
//	private String promoteRemark;// 促销说明
//	private String promoteMinNum;// 促销赠送起订量
//	private String promoteGavingNum;// 促销赠送数量

    // 非数据库字段
    private String categoryName;// 商品所属分类
    private String brandId;// 商品所属品牌id
    private String brandName;// 商品所属品牌名称
    private String filePaths;// 附件路径，多个以逗号隔开
    private String displayPrice;//显示价格
    private String displayMaxNum;//显示最大订单量
    private String displayMinNum;//显示最小起订量
    private String commisionProportion;//佣金比例
    private String costPrice;//商品成本价
    private String originalPrice;// 普通会员价格
    private String topPrice;//一级经销商价格
    private String secondPrice;//二级经销商价格
    private String thirdPrice;//三级经销商价格
    private String forthPrice;//四级经销商价格
    private String orderMinNum;//最低起订量
    private String orderMaxNum;//最大订单量

    private String tariff;   // 税率
    private String taxprice;   // 税价，通过（originalPrice+carriage）*tariff确定

    private String productPrice;//价格

    public String getId() {
        return id;
    }

    public String getSKU() {
        return SKU;
    }

    public String getProductName() {
        return productName;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getCarriage() {
        return carriage;
    }

    public Integer getProductType() {
        return productType;
    }

    public String getProductProperty() {
        return productProperty;
    }

    public String getProductBillMode() {
        return productBillMode;
    }

    public String getProductPic() {
        return productPic;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public String getCommodityNum() {
        return commodityNum;
    }

    public String getDutyNum() {
        return dutyNum;
    }

    public String getPname() {
        return pname;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getStockWarn() {
        return stockWarn;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getPromiseTime() {
        return promiseTime;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public String getCheckRemark() {
        return checkRemark;
    }

    public String getDownShelvesTime() {
        return downShelvesTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public Integer getSortField() {
        return sortField;
    }

    public Integer getShopSortField() {
        return shopSortField;
    }

    public Integer getVolume() {
        return volume;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getFilePaths() {
        return filePaths;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public String getDisplayMaxNum() {
        return displayMaxNum;
    }

    public String getDisplayMinNum() {
        return displayMinNum;
    }

    public String getCommisionProportion() {
        return commisionProportion;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public String getTopPrice() {
        return topPrice;
    }

    public String getSecondPrice() {
        return secondPrice;
    }

    public String getThirdPrice() {
        return thirdPrice;
    }

    public String getForthPrice() {
        return forthPrice;
    }

    public String getOrderMinNum() {
        return orderMinNum;
    }

    public String getOrderMaxNum() {
        return orderMaxNum;
    }

    public String getTariff() {
        return tariff;
    }

    public String getTaxprice() {
        return taxprice;
    }


    //統一商品图片Logo
    public String getProductPicUrl() {
        return NetWorkConfig.imageHost + productPic;
    }

    public String getProductPrice() {
        return productPrice;
    }
}
