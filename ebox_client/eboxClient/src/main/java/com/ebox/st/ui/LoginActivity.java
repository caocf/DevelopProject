package com.ebox.st.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

import com.ebox.AppApplication;
import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.network.model.enums.SysValueDefine;
import com.ebox.pub.service.global.Constants;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.ui.keyboard.KeyboardUtil.onKeyListener;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.FunctionUtils;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.model.WorkerLoginReq;
import com.ebox.st.model.WorkerLoginRsp;
import com.ebox.st.network.http.ApiClient;

public class LoginActivity extends CommonActivity implements
		OnClickListener, onKeyListener {
	private static final String TAG = "LoginFragment";
	private EditText et_username;
	private EditText et_verify;
	private Button bt_ok;
	private DialogUtil dialogUtil;
	private KeyboardUtil keyBoard;
	private Tip tip;
	private Context mContext;
	
	private Title title;
	private TitleData titleData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = LoginActivity.this;
		setContentView(R.layout.st_fg_zzsl_login);
        MGViewUtil.scaleContentView(this,R.id.rootView);
		initView();
        testData();
	}
	

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
	public void onDestroy() {
		super.onDestroy();
		dialogUtil.closeProgressDialog();
		if (tip != null) {
			tip.closeTip();
		}
	}

    private void testData()
    {
        if (Constants.DEBUG)
        {
            et_username.setText("123");
            et_verify.setText("123456");
        }
    }

	private void initView() {
		
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_login);
		title.setData(titleData, this);
	
		et_username = (EditText) findViewById(R.id.et_username);
		KeyboardUtil.hideInput(this, et_username);
		et_username.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v1, MotionEvent event) {
				keyBoard = new KeyboardUtil(LoginActivity.this, mContext, et_username);
				keyBoard.showKeyboard();
				keyBoard.setonKeyListener(LoginActivity.this);
				return false;
			}
		});
		keyBoard = new KeyboardUtil(this,mContext, et_username);
		keyBoard.showKeyboard();
		keyBoard.setonKeyListener(LoginActivity.this);

		et_verify = (EditText) findViewById(R.id.et_verify);
		KeyboardUtil.hideInput(this, et_verify);
		et_verify.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v1, MotionEvent event) {
				keyBoard = new KeyboardUtil(LoginActivity.this, mContext, et_verify);
				keyBoard.showKeyboard();
				keyBoard.setonKeyListener(null);
				return false;
			}
		});
		bt_ok = (Button) findViewById(R.id.bt_ok);

		// 用户名最多11位
		EditTextUtil.limitCount(et_username, 11, null);
		// 密码最多32位
		EditTextUtil.limitCount(et_verify, SysValueDefine.MAX_PASSWD_LEN, null);
		bt_ok.setOnClickListener(this);

		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog(this);

		et_username.requestFocus();

		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_ok:
			if (checkPara()) {
				LoginReq();
			}
			break;
		
		}
	}

	private void showPrompt(int resId) {
		tip = new Tip(LoginActivity.this, getResources().getString(
				resId), null);
		tip.show(0);
	}

	private boolean checkPara() {
		if (et_username.getText() == null
				|| et_username.getText().toString().length() == 0
				|| et_username.getText().toString().length() > SysValueDefine.MAX_OPERATOR_ID_LEN) {
			showPrompt(R.string.pub_username_error);
			return false;
		}

		if (et_verify.getText() == null
				|| et_verify.getText().toString().length() == 0
				|| et_verify.getText().toString().length() > SysValueDefine.MAX_PASSWD_LEN) {
			showPrompt(R.string.pub_passwd_error);
			return false;
		}

		return true;
	}
	
	private void LoginReq(){
		
		WorkerLoginReq req = new WorkerLoginReq();
		req.setTerminal_code(AppApplication.getInstance().getTerminal_code());
		req.setUsername(et_username.getText().toString());
		req.setPassword(FunctionUtils.getMD5Str(et_verify.getText().toString()));
		ApiClient.workerLogin(this, req, new ApiClient.ClientCallback() {
			@Override
			public void onSuccess(Object data) {
				dialogUtil.closeProgressDialog();
				WorkerLoginRsp rsp = (WorkerLoginRsp) data;
				if(rsp !=null && rsp.getSuccess())
				{
					Intent intent =new Intent(LoginActivity.this,LoginMainActivity.class);
					startActivity(intent);
					finish();
				}
				else
				{
					if (rsp.getMessage()!=null )
					{

						tip = new Tip(LoginActivity.this,rsp.getMessage(), null);
						tip.show(0);
					}else {
						tip = new Tip(LoginActivity.this,
								getResources().getString(R.string.pub_username_passwd_error), null);
						tip.show(0);
					}
				}
			}

			@Override
			public void onFailed(Object data) {
				dialogUtil.closeProgressDialog();
			}
		});
	
	}

	@Override
	public void onKey(int primaryCode) {
		if (et_username.getText() != null
				&& et_username.getText().toString().length() >= 11) {
			et_verify.requestFocus();
			keyBoard = new KeyboardUtil(LoginActivity.this,
					LoginActivity.this, et_verify);
			keyBoard.showKeyboard();
			keyBoard.setonKeyListener(null);
		}
	}
}
