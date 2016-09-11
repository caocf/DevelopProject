package com.ebox.pub.utils;

import android.os.CountDownTimer;

public class TimeCount extends CountDownTimer {
	private TimeOutCallback callback;
	public TimeCount(long millisInFuture, long countDownInterval,TimeOutCallback l) {
		super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		this.callback = l;
		
	}
	@Override
	public void onFinish() {//计时完毕时触发
		this.callback.timeFinish();
	}
	@Override
	public void onTick(long millisUntilFinished){//计时过程显示
		this.callback.onTick(millisUntilFinished);
	}
	
	 public interface TimeOutCallback {
	        public void timeFinish();
	        public void onTick(long st);
	    }
}
