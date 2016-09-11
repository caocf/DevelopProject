package com.ebox.pub.service.task.report;

import android.util.Log;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqPickupItem;
import com.ebox.ex.network.request.ReportPickupItemRequest;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.utils.LogUtil;

/**
 * Created by Android on 2015/9/1.
 */
public class PickupItemReport implements IReport, ResponseEventHandler<BaseRsp> {


    private OrderLocalInfo order = null;

    private boolean isReporting;
    public PickupItemReport() {
        isReporting=false;
    }

    private void readStatus() {
        // 读取一个带同步订单
        if (order==null&&!isReporting)
        {
            order = OrderLocalInfoOp.getOneWaitUploadOrder();
        }
    }

    @Override
    public void create() {
        readStatus();
    }

    @Override
    public void report() {

        if (order != null&&!isReporting)
        {
            isReporting=true;
            Log.i(GlobalField.tag,
                    "upload PickupItem: BoxCode:" + order.getBox_code()
                            + " ItemId:" + order.getItem_id() + " Password:"
                            + order.getPassword() +" customerPhone:"+order.getCustomer_telephone()
                            + " UserType:" + order.getUser_type());

            ReqPickupItem req = new ReqPickupItem();
            //取件密码
            req.setPassword(order.getPassword());
            //取件授权码
            req.setPickup_id(order.getPick_id());
            //取件人
            req.setUser_type(order.getUser_type());
            //取件时间
            String pick_time = order.getPick_time();
            if (pick_time!=null)
            {
                req.setLocal_time(order.getPick_time());
            }

            ReportPickupItemRequest request=new ReportPickupItemRequest(req,this);

            RequestManager.addRequest(request,null);

        }

    }

    @Override
    public int reportType() {
        return Type_PickupItem;
    }


    @Override
    public void onResponseSuccess(BaseRsp result) {
        if (result.isSuccess())
        {
            try
            {
                // 删除取走的订单
                OrderLocalInfoOp.deleteLocalOrder(order.getBox_code());
                LogUtil.i("upload PickupItem success, delete orderId:" + order.getOrder_id() + ",operator:" + order.getOperator_telephone());

            } catch (Exception e)
            {
                LogUtil.e(e.getMessage());
            }
        }
        isReporting=false;
        order=null;
    }

    @Override
    public void onResponseError(VolleyError error) {
        try {
            if (order!=null)
            {
                LogUtil.e("upload PickupItem error boxCode:" + order.getBox_code() + ",orderId:" + order.getOrder_id());
            }
            isReporting=false;
            order = null;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }
}
