package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.database.operator.OperatorOp;
import com.ebox.ex.network.model.base.BaseRsp;
import com.ebox.ex.network.model.enums.SysValueDefine;
import com.ebox.ex.network.model.req.ReqChangePwd;
import com.ebox.ex.network.request.PwdChangeRequest;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.EditTextUtil;
import com.ebox.pub.utils.MD5Util;
import com.ebox.pub.utils.Tip;
import com.ebox.pub.utils.Tip.onDismissListener;

public class ChangePwdLayout extends OperMainLayout implements OnClickListener{
	private Context context;
	private EditText et_pwd;
	private EditText et_verify;
	private EditText et_verify_again;
	private Button bt_ok;
	private DialogUtil dialogUtil;
	private KeyboardUtil keyBoard;
	private Tip tip;
    private String operatorId;

	private void initViews(final Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_m_change_pwd_layout, this,
				true);
		initView();
	}
	
	public ChangePwdLayout(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public ChangePwdLayout(Context context,String operatorId) {
		super(context);
        this.operatorId = operatorId;
		initViews(context);
	}
	
	public void saveParams() {
		dialogUtil.closeProgressDialog();
		
		if(tip != null)
		{
			tip.closeTip();
		}
	}
	private void initView()
	{
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		KeyboardUtil.hideInput((Activity)context, et_pwd);
		et_pwd.setOnTouchListener(new OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
            		keyBoard = new KeyboardUtil(ChangePwdLayout.this, (Activity)context, 
            				et_pwd);
            		keyBoard.showKeyboard();  
                    return false;  
            }  
		});
		keyBoard = new KeyboardUtil(ChangePwdLayout.this, (Activity)context, 
				et_pwd);
		keyBoard.showKeyboard();
		et_pwd.requestFocus();
		
		et_verify = (EditText) findViewById(R.id.et_verify);
		KeyboardUtil.hideInput((Activity)context, et_verify);
		et_verify.setOnTouchListener(new OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
            		keyBoard = new KeyboardUtil(ChangePwdLayout.this, (Activity)context, 
                    		et_verify);
            		keyBoard.showKeyboard();  
            		keyBoard.setonKeyListener(null);
                    return false;  
            }  
		});  
		
		et_verify_again = (EditText) findViewById(R.id.et_verify_again);
		KeyboardUtil.hideInput((Activity)context, et_verify_again);
		et_verify_again.setOnTouchListener(new OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
            		keyBoard = new KeyboardUtil(ChangePwdLayout.this, (Activity)context, 
            				et_verify_again);
            		keyBoard.showKeyboard();  
            		keyBoard.setonKeyListener(null);
                    return false;  
            }  
		});  
		bt_ok = (Button) findViewById(R.id.bt_ok);
		
		// 密码最多32位
		EditTextUtil.limitCount(et_pwd, SysValueDefine.MAX_PASSWD_LEN, null);
		EditTextUtil.limitCount(et_verify, SysValueDefine.MAX_PASSWD_LEN, null);
		EditTextUtil.limitCount(et_verify_again, SysValueDefine.MAX_PASSWD_LEN, null);
		bt_ok.setOnClickListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog((Activity)context);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_ok:
			if(checkPara()) {
				dialogUtil.showProgressDialog();

				if (OperatorHelper.cacheCookie != null)
				{
					ChangePwd();
				} else {
					//没cookie时需要重新登陆
					OperatorHelper.silenceLogin(new OperatorHelper.LoginResponseListener() {
						@Override
						public void success()
						{
							ChangePwd();
						}

						@Override
						public void failed()
						{
							showPrompt(R.string.pub_connect_failed);
							dialogUtil.closeProgressDialog();
						}
					});
				}

			}
			break;
		}
	}
	
	private void showPrompt(int resId)
	{
		tip = new Tip((Activity)context, 
				getResources().getString(resId),
				null);
		tip.show(0);
	}
	
	private boolean checkPara()
	{
		if(et_pwd.getText() == null ||  
				et_pwd.getText().toString().length() == 0 ||
						et_pwd.getText().toString().length() > SysValueDefine.MAX_PASSWD_LEN)
		{
			showPrompt(R.string.ex_input_old_pwd);
			return false;
		}
		
		if(et_verify.getText() == null ||
				et_verify.getText().toString().length() == 0 ||
				et_verify.getText().toString().length() > SysValueDefine.MAX_PASSWD_LEN)
		{
			showPrompt(R.string.ex_input_new_pwd);
			return false;
		}
		
		if(et_verify_again.getText() == null ||
				et_verify_again.getText().toString().length() == 0 ||
						et_verify_again.getText().toString().length() > SysValueDefine.MAX_PASSWD_LEN)
		{
			showPrompt(R.string.ex_input_new_pwd_again);
			return false;
		}
		
		if(!et_verify.getText().toString().equals(et_verify_again.getText().toString()))
		{
			showPrompt(R.string.ex_pwd_not_same);
			return false;
		}
		
		return true;
	}
	
	private void resetMyself()
	{
		et_pwd.setText("");
		et_verify.setText("");
		et_verify_again.setText("");
	}
	
	private void ChangePwd()
	{
		ReqChangePwd req = new ReqChangePwd();
		req.setOld_pwd(et_pwd.getText().toString());
		req.setNew_pwd(et_verify.getText().toString());

		PwdChangeRequest request = new PwdChangeRequest(req, new ResponseEventHandler<BaseRsp>() {
			@Override
			public void onResponseSuccess(BaseRsp result)
			{
				dialogUtil.closeProgressDialog();
				if (result.isSuccess()) {
					//更新本地数据库密码
					updateSqlitePwd();
					tip = new Tip((Activity) context,
							getResources().getString(R.string.ex_change_success),
							new onDismissListener() {
								@Override
								public void onDismiss() {
									resetMyself();
								}
							});
					tip.show(0);
				} else {
					tip = new Tip((Activity) context, result.getMsg(),
							new onDismissListener() {
								@Override
								public void onDismiss() {
									resetMyself();
								}
							});
					tip.show(0);
				}
			}

			@Override
			public void onResponseError(VolleyError error) {
				Toast.makeText(context,"修改密码失败:"+error.getMessage(),Toast.LENGTH_LONG).show();
				dialogUtil.closeProgressDialog();
			}
		});
		RequestManager.addRequest(request,null);
	}

	protected void updateSqlitePwd() {
		String newPwd=et_verify_again.getText().toString();
		OperatorOp.updateOperatorPwd(operatorId,MD5Util.getMD5String(newPwd));
	}
}
