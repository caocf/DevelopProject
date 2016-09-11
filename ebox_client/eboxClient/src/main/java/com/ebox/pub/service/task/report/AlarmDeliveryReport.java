package com.ebox.pub.service.task.report;

import android.util.Log;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.database.deliver.Deliver;
import com.ebox.ex.database.deliver.DeliverOp;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.enums.AlarmCode;
import com.ebox.ex.network.model.req.ReqAlarmReport;
import com.ebox.ex.network.request.ReportAlarmRequest;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.utils.LogUtil;

/**
 * Created by Android on 2015/9/1.
 */
public class AlarmDeliveryReport implements IReport, ResponseEventHandler<BaseRsp> {


    private Deliver alarmDeliver = null;

    public AlarmDeliveryReport() {
    }

    private void readStatus() {
        //订单同步失败，告警订单
      //  alarmDeliver = DeliverOp.getDeliverByState(DeliverTable.STATE_1);
    }

    @Override
    public void create() {
        readStatus();
    }

    @Override
    public void report() {
        if (alarmDeliver != null)
        {
            Log.i(GlobalField.tag,
                    "upload Alarm Deliver: ItemId:"
                            + alarmDeliver.getItemId() + " BoxId:"
                            + alarmDeliver.getBox_code() + " Operator:"
                            + alarmDeliver.getOperatorId() + " Telephone:"
                            + alarmDeliver.getTelephone() + " State:"
                            + alarmDeliver.getState());

            ReqAlarmReport req = new ReqAlarmReport();

            req.setBox_code(alarmDeliver.getBox_code());
            req.setAlarm_code(AlarmCode.code_8);
            req.setAlarm_state(0);
            req.setContent("create order failed [OrderId:"+alarmDeliver.getOrder_id()
                    +" OperatorId:"+alarmDeliver.getOperatorId()
                    +" CustomerPhone:"+alarmDeliver.getTelephone()
                    +" ItemId:"+alarmDeliver.getItemId()
                    +" BoxCode:"+alarmDeliver.getBox_code()+"]");

            ReportAlarmRequest request=new ReportAlarmRequest(req,this);
            RequestManager.addRequest(request,null);

        }
    }

    @Override
    public int reportType() {
        return Type_AlarmDelivery;
    }


    @Override
    public void onResponseSuccess(BaseRsp result) {
        if (result.isSuccess())
        {
            LogUtil.i("report alarmDeliver success,delete alarmDelivery orderId:" + alarmDeliver.getOrder_id() + ",BoxCode:" + alarmDeliver.getBox_code());
            DeliverOp.deleteDeliver(alarmDeliver.getBox_code());
        }
        alarmDeliver = null;
    }

    @Override
    public void onResponseError(VolleyError error) {

        try {
            if (alarmDeliver!=null)
            {
                LogUtil.e("report alarmDeliver error ,delete alarmDelivery orderId:" + alarmDeliver.getOrder_id() + ",error:" + error.getMessage());
            }
            alarmDeliver = null;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }
}
