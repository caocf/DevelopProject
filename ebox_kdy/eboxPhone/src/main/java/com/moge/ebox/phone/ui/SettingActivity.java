package com.moge.ebox.phone.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.network.http.ApiClient;
import com.moge.ebox.phone.bettle.network.http.ApiClient.ClientCallback;
import com.moge.ebox.phone.bettle.tools.Logger;
import com.moge.ebox.phone.config.GlobalConfig;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;
import com.moge.ebox.phone.ui.update.VersionUpdateCache;
import com.moge.ebox.phone.ui.update.VersionUpdateCache.UpdateResultListener;
import com.moge.ebox.phone.utils.ToastUtil;

@SuppressLint("NewApi")
public class SettingActivity extends BaseActivity implements OnClickListener {

	private Context mContext;
	private RelativeLayout rl_check_version,rl_contact,rl_feed_back,rl_about,rl_persional;
	private Button btn_exit;
	private Head mHead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mContext = SettingActivity.this;
		initView();
	}

	private void initView() {
		rl_check_version=findviewById_(R.id.rl_check_version);
		rl_check_version.setOnClickListener(this);
		rl_about=findviewById_(R.id.rl_about);
		rl_about.setOnClickListener(this);
		rl_contact=findviewById_(R.id.rl_contact);
		rl_contact.setOnClickListener(this)	;
		rl_feed_back=findviewById_(R.id.rl_feed_back);
		rl_feed_back.setOnClickListener(this);
		btn_exit=findviewById_(R.id.btn_exit);
		btn_exit.setOnClickListener(this);
		rl_persional=findviewById_(R.id.rl_persional);
		rl_persional.setOnClickListener(this);
		initHead();
	}

	private void initHead() {
		mHead = findviewById_(R.id.title);
		HeadData data = mHead.new HeadData();
		data.backVisibility = 1;
		data.tvVisibility = true;
		data.tvContent = getResources().getString(R.string.setting);
		mHead.setData(data, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_check_version:
			check();
			break;
		case R.id.rl_about:
			start(GlobalConfig.TAG_ABOUT);
			break;
		case R.id.rl_contact:
			start(GlobalConfig.TAG_CONTACT);
			break;
		case R.id.rl_feed_back:
			start(GlobalConfig.TAG_FEED_BACK);
			break;
		case R.id.rl_persional:
			start(GlobalConfig.TAG_PERSIONAL_CENTER);
			break;
		case R.id.btn_exit:
			quit();
			break;
		}
	}

	private void check() {
		Map params = new HashMap();
		params.put("client_os",ApiClient.android_id);
		ApiClient.getVersion(EboxApplication.getInstance(), params, new ClientCallback() {
			@Override
			public void onSuccess(JSONArray data, int code) {
				if (data.length() > 0) {
					try {
						JSONObject versionObject = data.getJSONObject(0).getJSONObject("version");
						String clientVersion = versionObject.getString("clientVersion");
						int importantLevel = versionObject.getInt("importantLevel");
						String downloadUrl = versionObject.getString("downloadUrl");
						String updateContent = versionObject.getString("updateContent");
						Logger.i("IndexActivity:---version:" + versionObject.toString());
						VersionUpdateCache versionUpdateCache = new VersionUpdateCache(SettingActivity.this, new UpdateResultListener() {
							@Override
							public void onNotUpdate() {
								ToastUtil.showToastShort("当前已经是最新版本");
							}

							@Override
							public void onQuit() {
							}

							@Override
							public void onNextUpdate() {
							}
						});
						versionUpdateCache.checkNewVersion(clientVersion, importantLevel, downloadUrl, updateContent);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailed(byte[] data, int code) {
			}
		});
	}

	private void quit() {
		View view = View.inflate(mContext, R.layout.dialog, null);
		final Dialog dialog=new Dialog(mContext, R.style.customdialog);
		dialog.setContentView(view);
		view.findViewById(R.id.buttonOk).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				exit();
			}
		});
		view.findViewById(R.id.buttonCancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	protected void exit() {
		EboxApplication.getInstance().setUserLogout();
		Intent intent=new Intent(mContext, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	startActivity(intent);
        this.finish();
	}

	private void start(int tagCheckVersion) {
		Intent intent=new Intent(mContext, SettingItemActivity.class);
		intent.putExtra("tag", tagCheckVersion);
		startActivity(intent);
	}
	

}
