package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.pub.service.global.GlobalField;

/**
 * 扫码开柜的倒计时显示 xiash
 * 
 * @author prin
 * 
 */
public class ScanTimeLeftBar {

	private TextView time;
	private Button enterBT;
	private Activity activity;
	private Handler handler;
	private static Runnable run;
	private int leftTimer = 0;
	public boolean isShow=false;

	public ScanTimeLeftBar(TextView time, Activity activity, Button enterBT) {
		this.time = time;
		this.activity = activity;
		this.enterBT = enterBT;
		this.leftTimer = GlobalField.config.getScanTimer();
	}

	public void show() {
		isShow = true;
		time.setText(leftTimer + "");
		handler = new MyHandler();
		run = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				leftTimer--;
				if (time != null) {
					time.setText(leftTimer + "");

					if (leftTimer <= 0) {
						enterBT.setVisibility(View.VISIBLE);
						time.setVisibility(View.GONE);
						// activity.finish();
					}
					handler.sendEmptyMessageDelayed(0, 0);
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 0);

	}

	public void stop() {
		handler.removeCallbacks(run);
	}

	static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				this.postDelayed(run, 1000);
			}
		}

		// 自定义接口
		public interface ScanTimerOverListener {
			abstract void onTimerOver(TextView time);
		}

		public void setOnTimerOver(ScanTimerOverListener listener) {

		}

		public ScanTimerOverListener scanTimerOverListener;

	}

}
