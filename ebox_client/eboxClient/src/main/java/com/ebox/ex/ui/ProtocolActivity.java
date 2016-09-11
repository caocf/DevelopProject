package com.ebox.ex.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.utils.MGViewUtil;

public class ProtocolActivity extends CommonActivity implements OnClickListener{
	private TextView tv_title;
	private Button bt_agree;
	private Button bt_refuse;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ex_activity_protocol);
        MGViewUtil.scaleContentView(this, R.id.rootView);
		bt_agree = (Button) findViewById(R.id.bt_agree);
		bt_refuse = (Button) findViewById(R.id.bt_refuse);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.getPaint().setFakeBoldText(true);
		 
		bt_agree.setOnClickListener(this);
		bt_refuse.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_agree:
			Intent intent = new Intent(ProtocolActivity.this, OperatorHomeActivity.class);
	        startActivity(intent);
	        finish();
			break;
		case R.id.bt_refuse:
			finish();
			break;
		}
	}
}
