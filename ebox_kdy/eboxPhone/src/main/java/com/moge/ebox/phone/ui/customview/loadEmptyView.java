package com.moge.ebox.phone.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moge.ebox.phone.R;

public class loadEmptyView extends RelativeLayout {

	private ImageView iv_load;
	public loadEmptyView(Context context) {
		super(context);
		initViews(context);
	}

	public loadEmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public loadEmptyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.load_empty_view, this, true);
//		iv_load=(ImageView) findViewById(R.id.iv_load);
//		Animation animation = AnimationUtils.loadAnimation(context, R.anim.loading_rotate);
//		iv_load.startAnimation(animation);
	}
}