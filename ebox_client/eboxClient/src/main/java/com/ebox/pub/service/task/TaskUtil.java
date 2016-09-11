package com.ebox.pub.service.task;

import java.util.Timer;

import android.util.Log;

import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.websocket.helper.WebSocketManager;

public class TaskUtil {
	public static void startTask(TaskData data)
	{
		data.setTimer(new Timer());
		if(data.getType() == TaskType.BoxCtrlTask)
		{
			data.setTask(new BoxCtrlTask(data));
		}
		else if(data.getType() == TaskType.AutoReportTask)
		{
			data.setTask(new AutoReportTask(data));
		}
		else if(data.getType() == TaskType.ScreenProtectTask)
		{
			data.setTask(new ScreenProtectTask(data));
		}
		else if(data.getType() == TaskType.PicQueueTask)
		{
			data.setTask(new PicUploadTask(data));
		}
		else if(data.getType() == TaskType.CheckTask)
		{
			data.setTask(new CheckTask(data));
		}
		//增加websocket任务
		else if(data.getType()==TaskType.WebSocketTask){
			WebSocketManager wsktManager = WebSocketManager.instance();
			wsktManager.init();
			data.setTask(new WebSocketTask(data));
		}
		
		//增加检测二维码生命期的任务
		else if (data.getType()==TaskType.CheckQrCodeTask) {
			data.setTask(new CheckQrTask(data));
		}
		
		
		
		data.getTimer().schedule(data.getTask(), 1000, data.getPeriod());
		data.setLastRunning(System.currentTimeMillis());
	}
	
	public static void checkTask(TaskData data)
	{
		//Log.e("Timer", "check lastRunning:"+lastRunning+" "+(System.currentTimeMillis() - lastRunning));
		
		// 超长时间没响应，则超时
		if(System.currentTimeMillis() - data.getLastRunning() > data.getPeriod()*data.getTimeout())
		{
			Log.e("warning!!", "cancel lastRunning:"+
					System.currentTimeMillis()+" "+
					data.getLastRunning()+" "
					+data.getPeriod()*60+" "
					+data.getType());
			cleanTimerTask(data);
			startTask(data);
			if(data.getType() == TaskType.BoxCtrlTask)
			{
				GlobalField.systemErr = 1;
			}
		}
	}
	
	public static void cleanTimerTask(TaskData data) 
	{
        if (data.getTask() != null) {
        	data.getTask().cancel();
        	data.setTask(null);
        }
        if (data.getTimer() != null) {
        	data.getTimer().cancel();
        	data.getTimer().purge();
        	data.setTimer(null);
        }
	}
}
