package com.xhl.bqlh.Api;

import android.text.TextUtils;

import com.xhl.bqlh.AppConfig.NetWorkConfig;
import com.xhl.bqlh.model.AOrderDetails;
import com.xhl.bqlh.model.AOrderInit;
import com.xhl.bqlh.model.AProductDetails;
import com.xhl.bqlh.model.AShopDetails;
import com.xhl.bqlh.model.AdModel;
import com.xhl.bqlh.model.BrandModel;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.SearchFastModel;
import com.xhl.bqlh.model.ShopModel;
import com.xhl.bqlh.model.VersionInfo;
import com.xhl.bqlh.model.app.SearchParams;
import com.xhl.bqlh.model.base.ResponseModel;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 16/4/8.
 */
public class ApiImpl extends BaseApi implements Api {

    @Override
    public Callback.Cancelable userLogin(String loginName, String password, Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + userLogin;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("loginName", loginName);
        params.addBodyParameter("passWord", password);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable userUpdateImage(String face, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + userUpdateImage;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("face", face);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable userUpdateInfo(String address, String liablephone, String liablemail, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + userUpdateInfo;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("address", address);
        params.addBodyParameter("liablephone", liablephone);
        params.addBodyParameter("liablemail", liablemail);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable userFixCheckPwd(String original, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + userFixCheckPwd;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("original", original);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable userFixNewPwd(String latest, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + userFixNewPwd;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("latest", latest);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable userForgetPwdGet(String userName, Callback.CommonCallback<ResponseModel<HashMap<String, String>>> callback) {
        String url = NetWorkConfig.generalHost + userForgetPwdGet;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("userId", userName);
//        addExtendParams(params);
        print(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable userForgetPwdCheck(String phone, String num, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + userForgetPwdCheck;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("verifyCode", num);
//        addExtendParams(params);
        print(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable userForgetPwdNew(String userName, String pwd, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + userForgetPwdNew;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("pwd", pwd);
        params.addBodyParameter("userId", userName);
//        addExtendParams(params);
        print(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable userAccount(Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + userAccount;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable homeAd(Callback.CommonCallback<ResponseModel<HashMap<String, AdModel>>> callback) {
        String url = NetWorkConfig.generalHost + homeAd;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable homeMenu(Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + homeMenu;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable homeArea(Callback.CommonCallback<GarbageModel> callback) {
        String url = NetWorkConfig.generalHost + homeArea;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable classify(Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + classify;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable searchFast(String searchType, String searchText, Callback.CommonCallback<ResponseModel<SearchFastModel>> callback) {
        String url = NetWorkConfig.generalHost + searchFast;
        RequestParams params = new RequestParams(url);

        params.addBodyParameter("searchType", searchType);

        params.addBodyParameter("searchText", searchText);

        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable searchProduct(SearchParams searchParams, Callback.CommonCallback<ResponseModel<GarbageModel<ProductModel>>> callback) {
        String url;
        if (!TextUtils.isEmpty(searchParams.sku)) {
            url = NetWorkConfig.generalHost + searchProductSKU;//搜索sku
        } else {
            url = NetWorkConfig.generalHost + searchProduct;//搜索
        }

        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(searchParams.pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(searchParams.pageSize));
        params.addBodyParameter("shopId", searchParams.shopId);
        params.addBodyParameter("productName", searchParams.productName);
        params.addBodyParameter("categoryId", searchParams.categoryId);
        params.addBodyParameter("brandId", searchParams.brandId);
        params.addBodyParameter("orderBy", searchParams.getOrderBy());
        params.addBodyParameter("sortType", searchParams.getSortBy());
        params.addBodyParameter("myParam", searchParams.myParam);
        params.addBodyParameter("sku", searchParams.sku);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable searchShop(String searchStore, Callback.CommonCallback<ResponseModel<GarbageModel<ShopModel>>> callback) {
        String url = NetWorkConfig.generalHost + searchShop;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("searchStore", searchStore);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable searchShopNewPro(String shopId, Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + searchShopNewPro;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeId", shopId);
        params.addBodyParameter(PAGE_NUM, "1");
        params.addBodyParameter(PAGE_SIZE, "3");
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable searchProductBrand(SearchParams searchParams, Callback.CommonCallback<ResponseModel<List<BrandModel>>> callback) {
        String url = NetWorkConfig.generalHost + searchProductBrand;//搜索品牌
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("productName", searchParams.productName);
        params.addBodyParameter("categoryId", searchParams.categoryId);
        params.addBodyParameter("sku", searchParams.sku);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable productDetails(String productId, Callback.CommonCallback<ResponseModel<AProductDetails>> callback) {
        String url = NetWorkConfig.generalHost + productDetails;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("id", productId);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable carAdd(String productId, String storeId, int num, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + carAdd;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("productId", productId);
        params.addBodyParameter("storeId", storeId);
        params.addBodyParameter("productType", "1");
        params.addBodyParameter("purchaseQuantity", String.valueOf(num));
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable carDelete(String ids, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + carDelete;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("ids", ids);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable carInfo(Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + carInfo;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable shopInfo(String shopId, Callback.CommonCallback<ResponseModel<AShopDetails>> callback) {
        String url = NetWorkConfig.generalHost + shopInfo;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("shopId", shopId);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable shopAdInfo(String shopId, Callback.CommonCallback<ResponseModel<AShopDetails>> callback) {
        String url = NetWorkConfig.generalHost + shopAdInfo;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("shopId", shopId);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable shopCategoryInfo(String shopId, Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + shopCategoryInfo;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeId", shopId);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable orderNum(Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + orderNum;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable orderQuery(int type, int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + orderQuery;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("pageType", String.valueOf(type));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable orderDetails(String storeOrderCode, Callback.CommonCallback<ResponseModel<AOrderDetails>> callback) {
        String url = NetWorkConfig.generalHost + orderDetails;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", storeOrderCode);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable orderUpdateType(int type, String storeOrderCode, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + orderUpdateType;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", storeOrderCode);
        params.addBodyParameter("type", String.valueOf(type));
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable orderCreateInit(String strPar, Callback.CommonCallback<ResponseModel<AOrderInit>> callback) {
        String url = NetWorkConfig.generalHost + orderCreateInit;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("strPar", strPar);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable orderCreate(String attrJsonStr, Callback.CommonCallback<ResponseModel<HashMap<String, Object>>> callback) {
        String url = NetWorkConfig.generalHost + orderCreate;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("attrJsonStr", attrJsonStr);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable orderPay(String orderCode, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + orderPay;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("orderCode", orderCode);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable collectAdd(String contentId, String collectType, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + collectAdd;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("contentId", contentId);
        params.addBodyParameter("collectType", collectType);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable collectDelete(String contentId, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + collectDelete;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("contentId", contentId);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable collectQueryProduct(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<GarbageModel<ProductModel>>> callback) {
        String url = NetWorkConfig.generalHost + collectQueryProduct;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable collectQueryShop(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<GarbageModel<ShopModel>>> callback) {
        String url = NetWorkConfig.generalHost + collectQueryShop;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable collectQueryNum(Callback.CommonCallback<ResponseModel<GarbageModel>> callback) {
        String url = NetWorkConfig.generalHost + collectQueryNum;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);

        return x.http().post(params, callback);
    }


    @Override
    public Callback.Cancelable versionInfo(Callback.CommonCallback<ResponseModel<VersionInfo>> callback) {
        String url = NetWorkConfig.generalHost + versionInfo;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("client_type", "A");
        addExtendParams(params);
        return x.http().post(params, callback);
    }
}
