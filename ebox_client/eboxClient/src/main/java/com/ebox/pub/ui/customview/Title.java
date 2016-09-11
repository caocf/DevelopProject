package com.ebox.pub.ui.customview;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.ui.TopActivity;
import com.ebox.ex.ui.bar.TimeLeftBar;


public class Title extends RelativeLayout
	implements OnClickListener{
	public ImageButton back;
    //添加返回首页
    private Button backHome;

	public TextView tv_timer;
	private TimeLeftBar timeLeft;
	public TextView tv;
	private ZCTitleListener listener;
	private Activity activity;
	private TitleData titleData;
    private Context mContext;
	public ImageView iv_title;
	
	public Title(Context context) {
		super(context);
		initViews(context);
	}

	public Title(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}
	
	public Title(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	private void initViews(Context context)
	{
        mContext=context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.pub_title, this, 
				true);
		back = (ImageButton) findViewById(R.id.bt_back);
        backHome= (Button) findViewById(R.id.ex_bt_home);
		tv = (TextView) findViewById(R.id.tv_title);
		tv_timer = (TextView) findViewById(R.id.tv_timer);
		iv_title= (ImageView) findViewById(R.id.iv_title);
		timeLeft = new TimeLeftBar(tv_timer);
		
		back.setOnClickListener(this);
        backHome.setOnClickListener(this);
	}
	
	public void setData(TitleData data, Activity activity)
	{
		this.activity = activity;
		titleData = data;
		
		if(data.backVisibility  == 0)
		{
			back.setVisibility(View.GONE);
		}else if (data.backHomeVisibility==1){
            backHome.setVisibility(View.VISIBLE);
        }
		else if(data.backVisibility  == 1 || data.backVisibility  == 3)
		{
			back.setVisibility(View.VISIBLE);
		}
		else
		{
			back.setVisibility(View.VISIBLE);
//			back.setText(data.backStr);
		}
		
		if(data.tvVisibility)
		{
			tv.setVisibility(View.VISIBLE);
			tv.setText(data.tvContent);
			iv_title.setImageResource(data.ivIcon);

		}
		else
		{
			tv.setVisibility(View.GONE);
		}
	}
	
	public void showTimer()
	{
		timeLeft.show();
	}
	
	public void stopTimer()
	{
		timeLeft.stop();
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
		case R.id.bt_back:
			if(titleData.backVisibility  == 1)
			{
				activity.finish();
				break;
			}
			else if(titleData.backVisibility  == 2 || titleData.backVisibility  == 3)
			{
				if(listener != null)
					listener.onOperateBtnClick(1);
			}
			break;

        //返回首页
         case R.id.ex_bt_home:
             if (titleData.backHomeVisibility==1){
                 Intent homeIntent=new Intent(mContext, TopActivity.class);
                 mContext.startActivity(homeIntent);


             }

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
	
	public class TitleData{
		// 左侧操作区 0:不显示 1:显示为返回 2:显示自定义内容并处理点击事件 3:显示为返回处理点击事件
		public int backVisibility; 
		public String backStr =getResources().getString(R.string.pub_back) ;
		
		
		// 中间内容显示区
		public boolean tvVisibility;
		public String tvContent;
		public int ivIcon;

        //首页是否显示
        public int backHomeVisibility;
	}

}
