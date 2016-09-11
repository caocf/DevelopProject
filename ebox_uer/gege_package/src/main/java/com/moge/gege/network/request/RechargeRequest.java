package com.moge.gege.network.request;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.moge.gege.AppApplication;
import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.RespPayOrderModel;
import com.moge.gege.model.RespRechargeResultModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;
import com.moge.gege.util.pay.PayHelper;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * recharge
 * recharge_type 1 #充值类型 1:在线充值， 2:充值卡充值
 */
public class RechargeRequest extends BaseRequest<RespRechargeResultModel> {

    public RechargeRequest(int fee, String cardCode, String cardPwd,
                           ResponseEventHandler<RespRechargeResultModel> listener) {
        super(Method.POST, getRequestUrl(), getRequestParam(fee, cardCode, cardPwd), RespRechargeResultModel.class,
                listener);
    }

    private static String getRequestUrl() {
        return NetworkConfig.payAddress + "/v1/balance/recharge";
    }

    private static String getRequestParam(int fee, String cardCode, String cardPwd) {

        String bodyParams = "";

        try {
            JSONObject bodyObject = new JSONObject();
            if(TextUtils.isEmpty(cardCode))
            {
                bodyObject.put("recharge_type", 1);
                bodyObject.put("fee", fee);
                bodyObject.put("remark", "");
            }
            else
            {
                bodyObject.put("recharge_type", 2);
                bodyObject.put("card_no", cardCode);
                bodyObject.put("passwd", cardPwd);
            }

            bodyObject.put("device_info", PayHelper.getDeviceInfoObject(AppApplication.mGlobalContext));
            bodyParams = bodyObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bodyParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        headers.put("Content-Type", "application/json");
        return headers;
    }

}
