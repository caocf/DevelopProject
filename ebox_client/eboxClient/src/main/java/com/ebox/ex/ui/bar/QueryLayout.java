package com.ebox.ex.ui.bar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.ebox.Anetwork.RequestManager;
import com.ebox.Anetwork.ResponseEventHandler;
import com.ebox.R;
import com.ebox.ex.network.model.RspOperatorQueryItem;
import com.ebox.ex.network.request.RequestGetHistory;
import com.ebox.ex.ui.OperatorItemQueryRstActivity;
import com.ebox.ex.utils.OperatorHelper;
import com.ebox.pub.ui.keyboard.KeyboardUtil;
import com.ebox.pub.utils.DialogUtil;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.utils.Tip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class QueryLayout  extends OperMainLayout implements OnClickListener{
	private Context context;
	private EditText et_start;
	private EditText et_end;
	private String start_time,end_time;
	private Button bt_ok;
	private DialogUtil dialogUtil;
	private Tip tip;
    private String operatorId;
	private void initViews(final Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.ex_m_query_layout, this, 
				true);
		initView();
	}
	
	public QueryLayout(Context context, AttributeSet attribute) {
		super(context, attribute);
		initViews(context);
	}

	public QueryLayout(Context context,String operatorId) {
		super(context);
        this.operatorId = operatorId;
		initViews(context);
	}
	
	public void saveParams() {
		dialogUtil.closeProgressDialog();
		if(tip != null)
		{
			tip.closeTip();
		}
	}

	private void initView()
	{
		et_start = (EditText) findViewById(R.id.et_start);
		et_end = (EditText) findViewById(R.id.et_end);
		bt_ok = (Button) findViewById(R.id.bt_ok);
		KeyboardUtil.hideInput((Activity)context, et_start);
		KeyboardUtil.hideInput((Activity)context, et_end);
		et_start.requestFocus();
		
		bt_ok.setOnClickListener(this);
		et_start.setOnClickListener(this);
		et_end.setOnClickListener(this);
		
		dialogUtil = new DialogUtil();
		dialogUtil.createProgressDialog((Activity)context);

		String today = getToady();
		et_start.setText(today);
		et_end.setText(today);
	}

	private String getToady() {
		Date d=new Date();//获取时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		return sdf.format(d);
	}

	private  long getDataDuration(String beginDate, String endDate)
	    {
	        Date fromDate = null;
	        Date toDate = null;

	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	        try
	        {
	            fromDate = dateFormat.parse(beginDate);
	            toDate = dateFormat.parse(endDate);
	        }
	        catch (ParseException e)
	        {
	        }
	        long milliSecond = toDate.getTime() - fromDate.getTime();
	        return milliSecond/1000;
	    }
	 
	 public  boolean moreOneMonth(String beginDate, String endDate)
	 {
		 long duration = getDataDuration(beginDate,endDate);
		 if (duration>2592000) {
			return true;
		}else {
			return false;
		}
	 }
	 
	 private static Date getDate(String date)
	 {
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		 try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 return null;
	 }
	 private static String getMonth(String end)
	 {
		 Calendar c=Calendar.getInstance();

		 Date date = getDate(end);
		 Date d2=new Date();//当前日期

		 c.clear();
		 if (date.getTime()-d2.getTime()>2592000)
		 {
			 c.setTime(d2);
		 }else
		 {
			 c.setTime(date);
		 }
		 c.add(Calendar.MONTH, -1);
		 Date time = c.getTime();
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		 String d=dateFormat.format(time);
		 return d;
	 }
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.bt_ok:
			
			if(checkPara())
			{
				TSOperatorQueryItem();
			}
			break;
		case R.id.et_start:
		{
			Calendar c = Calendar.getInstance();
			new DatePickerView(
            		(Activity)context,
                    new DatePickerView.OnDateSetListener() {
                        public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) 
                        {
                        	et_start.setText(String.format("%02d-%02d-%02d",year,(month+1),dayOfMonth));
                        }
                    }, 
                    c.get(Calendar.YEAR), // 传入年份
                    c.get(Calendar.MONTH), // 传入月份 
                    c.get(Calendar.DAY_OF_MONTH) // 传入天数
                ).myShow();
			break;
		}
		case R.id.et_end:
		{
			Calendar c = Calendar.getInstance();
            new DatePickerView(
            		(Activity)context,
                new DatePickerView.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year,int month, int dayOfMonth) 
                    {
                    	et_end.setText(String.format("%02d-%02d-%02d",year,(month+1),dayOfMonth));
                    }
                }, 
                c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
            ).myShow();	
			break;
		}
		}
	}
	
	private void showPrompt(int resId)
	{
		tip = new Tip((Activity)context, 
				getResources().getString(resId),
				null);
		tip.show(0);
	}
	
	private boolean checkPara()
	{
		return true;
	}
	
	private void TSOperatorQueryItem()
	{
		String start=et_start.getText().toString();
		String end=et_end.getText().toString();
		String today = getToady();

		if (moreOneMonth(today,end)) {
			end=today;
		}
		if (moreOneMonth(start, end))
		{
			start=getMonth(end);
		}

		start_time = start;
		end_time = end;

		LogUtil.d("query history [startTime："+start+",endTime"+end+"]");
		dialogUtil.showProgressDialog();

		if (OperatorHelper.cacheCookie != null)
		{
			query(start_time, end_time);

		} else
		{
			OperatorHelper.silenceLogin(new OperatorHelper.LoginResponseListener() {
				@Override
				public void success()
				{
					query(start_time, end_time);
				}

				@Override
				public void failed()
				{
					dialogUtil.closeProgressDialog();
					showPrompt(R.string.pub_connect_failed);
				}
			});
		}
	}

	private void query(String start, String end) {

		HashMap<String, Object> prams = new HashMap<String, Object>();
		prams.put("page",0);
		prams.put("page_size", 9);
		prams.put("start_date", start);
		prams.put("end_date", end);
		prams.put("state", 100);

		RequestGetHistory requestGetHistory=new RequestGetHistory(prams, new ResponseEventHandler<RspOperatorQueryItem>() {
			@Override
			public void onResponseSuccess(RspOperatorQueryItem result) {
				dialogUtil.closeProgressDialog();
				if (result.isSuccess()) {

					if (result.getData() == null ||
							result.getData().getItems().size() == 0) {
						showPrompt(R.string.ex_no_result);
					} else {

						Intent intent = new Intent((Activity) context, OperatorItemQueryRstActivity.class);
						intent.putExtra("start_time", start_time);
						intent.putExtra("end_time", end_time);
						intent.putExtra("cache",result.getData());
						((Activity) context).startActivity(intent);
					}
				}
			}

			@Override
			public void onResponseError(VolleyError error) {
				dialogUtil.closeProgressDialog();
				showPrompt(R.string.pub_connect_failed);
				LogUtil.e(error.getMessage());
			}
		});
		RequestManager.addRequest(requestGetHistory, null);
		dialogUtil.showProgressDialog();
	}

}
