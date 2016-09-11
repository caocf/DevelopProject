package com.moge.ebox.phone.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.moge.ebox.phone.R;
import com.moge.ebox.phone.utils.wheel.OnWheelChangedListener;
import com.moge.ebox.phone.utils.wheel.WheelView;
import com.moge.ebox.phone.utils.wheel.adapters.ArrayWheelAdapter;

public class TimePickerUtil {

	private String TAG = "TimePickerUtil";

	private static TimePickerUtil instance;

	public static TimePickerUtil getInstance() {
		if (instance == null) {
			instance = new TimePickerUtil();
		}
		return instance;
	}

	public interface TimePickerClickListener {
		public void onTimePickClick(int year, int month, int day, int hour,
				int min, int sec, String formatedDateStr);

	}

	private Integer pickedP1Info;
	private Integer pickedP2Info;
	private Integer pickedP3Info;
	private Integer pickedP4Info;
	private Integer pickedP5Info;
	private Integer pickedP6Info;
	private boolean firstPickP1 = true;
	private Date currentDate;
	private Date initDate;
	private SimpleDateFormat sFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
//	private SimpleDateFormat sFormat = new SimpleDateFormat(
//			"yyyy-MM-dd");


	public void showTimePicker(final String initDateString,
			final Activity activity, final TimePickerClickListener listener) {

		pickedP1Info = null;
		pickedP2Info = null;
		pickedP3Info = null;
		pickedP4Info = null;
		pickedP5Info = null;
		pickedP6Info = null;
		firstPickP1 = true;
		currentDate = new Date();
		initDate = null;
		try {
			initDate = sFormat.parse(initDateString);
		} catch (Exception e) {
			// TODO: handle exception
		}

		final View geoPickerView = activity.getLayoutInflater().inflate(
				R.layout.picker_time, null);
		final PopupWindow pop = new PopupWindow(geoPickerView,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, false);

		Button buttonOk = (Button) geoPickerView.findViewById(R.id.buttonOk);
		Button buttonCancel = (Button) geoPickerView
				.findViewById(R.id.buttonCancel);
		final WheelView wheelView1 = (WheelView) geoPickerView
				.findViewById(R.id.picker1);
		final WheelView wheelView2 = (WheelView) geoPickerView
				.findViewById(R.id.picker2);
		final WheelView wheelView3 = (WheelView) geoPickerView
				.findViewById(R.id.picker3);
		final WheelView wheelView4 = (WheelView) geoPickerView
				.findViewById(R.id.picker4);
		final WheelView wheelView5 = (WheelView) geoPickerView
				.findViewById(R.id.picker5);
		final WheelView wheelView6 = (WheelView) geoPickerView
				.findViewById(R.id.picker6);

		wheelView1.setVisibleItems(7);
		wheelView2.setVisibleItems(7);
		wheelView3.setVisibleItems(7);
		wheelView4.setVisibleItems(7);
		wheelView5.setVisibleItems(7);
		wheelView6.setVisibleItems(7);

		if (initDate != null && firstPickP1) {
			int p1Index = updateP1Info(wheelView1, activity,
					initDate.getYear() + 1900);
			setcurrentP1Info(p1Index);
			int p2Index = updateP2Info(wheelView2, activity,
					initDate.getMonth());
			setcurrentP2Info(p2Index);
			int p3Index = updateP3Info(wheelView3, activity, pickedP1Info,
					pickedP2Info, initDate.getDate());
			setcurrentP3Info(p3Index);

			int p4Index = updateP4Info(wheelView4, activity,
					initDate.getHours());
			setcurrentP4Info(p4Index);
			int p5Index = updateP5Info(wheelView5, activity,
					initDate.getMinutes());
			setcurrentP5Info(p5Index);
			int p6Index = updateP6Info(wheelView6, activity,
					initDate.getSeconds());
			setcurrentP6Info(p6Index);
			firstPickP1 = false;
			currentDate=initDate;
		} else {
			int p1Index = updateP1Info(wheelView1, activity,
					currentDate.getYear() + 1900);
			setcurrentP1Info(p1Index);
			int p2Index = updateP2Info(wheelView2, activity,
					currentDate.getMonth());
			setcurrentP2Info(p2Index);
			int p3Index = updateP3Info(wheelView3, activity, pickedP1Info,
					pickedP2Info, currentDate.getDate());
			setcurrentP3Info(p3Index);

			int p4Index = updateP4Info(wheelView4, activity,
					currentDate.getHours());
			setcurrentP4Info(p4Index);
			int p5Index = updateP5Info(wheelView5, activity,
					currentDate.getMinutes());
			setcurrentP5Info(p5Index);
			int p6Index = updateP6Info(wheelView6, activity,
					currentDate.getSeconds());
			setcurrentP6Info(p6Index);
		}

		wheelView1.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				setcurrentP1Info(newValue);
				int p3Index = updateP3Info(wheelView3, activity, pickedP1Info,
						pickedP2Info, pickedP3Info);
				setcurrentP3Info(p3Index);
			}
		});
		wheelView2.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				setcurrentP2Info(newValue);
				int p3Index = updateP3Info(wheelView3, activity, pickedP1Info,
						pickedP2Info, pickedP3Info);
				setcurrentP3Info(p3Index);
			}
		});
		wheelView3.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				setcurrentP3Info(newValue);
				updateDay();
			}
		});

		wheelView4.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				setcurrentP4Info(newValue);
			}
		});

		wheelView5.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				setcurrentP5Info(newValue);
			}
		});

		wheelView6.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				setcurrentP6Info(newValue);
			}
		});

		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(false);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);

		buttonOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (pop.isShowing()) {
					pop.dismiss();
					if (listener != null) {
						Date date = new Date(pickedP1Info - 1900, pickedP2Info - 1, pickedP3Info, pickedP4Info, pickedP5Info, pickedP6Info);
						//Date date = new Date(pickedP1Info - 1900, pickedP2Info - 1, pickedP3Info);
						
						listener.onTimePickClick(pickedP1Info, pickedP2Info,
								pickedP3Info, pickedP4Info, pickedP5Info,
								pickedP6Info,
//								String.valueOf(pickedP1Info)
//										+ "-" + String.valueOf(pickedP2Info)
//										+ "-" + String.valueOf(pickedP3Info)
//										+ " " + String.valueOf(pickedP4Info)
//										+ ":" + String.valueOf(pickedP5Info)
//										+ ":" + String.valueOf(pickedP3Info)
								sFormat.format(date)
								);
					}
				}
			}
		});
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (pop.isShowing()) {
					pop.dismiss();
				}
			}
		});
		pop.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER,
				0, 0);
	}

	protected void updateDay() {
		//pickedP3Info		
		
	}

	private int updateP1Info(WheelView p1View, Activity activity,
			final Integer currentItem) {

		int currentItemIndex = currentDate.getYear() + 1900;

		String[] years = new String[40];
		for (int i = 0; i < years.length; i++) {
			years[i] = String.valueOf(currentDate.getYear() + 1900 - 20 + i)
					+ "年";
		}

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				activity, years);
		adapter.setTextSize(15);
		p1View.setViewAdapter(adapter);
		if (currentItem != null) {
			currentItemIndex = currentItem
					- (currentDate.getYear() + 1900 - 20);
			if (currentItemIndex > 39) {
				currentItemIndex = 0;
			}
		}
		p1View.setCurrentItem(currentItemIndex);
		return currentItemIndex;
	}

	private void setcurrentP1Info(int index) {
		pickedP1Info = currentDate.getYear() + 1900 - 20 + index;
	}

	private int updateP2Info(WheelView p2View, Activity activity,
			final Integer currentMonthIndex) {

		int currentItemIndex = currentDate.getMonth();

		String[] months = new String[12];
		for (int i = 0; i < months.length; i++) {
			months[i] = String.valueOf(i + 1) + "月";
		}

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				activity, months);
		adapter.setTextSize(15);
		p2View.setViewAdapter(adapter);
		if (currentMonthIndex != null) {
			currentItemIndex = currentMonthIndex;
			if (currentItemIndex > 11) {
				currentItemIndex = 0;
			}
		}

		p2View.setCurrentItem(currentItemIndex);
		return currentItemIndex;
	}

	private void setcurrentP2Info(int monthIndex) {
		pickedP2Info = monthIndex + 1;
	}

	private int updateP3Info(WheelView p3View, Activity activity,
			final Integer year, final Integer month, final Integer currentItem) {

		int currentItemIndex = currentDate.getDate() - 1;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		String[] days = new String[maxDays];
		for (int i = 0; i < days.length; i++) {
			days[i] = String.valueOf(i + 1) + "日";
		}

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				activity, days);
		adapter.setTextSize(15);
		p3View.setViewAdapter(adapter);
		if (currentItem != null) {
			currentItemIndex = currentItem - 1;
			if (currentItemIndex > maxDays - 1) {
				currentItemIndex = 0;
			}
		}
		p3View.setCurrentItem(currentItemIndex);

		return currentItemIndex;
	}

	private void setcurrentP3Info(int date) {
		pickedP3Info = date + 1;
	}

	private int updateP4Info(WheelView p4View, Activity activity,
			final Integer currentItem) {

		int currentItemIndex = currentDate.getHours();

		String[] hours = new String[24];
		for (int i = 0; i < hours.length; i++) {
			hours[i] = String.valueOf(i) + "时";
		}

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				activity, hours);

		adapter.setTextSize(15);
		p4View.setViewAdapter(adapter);
		if (currentItem != null) {
			currentItemIndex = currentItem;
			if (currentItemIndex > 23) {
				currentItemIndex = 0;
			}
		}
		p4View.setCurrentItem(currentItemIndex);

		return currentItemIndex;
	}

	private void setcurrentP4Info(int hour) {
		pickedP4Info = hour;
	}

	private int updateP5Info(WheelView p5View, Activity activity,
			final Integer currentItem) {

		int currentItemIndex = currentDate.getMinutes();

		String[] mins = new String[60];
		for (int i = 0; i < mins.length; i++) {
			mins[i] = String.valueOf(i) + "分";
		}

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				activity, mins);
		adapter.setTextSize(15);
		p5View.setViewAdapter(adapter);
		if (currentItem != null) {
			currentItemIndex = currentItem;
			if (currentItemIndex > 59) {
				currentItemIndex = 0;
			}
		}
		p5View.setCurrentItem(currentItemIndex);

		return currentItemIndex;
	}

	private void setcurrentP5Info(int min) {
		pickedP5Info = min;
	}

	private int updateP6Info(WheelView p6View, Activity activity,
			final Integer currentItem) {

		int currentItemIndex = currentDate.getMinutes();

		String[] secs = new String[60];
		for (int i = 0; i < secs.length; i++) {
			secs[i] = String.valueOf(i) + "秒";
		}

		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				activity, secs);
		adapter.setTextSize(15);
		p6View.setViewAdapter(adapter);
		if (currentItem != null) {
			currentItemIndex = currentItem;
			if (currentItemIndex > 59) {
				currentItemIndex = 0;
			}
		}
		p6View.setCurrentItem(currentItemIndex);

		return currentItemIndex;
	}

	private void setcurrentP6Info(int sec) {
		pickedP6Info = sec;
	}

}
