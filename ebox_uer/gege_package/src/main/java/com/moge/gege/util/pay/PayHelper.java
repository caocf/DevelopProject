/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.moge.gege.util.pay;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import com.android.volley.VolleyError;
import com.moge.gege.R;
import com.moge.gege.model.*;
import com.moge.gege.model.enums.PayResultType;
import com.moge.gege.model.enums.PayType;
import com.moge.gege.network.ErrorCode;
import com.moge.gege.network.request.TradingCreateOrderRequest;
import com.moge.gege.network.request.TradingFinishOrderRequest;
import com.moge.gege.network.request.TradingPayOrderRequest;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.RequestManager;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.DeviceInfoUtil;
import com.moge.gege.util.DialogUtil;
import com.moge.gege.util.PackageUtil;
import com.moge.gege.util.ToastUtil;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class PayHelper
{
    private Activity mContext;
    private PayResultModel mPayResultModel;
    private PayHelperResultLinstener mListener;
    private ArrayList<String> mOrderIdList = new ArrayList<String>();
    private int mPayType;
    private Dialog mProgressDialog;

    public PayHelper(Activity context, PayHelperResultLinstener listener)
    {
        mContext = context;
        mListener = listener;
        mProgressDialog = DialogUtil.createProgressDialog(mContext,
                mContext.getResources().getString(R.string.create_pay_order));
    }

    public boolean createNewOrder(String userCouponId, String cardCouponId,
            String couponId, AddressModel addressModel, String remark,
            ArrayList<TradingBaseModel> goodsList)
    {
        // {"merchants":[{"merchant_id":"53bf946ebea2ff642842c0d3","goods":[{"trading_id":"54084bf2b8a9bd17058b4569","num":1,"price":100,"totle_fee":100}],"totle_fee":100,"totle_num":1}],"totle_fee":100,"totle_num":1,"address_info":""}
        String orderContent = "";

        try
        {
            JSONObject merchantsObject = new JSONObject();

            JSONObject addressObject = new JSONObject();
            addressObject.put("_id", addressModel.get_id());
            addressObject.put("address_type", addressModel.getAddress_type());
            merchantsObject.put("address_info", addressObject);

            merchantsObject.put("device_info", getDeviceInfoObject(mContext));

            JSONArray merchantArray = new JSONArray();
            JSONObject merchantObject = new JSONObject();
            JSONArray goodsArray = new JSONArray();

            int total_num = 0;
            int total_fee = 0;

            for (int i = 0; i < goodsList.size(); i++)
            {
                TradingBaseModel model = (TradingBaseModel) goodsList.get(i);

                if (i == 0)
                {
                    merchantObject.put("merchant_id", model.getAuthor_uid());
                }
                JSONObject goodItemObject = new JSONObject();

                goodItemObject.put("trading_id", model.get_id());
                goodItemObject.put("num", model.getBuyNum());
                goodItemObject.put("price", model.getDiscount_price());
                goodItemObject.put("total_fee",
                        model.getBuyNum() * model.getDiscount_price());
                if (!TextUtils.isEmpty(model.getPromotion_id()))
                {
                    goodItemObject.put("promotion_id", model.getPromotion_id());
                }
                goodsArray.put(goodItemObject);

                total_num += model.getBuyNum();
                total_fee += model.getBuyNum() * model.getDiscount_price();
            }

            merchantObject.put("goods", goodsArray);
            merchantObject.put("total_fee", total_fee);
            merchantObject.put("total_num", total_num);
            merchantObject.put("pay_fee", 0);
            if (!TextUtils.isEmpty(couponId))
            {
                JSONObject couponObject = new JSONObject();
                if (!TextUtils.isEmpty(userCouponId))
                {
                    couponObject.put("user_coupon_id", userCouponId);
                }
                if(!TextUtils.isEmpty(cardCouponId))
                {
                    couponObject.put("card_coupon_id", cardCouponId);
                }
                couponObject.put("coupon_id", couponId);
                merchantObject.put("coupon", couponObject);
            }

            if (!TextUtils.isEmpty(remark))
            {
                merchantObject.put("remark", remark);
            }

            merchantArray.put(merchantObject);

            merchantsObject.put("merchants", merchantArray);
            merchantsObject.put("total_fee", total_fee);
            merchantsObject.put("total_num", total_num);

            orderContent = merchantsObject.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(orderContent))
        {
            ToastUtil.showToastShort(R.string.generate_order_failed);
            return false;
        }

        // show progress dialog
        mProgressDialog.show();
        doTradingCreateOrderRequest(orderContent);
        return true;
    }

    public void payForRechargeOrder(int payType, String orderId, int fee)
    {
        String orderContent = "";

        try
        {
            JSONObject payObject = new JSONObject();
            payObject.put("device_info", getDeviceInfoObject(mContext));
            payObject.put("pay_type", payType);
            payObject.put("total_fee", fee);
            payObject.put("total_num", 1);
            payObject.put("service", "recharge_order_service"); // tag for 充值

            JSONArray orderIdArray = new JSONArray();
            orderIdArray.put(orderId);
            payObject.put("order_ids", orderIdArray);

            orderContent = payObject.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(orderContent))
        {
            ToastUtil.showToastShort(R.string.pay_order_failed);
            return;
        }

        mProgressDialog.show();
        mPayType = payType;
        doTradingPayOrderRequest(orderContent);
    }

    public void payForOneOrder(int payType, OrderModel orderModel)
    {
        String orderContent = "";

        mOrderIdList.clear();
        mOrderIdList.add(orderModel.getOrder_id());

        try
        {
            JSONObject payObject = new JSONObject();
            payObject.put("device_info", getDeviceInfoObject(mContext));
            payObject.put("pay_type", payType);
            payObject.put("total_fee", orderModel.getTotal_fee());
            payObject.put("total_num", orderModel.getTotal_num());
            payObject.put("service", "trading_order_service"); // tag for 购买商品

            JSONArray orderIdArray = new JSONArray();
            orderIdArray.put(orderModel.getOrder_id());
            payObject.put("order_ids", orderIdArray);

            orderContent = payObject.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(orderContent))
        {
            ToastUtil.showToastShort(R.string.pay_order_failed);
            return;
        }

        mProgressDialog.show();
        mPayType = payType;
        doTradingPayOrderRequest(orderContent);
    }

    public void payForMoreOrder(int payType, MerchantListModel merchantListModel)
    {
        // "pay_type":1,
        // "total_fee":10000,
        // "total_num":1,
        // "order_ids":["201409131744318249297081"]

        String orderContent = "";

        try
        {
            JSONObject payObject = new JSONObject();
            payObject.put("device_info", getDeviceInfoObject(mContext));
            payObject.put("pay_type", payType);
            payObject.put("total_fee", merchantListModel.getTotal_fee());
            payObject.put("total_num", merchantListModel.getTotal_num());

            JSONArray orderIdArray = new JSONArray();

            mOrderIdList.clear();
            for (int i = 0; i < merchantListModel.getMerchants().size(); i++)
            {
                MerchantModel merchantModel = merchantListModel.getMerchants()
                        .get(i);
                orderIdArray.put(merchantModel.getOrder_id());
                mOrderIdList.add(merchantModel.getOrder_id());
            }

            payObject.put("order_ids", orderIdArray);

            orderContent = payObject.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(orderContent))
        {
            ToastUtil.showToastShort(R.string.pay_order_failed);
            notifyPayResult(PayResultType.PAY_FAILED);
            mProgressDialog.dismiss();
            return;
        }

        mProgressDialog.show();
        doTradingPayOrderRequest(orderContent);
    }

    public void payForBalanceOrder(String payId, String code)
    {
        // "pay_id":"",
        // "pay_type":1,
        // "result":0,
        // "error_msg":'',
        // "pay_info":{
        // }

        String finishOrderContent = "";

        try
        {
            JSONObject object = new JSONObject();
            object.put("pay_id", payId);
            object.put("pay_type", PayType.Balance_Pay);
            object.put("result",  PayResultType.PAY_SUCCESS);
            object.put("error_msg", "");
            object.put("code", code);

            JSONObject payInfoObject = new JSONObject();
            object.put("pay_info", payInfoObject);

            finishOrderContent = object.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(finishOrderContent))
        {
            ToastUtil.showToastShort(R.string.pay_order_failed);
            return;
        }

        doTradingFinishOrderRequest(payId, finishOrderContent);
    }

    public void finishAliOrderPay(String payId, Result aliResult)
    {
        // "pay_id":"",
        // "pay_type":1,
        // "result":0,
        // "error_msg":'',
        // "pay_info":{
        // }

        String finishOrderContent = "";
        int result = PayResultType.PAY_SUCCESS;

        try
        {
            JSONObject object = new JSONObject();
            object.put("pay_id", payId);
            object.put("pay_type", PayType.Ali_Pay);
            if (aliResult.getResultStatus().equals(Result.SUCCESS)
                    || aliResult.getResultStatus().equals(Result.DEALING))
            {
                result = PayResultType.PAY_SUCCESS;
            }
            else if (aliResult.getResultStatus().equals(Result.CANCEL))
            {
                result = PayResultType.PAY_CANCEL;
            }
            else
            {
                result = PayResultType.PAY_FAILED;
            }
            object.put("result", result);
            object.put("error_msg", aliResult.getMemo());

            JSONObject payInfoObject = new JSONObject();
            payInfoObject.put("resultStatus",
                    Integer.parseInt(aliResult.getResultStatus()));
            payInfoObject.put("memo", aliResult.getMemo());
            payInfoObject.put("result", aliResult.getResult());

            object.put("pay_info", payInfoObject);

            finishOrderContent = object.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(finishOrderContent))
        {
            ToastUtil.showToastShort(R.string.pay_order_failed);
            return;
        }

        RequestManager.addRequest(new TradingFinishOrderRequest(payId, finishOrderContent, null), mContext);

        notifyPayResult(result);
    }

    public void finishWXPayOrder(String payId, PayResp payResp)
    {
        // "pay_id":"",
        // "pay_type":1,
        // "result":0,
        // "error_msg":'',
        // "pay_info":{
        // }

        String finishOrderContent = "";
        int result = PayResultType.PAY_SUCCESS;

        try
        {
            JSONObject object = new JSONObject();
            object.put("pay_id", payId);
            object.put("pay_type", PayType.Weixin_Pay);
            if (payResp.errCode == BaseResp.ErrCode.ERR_OK)
            {
                result = PayResultType.PAY_SUCCESS;
            }
            else if (payResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL)
            {
                result = PayResultType.PAY_CANCEL;
            }
            else
            {
                result = PayResultType.PAY_FAILED;
            }
            object.put("result", result);
            object.put("error_msg", payResp.errStr);

            JSONObject payInfoObject = new JSONObject();
            payInfoObject.put("errCode", payResp.errCode);
            payInfoObject.put("errStr", payResp.errStr);
            payInfoObject.put("transaction", payResp.transaction);
            payInfoObject.put("prepayId", payResp.prepayId);
            payInfoObject.put("returnKey", payResp.returnKey);
            object.put("pay_info", payInfoObject);

            finishOrderContent = object.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(finishOrderContent))
        {
            ToastUtil.showToastShort(R.string.pay_order_failed);
            return;
        }

        RequestManager.addRequest(new TradingFinishOrderRequest(payId, finishOrderContent, null), mContext);

        notifyPayResult(result);
    }

    public void cancelPayOrder(int payType, String payId)
    {
        String cancelOrderContent = "";

        try
        {
            JSONObject object = new JSONObject();
            object.put("pay_id", payId);
            object.put("pay_type", payType);
            object.put("result", PayResultType.PAY_CANCEL);
            object.put("error_msg", "用户中途取消");

            JSONObject payInfoObject = new JSONObject();
            object.put("pay_info", payInfoObject);

            cancelOrderContent = object.toString();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return;
        }

        TradingFinishOrderRequest request = new TradingFinishOrderRequest(
                payId, cancelOrderContent,
                new ResponseEventHandler<RespFinishOrderModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespFinishOrderModel> request,
                            RespFinishOrderModel result)
                    {
                        if(mListener != null)
                        {
                            mListener.onCancelPayOrder(result.getStatus());
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        if(mListener != null)
                        {
                            mListener.onCancelPayOrder(ErrorCode.NETWORK_ERROR);
                        }
                    }

                });
        RequestManager.addRequest(request, mContext);

    }

    private void doTradingCreateOrderRequest(String content)
    {
        TradingCreateOrderRequest request = new TradingCreateOrderRequest(
                content, new ResponseEventHandler<RespMerchantListModel>()
        {
            @Override
            public void onResponseSucess(
                    BaseRequest<RespMerchantListModel> request,
                    RespMerchantListModel result)
            {
                mProgressDialog.dismiss();
                if (result.getStatus() != ErrorCode.SUCCESS)
                {
                    ToastUtil.showToastShort(result.getMsg());
                }

                if (mListener != null)
                {
                    mListener.onCreateOrderResult(result.getStatus(), result.getData());
                }
            }

            @Override
            public void onResponseError(VolleyError error)
            {
                mProgressDialog.dismiss();
                ToastUtil.showToastShort(error.getMessage());
                if (mListener != null)
                {
                    mListener.onCreateOrderResult(ErrorCode.NETWORK_ERROR, null);
                }
            }
        });
        RequestManager.addRequest(request, mContext);
    }

    private void doTradingPayOrderRequest(String content)
    {
        TradingPayOrderRequest request = new TradingPayOrderRequest(content,
                new ResponseEventHandler<RespPayOrderModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespPayOrderModel> request,
                            RespPayOrderModel result)
                    {
                        mProgressDialog.dismiss();

                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            mPayResultModel = result.getData();

                            if (mListener != null)
                            {
                                mListener.onCreatePayOrder(mPayResultModel);
                            }

                            realPayByClient(mPayResultModel);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            notifyPayResult(PayResultType.PAY_FAILED);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        notifyPayResult(PayResultType.PAY_FAILED);
                        mProgressDialog.dismiss();
                    }
                });
        RequestManager.addRequest(request, mContext);
    }

    private void doTradingFinishOrderRequest(String payId, String content)
    {
        TradingFinishOrderRequest request = new TradingFinishOrderRequest(
                payId, content,
                new ResponseEventHandler<RespFinishOrderModel>()
                {
                    @Override
                    public void onResponseSucess(
                            BaseRequest<RespFinishOrderModel> request,
                            RespFinishOrderModel result)
                    {
                        if (result.getStatus() == ErrorCode.SUCCESS)
                        {
                            notifyPayResult(PayResultType.PAY_SUCCESS);
                        }
                        else
                        {
                            ToastUtil.showToastShort(result.getMsg());
                            notifyPayResult(PayResultType.PAY_FAILED);
                        }
                    }

                    @Override
                    public void onResponseError(VolleyError error)
                    {
                        ToastUtil.showToastShort(error.getMessage());
                        notifyPayResult(PayResultType.PAY_FAILED);
                    }

                });
        RequestManager.addRequest(request, mContext);
    }

    public static JSONObject getDeviceInfoObject(Context context)
    {
        JSONObject deviceObject = new JSONObject();
        try
        {
            deviceObject.put("udid", DeviceInfoUtil.getDeviceId(context));
            deviceObject.put("appver", PackageUtil.getVersionName(context));
            deviceObject.put("os", "android");
            deviceObject.put("phonemodel",
                    DeviceInfoUtil.getDeviceName(context));
            deviceObject.put("network", DeviceInfoUtil.getNetType(context));
            deviceObject.put("app_name", "gege");
            deviceObject.put("osver", DeviceInfoUtil.getSysVersion(context));
            return deviceObject;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return deviceObject;
    }

    private void realPayByClient(PayResultModel resultModel)
    {
        switch (resultModel.getPay_type())
        {
            case PayType.Ali_Pay:
                aliPay(resultModel);
                break;
            case PayType.Weixin_Pay:
                weixinPay(resultModel);
                break;
            case PayType.Cod_Pay:
                notifyPayResult(PayResultType.PAY_SUCCESS);
                break;
            default:
                break;
        }
    }

    private void aliPay(final PayResultModel resultModel)
    {
        String orderParams = resultModel.getOrder_info().getParams();
        AliPayHelper.toPayOrder(mContext, this, resultModel.getPay_id(),
                orderParams);
    }

    private void weixinPay(final PayResultModel resultModel)
    {
        PayOrderInfoModel weixinOrder = resultModel.getOrder_info();
        if (!WXPayHelper.toPayOrder(mContext, this, resultModel.getPay_id(),
                weixinOrder, mOrderIdList))
        {
            notifyPayResult(PayResultType.PAY_FAILED);
        }
    }

    public void notifyPayResult(int result)
    {
        if (mListener != null)
        {
            mListener.onPayResult(result, mOrderIdList);
        }
    }

    public static interface PayHelperResultLinstener
    {
        public void onCreateOrderResult(int result, MerchantListModel orderListModel);
        public void onCreatePayOrder(PayResultModel payOrderModel);
        public void onCancelPayOrder(int result);
        public void onPayResult(int result, ArrayList<String> orderList);
    }

}
