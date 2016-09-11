package com.ebox.pub.service.task.report;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.adv.AdvDownload;
import com.ebox.ex.network.model.enums.UpdateType;
import com.ebox.ex.network.model.RspPullUpdateInfo;
import com.ebox.ex.network.request.PullUpdateInfoRequest;
import com.ebox.pub.file.CtrlFile;
import com.ebox.pub.ledctrl.LedUtil;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.service.task.report.helper.ServiceConfigHelper;
import com.ebox.pub.utils.LogUtil;

import java.util.List;

/**
 * Created by Android on 2015/9/1.
 */
public class PullUpdateInfoTask implements IReport, ResponseEventHandler<RspPullUpdateInfo> {


    private static final String TAG = "PullUpdateInfoTask";
    private PullUpdateInfoRequest request;

    public PullUpdateInfoTask() {
        request = new PullUpdateInfoRequest(this);
    }

    @Override
    public void create() {
    }

    @Override
    public void report() {
        if (!GlobalField.boxLocalInit) {
            return;
        }
        if (request==null) {
            request = new PullUpdateInfoRequest(this);
        }
        RequestManager.addRequest(request,null);
    }

    @Override
    public int reportType() {
        return Type_TimeoutOrder;
    }

    @Override
    public void onResponseError(VolleyError error) {
        LogUtil.e(TAG, "pull update info error " + error.getMessage());
    }

    @Override
    public void onResponseSuccess(RspPullUpdateInfo result) {
        if (result==null) {
            return;
        }

        if (!result.isSuccess()) {
            return;
        }

        List<Integer> rsp = result.getData();

        if (rsp != null && rsp.size() > 0) {
            for (int i = 0; i < rsp.size(); i++) {
                int update = rsp.get(i);

                if (update == UpdateType.config) {
                    new ServiceConfigHelper(1).undateConfig();
                } else if (update == UpdateType.version) {
                    CtrlFile.setNeedUpdate(1);
                } else if (update == UpdateType.advertise) {
                    //下载广告
                    new AdvDownload().advDownload();
                } else if (update == UpdateType.led_content) {
                    //更新LED内容
                    new LedUtil().updateLedContent();
                } else {
                    //更新LED配置
                    new LedUtil().updateLedConfig();
                }

            }
        }

    }

}
