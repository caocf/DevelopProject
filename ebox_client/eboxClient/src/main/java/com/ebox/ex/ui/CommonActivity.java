package com.ebox.ex.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

import com.ebox.Anetwork.BaseRequest;
import com.ebox.Anetwork.RequestManager;
import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.network.model.enums.DotType;
import com.ebox.pub.service.AppService;
import com.ebox.pub.service.global.GlobalField;

public class CommonActivity extends Activity{

    public int mTheme = R.style.AppTheme_normal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            mTheme = getTheme(GlobalField.config.getTheme());
        } else {
            mTheme = savedInstanceState.getInt("theme");
        }

        setTheme(mTheme);

		super.onCreate(savedInstanceState);
		AppApplication.getInstance().addToActivityList(this);
		//systemUI Gone
//		SystemUIVisible();
	}
	@Override
	protected void onResume()
	{

        super.onResume();
        if (mTheme != getTheme(GlobalField.config.getTheme())) {
            reload();
        }
      //AppService.getIntance().hasOnKeyDown();

	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_close_enter,R.anim.activity_close_exit);
    }

    @Override
	protected void onDestroy() {
		super.onDestroy();
		AppApplication.getInstance().removeFromActivityList(this);
	}

	// 页面上需保存的数据，子类需保存时，重写
	public void saveParams()
	{

	}


    private int getTheme(int config)
    {
        int theme = R.style.AppTheme_normal;

        //恒喜主题1
        if(config == 1)
        {
            theme = R.style.AppTheme_hx01;
        }

        //恒喜主题2
        if(config == 2)
        {
            theme = R.style.AppTheme_hx02;
        }

        //银川主题
        if (GlobalField.config.getDot() == DotType.YINCHUAN)
        {
            theme=R.style.AppTheme_yc;
        }

        return theme;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("theme", mTheme);
    }

    protected void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
		if(AppService.getIntance() == null)
		{
			startService(new Intent(this, AppService.class));
		}
        AppService i = AppService.getIntance();
        if (null != i)
        {
            i.hasOnKeyDown();
        }
		return super.dispatchTouchEvent(ev);
	}

	public void SystemUIVisible(){
		int flag = this.getWindow().getDecorView().getSystemUiVisibility();
        int fullScreen = 0x8;
        if((flag & fullScreen) == 0) {
            this.getWindow().getDecorView().setSystemUiVisibility(flag | fullScreen);
        }
	}


    /**
     * 执行请求
     * @param request
     * @param
     */
    protected void executeRequest(BaseRequest request)
    {
        RequestManager.addRequest(request,null);
    }
    protected void executeRequest(BaseRequest request,Object o)
    {
        RequestManager.addRequest(request,o);
    }

}
