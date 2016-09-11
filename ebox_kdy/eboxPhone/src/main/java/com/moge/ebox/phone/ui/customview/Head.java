package com.moge.ebox.phone.ui.customview;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moge.ebox.phone.R;

public class Head extends RelativeLayout
	implements OnClickListener{
	private RelativeLayout back;
	public Button right;
	private TextView title;
	private ZCTitleListener listener;
	private Activity activity;
	private HeadData titleData;
	
	public Head(Context context) {
		super(context);
		initViews(context);
	}
	
	public Head(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}
	
	public Head(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	private void initViews(Context context)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.head, this, 
				true);
		back = (RelativeLayout) findViewById(R.id.rl_back);
		title = (TextView) findViewById(R.id.header_title);
		right = (Button) findViewById(R.id.header_right);
		right.setOnClickListener(this);
		back.setOnClickListener(this);
	}
	
	public void setData(HeadData data, Activity activity)
	{
		this.activity = activity;
		titleData = data;
		
		if(data.backVisibility  == 0)
		{
			back.setVisibility(View.GONE);
		}
		else if(data.backVisibility  == 1 || data.backVisibility  == 3)
		{
			back.setVisibility(View.VISIBLE);
		}
		else
		{
			back.setVisibility(View.VISIBLE);
		}
		
		if(data.tvVisibility)
		{
			title.setVisibility(View.VISIBLE);
			title.setText(data.tvContent);
		}
		else
		{
			title.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.rl_back:
			if(titleData.backVisibility  == 1)
			{
				activity.finish();
			}
			else if(titleData.backVisibility  == 2 || titleData.backVisibility  == 3)
			{
				if(listener != null)
					listener.onOperateBtnClick(1);
			}
			break;
		}
	}
	
	public void setZCTitleListner(ZCTitleListener listener)
	{
		this.listener = listener;
	}
	
	public interface ZCTitleListener
	{
		// 1: 左侧点击  2:右侧点击
		void onOperateBtnClick(int index);
	}
	
	public class HeadData{
		// 左侧操作区 0:不显示 1:显示为返回 2:显示自定义内容并处理点击事件 3:显示为返回处理点击事件
		public int backVisibility; 
		
		// 中间内容显示区
		public boolean tvVisibility;
		public String tvContent;
	}

}