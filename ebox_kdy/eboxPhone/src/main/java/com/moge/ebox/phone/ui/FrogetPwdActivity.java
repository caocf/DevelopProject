package com.moge.ebox.phone.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.bettle.utils.ViewUtils;
import com.moge.ebox.phone.config.GlobalConfig;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.utils.LogonUtils;
import com.moge.ebox.phone.utils.ToastUtil;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class FrogetPwdActivity extends BaseActivity {
	private Head mHead;
	private Button mbtnRegister, mbtnGetCheck;
	private EditText medPhone, medPwd, medPwdAgain, medCheckCode;
	private static final int TAG_TIME = 1;
	private static final int TAG_GET = 2;
	private Handler mHandler;
	private String verifyNumber = "";

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwd);
		mHandler = new Handler(getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == TAG_TIME) {
					int time = msg.arg1;
					mbtnGetCheck.setText(time + " s");
					mbtnGetCheck.setEnabled(false);
				} else if (msg.what == TAG_GET) {
					mbtnGetCheck.setEnabled(true);
					mbtnGetCheck.setText(getResources().getString(
							R.string.retry_get_checkcode));
				}
			}
		};
		initView();
	}

	private void initView() {
		medPhone = findviewById_(R.id.phoneEdit);
		medPwd = findviewById_(R.id.ed_pwd);
		medCheckCode = findviewById_(R.id.ed_check_code);
		medPwdAgain = findviewById_(R.id.ed_pwd_again);
		mbtnGetCheck = findviewById_(R.id.btn_get_check);
		mbtnGetCheck.setOnClickListener(mClickListener);
		mbtnRegister = findviewById_(R.id.btn_register);
		mbtnRegister.setOnClickListener(mClickListener);
		initHead();
	}

	private void initHead() {
		mHead = findviewById_(R.id.title);
		HeadData data = mHead.new HeadData();
		data.backVisibility = 1;
		data.tvVisibility = true;
		data.tvContent = getResources().getString(R.string.find_forget_pwd);
		mHead.setData(data, this);
	}

	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_register:// 提交返回
				onRegisterAction();
				break;
			case R.id.btn_get_check:
				onGetCheckAction();
				break;
			default:
				break;
			}
		}
	};

	private void onRegisterAction() {

		if (checkInput()) {
			final ProgressDialog dialog = UIHelper.showProgress(this, "",
					"修改密码中...", false);
			Map params = new HashMap();
			params.put("username", getText(medPhone));
			params.put("verify", getText(medCheckCode));
			String pwd = getText(medPwd);
			params.put("password", pwd);
			/**
			 * 修改密码
			 */
			ApiClient.retrievePassword(EboxApplication.getInstance(), params,
					new ClientCallback() {

						@Override
						public void onSuccess(JSONArray data,int code) {
							UIHelper.dismissProgress(dialog);
							retrieveSuccess();
						}

						@Override
						public void onFailed(byte[] data,int code) {
							UIHelper.dismissProgress(dialog);
							time = 0;
						}
					});
		}
	}

	private void retrieveSuccess() {
		ToastUtil.showToastShort("密码修改成功！");
		mbtnRegister.setEnabled(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				SystemClock.sleep(1500);

				FrogetPwdActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String phone = medPhone.getText().toString().trim();
						Intent intent = new Intent();
						intent.putExtra(GlobalConfig.FLAG_TEL, phone);
						setResult(RESULT_OK, intent);
						FrogetPwdActivity.this.finish();
					}
				});
			}
		}).start();

	}

	private Thread thread;
	private int time;

	protected void onGetCheckAction() {

		String phone = medPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone) || !LogonUtils.isMobileNO(phone)) {
			ToastUtil.showToastShort("请输入正确手机号");
			return;
		}
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				time = 120;
				while (time != 0) {
					time--;
					Message msg = Message.obtain();
					msg.what = TAG_TIME;
					msg.arg1 = time;
					mHandler.sendMessage(msg);
					SystemClock.sleep(1000);
				}
				if (time == 0) {
					mHandler.sendEmptyMessage(TAG_GET);
				}
			}
		});
		thread.start();
		getCheckCode(phone);
	}

	private void getCheckCode(String phone) {
		Map params = new HashMap();
		params.put("username", phone);
		params.put("type", "5");// 注册：4，忘记密码：5
//		params.put("action","reset");
		/**
		 * 忘记密码  获得验证码
		 */
		ApiClient.getVerifySms(EboxApplication.getInstance(), params,
				new ClientCallback() {

					@Override
					public void onSuccess(JSONArray data,int code) {

						Logger.i("FrogetPwdActivity:获得忘记密码验证码"+data.toString());
//						try {
////							verifyNumber = (String) data.getJSONObject(0).get(
////									"verify");
//
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
					}

					@Override
					public void onFailed(byte[] data,int code) {
						time = 0;
					}
				});
	}

	private boolean checkInput() {
		
		String phone = medPhone.getText().toString().trim();
		
		if (TextUtils.isEmpty(phone) || !LogonUtils.isMobileNO(phone))
		{
			ToastUtil.showToastShort(R.string.mobile_format_error);
			return false;
		}
		String code = medCheckCode.getText().toString().trim();
		if (TextUtils.isEmpty(code)) {
			ToastUtil.showToastShort(R.string.checkcode_empty);
			return false;
		}
//


		String pwd = medPwd.getText().toString().trim();
		if (TextUtils.isEmpty(pwd)) {
			ToastUtil.showToastShort(R.string.register_pwd);
			return false;
		}
		String pwdAgain = medPwdAgain.getText().toString().trim();
		if (TextUtils.isEmpty(pwdAgain)) {
			ToastUtil.showToastShort(R.string.pwd_again);
			return false;
		}
		if (!pwd.equals(pwdAgain)) {
			ToastUtil.showToastShort(R.string.two_pwd_error);
			return false;
		}

		return true;
	}

	private String getText(TextView textView) {
		return textView.getText().toString().trim();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		ViewUtils.closeInput(this, medPhone);
		ViewUtils.closeInput(this, medPwd);
		ViewUtils.closeInput(this, medPwdAgain);
		ViewUtils.closeInput(this, medCheckCode);
		return super.dispatchTouchEvent(ev);
	}

}
