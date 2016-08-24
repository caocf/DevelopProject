package com.xhl.world.api;

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

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sum on 15/12/8.
 */
public interface Api {

    /* 用户操作接口 */

    /**
     * 用户登陆
     */
    void userLogin(String loginName, String loginPwd, Callback.CommonCallback<ResponseModel<UserInfo>> callback);

    String userLogin = "users/validateUserLogin";

    /**
     * 第三方登陆
     */
    void thirdLogin(String thirdType, String openId, Callback.CommonCallback<ResponseModel<UserInfo>> callback);

    String thirdLogin = "users/openIdLogin";

    /**
     * 绑定第三方账号
     */
    void thirdLoginBind(String openId, String openIdType, String loginName, String password, Callback.CommonCallback<ResponseModel<String>> callback);

    String thirdLoginBind = "users/openIdBindUser";

    /**
     * 快速注册
     */
    void thirdRegisterBind(HashMap<String, String> data, Callback.CommonCallback<ResponseModel<UserInfo>> callback);

    String thirdRegisterBind = "users/register";

    /**
     * 用户注销
     */
    void userLogout(Callback.CommonCallback<ResponseModel> callback);

    String userLogout = "users/userlogout";

    /**
     * 用户注册
     */
    void userRegister(String phone, String pwd, String userCode, Callback.CommonCallback<ResponseModel> callback);

    String userRegister = "users/useradd";


    /**
     * 用户注册验证码
     */
    void getRegisterVerifyCode(String phone, Callback.CommonCallback<ResponseModel> callback);

    String getRegisterVerifyCode = "users/getValidateCode";

    /**
     * 检测验证码
     */
    void checkVerifyCode(String phone, String code, Callback.CommonCallback<ResponseModel<String>> callback);

    String checkVerifyCode = "users/checkCode";


    /**
     * 修改密码
     */
    void changePwd(String code, String phone, String newPwd, Callback.CommonCallback<ResponseModel<SmsModel>> callback);

    String changePwd = "users/changePassword";


    /**
     * 获取修改密码的验证码
     */
    void getForgetPwdVerifyCode(String phone, Callback.CommonCallback<ResponseModel> callback);

    String getForgetPwdVerifyCode = "users/sendchangePasswordPhoneCode";

    /**
     * 更新用户信息
     */
    void updateUserInfo(UserInfo userInfo, Callback.CommonCallback<ResponseModel> callback);

    String updateUserInfo = "users/updateUserMsg";

    /**
     * 实名认证验证
     */
    void idCardVerify(String name, String cartId, String path, Callback.CommonCallback<ResponseModel> callback);

    String idCardVerify = "users/realNameApply";

    /**
     * 检测是否已经实名认证验证
     */
    void checkSecurity(Callback.CommonCallback<ResponseModel<HashMap<String, String>>> callback);

    String checkSecurity = "users/gotoSecurity";



    /*首页接口*/

    /**
     * 广告位查询
     */
    void homeAdvQueryList(Callback.CommonCallback<ResponseModel<AdvListModel>> callback);

    String homeAdvQueryList = "advert/queryList";


    /**
     * 限时特卖
     */
    void homeLimitSale(Callback.CommonCallback<ResponseModel<List<LimitSaleModel>>> callback);

    String homeLimitSale = "appProduct/limitbuy";

    /**
     * 猜你喜欢
     */
    void homeGuessLike(int pageNo, int pageSize, Callback.CommonCallback<ResponseModel<Response<ProductModel>>> callback);

    String homeGuessLike = "guessLike/queryGuessLike";



    /*分类接口*/

    /**
     * 商品分类信息查询
     */
    void categoryQueryList(String parentId, Callback.CommonCallback<ResponseModel<List<ClassifyItemModel>>> callback);

    String categoryQueryList = "appCategory/queryList";

    /**
     * 热门搜索
     */
    void hotSearch(Callback.CommonCallback<ResponseModel<List<HotSearchModel>>> callback);

    String hotSearch = "appProduct/queryHotWord";

    /**
     * 商品搜索结果
     */
    void queryProductByPage(QueryConditionEntity condition, Callback.CommonCallback<ResponseModel<Response<Product>>> callback);

    String queryProductByPage = "appProduct/queryProductByPage";

    /**
     * 商品详情
     */
    void productDetails(String productId, Callback.CommonCallback<ResponseModel<ProductDetailsModel>> callback);

    String productDetails = "appProduct/queryById";

