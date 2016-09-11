package com.moge.ebox.phone.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.config.GlobalConfig;
import com.moge.ebox.phone.fragment.FragmentSetAbout;
import com.moge.ebox.phone.fragment.FragmentSetContact;
import com.moge.ebox.phone.fragment.FragmentSetFeedback;
import com.moge.ebox.phone.fragment.FragmentSetPersional;
import com.moge.ebox.phone.ui.customview.Head;
import com.moge.ebox.phone.ui.customview.Head.HeadData;

public class SettingItemActivity extends BaseActivity {

	private int mTag;
	private Head mHead;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_item);
		mTag = getIntent().getIntExtra("tag", -1);
		initView();
		
	}

	private void initView() {
		initHead();
	}

	private void initHead() {
		mHead = findviewById_(R.id.title);
		HeadData data = mHead.new HeadData();
		data.backVisibility = 1;
		data.tvVisibility = true;
		switch (mTag) {
		case GlobalConfig.TAG_ABOUT:
			data.tvContent = getResources().getString(R.string.about);
			break;
		case GlobalConfig.TAG_CHECK_VERSION:
			data.tvContent = getResources().getString(R.string.check_version);
			break;
		case GlobalConfig.TAG_CONTACT:
			data.tvContent = getResources().getString(R.string.contact);
			break;
		case GlobalConfig.TAG_FEED_BACK:
			data.tvContent = getResources().getString(R.string.feed_back);
			break;
		case GlobalConfig.TAG_PERSIONAL_CENTER:
			data.tvContent = getResources().getString(R.string.persional_center);
			break;
		}
		mHead.setData(data, this);
		startFragment(mTag);
	}
	
	private void startFragment(int tag){
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment f=null;
		switch (tag) {
		case GlobalConfig.TAG_ABOUT:
			f=new FragmentSetAbout();
			break;
		case GlobalConfig.TAG_CONTACT:
			f=new FragmentSetContact();
			break;
		case GlobalConfig.TAG_FEED_BACK:
			f=new FragmentSetFeedback();
			break;
		case GlobalConfig.TAG_PERSIONAL_CENTER:
			f=new FragmentSetPersional();
			break;
		default:
			break;
		}
		if (f!=null) {
			transaction.replace(R.id.fl_content, f).commit();
		}
		
	}

}
