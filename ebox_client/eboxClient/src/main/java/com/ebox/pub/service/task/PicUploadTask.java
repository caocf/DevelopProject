package com.ebox.pub.service.task;

import com.ebox.pub.camera.OrderPicture;
import com.ebox.pub.service.task.images.ImageUploadHelper;
import com.ebox.pub.utils.LogUtil;

import java.io.File;
import java.util.Calendar;
import java.util.TimerTask;

public class PicUploadTask extends TimerTask {
	private TaskData data;
	private boolean start = false;
	public PicUploadTask(TaskData data)
	{
		this.data = data;
	}
	@Override
	public void run() {
		data.setLastRunning(System.currentTimeMillis());
		//AppService.getIntance().checkTask();
		//Log.e("Timer", "set mPicQueueRunning:"+AppService.getIntance().mPicQueueRunning);

		// 凌晨1~8点上传
		Calendar c = Calendar.getInstance();
		if (c.get(Calendar.HOUR_OF_DAY) >= 1
				&& c.get(Calendar.HOUR_OF_DAY) <= 8)
		{
			uploadImage();
		}else
		{
			ImageUploadHelper.instance().stop();
		}
		//uploadImage();

	}

	private void uploadImage() {
		if (!start)
		{
			start=true;
			File files = OrderPicture.getUploadDir();

			ImageUploadHelper.instance().files(files).listener(new ImageUploadHelper.uploadFinishListener()
			{
				@Override
				public void uploadFinish()
				{
					start = false;
					LogUtil.i("All  photo files upload finish");
				}

				@Override
				public void nullStop() {
					LogUtil.i("response null stop upload ");
					start=false;
				}
			}).start();
		}
	}
}
