package com.moge.ebox.phone.bettle.network.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.tools.StringUtils;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.bettle.utils.JsonSerializeUtil;
import com.moge.ebox.phone.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import tools.Logger;

public class ApiClient {
    public final static String message_error = "服务器连接有问题";

    //客户端类型
    public static final int android_id = 1;
    public static final int ios_id = 2;

    public static int code = -400;

    private static void saveCache(EboxApplication appContext, String key,
                                  Serializable entity) {
        appContext.saveObject(entity, key);
    }

    public interface ClientCallback {
        abstract void onSuccess(JSONArray data, int code);

        abstract void onFailed(byte[] data, int code);
    }

    public interface UploadCallback {
        abstract void onSuccess(String fileHash);

        abstract void onFailed(byte[] data, int code);
    }

    private static ProgressDialog loadingPd;

    /**
     * Post  Json
     * 向服务器POST发送json，不能使用RequestParams，而是需要使用post（。。。HttpEntity。。）
     */
    public static void post(final EboxApplication appContext, final String action,
                            Map requestParams, final ClientCallback clientCallback, boolean isShowLoad) {
        try {
            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null, -400);
                ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }
            if (isShowLoad) {
                loadingPd = UIHelper.showProgress(appContext, null,
                        appContext.getResString(R.string.progress_message),
                        true);
            }
            //保存cookie
            RestClient restClient = RestClient.getIntance(appContext);
            Gson gson = new Gson();
            String params = gson.toJson(requestParams);
            //传递的参数      cookie的值
            Logger.i("APIClient  request:---post传递的参数" + params);
            restClient.post(action, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] responseBody) {
                    try {
                        if (loadingPd != null && loadingPd.isShowing()) {
                            UIHelper.dismissProgress(loadingPd);
                        }

                        String data = new String(responseBody);

                        Logger.d("APIClient:----POST -Sucess网络请求成功，" + action + "  " + data);

                        JSONObject json = new JSONObject(new String(
                                responseBody));

                        //状态接口改变
                        int status = json.getInt("status");
                        if (status == 0) {
                            //获取jsonarray
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(json.getJSONObject("data"));
                            Logger.i("Apiclient:状态为0，" + jsonArray.toString());
                            clientCallback.onSuccess(jsonArray, code);
                        }
                        else if (status == 500) {
                            UIHelper.ToastMessage(appContext, "服务器休息了，请稍后再试",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        } else if (status == 400) {
                            UIHelper.ToastMessage(appContext, "发生了一个大家都不知道的错误，重新打开看看",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        } else if (status == 30112) {
                            code = 30112;
                        } else {
                            String message = json.getString("msg");
                            if (message != null && message.length() > 0) {
                                UIHelper.ToastMessage(appContext, message,
                                        Toast.LENGTH_SHORT);
                            }
//                            Logger.i("Apiclient:状态不为0，" + json.getJSONArray("data"));
                            clientCallback.onFailed(responseBody, status);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Logger.d("APIClient:----POST  网络请求失败" + "  " + i);

                    clientCallback.onFailed(bytes, code);
                }
            });

        } catch (Exception e) {

        }
    }


    /**
     * Post  Json
     * 获得支付单接口是走的格格云店的接口
     */
    public static void postPay(final EboxApplication appContext, final String action,
                               Map requestParams, final ClientCallback clientCallback, boolean isShowLoad) {
        try {
            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null, -400);
                ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }
            if (isShowLoad) {
                loadingPd = UIHelper.showProgress(appContext, null,
                        appContext.getResString(R.string.progress_message),
                        true);
            }
            //保存cookie
            RestClient restClient = RestClient.getIntance(appContext);
            Gson gson = new Gson();
            String params = gson.toJson(requestParams);
            //传递的参数      cookie的值
            Logger.i("APIClient  request:---post传递的参数" + params);
            restClient.postPay(action, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int i, Header[] headers, byte[] responseBody) {
                    try {
                        if (loadingPd != null && loadingPd.isShowing()) {
                            UIHelper.dismissProgress(loadingPd);
                        }

                        String data = new String(responseBody);

                        Logger.d("APIClient:----POST -Sucess网络请求成功，" + action + "  " + data);

                        JSONObject json = new JSONObject(new String(
                                responseBody));

                        //状态接口改变
                        int status = json.getInt("status");
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }

                        if (status == 0) {
                            //获取jsonarray
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(json.getJSONObject("data"));
                            Logger.i("Apiclient:状态为0，" + jsonArray.toString());
                            clientCallback.onSuccess(jsonArray, code);
                        }
                        else if (status == 500) {
                            UIHelper.ToastMessage(appContext, "服务器休息了，请稍后再试",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        } else if (status == 400) {
                            UIHelper.ToastMessage(appContext, "发生了一个大家都不知道的错误，重新打开看看",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        }
                        else {
                            String message = json.getString("msg");
                            if (message != null && message.length() > 0) {
                                UIHelper.ToastMessage(appContext, message,
                                        Toast.LENGTH_LONG);
                            }
//                            Logger.i("Apiclient:状态不为0，" + json.getJSONArray("data"));
                            clientCallback.onFailed(responseBody, code);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Logger.d("APIClient:----POST  网络请求失败" + "  " + i);

                    clientCallback.onFailed(bytes, code);
                }
            });

        } catch (Exception e) {

        }
    }


    /**
     * POST请求网络   使用ClientCallBack回调jsonarry
     */
    @SuppressWarnings("unchecked")
    public static void post(final EboxApplication appContext, final String action,
                            RequestParams requestParams, final ClientCallback clientCallback, boolean isShowLoad) {
        try {
            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null, -400);
                ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }
            if (isShowLoad) {
                loadingPd = UIHelper.showProgress(appContext, null,
                        appContext.getResString(R.string.progress_message),
                        true);
            }
            RestClient restClient = RestClient.getIntance(appContext);
            restClient.post(action, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    try {
                        if (loadingPd != null && loadingPd.isShowing()) {
                            UIHelper.dismissProgress(loadingPd);
                        }

                        String data = new String(responseBody);
                        Logger.d("APIClient:----POST-" + action + "  " + data);

                        JSONObject json = new JSONObject(new String(
                                responseBody));

                        //状态接口改变
                        int status = json.getInt("status");
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }

                        if (status == 0) {
                            //获取jsonarray
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(json.getJSONObject("data"));
                            Logger.d("--------ceshi");
                            clientCallback.onSuccess(jsonArray, code);
                        } else if (status == 500) {
                            UIHelper.ToastMessage(appContext, "服务器休息了，请稍后再试",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        } else if (status == 400) {
                            UIHelper.ToastMessage(appContext, "发生了一个大家都不知道的错误，重新打开看看",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        } else {
                            String message = json.getString("msg");
                            if (message != null && message.length() > 0) {
                                UIHelper.ToastMessage(appContext, message,
                                        Toast.LENGTH_LONG);
                            }
                            clientCallback.onFailed(responseBody, code);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    if (loadingPd != null && loadingPd.isShowing()) {
                        UIHelper.dismissProgress(loadingPd);
                    }

                    try {
                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        String message = json.getString("msg");


                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }
                        if (message != null && message.length() > 0) {
                            UIHelper.ToastMessage(appContext, message,
                                    Toast.LENGTH_LONG);
                        }
                        clientCallback.onFailed(responseBody, code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * GET网络请求
     *
     * @param appContext
     * @param action
     * @param clientCallback
     * @param isShowLoad
     */
    @SuppressWarnings("unchecked")
    public static void get(final EboxApplication appContext, final String action,
                           RequestParams requestParams, final ClientCallback clientCallback, boolean isShowLoad) {
        try {
            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null, -400);
                ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }
            if (isShowLoad) {
                loadingPd = UIHelper.showProgress(appContext, null,
                        appContext.getResString(R.string.progress_message),
                        true);
            }
            RestClient restClient = RestClient.getIntance(appContext);
            com.moge.ebox.phone.bettle.tools.Logger.i("APIclient: Get 传递的参数，cookie   " + action + "   " + restClient.myCookieStore);
            restClient.get(action, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    try {
                        if (loadingPd != null && loadingPd.isShowing()) {
                            UIHelper.dismissProgress(loadingPd);
                        }

                        String data = new String(responseBody);
                        Logger.d("APIclient:GET--数据获取成功" + action + "   " + data);

                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        int status = json.getInt("status");
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }

                        if (status == 0) {
                            //如果获取数据成功，需要解析成数组形式
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(json.getJSONObject("data"));
                            Logger.d("APIclient:  GET--" + action + "   " + jsonArray.toString());
                            clientCallback.onSuccess(jsonArray, code);
                        }  else if (status == 500) {
                            UIHelper.ToastMessage(appContext, "服务器休息了，请稍后再试",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        } else if (status == 400) {
                            UIHelper.ToastMessage(appContext, "未知错误，重新打开看看",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        }
                        else {
                            String message = json.getString("msg");
                            if (message != null && message.length() > 0) {
                                UIHelper.ToastMessage(appContext, message,
                                        Toast.LENGTH_LONG);

                            }
                            clientCallback.onFailed(responseBody, status);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    if (loadingPd != null && loadingPd.isShowing()) {
                        UIHelper.dismissProgress(loadingPd);
                    }

                    ToastUtil.showToastShort("数据获取失败");

                    try {
                        JSONObject json = null;
                        String message=null;
                        if (responseBody != null) {
                            json = new JSONObject(new String(
                                    responseBody));
                            message = json.getString("msg");
                        }

                        if (message != null && message.length() > 0) {
                            UIHelper.ToastMessage(appContext, message,
                                    Toast.LENGTH_LONG);
                        }
                        clientCallback.onFailed(responseBody, code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * GET网络请求
     */
    @SuppressWarnings("unchecked")
    public static void getEmptyBox(final EboxApplication appContext, final String action,
                           RequestParams requestParams, final ClientCallback clientCallback, boolean isShowLoad) {
        try {
            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null, -400);
                ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }
            if (isShowLoad) {
                loadingPd = UIHelper.showProgress(appContext, null,
                        appContext.getResString(R.string.progress_message),
                        true);
            }
            RestClient restClient = RestClient.getIntance(appContext);
            com.moge.ebox.phone.bettle.tools.Logger.i("APIclient: Get 传递的参数，cookie   " + action + "   " + restClient.myCookieStore);
            restClient.get(action, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    try {
                        if (loadingPd != null && loadingPd.isShowing()) {
                            UIHelper.dismissProgress(loadingPd);
                        }

                        String data = new String(responseBody);
                        Logger.d("APIclient:GET--数据获取成功" + action + "   " + data);

                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        int status = json.getInt("status");
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }

                        if (status == 0) {
                            //如果获取数据成功，需要解析成数组形式
//                            JSONArray jsonArray = new JSONArray();
//                            jsonArray.put(json.getJSONObject("data"));
//                            Logger.d("APIclient:  GET--" + action + "   " + jsonArray.toString());
                            com.moge.ebox.phone.bettle.tools.Logger.i("api----empty"+json.getJSONArray("data"));
                            clientCallback.onSuccess(json.getJSONArray("data"), code);
                        }  else if (status == 500) {
                            UIHelper.ToastMessage(appContext, "服务器休息了，请稍后再试",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        } else if (status == 400) {
                            UIHelper.ToastMessage(appContext, "发生了一个大家都不知道的错误，重新打开看看",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        }
                        else {
                            String message = json.getString("msg");
                            if (message != null && message.length() > 0) {
                                UIHelper.ToastMessage(appContext, message,
                                        Toast.LENGTH_LONG);

                            }
                            clientCallback.onFailed(responseBody, code);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    if (loadingPd != null && loadingPd.isShowing()) {
                        UIHelper.dismissProgress(loadingPd);
                    }

                    try {
                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        Logger.i("APIclient:错误" + json.toString());
                        String message = json.getString("msg");
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }
                        if (message != null && message.length() > 0) {
                            UIHelper.ToastMessage(appContext, message,
                                    Toast.LENGTH_SHORT);
                        }
                        clientCallback.onFailed(responseBody, code);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


    /**
     * 获得登录aimoge登录状态
     */
    public static void postLoginStateForm(final EboxApplication appContext,
                                          String action, Map params, final ClientCallback clientCallback,
                                          boolean isShowLoad) {
        try {

            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null, -400);
                ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }
            String value = JsonSerializeUtil.map2Json(params);
            RequestParams params2 = new RequestParams();
//            params2.put("SERVICENAME_PARAM", "OPERATOR_SERVICE");
//            params2.put("ACTIV_PARAM", action);
//            params2.put("PARAMES_PARAM", value);
            params2.put("mobile", params.get("mobile"));
            params2.put("code", params.get("code"));
            Logger.i("APIclient:postLogin请求的参数");

            RestClient restClient = RestClient.getIntance(appContext);
            restClient.postLoginForm(params2, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    try {
                        String data = new String(responseBody);
                        Logger.d("Apiclient:postLoginOK" + data.toString());
                        JSONObject json = new JSONObject(data);
                        JSONArray array = new JSONArray();
                        array.put(json);
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }
                        clientCallback.onSuccess(array, code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    try {
                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        String message = json.getString("message");
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }
                        if (message != null && message.length() > 0) {
                            UIHelper.ToastMessage(appContext, message,
                                    Toast.LENGTH_SHORT);
                        }
                        clientCallback.onFailed(responseBody, code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Logger.e(e);
                    }

                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public static void postPage(final EboxApplication appContext,
                                String action, Map params, final ClientCallback clientCallback,
                                boolean isShowLoad) {
        try {

            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null, -400);
                ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }
            String value = JsonSerializeUtil.map2Json(params);
            RequestParams params2 = new RequestParams();
            params2.put("SERVICENAME_PARAM", "OPERATOR_SERVICE");
            params2.put("ACTIV_PARAM", action);
            params2.put("PARAMES_PARAM", value);
            Log.i("main", "action=" + action + ",params=" + params.toString());

            RestClient restClient = RestClient.getIntance(appContext);
            restClient.post(params2, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    try {
                        String data = new String(responseBody);
                        Logger.d(data);
                        JSONObject json = new JSONObject(data);
                        JSONArray array = new JSONArray();
                        array.put(json);
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }
                        clientCallback.onSuccess(array, code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    try {
                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        String message = json.getString("message");
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }
                        if (message != null && message.length() > 0) {
                            UIHelper.ToastMessage(appContext, message,
                                    Toast.LENGTH_SHORT);
                        }
                        clientCallback.onFailed(responseBody, code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Logger.e(e);
                    }

                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public static void upload(final EboxApplication appContext,
                              File file, final UploadCallback clientCallback) {
        try {

            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null, -400);
                //ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }

            loadingPd = UIHelper.showProgress(appContext, null,
                    appContext.getResString(R.string.progress_message),
                    true);

            RequestParams params2 = new RequestParams();
            params2.put("file", file);
            params2.put("kind", "face");

            com.moge.ebox.phone.bettle.tools.Logger.i("Apiclient:upload" + params2.toString());
            RestClient restClient = RestClient.getIntance(appContext);
            restClient.upload(params2, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    try {
                        UIHelper.dismissProgress(loadingPd);
                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        Logger.i("------>Sucess" + json.toString());
                        int status = json.getInt("status");
                        Logger.i("------>Sucess  status" + status);
                        if (status == 0) {
                            String fileHash = json.getJSONObject("data").getString("hash");
                            clientCallback.onSuccess(fileHash);
                        }  else if (status == 500) {
                            UIHelper.ToastMessage(appContext, "服务器休息了，请稍后再试",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        } else if (status == 400) {
                            UIHelper.ToastMessage(appContext, "发生了一个大家都不知道的错误，重新打开看看",
                                    Toast.LENGTH_SHORT);
                            clientCallback.onFailed(responseBody, code);
                        }
                        else {
                            String message = json.getString("msg");
                            if (message != null && message.length() > 0) {
                                UIHelper.ToastMessage(appContext, message,
                                        Toast.LENGTH_SHORT);
                            }
                            clientCallback.onFailed(responseBody, code);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    UIHelper.dismissProgress(loadingPd);
                    Logger.i("Apiclient:upload  error+" + statusCode);
                    Logger.i("Apiclient:upload  error+" + error.getMessage());
                    try {
                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        Logger.i("Apiclient:upload error+" + json.toString());
                        String message = json.getString("msg");
                        if (json.has("code")) {
                            code = StringUtils.toInt(json.getString("code"), -400);
                        }
                        if (message != null && message.length() > 0) {
                            UIHelper.ToastMessage(appContext, message,
                                    Toast.LENGTH_SHORT);
                        }
                        clientCallback.onFailed(responseBody, code);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }

                }
            });
        } catch (Exception e) {
        }
    }


    /*
     * 获取快递员信息
     */
    public static void getOperatorInfor(EboxApplication appContext, Map params,
                                        ClientCallback clientCallback) {
        String action = "operator";
        RequestParams requestParams = new RequestParams();
        get(appContext, action, requestParams, clientCallback, false);
    }

    /*
     * 登录
     */
    public static void login(EboxApplication appContext, Map params,
                             ClientCallback clientCallback) {
        String action = "operator/login";
        post(appContext, action, params, clientCallback, false);
    }

    /*
     * 注册
     */
    public static void register(EboxApplication appContext, Map params,
                                ClientCallback clientCallback) {
        String action = "operator/register";
        post(appContext, action, params, clientCallback, false);
    }

    /*
     * 完善资料
     */
    public static void replenishOperator(EboxApplication appContext, Map params,
                                         ClientCallback clientCallback) {
        String action = "operator/replenish";
        post(appContext, action, params, clientCallback, false);

    }

    /*
     * 修改密码
     */
    public static void retrievePassword(EboxApplication appContext, Map params,
                                        ClientCallback clientCallback) {
        String action = "operator/password/reset";
//		post(appContext, action, requestParams, clientCallback, true);
        post(appContext, action, params, clientCallback, false);
    }

    /*
     * 查询格口状态
     */
    public static void queryBoxState(EboxApplication appContext, Map params,
                                     ClientCallback clientCallback) {

        String action = "queryBoxState";
        RequestParams requestParams = new RequestParams();
        post(appContext, action, requestParams, clientCallback, false);

    }

    /**
     * 空箱查询
     */
    public static void queryEmptyBox(EboxApplication appContext, Map params,
                                     ClientCallback clientCallback) {
        String action = "operator/box/empty";
        RequestParams requetParams = new RequestParams();
//        get(appContext, action, requetParams, clientCallback, false);
        getEmptyBox(appContext, action, requetParams, clientCallback, false);
    }

    /*
     * 查询历史快递信息
     */
    public static void queryHistoryExpress(EboxApplication appContext,
                                           Map params, ClientCallback clientCallback) {
        String action = "delivery/history";
//        postPage(appContext, action, params, clientCallback, false);
        RequestParams requestParams = new RequestParams();
        requestParams.put("start_date", params.get("start_date"));
        requestParams.put("end_date", params.get("end_date"));
        requestParams.put("cursor", params.get("cursor"));
        requestParams.put("page_size", params.get("page_size"));
        requestParams.put("state", params.get("state"));
        get(appContext, action, requestParams, clientCallback, false);
    }

    /*
     * 查询超时快递信息
     */
    public static void queryTimeOutExpress(EboxApplication appContext,
                                           Map params, ClientCallback clientCallback) {
        String action = "delivery/timeout";
//        postPage(appContext, action, params, clientCallback, false);
        RequestParams requestParams = new RequestParams();
        requestParams.put("cursor", params.get("cursor"));
        requestParams.put("page_size", Integer.valueOf(params.get("page_size").toString()));
        get(appContext, action, requestParams, clientCallback, false);
//        post(appContext, action, params, clientCallback, false);
    }

    /*
     * 充值  获得充值订单
     */
    public static void getOperatorChargeInfor(EboxApplication appContext,
                                              Map params, ClientCallback clientCallback) {
        String action = "operator/recharge/order";
//        get(appContext, action, requestParams, clientCallback, false);
        post(appContext, action, params, clientCallback, false);
    }

    /**
     * 获取支付单
     */
    public static void getPayOrder(EboxApplication appContext,
                                   Map params, ClientCallback clientCallback) {
        String action = "pay";
        postPay(appContext, action, params, clientCallback, false);
    }

    /**
     * 获得aimoge支付登录状态
     */
    public static void getAimogeLoginState(EboxApplication appContext,
                                           Map params, ClientCallback clientCallback) {
        String action = "login";
        postLoginStateForm(appContext, action, params, clientCallback, false);
    }

    /**
     * 订单完成  上报订单信息
     */
    public static void getPayOrderOver(EboxApplication appContext,
                                       Map params, ClientCallback clientCallback) {
        String action = "pay/" + params.get("pay_id");
//        post(appContext, action, params, clientCallback, false);
        postPay(appContext, action, params, clientCallback, false);

    }

    /*
     * 活动
     */
    public static void getActive(EboxApplication appContext,
                                 Map params, ClientCallback clientCallback) {
        String action = "operator/recharge/activity";
        RequestParams requestParams = new RequestParams();
//		post(appContext, action, requestParams, clientCallback, false);
        get(appContext, action, requestParams, clientCallback, false);
    }


    /*
     * 查询快递订单状态
     */
    public static void queryExpressOrderState(EboxApplication appContext,
                                              Map params, ClientCallback clientCallback) {
        String action = "delivery/public";
        RequestParams requestParams=new RequestParams();
        requestParams.put("search",params.get("search"));
        requestParams.put("cursor", params.get("cursor"));
        requestParams.put("page_size", Integer.valueOf(params.get("page_size").toString()));
        get(appContext, action, requestParams, clientCallback, false);
    }

    /*
     * 快递快递派件总数
     */
    public static void queryExpressOrderCount(EboxApplication appContext,
                                              Map params, ClientCallback clientCallback) {
        String action = "operator/delivery/count";
//        post(appContext, action, params, clientCallback, false);
        get(appContext, action, null, clientCallback, false);
    }

    /**
     * 获取短信验证码
     */
    public static void getVerifySms(EboxApplication appContext, Map params,
                                    ClientCallback clientCallback) {
        String action = "operator/mobile/sms";
        post(appContext, action, params, clientCallback, false);
    }

    /**
     * 验证短信验证码
     */
    public static void checkVerify(EboxApplication appContext, Map params, ClientCallback clientCallback) {
        String action = "operator/mobile/verify";
        post(appContext, action, params, clientCallback, false);
    }

    /**
     * 获得忘记密码的验证码
     *
     * @param appContext
     * @param params
     * @param clientCallback
     */
    public static void getVerifySmsForRetrievePassword(EboxApplication appContext, Map params,
                                                       ClientCallback clientCallback) {
        String action = "operator/mobile/sms";
        post(appContext, action, params, clientCallback, false);
    }


    public static void getVersion(EboxApplication appContext, Map params,
                                  ClientCallback clientCallback) {
        String action = "operator/version";
        RequestParams requestParams = new RequestParams();
//		post(appContext, action, params, clientCallback, false);
        requestParams.put("client_os", params.get("client_os"));
        get(appContext, action, requestParams, clientCallback, false);
    }

    public static void uploadFace(EboxApplication appContext, File file,
                                  UploadCallback clientCallback) {
        upload(appContext, file, clientCallback);
    }

    /**
     * 获取快递公司
     */
    public static void getOrgnization(EboxApplication appContext, Map params,
                                      ClientCallback clientCallback) {
        String action = "public/organization";
        get(appContext, action, null, clientCallback, false);
    }


    /**
     * 获取省份
     */
    public static void getProvince(EboxApplication appContext, Map params, ClientCallback clientCallback) {
        String action = "public/province";
        get(appContext, action, null, clientCallback, false);
    }

    /**
     * 获取城市
     */
    public static void getCity(EboxApplication appContext, int pid, ClientCallback clientCallback) {
        String action = "public/city/" + pid;
        get(appContext, action, null, clientCallback, false);
    }

    /**
     * 获取所在区域
     */
    public static void getRegion(EboxApplication appContext, int cid,
                                 ClientCallback clientCallback) {
        String action = "public/region/" + cid;
        get(appContext, action, null, clientCallback, false);
    }

    /**
     * 应用初始化接口
     */
    public static void getAppInit(EboxApplication appContext, Map params, ClientCallback clientCallback) {
        String action = "operator/app/init";
        RequestParams requestParams = new RequestParams();
        requestParams.put("type", params.get("type"));
        requestParams.put("client", params.get("client"));
        get(appContext, action, requestParams, clientCallback, false);
    }


    public static boolean hasInternetConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }
}
