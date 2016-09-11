package com.ebox.pub.service.task.report.helper;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.request.RequestBackFillUpdate;
import com.ebox.pub.utils.LogUtil;

/**
 * Created by Android on 2015/9/2.
 */
public class BackFillHelper implements ResponseEventHandler<BaseRsp> {


    private static BackFillHelper helper;

    private BackFillHelper() {
    }

    public static BackFillHelper instance() {
        if (helper == null) {
            helper = new BackFillHelper();
        }
        return helper;
    }


    public void backFill(int type) {
//        ReqBackFillUpdate req = new ReqBackFillUpdate();
//        req.setUpdate_type(type);
        backFillRequest(type);
    }

    private void backFillRequest(Integer req) {
        RequestBackFillUpdate request = new RequestBackFillUpdate(req, this);
        RequestManager.addRequest(request, null);
    }


    @Override
    public void onResponseSuccess(BaseRsp result) {
        if (result.isSuccess())
        {
            LogUtil.i("state report success ");
        }
    }

    @Override
    public void onResponseError(VolleyError error) {
        LogUtil.i("state report error " + error.getMessage());

    }
}
