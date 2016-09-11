package com.ebox.ex.ui.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;
import com.ebox.pub.boxctl.BoxUtils;

public class TimeoutItemBar extends RelativeLayout{
	private TextView tv_time;
	private TextView tv_barcode;
	private TextView tv_telephone;
	private TextView tv_number;
	private Button btn_deal;
	public TimeoutItemBarListener listener;
	
	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_bar_timeout_item, this, 
				true);
		tv_number=(TextView) findViewById(R.id.tv_number);
		tv_time = (TextView)findViewById(R.id.tv_time);
		tv_barcode = (TextView)findViewById(R.id.tv_barcode);
		tv_telephone = (TextView)findViewById(R.id.tv_telephone);
		btn_deal = (Button)findViewById(R.id.btn_deal);
	}
	
	public TimeoutItemBar(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public TimeoutItemBar(Context context) {
		super(context);
		initViews(context);
	}
	
	public void setData(final OrderLocalInfo item) {
		tv_time.setText(item.getDelivery_at());
		tv_time.getPaint().setFakeBoldText(true);
		tv_number.setText(BoxUtils.getBoxDesc(item.getBox_code()));

		tv_barcode.setText(item.getItem_id());
		tv_telephone.setText(item.getCustomer_telephone());

		btn_deal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				listener.onDealClick(item);
			}
	    });
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	public void setTimeoutItemBarListener(TimeoutItemBarListener listener)
	{
		this.listener = listener;
	}
	
	public interface TimeoutItemBarListener
	{
		void onDealClick(OrderLocalInfo item);
	}
}
