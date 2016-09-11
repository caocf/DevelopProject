package com.ebox.pub.service.task;

import java.io.IOException;
import java.util.TimerTask;

import android.util.Log;

import com.ebox.pub.websocket.helper.WebSocketManager;
import com.jni.serialport.SerialPort;

public class WebSocketTask extends TimerTask {

	private TaskData data;
	private int connectTimes = 0;
	private static final String TAG = "WebSocketTask";

	public WebSocketTask(TaskData data) {
		this.data = data;
	}

	@Override
	public void run() {
		// 执行websocket的连接
		data.setLastRunning(System.currentTimeMillis());
		WebSocketManager wsktManager = WebSocketManager.instance();
		if (!wsktManager.isConnected()) {
			// 如果连接不成功
			connectTimes++;
			wsktManager.reconnect();
		} else {
			connectTimes = 0;

		}

		// 如果3分钟仍然没有连接上的话
		if (connectTimes >= 3 * 6) {
			// 需要重新启动底层网络
			restart3G();
			connectTimes = -42;
		}

	}

	public void restart3G() {
		Log.i(TAG, "restart3G");
		Process su = null;
		try {
			su = SerialPort.getSu();
			String cmd;
			cmd = "echo 1 > /sys/devices/platform/hub_power/ctrl_modem\n";
			su.getOutputStream().write(cmd.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (su != null) {
				// su.destroy();
			}
		}
	}

}
