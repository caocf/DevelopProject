package com.ebox.st.ui.lic;

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
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.enums.WorkType;
import com.ebox.st.ui.IdCardActivity;

public class LicMainActivity extends CommonActivity implements OnClickListener{
	private Button st_btn_handle;
	private Button st_btn_zzlq;
	private Button st_btn_jdcx;
	private Tip tip;
	
	private Title title;
	private TitleData titleData;
	
	private String type;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.st_fg_lic_lnyd_main);
		MGViewUtil.scaleContentView(this, R.id.rootView);
        type = getIntent().getStringExtra("work_type");
        initView();
	}
	
	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		if(type.equals(WorkType.Jhsy))
		{
			titleData.tvContent = getResources().getString(R.string.jhsy_licenses);
		}
		else
		{
			titleData.tvContent = getResources().getString(R.string.st_lic_lnyd);
		}
		title.setData(titleData, this);
		
		st_btn_handle = (Button) findViewById(R.id.st_btn_handle);
		st_btn_handle.setOnClickListener(this);
		st_btn_zzlq = (Button) findViewById(R.id.st_btn_zzlq);
		st_btn_zzlq.setOnClickListener(this);
		st_btn_jdcx = (Button) findViewById(R.id.st_btn_jdcx);
		st_btn_jdcx.setOnClickListener(this);
		
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
		case R.id.st_btn_handle:
			dealIdcard();
			break;
		case R.id.st_btn_zzlq:
		{
			lqIdcard();
			break;
		}
		case R.id.st_btn_jdcx:
			cxIdcard();
			break;
		}
	}
	
	private void dealIdcard(){
        Intent intent = new Intent(this, IdCardActivity.class);
        intent.putExtra("idcard_type",IdCardActivity.type_4);
        intent.putExtra("work_type",type);
        startActivity(intent);
	
	}
	
	private void lqIdcard(){
        Intent intent = new Intent(this, IdCardActivity.class);
        intent.putExtra("idcard_type",IdCardActivity.type_8);
        intent.putExtra("work_type",type);
        startActivity(intent);
	}
	
	private void cxIdcard(){
        Intent intent = new Intent(this, IdCardActivity.class);
        intent.putExtra("idcard_type",IdCardActivity.type_7);
        intent.putExtra("work_type",type);
        startActivity(intent);
	}
	
}
