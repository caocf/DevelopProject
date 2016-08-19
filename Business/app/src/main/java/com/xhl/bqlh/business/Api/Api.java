package com.xhl.bqlh.business.Api;

import com.xhl.bqlh.business.Db.Member;
import com.xhl.bqlh.business.Model.App.OrderQueryCondition;
import com.xhl.bqlh.business.Model.App.ProductQueryCondition;
import com.xhl.bqlh.business.Model.App.RegisterModel;
import com.xhl.bqlh.business.Model.App.ShopSignModel;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.BusinessTypeModel;
import com.xhl.bqlh.business.Model.CategoryItemModel;
import com.xhl.bqlh.business.Model.GiftModel;
import com.xhl.bqlh.business.Model.OrderClearModel;
import com.xhl.bqlh.business.Model.OrderInitModel;
import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.Model.OrderSaveModel;
import com.xhl.bqlh.business.Model.ProductBrandModel;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.Model.ProductReturn;
import com.xhl.bqlh.business.Model.ShopApplyModel;
import com.xhl.bqlh.business.Model.ShopDisplayModel;
import com.xhl.bqlh.business.Model.ShopFriendsModel;
import com.xhl.bqlh.business.Model.SignConfigModel;
import com.xhl.bqlh.business.Model.SignRecordModel;
import com.xhl.bqlh.business.Model.StatisticsModel;
import com.xhl.bqlh.business.Model.TaskModel;
import com.xhl.bqlh.business.Model.UserInfo;
import com.xhl.bqlh.business.Model.VersionInfo;

import org.xutils.common.Callback;

/**
 * Created by Sum on 16/4/8.
 */
public interface Api {

    /**
     * 用户登陆接口
     */
    Callback.Cancelable userLogin(String loginName, String password, Callback.CommonCallback<ResponseModel<UserInfo>> callback);

    String userLogin = "serviceUser/checkLogin";

    /**
     * 用户退出接口
     */
    Callback.Cancelable userQuit(Callback.CommonCallback<ResponseModel<Object>> callback);

    String userQuit = "serviceUser/loginOut";

    /**
     * 获取验证码
     */
    Callback.Cancelable userGetCode(String userName, Callback.CommonCallback<ResponseModel<String>> callback);

    String userGetCode = "serviceUser/sendPhoneVerificationCode";

    /**
     * 验证验证码
     */
    Callback.Cancelable userVerifyCode(String userName, String verifyCode, Callback.CommonCallback<ResponseModel<Object>> callback);

    String userVerifyCode = "serviceUser/checkPhoneVerificationCode";

    /**
     * 修改密码
     */
    Callback.Cancelable userUpdatePwd(String userName, String userPassword, Callback.CommonCallback<ResponseModel<String>> callback);

    String userUpdatePwd = "serviceUser/updatePassWord";

    /**
     * 更新用户头像
     */
    Callback.Cancelable userUpdateImage(String face, Callback.CommonCallback<ResponseModel<Object>> callback);

    String userUpdateImage = "serviceUser/updateSalesMan";

    /**
     * 获取每天的任务数据
     */
    Callback.Cancelable pullTask(Callback.CommonCallback<ResponseModel<TaskModel>> callback);

    String pullTask = "serviceUser/queryVisitPlanBySalesMan";

    /**
     * 获取本周数据
     */
    Callback.Cancelable pullTaskRecord(Callback.CommonCallback<ResponseModel<TaskModel>> callback);

    String pullTaskRecord = "serviceUser/queryCheckin";

    /**
     * 查询全部经销商会员
     */
    Callback.Cancelable pullTaskMember(Callback.CommonCallback<ResponseModel<Member>> callback);

    String pullTaskMember = "serviceUser/queryAllRetailerBySalesMan";


    /**
     * 经销商店铺三级分类
     */
    Callback.Cancelable storeCategory(String retailerId, Callback.CommonCallback<ResponseModel<CategoryItemModel>> callback);

    String storeCategory = "appServiceShop/queryCategory";

    /**
     * 经销商店铺分类对应的品牌
     */
    Callback.Cancelable storeBrand(String categoryId, Callback.CommonCallback<ResponseModel<ProductBrandModel>> callback);

    String storeBrand = "appServiceShop/shopBrand";

    /**
     * 经销商店商品
     */
    Callback.Cancelable storeProduct(int pageSize, int pageNum, String shopId, String categoryId, String brandId, String productName, String sku, Callback.CommonCallback<ResponseModel<ProductModel>> callback);

    String storeProduct = "appServiceProduct/productList";


    /**
     * 车削商品查看申请通过商品
     */
    Callback.Cancelable storeCarQueryApply(int pageSize, int pageNum, String shopId, Callback.CommonCallback<ResponseModel<ApplyModel>> callback);

