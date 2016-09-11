package com.ebox.mgt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.utils.MGViewUtil;

public class CameraTestActivity extends CommonActivity{

	private CameraTest cp;
	private Button test;
	private Button bt_back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mgt_activity_camera_test);
        MGViewUtil.scaleContentView(this, R.id.rootView);

		AppApplication.getInstance().getCc().stop();

		cp = (CameraTest) findViewById(R.id.cp);
		test = (Button) findViewById(R.id.test);

		test.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				cp.takePicture();
			}

		});
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK,new Intent(CameraTestActivity.this,FirstInstallActivity.class));
				finish();
			}

		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		cp.stopCamera();
		super.onDestroy();
	}
}
