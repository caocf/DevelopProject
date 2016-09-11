package com.ebox.st.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.ui.lic.LicMainActivity;

public class LicenceMainActivity extends CommonActivity implements OnClickListener{
	private Button st_btn_jhsy;
	private Button st_btn_lnyd;
	private Button st_btn_login;
	private Tip tip;
	
	private Title title;
	private TitleData titleData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RingUtil.playRingtone(RingUtil.st004);
		setContentView(R.layout.st_fg_licence_main);
		MGViewUtil.scaleContentView(this, R.id.rootView);
		initView();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.handle_business_licenses);
		title.setData(titleData, this);
		
		st_btn_jhsy = (Button) findViewById(R.id.st_btn_jhsy);
		st_btn_jhsy.setOnClickListener(this);
		st_btn_lnyd = (Button) findViewById(R.id.st_btn_lnyd);
		st_btn_lnyd.setOnClickListener(this);
		st_btn_login = (Button) findViewById(R.id.st_btn_login);
		st_btn_login.setOnClickListener(this);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		title.showTimer();
	}
	@Override
	public void onPause() {
		super.onPause();
		title.stopTimer();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.st_btn_jhsy:
		{
			Intent intent = new Intent(this, LicMainActivity.class);
			intent.putExtra("work_type", WorkType.Jhsy);
			startActivity(intent);
			break;
		}
		case R.id.st_btn_lnyd:
		{
			Intent intent = new Intent(this, LicMainActivity.class);
			intent.putExtra("work_type", WorkType.Lnyd);
			startActivity(intent);
			break;
		}
		case R.id.st_btn_login:
			startActivity(new Intent(this,LoginActivity.class));
			break;
		}
	}
	
	
}
