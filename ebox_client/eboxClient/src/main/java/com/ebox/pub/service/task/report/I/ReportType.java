package com.ebox.pub.service.task.report.I;

/**
 * Created by Android on 2015/9/1.
 */
public interface ReportType {

    public static final int Type_TerminalStatusReport = 1;//格口上报

    public static final int Type_TimeoutOrder = 2;//超期件

    public static final int Type_PickupItem = 3;//订单取走

    public static final int Type_ConfirmDelivery= 4;//订单生成

    public static final int Type_AlarmDelivery = 5;//订单告警

    public static final int Type_Alarm= 6;//告警

    public static final int Type_Trading= 7;//生鲜交易订单

    public static final int Type_UpdateInfo = 8;//下拉服务端配置信息

}
