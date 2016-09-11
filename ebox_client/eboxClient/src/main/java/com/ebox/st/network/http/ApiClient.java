package com.ebox.st.network.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ebox.R;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.utils.FunctionUtils;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.Anetwork.util.json.JsonSerializeUtil;
import com.ebox.st.model.CarryCertificationReq;
import com.ebox.st.model.CarryCertificationRsp;
import com.ebox.st.model.CheckWorkReq;
import com.ebox.st.model.CheckWorkRsp;
import com.ebox.st.model.GetNoticeReq;
import com.ebox.st.model.GetNoticeRsp;
import com.ebox.st.model.GetUserFileReq;
import com.ebox.st.model.GetUserFileRsp;
import com.ebox.st.model.QueryUserFilesReq;
import com.ebox.st.model.QueryUserFilesRsp;
import com.ebox.st.model.QueryWorkInfoReq;
import com.ebox.st.model.QueryWorkInfoRsp;
import com.ebox.st.model.QueryWorkProgressReq;
import com.ebox.st.model.QueryWorkProgressRsp;
import com.ebox.st.model.SubmitCertificationReq;
import com.ebox.st.model.SubmitCertificationRsp;
import com.ebox.st.model.SubmitCommentsReq;
import com.ebox.st.model.SubmitCommentsRsp;
import com.ebox.st.model.SubmitEditCertificationReq;
import com.ebox.st.model.SubmitEditCertificationRsp;
import com.ebox.st.model.SubmitPopulationReq;
import com.ebox.st.model.SubmitPopulationRsp;
import com.ebox.st.model.TakeCertificationReq;
import com.ebox.st.model.TakeCertificationRsp;
import com.ebox.st.model.WorkerLoginReq;
import com.ebox.st.model.WorkerLoginRsp;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;


public class ApiClient {
    public final static String message_error = "服务器连接有问题";

    public static int code = -400;

    private static Tip tip;


    public interface ClientCallback {
        abstract void onSuccess(Object data);

        abstract void onFailed(Object data);
    }

    public interface UploadCallback {
        abstract void onSuccess(String filePath, int fileId);

        abstract void onFailed(byte[] data);
    }


    public static void upload(final Context appContext,
                              File file, final UploadCallback clientCallback) {
        try {

            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null);
                //ToastUtil.showToastShort("网络开小差了,请检查网络连接");
                return;
            }


            RequestParams params2 = new RequestParams();
            params2.put("file", file);
            params2.put("kind", "face");

            RestClient restClient = RestClient.getIntance(appContext);
            restClient.upload(params2, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    try {

                        String data = new String(responseBody);


                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        boolean success = json.getBoolean("success");

                        if (success) {
                            int fileId = json.getInt("id");
                            String filePath = json.getString("filePath");
                            clientCallback.onSuccess(filePath, fileId);
                        } else {
                            String message = json.getString("message");
                            if (message != null && message.length() > 0) {
                            /*	UIHelper.ToastMessage(appContext, message,
										Toast.LENGTH_SHORT);*/
                            }
                            clientCallback.onFailed(responseBody);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    try {
                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        String message = json.getString("message");
                        if (message != null && message.length() > 0) {
	/*						UIHelper.ToastMessage(appContext, message,
									Toast.LENGTH_SHORT);*/
                            showPrompt(message, appContext);

                        }
                        clientCallback.onFailed(responseBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }

                }
            });
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unchecked")
    public static void post(final Context appContext, String action,
                            String params, final ClientCallback clientCallback,
                            final Class RspClass) {
        try {

            boolean connected = hasInternetConnected(appContext);
            if (!connected) {
                clientCallback.onFailed(null);
//				showPrompt(appContext.getResources().getString(R.string.connect_failed),appContext);
                FunctionUtils.showToastLong(appContext, appContext.getResources().getString(R.string.pub_connect_failed));
                return;
            }


//			String value = JsonSerializeUtil.map2Json(params);
            RequestParams params2 = new RequestParams();
            params2.put("SERVICENAME_PARAM", "LONGAN_SERVICE");
            params2.put("ACTIV_PARAM", action);
            params2.put("PARAMES_PARAM", params);
            if (Constants.DEBUG)
                LogUtil.d("ApiClient", "action=" + action + ",params=" + params.toString()
                        + ",value=" + params);
            RestClient restClient = RestClient.getIntance(appContext);
            restClient.post(params2, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    try {

                        String sx = new String(responseBody);
                        LogUtil.d("ApiClient", sx);
                        Gson gson = new Gson();
                        Object data = gson.fromJson(sx, RspClass);

                        clientCallback.onSuccess(data);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Logger.e(e);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    if (responseBody == null) {
                        clientCallback.onFailed(null);
                        FunctionUtils.showToastLong("网络有问题，请稍后再试！");
                    }
                    try {
                        JSONObject json = new JSONObject(new String(
                                responseBody));
                        String message = json.getString("message");
                        if (message != null && message.length() > 0) {
                            showPrompt(message, appContext);
                        }
                        clientCallback.onFailed(JsonSerializeUtil.json2Bean(json.toString(), RspClass));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
        }
    }


    private static void showPrompt(String msg, Context mContext) {

        tip = new Tip(mContext, msg, null);
        tip.show(0);
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

    public static void checkWork(Context mContext, CheckWorkReq req,
                                 ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "judgeUserOrder", json, clientCallback, CheckWorkRsp.class);
    }

    public static void submitCertification(Context mContext, SubmitCertificationReq req,
                                           ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "submitCertification", json, clientCallback, SubmitCertificationRsp.class);

    }

    public static void submitEditCertification(Context mContext, SubmitEditCertificationReq req,
                                               ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "submitEditCertification", json, clientCallback, SubmitEditCertificationRsp.class);

    }

    public static void carryCertification(Context mContext, CarryCertificationReq req,
                                          ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "carryCertification", json, clientCallback, CarryCertificationRsp.class);

    }

    public static void queryWorkInfor(Context mContext, QueryWorkInfoReq req,
                                      ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "queryWorkInfor", json, clientCallback, QueryWorkInfoRsp.class);

    }

    public static void queryUserFiles(Context mContext, QueryUserFilesReq req,
                                      ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "queryUserFiles", json, clientCallback, QueryUserFilesRsp.class);

    }

    public static void getUserFile(Context mContext, GetUserFileReq req,
                                   ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "getUserFile", json, clientCallback, GetUserFileRsp.class);

    }

    public static void queryWorkProgress(Context mContext, QueryWorkProgressReq req,
                                         ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "queryWorkProgress", json, clientCallback, QueryWorkProgressRsp.class);

    }

    public static void takeCertification(Context mContext, TakeCertificationReq req,
                                         ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "takeCertification", json, clientCallback, TakeCertificationRsp.class);

    }

    public static void submitPopulation(Context mContext, SubmitPopulationReq req,
                                        ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "submitPopulation", json, clientCallback, SubmitPopulationRsp.class);

    }

    public static void submitComments(Context mContext, SubmitCommentsReq req,
                                      ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "submitComments", json, clientCallback, SubmitCommentsRsp.class);

    }

    public static void workerLogin(Context mContext, WorkerLoginReq req,
                                   ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "workerLogin", json, clientCallback, WorkerLoginRsp.class);

    }

    public static void getNotice(Context mContext, GetNoticeReq req,
                                 ClientCallback clientCallback) {
        String json = JsonSerializeUtil.bean2Json(req);
        post(mContext, "getNotice", json, clientCallback, GetNoticeRsp.class);

    }
}
