package com.ebox.st.ui.lic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.pub.boxctl.BoxUtils;
import com.ebox.st.model.IdentiOrderModel;


public class GetfileItemBar extends RelativeLayout{
	private TextView tv_time;
	private TextView tv_barcode;
	private TextView tv_telephone;
	private TextView tv_number;
	private TextView btn_deal;
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
		btn_deal = (TextView)findViewById(R.id.btn_deal);
	}
	
	public GetfileItemBar(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public GetfileItemBar(Context context) {
		super(context);
		initViews(context);
	}
	
	public void setData(final IdentiOrderModel item) {
		//2015-07-17 09:31:07.0
		String barcode = item.getUpdate_at();
		if (barcode.contains("."))
		{
			int index=barcode.indexOf(".");
			barcode=barcode.substring(0,index);
		}
		tv_time.setText(barcode);
		tv_number.setText(item.getBarcode());
		tv_barcode.setText(item.getIdcard().getName());
		tv_telephone.setText(BoxUtils.getBoxDesc(item.getBox_id()));
		btn_deal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				listener.onDealClick(item);
			}
	    });
	}
	
	public void setBtnDeal(String text)
	{
		btn_deal.setText(text);
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
		void onDealClick(IdentiOrderModel item);
	}
}
