package com.ebox.ex.ui.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.pub.boxctl.BoxInfo;
import com.ebox.pub.boxctl.BoxOp;
import com.ebox.pub.boxctl.BoxOpId;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.service.task.BoxCtrlTask;
import com.ebox.pub.utils.Dialog;
import com.ebox.pub.utils.Dialog.onClickListener;

public class BoxBar extends RelativeLayout implements onClickListener{
	private Button bt_box;
	private Context context;
	private BoxInfo box;
	
	private void initViews(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_box_bar, this, 
				true);
		bt_box = (Button)findViewById(R.id.bt_box);
	}
	
	public BoxBar(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public BoxBar(Context context) {
		super(context);
		initViews(context);
	}
	
	public void setData(BoxInfo box)
	{
		this.box = box;
		// 文字显示
		String state = "";
		if(box.getDoorState() == 0)
		{
			state = AppApplication.getInstance().getResources().getString(R.string.ex_box_state_close);
			bt_box.setBackgroundResource(R.drawable.pub_code_btn_blue);
			bt_box.setClickable(true);
		}
		else if(box.getDoorState() == 1)
		{
			state = AppApplication.getInstance().getResources().getString(R.string.ex_box_state_open);
			bt_box.setBackgroundResource(R.drawable.pub_code_btn_disabled);
			bt_box.setClickable(false);
		}
		else
		{
			state = AppApplication.getInstance().getResources().getString(R.string.ex_box_state_unknow);
			bt_box.setBackgroundResource(R.drawable.pub_code_btn_disabled);
			bt_box.setClickable(false);
		}
		
		// 是否有快递
		if(OrderLocalInfoOp.getOrderByBoxCode(BoxUtils.generateBoxCode(box.getBoardNum(), box.getBoxNum())) != null)
		{
			state = state+AppApplication.getInstance().getResources().getString(R.string.ex_has_item);
		}
		
		bt_box.setText(AppApplication.getInstance().getResources().getString(R.string.ex_box_state_show, 
				box.getBoxNum(), state));
		bt_box.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Dialog(context, 
						getResources().getString(R.string.mgt_sure_to_open), 
						BoxBar.this, 
						null);
			}
			});
	}

	@Override
	public void onOk(Object value) {
		Log.i(GlobalField.tag, "openDoor:"+box.getBoardNum()+box.getBoxNum());
		// 开门
		BoxOp op =  new BoxOp();
		op.setOpId(BoxOpId.OpenDoor);
		op.setOpBox(box);
		BoxCtrlTask.addBoxCtrlQueue(op);
	}

	@Override
	public void onCancel(Object value) {
	}
}
