package com.xhl.bqlh.business.doman;

import android.text.TextUtils;

import com.xhl.bqlh.business.Api.ApiControl;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.Base.ResponseModel;
import com.xhl.bqlh.business.Model.OrderInitModel;
import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.Model.OrderSaveModel;
import com.xhl.bqlh.business.Model.ProductReturn;
import com.xhl.bqlh.business.Model.ProductReturnDetail;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.doman.callback.ContextValue;
import com.xhl.bqlh.business.utils.ToastUtil;
import com.xhl.bqlh.business.view.base.Common.DefaultCallback;
import com.xhl.bqlh.business.view.ui.activity.ConfirmProductActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/4/25.
 */
public class ConfirmOrderHelper extends BaseValue {

    //支付类型
    public static final int TYPE_PAY_TYPE = 0;

    //订单类型
    public static final int TYPE_ORDER_TYPE = 1;

    //订单商品数据
    public static final int TYPE_ORDER_DATA = 2;
    //赠品数据
    public static final int TYPE_GIFT_DATA = 10;

    //订单金额
    public static final int TYPE_ORDER_MONEY = 3;

    //订单支付金额
    public static final int TYPE_ORDER_PAY_MONEY = 4;

    //零售商店铺id
    public static final int TYPE_SHOP_ID = 5;

    //订单id，订单退商品的时候使用
    public static final int TYPE_SHOP_ORDER_ID = 9;

    //订单提交成功
    public static final int TYPE_RES_SAVE_ORDER_FINISH = 6;
    //装车单创建新增
    public static final int TYPE_RES_CREATE_CAR_ADD_FINISH = 7;
    //装车单更新新增
    public static final int TYPE_RES_UPDATE_CAR_ADD_FINISH = 8;
    //退货完成
    public static final int TYPE_RES_RETURN_FINISH = 11;


    public ConfirmOrderHelper(ContextValue value) {
        super(value);
    }

    public void confirmOrder() {

        int orderType = (int) mValue.getValue(TYPE_ORDER_TYPE);
        if (orderType == ConfirmProductActivity.TYPE_CAR_CREATE_ADD) {
            //车销新增
            showValue(TYPE_RES_CREATE_CAR_ADD_FINISH, null);
        } else if (orderType == ConfirmProductActivity.TYPE_CAR_UPDATE_ADD) {
            //装车单更新
            showValue(TYPE_RES_UPDATE_CAR_ADD_FINISH, null);
        } else {
            ArrayList<ShopCarModel> data = (ArrayList<ShopCarModel>) mValue.getValue(TYPE_ORDER_DATA);
            orderInit(data);
        }
    }

    //退货申请
    public void confirmReturn(String remark) {
        returnProduct(remark);
    }

