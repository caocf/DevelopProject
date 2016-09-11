package com.moge.ebox.phone.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.ui.update.VersionUpdateCache;
import com.moge.ebox.phone.utils.DeviceInfoUtil;
import com.moge.ebox.phone.utils.UmengUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IndexActivity extends BaseActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        context = IndexActivity.this;
        Log.i("main", "getNetType:" + DeviceInfoUtil.getNetType(context));
        Log.i("main", "getDeviceId:" + DeviceInfoUtil.getDeviceId(context));
        Log.i("main", "getDeviceName:" + DeviceInfoUtil.getDeviceName(context));
        Log.i("main", "getCpuName:" + DeviceInfoUtil.getCpuName());
        Log.i("main", "getSysVersion:" + DeviceInfoUtil.getSysVersion(context));
        UmengUtil.initConfig(getApplicationContext());
        PushManager.getInstance().initialize(this.getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean connected = hasInternetConnected();
        if (!connected) {
            openWirelessSet();
        } else {
            //进行初始化接口
            HashMap paramsInit = new HashMap();
            paramsInit.put("type", 1);    //1，快递员app，2terminal
            paramsInit.put("client", 1);    //1,andorid  2,ios
            ApiClient.getAppInit(EboxApplication.getInstance(), paramsInit, new ApiClient.ClientCallback() {
                @Override
                public void onSuccess(JSONArray data, int code) {
                    Logger.i("IndexActivity: sucess" + data.toString());
                    if (data.length() > 0) {
                        try {
                            boolean check_new_version=data.getJSONObject(0).getJSONObject("settings").getBoolean("check_new_version");
                            if (check_new_version){
                                doVersionUpdate();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailed(byte[] data, int code) {
                    Logger.i("IndexActivity: failed" + code);
                    doVersionUpdate();
                }
            });
            doVersionUpdate();
        }
    }

    private void doVersionUpdate() {
        Map params = new HashMap();
        params.put("client_os", ApiClient.android_id);
        //版本更新
        ApiClient.getVersion(EboxApplication.getInstance(), params, new ApiClient.ClientCallback() {
            @Override
            public void onSuccess(JSONArray data, int code) {
                Logger.i("IndexActivity:---version:获取版本成功");
                Log.i("Index", "获取版本成功");
                if (data.length() > 0) {
                    try {
                        JSONObject versionObject = data.getJSONObject(0).getJSONObject("version");
                        String clientVersion = versionObject.getString("clientVersion");
                        int importantLevel = versionObject.getInt("importantLevel");
                        String downloadUrl = versionObject.getString("downloadUrl");
                        String updateContent = versionObject.getString("updateContent");
                        Logger.i("IndexActivity:---version:" + versionObject.toString());
                        VersionUpdateCache versionUpdateCache = new VersionUpdateCache(
                                IndexActivity.this,
                                new VersionUpdateCache.UpdateResultListener() {
                                    @Override
                                    public void onNotUpdate() {
                                        onChoose();
                                    }

                                    @Override
                                    public void onQuit() {
                                        IndexActivity.this.finish();
                                    }

                                    @Override
                                    public void onNextUpdate() {
                                        onChoose();
                                    }
                                });
                        versionUpdateCache.checkNewVersion(clientVersion, importantLevel, downloadUrl, updateContent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    onChoose();
                }
            }

            @Override
            public void onFailed(byte[] data, int code) {
                Logger.i("IndexActivity:---version:获得版本失败");
                onChoose();
            }
        });
    }

    public void onChoose() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                choose();
            }
        }, 1000);
    }

    public void openWirelessSet() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder
                .setTitle("提示")
                .setMessage("网络异常,是否检测网络连接?")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                        IndexActivity.this.finish();
                    }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        IndexActivity.this.finish();
                    }
                });
        dialogBuilder.show();
    }

    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    protected void choose() {
        boolean loginState = EboxApplication.getInstance().isLogin();
        if (loginState) {
            gotoHomePage();
        } else {
            doSignin();
        }
    }

    protected void doSignin() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        this.finish();
    }

    private void gotoHomePage() {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        this.finish();
    }


}
