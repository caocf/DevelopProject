package com.ebox.pub.service.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ebox.AppApplication;
import com.ebox.mall.warehouse.network.WHNetworkConfig;
import com.ebox.Anetwork.RequestManager;
import com.ebox.pub.service.global.GlobalField;
import com.ebox.pub.utils.LogUtil;
import com.ebox.pub.websocket.helper.WebSocketManager;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.TimerTask;

/**
 * 
 * @author Administrator 检测二维码是否需要更新
 */
public class CheckQrTask extends TimerTask {

	private TaskData data;
	private static final String TAG = "CheckQrTask";

	private static final String SCAN_QRCODE_URL = WHNetworkConfig.generalAddress+"/v1/delivery/terminal/box/open/qrcode?terminal_code="; // 获取二维码的地址
	private static final String SP_SCAN_NAME = "scan_module";
	private static final String SP_DELAYMILLIS = "delaymillis";
	private Context context;
	private String terminal_code;

	private static final int QR_WIDTH = 220;
	private static final int QR_HEIGHT = 220;

	private static final String QR_IMAGE_NAME = "/qrcode.jpg";

	private static final int HANDLER_MSG = 1;

	private RequestQueue mQueue;

	public CheckQrTask(TaskData data) {
		this.data = data;
		terminal_code = AppApplication.getInstance().getTerminal_code();
		context = AppApplication.globalContext;
		mQueue= RequestManager.getRequestQueue();
	}

	@Override
	public void run() {
		data.setLastRunning(System.currentTimeMillis());
		//再加上是否开启了扫码开柜功能
		if (getDelayMillis() == 0
				|| getDelayMillis() <= System.currentTimeMillis()) {
			// 过期则获得新的数据
			if (GlobalField.serverConfig.isScanPickup() == 1 && WebSocketManager.instance().isConnected()){
				getQRCodeByJsonVolley();
			}

		}

	}

	private void getQRCodeByJsonVolley() {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				SCAN_QRCODE_URL + terminal_code, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						// 根据过期时间 判断是否需要从网络获得图片
						try {
                            int status = jsonObject.getInt("status");
                            String msg = jsonObject.getString("msg");
                            if(status == 0)
                            {
                                JSONObject dataJO = jsonObject
                                        .getJSONObject("data");
                                String qrImageUrl = dataJO.getString("url");
                                int expried = dataJO.getInt("expried"); // 下次请求图片的时间
                                long delayMillis = (System.currentTimeMillis() + expried * 1000) - 10 * 1000;
                                // 保存过期时间
                                saveDelayMillis(delayMillis);

                                getQRImageByVolley(qrImageUrl);
                            }
                            else
                            {
                                LogUtil.d(GlobalField.tag,msg);
                            }

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		RequestManager.addRequest(jsonObjectRequest, "checkqrtask");

	}

	// 使用volley获得网络的二维码图片
	public void getQRImageByVolley(String qrImageUrl) {
		Log.i(TAG, qrImageUrl);
		ImageRequest imageRequest = new ImageRequest(qrImageUrl,
				new Response.Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap bitmap) {
						Log.i(TAG,
								"-------->>>>>>>Image OK" + bitmap.toString());
						// 二维码获得成功 向文件中存储图片
						storeQrCode(bitmap);

					}

				}, QR_WIDTH, QR_HEIGHT, Config.ARGB_8888,
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError volleyError) {
						// 二维码获的失败 没有图片存储
						// 直接跳转到原来取件模块
						Log.i(TAG, volleyError.getMessage());
					}
				});

		RequestManager.addRequest(imageRequest, "checkqrtask");
	}

	private void storeQrCode(Bitmap bitmap) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 如果sd卡存在
			File file = new File(Environment.getExternalStorageDirectory()
					+ QR_IMAGE_NAME);
			if (file.exists()) {
				file.delete();
			}
			Log.i(TAG,
					"------>////////////////---check--"
							+ Environment.getExternalStorageDirectory());
			try {
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(CompressFormat.JPEG, 50, fos);
				fos.flush();
				fos.close();
				// handler.sendEmptyMessageDelayed(HANDLER_MSG, delayMillis);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void saveDelayMillis(long delaymillis) {
		SharedPreferences sp = context.getSharedPreferences(SP_SCAN_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putLong(SP_DELAYMILLIS, delaymillis);
		editor.commit();
	}

	public long getDelayMillis() {
		SharedPreferences sp = context.getSharedPreferences(SP_SCAN_NAME,
				Context.MODE_PRIVATE);
		return sp.getLong(SP_DELAYMILLIS, 0);
	}

}
