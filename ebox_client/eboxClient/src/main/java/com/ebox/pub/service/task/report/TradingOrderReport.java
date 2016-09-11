package com.ebox.pub.service.task.report;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.mall.warehouse.model.RespFinishOrderModel;
import com.ebox.mall.warehouse.network.ErrorCode;
import com.ebox.mall.warehouse.network.request.TradingFinishOrderRequest;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.MD5Util;
import com.ebox.ums.database.payRst.PayRstOp;
import com.ebox.ums.database.payRst.PayRstTable;
import com.ebox.ums.database.payRst.TradingFinishOrderData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Android on 2015/9/1.
 */
public class TradingOrderReport implements IReport {


    private TradingFinishOrderData TradingFinishOrder = null;

    public TradingOrderReport() {
    }

    private void readStatus() {
        TradingFinishOrder = PayRstOp.getTradingFinishOrderByState(PayRstTable.STATE_0);
    }

    @Override
    public void create() {
        readStatus();
    }

    @Override
    public void report() {
        if (TradingFinishOrder != null) {
            finishOrderPay(TradingFinishOrder);
        }
    }

    @Override
    public int reportType() {
        return Type_Trading;
    }

    private void doTradingFinishOrderRequest(final String PayId, String content) {
        TradingFinishOrderRequest request = new TradingFinishOrderRequest(
                PayId, content,
                new ResponseEventHandler<RespFinishOrderModel>() {
                    @Override
                    public void onResponseSuccess(RespFinishOrderModel result)
                    {
                        LogUtil.d("pay msg :" + result.getMsg());
                        if (result.getStatus() == ErrorCode.SUCCESS ||
                                result.getStatus() == ErrorCode.PAY_SUCCESS_BEFOR ||
                                result.getStatus() == ErrorCode.PAY_FAILED_BEFOR)
                            PayRstOp.updateTradingFinishOrderState(PayRstTable.STATE_1, PayId);
                    }

                    @Override
                    public void onResponseError(VolleyError error) {

                        LogUtil.d(error.getMessage());
                    }

                });
        RequestManager.addRequest(request, this);
    }


    private void finishOrderPay(TradingFinishOrderData tradingData) {

        String finishOrderContent = "";

        try {
            JSONObject object = new JSONObject();
            object.put("pay_id", tradingData.getPayId());
            object.put("pay_type", tradingData.getPayType());
            object.put("result", tradingData.getResult());
            object.put("error_msg", tradingData.getError_msg());
            object.put("mobile", tradingData.getMobile());
            JSONObject payInfoObject = new JSONObject();
            payInfoObject.put("rstCode", tradingData.getRstCode());
            payInfoObject.put("amount", tradingData.getAmount());
            payInfoObject.put("appendField", tradingData.getAppendField());
            payInfoObject.put("authNo", tradingData.getAuthNo());
            payInfoObject.put("bankCode", tradingData.getBankcode());
            payInfoObject.put("bath", tradingData.getBatch());
            payInfoObject.put("cardNo", tradingData.getCardNo());
            payInfoObject.put("exp", tradingData.getExp());
            payInfoObject.put("mchtId", tradingData.getMchtId());
            payInfoObject.put("printInfo", tradingData.getPrintInfo());
            payInfoObject.put("reference", tradingData.getReference());
            payInfoObject.put("rspchin", tradingData.getRspChin());
            payInfoObject.put("settleDate", tradingData.getSettleDate());
            payInfoObject.put("termId", tradingData.getTermId());
            payInfoObject.put("trace", tradingData.getTrace());
            payInfoObject.put("transDate", tradingData.getTransDate());
            payInfoObject.put("transTime", tradingData.getTransTime());
            payInfoObject.put("noncestr", tradingData.getNoncestr());
            payInfoObject.put("timestamp", tradingData.getTimestamp());
            payInfoObject.put("sign", signPay(tradingData));


            object.put("pay_info", payInfoObject);

            finishOrderContent = object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(finishOrderContent)) {
            return;
        }

        doTradingFinishOrderRequest(tradingData.getPayId(), finishOrderContent);
    }

    private String signPay(TradingFinishOrderData tradingData) {
        String sign = "";
        sign += "amount=";
        sign += tradingData.getAmount();
        sign += "&appendField=";
        sign += tradingData.getAppendField();
        sign += "&authNo=";
        sign += tradingData.getAuthNo();
        sign += "&bankCode=";
        sign += tradingData.getBankcode();
        sign += "&bath=";
        sign += tradingData.getBatch();
        sign += "&cardNo=";
        sign += tradingData.getCardNo();
        sign += "&exp=";
        sign += tradingData.getExp();
        sign += "&key=";
        sign += GlobalField.Umskey;
        sign += "&mchtId=";
        sign += tradingData.getMchtId();
        sign += "&noncestr=";
        sign += tradingData.getNoncestr();
        sign += "&pay_id=";
        sign += tradingData.getPayId();
        sign += "&printInfo=";
        sign += tradingData.getPrintInfo();
        sign += "&reference=";
        sign += tradingData.getReference();
        sign += "&rspchin=";
        sign += tradingData.getRspChin();
        sign += "&rstCode=";
        sign += tradingData.getRstCode();
        sign += "&settleDate=";
        sign += tradingData.getSettleDate();
        sign += "&termId=";
        sign += tradingData.getTermId();
        sign += "&timestamp=";
        sign += tradingData.getTimestamp();
        sign += "&trace=";
        sign += tradingData.getTrace();
        sign += "&transDate=";
        sign += tradingData.getTransDate();
        sign += "&transTime=";
        sign += tradingData.getTransTime();


//		LogUtil.d("签名MD5 前："+ sign);
        sign = MD5Util.getMD5String(sign);

        LogUtil.d(sign);
        return sign;
    }


}
