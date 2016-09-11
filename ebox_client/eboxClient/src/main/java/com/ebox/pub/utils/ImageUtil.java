package com.ebox.pub.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import android.text.format.DateFormat;
import android.util.Log;

import com.ebox.AppApplication;
import com.ebox.pub.camera.OrderPicture;
import com.ebox.Anetwork.BaseHttpNetCfg;
import com.ebox.pub.service.NetworkCtrl;
import com.ebox.pub.service.global.GlobalField;

public class ImageUtil {
	private static final byte HEADER1[] = "\r\n------WebKitFormBoundaryfxWtza51lRI2LJUy\r\nContent-Disposition: form-data; name=\"file\"; filename=\"".getBytes();
    private static final byte HEADER2[] = "\"\r\nContent-Type: application/octet-stream\r\n\r\n".getBytes();
    private static final byte TAIL[] = "\r\n------WebKitFormBoundaryfxWtza51lRI2LJUy--".getBytes();
    
    
	public static boolean uploadImage(String url, File fc, String filename) {
		if (!NetworkCtrl.IsConnectedToNetwork(AppApplication.globalContext)) {
			return false;
		}
		HttpURLConnection httpConn = null;
		OutputStream os = null;
		int retries = 3;
		
		Log.d("Pic", "uploadImage "+url+" "+fc+" "+filename);
		
		while (retries > 0) {
			try {
				byte[] name = filename.getBytes("UTF-8");
				boolean s = fc.exists();
				if (!s) {
					Log.d("Pic", "fc not exists "+url+" "+fc+" "+filename);
					return false;
				}
				int size = (int) fc.length();
				byte[] content = new byte[size];
				InputStream is = new FileInputStream(fc);
				is.read(content);
				is.close();

				if (NetworkCtrl.isNeedProxy()) {
					int startIndex = url.indexOf("/") + 2;
					int endIndex = url.substring(startIndex).indexOf("/");
					String temp = url.substring(startIndex, endIndex + 7);
					String str1 = url.substring(endIndex + 7);
					URL url1 = new URL("http://" + GlobalField.hostUrl
							+ ":" + GlobalField.hostPort + str1);
					httpConn = (HttpURLConnection) url1.openConnection();
					httpConn.setRequestProperty("X-Online-Host", temp);
					
					/*  TODO 模拟器需要用此代码
					URL upUrl = new URL(url);
					Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
							GlobalField.hostUrl, GlobalField.hostPort));
					httpConn = (HttpURLConnection) upUrl.openConnection(proxy);*/
				} else {
					httpConn = (HttpURLConnection) new URL(url)
							.openConnection();
				}
				httpConn.setDoOutput(true);
				httpConn.setDoInput(true);
				// Setup HTTP Request to POST
				httpConn.setRequestMethod("POST");
				httpConn.setRequestProperty("Operators-Agent",
						"Profile/MIDP-1.0 Confirguration/CLDC-1.0");
				String length = String.valueOf(HEADER1.length + name.length
						+ HEADER2.length + content.length + TAIL.length);
				httpConn.setRequestProperty("Content-Length", length);
				httpConn
						.setRequestProperty("Content-Type",
								"multipart/form-data; boundary=----WebKitFormBoundaryfxWtza51lRI2LJUy");
				httpConn.setRequestProperty("Cookie", "terminal=\""+AppApplication.getInstance().getTerminal_code()+"\";"
						+"item=\""+OrderPicture.getItemId(filename)+"\";"
						+"order=\""+OrderPicture.getOrderId(filename)+"\";"
						+"date=\""+fc.lastModified()+"\"");
				httpConn.connect();
				os = httpConn.getOutputStream();
				os.write(HEADER1);
				os.write(name);
				os.write(HEADER2);
				os.write(content);
				os.write(TAIL);
				/*
				 * Caution: os.flush() is controversial. It may create
				 * unexpected behavior on certain mobile devices. Try it out for
				 * your mobile device
				 */
				// os.flush();
				// Read Response from the Server
				int responseCode = httpConn.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK ||
						responseCode ==  HttpURLConnection.HTTP_UNAUTHORIZED) {
					return true;
				} else {
					Log.d("Pic", "failed "+responseCode);
					return false;
				}
			} catch (Exception e) {
				Log.d("Pic", "Exception "+e.getMessage());
				retries--;
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (Exception ignore) {
					}
				}
				if (httpConn != null) {
					try {
						httpConn.disconnect();
					} catch (Exception ignore) {
					}
				}
			}
		}
		return false;
	}
	
	public static String getUploadImageUrl(Long id) {
		String date = DateFormat.format("yyyyMM", Calendar.getInstance())
				.toString();
		return BaseHttpNetCfg.imageUploadBaseURL + "/" + "order/"+date+ "/"
				+ id / 10000;
	}
}
