package com.moge.ebox.phone.ui.update;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.ui.BaseActivity;
import com.moge.ebox.phone.utils.LogUtil;
import com.moge.ebox.phone.utils.PackageUtil;

public class VersionUpdateCache {
	private  String APK_VERSION = "";
	private Activity activity;
	private EboxApplication mApplication;
	private UpdateResultListener mListener;
	private HttpHandler handler;

	public interface UpdateResultListener {
		public void onNotUpdate();
		public void onNextUpdate();
		public void onQuit();
	}

	public VersionUpdateCache(BaseActivity activity,
			UpdateResultListener listener) {
		this.activity = activity;
		mApplication = EboxApplication.getInstance();
		mListener = listener;
	}

	public void checkNewVersion(String clientVersion,int importantLevel,String downloadUrl,String updateContent) {
		
		String versionName = PackageUtil.getVersionName(mApplication);
		String new_version = clientVersion.substring(1,clientVersion.length());
	
		APK_VERSION=new_version;
		
		if(!versionName.equalsIgnoreCase(new_version))
		{
			if(importantLevel==3)
			{
				displayDialog2(updateContent,clientVersion,downloadUrl);
			}else{
				displayDialog(updateContent,clientVersion,downloadUrl);
			}
		}else {
			if (mListener!=null) {
				mListener.onNotUpdate();
			}
		}
		
	}
	


	public void displayDialog(String updescText, String versionName,final String url) {
		AlertDialog.Builder builer = new Builder(activity);
		builer.setTitle("发现新版本:" + versionName);
		builer.setMessage(updescText);
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				download(url);
			}
		});
		builer.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// notify other listener
				if (mListener != null) {
					mListener.onNextUpdate();
				}
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
	
	public void displayDialog2(String updescText, String versionName,final String url) {
		AlertDialog.Builder builer = new Builder(activity);
		builer.setTitle("发现新版本:" + versionName);
		builer.setMessage(updescText);
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				download(url);
			}
		});
		builer.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				if (mListener != null) {
					mListener.onQuit();
				}
				dialog.dismiss();
				activity.finish();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}


	
	protected void download(String url) {
			final ProgressDialog pro = new ProgressDialog(this.activity);
			//下载apk
			HttpUtils http = new HttpUtils();
			handler = http
					.download(
							url,
							getFile().getAbsolutePath(),
							true, 
							false,
							new RequestCallBack<File>() {

								/*
								 * (non-Javadoc)
								 * 
								 * @see
								 * com.lidroid.xutils.http.callback.RequestCallBack
								 * #onLoading(long, long, boolean)
								 */
								@Override
								public void onLoading(long total, long current,
										boolean isUploading) {
									// TODO Auto-generated method stub
									super.onLoading(total, current, isUploading);
									//总共大小
									LogUtils.i("下载中：" + total);
									//已经下载大小
									LogUtils.i("下载中：" + current);
									//控制进度条
									pro.setProgress((int)((double)current/(double)total*100));

								}

								/*
								 * (non-Javadoc)
								 * 
								 * @see
								 * com.lidroid.xutils.http.callback.RequestCallBack
								 * #onStart()
								 */
								@Override
								public void onStart() {
									// TODO Auto-generated method stub
									LogUtils.i("开始下载");
//									设置进度条
									pro.setTitle("正在下载...");
									pro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
									pro.setProgress(100);
									pro.setMessage("开始下载");
									pro.setButton("取消下载", new DialogInterface.OnClickListener() {
							             public void onClick(DialogInterface dialog, int i)
							             {
							                 //点击“确定按钮”取消对话框
							            	 LogUtils.i("取消下载");
							                 dialog.cancel();
							                 handler.cancel();
							                 activity.finish();
							             }
							         });
									pro.show();
								}

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									LogUtils.i("下载失败：" + arg1);
								}

								@Override
								public void onSuccess(ResponseInfo<File> arg0) {
									// TODO Auto-generated method stub
									LogUtils.i("下载成功：" + arg0);
									pro.dismiss();
									install(activity);
								}
							});


		}


 
	private File getFile() {
		File file = null;
		boolean isHaveSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (isHaveSDCard) {
			file = new File(Environment.getExternalStorageDirectory() + "/"
					+ getAPK_NAME());
		} else {
			file = new File(mApplication.getFilesDir() + "/" + getAPK_NAME());
		}
		LogUtil.i("main","apk_path:"+file.getAbsolutePath());
		return file;
	}
	
	private String getAPK_NAME(){
		return "eboxphone_"+APK_VERSION+".apk";
	}
	

	private void install(Context context) {
		String[] args = { "chmod", "604", getFile().getAbsolutePath() };
		exec(args);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(getFile()),
				"application/vnd.android.package-archive");
		//application/vnd.android.package-archive==.apk
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private static String exec(String[] args) {
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('\n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			LogUtil.logException(e);
		} catch (Exception e) {
			LogUtil.logException(e);
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				LogUtil.logException(e);
			}
			if (process != null) {
				process.destroy();
			}
		}
		LogUtil.e(result);
		return result;
	}

}
