package com.moge.ebox.phone.utils;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.moge.ebox.phone.ui.HomeActivity;

public class LauncherNumberUtils {
	private final static String lancherActivityClassName = HomeActivity.class
			.getName();

	@SuppressLint("DefaultLocale")
	public static void sendBadgeNumber(Activity context, String number) {
		if (TextUtils.isEmpty(number)) {
			number = "";
		} else {
			int numInt = Integer.valueOf(number);
			number = String.valueOf(Math.max(0, Math.min(numInt, 99)));
		}

		if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
			sendToXiaoMi(context, number);
		} else if (Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
			sendToSony(context, number);
		} else if (Build.MANUFACTURER.toLowerCase().contains("sony")) {
			sendToSamsumg(context, number);
		} else {
		}
	}

	private static void sendToXiaoMi(Activity context, String number) {
		try {
			Class miuiNotificationClass = Class
					.forName("android.app.MiuiNotification");
			Object miuiNotification = miuiNotificationClass.newInstance();
			Field field = miuiNotification.getClass().getDeclaredField(
					"messageCount");
			field.setAccessible(true);
			field.set(miuiNotification, number);// 设置信息数-->这种发送必须是miui 6才行
		} catch (Exception e) {
			e.printStackTrace();
			// miui 6之前的版本
			Intent localIntent = new Intent(
					"android.intent.action.APPLICATION_MESSAGE_UPDATE");
			localIntent.putExtra(
					"android.intent.extra.update_application_component_name",
					context.getPackageName() + "/" + lancherActivityClassName);
			localIntent.putExtra(
					"android.intent.extra.update_application_message_text",
					number);
			context.sendBroadcast(localIntent);
		}

	}

	private static void sendToSony(Activity context, String number) {
		boolean isShow = true;
		if ("0".equals(number)) {
			isShow = false;
		}
		Intent localIntent = new Intent();
		localIntent
				.putExtra(
						"com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",
						isShow);// 是否显示
		localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
		localIntent.putExtra(
				"com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",
				lancherActivityClassName);// 启动页
		localIntent.putExtra(
				"com.sonyericsson.home.intent.extra.badge.MESSAGE", number);// 数字
		localIntent.putExtra(
				"com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME",
				context.getPackageName());// 包名
		context.sendBroadcast(localIntent);
	}

	private static void sendToSamsumg(Activity context, String number) {
		Intent localIntent = new Intent(
				"android.intent.action.BADGE_COUNT_UPDATE");
		localIntent.putExtra("badge_count", number);// 数字
		localIntent.putExtra("badge_count_package_name",
				context.getPackageName());// 包名
		localIntent
				.putExtra("badge_count_class_name", lancherActivityClassName); // 启动页
		context.sendBroadcast(localIntent);
	}

	public static Bitmap getIcon(int id, Activity context, String number) {

		Bitmap icon = BitmapFactory.decodeResource(context.getResources(), id);

		return getNumberIcon(icon, context, number);
	}

	public static Bitmap getNumberIcon(Bitmap icon, Activity context,
			String number) {
		// 初始化画布
		int iconSize = (int) context.getResources().getDimension(
				android.R.dimen.app_icon_size);
		Log.d("main", "the icon size is " + iconSize);
		Bitmap contactIcon = Bitmap.createBitmap(iconSize, iconSize,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(contactIcon);

		// 拷贝图片
		Paint iconPaint = new Paint();
		iconPaint.setDither(true);// 防抖动
		iconPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯齿的效果
		Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
		Rect dst = new Rect(0, 0, iconSize, iconSize);
		canvas.drawBitmap(icon, src, dst, iconPaint);

		int contacyCount = Integer.valueOf(number);
		// 启用抗锯齿和使用设备的文本字距
		Paint countPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);
		countPaint.setColor(Color.RED);
		countPaint.setTextSize(20f);
		countPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(String.valueOf(contacyCount), iconSize - 18, 25,
				countPaint);
		return contactIcon;
	}

 public void addShortcutToDesktop(Activity context) {

		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		BitmapDrawable iconBitmapDrawabel = null;

		// 获取应用基本信息
		String label = context.getPackageName();
		PackageManager packageManager = context.getPackageManager();
		try {
			iconBitmapDrawabel = (BitmapDrawable) packageManager
					.getApplicationIcon(label);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		// 设置属性
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, label);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON,
				iconBitmapDrawabel.getBitmap());

		// 是否允许重复创建 -- fase-->否
		shortcut.putExtra("duplicate", false);

		// 设置启动程序
		ComponentName comp = new ComponentName(label, "."
				+ context.getLocalClassName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));

		context.sendBroadcast(shortcut);
	}

}
