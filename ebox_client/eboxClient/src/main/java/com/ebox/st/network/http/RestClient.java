package com.ebox.st.network.http;

import android.content.Context;

import com.ebox.pub.utils.LogUtil;
import com.ebox.st.network.config.CommonValue;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * wechat
 *
 * @author donal
 *
 */
public class RestClient {
	

	  private  AsyncHttpClient client = new AsyncHttpClient();
	  
	  private  SyncHttpClient  clientSync = new SyncHttpClient();
	  
	  private static RestClient restClient;
	  
	  private  PersistentCookieStore myCookieStore;
	  
	  public static RestClient getIntance(Context context) {
		  if(restClient==null){
			  restClient = new RestClient();
			  if(restClient.myCookieStore==null && context!=null){
				  restClient.myCookieStore = new PersistentCookieStore(context);  
				  restClient.client.setCookieStore(restClient.myCookieStore);  
				  restClient.clientSync.setCookieStore(restClient.myCookieStore); 
			  }
		  }
		  
		  return restClient;
	  }
	  


	  public  void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public  void getWeb(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(context, url, params, responseHandler);
	  }

	  public  void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(context, getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public  void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  public  void post(RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(getAbsoluteUrl(""), params, responseHandler);
	  }
	  
	  public  void upload(RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(getAbsoluteUploadUrl(""), params, responseHandler);
	  }
	  
	  public  void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.post(context, getAbsoluteUrl(url), params, responseHandler);
	  }
	  
	  
	  public  void getSync(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  clientSync.get(getAbsoluteUrlSync(url), params, responseHandler);
	  }
	  
	  public  void getWebSync(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  clientSync.get(context, url, params, responseHandler);
	  }

	  public  void getSync(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  clientSync.get(context, getAbsoluteUrlSync(url), params, responseHandler);
	  }
	  
	  public  void postSync(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  clientSync.post(getAbsoluteUrlSync(url), params, responseHandler);
	  }
	  
	  public  void postSync(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		  clientSync.post(context, getAbsoluteUrlSync(url), params, responseHandler);
	  }

	  private  String getAbsoluteUrl(String relativeUrl) {
		  client.setTimeout(5*1000);
		  client.setMaxConnections(5);
		  LogUtil.d(CommonValue.BASE_API + relativeUrl);
	      return CommonValue.BASE_API + relativeUrl;
	  }
	  
	  private  String getAbsoluteUploadUrl(String relativeUrl) {
		  client.setTimeout(30*1000);
		  client.setMaxConnections(5);
		  LogUtil.d( CommonValue.UPLOAD_API + relativeUrl);
	      return CommonValue.UPLOAD_API + relativeUrl;
	  }
	  
	  private  String getAbsoluteUrlSync(String relativeUrl) {
		  clientSync.setTimeout(30*1000);
		  clientSync.setMaxConnections(5);
	      return CommonValue.BASE_API + relativeUrl;
	  }

	  
	public  void clearCookieStore() {
		 if(myCookieStore!=null){
			 myCookieStore.clear();
		  }
	} 
}
