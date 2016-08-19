package com.xhl.bqlh.business.Api;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xhl.bqlh.business.AppConfig.NetWorkConfig;
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
import com.xhl.xhl_library.utils.TimeUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Sum on 16/4/8.
 */
public class ApiImpl extends BaseApi implements Api {

    /**
     * 用户登陆接口
     */
    @Override
    public Callback.Cancelable userLogin(String loginName, String password, Callback.CommonCallback<ResponseModel<UserInfo>> callback) {
        String url = NetWorkConfig.generalHost + userLogin;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("loginName", loginName);
        params.addBodyParameter("passWord", password);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    /**
     * 用户退出接口
     */
    @Override
    public Callback.Cancelable userQuit(Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + userQuit;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 获取验证码
     */
    @Override
    public Callback.Cancelable userGetCode(String userName, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + userGetCode;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("userName", userName);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 验证验证码
     */
    @Override
    public Callback.Cancelable userVerifyCode(String userName, String verifyCode, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + userVerifyCode;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("verifyCode", verifyCode);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 修改密码
     */
    @Override
    public Callback.Cancelable userUpdatePwd(String userName, String userPassword, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + userUpdatePwd;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("userName", userName);
        params.addBodyParameter("userPassword", userPassword);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 更新用户头像
     */
    @Override
    public Callback.Cancelable userUpdateImage(String face, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + userUpdateImage;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("headImage", face);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 获取每天的任务数据
     */
    @Override
    public Callback.Cancelable pullTask(Callback.CommonCallback<ResponseModel<TaskModel>> callback) {
        String url = NetWorkConfig.generalHost + pullTask;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 获取本周的签到数据
     */
    @Override
    public Callback.Cancelable pullTaskRecord(Callback.CommonCallback<ResponseModel<TaskModel>> callback) {
        String url = NetWorkConfig.generalHost + pullTaskRecord;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 查询全部经销商会员
     */
    @Override
    public Callback.Cancelable pullTaskMember(Callback.CommonCallback<ResponseModel<Member>> callback) {
        String url = NetWorkConfig.generalHost + pullTaskMember;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable storeCategory(String retailerId, Callback.CommonCallback<ResponseModel<CategoryItemModel>> callback) {
        String url = NetWorkConfig.generalHost + storeCategory;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("retailerId", retailerId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable storeBrand(String categoryId, Callback.CommonCallback<ResponseModel<ProductBrandModel>> callback) {
        String url = NetWorkConfig.generalHost + storeBrand;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("categoryId", categoryId);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable storeProduct(int pageSize, int pageNum, String shopId, String categoryId, String brandId, String productName, String sku, Callback.CommonCallback<ResponseModel<ProductModel>> callback) {
        String url = NetWorkConfig.generalHost + storeProduct;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("retailerId", shopId);
        params.addBodyParameter("categoryId", categoryId);
        params.addBodyParameter("brandId", brandId);
        params.addBodyParameter("productName", productName);
        params.addBodyParameter("SKU", sku);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    /**
     * 我的仓库查看申请
     */
    @Override
    public Callback.Cancelable storeCarQueryApply(int pageSize, int pageNum, String shopId, Callback.CommonCallback<ResponseModel<ApplyModel>> callback) {
        String url = NetWorkConfig.generalHost + storeCarQueryApply;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        params.addBodyParameter("retailerId", shopId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable storeCarReturnApply(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<ApplyModel>> callback) {
        String url = NetWorkConfig.generalHost + storeCarReturnApply;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable storeCarConfirmDelete(String products, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + storeCarConfirmDelete;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("products", products);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 我的仓库确认申请
     */
    @Override
    public Callback.Cancelable storeCarConfirmApply(String logId, String products, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + storeCarConfirmApply;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("products", products);
        params.addBodyParameter("logId", logId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 装车单查询
     */
    @Override
    public Callback.Cancelable storeApplyQuery(Callback.CommonCallback<ResponseModel<ApplyModel>> callback) {
        String url = NetWorkConfig.generalHost + storeApplyQuery;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(0));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(1000));

        //前一天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, -1);
        params.addBodyParameter("timeStart", TimeUtil.getNormalStringDate(instance.getTime()) + " 00:00:00");
        params.addBodyParameter("timeEnd", TimeUtil.currentTime());
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 装车单添加
     */
    @Override
    public Callback.Cancelable storeApplyAdd(String products, String orderIds, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + storeApplyAdd;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("products", products);
        params.addBodyParameter("salseOrderIds", orderIds);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 装车单更新数量
     * type:1 更新
     * type:3 删除
     */
    @Override
    public Callback.Cancelable storeApplyUpdate(String type, String data, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + storeApplyUpdate;
        RequestParams params = new RequestParams(url);
        if (type.equals("1")) {
            params.addBodyParameter("products", data);
        } else if (type.equals("3")) {
            params.addBodyParameter("id", data);
        }
        params.addBodyParameter("type", type);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable orderInit(String strPar, String orderType, String retailerId, Callback.CommonCallback<ResponseModel<OrderInitModel>> callback) {
        String url = NetWorkConfig.generalHost + orderInit;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("strPar", strPar);
        params.addBodyParameter("orderType", orderType);
        params.addBodyParameter("retailerId", retailerId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 保存订单
     */
    @Override
    public Callback.Cancelable orderSave(String attrJsonStr, String giftJsonStr, String realMoney, String realPayMoney, String retailerId, Callback.CommonCallback<ResponseModel<OrderSaveModel>> callback) {
        String url = NetWorkConfig.generalHost + orderSave;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("attrJsonStr", attrJsonStr);
        params.addBodyParameter("giftJsonStr", giftJsonStr);
        params.addBodyParameter("realMoney", realMoney);
        params.addBodyParameter("realPayMoney", realPayMoney);
        params.addBodyParameter("retailerId", retailerId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 订单详情
     */
    @Override
    public Callback.Cancelable orderInfo(String storeOrderCode, Callback.CommonCallback<ResponseModel<OrderModel>> callback) {
        String url = NetWorkConfig.generalHost + orderInfo;

        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", storeOrderCode);
        addExtendParams(params);

        return x.http().post(params, callback);
    }

    /**
     * 订单搜索
     *
     * @param condition
     * @param callback
     * @return
     */
    @Override
    public Callback.Cancelable orderQuery(int pageSize, int pageNum, OrderQueryCondition condition, Callback.CommonCallback<ResponseModel<OrderModel>> callback) {
        String url = NetWorkConfig.generalHost + orderQuery;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        //开始时间
        if (!TextUtils.isEmpty(condition.startTime)) {
            params.addBodyParameter("orderTimeStart", condition.startTime + " 00:00:00");
        }
        //结束时间
        if (!TextUtils.isEmpty(condition.endTime)) {
            params.addBodyParameter("orderTimeEnd", condition.endTime + " 23:59:59");
        }
        //订单类型
        int orderType = condition.orderType;
        params.addBodyParameter("orderType", orderType == 0 ? "" : String.valueOf(orderType));
        //订单状态
        int orderState = condition.orderState;
        params.addBodyParameter("distributionStatus", orderState == 0 ? "" : String.valueOf(orderState));
        //赊账
        params.addBodyParameter("creditStatus", condition.creditStatus);
        //店铺id
        params.addBodyParameter("retailerId", condition.shopId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 统计订单商品
     */
    @Override
    public Callback.Cancelable orderStatistics(ProductQueryCondition condition, Callback.CommonCallback<ResponseModel<StatisticsModel>> callback) {
        String url = NetWorkConfig.generalHost + orderStatistics;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("beginTime", condition.startTime);
        params.addBodyParameter("endTime", condition.endTime);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 赊账统计店铺
     *
     * @param condition
     * @param callback
     */
    @Override
    public Callback.Cancelable orderAccountStatistics(ProductQueryCondition condition, Callback.CommonCallback<ResponseModel<StatisticsModel>> callback) {
        String url = NetWorkConfig.generalHost + orderAccountStatistics;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 代发货的全部订单加详情
     */
    @Override
    public Callback.Cancelable orderQueryDetail(Callback.CommonCallback<ResponseModel<OrderModel>> callback) {
        String url = NetWorkConfig.generalHost + orderQueryDetail;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 订单还款
     */
    @Override
    public Callback.Cancelable orderRepay(String orderCode, String storeOrderCode, String repayment, String arrears, String retailerId, String remark, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + orderRepay;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("orderCode", orderCode);
        params.addBodyParameter("storeOrderCode", storeOrderCode);
        params.addBodyParameter("repayment", repayment);
        params.addBodyParameter("arrears", arrears);
        params.addBodyParameter("retailerId", retailerId);
        params.addBodyParameter("remark", remark);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 订单还款记录
     */
    @Override
    public Callback.Cancelable orderClearRecord(int pageSize, int pageNum, String storeOrderCode, Callback.CommonCallback<ResponseModel<OrderClearModel>> callback) {
        String url = NetWorkConfig.generalHost + orderClearRecord;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", storeOrderCode);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 订单发货
     */
    @Override
    public Callback.Cancelable orderSend(String storeOrderCode, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + orderSend;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("storeOrderCode", storeOrderCode);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable shopCheckIn(ShopSignModel condition, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url = NetWorkConfig.generalHost + shopCheckIn;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("retailerId", condition.retailerId);
        params.addBodyParameter("checkType", String.valueOf(condition.checkType));
        params.addBodyParameter("img1", condition.img1);
        params.addBodyParameter("img2", condition.img2);
        params.addBodyParameter("img3", condition.img3);
        params.addBodyParameter("chinkInLocation", condition.chinkInLocation);
        params.addBodyParameter("distance", condition.distance);
        params.addBodyParameter("remark", condition.remark);
        params.addBodyParameter("coordinateX", String.valueOf(condition.coordinateX));
        params.addBodyParameter("coordinateY", String.valueOf(condition.coordinateY));
        params.addBodyParameter("enterPoint", condition.enterPoint ? "1" : "2");//1正常：2：异常

        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 店铺赊账订单数量
     */
    @Override
    public Callback.Cancelable shopAccountOrder(String shopId, Callback.CommonCallback<ResponseModel<Integer>> callback) {
        String url = NetWorkConfig.generalHost + shopAccountOrder;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("retailerId", shopId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable shopClose(String shopId, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + shopClose;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("retailerId", shopId);
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

    /**
     * 支付二维码
     */
    @Override
    public Callback.Cancelable payImage(int type, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + payImage;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("paycodetype", String.valueOf(type));
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 考勤规则
     */
    @Override
    public Callback.Cancelable signRule(Callback.CommonCallback<ResponseModel<SignConfigModel>> callback) {
        String url = NetWorkConfig.generalHost + signRule;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 考勤记录
     */
    @Override
    public Callback.Cancelable signRecord(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<SignRecordModel>> callback) {
        String url = NetWorkConfig.generalHost + signRecord;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 一个月的记录
     */
    @Override
    public Callback.Cancelable signRecordTable(Callback.CommonCallback<ResponseModel<SignRecordModel>> callback) {
        String url = NetWorkConfig.generalHost + signRecordTable;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 添加考勤
     */
    @Override
    public Callback.Cancelable sign(SignRecordModel signRecord, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + sign;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("type", String.valueOf(signRecord.getType()));

        params.addBodyParameter("workPlace", signRecord.getWorkPlace());
        params.addBodyParameter("workCoordinateX", signRecord.getWorkCoordinateX());
        params.addBodyParameter("workCoordinateY", signRecord.getWorkCoordinateY());

        params.addBodyParameter("dutyPlace", signRecord.getDutyPlace());
        params.addBodyParameter("dutyCoordinateX", signRecord.getDutyCoordinateX());
        params.addBodyParameter("dutyCoordinateY", signRecord.getDutyCoordinateY());

        params.addBodyParameter("workOnTime", signRecord.getWorkOnTime());
        params.addBodyParameter("dutyUpTime", signRecord.getDutyUpTime());
        //是否进入签到范围
        params.addBodyParameter("enterPoint", signRecord.enterPoint ? "1" : "2");

        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 添加一个陈列展示
     *
     * @param addOne
     * @param callback
     */
    @Override
    public Callback.Cancelable shopDisplayAdd(ShopDisplayModel addOne, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + shopDisplayAdd;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("img", addOne.getImg());
        params.addBodyParameter("retailerId", addOne.shopId);
        params.addBodyParameter("remark", addOne.getRemark());
        params.addBodyParameter("address", addOne.getAddress());
        params.addBodyParameter("coordinateX", String.valueOf(addOne.getCoordinateX()));
        params.addBodyParameter("coordinateY", String.valueOf(addOne.getCoordinateY()));
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 查询陈列展示
     */
    @Override
    public Callback.Cancelable shopDisplayQuery(String shopId, int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<ShopDisplayModel>> callback) {
        String url = NetWorkConfig.generalHost + shopDisplayQuery;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        params.addBodyParameter("retailerId", shopId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable giftQuery(Callback.CommonCallback<ResponseModel<GiftModel>> callback) {
        String url = NetWorkConfig.generalHost + giftQuery;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable productReturnQuery(int pageSize, int pageNum, String type, Callback.CommonCallback<ResponseModel<ProductReturn>> callback) {
        String url = NetWorkConfig.generalHost + productReturnQuery;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        params.addBodyParameter("queryState", type);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable productReturnQueryDetails(String returnId, Callback.CommonCallback<ResponseModel<ProductReturn>> callback) {
        String url = NetWorkConfig.generalHost + productReturnQueryDetails;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("returnId", returnId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable productReturnApply(ProductReturn condition, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + productReturnApply;
        RequestParams params = new RequestParams(url);
        List<ProductReturn> res = new ArrayList<>();
        res.add(condition);
        params.addBodyParameter("returnJson", new Gson().toJson(res));
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable registerBusinessType(Callback.CommonCallback<ResponseModel<BusinessTypeModel>> callback) {
        String url = NetWorkConfig.generalHost + registerBusinessType;
        RequestParams params = new RequestParams(url);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 零售商区域
     */
    @Override
    public Callback.Cancelable registerShopAreaId(RegisterModel register, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + registerShopAreaId;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("provinces", register.province);
        params.addBodyParameter("city", register.city);
        params.addBodyParameter("county", register.district);

        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable registerRetailer(RegisterModel data, Callback.CommonCallback<ResponseModel<Object>> callback) {
        String url;
        if (!TextUtils.isEmpty(data.uid)) {
            url = NetWorkConfig.generalHost + registerRetailerUpdate;
        } else {
            url = NetWorkConfig.generalHost + registerRetailer;
        }
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("uid", data.uid);
        params.addBodyParameter("companyName", data.companyName);
        params.addBodyParameter("companyAddress", data.companyAddress);
        params.addBodyParameter("areaId", data.areaId);
        params.addBodyParameter("area", data.location);
        params.addBodyParameter("address", data.address);
        params.addBodyParameter("liableName", data.liableName);
        params.addBodyParameter("liablePhone", data.liablePhone);
        params.addBodyParameter("line", data.line);
        params.addBodyParameter("companyTypeId", data.companyTypeId);
        params.addBodyParameter("loginUserName", data.loginUserName);
        params.addBodyParameter("businessLicence", data.businessLicence);
        params.addBodyParameter("retailerImg", data.retailerImg);
        params.addBodyParameter("businessLicenceId", data.businessLicenceId);
        params.addBodyParameter("password", data.password);

        params.addBodyParameter("coordinateX", String.valueOf(data.coordinateX));
        params.addBodyParameter("coordinateY", String.valueOf(data.coordinateY));
        //修改零售商信息
        if (data.fixInfo) {
            params.addBodyParameter("shstate", "0");
            params.addBodyParameter("shstateType", "2");//修改零售商
        } else {
            params.addBodyParameter("shstateType", "1");//注册
        }


        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 查看注册零售商
     */
    @Override
    public Callback.Cancelable registerQueryRetailer(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<ShopApplyModel>> callback) {
        String url = NetWorkConfig.generalHost + registerQueryRetailer;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 查看零售商
     */
    @Override
    public Callback.Cancelable registerQueryRetailerById(String shopId, Callback.CommonCallback<ResponseModel<ShopApplyModel>> callback) {
        String url = NetWorkConfig.generalHost + registerQueryRetailerById;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("retailerId", shopId);
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    /**
     * 查看提交的好友零售商
     */
    @Override
    public Callback.Cancelable registerQueryRetailerFriends(int pageSize, int pageNum, Callback.CommonCallback<ResponseModel<ShopFriendsModel>> callback) {
        String url = NetWorkConfig.generalHost + registerQueryRetailerFriends;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter(PAGE_NUM, String.valueOf(pageNum));
        params.addBodyParameter(PAGE_SIZE, String.valueOf(pageSize));
        addExtendParams(params);
        return x.http().post(params, callback);
    }

    @Override
    public Callback.Cancelable registerAddRetailer(String shop, Callback.CommonCallback<ResponseModel<String>> callback) {
        String url = NetWorkConfig.generalHost + registerAddRetailer;
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("retailerId", shop);
        addExtendParams(params);
        return x.http().post(params, callback);
    }
}
