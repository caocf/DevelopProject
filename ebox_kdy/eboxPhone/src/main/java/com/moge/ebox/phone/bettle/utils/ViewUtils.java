package com.moge.ebox.phone.bettle.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ViewUtils {

	public static void setValues(Map<View, String> valuesMap, JSONObject json)
			throws JSONException {

		Set<Entry<View, String>> entrySet = valuesMap.entrySet();
		for (Entry<View, String> entry : entrySet) {
			View key = entry.getKey();
			String value = entry.getValue();
			if (TextUtils.isEmpty(value)) {
				String values = json.getString(value);
				((TextView) key).setText(values);
			} else {
				((TextView) key).setText("");
			}
		}
	}

	
	public static String getMoney(int sMoney) {

		Integer money = Integer.valueOf(sMoney);

		return money / 100 + "." + String.format("%02d", money % 100) + "元";
	}
	
	public static Dialog createChooseDialog(Context context,String title,ArrayList<HashMap<String, String>>items,DialogInterface.OnClickListener l)
	{
		ArrayList<String> data=new ArrayList<String>();
		for (int i = 0; i < items.size(); i++) {
			HashMap<String,String> map = items.get(i);
			String name = map.get("name");
			data.add(name);
		}
		String[] array = data.toArray(new String[data.size()]);
		
		return createChooseDialog(context,title,array,l);
	}

	/**
	 * 创建view的对话框
	 */
	public static Dialog createLayoutDialog(Context context,String title,View view){
		AlertDialog.Builder builder=new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setCancelable(true);
		AlertDialog dialog=builder.create();
		return dialog;
	}
	
	public static Dialog createChooseDialog(Context context,String title,String [] item,DialogInterface.OnClickListener l)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setItems(item, l);
		builder.setCancelable(true);
		AlertDialog dialog = builder.create();
		return dialog;
	}
	
	

	public static String checkState(String state) {
		if (state.contains("t")) {
			return "";
		}
		Integer st = Integer.valueOf(state);
		String res = null;
		switch (st) {
		case 0:// 快件生成
			res = "未取走";
			break;
		case 3:// 快件超时
			res = "已超期";
			break;
		case 4:// 自己取走
			res = "快递员取出";
			break;
		case 5:// 用户取走
			res = "用户已取走";
			break;
		case 6:// 管理员取出
			res = "管理员取出";
			break;
		default:
			res = "用户已取走";
			break;
		}
		return res;
	}

	
	/**
	 * 打开键盘
	 * @param v
	 */
	public static void openInput(Activity context,View v){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
	}
	
	/**
	 * 关闭键盘
	 * @param v
	 */
	public static void closeInput(Activity context,View v){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘  
	}
	
	public static void hideInput(Activity activity, EditText ed) {
		// ed.setInputType(InputType.TYPE_NULL);
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		try {
			Class<EditText> cls = EditText.class;
			Method setShowSoftInputOnFocus;
			setShowSoftInputOnFocus = cls.getMethod("setSoftInputShownOnFocus",
					boolean.class);
			setShowSoftInputOnFocus.setAccessible(true);
			setShowSoftInputOnFocus.invoke(ed, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
