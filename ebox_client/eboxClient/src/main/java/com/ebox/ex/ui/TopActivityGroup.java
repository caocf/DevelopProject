package com.ebox.ex.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.bar.Logo;

public class TopActivityGroup extends CommonActivity{


	private void getResolution() {
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		float width = displayMetrics.widthPixels;//得到宽度
		float height = displayMetrics.heightPixels;//得到高度

		if (width == 1024 && height == 1280)
		{
			Log.d("screen:", "55寸 width:" + width + " height:" + height);
			AppApplication.getInstance().is55Screen = true;
		}else {
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//判断屏幕尺寸
		getResolution();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		try {
			Logo logo = (Logo)findViewById(R.id.logo);
			if (logo != null) {
                logo.initLogo();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	// 页面上需重置数据，子类需保存时，重写
	public void resetMyself()
	{

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
	}
}
