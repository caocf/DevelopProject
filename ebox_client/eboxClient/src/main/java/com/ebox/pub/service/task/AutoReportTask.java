package com.ebox.pub.service.task;

import com.ebox.ex.utils.SharePreferenceHelper;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.report.AutoReportManager;
import com.ebox.pub.service.task.report.I.ReportType;

import java.util.TimerTask;

public class AutoReportTask extends TimerTask implements ReportType{
	private static Long autoReportTaskTimes = 580L;


	private TaskData data;

	public AutoReportTask(TaskData data) {
		this.data = data;
	}


	@Override
	public void run() {
		data.setLastRunning(System.currentTimeMillis());
		// Log.e("Timer",
		// "set mAutoReportLastRunning:"+AppService.getIntance().mAutoReportLastRunning);
		// AppService.getIntance().checkTask();

		autoReportTaskTimes++;
		if (autoReportTaskTimes >= 65535) {
			autoReportTaskTimes = 1L;
		}

		syncTask();
	}

	private void syncTask()
	{
		//系统未完成初始化
		if (!SharePreferenceHelper.getInitBoxState())
		{
			return;
		}
		// 向服务端上报
		long period = 5 * 1000 * 60;
		if (GlobalField.serverConfig != null
				&& GlobalField.serverConfig.getHeartbeat() != null
				&& GlobalField.serverConfig.getHeartbeat() != 0)
		{
			period = GlobalField.serverConfig.getHeartbeat() * 1000 * 60;

		}
		// 快件箱状态报告,下拉超期件,下拉更新信息
		if (autoReportTaskTimes % (period / 1000) == 0) {

			AutoReportManager.instance().executeReport(Type_TerminalStatusReport);

			AutoReportManager.instance().executeReport(Type_TimeoutOrder);

			AutoReportManager.instance().executeReport(Type_UpdateInfo);

		}

		// 每分钟离线取件信息同步
		if (autoReportTaskTimes % 60 == 0) {
			AutoReportManager.instance().executeReport(Type_PickupItem);
		}

		//4分钟本地重试订单同步
		if (autoReportTaskTimes % (4 * 60) == 0) {
			AutoReportManager.instance().executeReport(Type_ConfirmDelivery);
		}

		// 每分钟同步订单失败告警记录
		/*if ((autoReportTaskTimes + 25) % 60 == 0) {
			AutoReportManager.instance().executeReport(Type_AlarmDelivery);
		}*/

		// 每分钟同步告警
		if ((autoReportTaskTimes + 30) % 60 == 0) {
			AutoReportManager.instance().executeReport(Type_Alarm);
		}


		// 每分钟同步支付成功、未同步的订单
		if ((autoReportTaskTimes + 40) % 60 == 0) {
			AutoReportManager.instance().executeReport(Type_Trading);
		}
	}




}
