package com.ebox.pub.service.task.report;

import android.util.Log;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.req.ReqAlarmReport;
import com.ebox.ex.network.request.ReportAlarmRequest;
import com.ebox.pub.database.alarm.Alarm;
import com.ebox.pub.database.alarm.AlarmOp;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.utils.LogUtil;

/**
 * Created by Android on 2015/9/1.
 */
public class AlarmReport implements IReport, ResponseEventHandler<BaseRsp> {


    /**
     * 告警类型	告警码	告警项	           自动恢复	      说明
             0	1	    数据库异常	         是	        数据库连接失败时告警
             0	2	    磁盘空间异常	         是	        磁盘空间超过90%时告警
             1	1	    终端副柜异常	         是	        主柜和副柜通讯异常
             1	2	    终端上报异常	         是  	    终端信息上报异常
             1	2	    终端网络异常	         是	        主柜通讯异常
             1	3	    开锁失败告警	         否	        开锁失败，锁定该箱门，人工处理
             1	4	    磁盘空间异常	         是	        终端磁盘空间小于100M告警
             1	5	    箱门长时间打开告警	 是	        终端某个门长时间打开未关闭
             1	6	    箱门状态异常	         是	        箱门状态和是否有存件等不匹配
             1	7	    长时间未取件告警	     是	        超过3天仍未取件告警
             1	8	    快递员未点击存件，自动重试失败	否	快递员未点击存件，自动重试失败
             1	9	    吞卡告警	             否	        机器因某种异常吞卡
             1	10	    断电告警	             是	        220V电源断电，切换到电瓶电时上报告警
     */
    private Alarm alarm = null;

    public AlarmReport() {
    }

    private void readStatus() {
        alarm = AlarmOp.getAlarm();
    }

    @Override
    public void create() {
        readStatus();
    }

    @Override
    public void report() {

        if (alarm != null)
        {
            Log.i(GlobalField.tag,"upload alarm : alarmCode:"+alarm.getAlarmCode());

            ReqAlarmReport req = new ReqAlarmReport();

            req.setBox_code(alarm.getBoxId());

            req.setAlarm_code(alarm.getAlarmCode());

            req.setAlarm_state(alarm.getState());

            req.setContent(alarm.getContent());

            ReportAlarmRequest request=new ReportAlarmRequest(req,this);
            RequestManager.addRequest(request,null);
        }

    }

    @Override
    public int reportType() {
        return Type_Alarm;
    }


    @Override
    public void onResponseSuccess(BaseRsp result) {
        if (result.isSuccess())
        {
            LogUtil.i("report alarm success ,delete alarm : alarmCode:" + alarm.getAlarmCode() + ",boxCode:" + alarm.getBoxId());
            AlarmOp.deleteAlarm(alarm.get_id());
        }
        alarm = null;
    }

    @Override
    public void onResponseError(VolleyError error) {
        try {
            if (alarm!=null)
            {
                LogUtil.i("report alarm error  alarmCode:" + alarm.getAlarmCode() + ",boxCode:" + alarm.getBoxId());
            }
            alarm = null;
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }
}
