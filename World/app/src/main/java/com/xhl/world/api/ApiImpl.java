package com.xhl.world.api;

import android.text.TextUtils;

import com.xhl.world.AppApplication;
import com.xhl.world.config.Constant;
import com.xhl.world.config.NetWorkConfig;
import com.xhl.world.model.AddressModel;
import com.xhl.world.model.AdvListModel;
import com.xhl.world.model.ApplyReturnGoodsModel;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.BrowseModel;
import com.xhl.world.model.ClassifyItemModel;
import com.xhl.world.model.CollectionModel;
import com.xhl.world.model.Coupon;
import com.xhl.world.model.Entities.QueryConditionEntity;
import com.xhl.world.model.EvaluateModel;
import com.xhl.world.model.HotSearchModel;
import com.xhl.world.model.InitOrderModel;
import com.xhl.world.model.InitReturnOrderModel;
import com.xhl.world.model.LimitSaleModel;
import com.xhl.world.model.LogisticsModel;
import com.xhl.world.model.ProductDetailsModel;
import com.xhl.world.model.ProductModel;
import com.xhl.world.model.SaveOrderModel;
import com.xhl.world.model.ScoreModel;
import com.xhl.world.model.ShoppingCarProductModel;
import com.xhl.world.model.SmsModel;
import com.xhl.world.model.VersionModel;
import com.xhl.world.model.serviceOrder.Order;
import com.xhl.world.model.serviceOrder.Product;
import com.xhl.world.model.user.UserInfo;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 15/12/8.
 */
public final class ApiImpl implements Api {

    private static final String PAGE_NO = "pageNo";
    private static final String PAGE_SIZE = "pageSize";

    private ApiImpl() {
    }

    private static ApiImpl api;

    public static ApiImpl instance() {
        if (api == null) {
            synchronized (ApiImpl.class) {
                api = new ApiImpl();
            }
        }
        return api;
    }

    //添加Cookie
    private void addCookie(RequestParams params) {

        String cookie = AppApplication.appContext.mCookie;
        if (!TextUtils.isEmpty(cookie)) {
            params.setHeader("Cookie", cookie);
        }
        Logger.v("req url:" + params.getUri() + " localCookie:" + cookie);

        if (Constant.isDebug) {
            List<KeyValue> bodyParams = params.getQueryStringParams();
            if (bodyParams.size() <= 0) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            for (KeyValue key : bodyParams) {
                if (key != null) {
                    builder.append(key.key).append(":").append(key.value == null ? "null" : key.value).append(" ");
                }
            }
            String log = builder.toString();
            Logger.v("body params [ " + log + " ]");
        }

    }

    /**
     * 用户登陆
     *
     * @param loginName
     * @param loginPwd
     * @param callback
     */
    @Override
    public void userLogin(String loginName, String loginPwd, Callback.CommonCallback<ResponseModel<UserInfo>> callback) {
        String method = userLogin;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("loginName", loginName);
        params.addBodyParameter("passWord", loginPwd);
        addCookie(params);
        x.http().post(params, callback);
    }

    @Override
    public void thirdLogin(String thirdType, String openId, Callback.CommonCallback<ResponseModel<UserInfo>> callback) {
        String method = thirdLogin;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("openIdType", thirdType);
        params.addBodyParameter("openId", openId);
        addCookie(params);
        x.http().post(params, callback);
    }

    @Override
    public void thirdLoginBind(String openId, String openIdType, String loginName, String password, Callback.CommonCallback<ResponseModel<String>> callback) {
        String method = thirdLoginBind;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("openIdType", openIdType);
        params.addBodyParameter("openId", openId);
        params.addBodyParameter("loginName", loginName);
        params.addBodyParameter("passWord", password);
//        addCookie(params);
        x.http().post(params, callback);
    }

