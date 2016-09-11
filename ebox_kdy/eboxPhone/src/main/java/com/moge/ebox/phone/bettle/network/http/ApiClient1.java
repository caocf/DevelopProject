package com.moge.ebox.phone.bettle.network.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.moge.ebox.phone.R;
import com.moge.ebox.phone.bettle.base.EboxApplication;
import com.moge.ebox.phone.bettle.tools.StringUtils;
import com.moge.ebox.phone.bettle.tools.UIHelper;
import com.moge.ebox.phone.bettle.utils.JsonSerializeUtil;
import com.moge.ebox.phone.utils.ToastUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import tools.Logger;

public class ApiClient1 {
	public final static String message_error = "服务器连接有问题";
	
	public  static int code = -400;

	private static void saveCache(EboxApplication appContext, String key,
			Serializable entity) {
		appContext.saveObject(entity, key);
	}

	public interface ClientCallback {
		abstract void onSuccess(JSONArray data, int code);

		abstract void onFailed(byte[] data, int code);
	}
	
	public interface UploadCallback 
	{
		abstract void onSuccess(String filePath, int fileId, int code);

		abstract void onFailed(byte[] data, int code);
	}

	private static ProgressDialog loadingPd;

	@SuppressWarnings("unchecked")
	public static void post(final EboxApplication appContext, String action,
			Map params, final ClientCallback clientCallback, boolean isShowLoad) {
		try {

			boolean connected = hasInternetConnected(appContext);
			if (!connected) {
				clientCallback.onFailed(null,-400);
				ToastUtil.showToastShort("网络开小差了,请检查网络连接");
				return;
			}

			if (isShowLoad) {
				loadingPd = UIHelper.showProgress(appContext, null,
						appContext.getResString(R.string.progress_message),
						true);
			}

			String value = JsonSerializeUtil.map2Json(params);
			RequestParams params2 = new RequestParams();
			params2.put("SERVICENAME_PARAM", "OPERATOR_SERVICE");
			params2.put("ACTIV_PARAM", action);
			params2.put("PARAMES_PARAM", value);
			Logger.d("action:" + action + ",params:" + value + ",p2:" + params2.toString());
			RestClient restClient = RestClient.getIntance(appContext);
			restClient.post(params2, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						if (loadingPd != null && loadingPd.isShowing()) {
							UIHelper.dismissProgress(loadingPd);
						}

						String data = new String(responseBody);
						/*
						 * List<Cookie> cookies =
						 * RestClient.getCookieStore().getCookies(); for (int i
						 * = 0; i < cookies.size(); i++) {
						 * //这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
						 * Logger.d(cookies
						 * .get(i).getName()+"::"+cookies.get(i).getValue()); }
						 */
						Logger.d(data);

						JSONObject json = new JSONObject(new String(
								responseBody));
						// clientCallback.onSuccess(json);
						boolean success = json.getBoolean("success");
						if(json.has("code")){
							code = StringUtils.toInt(json.getString("code"),-400);
						}

						if (success) {
							clientCallback.onSuccess(json
									.getJSONArray("result"),code);
						} else {
							String message = json.getString("message");
							if (message != null && message.length() > 0) {
								UIHelper.ToastMessage(appContext, message,
										Toast.LENGTH_LONG);
								
							}
							clientCallback.onFailed(responseBody,code);
						}
					} catch (Exception e) {
						e.printStackTrace();
						//Logger.e(e);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {

					if (loadingPd != null && loadingPd.isShowing()) {
						UIHelper.dismissProgress(loadingPd);
					}

					try {
						JSONObject json = new JSONObject(new String(
								responseBody));
						String message = json.getString("message");
						if(json.has("code")){
							code = StringUtils.toInt(json.getString("code"),-400);
						}
						if (message != null && message.length() > 0) {
							UIHelper.ToastMessage(appContext, message,
									Toast.LENGTH_LONG);
						}
						clientCallback.onFailed(responseBody,code);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});
		} catch (Exception e) {
		}
	}

