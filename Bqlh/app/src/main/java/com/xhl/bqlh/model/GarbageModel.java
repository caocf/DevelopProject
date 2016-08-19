package com.xhl.bqlh.model;

import android.text.TextUtils;

import com.xhl.bqlh.model.base.JsonResParser;

import org.xutils.http.annotation.HttpResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 16/7/1.
 * 赋值类
 */
@HttpResponse(parser = JsonResParser.class)
public class GarbageModel<T> {

    //用户接口返回
    private UserInfo user;
    private String token;

    public UserInfo getUserInfo() {
        return user;
    }

    //分类接口返回
    private List<ClassifyModel> cateList;

    public List<ClassifyModel> getCateList() {
        return cateList;
    }

    //店铺分类
    private List<ClassifyModel> categoryList;

    public List<ClassifyModel> getCategoryList() {
        return categoryList;
    }

    //resultData结构
    private List<T> resultData;
    private int totalResult;

    public List<T> getResultData() {
        return resultData;
    }

    public int getTotalResult() {
        return totalResult;
    }

    //搜索店铺新商品
    private List<ProductModel> productList;
    public List<ProductModel> getProductList() {
        return productList;
    }

    //购物车数据
    private HashMap<String, List<CarModel>> result;

    public HashMap<String, List<CarModel>> getResult() {
        return result;
    }


    //订单数量
    private String payNum;
    private String evaluateNum;
    private String receiveNum;

    public String getPayNum() {
        return payNum;
    }

    public String getEvaluateNum() {
        return evaluateNum;
    }

    public String getReceiveNum() {
        return receiveNum;
    }


    //订单查询
    private List<OrderModel> orderList;

    public List<OrderModel> getOrderList() {
        return orderList;
    }


    //收藏数据
    private List<T> list;

    public List<T> getList() {
        return list;
    }

    //收藏的数量
    private String UserCollectProductCount;
    private String UserCollectShopCount;

    public String getUserCollectProductCount() {
        if (TextUtils.isEmpty(UserCollectProductCount)) {
            return "0";
        }
        return UserCollectProductCount;
    }

    public String getUserCollectShopCount() {
        if (TextUtils.isEmpty(UserCollectShopCount)) {
            return "0";
        }
        return UserCollectShopCount;
    }


    //首页操作的广告位
    private List<OperatorMenu> homeMenu;

    public List<OperatorMenu> getHomeMenu() {
        return homeMenu;
    }


    private UserAccount userAccount;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    //区域数据
    private List<AreaModel> cityList;

    public List<AreaModel> getCityList() {
        return cityList;
    }

}
