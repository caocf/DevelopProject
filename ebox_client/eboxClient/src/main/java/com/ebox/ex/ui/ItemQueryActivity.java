package com.ebox.ex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.network.model.enums.SysValueDefine;
import com.ebox.ex.ui.bar.EboxKeyboard;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;

public class ItemQueryActivity extends CommonActivity implements OnClickListener
{
	private EditText et_item_query;
	private Button bt_ok;
	private DialogUtil dialogUtil;
	private EboxKeyboard mKeyboard;
	private Tip tip;
	private TextView tv_error_tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ex_activity_item_query);
        MGViewUtil.scaleContentView(this, R.id.rootView);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();

		if(tip != null)
		{
			tip.closeTip();
		}
	}

	private void initView()
	{

		et_item_query = (EditText) findViewById(R.id.et_item_query);
		et_item_query.setOnTouchListener(touchListener);
		tv_error_tip= (TextView) findViewById(R.id.tv_errortip);
		mKeyboard= (EboxKeyboard) this.findViewById(R.id.keyboard);
		et_item_query.requestFocus();
		mKeyboard.setEditText(et_item_query);
		mKeyboard.setNumberAudio(false);



		bt_ok = (Button) findViewById(R.id.bt_ok);

		// 单号最多32位
		EditTextUtil.limitCount(et_item_query, SysValueDefine.MAX_ITEM_ID_LEN, null);
		bt_ok.setOnClickListener(this);

		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		et_item_query.requestFocus();
		// 遇到回车自动访问服务端
		et_item_query.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN)
				{
					ItemQuery();
				}
				return false;
			}
		});
		initTitle();
	}

	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.ex_i_query);
		data.ivIcon=R.drawable.ex_icon_query;
		data.tvVisibility=true;
		title.setData(data, this);
	}


	View.OnTouchListener touchListener=new OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (view instanceof EditText){
				mKeyboard.setEditText((EditText) view);
				mKeyboard.setNumberAudio(true);
			}
			return false;
		}
	};

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_ok:

			if(checkPara())
			{
				ItemQuery();
			}
			break;
		}
	}

	private void showPrompt(int resId)
	{
		tip = new Tip(ItemQueryActivity.this,
				getResources().getString(resId),
				null);
		tip.show(0);
	}

	private boolean checkPara()
	{
		if(et_item_query.getText() == null ||
				et_item_query.getText().toString().length() == 0 ||
						et_item_query.getText().toString().length() > SysValueDefine.MAX_ITEM_ID_LEN)
		{
			showPrompt(R.string.ex_item_query);
			return false;
		}

		return true;
	}

	private void TSItemQuery()
	{
		showPrompt(R.string.ex_no_result);
	}

	//所有数据本地查询
	private void ItemQuery(){

		String phone = et_item_query.getText().toString();
		ArrayList<OrderLocalInfo> list = OrderLocalInfoOp.getAllOrderLocalsByMobilePhone(phone);

		if (list != null && list.size() > 0) {

			start(phone);

		} else {
			TSItemQuery();
		}
	}

	private void start(String  phone) {

		if (phone.length() == 0)
		{
			showPrompt(R.string.ex_no_result);
		} else {
			Intent intent = new Intent(ItemQueryActivity.this, ItemQueryRstActivity.class);
			intent.putExtra("phone", phone);
			startActivity(intent);
			resetMyself();
			this.finish();
		}
	}



	public void resetMyself()
	{
		et_item_query.setText("");
	}
}
