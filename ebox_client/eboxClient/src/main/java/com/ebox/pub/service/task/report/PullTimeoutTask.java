package com.ebox.pub.service.task.report;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.network.model.RspPullTimeoutOrder;
import com.ebox.ex.network.request.PullTimeoutOrderRequest;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.utils.LogUtil;

import java.util.List;

/**
 * Created by Android on 2015/9/1.
 */
public class PullTimeoutTask implements IReport, ResponseEventHandler<RspPullTimeoutOrder> {


    private static final String TAG = "PullTimeoutTask";
    PullTimeoutOrderRequest request;

    public PullTimeoutTask() {
        request = new PullTimeoutOrderRequest(this);
    }

    @Override
    public void create() {
    }

    @Override
    public void report() {
        if (!GlobalField.boxLocalInit) {
            return;
        }
        if (request == null)
        {
            request = new PullTimeoutOrderRequest(this);
        }
        RequestManager.addRequest(request,null);
    }

    @Override
    public int reportType() {
        return Type_TimeoutOrder;
    }

    @Override
    public void onResponseError(VolleyError error) {
        LogUtil.e(TAG, "pull time out order error " + error.getMessage());
    }

    @Override
    public void onResponseSuccess(RspPullTimeoutOrder result) {
        if (result==null) {
            return;
        }

        if (!result.isSuccess())
        {
            return;
        }

        List<String> items = result.getData();

        if (items == null) {
            return;
        }
        for (String orderId : items) {

            OrderLocalInfoOp.updateLocalOrderToTimeOutState(orderId);
        }
    }

}
