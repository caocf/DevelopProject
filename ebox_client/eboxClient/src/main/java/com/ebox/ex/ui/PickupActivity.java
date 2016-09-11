package com.ebox.ex.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.ex.database.LocalOrder.OrderLocalInfoOp;
import com.ebox.ex.network.model.base.type.TerminalActivity;
import com.ebox.ex.network.model.enums.PickupType;
import com.ebox.ex.ui.bar.EboxKeyboard;
import com.ebox.ex.ui.fragment.FragmentAdvPickUp;
import com.ebox.ex.ui.fragment.OpDownloadFragment;
import com.ebox.ex.ui.fragment.ResultPickupFragment;
import com.ebox.ex.utils.ActiveUtil;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.QRImageUtil;
import com.ebox.pub.utils.Tip;

public class PickupActivity extends CommonActivity implements OnClickListener,EboxKeyboard.OnEboxKeyListener{
	private Context ctx;
    private Activity act;
	private EditText et_verify;
	private TextView et_verify1;
	private TextView et_verify2;
	private TextView et_verify3;
	private TextView et_verify4;
	private TextView et_verify5;
	private TextView et_verify6;
	private TextView tv_errortip;
	private RelativeLayout rl_verify;
	private Button bt_ok;
	private ImageView iv_app;
	private DialogUtil dialogUtil;
	private String verify[] = new String[6];
	private int focusIndex = 0;
	private EboxKeyboard mKeyboard;
	
