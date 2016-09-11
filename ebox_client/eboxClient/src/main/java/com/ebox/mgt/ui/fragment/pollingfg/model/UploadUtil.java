package com.ebox.mgt.ui.fragment.pollingfg.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ebox.Anetwork.RequestManager;
import com.ebox.pub.utils.LogUtil;

import org.json.JSONObject;

/**
 * Created by prin on 2015/9/30.
 */
public class UploadUtil {

    private static String upload_url;
    private static UploadUtil uploadUtil;
    public static String UPLOADUTIL = "UploadUtil";

    private UploadUtil() {
    }

    public static UploadUtil newInstance() {
        if (uploadUtil == null) {
            uploadUtil = new UploadUtil();
        }
        return uploadUtil;
    }

    /**
     * 上报巡检信息
     * @param jsonRequest
     */
    public void uploadPolling(JSONObject jsonRequest) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(upload_url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.i("上报成功 ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i("上报失败");
            }
        });

        RequestManager.addRequest(jsonObjectRequest, "uploadPolling");
    }


    /**
     * 上报生产，出厂信息
     */
    public void uploadBoard(JSONObject jsonRequest){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(upload_url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                LogUtil.i("上报成功 ");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i("上报失败");
            }
        });

        RequestManager.addRequest(jsonObjectRequest, "uploadBoard");
    }


}
