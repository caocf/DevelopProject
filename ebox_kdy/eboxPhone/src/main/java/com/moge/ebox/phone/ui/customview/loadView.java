package com.moge.ebox.phone.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moge.ebox.phone.R;

public class loadView extends RelativeLayout {

	private ImageView iv_load;
	public loadView(Context context) {
		super(context);
		initViews(context);
	}

	public loadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public loadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.load_view, this, true);
		iv_load=(ImageView) findViewById(R.id.iv_load);
		Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
		iv_load.startAnimation(animation);
	}
}