package com.moge.ebox.phone.bettle.tools;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moge.ebox.phone.R;

public class UIHelper {
	public static final int WEIXIN_TIMELINE_SUPPORTED_VERSION = 553779201;
	public static final int LISTVIEW_ACTION_INIT = 1;
	public static final int LISTVIEW_ACTION_REFRESH = 2;
	public static final int LISTVIEW_ACTION_SCROLL = 3;
	public static final int LISTVIEW_ACTION_CHANGE_CATALOG = 4;
	public static final int LISTVIEW_DATA_MORE = 1;
	public static final int LISTVIEW_DATA_LOADING = 2;
	public static final int LISTVIEW_DATA_FULL = 3;
	public static final int LISTVIEW_DATA_EMPTY = 4;
	public static final int LISTVIEW_DATATYPE_MESSAGE = 1;
	public static final int LISTVIEW_DATATYPE_COMMENT = 2;
	public static final int LISTVIEW_COUNT = 10;
	public static final int MOMOKA_TEXT_FONTSSIZE = 13;
	public static final int LISTVIEW_EMOTION_SIZE = 20;
	public static Pattern emotionPattern = Pattern
			.compile("\\[([^\\]\\[\\/ ]+)\\]");
	public static Activity mContext;

	public static View.OnClickListener finish(Activity activity) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) mContext
						.getSystemService("input_method");
				if (mContext.getCurrentFocus() != null)
					imm.hideSoftInputFromWindow(mContext.getCurrentFocus()
							.getWindowToken(), 0);
				mContext.finish();
			}
		};
	}

	public static ProgressDialog showProgress(Context context,
			CharSequence title, CharSequence message) {
		try {
			if (context == null) {
				return null;
			}
			ProgressDialog dialog = new ProgressDialog(context);
			dialog.setTitle(title);
			dialog.setMessage(message);
			dialog.setCancelable(false);
			dialog.show();
			return dialog;
		} catch (Exception e) {
			Logger.i(e);
		}
		return null;
	}

	public static ProgressDialog showProgress(Context context,
			CharSequence title, CharSequence message, boolean cancelable) {
		try {
			if (context == null) {
				return null;
			}
			final ProgressDialog dialog = ProgressDialog.show(context, title, message);;
//			dialog.setTitle(title);
//			dialog.setMessage(message);
			dialog.setCancelable(cancelable);
			new CountDownTimer(10000L, 1000L) {
				public void onTick(long millisUntilFinished) {
				}

				public void onFinish() {
					try {
						dialog.dismiss();
					} catch (Exception localException) {
					}
				}
			}.start();
		//	dialog.show();
			return dialog;
		} catch (Exception e) {
		}
		return null;
	}

	public static ProgressDialog dismissProgress(ProgressDialog progressDialog) {
		try {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return progressDialog;
	}

	public static void ToastMessage(Context cont, int layout, int resid,
			int time) {
		try {
			View view = LayoutInflater.from(cont).inflate(layout, null);
			
			TextView tv = (TextView) view.findViewById(R.id.tv);
			tv.setText(resid);
			Toast toast = new Toast(cont);
			toast.setGravity(17, 0, 0);
			toast.setDuration(time);
			toast.setView(view);
			toast.show();
		} catch (Exception localException) {
		}
	}

	public static void ToastMessage(Context cont, int layout, String resid,
			int time) {
		try {
			View view = LayoutInflater.from(cont).inflate(layout, null);
			TextView tv = (TextView) view.findViewById(R.id.tv);
			tv.setText(resid);
			Toast toast = new Toast(cont);
			toast.setGravity(17, 0, 0);
			toast.setDuration(time);
			toast.setView(view);
			toast.show();
		} catch (Exception localException) {
		}
	}

	public static void ToastMessage(Context cont, int resid, int time) {
		try {
			View view = LayoutInflater.from(cont).inflate(
					R.layout.toastmessage_text, null);
			TextView tv = (TextView) view.findViewById(R.id.tv);
			tv.setText(resid);
			Toast toast = new Toast(cont);
			toast.setGravity(17, 0, 0);
			toast.setDuration(time);
			toast.setView(view);
			toast.show();
		} catch (Exception localException) {
		}
	}

	public static void ToastMessage(Context cont, String resid, int time) {
		try {
			View view = LayoutInflater.from(cont).inflate(
					R.layout.toastmessage_text, null);
			TextView tv = (TextView) view.findViewById(R.id.tv);
			tv.setText(resid);
			Toast toast = new Toast(cont);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setDuration(time);
			toast.setView(view);
			toast.show();
		} catch (Exception localException) {
		}
	}
	
	
	

	public static void showClearWordsDialog(Context cont, final EditText editer,
			final TextView numwords) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setTitle(R.string.clearwords);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						editer.setText("");
						numwords.setText("0");
					}
				});
		builder.setNegativeButton(R.string.cancle,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	public static SpannableStringBuilder parseFaceByText(Context context,
			String content, JSONObject emotionJson) {
		SpannableStringBuilder builder = new SpannableStringBuilder(content);
		Matcher matcher = emotionPattern.matcher(content);
		try {
			while (matcher.find()) {
				String ma = matcher.group(0);
				String imageurl = emotionJson.getString(ma);
				URL url = new URL(imageurl);
				Drawable d = Drawable.createFromStream(url.openStream(),
						FileUtils.getFileName(imageurl));

				int max = ImageUtils.dip2px(context, 20.0F);
				d.setBounds(0, 0, max, max);
				ImageSpan span = new ImageSpan(d, 1);
				builder.setSpan(span, matcher.start(), matcher.end(), 33);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder;
	}

	public static void Exit(Activity cont) {
		AlertDialog.Builder builder = new AlertDialog.Builder(cont);
		builder.setIcon(17301659);
		builder.setTitle("确定退出");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				mContext.finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

	public static void clearAppCache(Activity activity) {
		final AppContext ac = (AppContext) activity.getApplication();
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1)
					UIHelper.ToastMessage(mContext, "缓存清除成功", 0);
				else
					UIHelper.ToastMessage(mContext, "缓存清除失败", 0);
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					ac.clearAppCache();
					msg.what = 1;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
}
