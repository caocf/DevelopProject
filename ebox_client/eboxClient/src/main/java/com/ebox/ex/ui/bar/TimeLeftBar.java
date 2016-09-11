package com.ebox.ex.ui.bar;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.ebox.pub.service.AppService;

public class TimeLeftBar {
	
	private TextView time;
	private Handler handler;
	private static Runnable run;

	public TimeLeftBar(TextView time)
	{
		this.time = time;
	}
	
	public void show()
	{
		if(AppService.getIntance() != null)
		{
			time.setText(AppService.getIntance().getTimeoutLeft()+"");
		}
		handler = new MyHandler();
		run = new Runnable() {
			@Override
			public void run() {
				if(time != null)
				{
					AppService i = AppService.getIntance();
					if (i!=null)
					{
						time.setText(i.getTimeoutLeft()+"");
					}
					handler.sendEmptyMessageDelayed(0, 0);
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 0);
	}
	
	public void stop()
	{
		handler.removeCallbacks(run);
	}
	
	static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				this.postDelayed(run, 1000);
			}
		}
	};
}
