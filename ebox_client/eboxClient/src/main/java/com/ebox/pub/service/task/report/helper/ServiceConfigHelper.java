package com.ebox.pub.service.task.report.helper;

import android.os.SystemClock;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.Anetwork.config.Temp;
import com.ebox.AppApplication;
import com.ebox.ex.network.model.RspGetConfig;
import com.ebox.ex.network.model.base.type.AppVersion;
import com.ebox.ex.network.model.enums.UpdateType;
import com.ebox.ex.network.model.req.ReqGetConfig;
import com.ebox.ex.network.request.RequestGetConfig;
import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.file.TempFile;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.FunctionUtil;
import com.ebox.pub.utils.LogUtil;

/**
 * Created by Android on 2015/9/7.
 */
public class ServiceConfigHelper {


    private int type;

    public ServiceConfigHelper(int type) {
        this.type = type;
    }

    public void undateConfig() {
        // 获取配置信息
        ReqGetConfig req = new ReqGetConfig();

        AppVersion app = new AppVersion();
        app.setPackageName("com.ebox");
        app.setClientVersion(FunctionUtil.getCurVersion());
        req.setVersion(app);

        RequestGetConfig requestGetConfig = new RequestGetConfig(req, new ResponseEventHandler<RspGetConfig>() {

            @Override
            public void onResponseSuccess(RspGetConfig result) {
                if (result.isSuccess()) {
                    GlobalField.serverConfig = result.getData();

                    //记录管理员密码
                    SharePreferenceHelper.setSupperAdmin(AppApplication.globalContext,
                            GlobalField.serverConfig.getUserList());
                    //保存计费字段
                    if (GlobalField.serverConfig.getIsAccount() == null)
                    {
                        GlobalField.serverConfig.setIsAccount(TempFile.getTemp().getIs_account());
                    } else {
                        Temp tmp = new Temp();
                        tmp.setIs_account(GlobalField.serverConfig.getIsAccount());
                        tmp.setLastReboot(null);
                        TempFile.saveTemp(tmp);
                    }
                    if (type == 1) {
                        //上报状态
                        BackFillHelper.instance().backFill(UpdateType.config);
                    }

                    //String system_time = result.getData().getSystem_time();
                   // checkTime(system_time);
                }
            }

            @Override
            public void onResponseError(VolleyError error) {
                LogUtil.e("update service config error " + error.getMessage());
            }
        });

        RequestManager.addRequest(requestGetConfig, null);
    }

    private void checkTime(String time) {
        if (time == null || time.equals(""))
        {
            return;
        }
        //1444808652
        long system_time = Long.valueOf(time);

        long current_time = SystemClock.currentThreadTimeMillis();

        if (system_time - current_time > 5 * 1000 * 10)
        {
            SystemClock.setCurrentTimeMillis(system_time);
        }

    }

}
