package com.xhl.bqlh.model;

/**
 * Created by Sum on 16/7/7.
 */
public class AProductDetails {

    private ProductModel product;
    private ProductSkuInfo skuInfo;

    private ShopModel shop;

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public ProductSkuInfo getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(ProductSkuInfo skuInfo) {
        this.skuInfo = skuInfo;
    }

    public ShopModel getShop() {
        return shop;
    }
}
