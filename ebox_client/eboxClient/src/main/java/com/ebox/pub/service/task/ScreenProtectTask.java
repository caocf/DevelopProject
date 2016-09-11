package com.ebox.pub.service.task;

import java.util.TimerTask;

import android.content.Intent;

import com.ebox.AppApplication;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.ui.TopActivityGroup;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;

public class ScreenProtectTask extends TimerTask{
	private TaskData data;
	
	public ScreenProtectTask(TaskData data)
	{
		this.data = data;
	}
	@Override
	public void run() {
		data.setLastRunning(System.currentTimeMillis());
//		AppService.getIntance().checkTask();
		//Log.e("Timer", "set mScreenProtectLastRunning:"+AppService.getIntance().mScreenProtectLastRunning);
		AppService.getIntance().noKeyCount++;
		if(AppService.getIntance().noKeyCount >= GlobalField.screenProtectTimes)
		{
			AppService.getIntance().noKeyCount = 0;
			AppService.getIntance().sysIdle = true;
			
//			final TopActivity topActivity = TopActivity.topActivity;
//			if (topActivity!=null)
//			{
//				topActivity.runOnUiThread(
//						new Runnable() {
//							@Override
//							public void run() {
//								topActivity.resetMyself();
//							}
//						});
//			}
			for(int i=(AppApplication.getInstance().activityList.size()-1); i >= 0; i--)
			{
				if((AppApplication.getInstance().activityList.get(i) instanceof TopActivityGroup))
				{
					final TopActivityGroup top = ((TopActivityGroup) AppApplication.getInstance().activityList.get(i));
					top.runOnUiThread(
							new Runnable() {
								@Override
								public void run() {
									top.resetMyself();
								}
							});
					continue;
				}
				
				if (AppApplication.getInstance().activityList.get(i) instanceof CommonActivity) {
					((CommonActivity) AppApplication.getInstance().activityList.get(i)).saveParams();
				}
				AppApplication.getInstance().activityList.get(i).finish();
			}
			
//			if(!ScreenProtectActivity.isScreening)
//			{
//				Intent intent = new Intent(AppApplication.globalContext, 
//						ScreenProtectActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//				AppApplication.globalContext.startActivity(intent);
//			}
			
			Intent intent = new Intent();
		    intent.setAction("com.ebox.ScreenProtect");
		    AppApplication.globalContext.sendBroadcast(intent);
			
			AppApplication.getInstance().systemErrRestart();
		}
	}
}
