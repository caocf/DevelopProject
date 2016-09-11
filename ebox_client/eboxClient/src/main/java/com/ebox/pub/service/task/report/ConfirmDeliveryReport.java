package com.ebox.pub.service.task.report;

import android.util.Log;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoTable;
import com.ebox.ex.database.deliver.Deliver;
import com.ebox.ex.database.deliver.DeliverOp;
import com.ebox.ex.database.deliver.DeliverTable;
import com.ebox.ex.network.model.RspConfirmDelivery;
import com.ebox.ex.network.model.base.type.OrderInfo;
import com.ebox.ex.network.model.enums.OrderImageType;
import com.ebox.ex.network.model.req.ReqConfirmDelivery;
import com.ebox.ex.network.request.ReportConfirmDeliveryRequest;
import com.ebox.pub.camera.CameraData;
import com.ebox.pub.camera.OrderPicture;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Android on 2015/9/1.
 */
public class ConfirmDeliveryReport implements IReport, ResponseEventHandler<RspConfirmDelivery> {

    private ArrayList<Deliver> all_deliver;

    public ConfirmDeliveryReport() {
    }

    private void readStatus()
    {
        if (all_deliver == null)
        {
            all_deliver = DeliverOp.getDeliverByState(DeliverTable.STATE_0);
        }
    }

    @Override
    public void create() {
        readStatus();
    }

    @Override
    public void report() {
        if (all_deliver != null && all_deliver.size() > 0)
        {
            for (Deliver deliver : all_deliver)
            {
                reportDelivery(deliver);
            }
        }else {
            Log.i(GlobalField.tag,"no deliver");
        }

        all_deliver=null;
    }

    private void reportDelivery(Deliver deliver) {

        if (deliver != null ) {
            Log.i(GlobalField.tag,
                    "upload local deliver: ItemId:" + deliver.getItemId()
                            + " OrderId:" + deliver.getOrder_id()
                            + " BoxCode:" + deliver.getBox_code()
                            + " Operator:" + deliver.getOperatorId()
                            + " Msisdn:" + deliver.getTelephone()
                            + " time:" + deliver.getTime()
            );
            // 确认投放完成,提交订单
            ReqConfirmDelivery req = new ReqConfirmDelivery();

            req.setBox_code(deliver.getBox_code());
            req.setItem_id(deliver.getItemId());
            req.setOperator(deliver.getOperatorId());
            req.setMsisdn(deliver.getTelephone());
            req.setOrder_id(deliver.getOrder_id());
            String time= deliver.getTime();
            req.setLocal_time(time==null?System.currentTimeMillis()+"":time);//本地存件时间
            ReportConfirmDeliveryRequest request = new ReportConfirmDeliveryRequest(req, this);

            RequestManager.addRequest(request, null);
        }
    }

    @Override
    public int reportType() {
        return Type_ConfirmDelivery;
    }


    @Override
    public void onResponseSuccess(RspConfirmDelivery confirmDelivery) {
        if (confirmDelivery.isSuccess())
        {
            try {

                OrderInfo rsp = confirmDelivery.getData();
                String box_code=rsp.getBox().getCode();
                LogUtil.i(GlobalField.tag, "upload delivery success , boxCode:" + box_code + ",itemId:" + rsp.getItem_id());

                // 本地存储order订单
                OrderLocalInfo orderLocal = new OrderLocalInfo();
                orderLocal.setBox_code(box_code);
                orderLocal.setOperator_telephone(rsp.getOperator_username());
                orderLocal.setCustomer_telephone(rsp.getMsisdn());
                orderLocal.setItem_id(rsp.getItem_id());
                orderLocal.setOrder_id(rsp.getOrder_id());

                orderLocal.setOrder_state(OrderLocalInfoTable.STATE_CREATE);
                orderLocal.setDelivery_at(rsp.getDelivery_at());//存件时间
                orderLocal.setPassword(rsp.getPassword());
                orderLocal.setPick_id(rsp.getPickup_id());
                orderLocal.setTime_out(0);//未超时

                OrderLocalInfoOp.addLocalOrder(orderLocal);


                // 存件照片转移到photo目录
                CameraData cameraData = new CameraData();
                cameraData.setOrderId(rsp.getOrder_id());
                cameraData.setItemId(rsp.getItem_id());
                cameraData.setImageType(OrderImageType.STORE);

                String filename = OrderPicture.getDeliverDir().getPath()
                        + File.separator
                        + OrderPicture.getOrderPictureName(cameraData);
                cameraData.setOrderId(rsp.getOrder_id());
                String upload_name = OrderPicture.getUploadDir().getPath()
                        + File.separator
                        + OrderPicture.getOrderPictureName(cameraData);
                File file = new File(filename);
                File uploadFile = new File(upload_name);

                if (file.exists()) {
                    file.renameTo(uploadFile);
                }

                //删除delivery
                DeliverOp.deleteDeliver(box_code);
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }
        }
      /*  {
            try {
                LogUtil.e("upload delivery failed ,change local delivery stateTo1 : orderId:" + deliver.getOrder_id());
                deliver.setState(DeliverTable.STATE_1);
                DeliverOp.updateDeliver(deliver);
            } catch (Exception e) {
                LogUtil.e(e.getMessage());
            }
        }*/
    }

    @Override
    public void onResponseError(VolleyError error) {

        try {
            {
                LogUtil.e("upload delivery error " + error.getMessage());
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }

}