    //礼品数据
    private String giftJson(ArrayList<ShopCarModel> product) {
        JSONArray array = new JSONArray();
        for (ShopCarModel pro : product) {
            if (pro.productType == ProductType.PRODUCT_GIFT && pro.curNum > 0) {
                JSONObject object = new JSONObject();
                try {
                    object.put("id", pro.productId);
                    object.put("giftNum", pro.curNum);
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return array.toString();
    }

    //保存订单
    private void saveOrder(OrderInitModel order) {
        //创建订单数据
        String attrJson = attrJson(order.getOrderList());

        float realPay = (float) mValue.getValue(TYPE_ORDER_MONEY);

        float realPayMoney = (float) mValue.getValue(TYPE_ORDER_PAY_MONEY);

        String shopId = (String) mValue.getValue(TYPE_SHOP_ID);
        //礼品
        String gift = giftJson((ArrayList<ShopCarModel>) mValue.getValue(TYPE_ORDER_DATA));

        ApiControl.getApi().orderSave(attrJson, gift, String.valueOf(realPay), String.valueOf(realPayMoney), shopId, new Callback.CommonCallback<ResponseModel<OrderSaveModel>>() {
            @Override
            public void onSuccess(ResponseModel<OrderSaveModel> result) {
                if (result.isSuccess()) {
                    showValue(TYPE_RES_SAVE_ORDER_FINISH, result.getObj());
                } else {
                    toastShow(result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toastShow(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialogHide();
            }
        });

    }

    //订单初始化
    private void orderInit(List<ShopCarModel> datas) {
        progressShow("提交订单中");

        String orderData = initOrder(datas);
        int orderType = (int) mValue.getValue(TYPE_ORDER_TYPE);
        String shopId = (String) mValue.getValue(TYPE_SHOP_ID);

        ApiControl.getApi().orderInit(orderData, String.valueOf(orderType), shopId, new Callback.CommonCallback<ResponseModel<OrderInitModel>>() {
            @Override
            public void onSuccess(ResponseModel<OrderInitModel> result) {
                if (result.isSuccess()) {
                    saveOrder(result.getObj());
                } else {
                    toastShow(result.getMessage());
                    dialogHide();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toastShow(ex.getMessage());
                dialogHide();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                dialogHide();
            }

            @Override
            public void onFinished() {

            }
        });

    }

    private String initOrder(List<ShopCarModel> orders) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (ShopCarModel order : orders) {
                if (order.curNum <= 0 || order.productType == ProductType.PRODUCT_GIFT) {
                    continue;
                }
                JSONObject itemJson = new JSONObject();
                itemJson.put("shoppingCartId", "");
                itemJson.put("sellerId", order.companyId);
                itemJson.put("productId", order.productId);
                itemJson.put("quantity", order.curNum);
                itemJson.put("productType", "");
                //添加到数组中
                jsonArray.put(itemJson);
            }
            jsonObject.putOpt("results", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    private String attrJson(List<OrderModel> datas) {
        return saveOrderToJson(datas).toString();
    }

    //创建生成一组订单数据
    private JSONArray saveOrderToJson(List<OrderModel> orders) {
        JSONArray ordersJson = new JSONArray();
        for (OrderModel order : orders) {
            JSONObject oneOrder = createOneOrder(order);
            ordersJson.put(oneOrder);
        }
        return ordersJson;
    }

    private String getAttr(String attr) {
        if (TextUtils.isEmpty(attr)) {
            return "";
        }
        return attr;
    }

    //创建一个订单数据
    private JSONObject createOneOrder(OrderModel order) {
        JSONObject orderJson = new JSONObject();
        try {
            orderJson.put("companyId", getAttr(order.getCompanyId()));
            orderJson.put("couponsMoney", getAttr(order.getCouponsMoney().toString()));//优惠金额
            orderJson.put("payType", "2");//1 在线支付
            orderJson.put("liableMail", getAttr(order.getLiableMail()));//责任人邮箱
            orderJson.put("orderType", getAttr(order.getOrderType()));//1：普通订单；2：团购订单；3：消费者促销订单；4：渠道促销订单；5订货会订单；6：积分订单）
            orderJson.put("couId", getAttr(order.getCouId()));//优惠券主题id
            orderJson.put("productType", getAttr(order.getProductType() + ""));
            orderJson.put("orderProperty", "");

            //创建订单商品数据
            JSONArray jsonArray = new JSONArray();
           /* List<OrderDetail> details = order.getOrderDetailList();
            for (OrderDetail de : details) {
                JSONObject oneProduct = createOneProduct(de);
                jsonArray.put(oneProduct);
            }*/
            ArrayList<ShopCarModel> data = (ArrayList<ShopCarModel>) mValue.getValue(TYPE_ORDER_DATA);
            for (ShopCarModel shop : data) {
                if (shop.productType != ProductType.PRODUCT_GIFT) {
                    jsonArray.put(createOneProduct(shop));
                }
            }

            orderJson.put("attrOrderDetail", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderJson;
    }

    //创建一个商品数据
    private JSONObject createOneProduct(ShopCarModel car) {
        JSONObject itemJson = new JSONObject();
        try {
            itemJson.put("shoppingCartId", "");
            itemJson.put("taxprice", "");
            itemJson.put("goodId", car.productId);
            itemJson.put("productName", car.productName);
            itemJson.put("num", car.curNum);
            itemJson.put("unitPrice", car.getProductPrice());
            itemJson.put("discount", car.getDiscount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemJson;
    }


    //申请退货
    private void returnProduct(String remark) {
        progressShow("提交退货单中");

        ArrayList<ShopCarModel> data = (ArrayList<ShopCarModel>) mValue.getValue(TYPE_ORDER_DATA);

        //商品数据
        List<ProductReturnDetail> details = new ArrayList<>();
        for (ShopCarModel car : data) {
            if (car.curNum == 0) {
                continue;
            }
            ProductReturnDetail detail = CarToReturnDetail(car);
            details.add(detail);
        }
        if (details.size() == 0) {
            ToastUtil.showToastShort("请输入退货商品数量");
            return;
        }
        //退货总额
        float realPay = (float) mValue.getValue(TYPE_ORDER_MONEY);

        ProductReturn productReturn = new ProductReturn();
        productReturn.setRetailer((String) mValue.getValue(TYPE_SHOP_ID));
        productReturn.setStoreOrderCode((String) mValue.getValue(TYPE_SHOP_ORDER_ID));
        productReturn.setReturnDesc(remark);
        productReturn.setApplyReturnMoney(realPay);
        productReturn.setReturnDetailList(details);

        ApiControl.getApi().productReturnApply(productReturn, new DefaultCallback<ResponseModel<String>>() {
            @Override
            public void success(ResponseModel<String> result) {
                ToastUtil.showToastLong(result.getObj());
                showValue(TYPE_RES_RETURN_FINISH, null);
            }

            @Override
            public void finish() {
                dialogHide();
            }
        });
    }

    private ProductReturnDetail CarToReturnDetail(ShopCarModel car) {
        ProductReturnDetail detail = new ProductReturnDetail();
        detail.setNum(String.valueOf(car.curNum));
        detail.setApplyReturnPrice(String.valueOf(car.getProductPrice()));
        detail.setProductId(car.productId);
        detail.setProductName(car.productName);
        return detail;
    }


}
