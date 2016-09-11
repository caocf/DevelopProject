package com.ebox.ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ebox.R;
import com.ebox.ex.database.boxDyn.BoxDynSyncOp;
import com.ebox.ex.network.model.enums.BoxUserState;
import com.ebox.ex.network.model.enums.DoorStatusType;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;

public class KuaidiActivity extends CommonActivity implements OnClickListener{
	private Button pick;
	private Button deliver;
	private Button query;
	private Tip tip;

	private Title title;
	private TitleData titleData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ex_activity_kuaidi);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		initView();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.ex_kuaidi);
		title.setData(titleData, this);
		
		pick = (Button) findViewById(R.id.pick);
		pick.setOnClickListener(this);
		deliver = (Button) findViewById(R.id.deliver);
		deliver.setOnClickListener(this);
		query = (Button) findViewById(R.id.query);
		query.setOnClickListener(this);
		
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
		case R.id.pick:
		{
			boolean state = readDoorState();
			if (state)
			{
				Intent intent = new Intent(this,CloseDoorActivity.class);
				startActivity(intent);
			}
			else
			{
				startActivity(new Intent(this,PickupActivity.class));
			}
			
			break;
		}
		case R.id.deliver:
		{
			boolean state = readDoorState();
			if (state)
			{
				Intent intent = new Intent(this,CloseDoorActivity.class);
				startActivity(intent);
			}
			else
			{
				startActivity(new Intent(this, OperatorLoginActivity.class));
			}
			break;
		}
		case R.id.query:
		{
			startActivity(new Intent(this, ItemQueryActivity.class));
			break;
		}
		}
	}
	
	// 读取数据库缓存的门的信息
	private boolean readDoorState() {
		ArrayList<BoxInfo> list = BoxDynSyncOp.getAllSyncBoxList();
		if (list!=null&&list.size()>0)
		{
			for (BoxInfo boxInfo : list)
			{
				Integer doorState = boxInfo.getDoorState();
				if (boxInfo.getBoxUserState()==BoxUserState.normal)
				{
					if (doorState == DoorStatusType.open) 
					{
						return true;
					} 
				}
			}
		}
		return false;
	}
}
