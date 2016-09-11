package com.moge.ebox.phone.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.moge.ebox.phone.R;

public class DialogUtil {

	public static Dialog createConfirmDialog(
			Context context,
			int iconId,
			String title,
			String message,
			String positiveBtnName,
			String negativeBtnName,
			android.content.DialogInterface.OnClickListener positiveBtnListener,
			android.content.DialogInterface.OnClickListener negativeBtnListener) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveBtnName, positiveBtnListener);
		builder.setNegativeButton(negativeBtnName, negativeBtnListener);
		dialog = builder.create();
		return dialog;
	}

	public static Dialog createSingleChoiceDialog(Context context,
			String title, CharSequence[] items,
			DialogInterface.OnClickListener clickListener) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
		builder.setSingleChoiceItems(items, 0, clickListener);
		builder.setNegativeButton("取消", null);
		dialog = builder.create();
		return dialog;
	}


	public static Dialog createProgressDialog(Context context, int resId) {
		return createProgressDialog(context,
				context.getResources().getString(resId));
	}

	public static Dialog createProgressDialog(Context context, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = LayoutInflater.from(context).inflate(
				R.layout.custom_progress_dialog, null);
		TextView mTextView = (TextView) view.findViewById(R.id.msgText);
		mTextView.setText(msg);
		builder.setView(view);
		return builder.create();
	}
}
