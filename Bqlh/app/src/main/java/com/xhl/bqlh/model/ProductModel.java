package com.xhl.bqlh.model;

import android.text.TextUtils;

import com.xhl.bqlh.AppConfig.NetWorkConfig;
import com.xhl.bqlh.R;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sum on 16/7/5.
 */
public class ProductModel implements Serializable {

    public int mCurNum;//当前选中的数量

    private String id;//商品id
    private String contentId;//收藏id
    private String productPrice;//价格
    private String productName;
    private String productPic;
    private String storeId;//店铺id
    private int orderMinNum;
    private int productType;
    private int stock;
    private int productCollected;
    private String promiseTime;
    private String memberPrice;//会员价格
    private String originalPrice;
    private String bussinessPrice;
    private String promoteRemark;//活动提示
    private String sku;
    private String productDesc;
    private List<ProductAttachModel> attachmentList;

    public String getMemberPrice() {
        return memberPrice;
    }

    public String getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public String getStoreId() {
        return storeId;
    }

    public int getProductType() {
        return productType;
    }

    public String getProductPic() {
        return NetWorkConfig.imageHost + productPic;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public int getStock() {
        return stock;
    }

    public String getPromiseTime() {
        return promiseTime;
    }

    //登录后优先显示会员价
    public String getBussinessPrice() {
        if (!TextUtils.isEmpty(memberPrice)) {
            return memberPrice;
        }
        return bussinessPrice;
    }

    public int priceId() {
        if (!TextUtils.isEmpty(memberPrice)) {
            return R.string.price_m;
        } else if (!TextUtils.isEmpty(bussinessPrice)) {
            return R.string.price_b;
        }
        return R.string.price_s;
    }

    public int getOrderMinNum() {
        return orderMinNum;
    }

    public String getSku() {
        return sku;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public List<ProductAttachModel> getAttachmentList() {
        return attachmentList;
    }

    public String getProductActive() {
        if (!TextUtils.isEmpty(promoteRemark)) {
            return "【促销】" + promoteRemark;
        }
        return null;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getContentId() {
        return contentId;
    }

    public int getProductCollected() {
        return productCollected;
    }
}
