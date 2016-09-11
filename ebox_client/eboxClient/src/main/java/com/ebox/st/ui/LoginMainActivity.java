package com.ebox.st.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ebox.R;
import com.ebox.ex.ui.CommonActivity;
import com.ebox.ex.ui.bar.OperMainLayout;
import com.ebox.pub.ui.customview.Title;
import com.ebox.pub.ui.customview.Title.TitleData;
import com.ebox.pub.utils.MGViewUtil;
import com.ebox.pub.utils.Tip;
import com.ebox.st.ui.lic.DeliverLayout;
import com.ebox.st.ui.lic.FileHasPutLayout;
import com.ebox.st.ui.lic.GetfileLayout;

public class LoginMainActivity extends CommonActivity implements OnClickListener{
	public Button bt_get_normal;
	public Button bt_get_pressed;
	public Button bt_put_normal;
	public Button bt_put_pressed;
	public Button bt_has_put_normal;
	public Button bt_has_put_pressed;
	private RelativeLayout content;
	private OperMainLayout currentView;
	private Tip tip;
	private int currentPage = 0;
	
	private Title title;
	private TitleData titleData;

    private int DeliverLayoutCount =0;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.st_login_main);
        MGViewUtil.scaleContentView(this, R.id.rootView);
		initView();
	}
	

	private void initView() {
		title = (Title) findViewById(R.id.title);
		titleData = title.new TitleData();
		titleData.backVisibility = 1;
		titleData.tvVisibility = true;
		titleData.tvContent = getResources().getString(R.string.st_operator_main);
		title.setData(titleData, this);
		bt_get_normal = (Button) findViewById(R.id.bt_get_normal);
		bt_get_pressed = (Button) findViewById(R.id.bt_get_pressed);
		bt_put_normal = (Button) findViewById(R.id.bt_put_normal);
		bt_put_pressed = (Button) findViewById(R.id.bt_put_pressed);
		bt_has_put_normal = (Button) findViewById(R.id.bt_has_put_normal);
		bt_has_put_pressed = (Button) findViewById(R.id.bt_has_put_pressed);
		content = (RelativeLayout) findViewById(R.id.content);
		bt_get_normal.setOnClickListener(this);
		bt_get_pressed.setOnClickListener(this);
		bt_put_normal.setOnClickListener(this);
		bt_put_pressed.setOnClickListener(this);
		bt_has_put_normal.setOnClickListener(this);
		bt_has_put_pressed.setOnClickListener(this);
	}
	@Override
	public void onResume() {
		super.onResume();
		title.showTimer();

		changePage(currentPage);


	}
	
	@Override
	public void onPause() {
		super.onPause();
		title.stopTimer();
	}
	
	@Override
	public void onDestroy() {
		if(tip != null)
		{
			tip.closeTip();
		}
		
		super.onDestroy();
	}

	// 0:存证照  1：取文件 2：已存证照
	private void changePage(int currentPage)
	{
		this.currentPage = currentPage;
		if(currentPage == 0)
		{
			if(currentView != null)
			{
				content.removeAllViews();
				currentView.saveParams();
			}
			currentView = new DeliverLayout(this);
			content.addView(currentView);
			bt_put_normal.setVisibility(View.GONE);
			bt_put_pressed.setVisibility(View.VISIBLE);
			bt_get_normal.setVisibility(View.VISIBLE);
			bt_get_pressed.setVisibility(View.GONE);
			bt_has_put_normal.setVisibility(View.VISIBLE);
			bt_has_put_pressed.setVisibility(View.GONE);
		}
		else if(currentPage == 1)
		{
			if(currentView != null)
			{
				content.removeAllViews();
				currentView.saveParams();
			}
			currentView = new GetfileLayout(this);
			content.addView(currentView);

			bt_get_normal.setVisibility(View.GONE);
			bt_get_pressed.setVisibility(View.VISIBLE);
			bt_put_normal.setVisibility(View.VISIBLE);
			bt_put_pressed.setVisibility(View.GONE);
			bt_has_put_normal.setVisibility(View.VISIBLE);
			bt_has_put_pressed.setVisibility(View.GONE);
		}
		else if(currentPage == 2)
		{
			if(currentView != null)
			{
				content.removeAllViews();
				currentView.saveParams();
			}
			currentView = new FileHasPutLayout(this);
			content.addView(currentView);
			bt_get_normal.setVisibility(View.VISIBLE);
			bt_get_pressed.setVisibility(View.GONE);
			bt_put_normal.setVisibility(View.VISIBLE);
			bt_put_pressed.setVisibility(View.GONE);
			bt_has_put_normal.setVisibility(View.GONE);
			bt_has_put_pressed.setVisibility(View.VISIBLE);
		}

        MGViewUtil.scaleContentView(currentView);

		int i = this.getFragmentManager().getBackStackEntryCount();
		if (i>0)
		{
			this.getFragmentManager().popBackStack();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_put_normal:
		{
			changePage(0);
			break;
		}
		case R.id.bt_get_normal:
		{
			changePage(1);
			break;
		}
		case R.id.bt_has_put_normal:
		{
			changePage(2);
			break;
		}
		}
	}
}