    /**
     * 商品评价
     */
    void productEvaluate(String productId, String type, int curPage, int curPageSize, Callback.CommonCallback<ResponseModel<Response<EvaluateModel>>> callback);

    String productEvaluate = "appProduct/queryevaluatebyid";




    /*购物车接口*/

    /**
     * 我的购物列表查询
     */
    void shoppingCar(int pageNo, int pageSize, Callback.CommonCallback<ResponseModel<Response<ShoppingCarProductModel>>> callback);

    String ShoppingCar = "appShoppingCart/queryAppShoppingCart";

    /**
     * 增加到购物车
     */
    void addToShoppingCar(String id, String type, String num, Callback.CommonCallback<ResponseModel<Integer>> callback);

    String addToShoppingCar = "appShoppingCart/appSaveShoppingCart";


    /**
     * 修改购物车商品数量
     */
    void updateShoppingCartNum(String carId, String productId, String curSize, String range, Callback.CommonCallback<ResponseModel> callback);

    String updateShoppingCartNum = "appShoppingCart/updateAppShoppingCart";

    /**
     * 删除购物车商品
     **/
    void deleteShoppingCart(String[] array, Callback.CommonCallback<ResponseModel<Boolean>> callback);

    String deleteShoppingCart = "appShoppingCart/deleteAppShoppingCart";

    /**
     * 购物车商品验证
     */
    void verifyShopCarProduct(String productIdAndNum, Callback.CommonCallback<ResponseModel<HashMap<String, String>>> callback);

    String verifyShopCarProduct = "appOrder/verificationShopCart";

    /**
     * 确认订单前的商品验证
     */
    void verifyOrder(String attrJsonStr, Callback.CommonCallback<ResponseModel<HashMap<String, String>>> callback);

    String verifyOrder = "appOrder/verificationOrder";


    /*个人信息接口*/

    /**
     * 查询收藏的店铺
     */
    void collectionQueryShop(Callback.CommonCallback<ResponseModel<Response<CollectionModel>>> callback);

    String collectionQueryShop = "collect/queryShopCollect";


    /**
     * 查询收藏的商品
     */
    void collectionQueryProduct(Callback.CommonCallback<ResponseModel<Response<CollectionModel>>> callback);

    String collectionQueryProduct = "collect/queryProductCollect";

    /**
     * 添加收藏
     *
     * @param type 1:收藏商品，2:收藏店铺
     */
    void collectionAdd(String id, String type, Callback.CommonCallback<ResponseModel> callback);

    String collectionAdd = "collect/saveIntoAppCollect";


    /**
     * 移除收藏
     */
    void collectionRemove(String id, Callback.CommonCallback<ResponseModel> callback);

    String collectionRemove = "collect/deleteCollectById";

    /**
     * 浏览记录
     */

    void browseQuery(int curPage, int pageSize, Callback.CommonCallback<ResponseModel<Response<CollectionModel>>> callback);

    String browseQuery = "myInformation/queryMyBrowses";

    /**
     * 清空记录
     */
    void browseDelete(Callback.CommonCallback<ResponseModel<String>> callback);

    String browseDelete = "myInformation/deleteMyBrowses";

    /**
     * 查询数量
     */
    void browseAll(Callback.CommonCallback<ResponseModel<Response<BrowseModel>>> callback);

    String browseAll = "myInformation/myBrowse";


    /**
     * 个人总积分
     */
    void totalIntegral(Callback.CommonCallback<ResponseModel<Integer>> callback);

    String totalIntegral = "appIntegral/totalIntegral";

    /**
     * 积分明细
     */

    void integralList(int pageNo, int pageSize, Callback.CommonCallback<ResponseModel<Response<ScoreModel>>> callback);

    String integralList = "appIntegral/integralList";



    /*订单管理*/

    /**
     * 订单初始化
     */

    void orderInit(String strPar, String oderType, String orderListType, Callback.CommonCallback<ResponseModel<InitOrderModel>> callback);

    String orderInit = "appOrder/initConfirmOrder";


    /**
     * 订单保存
     *
     * @param attrJsonStr :[ {companyId payType couponsMoney:0.00 liableMail orderType couId productType orderProperty
     *                    attrOrderDetail[ goodId,productName,num,shoppingCartId,taxprice]}]
     * @param payMoney    支付的金额
     * @param integral    支付的积分
     * @param splitOrNot  是否分单 1：分单 2：不分单
     * @param addressId   收货地址Id
     * @param couponId    优惠券Id
     */
    void orderSave(String attrJsonStr, String payMoney, String integral, String splitOrNot, String addressId, String couponId,Callback.CommonCallback<ResponseModel<SaveOrderModel>> callback);

