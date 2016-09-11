package com.ebox.pub.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ebox.ex.network.model.base.type.ServiceConfig;
import com.ebox.pub.file.TempFile;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.TaskData;
import com.ebox.pub.service.task.TaskManager;
import com.ebox.pub.service.task.TaskType;

public class AppService extends Service {
	public static final String TAG = "AppService";
	private static AppService appManager;
	private final IBinder mBinder = new LocalBinder();

	private TaskManager mTaskManager;
	private boolean taskInit = false;
	
	public boolean sysIdle = false;
	public int noKeyCount = 0;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	public static AppService getIntance() {
		return appManager;
	}
	
	public class LocalBinder extends Binder {
		public AppService getService() {
			return AppService.this;
		}
	}
	
	//在服务中开启相应的任务
	private void startTask()
	{
		mTaskManager=new TaskManager();

		//开启主副板操作任务
		mTaskManager.addTask(new TaskData(TaskType.BoxCtrlTask, 1000, 60));
		
		//开启自动上报任务
		mTaskManager.addTask(new TaskData(TaskType.AutoReportTask, 1000, 60));

		//开启屏保的任务
		mTaskManager.addTask(new TaskData(TaskType.ScreenProtectTask, 1000, 60));

		//开启照片上传任务
		mTaskManager.addTask(new TaskData(TaskType.PicQueueTask, 60 * 1000, 5));

		//开启系统自检查任务
		mTaskManager.addTask(new TaskData(TaskType.CheckTask, 10 * 1000, 12));

		//开启websocket的任务
	//	mTaskManager.addTask(new TaskData(TaskType.WebSocketTask, 10*1000, 60));

		//开启二维码检测任务
	//	mTaskManager.addTask(new TaskData(TaskType.CheckQrCodeTask, 60 * 1000, 60));


		taskInit = true;
	}
	
	
	//检查任务是否超时   如果超时重新启动任务
	public void checkTask()
	{
		Log.i(TAG,"checkTask");
		
		if(taskInit)
		{
			mTaskManager.checkAllTasks();
		}
	}
	
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
          public void onReceive(Context context, Intent intent) {
              String action = intent.getAction();
                if (action.equals(Intent.ACTION_TIME_TICK)) {
                	checkTask();
                }
          }
    };
    
    private final BroadcastReceiver timerReceiver = new BroadcastReceiver() {
        @Override
          public void onReceive(Context context, Intent intent) {
              String action = intent.getAction();
                if (action.equals("com.ebox.timerreset")) {
                	hasOnKeyDown();
                }
          }
    };
	
	@Override
	public void onCreate() {
		super.onCreate();
		appManager = this;
		Log.e(TAG,"onCreate()");
		startTask();

		// 读取系统配置,读取不到按默认10分钟
		GlobalField.serverConfig = new ServiceConfig();
		GlobalField.serverConfig.setHeartbeat(10);
		GlobalField.serverConfig.setRebootDay(10);
		GlobalField.serverConfig.setIsAccount(TempFile.getTemp().getIs_account());
		GlobalField.serverConfig.setpickupType(0);
		GlobalField.serverConfig.setIsOpenGeGe(0);
		GlobalField.serverConfig.setScanPickup(0);
		
		IntentFilter filter=new IntentFilter();
		filter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(receiver,filter);
		
		IntentFilter filter1=new IntentFilter();
		filter1.addAction("com.ebox.timerreset");
		registerReceiver(timerReceiver,filter1);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//close service atuo start
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		Log.e(TAG, "onDestroy()");
		appManager = null;
		//清理所有任务
		mTaskManager.clearAllTasks();
	}
	
	public int getTimeoutLeft()
	{
		return GlobalField.screenProtectTimes-noKeyCount;
	}
	
	public void hasOnKeyDown()
	{
		noKeyCount = 0;
		sysIdle = false;
	}
	
	public void setTimeoutLeft(int num)
	{
		noKeyCount = GlobalField.screenProtectTimes-num;
	}
	
	public void setNoKeyCount(int num)
	{
		noKeyCount = num;
	}
	
	public boolean isSysIdle()
	{
		return sysIdle;
	}
	
}