	private OrderLocalInfo order;
	private Tip tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ex_activity_pickup);
        MGViewUtil.scaleContentView(this, R.id.rootView);
		initView();
		initPickActivity();
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

	private void error(){
		rl_verify.setBackgroundResource(R.drawable.pub_code_input_error);
		tv_errortip.setVisibility(View.VISIBLE);
	}

	private void right(){
		rl_verify.setBackgroundResource(R.drawable.pub_code_input_normal);
		tv_errortip.setVisibility(View.INVISIBLE);
	}

	private void initView()
	{
		ctx = this;  
        act = this;
		rl_verify = (RelativeLayout) findViewById(R.id.rl_verify);
		tv_errortip = (TextView) findViewById(R.id.tv_errortip);
		right();

		et_verify = (EditText) findViewById(R.id.et_verify);
		et_verify.setInputType(InputType.TYPE_NULL);

		mKeyboard = (EboxKeyboard) findViewById(R.id.keyboard);

		mKeyboard.setMkeyListener(this);

		mKeyboard.setEditText(et_verify);

		iv_app= (ImageView) findViewById(R.id.iv_code);
		Bitmap android_bitmap= QRImageUtil.createQRImage(OpDownloadFragment.APP_URL, BitmapFactory.decodeResource(getResources(), R.drawable.ex_icon_delivery_app));
		iv_app.setImageBitmap(android_bitmap);

		et_verify.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				s.delete(0, et_verify.getSelectionEnd());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				String num = s.toString();

				if (num.length() == 1) {

					verify[focusIndex] = num;

					if (focusIndex == 0) {
//						et_verify1.setText("•");
						et_verify1.setText(verify[focusIndex]);
					} else if (focusIndex == 1) {
//						et_verify2.setText("•");
						et_verify2.setText(verify[focusIndex]);
					} else if (focusIndex == 2) {
//						et_verify3.setText("•");
						et_verify3.setText(verify[focusIndex]);
					} else if (focusIndex == 3) {
//						et_verify4.setText("•");
						et_verify4.setText(verify[focusIndex]);
					} else if (focusIndex == 4) {
//						et_verify5.setText("•");
						et_verify5.setText(verify[focusIndex]);
					} else if (focusIndex == 5) {
//						et_verify6.setText("•");
						et_verify6.setText(verify[focusIndex]);
					}
					if (focusIndex <= 5) {
						focusIndex++;
					}
					if (focusIndex == 6) {
						focusIndex = 5;
						pick();
					}
					//Log.i("main", "focusIndex="+focusIndex);
				}
			}
		});

		findViewById(R.id.bt_help).setOnClickListener(this);
		findViewById(R.id.btn_query).setOnClickListener(this);

		et_verify1 = (TextView) findViewById(R.id.et_verify1);
		et_verify2 = (TextView) findViewById(R.id.et_verify2);
		et_verify3 = (TextView) findViewById(R.id.et_verify3);
		et_verify4 = (TextView) findViewById(R.id.et_verify4);
		et_verify5 = (TextView) findViewById(R.id.et_verify5);
		et_verify6 = (TextView) findViewById(R.id.et_verify6);

		bt_ok = (Button) findViewById(R.id.bt_ok);
		bt_ok.setOnClickListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);
		resetMyself();
		initTitle();
	}
	private Title title;
	private TitleData data;
	private void initTitle() {
		title=(Title) findViewById(R.id.title);
		data=title.new TitleData();
		data.backVisibility=1;
		data.tvContent=getResources().getString(R.string.ex_i_pickup);
		data.tvVisibility=true;
		title.setData(data, this);
	}

	private void initPickActivity() {
		// TODO Auto-generated method stub
		/**
		 * 判断是否显示用户取件活动
		 */
		TerminalActivity activity = ActiveUtil.getUserActive();
		Intent lastIntent = getIntent();
		boolean isShowed = lastIntent.getBooleanExtra("isActivity", false);
		if (activity != null && !isShowed)
		{
			String url = activity.getUrl();
			Intent intent = new Intent(this, DialogWebActivity.class);
			if (url.contains("?")) {
				intent.putExtra("web_url", url + "&terminal_code="
						+ AppApplication.getInstance().getTerminal_code());
			} else {
				intent.putExtra("web_url", url + "?terminal_code="
						+ AppApplication.getInstance().getTerminal_code());
			}

			intent.putExtra("title", activity.getName());
			startActivity(intent);
		}
	}


	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.bt_ok:
			{
				pick();
				break;
			}
			case R.id.bt_help:
			{
				startActivity(new Intent(this,HelpActivity.class));
				break;
			}case R.id.btn_query:
			{
				startActivity(new Intent(this,ItemQueryActivity.class));
				break;
			}
		}
	}

	private void pick() {
		if(checkPara())
		{
			String systemOk = AppApplication.getInstance().isSystemOk();
			if(!systemOk.equals("ok"))
			{
				tip = new Tip(PickupActivity.this,
						systemOk,
						null);
				tip.show(0);
				return;
			}
			PickupItem();
		}
	}
	
	private boolean checkPara()
	{
		if(verify[0].length() != 1 ||
				verify[1].length() != 1 ||
				verify[2].length() != 1 ||
				verify[3].length() != 1 ||
				verify[4].length() != 1 ||
				verify[5].length() != 1 )
		{
			showPrompt(R.string.ex_verify_error);
			return false;
		}
		
		return true;
	}
	
	private void showPrompt(int resId)
	{
		tip = new Tip(PickupActivity.this, 
				getResources().getString(resId),
				null);
		tip.show(0);
	}
	
	private void PickupItem()
	{
		String pwd=verify[0]+verify[1]+verify[2]+verify[3]+verify[4]+verify[5];

		order= OrderLocalInfoOp.getOrderByPwd(pwd);

		Log.i(GlobalField.tag, "search from local order by pwd:" +pwd);
		if (order !=null)
		{
			right();
			openDoor();
		}
		else
		{
//			tip = new Tip(this, "没有查询到您的快递", null);
//			tip.show(0);
			error();
			resetMyself();
		}
	}
	
	public void resetMyself()
	{
		et_verify1.setText("");
		et_verify2.setText("");
		et_verify3.setText("");
		et_verify4.setText("");
		et_verify5.setText("");
		et_verify6.setText("");
		verify[0] = "";
		verify[1] = "";
		verify[2] = "";
		verify[3] = "";
		verify[4] = "";
		verify[5] = "";
		focusIndex = 0;
	}
	
	private void openDoor()
	{
		resetMyself();

		Intent intent = new Intent(PickupActivity.this, PickupResultActivity.class);

		intent.putExtra("order",order);

		intent.putExtra(ResultPickupFragment.PICK_TYPE, PickupType.customer);

		startActivity(intent);

		this.finish();
	}

	@Override
	public void onKey(int primaryCode) {
		if(primaryCode == Keyboard.KEYCODE_DELETE)
		{
			if(focusIndex >= 5)
			{
				if(verify[5].length() >=  1)
				{
					verify[5]  =  "";
					et_verify6.setText("");
				}
				else
				{
					focusIndex--;
				}
			}
			if(focusIndex == 4)
			{
				if(verify[4].length() >=  1)
				{
					verify[4]  =  "";
					et_verify5.setText("");
				}
				else
				{
					focusIndex--;
				}
			}
			if(focusIndex == 3)
			{
				if(verify[3].length() >=  1)
				{
					verify[3]  =  "";
					et_verify4.setText("");
				}
				else
				{
					focusIndex--;
				}
			}
			if(focusIndex == 2)
			{
				if(verify[2].length() >=  1)
				{
					verify[2]  =  "";
					et_verify3.setText("");
				}
				else
				{
					focusIndex--;
				}
			}
			if(focusIndex == 1)
			{
				if(verify[1].length() >=  1)
				{
					verify[1]  =  "";
					et_verify2.setText("");
				}
				else
				{
					focusIndex--;
				}
			}
			if(focusIndex == 0)
			{
				if(verify[0].length() >=  1)
				{
					verify[0]  =  "";
					et_verify1.setText("");
				}
				else
				{
					
				}
			}
		}
		else if (primaryCode == Keyboard.KEYCODE_CANCEL) 
        {
			resetMyself();
        }
	}

	private void startAdvFragment() {

		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Fragment fragment = null;
		fragment = manager.findFragmentByTag("Adv_pick");
		if (fragment == null) {
			fragment = new FragmentAdvPickUp();
		}
		transaction.replace(R.id.rl_adv, fragment, "Adv_pick").commit();
	}
}
