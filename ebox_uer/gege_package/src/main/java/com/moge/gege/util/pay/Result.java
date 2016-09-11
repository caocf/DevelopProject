package com.moge.gege.util.pay;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

public class Result
{

    public static final String SUCCESS = "9000";
    public static final String DEALING = "8000";
    public static final String CANCEL = "6001";

    private static final Map<String, String> sResultStatus;

    private String mResult;

    String resultStatus = null;
    String memo = null;
    String result = null;
    boolean isSignOk = false;

    String resultMsg = null;

    public Result(String result)
    {
        this.mResult = result;

        parseResult();
    }

    static
    {
        sResultStatus = new HashMap<String, String>();
        sResultStatus.put("9000", "操作成功");
        sResultStatus.put("4000", "系统异常");
        sResultStatus.put("4001", "数据格式不正确");
        sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
        sResultStatus.put("4004", "该用户已解除绑定");
        sResultStatus.put("4005", "绑定失败或没有绑定");
        sResultStatus.put("4006", "订单支付失败");
        sResultStatus.put("4010", "重新绑定账户");
        sResultStatus.put("6000", "支付服务正在进行升级操作");
        sResultStatus.put("6001", "用户中途取消支付操作");
        sResultStatus.put("7001", "网页支付失败");
        sResultStatus.put("8000", "支付结果确认中，请稍后查看订单状态");
    }

    public String getResultStatus()
    {
        return resultStatus;
    }

    public String getMemo()
    {
        return memo;
    }

    public String getResult()
    {
        return result;
    }

    public String getErrorMsg()
    {
        return resultMsg;
    }

    private void parseResult()
    {
        try
        {
            String src = mResult.replace("{", "");
            src = src.replace("}", "");
            resultStatus = getContent(src, "resultStatus=", ";memo");
            if (sResultStatus.containsKey(resultStatus))
            {
                resultMsg = sResultStatus.get(resultStatus);
            }
            else
            {
                resultMsg = "其他错误";
            }
            resultMsg += "(" + resultStatus + ")";

            memo = getContent(src, "memo=", ";result");
            result = getContent(src, "result=", null);
            isSignOk = checkSign(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean checkSign(String result)
    {
        boolean retVal = false;
        try
        {
            JSONObject json = string2JSON(result, "&");

            int pos = result.indexOf("&sign_type=");
            String signContent = result.substring(0, pos);

            String signType = json.getString("sign_type");
            signType = signType.replace("\"", "");

            String sign = json.getString("sign");
            sign = sign.replace("\"", "");

            if (signType.equalsIgnoreCase("RSA"))
            {
//                retVal = Rsa.doCheck(signContent, sign, Keys.PUBLIC);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i("Result", "Exception =" + e);
        }
        Log.i("Result", "checkSign =" + retVal);
        return retVal;
    }

    public JSONObject string2JSON(String src, String split)
    {
        JSONObject json = new JSONObject();

        try
        {
            String[] arr = src.split(split);
            for (int i = 0; i < arr.length; i++)
            {
                String[] arrKey = arr[i].split("=");
                json.put(arrKey[0], arr[i].substring(arrKey[0].length() + 1));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return json;
    }

    private String getContent(String src, String startTag, String endTag)
    {
        String content = src;
        int start = src.indexOf(startTag);
        start += startTag.length();

        try
        {
            if (endTag != null)
            {
                int end = src.indexOf(endTag);
                content = src.substring(start, end);
            }
            else
            {
                content = src.substring(start);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return content;
    }
}