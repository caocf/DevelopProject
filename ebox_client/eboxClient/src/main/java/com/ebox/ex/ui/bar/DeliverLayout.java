package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.database.deliver.Deliver;
import com.ebox.ex.database.deliver.DeliverOp;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.network.model.RspUpdateOperatorBalance;
import com.ebox.ex.network.model.base.type.TerminalActivity;
import com.ebox.ex.network.model.enums.AcountType;
import com.ebox.ex.network.request.RequestUpdateOperatorInfo;
import com.ebox.ex.ui.DeliveryActivity;
import com.ebox.ex.utils.ActiveUtil;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.ui.WebPageActivity;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.ImageLoaderUtil;
import com.ebox.pub.utils.RingUtil;
import com.ebox.pub.utils.Tip;

import java.util.ArrayList;

public class DeliverLayout extends OperMainLayout implements OnClickListener{
	private Button bt_ok;
	private EditText et_barcode;
	private TextView tv_balance;
	private Button btn_get_balance;
	private DialogUtil dialogUtil;
	private LinearLayout rl_top;
	private Tip tip;
	private Context context;
	private Handler runHandler;
	private Runnable run;
	private int loginType;//登录状态  0：离线登录，1：联网登录
	private ImageView iv_gif;
	private TerminalActivity activity;
    private String operatorId;
	
	private void initViews(final Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_m_deliver_layout, this, 
				true);
		bt_ok = (Button)findViewById(R.id.bt_ok);
		rl_top= (LinearLayout) findViewById(R.id.rl_top);
		et_barcode = (EditText)findViewById(R.id.et_barcode);
		tv_balance = (TextView)findViewById(R.id.tv_balance);
		btn_get_balance = (Button) findViewById(R.id.btn_get_balance);
		btn_get_balance.setOnClickListener(this);
		KeyboardUtil.hideInput((Activity) context, et_barcode);
		et_barcode.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				new KeyboardUtil(DeliverLayout.this, context,
						et_barcode).showKeyboard();
				return false;
			}
		});
		new KeyboardUtil(DeliverLayout.this, context, 
				et_barcode).showKeyboard();  
		et_barcode.requestFocus();
		bt_ok.setOnClickListener(this);
		iv_gif = (ImageView)findViewById(R.id.iv_gif);
		activity = ActiveUtil.getOperatorActive();
		if(activity != null)
		{
			iv_gif.setVisibility(View.VISIBLE);
			ImageLoaderUtil.instance().displayImage(activity.getContent(), iv_gif);
			iv_gif.setOnClickListener(this);
		}
		else 
		{
			iv_gif.setVisibility(View.GONE);
		}
		// 单号最多30位
		EditTextUtil.limitCount(et_barcode, 30, null);
		
		// 遇到回车自动访问服务端
		et_barcode.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.KEYCODE_ENTER == keyCode && event.getAction() == KeyEvent.ACTION_DOWN) {
					RingUtil.playRingtone(RingUtil.scan_success_id);
					CheckItem();
					return true;
				}
				return false;
			}
		});
		showBalance();
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog((Activity)context);
		// 播放音效
		RingUtil.playRingtone(RingUtil.scan_id);
		
	}
	
	public DeliverLayout(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}
	

	public DeliverLayout(Context context,int type,String operatorId) {
		super(context);
		this.loginType = type;
        this.operatorId = operatorId;
		initViews(context);
	}
	
	public void onResume()
	{
		showBalance();
	}

	public void showBalance() {
		int isAccount = GlobalField.serverConfig.getIsAccount();
		if (isAccount==AcountType.is_acount && loginType == 1)
		{
			tv_balance.setText((OperatorHelper.getOperatorBalance2St(operatorId)));
			btn_get_balance.setVisibility(View.GONE);
		}
        else if(isAccount==AcountType.is_acount &&loginType == 0) {
			tv_balance.setText("---- ");
			btn_get_balance.setVisibility(View.VISIBLE);
		}
        else {
			rl_top.setVisibility(View.INVISIBLE);
			tv_balance.setVisibility(View.INVISIBLE);
			btn_get_balance.setVisibility(View.GONE);
		}
	}
	
	public void saveParams() {
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	
	public void resetMyself()
	{
		et_barcode.setText("");
		et_barcode.requestFocus();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.btn_get_balance:
			updateOperatorInfo();
			break;
		case R.id.bt_ok:
			CheckItem();
			break;
		case R.id.iv_gif:
		{
			ArrayList<Deliver> list = DeliverOp.getAllDeliverByOperator(operatorId);
			int item_count = 0;
			
			if(list != null)
			{
				item_count = list.size();
			}
			Intent intent;
			intent=new Intent(context, WebPageActivity.class);
			intent.putExtra("web_url", activity.getUrl() + "?operator="
					+ operatorId
					+ "&item_count=" + item_count + "&terminal_code="
					+ AppApplication.getInstance().getTerminal_code());
			intent.putExtra("title", activity.getName());
			context.startActivity(intent);
			break;
		}
		}
	}
	
	private boolean checkPara()
	{
		if(et_barcode.getText() == null ||
				et_barcode.getText().toString().length() == 0)
		{
			tip = new Tip((Activity)context, 
					getResources().getString(R.string.ex_input_barcode),
				null);
			tip.show(0);
			return false;
		}
		
		return true;
	}
	
	private void CheckItem()
	{
		if(checkPara())
		{
			Log.i(GlobalField.tag, "ScanBarcode item id:"+et_barcode.getText().toString());
			dialogUtil.showProgressDialog();
			runHandler = new Handler();
	        run = new Runnable() 
	        {
	            public void run()
	            {
	            	Intent intent = new Intent((Activity)context, DeliveryActivity.class);
	    			intent.putExtra("barcode", et_barcode.getText().toString());
                    intent.putExtra("operatorId",operatorId);
	    			((Activity)context).startActivity(intent);
	    			
	    			et_barcode.setText("");
	    			et_barcode.requestFocus();
	            	runHandler = null;
	            	dialogUtil.closeProgressDialog();
	            }
	        };
	        runHandler.postDelayed(run, 200);
		}
	}

	private void updateOperatorInfo() {
		dialogUtil.showProgressDialog();

		RequestUpdateOperatorInfo request=new RequestUpdateOperatorInfo(operatorId,new ResponseEventHandler<RspUpdateOperatorBalance>() {
			
			@Override
			public void onResponseSuccess(RspUpdateOperatorBalance result) {
				dialogUtil.closeProgressDialog();
				//设置为在线。。
				loginType = 1;
				if (result.isSuccess())
				{
					Long balance = result.getData().getBalance();

					OperatorOp.updateOperatorBalance(operatorId, balance, 1);

					showBalance();
				} else {
					tip = new Tip((Activity)context,
							result.getMsg(),
							null);
					tip.show(0);
				}
			}
			
			@Override
			public void onResponseError(VolleyError error) {
				dialogUtil.closeProgressDialog();
				tip = new Tip((Activity)context,
						getResources().getString(R.string.pub_connect_failed),
						null);
				tip.show(0);
			}
		});
		
		RequestManager.addRequest(request, null);
	}

}

