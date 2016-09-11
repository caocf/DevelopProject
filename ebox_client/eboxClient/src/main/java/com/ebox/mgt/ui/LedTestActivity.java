package com.ebox.mgt.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ebox.R;
import com.ebox.ex.network.model.req.ReqLedRefresh;
import com.ebox.ex.network.model.base.type.LedInfo;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.ledctrl.LedCtrl;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;

import java.util.ArrayList;
import java.util.List;

public class LedTestActivity extends CommonActivity implements OnClickListener, Runnable{
	private EditText et_led_test;
	private EditText et_startx;
	private EditText et_endx;
	private Button bt_save;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mgt_activity_led_test);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		title.showTimer();
	}

	@Override
	protected void onPause() {
		super.onPause();
		title.stopTimer();
	}
	
	private void initView()
	{
		et_led_test = (EditText) findViewById(R.id.et_led_test);
		et_startx = (EditText) findViewById(R.id.et_startx);
		et_endx = (EditText) findViewById(R.id.et_endx);
		bt_save = (Button) findViewById(R.id.bt_save);
		
		bt_save.setOnClickListener(this);
		
		et_startx.setText("0");
		et_endx.setText("63");
		et_led_test.setText("格格货栈，欢迎使用！");
		
		initTitle();
	}
	
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.mgt_led_test);
		data.tvVisibility=true;
		title.setData(data, this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_save:
		{
			new Thread(this).start();
			break;
		}
		}
	}

	@Override
	public void run() {
		String content = et_led_test.getText().toString();
		LedCtrl led = new LedCtrl();

		List<LedInfo> ledInfos = new ArrayList<LedInfo>();
		LedInfo ledInfo=new LedInfo();
		ledInfo = new LedInfo();
		ledInfo.setText(content);
		ledInfo.setStartx(Integer.valueOf(et_startx.getText().toString()));
		ledInfo.setEndx(Integer.valueOf(et_endx.getText().toString()));
		ledInfo.setStarty(0);
		ledInfo.setEndy(15);
		ledInfo.setIn(28);
		ledInfo.setOut(255);
		ledInfo.setSpeed(50);
		ledInfo.setShowTime(1);
		
		if(led.init())
		{
			ReqLedRefresh refreshReq = new ReqLedRefresh();
			refreshReq.setContent(ledInfos);
			
			if(!led.downloadText(refreshReq))
			{
				LedTestActivity.this.runOnUiThread(
						new Runnable() {
							@Override
							public void run() {
								Toast.makeText(LedTestActivity.this, "LED设置失败", Toast.LENGTH_LONG).show();
							}
						});
				
			}
			led.close();
		}
		else
		{
			LedTestActivity.this.runOnUiThread(
					new Runnable() {
						@Override
						public void run() {
							Toast.makeText(LedTestActivity.this, "LED连接失败", Toast.LENGTH_LONG).show();
						}
					});
			
			
		}
	}
}