    /**
     * 快速注册
     *
     * @param data
     * @param callback
     */
    @Override
    public void thirdRegisterBind(HashMap<String, String> data, Callback.CommonCallback<ResponseModel<UserInfo>> callback) {
        String method = thirdRegisterBind;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        String type = data.get("third_login_type");
        //登陆方法
        params.addBodyParameter("openIdType", type);
        //登陆类型
        params.addBodyParameter("openId", data.get("openid"));
        //注册手机号
        params.addBodyParameter("telephone", data.get("telephone"));
        //注册密码
        params.addBodyParameter("userPassword", data.get("userPassword"));
        //验证码
        params.addBodyParameter("userCode", data.get("userCode"));
        params.addBodyParameter("userId", "");

        //额外用户信息
        String name = type.equals("wx") ? data.get("nickname") : data.get("screen_name");
        String sex = type.equals("wx") ? data.get("headimgurl") : data.get("gender1");
        String avatar = type.equals("wx") ? data.get("") : data.get("profile_image_url");
        String area = data.get("province") + data.get("city");

        params.addBodyParameter("userName", name);
        params.addBodyParameter("sex", sex);
        params.addBodyParameter("area", area);
        params.addBodyParameter("vipPic",avatar);
        addCookie(params);
        x.http().post(params, callback);
    }

    /**
     * 用户注销
     *
     * @param callback
     */
    @Override
    public void userLogout(Callback.CommonCallback<ResponseModel> callback) {
        String method = userLogout;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        addCookie(params);
        x.http().get(params, callback);
    }