    String storeCarQueryApply = "apply/queryApplyInventory";

    /**
     * 车削商品退库申请
     */
    Callback.Cancelable storeCarReturnApply(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<ApplyModel>> callback);

    String storeCarReturnApply = "apply/queryApplyInventory";

    /**
     * 车削商品删除
     */
    Callback.Cancelable storeCarConfirmDelete(String products, Callback.CommonCallback<ResponseModel<Object>> callback);

    String storeCarConfirmDelete = "apply/deleteApplyProduct";

    /**
     * 我的仓库确认申请
     */
    @Deprecated()
    Callback.Cancelable storeCarConfirmApply(String logId, String products, Callback.CommonCallback<ResponseModel<Object>> callback);

    String storeCarConfirmApply = "apply/confirmApply";

    /**
     * 装车单查询
     */
    Callback.Cancelable storeApplyQuery(Callback.CommonCallback<ResponseModel<ApplyModel>> callback);

    String storeApplyQuery = "apply/queryApplyCarSales";

    /**
     * 装车单添加
     */
    Callback.Cancelable storeApplyAdd(String products, String orderIds, Callback.CommonCallback<ResponseModel<Object>> callback);

    String storeApplyAdd = "apply/applyCarSales";

    /**
     * 装车单更新数量
     */
    Callback.Cancelable storeApplyUpdate(String type, String products, Callback.CommonCallback<ResponseModel<Object>> callback);

    String storeApplyUpdate = "apply/updateCarSales";


    /**
     * 订单初始化
     */
    Callback.Cancelable orderInit(String strPar, String orderType, String retailerId, Callback.CommonCallback<ResponseModel<OrderInitModel>> callback);

    String orderInit = "appServiceOrder/initConfirmOrder";

    /**
     * 保存订单
     */
    Callback.Cancelable orderSave(String attrJsonStr, String giftJsonStr, String realMoney, String realPayMoney, String retailerId, Callback.CommonCallback<ResponseModel<OrderSaveModel>> callback);

    String orderSave = "appServiceOrder/saveAppConfirmOrder";

    /**
     * 订单详情
     */
    Callback.Cancelable orderInfo(String storeOrderCode, Callback.CommonCallback<ResponseModel<OrderModel>> callback);

    String orderInfo = "appServiceOrder/initDetailInfoOrder";

    /**
     * 订单搜索
     */
    Callback.Cancelable orderQuery(int pageSize, int pageNum, OrderQueryCondition condition, Callback.CommonCallback<ResponseModel<OrderModel>> callback);

    String orderQuery = "appServiceOrder/queryOrder";

    /**
     * 统计订单商品
     */
    Callback.Cancelable orderStatistics(ProductQueryCondition condition, Callback.CommonCallback<ResponseModel<StatisticsModel>> callback);

    String orderStatistics = "serviceUser/querySalesmanOrderCount";


    /**
     * 赊账统计店铺
     */
    Callback.Cancelable orderAccountStatistics(ProductQueryCondition condition, Callback.CommonCallback<ResponseModel<StatisticsModel>> callback);

    String orderAccountStatistics = "serviceUser/queryOrderCreditCount";


    /**
     * 代发货的全部订单加详情
     */
    Callback.Cancelable orderQueryDetail(Callback.CommonCallback<ResponseModel<OrderModel>> callback);

    String orderQueryDetail = "appServiceOrder/querySalseOrder";

    /**
     * 订单还款
     */
    Callback.Cancelable orderRepay(String orderCode, String storeOrderCode, String repayment, String arrears, String retailerId, String remark, Callback.CommonCallback<ResponseModel<String>> callback);

    String orderRepay = "appServiceOrder/repaymentOrder";

    /**
     * 订单还款记录
     */
    Callback.Cancelable orderClearRecord(int pageSize, int pageNum, String storeOrderCode, Callback.CommonCallback<ResponseModel<OrderClearModel>> callback);

    String orderClearRecord = "serviceUser/findOrderCreditlogByPage";

    /**
     * 订单发货
     */
    Callback.Cancelable orderSend(String storeOrderCode, Callback.CommonCallback<ResponseModel<Object>> callback);

    String orderSend = "appServiceOrder/updateOrder";

    /**
     * 零售商业态
     */
    Callback.Cancelable registerBusinessType(Callback.CommonCallback<ResponseModel<BusinessTypeModel>> callback);

    String registerBusinessType = "serviceUser/queryBussinessTypeList";

    /**
     * 零售商区域
     */
    Callback.Cancelable registerShopAreaId(RegisterModel register, Callback.CommonCallback<ResponseModel<String>> callback);

    String registerShopAreaId = "serviceUser/queryAreaId";

