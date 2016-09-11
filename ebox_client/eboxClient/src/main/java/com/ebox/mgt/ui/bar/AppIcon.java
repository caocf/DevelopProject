package com.ebox.mgt.ui.bar;

import com.ebox.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppIcon extends RelativeLayout{
	private ImageView iv_icon;
	private TextView tv_name;
	
	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.mgt_app_icon, this, 
				true);
		iv_icon = (ImageView)findViewById(R.id.iv_icon);
		tv_name = (TextView)findViewById(R.id.tv_name);
	}
	
	public AppIcon(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public AppIcon(Context context) {
		super(context);
		initViews(context);
	}
	
	public void setData(Drawable icon, String name)
	{
		iv_icon.setBackgroundDrawable(icon);
		tv_name.setText(name);
	}
}