    /**
     * 用户注册
     */
    @Override
    public void userRegister(String phone, String pwd, String userCode, Callback.CommonCallback<ResponseModel> callback) {
        String method = userRegister;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("telephone", phone);
        params.addBodyParameter("userPassword", pwd);
        params.addBodyParameter("usercode", userCode);
        params.addBodyParameter("userId", "");
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 用户注册验证码
     *
     * @param phone
     * @param callback
     */
    @Override
    public void getRegisterVerifyCode(String phone, Callback.CommonCallback<ResponseModel> callback) {
        String method = getRegisterVerifyCode;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("phone", phone);
//        addCookie(params);
        x.http().post(params, callback);
    }

    /**
     * 检测验证码
     *
     * @param phone
     * @param code
     * @param callback
     */
    @Override
    public void checkVerifyCode(String phone, String code, Callback.CommonCallback<ResponseModel<String>> callback) {
        String method = checkVerifyCode;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("telephone", phone);
        params.addBodyParameter("userCode", code);
        addCookie(params);
        x.http().post(params, callback);
    }

    /**
     * 修改密码
     *
     * @param code
     * @param newPwd
     * @param callback
     */
    @Override
    public void changePwd(String code, String phone, String newPwd, Callback.CommonCallback<ResponseModel<SmsModel>> callback) {
        String method = changePwd;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("code", code);
        params.addBodyParameter("newPwd", newPwd);

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 获取修改密码的验证码
     *
     * @param phone
     * @param callback
     */
    @Override
    public void getForgetPwdVerifyCode(String phone, Callback.CommonCallback<ResponseModel> callback) {

        String method = getForgetPwdVerifyCode;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("phone", phone);
        addCookie(params);

        x.http().post(params, callback);

    }

    /**
     * 更新用户信息
     *
     * @param userInfo
     * @param callback
     */
    @Override
    public void updateUserInfo(UserInfo userInfo, Callback.CommonCallback<ResponseModel> callback) {
        String method = updateUserInfo;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
//        if (!TextUtils.isEmpty(userInfo.vipPic))
        params.addBodyParameter("vipPic", userInfo.vipPic);
//        if (!TextUtils.isEmpty(userInfo.sex))
        params.addBodyParameter("sex", userInfo.sex);
//        if (!TextUtils.isEmpty(userInfo.birthday))
        params.addBodyParameter("birthday", userInfo.birthday);
//        if (!TextUtils.isEmpty(userInfo.qq))
        params.addBodyParameter("qq", userInfo.qq);
//        if (!TextUtils.isEmpty(userInfo.area))
        params.addBodyParameter("area", userInfo.area);

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 实名认证验证
     *
     * @param name     姓名
     * @param cartId   身份证
     * @param path     身份证图片
     * @param callback
     */
    @Override
    public void idCardVerify(String name, String cartId, String path, Callback.CommonCallback<ResponseModel> callback) {
        String method = idCardVerify;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("realName", name);
        params.addBodyParameter("idCardNum", cartId);
        params.addBodyParameter("path", path);
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 检测是否已经验证
     *
     * @param callback
     */
    @Override
    public void checkSecurity(Callback.CommonCallback<ResponseModel<HashMap<String, String>>> callback) {
        String method = checkSecurity;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        addCookie(params);
        x.http().get(params, callback);
    }

    @Override
    public void homeAdvQueryList(Callback.CommonCallback<ResponseModel<AdvListModel>> callback) {
        String method = homeAdvQueryList;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        addCookie(params);
        x.http().get(params, callback);
    }


    /**
     * 限时特卖
     *
     * @param callback
     */
    @Override
    public void homeLimitSale(Callback.CommonCallback<ResponseModel<List<LimitSaleModel>>> callback) {
        String method = homeLimitSale;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        addCookie(params);
        x.http().get(params, callback);
    }


    @Override
    public void homeGuessLike(int pageNo, int pageSize, final Callback.CommonCallback<ResponseModel<Response<ProductModel>>> callback) {
        String method = homeGuessLike;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NO, String.valueOf(pageNo));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 商品分类信息查询
     *
     * @param parentId
     * @param callback
     */
    @Override
    public void categoryQueryList(String parentId, Callback.CommonCallback<ResponseModel<List<ClassifyItemModel>>> callback) {
        String method = categoryQueryList;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("parentId", parentId);
        addCookie(params);

        x.http().post(params, callback);
    }

    @Override
    public void hotSearch(Callback.CommonCallback<ResponseModel<List<HotSearchModel>>> callback) {
        String method = hotSearch;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);

        x.http().post(params, callback);
    }

    @Override
    public void queryProductByPage(QueryConditionEntity condition, Callback.CommonCallback<ResponseModel<Response<Product>>> callback) {
        String method = queryProductByPage;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NO, condition.getPageNo());
        params.addBodyParameter(PAGE_SIZE, condition.getPageSize());
        //查询的类型
        params.addBodyParameter("type", condition.getType());
        //查询的类型参数
        params.addBodyParameter("sku", condition.getQueryParmas());
        params.addBodyParameter("productName", condition.getQueryParmas());
        params.addBodyParameter("categoryId", condition.getQueryParmas());
        //查询排序条件
        params.addBodyParameter("orderBy", condition.getOrderBy());
        params.addBodyParameter("sortBy", condition.getSortBy());

        addCookie(params);

        x.http().post(params, callback);
    }

    @Override
    public void productDetails(String productId, Callback.CommonCallback<ResponseModel<ProductDetailsModel>> callback) {
        String method = productDetails;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("productId", productId);
        addCookie(params);
        x.http().post(params, callback);
    }

    /**
     * 商品评价
     *
     * @param type
     * @param curPage
     * @param curPageSize
     * @param callback
     */
    @Override
    public void productEvaluate(String productId, String type, int curPage, int curPageSize, Callback.CommonCallback<ResponseModel<Response<EvaluateModel>>> callback) {
        String method = productEvaluate;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("productId", productId);
        params.addBodyParameter("type", type);
        params.addBodyParameter(PAGE_NO, String.valueOf(curPage));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(curPageSize));
        addCookie(params);
        x.http().post(params, callback);
    }


    /**
     * 我的购物列表查询
     */
    @Override
    public void shoppingCar(int pageNo, int pageSize, Callback.CommonCallback<ResponseModel<Response<ShoppingCarProductModel>>> callback) {
        String method = ShoppingCar;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
//        params.addBodyParameter(PAGE_NO, String.valueOf(pageNo));
//        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addCookie(params);
        x.http().post(params, callback);
    }

    /**
     * 增加到购物车
     *
     * @param productId
     * @param type
     * @param num
     * @param callback
     */
    @Override
    public void addToShoppingCar(String productId, String type, String num, Callback.CommonCallback<ResponseModel<Integer>> callback) {

        String method = addToShoppingCar;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("productId", productId);
        params.addBodyParameter("num", num);
        params.addBodyParameter("type", type);

        addCookie(params);

        x.http().post(params, callback);

    }

    /**
     * 修改购物车商品数量
     *
     * @param carId     购物车id
     * @param productId 商品id
     * @param curSize   当前数量
     * @param range     修改范围，包含正负数
     * @param callback
     */
    @Override
    public void updateShoppingCartNum(String carId, String productId, String curSize, String range, Callback.CommonCallback<ResponseModel> callback) {
        String method = updateShoppingCartNum;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("id", carId);
        params.addBodyParameter("productId", productId);
        params.addBodyParameter("quantity", String.valueOf(curSize));
        params.addBodyParameter("range", String.valueOf(range));

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 删除购物车商品
     *
     * @param array
     * @param callback
     */
    @Override
    public void deleteShoppingCart(String[] array, Callback.CommonCallback<ResponseModel<Boolean>> callback) {
        String method = deleteShoppingCart;
        String url = NetWorkConfig.generalHost + method;

        StringBuilder builder = new StringBuilder();
        for (String st : array) {
            builder.append(st).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("array", builder.toString());

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 购物车商品验证
     */
    @Override
    public void verifyShopCarProduct(String productIdAndNum, Callback.CommonCallback<ResponseModel<HashMap<String, String>>> callback) {
        String method = verifyShopCarProduct;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("productIdAndNum", productIdAndNum);
        addCookie(params);

        x.http().get(params, callback);
    }

    /**
     * 确认订单前的商品验证
     *
     * @param attrJsonStr
     * @param callback
     */
    @Override
    public void verifyOrder(String attrJsonStr, Callback.CommonCallback<ResponseModel<HashMap<String, String>>> callback) {

        String method = verifyOrder;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("attrJsonStr", attrJsonStr);
        addCookie(params);

        x.http().get(params, callback);

    }

    /**
     * 查询收藏的店铺
     *
     * @param callback
     */
    @Override
    public void collectionQueryShop(Callback.CommonCallback<ResponseModel<Response<CollectionModel>>> callback) {
        String method = collectionQueryShop;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        addCookie(params);

        x.http().get(params, callback);
    }

    /**
     * 查询收藏的商品
     *
     * @param callback
     */
    @Override
    public void collectionQueryProduct(Callback.CommonCallback<ResponseModel<Response<CollectionModel>>> callback) {
        String method = collectionQueryProduct;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);

        addCookie(params);

        x.http().get(params, callback);
    }

    /**
     * 添加收藏
     *
     * @param type 1:收藏商品，2:收藏店铺
     */
    @Override
    public void collectionAdd(String productId, String type, Callback.CommonCallback<ResponseModel> callback) {
        String method = collectionAdd;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter("id", productId);
        params.addBodyParameter("type", type);

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 移除收藏
     *
     * @param id
     * @param callback
     */
    @Override
    public void collectionRemove(String id, Callback.CommonCallback<ResponseModel> callback) {
        String method = collectionRemove;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("id", id);

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 浏览记录
     *
     * @param callback
     */
    @Override
    public void browseQuery(int curPage, int pageSize, Callback.CommonCallback<ResponseModel<Response<CollectionModel>>> callback) {
        String method = browseQuery;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NO, String.valueOf(curPage));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addCookie(params);

        x.http().get(params, callback);
    }

    /**
     * 清空记录
     *
     * @param callback
     */
    @Override
    public void browseDelete(Callback.CommonCallback<ResponseModel<String>> callback) {
        String method = browseDelete;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        addCookie(params);
        x.http().get(params, callback);
    }

    /**
     * 查询数量
     *
     * @param callback
     */
    @Override
    public void browseAll(Callback.CommonCallback<ResponseModel<Response<BrowseModel>>> callback) {
        String method = browseAll;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        addCookie(params);
        x.http().get(params, callback);
    }

    /**
     * 总积分
     */
    @Override
    public void totalIntegral(Callback.CommonCallback<ResponseModel<Integer>> callback) {
        String method = totalIntegral;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);

        addCookie(params);

        x.http().get(params, callback);
    }

    /**
     * 积分详细
     */
    @Override
    public void integralList(int pageNo, int pageSize, Callback.CommonCallback<ResponseModel<Response<ScoreModel>>> callback) {
        String method = integralList;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NO, String.valueOf(pageNo));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 订单初始化
     *
     * @param strPar        jsonObject results key:sellerId,productId,quantity,productType  购物车添加：shoppingCartId
     * @param oderType      1：普通订单；2：团购订单；3：消费者促销订单；4：渠道促销订单；5订货会订单；6：积分订单；7：经销商订单
     * @param orderListType 1：大贸商品  2：行邮商品 3: 直邮
     */
    @Override
    public void orderInit(String strPar, String oderType, String orderListType, Callback.CommonCallback<ResponseModel<InitOrderModel>> callback) {
        String method = orderInit;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("strPar", strPar);//商品json集合
        params.addBodyParameter("oderType", "1");//客户端默认都是1类型订单
        params.addBodyParameter("orderListType", "0");
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 订单保存
     *
     * @param attrJsonStr :[ {companyId payType couponsMoney:0.00 liableMail orderType couId productType orderProperty
     *                    attrOrderDetail[ goodId,productName,num,shoppingCartId,taxprice]}]
     * @param payMoney    支付的金额
     * @param integral    支付的积分
     * @param splitOrNot  是否分单 1：分单 2：不分单
     * @param addressId   收货地址Id
     * @param callback
     */
    @Override
    public void orderSave(String attrJsonStr, String payMoney, String integral, String splitOrNot, String addressId, String couponId, Callback.CommonCallback<ResponseModel<SaveOrderModel>> callback) {
        String method = orderSave;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("attrJsonStr", attrJsonStr);//订单json集合
        params.addBodyParameter("needInovice", "100");//是否需要发票，1：需要，其他都是不需要
        params.addBodyParameter("payMoney", payMoney);
        params.addBodyParameter("integral", integral);
        params.addBodyParameter("splitOrNot", splitOrNot);//分单状态
        params.addBodyParameter("addressId", addressId);//收货地址id
        params.addBodyParameter("couponUserId", couponId);//优惠券id
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 根据订单状态查询
     *
     * @param state 1    查询全部
     *              2    查询未付款
     *              3    查询待发货
     *              4    查询待收货
     *              5    查询未评价的订单
     *              6    查询退货订单
     */
    @Override
    public void orderQueryByState(int pageNo, int pageSize, String state, Callback.CommonCallback<ResponseModel<Response<Order>>> callback) {
        String method = orderQueryByState;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        if (state.equals(1)) {
            params.addBodyParameter(PAGE_NO, String.valueOf(pageNo));
            params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        }
        params.addBodyParameter("key", state);
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 修改订单状态
     *
     * @param type     0 取消订单
     *                 1 确认收货
     *                 2 申请退货
     *                 3 确认退款
     * @param order    子订单
     * @param callback
     */
    @Override
    public void orderUpdateState(String type, Order order, Callback.CommonCallback<ResponseModel<Integer>> callback) {
        String method = orderUpdateState;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("type", type);
        params.addBodyParameter("storeOrderCode", order.getStoreOrderCode());
        //确认收货
        params.addBodyParameter("payMoney", order.getOrderMoney());
//        if (type.equals("2")) {
//            params.addBodyParameter("returnExplain", order.getOrderMoney());
//            params.addBodyParameter("returnsType", order.getOrderMoney());
//        }

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 订单详情
     *
     * @param storeOrderCode 子订单编号
     * @param callback
     */
    @Override
    public void orderDetails(String storeOrderCode, Callback.CommonCallback<ResponseModel<Order>> callback) {
        String method = orderDetails;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", storeOrderCode);
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 保存退货订单信息
     *
     * @param apply
     * @param callback
     */
    @Override
    public void orderReturn(ApplyReturnGoodsModel apply, Callback.CommonCallback<ResponseModel<Integer>> callback) {
        String method = orderReturn;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", apply.getStoreOrderCode());
        params.addBodyParameter("returnType", apply.getReturnType());
        params.addBodyParameter("phone", apply.getPhone());
        params.addBodyParameter("returnContent", apply.getReturnContent());
        params.addBodyParameter("productId", apply.getProductId());
        params.addBodyParameter("productNum", apply.getProductNum());
        params.addBodyParameter("companyId", apply.getCompanyId());

        addCookie(params);

        x.http().post(params, callback);
    }

    @Override
    public void orderEvaluate(EvaluateModel evaluate, Callback.CommonCallback<ResponseModel<String>> callback) {
        String method = orderEvaluate;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", evaluate.getStoreOrderCode());
        params.addBodyParameter("companyId", evaluate.getCompanyId());
        params.addBodyParameter("goodsId", evaluate.getGoodsId());
        params.addBodyParameter("rate", evaluate.getRate());
        params.addBodyParameter("rateContenet", evaluate.getRateContent());
        params.addBodyParameter("rateImg", evaluate.getRateImg());

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 退货订单数据查询全部
     *
     * @param callback
     */
    @Override
    public void orderReturnInit(Callback.CommonCallback<ResponseModel<InitReturnOrderModel>> callback) {
        String method = orderReturnInit;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        addCookie(params);

        x.http().get(params, callback);
    }

    @Override
    public void toAliPay(String orderPrice, String orderCode, Callback.CommonCallback<ResponseModel<String>> callback) {
        String method = toAliPay;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("orderPrice", orderPrice);
        params.addBodyParameter("orderCode", orderCode);
        addCookie(params);
        x.http().post(params, callback);
    }

    @Override
    public void toAliPayReturn(String body, String outTreadNo, String tradeNo, String tradeStatus, Callback.CommonCallback<ResponseModel<Integer>> callback) {

    }

    /**
     * 查询物流信息
     */
    @Override
    public void orderLogistics(String storeOrderCode, Callback.CommonCallback<ResponseModel<LogisticsModel>> callback) {
        String method = orderLogistics;
        String url = NetWorkConfig.generalHost + method;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", storeOrderCode);
        addCookie(params);
        x.http().post(params, callback);
    }


    /**
     * 增加地址
     *
     * @param address
     * @param callback
     */
    @Override
    public void addressAdd(AddressModel address, Callback.CommonCallback<ResponseModel<AddressModel>> callback) {

        String method = addressAdd;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter("area", address.getArea());//区域

        params.addBodyParameter("postCode", address.getPostCode());//邮编
        params.addBodyParameter("consigneeName", address.getConsigneeName());//收货人
        params.addBodyParameter("telephone", address.getTelephone());//收货人手机号
        params.addBodyParameter("idCard", address.getIdCard());//身份证号
        params.addBodyParameter("address", address.getAddress());//地址
        params.addBodyParameter("defaultAddress", address.getDefaultAddress());//是否设置成默认

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 删除地址
     *
     * @param addressId
     * @param callback
     */
    @Override
    public void addressDelete(String addressId, Callback.CommonCallback<ResponseModel<AddressModel>> callback) {
        String method = addressDelete;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("addressId", addressId);//地址id

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 更新地址
     *
     * @param address
     * @param callback
     */
    @Override
    public void addressUpdate(AddressModel address, Callback.CommonCallback<ResponseModel<AddressModel>> callback) {
        String method = addressUpdate;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("area", address.getArea());//区域
        params.addBodyParameter("postCode", address.getPostCode());//邮编
        params.addBodyParameter("consigneeName", address.getConsigneeName());//收货人
        params.addBodyParameter("telephone", address.getTelephone());//收货人手机号
        params.addBodyParameter("idCard", address.getIdCard());//身份证号
        params.addBodyParameter("address", address.getAddress());//地址
        params.addBodyParameter("defaultAddress", address.getDefaultAddress());//是否设置成默认
        params.addBodyParameter("addressId", address.getId());//地址id

        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 更新默认地址
     *
     * @param addressId
     * @param callback
     */
    @Override
    public void addressUpdateDefault(String addressId, Callback.CommonCallback<ResponseModel<AddressModel>> callback) {
        String method = addressUpdateDefault;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("addressId", addressId);//地址id
        addCookie(params);
        x.http().post(params, callback);
    }

    /**
     * 查询地址
     *
     * @param callback
     */
    @Override
    public void addressQuery(Callback.CommonCallback<ResponseModel<Response<AddressModel>>> callback) {
        String method = addressQuery;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        addCookie(params);
        x.http().get(params, callback);
    }

    /**
     * 查询默认地址
     *
     * @param callback
     */
    @Override
    public void addressQueryDefault(Callback.CommonCallback<ResponseModel<AddressModel>> callback) {
        String method = addressQueryDefault;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);
        addCookie(params);

        x.http().get(params, callback);
    }

    @Override
    public void couponList(int pageSize, int pageNo, String type, Callback.CommonCallback<ResponseModel<Response<Coupon>>> callback) {
        String method = couponList;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter("status", type);
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 服务与反馈
     *
     * @param content
     * @param phone
     * @param tp       代表不同的类型
     * @param callback
     */
    @Override
    public void feedBack(String content, String phone, String tp, Callback.CommonCallback<ResponseModel<String>> callback) {
        String method = feedBack;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter("content", content);

        params.addBodyParameter("phone", phone);
        params.addBodyParameter("tp", tp);
        addCookie(params);

        x.http().post(params, callback);
    }

    /**
     * 版本更新
     *
     * @param callback
     */
    @Override
    public void version(Callback.CommonCallback<ResponseModel<VersionModel>> callback) {
        String method = version;
        String url = NetWorkConfig.generalHost + method;

        RequestParams params = new RequestParams(url);

        params.addBodyParameter("client_type", "A");
        addCookie(params);

        x.http().post(params, callback);
    }
}