    /**
     * 注册更新零售商
     */
    Callback.Cancelable registerRetailer(RegisterModel data, Callback.CommonCallback<ResponseModel<Object>> callback);

    String registerRetailer = "serviceUser/registerRetailer";
    String registerRetailerUpdate = "serviceUser/updateRetailerMember";

    /**
     * 查看注册零售商
     */
    Callback.Cancelable registerQueryRetailer(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<ShopApplyModel>> callback);

    String registerQueryRetailer = "serviceUser/queryRetailerBySalesMan";

    /**
     * 查看零售商
     */
    Callback.Cancelable registerQueryRetailerById(String shopId, Callback.CommonCallback<ResponseModel<ShopApplyModel>> callback);

    String registerQueryRetailerById = "serviceUser/queryRetailerMessageById";

    /**
     * 查看提交的好友零售商
     */
    Callback.Cancelable registerQueryRetailerFriends(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<ShopFriendsModel>> callback);

    String registerQueryRetailerFriends = "serviceUser/queryRetailerMemberBySalesMan";

    /**
     * 零售商添加好友
     */
    Callback.Cancelable registerAddRetailer(String shopId, Callback.CommonCallback<ResponseModel<String>> callback);

    String registerAddRetailer = "serviceUser/salesManAddRetailerAndDistributorNew";

    /**
     * 业务员签到
     */
    Callback.Cancelable shopCheckIn(ShopSignModel condition, Callback.CommonCallback<ResponseModel<Object>> callback);

    String shopCheckIn = "serviceUser/salseManCheckin";

    /**
     * 店铺赊账订单数量
     */
    Callback.Cancelable shopAccountOrder(String shopId, Callback.CommonCallback<ResponseModel<Integer>> callback);

    String shopAccountOrder = "serviceUser/creditNum";


    /**
     * 店铺关闭
     */
    Callback.Cancelable shopClose(String shopId, Callback.CommonCallback<ResponseModel<String>> callback);

    String shopClose = "serviceUser/closeRetailer";


    /**
     * 版本信息
     */
    Callback.Cancelable versionInfo(Callback.CommonCallback<ResponseModel<VersionInfo>> callback);

    String versionInfo = "serviceUser/queryNewVersionByType";

    /**
     * 支付二维码
     */
    Callback.Cancelable payImage(int type, Callback.CommonCallback<ResponseModel<String>> callback);

    String payImage = "serviceUser/queryDealerPayCode";


    /**
     * 考勤规则
     */
    Callback.Cancelable signRule(Callback.CommonCallback<ResponseModel<SignConfigModel>> callback);

    String signRule = "serviceUser/querySysUserTime";

    /**
     * 考勤记录
     */
    Callback.Cancelable signRecord(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<SignRecordModel>> callback);

    String signRecord = "serviceUser/querySalesManAttence";

    /**
     * 一个月的记录
     */
    Callback.Cancelable signRecordTable(Callback.CommonCallback<ResponseModel<SignRecordModel>> callback);

    String signRecordTable = "serviceUser/getSalesManView";

    /**
     * 添加考勤
     */
    Callback.Cancelable sign(SignRecordModel sign, Callback.CommonCallback<ResponseModel<String>> callback);

    String sign = "serviceUser/addSalesManAttence";

    /**
     * 添加一个陈列展示
     */
    Callback.Cancelable shopDisplayAdd(ShopDisplayModel addOne, Callback.CommonCallback<ResponseModel<String>> callback);

    String shopDisplayAdd = "serviceUser/addStoreDisplay";

    /**
     * 查询陈列展示
     */
    Callback.Cancelable shopDisplayQuery(String shopId, int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<ShopDisplayModel>> callback);

    String shopDisplayQuery = "serviceUser/queryStoreDisplay";

    /**
     * 赠品查询
     */
    Callback.Cancelable giftQuery(Callback.CommonCallback<ResponseModel<GiftModel>> callback);

    String giftQuery = "appServiceProduct/giftList";

    /**
     * 退货查询
     *
     * @param type OrderReturnType
     */
    Callback.Cancelable productReturnQuery(int pageSize, int pageNum, String type, Callback.CommonCallback<ResponseModel<ProductReturn>> callback);

    String productReturnQuery = "appServiceProduct/productReturnList";

    /**
     * 退货单详情
     */
    Callback.Cancelable productReturnQueryDetails(String id, Callback.CommonCallback<ResponseModel<ProductReturn>> callback);

    String productReturnQueryDetails = "appServiceProduct/findReturnDetail";

    /**
     * 退货申请
     */
    Callback.Cancelable productReturnApply(ProductReturn condition, Callback.CommonCallback<ResponseModel<String>> callback);

    String productReturnApply = "appServiceProduct/applyReturnProduct";


}