	public static void postPage(final EboxApplication appContext,
			String action, Map params, final ClientCallback clientCallback,
			boolean isShowLoad) {
		try {

			boolean connected = hasInternetConnected(appContext);
			if (!connected) {
				clientCallback.onFailed(null,-400);
				ToastUtil.showToastShort("网络开小差了,请检查网络连接");
				return;
			}
			String value = JsonSerializeUtil.map2Json(params);
			RequestParams params2 = new RequestParams();
			params2.put("SERVICENAME_PARAM", "OPERATOR_SERVICE");
			params2.put("ACTIV_PARAM", action);
			params2.put("PARAMES_PARAM", value);
			Log.i("main", "action="+action+",params=" + params.toString());

			RestClient restClient = RestClient.getIntance(appContext);
			restClient.post(params2, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						String data = new String(responseBody);
						Logger.d(data);
						JSONObject json = new JSONObject(data);
						JSONArray array = new JSONArray();
						array.put(json);
						if(json.has("code")){
							code = StringUtils.toInt(json.getString("code"),-400);
						}
						clientCallback.onSuccess(array,code);
					} catch (JSONException e) {
						e.printStackTrace();
						Logger.e(e);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {

					try {
						JSONObject json = new JSONObject(new String(
								responseBody));
						String message = json.getString("message");
						if(json.has("code")){
							code = StringUtils.toInt(json.getString("code"),-400);
						}
						if (message != null && message.length() > 0) {
							UIHelper.ToastMessage(appContext, message,
									Toast.LENGTH_SHORT);
						}
						clientCallback.onFailed(responseBody,code);
					} catch (JSONException e) {
						e.printStackTrace();
						Logger.e(e);
					}
					
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	public static void upload(final EboxApplication appContext,
			File file, final UploadCallback clientCallback) {
		try {

			boolean connected = hasInternetConnected(appContext);
			if (!connected) {
				clientCallback.onFailed(null,-400);
				//ToastUtil.showToastShort("网络开小差了,请检查网络连接");
				return;
			}

			loadingPd = UIHelper.showProgress(appContext, null,
						appContext.getResString(R.string.progress_message),
						true);
			
			RequestParams params2 = new RequestParams();
			params2.put("file", file);
			params2.put("kind", "face");

			RestClient restClient = RestClient.getIntance(appContext);
			restClient.upload(params2, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					try {
						UIHelper.dismissProgress(loadingPd);

						String data = new String(responseBody);
						Logger.d(data);

						JSONObject json = new JSONObject(new String(
								responseBody));
						boolean success = json.getBoolean("success");
						if(json.has("code")){
							code = StringUtils.toInt(json.getString("code"),-400);
						}
						if (success)
						{
							int fileId = json.getInt("id");
							String filePath = json.getString("filePath");
							clientCallback.onSuccess(filePath,fileId,code);
						}
						else {
							String message = json.getString("message");
							if (message != null && message.length() > 0) 
							{
								UIHelper.ToastMessage(appContext, message,
										Toast.LENGTH_SHORT);
							}
							clientCallback.onFailed(responseBody,code);
						}
					} catch (Exception e) {
						e.printStackTrace();
						//Logger.e(e);
					}
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {

						UIHelper.dismissProgress(loadingPd);

					try {
						JSONObject json = new JSONObject(new String(
								responseBody));
						String message = json.getString("message");
						if(json.has("code")){
							code = StringUtils.toInt(json.getString("code"),-400);
						}
						if (message != null && message.length() > 0) {
							UIHelper.ToastMessage(appContext, message,
									Toast.LENGTH_SHORT);
						}
						clientCallback.onFailed(responseBody,code);
					} catch (Exception e) {
						e.printStackTrace();
						//Logger.e(e);
					}
					
				}
			});
		} catch (Exception e) {
		}
	}
	
	
	/*
	 * 获取快递员信息
	 */
	public static void getOperatorInfor(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "getOperatorInfor";
		post(appContext, action, params, clientCallback, false);
	}
	
	/*
	 * 登录
	 */
	public static void login(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "login";
		post(appContext, action, params, clientCallback, false);
	}

	/*
	 * 注册
	 */
	public static void register(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "register";
		post(appContext, action, params, clientCallback, false);

	}
	/*
	 * 完善资料
	 */
	public static void replenishOperator(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "replenish";
		post(appContext, action, params, clientCallback, false);

	}

	/*
	 * 修改密码
	 */
	public static void retrievePassword(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "retrievePassword";
		post(appContext, action, params, clientCallback, true);
	}

	/*
	 * 查询格口状态
	 */
	public static void queryBoxState(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {

		String action = "queryBoxState";
		post(appContext, action, params, clientCallback, false);

	}

	public static void queryEmptyBox(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "queryEmptyTerminal";
		post(appContext, action, params, clientCallback, false);

	}

	/*
	 * 查询历史快递信息
	 */
	public static void queryHistoryExpress(EboxApplication appContext,
			Map params, ClientCallback clientCallback) {
		String action = "queryExpressOrderState";
		postPage(appContext, action, params, clientCallback, false);
	}

	/*
	 * 查询超时快递信息
	 */
	public static void queryTimeOutExpress(EboxApplication appContext,
			Map params, ClientCallback clientCallback) {
		String action = "queryTimeOutExpress";
		postPage(appContext, action, params, clientCallback, false);
	}

	/*
	 * 充值
	 */
	public static void getOperatorChargeInfor(EboxApplication appContext,
			Map params, ClientCallback clientCallback) {
		String action = "getOperatorChargeInfor";
		post(appContext, action, params, clientCallback, false);
	}

	/*
	 * 活动
	 */
	public static void getActive(EboxApplication appContext,
			Map params, ClientCallback clientCallback) {
		String action = "getActive";
		post(appContext, action, params, clientCallback, false);
	}

	
	/*
	 * 查询快递订单状态
	 */
	public static void queryExpressOrderState(EboxApplication appContext,
			Map params, ClientCallback clientCallback) {
		String action = "queryExpressOrderState";
		postPage(appContext, action, params, clientCallback, false);
	}

	/*
	 * 快递快递派件总数
	 */
	public static void queryExpressOrderCount(EboxApplication appContext,
			Map params, ClientCallback clientCallback) {
		String action = "queryExpressOrderCount";
		post(appContext, action, params, clientCallback, false);
	}

	public static void getVerifySms(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "getVerifySms";
		post(appContext, action, params, clientCallback, false);
	}
	
	public static void getVerifySmsForRetrievePassword(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "getVerifySmsForretrievePassword";
		post(appContext, action, params, clientCallback, false);
	}
	

	public static void getVersion(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "getVersion";
		post(appContext, action, params, clientCallback, false);
	}
	
	public static void uploadFace(EboxApplication appContext,File file,
			UploadCallback clientCallback) {
		upload(appContext, file, clientCallback);
	}
	
	public static void getOrgnization(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "getOrgnization";
		post(appContext, action, params, clientCallback, false);
	}
	
	public static void getRegion(EboxApplication appContext, Map params,
			ClientCallback clientCallback) {
		String action = "getRegion";
		post(appContext, action, params, clientCallback, false);
	}
	
	
	public static boolean hasInternetConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		if (manager != null) {
			NetworkInfo network = manager.getActiveNetworkInfo();
			if (network != null && network.isConnectedOrConnecting()) {
				return true;
			}
		}
		return false;
	}
}
