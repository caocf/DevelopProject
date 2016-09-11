package com.ebox.mgt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.pub.boxctl.RackInfo;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;

public class BoxManageActivity extends CommonActivity implements OnClickListener{
	private Button bt_box1;
	private Button bt_box2;
	private Button bt_box3;
	private Button bt_box4;
	private Button bt_box5;
	private Button bt_box6;
	private Button bt_box7;
	private Button bt_box8;
	private Button bt_box9;
	private Button bt_box10;
	private Button bt_box11;
	private Button bt_box12;
	private Button bt_box13;
	private Button bt_box14;
	private Button bt_box15;
	private Button bt_box16;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mgt_activity_box_manage);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		initView();
	}
	
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.mgt_box_manage);
		data.tvVisibility=true;
		title.setData(data, this);
	}
	
	private void initView()
	{
		bt_box1 = (Button) findViewById(R.id.bt_box1);
		bt_box1.setOnClickListener(this);
		bt_box2 = (Button) findViewById(R.id.bt_box2);
		bt_box2.setOnClickListener(this);
		bt_box3 = (Button) findViewById(R.id.bt_box3);
		bt_box3.setOnClickListener(this);
		bt_box4 = (Button) findViewById(R.id.bt_box4);
		bt_box4.setOnClickListener(this);
		bt_box5 = (Button) findViewById(R.id.bt_box5);
		bt_box5.setOnClickListener(this);
		bt_box6 = (Button) findViewById(R.id.bt_box6);
		bt_box6.setOnClickListener(this);
		bt_box7 = (Button) findViewById(R.id.bt_box7);
		bt_box7.setOnClickListener(this);
		bt_box8 = (Button) findViewById(R.id.bt_box8);
		bt_box8.setOnClickListener(this);
		bt_box9 = (Button) findViewById(R.id.bt_box9);
		bt_box9.setOnClickListener(this);
		bt_box10 = (Button) findViewById(R.id.bt_box10);
		bt_box10.setOnClickListener(this);
		bt_box11 = (Button) findViewById(R.id.bt_box11);
		bt_box11.setOnClickListener(this);
		bt_box12 = (Button) findViewById(R.id.bt_box12);
		bt_box12.setOnClickListener(this);
		bt_box13 = (Button) findViewById(R.id.bt_box13);
		bt_box13.setOnClickListener(this);
		bt_box14 = (Button) findViewById(R.id.bt_box14);
		bt_box14.setOnClickListener(this);
		bt_box15 = (Button) findViewById(R.id.bt_box15);
		bt_box15.setOnClickListener(this);
		bt_box16 = (Button) findViewById(R.id.bt_box16);
		bt_box16.setOnClickListener(this);
		
		bt_box1.setText("01"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box1.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box1.setClickable(false);
		bt_box2.setText("02"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box2.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box2.setClickable(false);
		bt_box3.setText("03"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box3.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box3.setClickable(false);
		bt_box4.setText("04"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box4.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box4.setClickable(false);
		bt_box5.setText("05"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box5.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box5.setClickable(false);
		bt_box6.setText("06"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box6.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box6.setClickable(false);
		bt_box7.setText("07"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box7.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box7.setClickable(false);
		bt_box8.setText("08"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box8.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box8.setClickable(false);
		bt_box9.setText("09"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box9.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box9.setClickable(false);
		bt_box10.setText("10"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box10.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box10.setClickable(false);
		bt_box11.setText("11"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box11.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box11.setClickable(false);
		bt_box12.setText("12"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box12.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box12.setClickable(false);
		bt_box13.setText("13"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box13.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box13.setClickable(false);
		bt_box14.setText("14"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box14.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box14.setClickable(false);
		bt_box15.setText("15"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box15.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box15.setClickable(false);
		bt_box16.setText("16"+getResources().getString(R.string.ex_rack)
				+getResources().getString(R.string.mgt_disconnectd));
		bt_box16.setBackgroundResource(R.drawable.pub_code_btn_disabled);
		bt_box16.setClickable(false);
		
		RackInfo rack;
		if(GlobalField.boxInfoLocal != null)
		{
			for(int i = 0 ; i < GlobalField.boxInfoLocal.size(); i++)
			{
				rack = GlobalField.boxInfoLocal.get(i);
				if(rack.getBoardNum() == 1)
				{
					bt_box1.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box1.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box1.setClickable(true);
				}
				else if(rack.getBoardNum() == 2)
				{
					bt_box2.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box2.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box2.setClickable(true);
				}
				else if(rack.getBoardNum() == 3)
				{
					bt_box3.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box3.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box3.setClickable(true);
				}
				else if(rack.getBoardNum() == 4)
				{
					bt_box4.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box4.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box4.setClickable(true);
				}
				else if(rack.getBoardNum() == 5)
				{
					bt_box5.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box5.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box5.setClickable(true);
				}
				else if(rack.getBoardNum() == 6)
				{
					bt_box6.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box6.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box6.setClickable(true);
				}
				else if(rack.getBoardNum() == 7)
				{
					bt_box7.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box7.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box7.setClickable(true);
				}
				else if(rack.getBoardNum() == 8)
				{
					bt_box8.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box8.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box8.setClickable(true);
				}
				else if(rack.getBoardNum() == 9)
				{
					bt_box9.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box9.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box9.setClickable(true);
				}
				else if(rack.getBoardNum() == 10)
				{
					bt_box10.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box10.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box10.setClickable(true);
				}
				else if(rack.getBoardNum() == 11)
				{
					bt_box11.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box11.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box11.setClickable(true);
				}
				else if(rack.getBoardNum() == 12)
				{
					bt_box12.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box12.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box12.setClickable(true);
				}
				else if(rack.getBoardNum() == 13)
				{
					bt_box13.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box13.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box13.setClickable(true);
				}
				else if(rack.getBoardNum() == 14)
				{
					bt_box14.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box14.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box14.setClickable(true);
				}
				else if(rack.getBoardNum() == 15)
				{
					bt_box15.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box15.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box15.setClickable(true);
				}
				else if(rack.getBoardNum() == 16)
				{
					bt_box16.setText(String.format("%02d",rack.getBoardNum())
							+getResources().getString(R.string.ex_rack)
							+getResources().getString(R.string.mgt_normal)
							+rack.getCount()
							+getResources().getString(R.string.ex_door));
					bt_box16.setBackgroundResource(R.drawable.pub_code_btn_blue);
					bt_box16.setClickable(true);
				}
			}
		}
		initTitle();
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


	private void showBoxStatus(int boardNum)
	{
		Intent intent = new Intent(BoxManageActivity.this, BoxStatusActivity.class);
		intent.putExtra("boardNum", boardNum);
        startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_box1:
			showBoxStatus(1);
			break;
		case R.id.bt_box2:
			showBoxStatus(2);
			break;
		case R.id.bt_box3:
			showBoxStatus(3);
			break;
		case R.id.bt_box4:
			showBoxStatus(4);
			break;
		case R.id.bt_box5:
			showBoxStatus(5);
			break;
		case R.id.bt_box6:
			showBoxStatus(6);
			break;
		case R.id.bt_box7:
			showBoxStatus(7);
			break;
		case R.id.bt_box8:
			showBoxStatus(8);
			break;
		case R.id.bt_box9:
			showBoxStatus(9);
			break;
		case R.id.bt_box10:
			showBoxStatus(10);
			break;
		case R.id.bt_box11:
			showBoxStatus(11);
			break;
		case R.id.bt_box12:
			showBoxStatus(12);
			break;
		case R.id.bt_box13:
			showBoxStatus(13);
			break;
		case R.id.bt_box14:
			showBoxStatus(14);
			break;
		case R.id.bt_box15:
			showBoxStatus(15);
			break;
		case R.id.bt_box16:
			showBoxStatus(16);
			break;
		}
	}
	


	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
