package com.xhl.bqlh.Api;

import com.xhl.bqlh.model.AOrderDetails;
import com.xhl.bqlh.model.AOrderInit;
import com.xhl.bqlh.model.AProductDetails;
import com.xhl.bqlh.model.AShopDetails;
import com.xhl.bqlh.model.AdModel;
import com.xhl.bqlh.model.BrandModel;
import com.xhl.bqlh.model.GarbageModel;
import com.xhl.bqlh.model.OrderSaveModel;
import com.xhl.bqlh.model.ProductModel;
import com.xhl.bqlh.model.SearchFastModel;
import com.xhl.bqlh.model.ShopModel;
import com.xhl.bqlh.model.VersionInfo;
import com.xhl.bqlh.model.app.SearchParams;
import com.xhl.bqlh.model.base.ResponseModel;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 16/4/8.
 */
public interface Api {


    /**
     * 用户登录
     */
    Callback.Cancelable userLogin(String loginName, String password, Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String userLogin = "appUser/checkLogin";

    /**
     * 更新用户头像
     */
    Callback.Cancelable userUpdateImage(String face, Callback.CommonCallback<ResponseModel<Object>> callback);

    String userUpdateImage = "appUser/updateHeadImage";

    /**
     * 修改个人信息
     */
    Callback.Cancelable userUpdateInfo(String address, String liablephone, String liablemail, Callback.CommonCallback<ResponseModel<String>> callback);

    String userUpdateInfo = "appUser/updatePersonalInfo";

    /**
     * 修改密码，检测旧密码
     */
    Callback.Cancelable userFixCheckPwd(String original, Callback.CommonCallback<ResponseModel<String>> callback);

    String userFixCheckPwd = "appUser/verifyOriginalPassword";

    /**
     * 修改密码，输入新密码
     */
    Callback.Cancelable userFixNewPwd(String latest, Callback.CommonCallback<ResponseModel<String>> callback);

    String userFixNewPwd = "appUser/updateLoginPassword";

    /**
     * 找回密码，获取验证码
     */
    Callback.Cancelable userForgetPwdGet(String userName, Callback.CommonCallback<ResponseModel<HashMap<String,String>>> callback);

    String userForgetPwdGet = "appUser/sendPhoneCode";

    /**
     * 找回密码，验证验证码
     */
    Callback.Cancelable userForgetPwdCheck(String phone, String num, Callback.CommonCallback<ResponseModel<Object>> callback);

    String userForgetPwdCheck = "appUser/checkCode";

    /**
     * 找回密码，新密码
     */
    Callback.Cancelable userForgetPwdNew(String userName, String pwd, Callback.CommonCallback<ResponseModel<Object>> callback);

    String userForgetPwdNew = "appUser/editPwd";

    //账户信息
    Callback.Cancelable userAccount(Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String userAccount = "appUser/queryPersonalAccount";

    /**
     * 首页广告
     */
    Callback.Cancelable homeAd(Callback.CommonCallback<ResponseModel<HashMap<String, AdModel>>> callback);

    String homeAd = "appAdvert/queryAppAdvert";

    /**
     * 首页按钮
     */
    Callback.Cancelable homeMenu(Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String homeMenu = "homeMenu/homeMenuList";

    /**
     * 首页区域选择
     */
    Callback.Cancelable homeArea(Callback.CommonCallback<GarbageModel> callback);

    String homeArea = "firstPage/initCityList";


    /**
     * 分类
     */
    Callback.Cancelable classify(Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String classify = "appCategory/queryCategoryList";


    /**
     * 联想搜索
     *
     * @param searchType 1:商品  2:店铺
     * @param searchKey  搜索内容
     */
    Callback.Cancelable searchFast(String searchType, String searchKey, Callback.CommonCallback<ResponseModel<SearchFastModel>> callback);

    String searchFast = "appProduct/lenovoMsgSearch";

    /**
     * 商品搜索
     */
    Callback.Cancelable searchProduct(SearchParams searchParams, Callback.CommonCallback<ResponseModel<GarbageModel<ProductModel>>> callback);

    String searchProduct = "appProduct/getProductByCondition";
    String searchProductSKU = "appProduct/getProductBySku";

    /**
     * 店铺搜索
     */
    Callback.Cancelable searchShop(String searchStore, Callback.CommonCallback<ResponseModel<GarbageModel<ShopModel>>> callback);

    String searchShop = "appProduct/seachStore";

    Callback.Cancelable searchShopNewPro(String shopId, Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String searchShopNewPro = "appProduct/getNewProductForStore";

    /**
     * 查询品牌
     */
    Callback.Cancelable searchProductBrand(SearchParams searchParams, Callback.CommonCallback<ResponseModel<List<BrandModel>>> callback);

    String searchProductBrand = "appProduct/findBrands";

    /**
     * 商品详情
     */
    Callback.Cancelable productDetails(String productId, Callback.CommonCallback<ResponseModel<AProductDetails>> callback);

    String productDetails = "appProduct/queryPorudctInfo";

    /**
     * 添加购物车商品
     */
    Callback.Cancelable carAdd(String productId, String storeId, int num, Callback.CommonCallback<ResponseModel<Object>> callback);

    String carAdd = "appshoppingcart/saveShoppingCart";

    /**
     * 删除购物车商品
     */
    Callback.Cancelable carDelete(String ids, Callback.CommonCallback<ResponseModel<Object>> callback);

    String carDelete = "appshoppingcart/deleteShoppingCart";

    /**
     * 购物车商品
     */
    Callback.Cancelable carInfo(Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String carInfo = "appshoppingcart/queryGoods";

    /**
     * 店铺信息
     */
    Callback.Cancelable shopInfo(String shopId, Callback.CommonCallback<ResponseModel<AShopDetails>> callback);

    String shopInfo = "appShop/appShopIndex";

    /**
     * 店铺广告信息
     */
    Callback.Cancelable shopAdInfo(String shopId, Callback.CommonCallback<ResponseModel<AShopDetails>> callback);

    String shopAdInfo = "appAdvert/queryShopAdvertList";

    /**
     * 店铺分类信息
     */
    Callback.Cancelable shopCategoryInfo(String shopId, Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String shopCategoryInfo = "appCategory/queryStoreCategory";

    /**
     * 订单个数信息
     */
    Callback.Cancelable orderNum(Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String orderNum = "appOrder/queryAllOrderNum";

    /**
     * 订单查询
     */
    Callback.Cancelable orderQuery(int type, int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String orderQuery = "appOrder/queryAllOrderByPage";

    /**
     * 订单详情
     */
    Callback.Cancelable orderDetails(String storeOrderCode, Callback.CommonCallback<ResponseModel<AOrderDetails>> callback);

    String orderDetails = "appOrder/initDetailInfoOrder";

    /**
     * 订单状态修改
     */
    Callback.Cancelable orderUpdateType(int type, String storeOrderCode, Callback.CommonCallback<ResponseModel<Object>> callback);

    String orderUpdateType = "appOrder/updateOrder";

    /**
     * 订单初始化
     */
    Callback.Cancelable orderCreateInit(String strPar, Callback.CommonCallback<ResponseModel<AOrderInit>> callback);

    String orderCreateInit = "appOrder/initConfirmOrder";

    /**
     * 订单保存
     */
    Callback.Cancelable orderCreate(String attrJsonStr, Callback.CommonCallback<ResponseModel<OrderSaveModel>> callback);

    String orderCreate = "appOrder/saveAppConfirmOrder";

    /**
     * 订单支付
     */
    Callback.Cancelable orderPay(String orderCode, Callback.CommonCallback<ResponseModel<String>> callback);

    String orderPay = "appPay/toPay";

    /**
     * 收藏添加
     */
    Callback.Cancelable collectAdd(String contentId, String collectType, Callback.CommonCallback<ResponseModel<Object>> callback);

    String collectAdd = "appUser/saveIntoCollect";

    /**
     * 收藏删除
     */
    Callback.Cancelable collectDelete(String contentId, Callback.CommonCallback<ResponseModel<Object>> callback);

    String collectDelete = "appUser/deleteCollectById";

    /**
     * 收藏查询商品
     */
    Callback.Cancelable collectQueryProduct( int pageSize, int pageNum,Callback.CommonCallback<ResponseModel<GarbageModel<ProductModel>>> callback);

    String collectQueryProduct = "appUser/queryCollectProductByPage";

    /**
     * 收藏查询店铺
     */
    Callback.Cancelable collectQueryShop( int pageSize, int pageNum,Callback.CommonCallback<ResponseModel<GarbageModel<ShopModel>>> callback);

    String collectQueryShop = "appUser/queryCollectShopByPage";

    /**
     * 收藏数量
     */
    Callback.Cancelable collectQueryNum(Callback.CommonCallback<ResponseModel<GarbageModel>> callback);

    String collectQueryNum = "appUser/queryCollectByUserCount";


    /**
     * 版本信息
     */
    Callback.Cancelable versionInfo(Callback.CommonCallback<ResponseModel<VersionInfo>> callback);

    String versionInfo = "appUser/queryNewVersionByType";

}
