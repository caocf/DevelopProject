package com.moge.ebox.phone.ui;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.config.GlobalConfig;
import com.moge.ebox.phone.ui.customview.loadEmptyView;
import com.moge.ebox.phone.ui.customview.loadView;
import com.moge.ebox.phone.utils.BitmapUtil;
import com.moge.ebox.phone.utils.LogUtil;
import com.moge.ebox.phone.utils.UmengUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class BaseActivity extends FragmentActivity {

	/**
	 * log TAG.
	 */
	private final String TAG = "activity";

	/**
	 * current activity name.
	 */
	private final String ACTIVITY_NAME = getClass().getSimpleName();
	/**
	 * application handler.
	 */
	protected Handler mHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.i(TAG, String.format("onCreate %s", ACTIVITY_NAME));

		Resources res = getResources();
		Configuration config = new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config, res.getDisplayMetrics());

		IntentFilter exitFilter = new IntentFilter();
		exitFilter.addAction(GlobalConfig.INTENT_ACTION_FINISH);
		registerReceiver(mExitReceiver, exitFilter);
	}

	private BroadcastReceiver mExitReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(GlobalConfig.INTENT_ACTION_FINISH)) {
				finish();
			}
		}
	};

	@SuppressWarnings("unchecked")
	public <T> T findviewById_(int id) {
		return ((T) findViewById(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.i(TAG, String.format("onStart %s", ACTIVITY_NAME));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		UmengUtil.statDurationStart(this);
		LogUtil.i(TAG, String.format("onResume %s", ACTIVITY_NAME));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		UmengUtil.statDurationEnd(this);
		LogUtil.i(TAG, String.format("onPause %s", ACTIVITY_NAME));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		LogUtil.i(TAG, String.format("onStop %s", ACTIVITY_NAME));

		// RequestManager.cancelAll(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(mExitReceiver);

		LogUtil.i(TAG, String.format("onDestroy %s", ACTIVITY_NAME));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		LogUtil.i(TAG, String.format("onRestart %s", ACTIVITY_NAME));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.FragmentActivity#onNewIntent(android.content.Intent
	 * )
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		LogUtil.i(TAG, String.format("onNewIntent %s", ACTIVITY_NAME));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		LogUtil.i(TAG, String.format("finalize %s", ACTIVITY_NAME));
		super.finalize();
	}

	public View getLoadFailedView() {
		return new loadEmptyView(this);
	}

	public View getLoadingView() {
		return new loadView(this);
	}

	// 头像文件路径
	public File getMyCachePicFile() {
		// 目录不存在就创建
		File dirPath = new File(getBaseCachePath());
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		return new File(getBaseCachePath(), getMyPicName());
	}

	public File getMyCachePicFile(String picName) {
		// 目录不存在就创建
		File dirPath = new File(getBaseCachePath());
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		return new File(getBaseCachePath(), picName +".jpg");
	}
	public Bitmap getMyCacheBitmap() {
		File file = getMyCachePicFile();
		Bitmap bitmap = BitmapUtil.readBitmap(file.toString());
		return bitmap;
	}

	public String getBaseCachePath() {
		return getSDPath() + "/com.moge.phone/cache";
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取根目录
		} else {
			Log.e("ERROR", "没有内存卡");
		}
		return sdDir.toString();
	}

	/**
	 * 显示图片
	 * 
	 * @param url
	 * @param imageView
	 */
	public void disPlayImage(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView);
	}

	public String getMyPicName() {
		String phone = EboxApplication.getInstance().getLoginPhone();
		return phone + ".jpg";
	}
	
	protected boolean isShow =false;
	public  void setBooleanIsShow(boolean isShow){
		this.isShow=isShow;
	}
	
}