    String orderSave = "appOrder/saveConfirmOrder";


    /**
     * 根据订单状态查询
     *
     * @param state 1    为查询全部，
     *              2    为查询未付款，
     *              3    为查询未发货，
     *              4    为查询未评价的订单
     *              5    为查询退货订单
     */
    void orderQueryByState(int pageNo, int pageSize, String state, Callback.CommonCallback<ResponseModel<Response<Order>>> callback);

    String orderQueryByState = "appOrder/appOrderList";

    /**
     * 修改订单状态
     *
     * @param type  0 取消订单
     *              1 确认收货
     *              2 申请退货
     *              3 确认退款
     * @param order 子订单
     */
    void orderUpdateState(String type, Order order, Callback.CommonCallback<ResponseModel<Integer>> callback);

    String orderUpdateState = "appOrder/updateOrder";

    /**
     * 订单详情
     */
    void orderDetails(String storeOrderCode, Callback.CommonCallback<ResponseModel<Order>> callback);

    String orderDetails = "appOrder/initDetailInfoOrder";

    /**
     * 保存退货订单信息
     */
    void orderReturn(ApplyReturnGoodsModel apply, Callback.CommonCallback<ResponseModel<Integer>> callback);

    String orderReturn = "appOrder/saveReturn";

    /**
     * 提交订单评价
     */
    void orderEvaluate(EvaluateModel evaluate, Callback.CommonCallback<ResponseModel<String>> callback);

    String orderEvaluate = "appOrder/saveEvaluate";


    /**
     * 退货订单数据查询全部
     */
    void orderReturnInit(Callback.CommonCallback<ResponseModel<InitReturnOrderModel>> callback);

    String orderReturnInit = "appOrder/initReturnOrder";

    /**
     * 支付接口
     */
    String toAliPay = "appPay/toAliPay";

    void toAliPay(String orderPrice, String orderCode, Callback.CommonCallback<ResponseModel<String>> callback);

    /**
     * 支付完成通知服务器
     */
    void toAliPayReturn(String body, String outTreadNo, String tradeNo, String tradeStatus, Callback.CommonCallback<ResponseModel<Integer>> callback);

    String toAliPayReturn = "appPay/returnAliPayRe";

    /**
     * 查询物流
     */
    void orderLogistics(String storeOrderCode, Callback.CommonCallback<ResponseModel<LogisticsModel>> callback);

    String orderLogistics = "appOrder/findLogistics";


    /*地址管理*/

    /**
     * 增加地址
     */
    void addressAdd(AddressModel address, Callback.CommonCallback<ResponseModel<AddressModel>> callback);

    String addressAdd = "users/addAddress";

    /**
     * 删除地址
     */
    void addressDelete(String addressId, Callback.CommonCallback<ResponseModel<AddressModel>> callback);

    String addressDelete = "users/deleteAddress";

    /**
     * 更新地址
     */
    void addressUpdate(AddressModel address, Callback.CommonCallback<ResponseModel<AddressModel>> callback);

    String addressUpdate = "users/updateAddress";

    /**
     * 更新默认地址
     */
    void addressUpdateDefault(String addressId, Callback.CommonCallback<ResponseModel<AddressModel>> callback);

    String addressUpdateDefault = "users/updateDefaultAddress";

    /**
     * 查询所有地址
     */
    void addressQuery(Callback.CommonCallback<ResponseModel<Response<AddressModel>>> callback);

    String addressQuery = "users/findAddress";

    /**
     * 查询默认地址
     */
    void addressQueryDefault(Callback.CommonCallback<ResponseModel<AddressModel>> callback);

    String addressQueryDefault = "users/finddefaultAddressInfo";

    /**
     * 查询个人优惠券
     */
    void couponList(int pageSize, int pageNo, String type, Callback.CommonCallback<ResponseModel<Response<Coupon>>> callback);

    String couponList = "app/coupon/user/all/conponList";

    /**
     * 服务与反馈
     *
     * @param tp 代表不同的类型
     */
    void feedBack(String content, String phone, String tp, Callback.CommonCallback<ResponseModel<String>> callback);

    String feedBack = "feedBack/insertFeedBack";


    /**
     * 版本更新
     */
    void version(Callback.CommonCallback<ResponseModel<VersionModel>> callback);

    String version = "Version/queryNewVersionByType";


}
