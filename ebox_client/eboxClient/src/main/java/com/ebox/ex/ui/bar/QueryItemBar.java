package com.ebox.ex.ui.bar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ebox.R;
import com.ebox.ex.database.LocalOrder.OrderLocalInfo;

public class QueryItemBar extends RelativeLayout{
	private TextView tv_time_date,tv_time_year;
	private TextView tv_barcode;
	private TextView tv_telephone;
	private TextView tv_operatorId;
	private Button btn_sms;
	public LinearLayout ll_item;
	public QueryItemBarListener listener;
	
	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_bar_query_item, this, 
				true);
		tv_time_date= (TextView)findViewById(R.id.tv_time_date);
		tv_time_year= (TextView) findViewById(R.id.tv_time_year);
		tv_operatorId=(TextView) findViewById(R.id.tv_operatorId);
		tv_barcode = (TextView)findViewById(R.id.tv_barcode);
		tv_telephone = (TextView)findViewById(R.id.tv_telephone);
		btn_sms = (Button)findViewById(R.id.btn_sms);
		ll_item= (LinearLayout) findViewById(R.id.ll_query_item);
	}
	
	public QueryItemBar(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public QueryItemBar(Context context) {
		super(context);
		initViews(context);
	}
	
	public void setData(final OrderLocalInfo item) {
		regroupTime(tv_time_date, tv_time_year, item.getDelivery_at());
//		tv_time.getPaint().setFakeBoldText(true);
		tv_operatorId.setText(item.getOperator_telephone());
		tv_barcode.setText(item.getItem_id());
		regroupTel(tv_telephone, item.getCustomer_telephone());
		//点击发送短信
		btn_sms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onDealClick(item);
				}
			}
		});


	}

	private void regroupTel(TextView tv_telephone, String customer_telephone) {
		String clean_tel=customer_telephone.replaceAll(" ", "");
		if (clean_tel.length()>=10){
			String tel_start=clean_tel.substring(0, 3);
			String tel_middle=clean_tel.substring(3,7);
			String tel_end=clean_tel.substring(7);
			tv_telephone.setText(tel_start+"****"+tel_end);
		}else{
			tv_telephone.setText(customer_telephone);
		}

	}

	private void regroupTime(TextView tv_time_date, TextView tv_time_year, String delivery_at) {
		String clean_at=delivery_at.replaceAll(" ","");
		String at_year=clean_at.substring(0,10);
		String at_date=clean_at.substring(10);
		tv_time_date.setText(at_date);
		tv_time_year.setText(at_year);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	public void setQueryItemBarListener(QueryItemBarListener listener)
	{
		this.listener = listener;
	}
	
	public interface QueryItemBarListener
	{
		void onDealClick(OrderLocalInfo item);
	}
}
