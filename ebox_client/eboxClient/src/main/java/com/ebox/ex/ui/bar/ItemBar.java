package com.ebox.ex.ui.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.network.model.base.type.OrderInfo;
import com.ebox.ex.network.model.base.type.TimeOutOrderType;
import com.ebox.pub.utils.FunctionUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ItemBar extends RelativeLayout{
	private TextView tv_time;
	private TextView tv_barcode;
	private TextView tv_telephone;
	private TextView tv_status;
	
	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_bar_item, this, 
				true);
		tv_time = (TextView)findViewById(R.id.tv_time);
		tv_barcode = (TextView)findViewById(R.id.tv_barcode);
		tv_telephone = (TextView)findViewById(R.id.tv_telephone);
		tv_status = (TextView)findViewById(R.id.tv_status);
	}
	
	public ItemBar(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public ItemBar(Context context) {
		super(context);
		initViews(context);
	}
	
	public void setData(final OrderInfo item) {

		tv_time.setText(item.getDelivery_at());
		tv_time.getPaint().setFakeBoldText(true);
		tv_barcode.setText(item.getItem_id());
		tv_telephone.setText(item.getMsisdn());
		tv_status.setText(FunctionUtil.getItemStatusDesc(item.getState()));
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
}
